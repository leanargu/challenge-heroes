package com.leandro.heroesapi.service;

import com.leandro.heroesapi.model.Heroe;

import java.util.List;

public interface HeroeService {


    List<Heroe> getAllHeroes();

    Heroe findHeroeById(Long l);

    List<Heroe> getHeroesThatNameCointains(String man);

    void updateHeroe(Long existentHeroeId, String newName);
}
