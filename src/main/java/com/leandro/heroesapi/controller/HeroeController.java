package com.leandro.heroesapi.controller;

import com.leandro.heroesapi.model.Heroe;
import com.leandro.heroesapi.service.HeroeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/heroe")
public class HeroeController {

    @Autowired
    private HeroeService heroeService;

    @GetMapping("/all")
    public List<Heroe> getAllHeroes(@RequestParam(required = false) String containing) {
        if (null != containing && !containing.isBlank())
            return heroeService.getHeroesThatNameCointains(containing);
        return heroeService.getAllHeroes();
    }

    @GetMapping("/{id}")
    public Heroe getHeroeById(@PathVariable Long id) {
        return heroeService.findHeroeById(id);
    }

    @PutMapping("/{id}")
    public void updateHeroe(
            @PathVariable Long id,
            @RequestParam(required = false) String name) {
        heroeService.updateHeroe(id, name);
    }

}
