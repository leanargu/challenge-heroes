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

public class HeroeServiceTest {

    private HeroeService underTest;
    @Mock
    private HeroeRepository heroeRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        underTest = new HeroeServiceImpl(heroeRepository);
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

    @Test
    public void getAllHeroes_withoutHeroes_throwsHeroesNotFoundException() {
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
    public void getHeroeById_withValidIdAndHeroe_returnHeroe() {
        //given
        String heroeName = "Green Arrow";

        given(heroeRepository.findById(Mockito.anyLong()))
                .willReturn(Optional.of(new Heroe(heroeName)));
        //when
        Heroe result = underTest.findHeroeById(1l);

        //then
        assertThat(result)
                .isNotNull()
                .extracting("name")
                .isEqualTo(heroeName);
    }

    @Test
    public void getHeroeById_withoutMatchingHeroes_throwsHeroesNotFoundException() {
        //given
        long nonExistentUserId = 1l;
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
    public void getHeroeById_withNullId_throwsIllegalArgumentException() {
        //then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    underTest.findHeroeById(null);
                })
                .withMessage("Id cannot be null.");
    }

    @Test
    public void getHeroesThatNameCointains_withMatchingHeroes_returnHeroesList() {
        //given
        given(heroeRepository.findByNameContainingIgnoreCase(Mockito.anyString()))
                .willReturn(List.of(new Heroe("Iron Man")));

        //when
        List<Heroe> result = underTest.getHeroesThatNameCointains("man");

        //then
        assertThat(result)
                .asList()
                .isNotEmpty();
    }

    @Test
    public void getHeroesThatNameCointains_withoutMatchingHeroes_throwsHeroesNotFoundException() {
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
    public void getHeroesThatNameCointains_withNullNameToFind_throwsIllegalArgumentException() {
        //then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    underTest.getHeroesThatNameCointains(null);
                })
                .withMessage("Name cannot be null or empty.");
    }

    @Test
    public void getHeroesThatNameCointains_withEmptyNameToFind_throwsIllegalArgumentException() {
        //then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    underTest.getHeroesThatNameCointains("");
                })
                .withMessage("Name cannot be null or empty.");
    }

    @Test
    public void updateHeroe_withValidHeroeInfoAndExistingUser_notThrowsException() {
        //given
        Long existentHeroeId = 1l;
        Heroe heroeToSave = new Heroe("Ant Man");

        given(heroeRepository.findById(existentHeroeId))
                .willReturn(Optional.of(heroeToSave));

        //given
        String newName = "Wakanda";
        underTest.updateHeroe(existentHeroeId,newName);

        //then
        verify(heroeRepository).findById(existentHeroeId);
        verify(heroeRepository).save(heroeToSave);
    }

}
