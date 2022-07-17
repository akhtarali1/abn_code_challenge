package com.abn.food.recipe.model;

import java.util.List;

import org.springframework.hateoas.RepresentationModel;

import com.abn.food.recipe.enums.DishType;

/**
 * Food Recipe Rest Model
 *
 * @author AKhtar
 */
public class FoodRecipe extends RepresentationModel<FoodRecipe> {

    private Long id;
    private DishType dishType;
    private Integer servings;
    private List<Ingredient> ingredients;
    private List<String> ingredientsWithQuantity;
    private String instructions;

    /**
     * Get Dish type
     *
     * @return dish type
     */
    public DishType getDishType() {
        return dishType;
    }

    /**
     * Get Food recipe Id
     *
     * @return Food recipe Id
     */
    public Long getId() {
        return id;
    }

    /**
     * Get number of servings
     *
     * @return number of servings
     */
    public Integer getServings() {
        return servings;
    }

    /**
     * Get ingredients for recipe
     *
     * @return ingredients
     */
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    /**
     * Get ingredients with quantity
     *
     * @return ingredients with quantity
     */
    public List<String> getIngredientsWithQuantity() {
        return ingredientsWithQuantity;
    }

    /**
     * Get fod recipe instructions
     *
     * @return instructions
     */
    public String getInstructions() {
        return instructions;
    }

    /**
     * Set dish type of food
     *
     * @param dishType dish type of food
     */
    public void setDishType(DishType dishType) {
        this.dishType = dishType;
    }

    /**
     * Set number of servings
     *
     * @param servings number of servings
     */
    public void setServings(Integer servings) {
        this.servings = servings;
    }

    /**
     * Set all required ingredients for recipe
     *
     * @param ingredients all ingredients for recipe
     */
    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * Set all required ingriendts with quantity for food recipe in single String
     *
     * @param ingredientsWithQuantity all ingredients with required quantity in single String
     */
    public void setIngredientsWithQuantity(List<String> ingredientsWithQuantity) {
        this.ingredientsWithQuantity = ingredientsWithQuantity;
    }


    /**
     * Set instructions for food recipe
     *
     * @param instructions instructions for food recipe
     */
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    /**
     * Set unique Id for each food recipe
     *
     * @param id unique Id for food recipe
     */
    public void setId(Long id) {
        this.id = id;
    }
}
