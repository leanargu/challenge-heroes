package com.leandro.heroesapi.service;

import com.leandro.heroesapi.exception.HeroesNotFoundException;
import com.leandro.heroesapi.model.Heroe;
import com.leandro.heroesapi.repository.HeroeRepository;
import com.leandro.heroesapi.service.impl.HeroeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

class HeroeServiceTest {

    private HeroeService underTest;
    @Mock
    private HeroeRepository heroeRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        underTest = new HeroeServiceImpl(heroeRepository);
    }

    @Test
    void getAllHeroes_withHeroes_returnAListOfHeroes() {
        //given
        given(heroeRepository.findAll())
                .willReturn(List.of(
                        new Heroe("Superman"),
                        new Heroe("Flash")
                ));

        //when
        var result = underTest.getAllHeroes();

        //then
        assertThat(result)
                .asList()
                .isNotEmpty();

    }

    @Test
    void getAllHeroes_withoutHeroes_throwsHeroesNotFoundException() {
        //given
        given(heroeRepository.findAll())
                .willReturn(List.of());

        //then
        assertThatExceptionOfType(HeroesNotFoundException.class)
                .isThrownBy(() -> {
                    underTest.getAllHeroes();
                })
                .withMessage("No heroes found.");
    }

    @Test
    void getHeroeById_withValidIdAndHeroe_returnHeroe() {
        //given
        var heroeName = "Green Arrow";

        given(heroeRepository.findById(Mockito.anyLong()))
                .willReturn(Optional.of(new Heroe(heroeName)));
        //when
        var result = underTest.findHeroeById(1L);

        //then
        assertThat(result)
                .isNotNull()
                .extracting("name")
                .isEqualTo(heroeName);
    }

    @Test
    void getHeroeById_withoutMatchingHeroes_throwsHeroesNotFoundException() {
        //given
        var nonExistentUserId = 1L;
        given(heroeRepository.findById(nonExistentUserId))
                .willReturn(Optional.empty());

        //then
        assertThatExceptionOfType(HeroesNotFoundException.class)
                .isThrownBy(() -> {
                    underTest.findHeroeById(nonExistentUserId);
                })
                .withMessage(String.format("Heroe with id %d does not exists.",nonExistentUserId));
    }

    @Test
    void getHeroeById_withNullId_throwsIllegalArgumentException() {
        //then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    underTest.findHeroeById(null);
                })
                .withMessage("Id cannot be null.");
    }

    @Test
    void getHeroesThatNameCointains_withMatchingHeroes_returnHeroesList() {
        //given
        given(heroeRepository.findByNameContainingIgnoreCase(Mockito.anyString()))
                .willReturn(List.of(new Heroe("Iron Man")));

        //when
        var result = underTest.getHeroesThatNameCointains("man");

        //then
        assertThat(result)
                .asList()
                .isNotEmpty();
    }

    @Test
    void getHeroesThatNameCointains_withoutMatchingHeroes_throwsHeroesNotFoundException() {
        //given
        given(heroeRepository.findByNameContainingIgnoreCase(Mockito.anyString()))
                .willReturn(List.of());

        //then
        assertThatExceptionOfType(HeroesNotFoundException.class)
                .isThrownBy(() -> {
                    underTest.getHeroesThatNameCointains("man");
                })
                .withMessage("No heroes found.");
    }

    @Test
    void getHeroesThatNameCointains_withNullNameToFind_throwsIllegalArgumentException() {
        //then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    underTest.getHeroesThatNameCointains(null);
                })
                .withMessage("Name cannot be null or empty.");
    }

    @Test
    void getHeroesThatNameCointains_withEmptyNameToFind_throwsIllegalArgumentException() {
        //then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    underTest.getHeroesThatNameCointains("");
                })
                .withMessage("Name cannot be null or empty.");
    }

    @Test
    void updateHeroe_withValidHeroeInfoAndExistingUser_notThrowsException() {
        //given
        var existentHeroeId = 1l;
        var heroeToSave = new Heroe("Ant Man");

        given(heroeRepository.findById(existentHeroeId))
                .willReturn(Optional.of(heroeToSave));

        //given
        var newName = "Wakanda";
        underTest.updateHeroe(existentHeroeId,newName);

        //then
        verify(heroeRepository).findById(existentHeroeId);
        verify(heroeRepository).save(heroeToSave);
    }

    @Test
    void updateHeroe_withNullHeroeNameToUpdate_throwsIllegalArgumentException() {
        //given
        var nonExistentUserId = 1l;
        given(heroeRepository.findById(nonExistentUserId))
                .willReturn(Optional.empty());

        //then
        String newName = null;
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    underTest.updateHeroe(nonExistentUserId,newName);;
                })
                .withMessage("Name cannot be null or empty.");
    }

    @Test
    void deleteHeroe_withSavedHeroe_notThrowsException() {
        //given
        var existentHeroeId = 1l;
        given(heroeRepository.findById(existentHeroeId))
                .willReturn(Optional.of(new Heroe("Superman")));

        underTest.deleteHeroe(existentHeroeId);

        //then
        verify(heroeRepository).findById(existentHeroeId);
        verify(heroeRepository).delete(Mockito.any());
    }

}
