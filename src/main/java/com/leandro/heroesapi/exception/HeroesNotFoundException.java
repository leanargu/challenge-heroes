package com.leandro.heroesapi.exception;

public class HeroesNotFoundException extends RuntimeException{

    public HeroesNotFoundException(String message) {
        super(message);
    }

}
