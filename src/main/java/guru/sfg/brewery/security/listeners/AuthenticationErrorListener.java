package guru.sfg.brewery.security.listeners;

import guru.sfg.brewery.domain.security.LoginFailure;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.LoginFailureRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthenticationErrorListener {

    private final LoginFailureRepository loginFailureRepository;
    private final UserRepository userRepository;

    @EventListener
    public void listen(AuthenticationFailureBadCredentialsEvent event) {
        LoginFailure.LoginFailureBuilder builder = LoginFailure.builder();
        log.debug("Bad Credentials event");

        if (event.getSource() instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) event.getSource();

            if (token.getPrincipal() instanceof String) {
                builder.username(token.getPrincipal().toString());
                log.debug("Bad username: " + token.getPrincipal());
                Optional<User> attemptedUser = userRepository.findByUsername(token.getPrincipal().toString());
                attemptedUser.ifPresent(builder::user);
            }

            if (token.getDetails() instanceof WebAuthenticationDetails) {
                WebAuthenticationDetails details = (WebAuthenticationDetails) token.getDetails();
                builder.sourceIp(details.getRemoteAddress());

                log.debug("Remote IP: " + details.getRemoteAddress());
            }
        }
        LoginFailure loginFailure = loginFailureRepository.save(builder.build());
        log.debug("Login failure saved. ID: " + loginFailure.getId());
        if(loginFailure.getUser() != null) {
            lockUserAccount(loginFailure.getUser());
        }
    }

    private void lockUserAccount(User user) {
        List<LoginFailure> failures = loginFailureRepository.findAllByUserAndCreatedDateIsAfter(user,
                Timestamp.valueOf(LocalDateTime.now().minusDays(1)));
        if(failures.size() >= 3) {
            log.debug("Locking user account...");
            user.setAccountNonLocked(false);
            userRepository.save(user);
        }
    }

}
