package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by jt on 6/12/20.
 */
@WebMvcTest
public class BeerControllerIT extends BaseIT {
//    @WithMockUser("spring")
//    @Test
//    void findBeers() throws Exception{
//        mockMvc.perform(get("/beers/find"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("beers/findBeers"))
//                .andExpect(model().attributeExists("beer"));
//    }

//    @Test
//    void findBeersWithHTTPBasic() throws Exception{
//        mockMvc.perform(get("/beers/find").with(httpBasic("spring", "guru")))
//                .andExpect(status().isOk())
//                .andExpect(view().name("beers/findBeers"))
//                .andExpect(model().attributeExists("beer"));
//    }

    @Test
    void findBeers() throws Exception{
        mockMvc.perform(get("/beers/find"))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/findBeers"))
                .andExpect(model().attributeExists("beer"));
    }

}