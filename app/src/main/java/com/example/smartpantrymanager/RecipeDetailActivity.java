package com.example.smartpantrymanager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView tvTitle, tvIngredients, tvInstructions;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        dbHelper = new DatabaseHelper(this);

        tvTitle = findViewById(R.id.tvDetailRecipeName);
        tvIngredients = findViewById(R.id.tvDetailIngredients);
        tvInstructions = findViewById(R.id.tvDetailInstructions);

        // Get data passed from suggestions activity
        int recipeId = getIntent().getIntExtra("RECIPE_ID", -1);
        String recipeName = getIntent().getStringExtra("RECIPE_NAME");
        String instructions = getIntent().getStringExtra("RECIPE_INSTRUCTIONS");

        tvTitle.setText(recipeName);
        tvInstructions.setText(instructions);

        // Load required ingredients from the database for this recipe
        loadRecipeIngredients(recipeId);
    }

    private void loadRecipeIngredients(int recipeId) {
        List<String> ingredients = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT ingredient_name FROM " + DatabaseHelper.TABLE_RECIPE_INGREDIENTS + " WHERE recipe_id = ?",
                new String[]{String.valueOf(recipeId)}
        );
        if (cursor.moveToFirst()) {
            do {
                String ing = cursor.getString(cursor.getColumnIndexOrThrow("ingredient_name"));
                ingredients.add("• " + ing);
            } while (cursor.moveToNext());
        }
        cursor.close();

        tvIngredients.setText(String.join("\n", ingredients));
    }
}