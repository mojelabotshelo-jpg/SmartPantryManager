package com.example.smartpantrymanager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvPantry;
    private TextView tvEmptyPantry;
    private Button btnAddIngredient, btnViewSuggestions, btnSettings;
    private DatabaseHelper dbHelper;
    private PantryAdapter adapter;
    private List<PantryItem> pantryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize SQLite Database Helper
        dbHelper = new DatabaseHelper(this);

        // Connect layout XML views to Java variables
        rvPantry = findViewById(R.id.rvPantry);
        tvEmptyPantry = findViewById(R.id.tvEmptyPantry);
        btnAddIngredient = findViewById(R.id.btnAddIngredient);
        btnViewSuggestions = findViewById(R.id.btnViewSuggestions);
        btnSettings = findViewById(R.id.btnSettings);

        rvPantry.setLayoutManager(new LinearLayoutManager(this));

        // Navigate to Add Ingredient Screen
        btnAddIngredient.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditIngredientActivity.class);
            startActivity(intent);
        });

        // Navigate to Recipe Suggestions Screen (Strict-Matching Engine)
        btnViewSuggestions.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RecipeSuggestionsActivity.class);
            startActivity(intent);
        });

        // Settings placeholder action
        btnSettings.setOnClickListener(v -> {
            Toast.makeText(this, "Settings screen coming soon!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPantryItems(); // Refresh pantry list every time we return to this screen
    }

    private void loadPantryItems() {
        pantryList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_PANTRY, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                double qty = cursor.getDouble(cursor.getColumnIndexOrThrow("quantity"));
                String unit = cursor.getString(cursor.getColumnIndexOrThrow("unit"));
                String expiry = cursor.getString(cursor.getColumnIndexOrThrow("expiry_date"));

                pantryList.add(new PantryItem(id, name, qty, unit, expiry));
            } while (cursor.moveToNext());
        }
        cursor.close();

        // Show/hide empty message vs RecyclerView list
        if (pantryList.isEmpty()) {
            tvEmptyPantry.setVisibility(View.VISIBLE);
            rvPantry.setVisibility(View.GONE);
        } else {
            tvEmptyPantry.setVisibility(View.GONE);
            rvPantry.setVisibility(View.VISIBLE);
        }

        // Set up adapter with both delete action and item click for updating quantity
        adapter = new PantryAdapter(
                pantryList,
                id -> deletePantryItem(id),
                item -> showUpdateQuantityDialog(item)
        );
        rvPantry.setAdapter(adapter);
    }

    private void deletePantryItem(int id) {
        dbHelper.deletePantryItem(id);
        Toast.makeText(this, "Item deleted from pantry", Toast.LENGTH_SHORT).show();
        loadPantryItems(); // Reload the list
    }

    private void showUpdateQuantityDialog(PantryItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Quantity: " + item.getName());

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setText(String.valueOf(item.getQuantity()));
        input.setPadding(50, 40, 50, 40);
        builder.setView(input);

        builder.setPositiveButton("Update", (dialog, which) -> {
            String qtyStr = input.getText().toString().trim();
            if (!qtyStr.isEmpty()) {
                try {
                    double newQuantity = Double.parseDouble(qtyStr);
                    dbHelper.updatePantryQuantity(item.getId(), newQuantity);
                    Toast.makeText(this, "Quantity updated successfully", Toast.LENGTH_SHORT).show();
                    loadPantryItems(); // Refresh the list
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Invalid number format", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}