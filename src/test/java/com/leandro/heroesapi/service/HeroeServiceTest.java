package com.leandro.heroesapi.service;

import com.leandro.heroesapi.model.Heroe;
import com.leandro.heroesapi.repository.HeroeRepository;
import com.leandro.heroesapi.service.impl.HeroeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

public class HeroeServiceTest {

    private HeroeService underTest;
    @Mock
    private HeroeRepository heroeRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        underTest = new HeroeServiceImpl();
    }

    @Test
    public void getAllHeroes_withHeroes_returnAListOfHeroes() {
        //given
        given(heroeRepository.findAll())
                .willReturn(List.of(
                        new Heroe("Superman"),
                        new Heroe("Flash")
                ));

        //when
        List<Heroe> result = underTest.getAllHeroes();

        //then
        assertThat(result)
                .asList()
                .isNotEmpty();

    }

}
