package com.leandro.heroesapi.service.impl;

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
        return heroeRepository.findAll();
    }

}
