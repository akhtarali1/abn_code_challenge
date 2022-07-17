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

/**
 * Ingredients Reference Controller
 *
 * @author Akhtar
 */
@RestController
@RequestMapping(value = "/abn/kitchen/reference/ingredients", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
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
    @GetMapping
    @ResponseStatus(OK)
    public List<String> getAllReferenceIngredients() {
        return referenceRepository.findAll()
            .stream().map(IngredientReferenceEntity::getName)
            .collect(toList());

    }
}
