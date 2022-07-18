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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * Food Recipe Controller to perform CRUD operations
 *
 * @author Akhtar
 */
@RestController
@RequestMapping(value = "/abn/kitchen/foodRecipe", produces = APPLICATION_JSON_VALUE)
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
    @Operation(summary = "Save new food recipe in inventory")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Recipe Saved",
            content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = FoodRecipe.class)) })
    })
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
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
    @Operation(summary = "Update existing food recipe in inventory")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recipe updated",
            content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = FoodRecipe.class)) })
    })
    @PutMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public FoodRecipe putFoodRecipe(@Parameter(description = "id of recipe ot be saved", required = true) @PathVariable Long id,
                                    @RequestBody FoodRecipe foodRecipe) {

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
    @Operation(summary = "Get all available food recipes from inventory based on filters")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recipes retrieved",
            content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = FoodRecipe.class)) })
    })
    @GetMapping
    @ResponseStatus(OK)
    public List<FoodRecipe> getFoodRecipes(@Parameter(description = "To query only vegetarian/vegan recipes")
                                               @RequestParam(required = false) Boolean isVegetarian,
                                           @Parameter(description = "To query number of servings recipes can be served")
                                                @RequestParam(required = false) Integer numberOfServings,
                                           @Parameter(description = "To query recipes which includes searched ingredients")
                                               @RequestParam(required = false, defaultValue = "#{T(java.util.Collections).emptyList()}")
                                               List<String> includeIngredients,
                                           @Parameter(description = "To query recipes which excludes searched ingredients")
                                               @RequestParam(required = false, defaultValue = "#{T(java.util.Collections).emptyList()}")
                                               List<String> excludeIngredients,
                                           @Parameter(description = "To query keywords which present in recipe instructions")
                                               @RequestParam(required = false, defaultValue = "#{T(java.util.Collections).emptyList()}")
                                               List<String> instructions) {

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
    @Operation(summary = "Get food recipes by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recipe retrieved",
            content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = FoodRecipe.class)) })
    })
    @GetMapping(value = "/{id}")
    @ResponseStatus(OK)
    public FoodRecipe getFoodRecipe(@Parameter(description = "Id of the food recipe to be retrieved") @PathVariable Long id) {

        return foodRecipeService.getIndividualFoodRecipe(id);
    }

    /**
     * Delete existing food recipe from inventory
     *
     * @param id to be deleted from food recipe inventory
     */
    @Operation(summary = "Delete food recipe from inventory")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Recipe deleted",
            content = { @Content(mediaType = "application/json") })
    })
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteFoodRecipe(@PathVariable Long id) {

        foodRecipeService.removeFoodRecipe(id);
    }
}
