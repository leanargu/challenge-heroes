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
class HeroeServiceCacheTest {

    @Mock
    private HeroeRepository heroeRepository;
    @Autowired
    @InjectMocks
    private HeroeServiceImpl underTest;

    @AfterEach
    void tearDown() {
        underTest.invalidateCache();
    }

    @Test
    void getAllHeroes_withHeroesAndCache_returnAListOfHeroesCallingRepositoryOnlyOnce() {
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
        var result = underTest.getAllHeroes();

        //then
        assertThat(result)
                .asList()
                .isNotEmpty();

        verify(heroeRepository, times(1)).findAll();
    }

    @Test
    void findHeroeById_withTwoHeroesAndCache_returnHeroesCallingRepositoryOnlyOnce() {
        //given
        var firstHeroeName = "Flash";
        var firstHeroeId = 1l;
        var secondHeroeName = "Hulk";
        var secondHeroeId = 2l;

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
        var resultFirstParameter = underTest.findHeroeById(firstHeroeId);

        underTest.findHeroeById(secondHeroeId);
        underTest.findHeroeById(secondHeroeId);
        var resultSecondParameter = underTest.findHeroeById(secondHeroeId);


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

    @Test
    void getHeroesThatNameCointains_withTwoHeroesAndCache_returnHeroesCallingRepositoryOnlyOnce() {
        //given
        var firstHeroeName = "Flash";
        var firstParameter = "sh";
        var secondHeroeName = "Hulk";
        var secondParameter = "lk";

        given(heroeRepository.findByNameContainingIgnoreCase(firstParameter))
                .willReturn(List.of(
                        new Heroe(firstHeroeName)
                ));
        given(heroeRepository.findByNameContainingIgnoreCase(secondParameter))
                .willReturn(List.of(
                        new Heroe(secondHeroeName)
                ));

        //when
        underTest.getHeroesThatNameCointains(firstParameter);
        underTest.getHeroesThatNameCointains(firstParameter);
        var resultFirstParameter = underTest.getHeroesThatNameCointains(firstParameter);

        underTest.getHeroesThatNameCointains(secondParameter);
        underTest.getHeroesThatNameCointains(secondParameter);
        var resultSecondParameter = underTest.getHeroesThatNameCointains(secondParameter);


        //then
        assertThat(resultFirstParameter)
                .asList()
                .hasSize(1);

        assertThat(resultSecondParameter)
                .asList()
                .hasSize(1);

        verify(heroeRepository, times(1)).findByNameContainingIgnoreCase(firstParameter);
        verify(heroeRepository, times(1)).findByNameContainingIgnoreCase(secondParameter);
    }

    @Test
    void updateHeroe_withCachedHeroe_returnUpdatedHeroe() {
        //given
        var heroeToFindId = 1l;
        var heroeToUpdate = new Heroe("Hulk");
        given(heroeRepository.findById(heroeToFindId))
                .willReturn(Optional.of(heroeToUpdate));
        given(heroeRepository.findAll())
                .willReturn(List.of(heroeToUpdate));

        /* First call to findById method */
        heroeRepository.findById(heroeToFindId);
        /* First call to findAll method */
        underTest.getAllHeroes();

        //when
        var newHeroeName = "Incredible Hulk";
        /* Second call to findById method inside updateHeroe method */
        underTest.updateHeroe(heroeToFindId, newHeroeName);

        /* Third call to findById method (because cache was invalidated on update) */
        underTest.findHeroeById(heroeToFindId);
        /* Second call to findAll method (because cache was invalidated on update) */
        underTest.getAllHeroes();

        /*Cached request*/
        underTest.getAllHeroes();
        underTest.getAllHeroes();
        underTest.findHeroeById(heroeToFindId);
        underTest.findHeroeById(heroeToFindId);

        //then
        verify(heroeRepository, times(3)).findById(heroeToFindId);
        verify(heroeRepository, times(2)).findAll();
    }

    @Test
    void deleteHeroe_withCachedHeroe_invalidateCache() {
        //given
        var heroeIdToDelete = 1l;
        var heroeName = "Hulk";
        given(heroeRepository.findById(heroeIdToDelete))
                .willReturn(Optional.of(new Heroe(heroeName)));
        given(heroeRepository.findAll())
                .willReturn(List.of(new Heroe(heroeName)));

        /* First call to findById method */
        underTest.findHeroeById(heroeIdToDelete);
        /* First call to findAll method */
        underTest.getAllHeroes();

        //when
        /* Second call to findById method inside deleteHeroe method */
        underTest.deleteHeroe(heroeIdToDelete);

        /* Third call to findById method (because cache was invalidated on delete) */
        underTest.findHeroeById(heroeIdToDelete);

        /* Second call to findAll method (because cache was invalidated on delete) */
        underTest.getAllHeroes();

        /*Cached request*/
        underTest.getAllHeroes();
        underTest.getAllHeroes();
        underTest.findHeroeById(heroeIdToDelete);
        underTest.findHeroeById(heroeIdToDelete);

        verify(heroeRepository, times(3)).findById(heroeIdToDelete);
        verify(heroeRepository, times(2)).findAll();
    }

}
