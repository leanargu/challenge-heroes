package com.leandro.heroesapi.controller;

import com.leandro.heroesapi.repository.HeroeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class HeroeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HeroeRepository heroeRepository;

    @Test
    void getAllHeroes_withHeroes_returnOkWithHeroes() throws Exception {
        mockMvc.perform(get("/api/v1/heroe/all"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        "[{\"id\":1,\"name\":\"Hulk\"}," +
                                "{\"id\":2,\"name\":\"Superman\"}," +
                                "{\"id\":3,\"name\":\"Batman\"}]"
                ));
    }

    @Test
    void getAllHeroes_withoutHeroes_returnNotFound() throws Exception {
        heroeRepository.deleteAll();

        mockMvc.perform(get("/api/v1/heroe/all"))
                .andExpect(status().isNotFound());
    }

}
