package com.abn.food.recipe.controller;

import static java.util.stream.Collectors.toSet;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.abn.food.recipe.model.FoodRecipe;
import com.abn.food.recipe.service.FoodRecipeServiceImpl;

/**
 * Food Recipe Controller to perform CRUD operations
 *
 * @author Akhtar
 */
@RestController
@RequestMapping(value = "/abn/kitchen/foodRecipe", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
public class FoodRecipeController {

    private final FoodRecipeServiceImpl foodRecipeService;

    /**
     * Constructor initialization
     *
     * @param foodRecipeService food recipe service implementation
     */
    public FoodRecipeController(FoodRecipeServiceImpl foodRecipeService) {
        this.foodRecipeService = foodRecipeService;
    }

    /**
     * Save new food recipe in inventory
     *
     * @param foodRecipe new food recipe to be saved
     * @return saved food recipe
     */
    @PostMapping
    @ResponseStatus(CREATED)
    public FoodRecipe postFoodRecipe(@RequestBody FoodRecipe foodRecipe) {

        return foodRecipeService.saveFoodRecipe(foodRecipe);
    }

    /**
     * Update existing food recipe from inventory
     *
     * @param id         of existing food recipe in inventory
     * @param foodRecipe food recipe to be updated in inventory
     * @return updated food recipe from inventory
     */
    @PutMapping(value = "/{id}")
    @ResponseStatus(OK)
    public FoodRecipe putFoodRecipe(@PathVariable Long id, @RequestBody FoodRecipe foodRecipe) {

        return foodRecipeService.updateFoodRecipe(foodRecipe, id);
    }

    /**
     * Get all matched food recipes from inventory
     *
     * @param isVegetarian       share food recipes which are only vegetarian(including vegan)
     * @param numberOfServings   recipes matching number of servings
     * @param includeIngredients recipes which has requested ingredients
     * @param excludeIngredients recipes which doesn't have requested ingredients
     * @param instructions       recipes which has instructions from keywords
     * @return all food recipes
     */
    @GetMapping
    @ResponseStatus(OK)
    public List<FoodRecipe> getFoodRecipes(@RequestParam(required = false) Boolean isVegetarian,
                                           @RequestParam(required = false) Integer numberOfServings,
                                           @RequestParam(required = false, defaultValue = "#{T(java.util.Collections).emptyList()}") List<String> includeIngredients,
                                           @RequestParam(required = false, defaultValue = "#{T(java.util.Collections).emptyList()}") List<String> excludeIngredients,
                                           @RequestParam(required = false, defaultValue = "#{T(java.util.Collections).emptyList()}") List<String> instructions) {

        return foodRecipeService.getAllFoodRecipes(isVegetarian, numberOfServings,
            includeIngredients.stream().map(String::toUpperCase).collect(toSet()),
            excludeIngredients.stream().map(String::toUpperCase).collect(toSet()),
            instructions.stream().map(String::toUpperCase).collect(toSet()));
    }

    /**
     * Get requested food recipe if found in inventory
     *
     * @param id of the food recipe
     * @return food recipe from inventory
     */
    @GetMapping(value = "/{id}")
    @ResponseStatus(OK)
    public FoodRecipe getFoodRecipe(@PathVariable Long id) {

        return foodRecipeService.getIndividualFoodRecipe(id);
    }

    /**
     * Delete existing food recipe from inventory
     *
     * @param id to be deleted from food recipe inventory
     */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteFoodRecipe(@PathVariable Long id) {

        foodRecipeService.removeFoodRecipe(id);
    }
}
