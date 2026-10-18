package com.example.smartpantrymanager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecipeSuggestionsActivity extends AppCompatActivity {

    private RecyclerView rvRecipes;
    private TextView tvNoRecipes;
    private DatabaseHelper dbHelper;
    private RecipeAdapter adapter;
    private List<Recipe> suggestedRecipesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_suggestions);

        dbHelper = new DatabaseHelper(this);
        rvRecipes = findViewById(R.id.rvRecipes);
        tvNoRecipes = findViewById(R.id.tvNoRecipes);

        rvRecipes.setLayoutManager(new LinearLayoutManager(this));

        loadSuggestedRecipes();
    }

    private void loadSuggestedRecipes() {
        suggestedRecipesList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // 1. Get all ingredient names currently in the user's pantry
        Set<String> pantryItems = new HashSet<>();
        Cursor pantryCursor = db.rawQuery("SELECT name FROM " + DatabaseHelper.TABLE_PANTRY, null);
        if (pantryCursor.moveToFirst()) {
            do {
                String itemName = pantryCursor.getString(pantryCursor.getColumnIndexOrThrow("name"));
                pantryItems.add(itemName.toLowerCase().trim());
            } while (pantryCursor.moveToNext());
        }
        pantryCursor.close();

        // 2. Get all recipes from the database using exact table columns: id, name, instructions
        Cursor recipeCursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_RECIPES, null);
        if (recipeCursor.moveToFirst()) {
            do {
                int recipeId = recipeCursor.getInt(recipeCursor.getColumnIndexOrThrow("id"));
                String recipeName = recipeCursor.getString(recipeCursor.getColumnIndexOrThrow("name"));
                String instructions = recipeCursor.getString(recipeCursor.getColumnIndexOrThrow("instructions"));

                // 3. Get required ingredients for this recipe
                List<String> requiredIngredients = new ArrayList<>();
                Cursor ingCursor = db.rawQuery(
                        "SELECT ingredient_name FROM " + DatabaseHelper.TABLE_RECIPE_INGREDIENTS + " WHERE recipe_id = ?",
                        new String[]{String.valueOf(recipeId)}
                );
                if (ingCursor.moveToFirst()) {
                    do {
                        String reqIng = ingCursor.getString(ingCursor.getColumnIndexOrThrow("ingredient_name"));
                        requiredIngredients.add(reqIng.toLowerCase().trim());
                    } while (ingCursor.moveToNext());
                }
                ingCursor.close();

                // 4. Strict-Matching Rule Check: User must possess ALL required ingredients
                boolean hasAllIngredients = true;
                if (requiredIngredients.isEmpty()) {
                    hasAllIngredients = false;
                } else {
                    for (String req : requiredIngredients) {
                        if (!pantryItems.contains(req)) {
                            hasAllIngredients = false;
                            break;
                        }
                    }
                }

                // If user has all items, add to suggested list
                if (hasAllIngredients) {
                    suggestedRecipesList.add(new Recipe(recipeId, recipeName, instructions));
                }

            } while (recipeCursor.moveToNext());
        }
        recipeCursor.close();

        // 5. Update UI based on whether matches were found
        if (suggestedRecipesList.isEmpty()) {
            tvNoRecipes.setVisibility(View.VISIBLE);
            rvRecipes.setVisibility(View.GONE);
        } else {
            tvNoRecipes.setVisibility(View.GONE);
            rvRecipes.setVisibility(View.VISIBLE);
        }

        adapter = new RecipeAdapter(suggestedRecipesList);
        rvRecipes.setAdapter(adapter);
    }
}