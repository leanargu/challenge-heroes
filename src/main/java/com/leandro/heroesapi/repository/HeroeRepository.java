package com.leandro.heroesapi.repository;

import com.leandro.heroesapi.model.Heroe;

import java.util.List;
import java.util.Optional;

public interface HeroeRepository {

    List<Heroe> findAll();

    Optional<Heroe> findById(long anyLong);

    List<Heroe> findByNameContainingIgnoreCase(String anyString);

    void save(Heroe any);
}
