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

}
