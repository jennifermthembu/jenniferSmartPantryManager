package com.richfield.smartpantry.ui;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.richfield.smartpantry.R;
import com.richfield.smartpantry.db.DatabaseHelper;
import com.richfield.smartpantry.model.Recipe;
import com.richfield.smartpantry.model.RecipeIngredient;

/**
 * Recipe Detail screen. Receives a recipe id through the launching Intent
 * (brief Section 3.1 - passing data between screens) and shows the full
 * ingredient list and preparation steps.
 */
public class RecipeDetailActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "extra_recipe_id";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        String recipeId = getIntent().getStringExtra(EXTRA_RECIPE_ID);
        if (recipeId == null) {
            finish();
            return;
        }
        DatabaseHelper db = DatabaseHelper.getInstance(this);
        db.getRecipe(recipeId, new DatabaseHelper.Callback<Recipe>() {
            @Override
            public void onSuccess(Recipe recipe) {
                if (isFinishing()) {
                    return;
                }
                if (recipe == null) {
                    finish();
                    return;
                }
                showRecipe(recipe);
            }

            @Override
            public void onError(@NonNull Exception error) {
                Toast.makeText(RecipeDetailActivity.this, error.getLocalizedMessage(),
                        Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void showRecipe(Recipe recipe) {
        setTitle(recipe.getName());
        ((TextView) findViewById(R.id.text_detail_name)).setText(recipe.getName());
        ((TextView) findViewById(R.id.text_detail_category)).setText(recipe.getCategory());

        StringBuilder ingredients = new StringBuilder();
        for (RecipeIngredient ing : recipe.getIngredients()) {
            ingredients.append("\u2022 ")
                    .append(trimNumber(ing.getQuantity()))
                    .append(' ')
                    .append(ing.getUnit())
                    .append(' ')
                    .append(ing.getName())
                    .append('\n');
        }
        ((TextView) findViewById(R.id.text_detail_ingredients))
                .setText(ingredients.toString().trim());
        ((TextView) findViewById(R.id.text_detail_steps)).setText(recipe.getSteps());
    }

    private String trimNumber(double value) {
        if (value == Math.floor(value)) {
            return String.valueOf((long) value);
        }
        return String.valueOf(value);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
