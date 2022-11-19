package com.leandro.heroesapi.repository;

import com.leandro.heroesapi.model.Heroe;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class HeroeRepositoryTest {

    @Autowired
    private HeroeRepository underTest;

    @AfterEach
    void tearDown(){
        underTest.deleteAll();
    }

    @Test
    void findAll() {
        //when
        List<Heroe> result = underTest.findAll();

        //then
        assertThat(result)
                .asList()
                .hasSize(3);
    }

}
