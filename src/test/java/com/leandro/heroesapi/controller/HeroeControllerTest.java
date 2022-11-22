package com.leandro.heroesapi.controller;

import com.leandro.heroesapi.model.Heroe;
import com.leandro.heroesapi.repository.HeroeRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
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

    public final String API_V_1_HEROE_URL = "/api/v1/heroe";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private HeroeRepository heroeRepository;
    private StringBuilder baseUrlBuilder = new StringBuilder(API_V_1_HEROE_URL);

    @Test
    void getAllHeroes_withoutAuth_returnUnauthorized() throws Exception {
        var url = baseUrlBuilder
                .append("/all")
                .toString();

        mockMvc.perform(get(url))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(value = "spring")
    @Test
    void getAllHeroes_withHeroes_returnOkWithHeroes() throws Exception {
        var url = baseUrlBuilder
                .append("/all")
                .toString();

        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        "[{\"id\":1,\"name\":\"Hulk\"}," +
                                "{\"id\":2,\"name\":\"Superman\"}," +
                                "{\"id\":3,\"name\":\"Batman\"}]"
                ));
    }

    @WithMockUser(value = "spring")
    @Test
    void getAllHeroes_withoutHeroes_returnNotFound() throws Exception {
        heroeRepository.deleteAll();

        var url = baseUrlBuilder
                .append("/all")
                .toString();

        mockMvc.perform(get(url))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(value = "spring")
    @Test
    void getHeroeById_withHeroe_returnOkWithHeroes() throws Exception {
        var url = baseUrlBuilder
                .append("/")
                .append(1L)
                .toString();

        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"name\":\"Hulk\"}"));
    }

    @WithMockUser(value = "spring")
    @Test
    void getHeroeById_withoutHeroe_returnNotFound() throws Exception {
        heroeRepository.deleteAll();
        var url = baseUrlBuilder
                .append("/")
                .append(1L)
                .toString();

        mockMvc.perform(get(url))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(value = "spring")
    @Test
    void getHeroeById_withNullHeroeId_returnBadRequest() throws Exception {
        Long idToFind = null;
        var url = baseUrlBuilder
                .append("/")
                .append(idToFind).toString();

        mockMvc.perform(get(url))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(value = "spring")
    @Test
    void getHeroesThatNameCointains_withtHeroes_returnOkWithHeroes() throws Exception {
        var paramContaining = "man";
        var url = baseUrlBuilder.append("/all?containing=")
                .append(paramContaining)
                .toString();

        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        "[{\"id\":2,\"name\":\"Superman\"}," +
                                "{\"id\":3,\"name\":\"Batman\"}]"));
    }

    @WithMockUser(value = "spring")
    @Test
    void updateHeroe_withHeroe_updateHeroe() throws Exception {
        var idToModify = 1L;
        var newName = "Incredible Hulk";
        var url = baseUrlBuilder
                .append("/")
                .append(idToModify)
                .append("?name=")
                .append(newName)
                .toString();

        mockMvc.perform(put(url))
                .andExpect(status().isOk());

        Optional<Heroe> expectedModifiedHeroe = heroeRepository.findById(idToModify);

        assertThat(expectedModifiedHeroe)
                .isPresent()
                .get()
                .isNotNull()
                .extracting("name")
                .isEqualTo(newName);
    }

    @WithMockUser(value = "spring")
    @Test
    void updateHeroe_withoutHeroe_returnNotFound() throws Exception {
        var unexistentHeroeIdToModify = 9L;
        var newName = "Incredible Hulk";
        var url = baseUrlBuilder.append("/")
                .append(unexistentHeroeIdToModify)
                .append("?name=")
                .append(newName)
                .toString();

        mockMvc.perform(put(url))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(value = "spring")
    @Test
    void deleteHeroe_withHeroe_deleteHeroe() throws Exception {
        var idTodelete = 1L;
        var url = baseUrlBuilder
                .append("/")
                .append(idTodelete)
                .toString();

        mockMvc.perform(delete(url))
                .andExpect(status().isOk());

        Optional<Heroe> result = heroeRepository.findById(idTodelete);

        assertThat(result)
                .isEmpty();

    }

}
