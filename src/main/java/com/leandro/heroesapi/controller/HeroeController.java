package com.leandro.heroesapi.controller;

import com.leandro.heroesapi.model.Heroe;
import com.leandro.heroesapi.service.HeroeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/heroe")
public class HeroeController {

    @Autowired
    private HeroeService heroeService;

    @GetMapping("/all")
    public List<Heroe> getAllHeroes() {
        return heroeService.getAllHeroes();
    }

}
