package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Name and Version
    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 2; // Incremented to force clean creation/upgrade

    // Table Names
    public static final String TABLE_PANTRY = "pantry";
    public static final String TABLE_RECIPES = "recipes";
    public static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 1. Create Pantry Table
        String createPantryTable = "CREATE TABLE " + TABLE_PANTRY + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "quantity REAL NOT NULL, " +
                "unit TEXT, " +
                "expiry_date TEXT)";
        db.execSQL(createPantryTable);

        // 2. Create Recipes Table
        String createRecipesTable = "CREATE TABLE " + TABLE_RECIPES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "instructions TEXT)";
        db.execSQL(createRecipesTable);

        // 3. Create Recipe Ingredients Table
        String createRecipeIngredientsTable = "CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "recipe_id INTEGER, " +
                "ingredient_name TEXT NOT NULL, " +
                "quantity REAL NOT NULL, " +
                "unit TEXT, " +
                "FOREIGN KEY(recipe_id) REFERENCES " + TABLE_RECIPES + "(id) ON DELETE CASCADE)";
        db.execSQL(createRecipeIngredientsTable);

        // Seed initial recipes into the database on first load
        seedRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Helper method to seed 15 initial recipes and their required ingredients
    private void seedRecipes(SQLiteDatabase db) {
        // Recipe 1
        long r1 = addRecipe(db, "Tomato Pasta", "Boil pasta in salted water. Sauté diced tomatoes and minced garlic in a pan. Mix together and serve warm.");
        addRecipeIngredient(db, r1, "pasta", 200.0, "grams");
        addRecipeIngredient(db, r1, "tomato", 3.0, "units");
        addRecipeIngredient(db, r1, "garlic", 2.0, "cloves");

        // Recipe 2
        long r2 = addRecipe(db, "Scrambled Eggs on Toast", "Toast the bread slices. Whisk eggs with a pinch of salt, cook in butter over low heat until soft. Serve on toast.");
        addRecipeIngredient(db, r2, "egg", 3.0, "units");
        addRecipeIngredient(db, r2, "bread", 2.0, "slices");
        addRecipeIngredient(db, r2, "butter", 15.0, "grams");

        // Recipe 3
        long r3 = addRecipe(db, "Simple Omelette", "Beat eggs in a bowl. Melt butter in a non-stick frying pan, pour eggs and cook until set. Fold in half and serve.");
        addRecipeIngredient(db, r3, "egg", 2.0, "units");
        addRecipeIngredient(db, r3, "butter", 10.0, "grams");

        // Recipe 4
        long r4 = addRecipe(db, "Garlic Fried Rice", "Heat oil, sauté minced garlic until light golden brown. Add cooked rice and stir thoroughly.");
        addRecipeIngredient(db, r4, "rice", 150.0, "grams");
        addRecipeIngredient(db, r4, "garlic", 3.0, "cloves");

        // Recipe 5
        long r5 = addRecipe(db, "Basic Pancakes", "Mix flour, milk, and egg into a smooth batter. Pour small portions into a hot pan and flip when bubbles appear.");
        addRecipeIngredient(db, r5, "flour", 200.0, "grams");
        addRecipeIngredient(db, r5, "milk", 250.0, "ml");
        addRecipeIngredient(db, r5, "egg", 1.0, "units");

        // Recipe 6
        long r6 = addRecipe(db, "Garlic Toast", "Spread butter and minced garlic over bread slices. Bake or toast until crisp.");
        addRecipeIngredient(db, r6, "bread", 2.0, "slices");
        addRecipeIngredient(db, r6, "butter", 10.0, "grams");
        addRecipeIngredient(db, r6, "garlic", 1.0, "cloves");

        // Recipe 7
        long r7 = addRecipe(db, "Tomato Soup", "Simmer chopped tomatoes and garlic in water with salt. Blend until smooth.");
        addRecipeIngredient(db, r7, "tomato", 5.0, "units");
        addRecipeIngredient(db, r7, "garlic", 2.0, "cloves");
        addRecipeIngredient(db, r7, "salt", 5.0, "grams");

        // Recipe 8
        long r8 = addRecipe(db, "French Toast", "Whisk egg and milk. Dip bread slices into mixture and cook in buttered pan until golden.");
        addRecipeIngredient(db, r8, "bread", 2.0, "slices");
        addRecipeIngredient(db, r8, "egg", 1.0, "units");
        addRecipeIngredient(db, r8, "milk", 50.0, "ml");
        addRecipeIngredient(db, r8, "butter", 10.0, "grams");

        // Recipe 9
        long r9 = addRecipe(db, "Boiled Rice and Tomato", "Boil rice until soft. Serve topped with fresh sautéed tomatoes.");
        addRecipeIngredient(db, r9, "rice", 150.0, "grams");
        addRecipeIngredient(db, r9, "tomato", 2.0, "units");

        // Recipe 10
        long r10 = addRecipe(db, "Boiled Eggs", "Place eggs in boiling water for 7 minutes. Remove, peel, and season with salt.");
        addRecipeIngredient(db, r10, "egg", 2.0, "units");
        addRecipeIngredient(db, r10, "salt", 2.0, "grams");

        // Recipe 11
        long r11 = addRecipe(db, "Garlic Butter Pasta", "Boil pasta. Toss hot pasta with butter, minced garlic, and salt.");
        addRecipeIngredient(db, r11, "pasta", 200.0, "grams");
        addRecipeIngredient(db, r11, "butter", 20.0, "grams");
        addRecipeIngredient(db, r11, "garlic", 2.0, "cloves");

        // Recipe 12
        long r12 = addRecipe(db, "Sautéed Tomatoes", "Slice tomatoes and sauté in butter with garlic and salt for 5 minutes.");
        addRecipeIngredient(db, r12, "tomato", 3.0, "units");
        addRecipeIngredient(db, r12, "butter", 10.0, "grams");
        addRecipeIngredient(db, r12, "garlic", 1.0, "cloves");

        // Recipe 13
        long r13 = addRecipe(db, "Rice Porridge", "Boil rice with milk and salt over low heat until creamed.");
        addRecipeIngredient(db, r13, "rice", 100.0, "grams");
        addRecipeIngredient(db, r13, "milk", 300.0, "ml");

        // Recipe 14
        long r14 = addRecipe(db, "Flatbread Batter", "Mix flour and milk with salt. Cook thin layers on a hot skillet.");
        addRecipeIngredient(db, r14, "flour", 150.0, "grams");
        addRecipeIngredient(db, r14, "milk", 150.0, "ml");

        // Recipe 15
        long r15 = addRecipe(db, "Fried Egg Rice", "Fry egg in oil, scramble, and mix in cooked rice with salt.");
        addRecipeIngredient(db, r15, "egg", 2.0, "units");
        addRecipeIngredient(db, r15, "rice", 150.0, "grams");
    }

    private long addRecipe(SQLiteDatabase db, String name, String instructions) {
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("instructions", instructions);
        return db.insert(TABLE_RECIPES, null, cv);
    }

    private void addRecipeIngredient(SQLiteDatabase db, long recipeId, String name, double qty, String unit) {
        ContentValues cv = new ContentValues();
        cv.put("recipe_id", recipeId);
        cv.put("ingredient_name", name.toLowerCase().trim());
        cv.put("quantity", qty);
        cv.put("unit", unit);
        db.insert(TABLE_RECIPE_INGREDIENTS, null, cv);
    }
}