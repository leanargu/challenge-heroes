package com.leandro.heroesapi.controller;

import com.leandro.heroesapi.model.Heroe;
import com.leandro.heroesapi.repository.HeroeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Test
    void getHeroeById_withHeroe_returnOkWithHeroes() throws Exception {
        Long idToFind = 1l;

        mockMvc.perform(get(String.format("/api/v1/heroe/%d", idToFind)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"name\":\"Hulk\"}"));
    }

    @Test
    void getHeroeById_withoutHeroe_returnNotFound() throws Exception {
        heroeRepository.deleteAll();
        Long idToFind = 1l;

        mockMvc.perform(get(String.format("/api/v1/heroe/%d", idToFind)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getHeroeById_withNullHeroeId_returnBadRequest() throws Exception {
        Long idToFind = null;
        mockMvc.perform(get(String.format("/api/v1/heroe/%d", idToFind)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getHeroesThatNameCointains_withtHeroes_returnOkWithHeroes() throws Exception {
        String paramContaining = "man";

        mockMvc.perform(get(String.format("/api/v1/heroe/all?containing=%s", paramContaining)))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        "[{\"id\":2,\"name\":\"Superman\"}," +
                                "{\"id\":3,\"name\":\"Batman\"}]"));
    }

    @Test
    void updateHeroe_withHeroe_updateHeroe() throws Exception {
        Long idToModify = 1l;
        String newName = "Incredible Hulk";

        mockMvc.perform(put(String.format("/api/v1/heroe/%d?name=%s",idToModify,newName)))
                .andExpect(status().isOk());

        Heroe expectedModifiedHeroe = heroeRepository.findById(idToModify).get();

        assertThat(expectedModifiedHeroe)
                .isNotNull()
                .extracting("name")
                .isEqualTo(newName);
    }

    @Test
    void updateHeroe_withoutHeroe_returnNotFound() throws Exception {
        Long unexistentHeroeIdToModify = 9l;
        String newName = "Incredible Hulk";

        mockMvc.perform(put(String.format("/api/v1/heroe/%d?name=%s",unexistentHeroeIdToModify,newName)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteHeroe_withHeroe_deleteHeroe() throws Exception{
        Long idTodelete = 1l;

        mockMvc.perform(delete(String.format("/api/v1/heroe/%d",idTodelete)))
                .andExpect(status().isOk());

        Optional<Heroe> result = heroeRepository.findById(idTodelete);

        assertThat(result.isEmpty());

    }

}
