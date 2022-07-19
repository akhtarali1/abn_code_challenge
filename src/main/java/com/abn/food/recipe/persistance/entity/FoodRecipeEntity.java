package com.abn.food.recipe.persistance.entity;

import static java.util.stream.Collectors.toSet;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.abn.food.recipe.enums.DishType;

/**
 * Food Recipe Persistence Entity class
 *
 * @author Akhtar
 */
@Entity
@Table(name = "FOOD_RECIPES")
public class FoodRecipeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String recipeName;
    @Enumerated(STRING)
    private DishType dishType;

    private Integer servings;

    private String instructions;
    @OneToMany(mappedBy = "foodRecipe", cascade = {PERSIST, MERGE, REMOVE}, orphanRemoval = true)
    private List<IngredientEntity> ingredients;

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
    public List<IngredientEntity> getIngredients() {
        return ingredients;
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
     * Get recipe name
     * @return recipe name
     */
    public String getRecipeName() { return recipeName; }

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
    public void setIngredients(List<IngredientEntity> ingredients) {
        this.ingredients = ingredients;
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

    /**
     * Set recipe name
     * @param recipeName recipe name
     */
    public void setRecipeName(String recipeName) { this.recipeName = recipeName; }

    public Set<String> getAllIngredientNamesInUpperCase() {
        return ingredients.stream()
            .map(ingredient -> ingredient.getReferenceEntity().getName().toUpperCase())
            .collect(toSet());
    }
}
