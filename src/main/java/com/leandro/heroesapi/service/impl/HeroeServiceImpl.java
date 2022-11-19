package com.leandro.heroesapi.service.impl;

import com.leandro.heroesapi.exception.HeroesNotFoundException;
import com.leandro.heroesapi.model.Heroe;
import com.leandro.heroesapi.repository.HeroeRepository;
import com.leandro.heroesapi.service.HeroeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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

    @Override
    public Heroe findHeroeById(Long id) {
        if (id == null)
            throw new IllegalArgumentException("Id cannot be null.");

        return heroeRepository.findById(id)
                .orElseThrow(() -> new HeroesNotFoundException(
                        String.format(String.format("Heroe with id %d does not exists.", id))
                ));
    }

    @Override
    public List<Heroe> getHeroesThatNameCointains(String param) {
        if (null == param || param.isBlank())
            throw new IllegalArgumentException("Name cannot be null or empty.");

        List<Heroe> foundHeroes = heroeRepository.findByNameContainingIgnoreCase(param);

        if (foundHeroes.isEmpty())
            throw new HeroesNotFoundException("No heroes found.");

        return foundHeroes;
    }

    @Override
    public void updateHeroe(Long existentHeroeId, String newName) {
        if(null == newName)
            throw new IllegalArgumentException("Name cannot be null or empty.");

        Heroe heroeToModify = findHeroeById(existentHeroeId);

        heroeToModify.setName(newName);

        heroeRepository.save(heroeToModify);
    }

    @Override
    public void deleteHeroe(Long existentHeroeId) {
        Heroe heroeToDelete = findHeroeById(existentHeroeId);

        heroeRepository.delete(heroeToDelete);
    }

}
