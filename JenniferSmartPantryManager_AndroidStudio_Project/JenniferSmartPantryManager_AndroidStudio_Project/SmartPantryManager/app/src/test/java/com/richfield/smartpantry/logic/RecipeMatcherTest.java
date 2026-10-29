package com.richfield.smartpantry.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.richfield.smartpantry.model.PantryItem;
import com.richfield.smartpantry.model.Recipe;
import com.richfield.smartpantry.model.RecipeIngredient;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for the strict-matching rule (brief Section 2.3). These are pure
 * JUnit tests with no Android dependencies, so the single most important piece
 * of business logic can be verified in isolation.
 */
public class RecipeMatcherTest {

    private final RecipeMatcher matcher = new RecipeMatcher();

    private Recipe recipe(String name, String[]... ings) {
        Recipe r = new Recipe(1, name, "Test", "steps");
        for (String[] i : ings) {
            r.addIngredient(new RecipeIngredient(i[0], Double.parseDouble(i[1]), i[2]));
        }
        return r;
    }

    private PantryItem pantry(String name, double qty, String unit) {
        return new PantryItem(0, name, qty, unit, null);
    }

    @Test
    public void allIngredientsPresent_recipeIsSuggested() {
        Recipe eggs = recipe("Scrambled Eggs",
                new String[]{"eggs", "3", "pcs"},
                new String[]{"butter", "10", "g"});
        List<PantryItem> p = new ArrayList<>();
        p.add(pantry("Eggs", 6, "pcs"));
        p.add(pantry("Butter", 100, "g"));

        List<Recipe> result = matcher.findStrictMatches(list(eggs), p);
        assertEquals(1, result.size());
    }

    @Test
    public void oneIngredientMissing_recipeExcludedFromStrict() {
        // Brief: 5 ingredients required, pantry has 4 -> must NOT appear.
        Recipe pasta = recipe("Tomato Pasta",
                new String[]{"pasta", "200", "g"},
                new String[]{"tomato", "3", "pcs"},
                new String[]{"garlic", "2", "pcs"},
                new String[]{"oil", "1", "tbsp"},
                new String[]{"salt", "1", "tsp"});
        List<PantryItem> p = new ArrayList<>();
        p.add(pantry("Pasta", 500, "g"));
        p.add(pantry("Tomatoes", 5, "pcs"));
        p.add(pantry("Garlic", 4, "pcs"));
        p.add(pantry("Oil", 1, "l"));
        // salt missing

        assertTrue(matcher.findStrictMatches(list(pasta), p).isEmpty());
        // ... but it should surface in the separate "Almost There" list.
        assertEquals(1, matcher.findAlmostThere(list(pasta), p).size());
    }

    @Test
    public void pluralAndCaseDifferences_stillMatch() {
        // "tomato" (recipe) vs "Tomatoes" (pantry) must match.
        Recipe r = recipe("Tomato", new String[]{"tomato", "1", "pcs"});
        List<PantryItem> p = new ArrayList<>();
        p.add(pantry("Tomatoes", 3, "pcs"));
        assertEquals(1, matcher.findStrictMatches(list(r), p).size());
    }

    @Test
    public void insufficientQuantity_recipeExcluded() {
        Recipe r = recipe("Big Bake", new String[]{"flour", "500", "g"});
        List<PantryItem> p = new ArrayList<>();
        p.add(pantry("Flour", 100, "g")); // not enough
        assertTrue(matcher.findStrictMatches(list(r), p).isEmpty());
    }

    @Test
    public void emptyPantry_noStrictMatches() {
        Recipe r = recipe("Anything", new String[]{"eggs", "1", "pcs"});
        assertFalse(matcher.findStrictMatches(list(r), new ArrayList<>()).size() > 0);
    }

    @SafeVarargs
    private final <T> List<T> list(T... items) {
        List<T> l = new ArrayList<>();
        for (T i : items) {
            l.add(i);
        }
        return l;
    }
}
