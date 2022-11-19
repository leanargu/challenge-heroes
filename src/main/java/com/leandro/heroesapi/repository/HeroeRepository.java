package com.leandro.heroesapi.repository;

import com.leandro.heroesapi.model.Heroe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HeroeRepository extends JpaRepository<Heroe, Long> {

    List<Heroe> findAll();
    List<Heroe> findByNameContainingIgnoreCase(String name);

}
