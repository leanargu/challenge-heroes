package com.leandro.heroesapi.repository;

import com.leandro.heroesapi.model.Heroe;

import java.util.List;

public interface HeroeRepository {

    List<Heroe> findAll();

}
