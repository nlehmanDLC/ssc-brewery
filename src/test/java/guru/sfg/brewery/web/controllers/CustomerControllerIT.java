package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class CustomerControllerIT extends BaseIT {

    @ParameterizedTest(name = "#{index} with [{arguments}]")
    @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamAdminCustomer")
    void testListCustomer(String user, String password) throws Exception {
        mockMvc.perform(get("/customers")
                .with(httpBasic(user, password)))
                .andExpect(status().isOk());
    }

    @Test
    void testListCustomersNOAUTH() throws Exception {
        mockMvc.perform(get("/customers").with(httpBasic("user", "password")))
                .andExpect(status().isForbidden());
    }

    @Test
    void testListCustomersNOTLOGGEDIN() throws Exception {
        mockMvc.perform(get("/customers"))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Add Customers")
    @Nested
    class AddCustomers {
        @Rollback
        @Test
        void processCreationForm() throws Exception {
            mockMvc.perform(post("/customers/new").with(csrf())
                    .param("customerName", "Foo customer")
                    .with(httpBasic("spring", "guru")))
                    .andExpect(status().is3xxRedirection());
        }

        @Rollback
        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamNotAdmin")
        void processCreationFormNOTAUTH(String user, String pwd) throws Exception {
            mockMvc.perform(post("/customers/new").with(csrf())
                    .param("customerName", "Foo customer")
                    .with(httpBasic(user, pwd)))
                    .andExpect(status().isForbidden());
        }

        @Test
        void processCreationFormNOAUTH() throws Exception {
            mockMvc.perform(post("/customers/new").with(csrf())
                    .param("customerName", "Foo customer"))
                    .andExpect(status().isUnauthorized());
        }


    }
}
