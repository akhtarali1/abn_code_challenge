package com.abn.food.recipe.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Ingredient Model
 *
 * @author Akhtar
 */
@Schema(description = "Each ingredient with Quantity and unit specification")
public class Ingredient {

    @NotNull
    @Size(min = 1, max = 200)
    @Schema(description = "Ingredient name")
    private String name;

    @NotNull
    @Schema(description = "Quantity if ingredient required")
    private Integer quantity;

    @Size(max = 20)
    @Schema(description = "unit measurement of Quantity")
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
