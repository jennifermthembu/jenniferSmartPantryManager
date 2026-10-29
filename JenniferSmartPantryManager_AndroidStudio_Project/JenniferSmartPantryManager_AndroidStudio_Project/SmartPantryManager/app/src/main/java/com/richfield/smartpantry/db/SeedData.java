package com.richfield.smartpantry.db;

import com.richfield.smartpantry.model.Recipe;
import com.richfield.smartpantry.model.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;

final class SeedData {

    private SeedData() {
    }

    static List<Recipe> recipes() {
        List<Recipe> recipes = new ArrayList<>();
        add(recipes, "1", "Scrambled Eggs", "Breakfast",
                "1. Beat the eggs with salt.\n2. Melt butter in a pan over low heat.\n3. Add eggs and stir gently until just set.",
                new String[][]{{"eggs", "3", "pcs"}, {"butter", "10", "g"}, {"salt", "1", "tsp"}});
        add(recipes, "2", "Tomato Omelette", "Breakfast",
                "1. Whisk eggs.\n2. Fry chopped tomato in oil.\n3. Pour in eggs and cook until firm.",
                new String[][]{{"eggs", "2", "pcs"}, {"tomato", "1", "pcs"}, {"oil", "1", "tbsp"}, {"salt", "1", "tsp"}});
        add(recipes, "3", "Buttered Toast", "Breakfast",
                "1. Toast the bread.\n2. Spread butter while warm.",
                new String[][]{{"bread", "2", "pcs"}, {"butter", "15", "g"}});
        add(recipes, "4", "Pancakes", "Breakfast",
                "1. Mix flour, milk, egg and sugar into a batter.\n2. Cook spoonfuls on a greased pan until golden.",
                new String[][]{{"flour", "200", "g"}, {"milk", "300", "ml"}, {"eggs", "1", "pcs"}, {"sugar", "2", "tbsp"}});
        add(recipes, "5", "Garlic Butter Rice", "Main",
                "1. Cook the rice.\n2. Saute garlic in butter.\n3. Toss rice through the garlic butter with salt.",
                new String[][]{{"rice", "200", "g"}, {"garlic", "2", "pcs"}, {"butter", "20", "g"}, {"salt", "1", "tsp"}});
        add(recipes, "6", "Tomato Pasta", "Main",
                "1. Boil the pasta.\n2. Simmer chopped tomato, garlic and oil into a sauce.\n3. Combine and season.",
                new String[][]{{"pasta", "200", "g"}, {"tomato", "3", "pcs"}, {"garlic", "2", "pcs"}, {"oil", "1", "tbsp"}, {"salt", "1", "tsp"}});
        add(recipes, "7", "Fried Rice", "Main",
                "1. Scramble the egg.\n2. Stir-fry cold rice with onion and soy sauce.\n3. Fold the egg through.",
                new String[][]{{"rice", "250", "g"}, {"eggs", "2", "pcs"}, {"onion", "1", "pcs"}, {"soy sauce", "2", "tbsp"}, {"oil", "1", "tbsp"}});
        add(recipes, "8", "Mashed Potatoes", "Side",
                "1. Boil the potatoes until soft.\n2. Mash with butter, milk and salt.",
                new String[][]{{"potato", "4", "pcs"}, {"butter", "30", "g"}, {"milk", "100", "ml"}, {"salt", "1", "tsp"}});
        add(recipes, "9", "Vegetable Stir Fry", "Main",
                "1. Heat oil.\n2. Stir-fry onion, carrot and pepper.\n3. Finish with soy sauce.",
                new String[][]{{"onion", "1", "pcs"}, {"carrot", "2", "pcs"}, {"pepper", "1", "pcs"}, {"oil", "1", "tbsp"}, {"soy sauce", "2", "tbsp"}});
        add(recipes, "10", "Chicken Rice Bowl", "Main",
                "1. Cook the rice.\n2. Pan-fry seasoned chicken.\n3. Serve chicken over rice with onion.",
                new String[][]{{"rice", "200", "g"}, {"chicken", "300", "g"}, {"onion", "1", "pcs"}, {"salt", "1", "tsp"}, {"oil", "1", "tbsp"}});
        add(recipes, "11", "Grilled Cheese Sandwich", "Snack",
                "1. Butter the bread.\n2. Add cheese between slices.\n3. Toast both sides until golden.",
                new String[][]{{"bread", "2", "pcs"}, {"cheese", "2", "pcs"}, {"butter", "15", "g"}});
        add(recipes, "12", "Potato Fries", "Snack",
                "1. Cut potatoes into strips.\n2. Toss with oil and salt.\n3. Bake or fry until crisp.",
                new String[][]{{"potato", "3", "pcs"}, {"oil", "2", "tbsp"}, {"salt", "1", "tsp"}});
        add(recipes, "13", "Fruit Salad", "Dessert",
                "1. Chop the apple and banana.\n2. Mix with sugar and a squeeze of lemon.",
                new String[][]{{"apple", "1", "pcs"}, {"banana", "2", "pcs"}, {"sugar", "1", "tbsp"}, {"lemon", "1", "pcs"}});
        add(recipes, "14", "Banana Pancakes", "Dessert",
                "1. Mash banana into flour, milk and egg.\n2. Cook spoonfuls until golden.",
                new String[][]{{"banana", "2", "pcs"}, {"flour", "150", "g"}, {"milk", "200", "ml"}, {"eggs", "1", "pcs"}});
        add(recipes, "15", "Cheese Omelette", "Breakfast",
                "1. Whisk eggs with salt.\n2. Cook in butter.\n3. Add cheese, fold and serve.",
                new String[][]{{"eggs", "3", "pcs"}, {"cheese", "2", "pcs"}, {"butter", "10", "g"}, {"salt", "1", "tsp"}});
        add(recipes, "16", "Onion Soup", "Soup",
                "1. Slowly caramelise onions in butter.\n2. Add water and salt, simmer.\n3. Serve hot.",
                new String[][]{{"onion", "3", "pcs"}, {"butter", "20", "g"}, {"salt", "1", "tsp"}, {"water", "500", "ml"}});
        add(recipes, "17", "Carrot Soup", "Soup",
                "1. Soften onion and carrot in oil.\n2. Add water and simmer until tender.\n3. Blend and season.",
                new String[][]{{"carrot", "4", "pcs"}, {"onion", "1", "pcs"}, {"oil", "1", "tbsp"}, {"water", "500", "ml"}, {"salt", "1", "tsp"}});
        add(recipes, "18", "Garlic Bread", "Snack",
                "1. Mix butter with crushed garlic.\n2. Spread on bread.\n3. Toast until crisp.",
                new String[][]{{"bread", "4", "pcs"}, {"butter", "30", "g"}, {"garlic", "3", "pcs"}});
        add(recipes, "19", "Egg Fried Noodles", "Main",
                "1. Boil the noodles.\n2. Stir-fry with egg, onion and soy sauce.",
                new String[][]{{"noodles", "200", "g"}, {"eggs", "2", "pcs"}, {"onion", "1", "pcs"}, {"soy sauce", "2", "tbsp"}, {"oil", "1", "tbsp"}});
        add(recipes, "20", "Apple Cinnamon Oats", "Breakfast",
                "1. Simmer oats in milk.\n2. Stir through grated apple, sugar and cinnamon.",
                new String[][]{{"oats", "80", "g"}, {"milk", "250", "ml"}, {"apple", "1", "pcs"}, {"sugar", "1", "tbsp"}, {"cinnamon", "1", "tsp"}});
        return recipes;
    }

    private static void add(List<Recipe> recipes, String id, String name, String category,
                            String steps, String[][] ingredients) {
        Recipe recipe = new Recipe(id, name, category, steps);
        for (String[] ingredient : ingredients) {
            recipe.addIngredient(new RecipeIngredient(ingredient[0],
                    Double.parseDouble(ingredient[1]), ingredient[2]));
        }
        recipes.add(recipe);
    }
}
