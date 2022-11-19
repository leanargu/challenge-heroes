package com.leandro.heroesapi.service.impl;

import com.leandro.heroesapi.exception.HeroesNotFoundException;
import com.leandro.heroesapi.model.Heroe;
import com.leandro.heroesapi.repository.HeroeRepository;
import com.leandro.heroesapi.service.HeroeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HeroeServiceImpl implements HeroeService {

    @Autowired
    private HeroeRepository heroeRepository;

    @Autowired
    public HeroeServiceImpl(HeroeRepository heroeRepository){
        this.heroeRepository = heroeRepository;
    }

    @Autowired
    public void setHeroeRepository(HeroeRepository heroeRepository) {
        this.heroeRepository = heroeRepository;
    }

    @Cacheable("all_heroes")
    public List<Heroe> getAllHeroes() {
        List<Heroe> foundHeroes = heroeRepository.findAll();

        if (foundHeroes.isEmpty())
            throw new HeroesNotFoundException("No heroes found.");

        return foundHeroes;
    }

    @Cacheable(value = "heroe", key = "#id")
    @Override
    public Heroe findHeroeById(Long id) {
        if (id == null)
            throw new IllegalArgumentException("Id cannot be null.");

        return heroeRepository.findById(id)
                .orElseThrow(() -> new HeroesNotFoundException(
                        String.format(String.format("Heroe with id %d does not exists.", id))
                ));
    }

    @Cacheable(value = "heroe", key = "#param")
    @Override
    public List<Heroe> getHeroesThatNameCointains(String param) {
        if (null == param || param.isBlank())
            throw new IllegalArgumentException("Name cannot be null or empty.");

        List<Heroe> foundHeroes = heroeRepository.findByNameContainingIgnoreCase(param);

        if (foundHeroes.isEmpty())
            throw new HeroesNotFoundException("No heroes found.");

        return foundHeroes;
    }

    @Caching(evict = {
            @CacheEvict(value = "all_heroes", allEntries = true),
            @CacheEvict(value = "heroe", key = "#id")
    })
    @Override
    public void updateHeroe(Long id, String newName) {
        if(null == newName)
            throw new IllegalArgumentException("Name cannot be null or empty.");

        Heroe heroeToModify = findHeroeById(id);

        heroeToModify.setName(newName);

        heroeRepository.save(heroeToModify);
    }

    @Caching(evict = {
            @CacheEvict(value = "all_heroes", allEntries = true),
            @CacheEvict(value = "heroe", key = "#id")
    })
    @Override
    public void deleteHeroe(Long id) {
        Heroe heroeToDelete = findHeroeById(id);

        heroeRepository.delete(heroeToDelete);
    }

    @Caching(evict = {
            @CacheEvict(value = "all_heroes", allEntries = true),
            @CacheEvict(value = "heroe", allEntries = true)
    })
    public void invalidateCache() {
    }
}
