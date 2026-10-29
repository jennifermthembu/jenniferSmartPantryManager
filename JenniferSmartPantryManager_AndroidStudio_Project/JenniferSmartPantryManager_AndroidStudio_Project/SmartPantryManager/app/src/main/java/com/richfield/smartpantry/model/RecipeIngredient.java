package com.richfield.smartpantry.model;

/**
 * One required ingredient for a recipe, including the amount and unit the
 * recipe calls for. Used by the strict-matching logic to compare against the
 * quantities the user currently has in their pantry.
 */
public class RecipeIngredient {

    private String name;
    private double quantity;
    private String unit;

    public RecipeIngredient(String name, double quantity, String unit) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }
}
