package com.richfield.smartpantry.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A recipe in the user's collection. The required ingredients are stored in a
 * embedded ingredient list loaded from the Firestore {@code recipes} collection.
 */
public class Recipe {

    private String id;
    private String name;
    private String category;
    private String steps;
    private final List<RecipeIngredient> ingredients = new ArrayList<>();

    public Recipe() {
    }

    public Recipe(String id, String name, String category, String steps) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.steps = steps;
    }

    public Recipe(long id, String name, String category, String steps) {
        this(String.valueOf(id), name, category, steps);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public List<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public void addIngredient(RecipeIngredient ingredient) {
        this.ingredients.add(ingredient);
    }

    public int getIngredientCount() {
        return ingredients.size();
    }
}
