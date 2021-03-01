package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class BeerRestControllerIT extends BaseIT {

    @Test
    void deleteBeerUrl() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/e756309a-bd6a-4d9d-842d-f14da9fa451f")
                .param("apiKey", "spring").param("apiSecret", "guru"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBeerBadCredentialsUrl() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/e756309a-bd6a-4d9d-842d-f14da9fa451f")
                .param("apiKey", "spring").param("apiSecret", "guruXXXX"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteBeerBadCredentials() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/e756309a-bd6a-4d9d-842d-f14da9fa451f")
                .header("Api-Key", "spring").header("Api-Secret", "guruXXXX"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteBeer() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/e756309a-bd6a-4d9d-842d-f14da9fa451f")
                .header("Api-Key", "spring").header("Api-Secret", "guru"))
                .andExpect(status().isOk());
    }

    @Test
    void findBeers() throws Exception {
        mockMvc.perform(get("/api/v1/beer/"))
                .andExpect(status().isOk());
    }

    @Test
    void findBeerById() throws Exception {
        mockMvc.perform(get("/api/v1/beer/e756309a-bd6a-4d9d-842d-f14da9fa451f"))
                .andExpect(status().isOk());
    }

    @Test
    void findBeerByUpc() throws Exception {
        mockMvc.perform(get("/api/v1/beerUpc/0631234300019"))
                .andExpect(status().isOk());
    }
}
