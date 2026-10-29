package com.richfield.smartpantry.db;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.richfield.smartpantry.model.PantryItem;
import com.richfield.smartpantry.model.Recipe;
import com.richfield.smartpantry.model.RecipeIngredient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Firestore persistence for pantry items and the shared recipe collection. */
public class DatabaseHelper {

    public interface Callback<T> {
        void onSuccess(T value);
        void onError(@NonNull Exception error);
    }

    public static final String T_PANTRY = "pantry_items";
    public static final String T_RECIPE = "recipes";

    private static DatabaseHelper instance;
    private final FirebaseFirestore firestore;
    private final FirebaseAuth auth;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper();
        }
        return instance;
    }

    private DatabaseHelper() {
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    private void withAuthentication(Callback<Void> callback, Runnable operation) {
        if (auth.getCurrentUser() == null) {
            callback.onError(new IllegalStateException("Sign in is required to access Firestore."));
            return;
        }
        operation.run();
    }

    public void addPantryItem(PantryItem item, Callback<String> callback) {
        withAuthentication(new Callback<Void>() {
            @Override
            public void onSuccess(Void ignored) {
            }

            @Override
            public void onError(@NonNull Exception error) {
                callback.onError(error);
            }
        }, () -> {
            Map<String, Object> values = pantryValues(item);
            firestore.collection(T_PANTRY).add(values)
                    .addOnSuccessListener(reference -> callback.onSuccess(reference.getId()))
                    .addOnFailureListener(callback::onError);
        });
    }

    public void updatePantryItem(PantryItem item, Callback<Void> callback) {
        withAuthentication(new Callback<Void>() {
            @Override
            public void onSuccess(Void ignored) {
            }

            @Override
            public void onError(@NonNull Exception error) {
                callback.onError(error);
            }
        }, () -> firestore.collection(T_PANTRY).document(item.getId())
                .set(pantryValues(item))
                .addOnSuccessListener(unused -> callback.onSuccess(null))
                .addOnFailureListener(callback::onError));
    }

    public void deletePantryItem(String id, Callback<Void> callback) {
        withAuthentication(new Callback<Void>() {
            @Override
            public void onSuccess(Void ignored) {
            }

            @Override
            public void onError(@NonNull Exception error) {
                callback.onError(error);
            }
        }, () -> firestore.collection(T_PANTRY).document(id).delete()
                .addOnSuccessListener(unused -> callback.onSuccess(null))
                .addOnFailureListener(callback::onError));
    }

    public void getAllPantryItems(Callback<List<PantryItem>> callback) {
        withAuthentication(new Callback<Void>() {
            @Override
            public void onSuccess(Void ignored) {
            }

            @Override
            public void onError(@NonNull Exception error) {
                callback.onError(error);
            }
        }, () -> firestore.collection(T_PANTRY).orderBy("name").get()
                .addOnSuccessListener(snapshot -> {
                    List<PantryItem> items = new ArrayList<>();
                    for (DocumentSnapshot document : snapshot.getDocuments()) {
                        items.add(readPantry(document));
                    }
                    callback.onSuccess(items);
                })
                .addOnFailureListener(callback::onError));
    }

    public void getPantryItem(String id, Callback<PantryItem> callback) {
        withAuthentication(new Callback<Void>() {
            @Override
            public void onSuccess(Void ignored) {
            }

            @Override
            public void onError(@NonNull Exception error) {
                callback.onError(error);
            }
        }, () -> firestore.collection(T_PANTRY).document(id).get()
                .addOnSuccessListener(document -> callback.onSuccess(
                        document.exists() ? readPantry(document) : null))
                .addOnFailureListener(callback::onError));
    }

    public void getAllRecipes(Callback<List<Recipe>> callback) {
        withAuthentication(new Callback<Void>() {
            @Override
            public void onSuccess(Void ignored) {
            }

            @Override
            public void onError(@NonNull Exception error) {
                callback.onError(error);
            }
        }, () -> seedRecipesIfEmpty(callback));
    }

    private void seedRecipesIfEmpty(Callback<List<Recipe>> callback) {
        firestore.collection(T_RECIPE).limit(1).get()
                .addOnSuccessListener(snapshot -> {
                    if (!snapshot.isEmpty()) {
                        loadRecipes(callback);
                        return;
                    }
                    List<Recipe> recipes = SeedData.recipes();
                    com.google.firebase.firestore.WriteBatch batch = firestore.batch();
                    for (Recipe recipe : recipes) {
                        batch.set(firestore.collection(T_RECIPE).document(recipe.getId()),
                                recipeValues(recipe));
                    }
                    batch.commit()
                            .addOnSuccessListener(unused -> loadRecipes(callback))
                            .addOnFailureListener(callback::onError);
                })
                .addOnFailureListener(callback::onError);
    }

    private void loadRecipes(Callback<List<Recipe>> callback) {
        firestore.collection(T_RECIPE).orderBy("name").get()
                .addOnSuccessListener(snapshot -> {
                    List<Recipe> recipes = new ArrayList<>();
                    for (QueryDocumentSnapshot document : snapshot) {
                        recipes.add(readRecipe(document));
                    }
                    callback.onSuccess(recipes);
                })
                .addOnFailureListener(callback::onError);
    }

    public void getRecipe(String id, Callback<Recipe> callback) {
        withAuthentication(new Callback<Void>() {
            @Override
            public void onSuccess(Void ignored) {
            }

            @Override
            public void onError(@NonNull Exception error) {
                callback.onError(error);
            }
        }, () -> firestore.collection(T_RECIPE).document(id).get()
                .addOnSuccessListener(document -> callback.onSuccess(
                        document.exists() ? readRecipe(document) : null))
                .addOnFailureListener(callback::onError));
    }

    private Map<String, Object> pantryValues(PantryItem item) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", item.getName());
        values.put("quantity", item.getQuantity());
        values.put("unit", item.getUnit());
        values.put("expiryDate", item.getExpiryDate());
        return values;
    }

    private PantryItem readPantry(DocumentSnapshot document) {
        Number quantity = document.getDouble("quantity");
        return new PantryItem(document.getId(), document.getString("name"),
                quantity == null ? 1 : quantity.doubleValue(),
                document.getString("unit"), document.getString("expiryDate"));
    }

    private Map<String, Object> recipeValues(Recipe recipe) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", recipe.getName());
        values.put("category", recipe.getCategory());
        values.put("steps", recipe.getSteps());
        List<Map<String, Object>> ingredients = new ArrayList<>();
        for (RecipeIngredient ingredient : recipe.getIngredients()) {
            Map<String, Object> value = new HashMap<>();
            value.put("name", ingredient.getName());
            value.put("quantity", ingredient.getQuantity());
            value.put("unit", ingredient.getUnit());
            ingredients.add(value);
        }
        values.put("ingredients", ingredients);
        return values;
    }

    @SuppressWarnings("unchecked")
    private Recipe readRecipe(DocumentSnapshot document) {
        Recipe recipe = new Recipe(document.getId(), document.getString("name"),
                document.getString("category"), document.getString("steps"));
        List<Map<String, Object>> ingredients =
                (List<Map<String, Object>>) document.get("ingredients");
        if (ingredients != null) {
            for (Map<String, Object> ingredient : ingredients) {
                Object quantityValue = ingredient.get("quantity");
                double quantity = quantityValue instanceof Number
                        ? ((Number) quantityValue).doubleValue() : 1;
                recipe.addIngredient(new RecipeIngredient(
                        (String) ingredient.get("name"), quantity,
                        (String) ingredient.get("unit")));
            }
        }
        return recipe;
    }
}
