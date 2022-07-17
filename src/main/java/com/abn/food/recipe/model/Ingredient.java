package com.abn.food.recipe.model;

/**
 * Ingredient Model
 *
 * @author Akhtar
 */
public class Ingredient {

    private String name;
    private Integer quantity;
    private String unit;

    /**
     * Get ingredient name
     *
     * @return ingredient name
     */
    public String getName() {
        return name;
    }

    /**
     * Set ingredient name
     *
     * @param name ingredient name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get ingredient quantity
     *
     * @return ingredient quantity
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Set ingredient quantity
     *
     * @param quantity ingredient quantity
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * Get ingredient quantity unit
     *
     * @return ingredient quantity unit
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Set ingredient quantity unit
     *
     * @param unit ingredient quantity unit
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }
}
