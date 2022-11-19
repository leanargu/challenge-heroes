package com.leandro.heroesapi.model;

import javax.persistence.*;

@Entity
@Table
public class Heroe {

    @Id
    @SequenceGenerator(
            name = "heroe_sequence",
            sequenceName = "heroe_sequence",
            allocationSize = 1)
    @GeneratedValue(
            generator = "heroe_sequence",
            strategy = GenerationType.SEQUENCE)
    private long id;
    private String name;

    public Heroe() {
    }

    public Heroe(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
