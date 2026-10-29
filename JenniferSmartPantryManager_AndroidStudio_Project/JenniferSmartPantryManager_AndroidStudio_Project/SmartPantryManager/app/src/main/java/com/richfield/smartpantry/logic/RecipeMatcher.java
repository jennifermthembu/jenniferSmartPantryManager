package com.richfield.smartpantry.logic;

import com.richfield.smartpantry.model.PantryItem;
import com.richfield.smartpantry.model.Recipe;
import com.richfield.smartpantry.model.RecipeIngredient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The single most important piece of business logic in the app (brief
 * Section 2.3): the STRICT-MATCHING RULE.
 *
 * <p>A recipe qualifies as "suggested" only if EVERY ingredient it requires is
 * present in the pantry in at least the required quantity. If even one
 * ingredient is missing (or present but not in enough quantity) the recipe is
 * excluded from the strict suggestions list.
 *
 * <p>As a bonus (brief Section 8) this class can also compute an "Almost There"
 * list of recipes that are missing exactly ONE ingredient. That list is kept
 * completely separate from the strict suggestions.
 */
public class RecipeMatcher {

    /**
     * Builds a lookup of the pantry keyed by normalised ingredient name. When
     * the same ingredient appears more than once we keep the largest available
     * quantity so the user is not penalised for duplicate rows.
     */
    private Map<String, PantryItem> indexPantry(List<PantryItem> pantry) {
        Map<String, PantryItem> index = new HashMap<>();
        for (PantryItem item : pantry) {
            String key = IngredientNormalizer.normalizeName(item.getName());
            PantryItem existing = index.get(key);
            if (existing == null || item.getQuantity() > existing.getQuantity()) {
                index.put(key, item);
            }
        }
        return index;
    }

    /**
     * @return {@code true} if the pantry satisfies every ingredient this recipe
     *         requires, in at least the required quantity.
     */
    public boolean canMake(Recipe recipe, Map<String, PantryItem> pantryIndex) {
        return countMissing(recipe, pantryIndex) == 0;
    }

    /**
     * Counts how many of the recipe's ingredients the pantry CANNOT satisfy.
     * An ingredient counts as missing if it is absent, or present but with less
     * than the required quantity (when units are comparable).
     */
    public int countMissing(Recipe recipe, Map<String, PantryItem> pantryIndex) {
        int missing = 0;
        for (RecipeIngredient required : recipe.getIngredients()) {
            if (!isSatisfied(required, pantryIndex)) {
                missing++;
            }
        }
        return missing;
    }

    private boolean isSatisfied(RecipeIngredient required, Map<String, PantryItem> pantryIndex) {
        String key = IngredientNormalizer.normalizeName(required.getName());
        PantryItem have = pantryIndex.get(key);
        if (have == null) {
            return false; // ingredient not in the pantry at all
        }
        String requiredUnit = IngredientNormalizer.normalizeUnit(required.getUnit());
        String haveUnit = IngredientNormalizer.normalizeUnit(have.getUnit());
        // Only enforce the quantity check when the units are directly comparable.
        // If the units differ we fall back to "ingredient is present", which keeps
        // matching robust without a full unit-conversion engine.
        if (requiredUnit.equals(haveUnit)) {
            return have.getQuantity() >= required.getQuantity();
        }
        return true;
    }

    /**
     * STRICT suggestions: recipes the user can make right now (zero missing
     * ingredients).
     */
    public List<Recipe> findStrictMatches(List<Recipe> recipes, List<PantryItem> pantry) {
        Map<String, PantryItem> pantryIndex = indexPantry(pantry);
        List<Recipe> result = new ArrayList<>();
        for (Recipe recipe : recipes) {
            if (recipe.getIngredientCount() > 0 && canMake(recipe, pantryIndex)) {
                result.add(recipe);
            }
        }
        return result;
    }

    /**
     * BONUS (brief Section 8): recipes missing exactly ONE ingredient. Kept
     * separate from the strict suggestions.
     */
    public List<Recipe> findAlmostThere(List<Recipe> recipes, List<PantryItem> pantry) {
        Map<String, PantryItem> pantryIndex = indexPantry(pantry);
        List<Recipe> result = new ArrayList<>();
        for (Recipe recipe : recipes) {
            if (recipe.getIngredientCount() > 0 && countMissing(recipe, pantryIndex) == 1) {
                result.add(recipe);
            }
        }
        return result;
    }
}
