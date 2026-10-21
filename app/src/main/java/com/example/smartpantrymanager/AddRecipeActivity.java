package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class AddRecipeActivity extends AppCompatActivity {

    private EditText etRecipeName, etInstructions, etIngName, etIngQty, etIngUnit;
    private Button btnAddIngToList, btnSaveCustomRecipe;
    private TextView tvAddedIngredientsSummary;
    private DatabaseHelper dbHelper;

    private List<RecipeIngredientInput> tempIngredientsList;

    public static class RecipeIngredientInput {
        String name, unit;
        double quantity;

        public RecipeIngredientInput(String name, double quantity, String unit) {
            this.name = name;
            this.quantity = quantity;
            this.unit = unit;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        dbHelper = new DatabaseHelper(this);
        tempIngredientsList = new ArrayList<>();

        etRecipeName = findViewById(R.id.etCustomRecipeName);
        etInstructions = findViewById(R.id.etCustomInstructions);
        etIngName = findViewById(R.id.etIngName);
        etIngQty = findViewById(R.id.etIngQty);
        etIngUnit = findViewById(R.id.etIngUnit);
        btnAddIngToList = findViewById(R.id.btnAddIngredientToList);
        btnSaveCustomRecipe = findViewById(R.id.btnSaveCustomRecipe);
        tvAddedIngredientsSummary = findViewById(R.id.tvAddedIngredientsSummary);

        // Add individual ingredient to temporary list
        btnAddIngToList.setOnClickListener(v -> {
            String name = etIngName.getText().toString().trim().toLowerCase();
            String qtyStr = etIngQty.getText().toString().trim();
            String unit = etIngUnit.getText().toString().trim();

            if (name.isEmpty() || qtyStr.isEmpty()) {
                Toast.makeText(this, "Enter ingredient name and quantity", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double qty = Double.parseDouble(qtyStr);
                tempIngredientsList.add(new RecipeIngredientInput(name, qty, unit));
                updateIngredientsSummary();

                // Clear input fields for next entry
                etIngName.setText("");
                etIngQty.setText("");
                etIngUnit.setText("");
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid quantity format", Toast.LENGTH_SHORT).show();
            }
        });

        // Save complete recipe to database
        btnSaveCustomRecipe.setOnClickListener(v -> {
            String name = etRecipeName.getText().toString().trim();
            String instructions = etInstructions.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter a recipe name", Toast.LENGTH_SHORT).show();
                return;
            }

            if (tempIngredientsList.isEmpty()) {
                Toast.makeText(this, "Add at least one ingredient", Toast.LENGTH_SHORT).show();
                return;
            }

            // Convert and save
            long result = dbHelper.insertRecipeWithIngredients(name, instructions, convertIngredients(tempIngredientsList));

            if (result != -1) {
                Toast.makeText(this, "Recipe saved successfully!", Toast.LENGTH_SHORT).show();
                finish(); // Return to previous screen
            } else {
                Toast.makeText(this, "Error saving recipe", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateIngredientsSummary() {
        StringBuilder sb = new StringBuilder("Added Ingredients:\n");
        for (RecipeIngredientInput ing : tempIngredientsList) {
            sb.append("• ").append(ing.name).append(" (").append(ing.quantity).append(" ").append(ing.unit).append(")\n");
        }
        tvAddedIngredientsSummary.setText(sb.toString());
    }

    private List<DatabaseHelper.RecipeIngredient> convertIngredients(List<RecipeIngredientInput> inputList) {
        List<DatabaseHelper.RecipeIngredient> list = new ArrayList<>();
        for (RecipeIngredientInput item : inputList) {
            list.add(new DatabaseHelper.RecipeIngredient(item.name, item.quantity, item.unit));
        }
        return list;
    }
}