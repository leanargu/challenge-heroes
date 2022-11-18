package com.leandro.heroesapi.service.impl;

import com.leandro.heroesapi.model.Heroe;
import com.leandro.heroesapi.service.HeroeService;

import java.util.List;

public class HeroeServiceImpl implements HeroeService {

    public List<Heroe> getAllHeroes() {
        return List.of(
                new Heroe("Superman"),
                new Heroe("Flash")
        );
    }

}
