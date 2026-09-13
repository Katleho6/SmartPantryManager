package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.sql.SQLException;
import java.util.ArrayList;

public class RecipeDataSource {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public RecipeDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // Add a new recipe, and return its new id
    public long insertRecipe(String name, String steps) {
        ContentValues initialValues = new ContentValues();
        initialValues.put("name", name);
        initialValues.put("steps", steps);
        return database.insert("recipes", null, initialValues);
    }

    // Add one required ingredient for a recipe
    public void insertRecipeIngredient(int recipeId, String ingredientName, double quantity, String unit) {
        ContentValues initialValues = new ContentValues();
        initialValues.put("recipeid", recipeId);
        initialValues.put("ingredientname", ingredientName);
        initialValues.put("requiredquantity", quantity);
        initialValues.put("unit", unit);
        database.insert("recipe_ingredients", null, initialValues);
    }

    // Get every recipe in the database
    public ArrayList<Recipe> getAllRecipes() {
        ArrayList<Recipe> recipeList = new ArrayList<>();
        try {
            String query = "SELECT * FROM recipes";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Recipe recipe = new Recipe();
                recipe.setRecipeId(cursor.getInt(0));
                recipe.setName(cursor.getString(1));
                recipe.setSteps(cursor.getString(2));

                recipeList.add(recipe);
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            recipeList = new ArrayList<>();
        }
        return recipeList;
    }

    // Get every ingredient required by one specific recipe
    public ArrayList<RecipeIngredient> getIngredientsForRecipe(int recipeId) {
        ArrayList<RecipeIngredient> ingredientList = new ArrayList<>();
        try {
            String query = "SELECT * FROM recipe_ingredients WHERE recipeid=" + recipeId;
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                RecipeIngredient ri = new RecipeIngredient();
                ri.setId(cursor.getInt(0));
                ri.setRecipeId(cursor.getInt(1));
                ri.setIngredientName(cursor.getString(2));
                ri.setRequiredQuantity(cursor.getDouble(3));
                ri.setUnit(cursor.getString(4));

                ingredientList.add(ri);
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            ingredientList = new ArrayList<>();
        }
        return ingredientList;
    }

    // Check if any recipes exist yet (used to decide whether to seed data)
    public int getRecipeCount() {
        int count = 0;
        try {
            String query = "SELECT COUNT(*) FROM recipes";
            Cursor cursor = database.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            cursor.close();
        }
        catch (Exception e) {
            count = 0;
        }
        return count;
    }

    // Add all our starting recipes, but only if the database is empty
    public void seedRecipesIfEmpty() {
        if (getRecipeCount() > 0) {
            return; // recipes already loaded, don't add them again
        }

        long recipeId;

        recipeId = insertRecipe("Tomato Pasta", "Boil pasta. Fry garlic, add chopped tomato and salt. Mix with pasta.");
        insertRecipeIngredient((int) recipeId, "pasta", 200, "g");
        insertRecipeIngredient((int) recipeId, "tomato", 3, "pcs");
        insertRecipeIngredient((int) recipeId, "garlic", 2, "pcs");
        insertRecipeIngredient((int) recipeId, "salt", 5, "g");

        recipeId = insertRecipe("Garlic Butter Rice", "Cook rice. Fry garlic in butter, mix with rice and salt.");
        insertRecipeIngredient((int) recipeId, "rice", 300, "g");
        insertRecipeIngredient((int) recipeId, "garlic", 2, "pcs");
        insertRecipeIngredient((int) recipeId, "butter", 30, "g");
        insertRecipeIngredient((int) recipeId, "salt", 5, "g");

        recipeId = insertRecipe("Cheesy Scrambled Eggs", "Beat eggs, cook in butter, add cheese and salt near the end.");
        insertRecipeIngredient((int) recipeId, "egg", 3, "pcs");
        insertRecipeIngredient((int) recipeId, "cheese", 50, "g");
        insertRecipeIngredient((int) recipeId, "butter", 10, "g");
        insertRecipeIngredient((int) recipeId, "salt", 2, "g");

        recipeId = insertRecipe("Potato and Onion Fry", "Slice potato and onion, fry together with salt until soft.");
        insertRecipeIngredient((int) recipeId, "potato", 4, "pcs");
        insertRecipeIngredient((int) recipeId, "onion", 2, "pcs");
        insertRecipeIngredient((int) recipeId, "salt", 5, "g");

        recipeId = insertRecipe("Vegetable Stir Fry", "Chop all vegetables, stir fry together for 5-7 minutes.");
        insertRecipeIngredient((int) recipeId, "carrot", 2, "pcs");
        insertRecipeIngredient((int) recipeId, "bell pepper", 1, "pcs");
        insertRecipeIngredient((int) recipeId, "onion", 1, "pcs");
        insertRecipeIngredient((int) recipeId, "garlic", 1, "pcs");

        recipeId = insertRecipe("Chicken and Rice", "Cook rice. Fry chicken with onion and garlic, season with salt, combine.");
        insertRecipeIngredient((int) recipeId, "chicken breast", 300, "g");
        insertRecipeIngredient((int) recipeId, "rice", 200, "g");
        insertRecipeIngredient((int) recipeId, "onion", 1, "pcs");
        insertRecipeIngredient((int) recipeId, "garlic", 1, "pcs");
        insertRecipeIngredient((int) recipeId, "salt", 5, "g");

        recipeId = insertRecipe("Mashed Potatoes", "Boil potato until soft. Mash with butter, milk, and salt.");
        insertRecipeIngredient((int) recipeId, "potato", 5, "pcs");
        insertRecipeIngredient((int) recipeId, "butter", 30, "g");
        insertRecipeIngredient((int) recipeId, "milk", 100, "ml");
        insertRecipeIngredient((int) recipeId, "salt", 5, "g");

        recipeId = insertRecipe("Egg Fried Rice", "Cook rice. Scramble egg, mix in onion, carrot, and rice.");
        insertRecipeIngredient((int) recipeId, "rice", 300, "g");
        insertRecipeIngredient((int) recipeId, "egg", 2, "pcs");
        insertRecipeIngredient((int) recipeId, "onion", 1, "pcs");
        insertRecipeIngredient((int) recipeId, "carrot", 1, "pcs");

        recipeId = insertRecipe("Simple Omelette", "Beat eggs, pour into pan, add cheese and salt, fold in half.");
        insertRecipeIngredient((int) recipeId, "egg", 3, "pcs");
        insertRecipeIngredient((int) recipeId, "cheese", 30, "g");
        insertRecipeIngredient((int) recipeId, "salt", 2, "g");

        recipeId = insertRecipe("Tomato Soup", "Fry onion and garlic in butter. Add chopped tomato, salt, and simmer.");
        insertRecipeIngredient((int) recipeId, "tomato", 5, "pcs");
        insertRecipeIngredient((int) recipeId, "onion", 1, "pcs");
        insertRecipeIngredient((int) recipeId, "garlic", 1, "pcs");
        insertRecipeIngredient((int) recipeId, "butter", 20, "g");
        insertRecipeIngredient((int) recipeId, "salt", 5, "g");

        recipeId = insertRecipe("Pancakes", "Mix flour, milk, and egg into a batter. Fry spoonfuls in butter.");
        insertRecipeIngredient((int) recipeId, "flour", 200, "g");
        insertRecipeIngredient((int) recipeId, "milk", 250, "ml");
        insertRecipeIngredient((int) recipeId, "egg", 2, "pcs");
        insertRecipeIngredient((int) recipeId, "butter", 20, "g");

        recipeId = insertRecipe("Chicken Alfredo Pasta", "Cook pasta. Fry chicken, add milk and cheese to make a sauce, combine with pasta and butter.");
        insertRecipeIngredient((int) recipeId, "pasta", 200, "g");
        insertRecipeIngredient((int) recipeId, "chicken breast", 300, "g");
        insertRecipeIngredient((int) recipeId, "cheese", 100, "g");
        insertRecipeIngredient((int) recipeId, "milk", 100, "ml");
        insertRecipeIngredient((int) recipeId, "butter", 20, "g");

        recipeId = insertRecipe("Carrot Soup", "Fry onion in butter, add chopped carrot and milk, simmer until soft.");
        insertRecipeIngredient((int) recipeId, "carrot", 4, "pcs");
        insertRecipeIngredient((int) recipeId, "onion", 1, "pcs");
        insertRecipeIngredient((int) recipeId, "butter", 20, "g");
        insertRecipeIngredient((int) recipeId, "milk", 100, "ml");

        recipeId = insertRecipe("Cheese and Pepper Omelette", "Beat eggs, pour into pan, add cheese, bell pepper, and salt, fold in half.");
        insertRecipeIngredient((int) recipeId, "egg", 3, "pcs");
        insertRecipeIngredient((int) recipeId, "cheese", 40, "g");
        insertRecipeIngredient((int) recipeId, "bell pepper", 1, "pcs");
        insertRecipeIngredient((int) recipeId, "salt", 2, "g");

        recipeId = insertRecipe("Cheesy Mashed Potatoes", "Boil potato until soft. Mash with cheese, butter, milk, and salt.");
        insertRecipeIngredient((int) recipeId, "potato", 4, "pcs");
        insertRecipeIngredient((int) recipeId, "cheese", 40, "g");
        insertRecipeIngredient((int) recipeId, "butter", 20, "g");
        insertRecipeIngredient((int) recipeId, "milk", 50, "ml");
        insertRecipeIngredient((int) recipeId, "salt", 5, "g");

        recipeId = insertRecipe("Onion Garlic Rice", "Cook rice. Fry onion and garlic in butter, mix with rice and salt.");
        insertRecipeIngredient((int) recipeId, "rice", 250, "g");
        insertRecipeIngredient((int) recipeId, "onion", 1, "pcs");
        insertRecipeIngredient((int) recipeId, "garlic", 2, "pcs");
        insertRecipeIngredient((int) recipeId, "butter", 20, "g");
        insertRecipeIngredient((int) recipeId, "salt", 5, "g");

        recipeId = insertRecipe("Beef Stew", "Brown beef, add potato, carrot, and onion, simmer until tender.");
        insertRecipeIngredient((int) recipeId, "beef", 500, "g");
        insertRecipeIngredient((int) recipeId, "potato", 3, "pcs");
        insertRecipeIngredient((int) recipeId, "carrot", 2, "pcs");
        insertRecipeIngredient((int) recipeId, "onion", 1, "pcs");

        recipeId = insertRecipe("Fish Tacos", "Cook fish, place in tortilla with cabbage and a squeeze of lime.");
        insertRecipeIngredient((int) recipeId, "fish", 300, "g");
        insertRecipeIngredient((int) recipeId, "lime", 2, "pcs");
        insertRecipeIngredient((int) recipeId, "tortilla", 4, "pcs");
        insertRecipeIngredient((int) recipeId, "cabbage", 100, "g");

        recipeId = insertRecipe("Grilled Cheese Sandwich", "Butter bread, add cheese between slices, grill until golden.");
        insertRecipeIngredient((int) recipeId, "bread", 2, "pcs");
        insertRecipeIngredient((int) recipeId, "cheese", 50, "g");
        insertRecipeIngredient((int) recipeId, "butter", 10, "g");

        recipeId = insertRecipe("Rice Pudding", "Cook rice with milk and sugar over low heat until creamy.");
        insertRecipeIngredient((int) recipeId, "rice", 150, "g");
        insertRecipeIngredient((int) recipeId, "milk", 500, "ml");
        insertRecipeIngredient((int) recipeId, "sugar", 50, "g");
    }
}