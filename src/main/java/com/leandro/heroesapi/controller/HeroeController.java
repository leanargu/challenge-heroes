package com.leandro.heroesapi.controller;

import com.leandro.heroesapi.aspect.LogExecutionTime;
import com.leandro.heroesapi.model.Heroe;
import com.leandro.heroesapi.service.HeroeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/heroe")
public class HeroeController {

    @Autowired
    private HeroeService heroeService;

    @Operation(summary = "Return all Heroes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Heroes found",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Heroe.class))
                    }),
            @ApiResponse(responseCode = "404", description = "No heroes found", content = @Content)
    })
    @GetMapping("/all")
    @LogExecutionTime
    public List<Heroe> getAllHeroes(@RequestParam(required = false) String containing) {
        if (null != containing && !containing.isBlank())
            return heroeService.getHeroesThatNameCointains(containing);
        return heroeService.getAllHeroes();
    }

    @Operation(summary = "Find Heroe by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Heroe found",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Heroe.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Heroe was not found", content = @Content)
    })
    @GetMapping("/{id}")
    @LogExecutionTime
    public Heroe getHeroeById(@PathVariable Long id) {
        return heroeService.findHeroeById(id);
    }

    @Operation(summary = "Update Heroe by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Heroe updated"),
            @ApiResponse(responseCode = "400", description = "Invalid id or heroe name supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Heroe to update was not found", content = @Content)
    })
    @PutMapping("/{id}")
    @LogExecutionTime
    public void updateHeroe(
            @PathVariable Long id,
            @RequestParam(required = false) String name) {
        heroeService.updateHeroe(id, name);
    }

    @Operation(summary = "Delete Heroe by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Heroe deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Heroe to delete was not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    @LogExecutionTime
    public void deleteHeroe(@PathVariable Long id) {
        heroeService.deleteHeroe(id);
    }

}
