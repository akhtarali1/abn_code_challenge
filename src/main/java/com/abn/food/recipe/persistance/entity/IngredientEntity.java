package com.abn.food.recipe.persistance.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Food Recipe each Ingredient Entity
 *
 * @author Akhtar
 */
@Entity
@Table(name = "INGREDIENT")
public class IngredientEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private Integer quantity;
    private String unit;
    @ManyToOne
    @JoinColumn(name = "REFERENCE_ID", nullable = false)
    private IngredientReferenceEntity referenceEntity;

    @ManyToOne
    @JoinColumn(name = "FOOD_RECIPE_ID", nullable = false)
    private FoodRecipeEntity foodRecipe;

    /**
     * Get Id of Food recipe Ingredient
     *
     * @return Id of the Ingredient
     */
    public Long getId() {
        return id;
    }

    /**
     * Get quantity of Ingredient for food recipe
     *
     * @return Quantity of Ingredient
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Get quantity unit of Ingredient
     *
     * @return quantity unit of Ingredient
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Get name of Ingredient as reference entity
     *
     * @return name of Ingredient as reference entity
     */
    public IngredientReferenceEntity getReferenceEntity() {
        return referenceEntity;
    }

    public FoodRecipeEntity getFoodRecipe() {
        return foodRecipe;
    }

    /**
     * Set quantity iof Food recipe
     *
     * @param quantity of the Ingredient for food recipe
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * Set Id for the Ingredient of food recipe
     *
     * @param id of the Ingredient for food recipe
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Set unit of quantity for Ingredient
     *
     * @param unit of the quantity for Ingredient
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * Set Ingredient name as reference entity
     *
     * @param name Ingredient name as reference entity
     */
    public void setReferenceEntity(IngredientReferenceEntity name) {
        this.referenceEntity = name;
    }

    public IngredientEntity setFoodRecipe(FoodRecipeEntity foodRecipe) {
        this.foodRecipe = foodRecipe;
        return this;
    }
}
