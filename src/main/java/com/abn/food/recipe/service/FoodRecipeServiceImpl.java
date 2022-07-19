package com.abn.food.recipe.service;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.abn.food.recipe.enums.DishType;
import com.abn.food.recipe.mapper.FoodRecipeMapper;
import com.abn.food.recipe.model.FoodRecipe;
import com.abn.food.recipe.persistance.entity.FoodRecipeEntity;
import com.abn.food.recipe.persistance.repository.FoodRecipeRepository;

/**
 * Food Recipe Service Implementation
 *
 * @author Akhtar
 */
@Service
public class FoodRecipeServiceImpl {

    private final FoodRecipeMapper foodRecipeMapper;
    private final FoodRecipeRepository foodRecipeRepository;

    /**
     * Constructor initialization
     *
     * @param foodRecipeMapper     Food Recipe Mapper
     * @param foodRecipeRepository Food Recipe Repository
     */
    public FoodRecipeServiceImpl(FoodRecipeMapper foodRecipeMapper, FoodRecipeRepository foodRecipeRepository) {
        this.foodRecipeMapper = foodRecipeMapper;
        this.foodRecipeRepository = foodRecipeRepository;
    }

    /**
     * Save new food recipe in inventory
     *
     * @param foodRecipeModel new food recipe to be saved
     * @return saved food recipe
     */
    public FoodRecipe saveFoodRecipe(FoodRecipe foodRecipeModel) {
        FoodRecipeEntity recipeEntity = new FoodRecipeEntity();
        return persistAndGetFoodRecipe(foodRecipeModel, null, recipeEntity);
    }

    /**
     * Update existing food recipe from inventory
     *
     * @param id              of existing food recipe in inventory
     * @param foodRecipeModel food recipe to be updated in inventory
     * @return updated food recipe from inventory
     */
    public FoodRecipe updateFoodRecipe(FoodRecipe foodRecipeModel, Long id) {
        return foodRecipeRepository.findById(id)
            .map(entity -> persistAndGetFoodRecipe(foodRecipeModel, id, entity))
            .orElseThrow(() -> new IdNotFoundException(id));
    }

    /**
     * Get requested food recipe if found in inventory
     *
     * @param id of the food recipe
     * @return food recipe from inventory
     */
    public FoodRecipe getIndividualFoodRecipe(Long id) {
        return foodRecipeRepository.findById(id)
            .map(recipeEntity -> foodRecipeMapper.formFoodRecipeModel(recipeEntity, true))
            .orElseThrow(() -> new IdNotFoundException(id));
    }

    /**
     * Delete existing food recipe from inventory
     *
     * @param id to be deleted from food recipe inventory
     */
    public void removeFoodRecipe(Long id) {
        foodRecipeRepository.deleteById(id);
    }

    /**
     * Get all matched food recipes from inventory
     *
     * @param isVegetarian        share food recipes which are only vegetarian(including vegan)
     * @param numberOfServings    recipes matching number of servings
     * @param includeIngredients  recipes which has requested ingredients
     * @param excludeIngredients  recipes which doesn't have requested ingredients
     * @param instructionKeyWords recipes which has instructions from keywords
     * @return all food recipes
     */
    public List<FoodRecipe> getAllFoodRecipes(Boolean isVegetarian, Integer numberOfServings, Set<String> includeIngredients,
                                              Set<String> excludeIngredients, Set<String> instructionKeyWords) {
        return foodRecipeRepository.findAll()
            .stream()
            .filter(entity -> !Boolean.TRUE.equals(isVegetarian) || !entity.getDishType().equals(DishType.NON_VEGETARIAN))
            .filter(entity -> numberOfServings == null || numberOfServings.equals(entity.getServings()))
            .filter(entity -> includeIngredients.isEmpty() || entity.getAllIngredientNamesInUpperCase().containsAll(includeIngredients))
            .filter(entity -> excludeIngredients.isEmpty() || checkExcludedIngredientsDoesntExist(entity.getAllIngredientNamesInUpperCase(), excludeIngredients))
            .filter(entity -> instructionKeyWords.isEmpty() || instructionKeyWords.stream().allMatch(entity.getInstructions().toUpperCase()::contains))
            .map(entity -> foodRecipeMapper.formFoodRecipeModel(entity, false))
            .collect(toList());
    }

    private boolean checkExcludedIngredientsDoesntExist(Set<String> entityIngredients, Set<String> userExcludedIngredients) {
        for (String s : userExcludedIngredients) {
            if (entityIngredients.contains(s)) {
                return false;
            }
        }
        return true;
    }

    private FoodRecipe persistAndGetFoodRecipe(FoodRecipe foodRecipeModel, Long id, FoodRecipeEntity recipeEntity) {
        foodRecipeMapper.formFoodRecipeEntity(foodRecipeModel, recipeEntity, id);
        FoodRecipeEntity savedEntity = foodRecipeRepository.save(recipeEntity);
        return foodRecipeMapper.formFoodRecipeModel(savedEntity, true);
    }

}
