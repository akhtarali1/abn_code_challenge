package com.abn.food.recipe.controller;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.abn.food.recipe.persistance.entity.IngredientReferenceEntity;
import com.abn.food.recipe.persistance.repository.IngredientReferenceRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * Ingredients Reference Controller
 *
 * @author Akhtar
 */
@RestController
@RequestMapping(value = "/abn/kitchen/reference/ingredients", produces = APPLICATION_JSON_VALUE)
public class IngredientsReferenceController {

    private final IngredientReferenceRepository referenceRepository;

    /**
     * Constructor initialization
     *
     * @param referenceRepository Ingredient Reference Repository
     */
    public IngredientsReferenceController(IngredientReferenceRepository referenceRepository) {
        this.referenceRepository = referenceRepository;
    }

    /**
     * Get all available Ingredients for showing in search options
     *
     * @return all available Ingredients
     */
    @Operation(summary = "Get all available ingredients to be used for searching on UI")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ingredients list retrieved",
            content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = List.class)) })
    })
    @GetMapping
    @ResponseStatus(OK)
    public List<String> getAllReferenceIngredients() {
        return referenceRepository.findAll()
            .stream().map(IngredientReferenceEntity::getName)
            .collect(toList());

    }
}
