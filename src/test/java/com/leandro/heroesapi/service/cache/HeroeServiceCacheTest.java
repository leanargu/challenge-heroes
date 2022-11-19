package com.leandro.heroesapi.service.cache;

import com.leandro.heroesapi.model.Heroe;
import com.leandro.heroesapi.repository.HeroeRepository;
import com.leandro.heroesapi.service.impl.HeroeServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class HeroeServiceCacheTest {

    @Mock
    private HeroeRepository heroeRepository;
    @Autowired
    @InjectMocks
    private HeroeServiceImpl underTest;

    @Test
    public void getAllHeroes_withHeroesAndCache_returnAListOfHeroesCallingRepositoryOnlyOnce() {
        //given
        given(heroeRepository.findAll())
                .willReturn(List.of(
                        new Heroe("Superman"),
                        new Heroe("Flash")
                ));

        //when
        underTest.getAllHeroes();
        underTest.getAllHeroes();
        underTest.getAllHeroes();
        underTest.getAllHeroes();
        List<Heroe> result = underTest.getAllHeroes();

        //then
        assertThat(result)
                .asList()
                .isNotEmpty();

        verify(heroeRepository, times(1)).findAll();
    }

    @Test
    public void findHeroeById_withTwoHeroesAndCache_returnHeroesCallingRepositoryOnlyOnce() {
        //given
        String firstHeroeName = "Flash";
        Long firstHeroeId = 1l;
        String secondHeroeName = "Hulk";
        Long secondHeroeId = 2l;

        given(heroeRepository.findById(firstHeroeId))
                .willReturn(Optional.of(
                        new Heroe(firstHeroeName)
                ));
        given(heroeRepository.findById(secondHeroeId))
                .willReturn(Optional.of(
                        new Heroe(secondHeroeName)
                ));

        //when
        underTest.findHeroeById(firstHeroeId);
        underTest.findHeroeById(firstHeroeId);
        Heroe resultFirstParameter = underTest.findHeroeById(firstHeroeId);

        underTest.findHeroeById(secondHeroeId);
        underTest.findHeroeById(secondHeroeId);
        Heroe resultSecondParameter = underTest.findHeroeById(secondHeroeId);


        //then
        assertThat(resultFirstParameter)
                .isNotNull()
                .extracting("name")
                .isEqualTo(firstHeroeName);

        assertThat(resultSecondParameter)
                .isNotNull()
                .extracting("name")
                .isEqualTo(secondHeroeName);

        verify(heroeRepository, times(1)).findById(firstHeroeId);
        verify(heroeRepository, times(1)).findById(secondHeroeId);
    }

}
