package com.leandro.heroesapi.service.impl;

import com.leandro.heroesapi.exception.HeroesNotFoundException;
import com.leandro.heroesapi.model.Heroe;
import com.leandro.heroesapi.repository.HeroeRepository;
import com.leandro.heroesapi.service.HeroeService;

import java.util.List;

public class HeroeServiceImpl implements HeroeService {

    private HeroeRepository heroeRepository;

    public HeroeServiceImpl(HeroeRepository heroeRepository){
        this.heroeRepository = heroeRepository;
    }

    public List<Heroe> getAllHeroes() {
        List<Heroe> foundHeroes = heroeRepository.findAll();

        if (foundHeroes.isEmpty())
            throw new HeroesNotFoundException("No heroes found.");

        return foundHeroes;
    }

}
