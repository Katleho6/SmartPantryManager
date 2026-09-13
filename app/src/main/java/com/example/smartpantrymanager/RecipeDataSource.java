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

        recipeId = insertRecipe("Tomato Pasta", "Boil the pasta in salted water until soft, about 10 minutes, then drain. In a pan, fry the chopped garlic in a little oil on low heat until fragrant, about 1 minute. Add the chopped tomato and salt, and simmer on low heat for 8-10 minutes until it forms a sauce. Mix the sauce into the drained pasta and serve.");
        insertRecipeIngredient((int) recipeId, "pasta", 200, "g");
        insertRecipeIngredient((int) recipeId, "tomato", 3, "pcs");
        insertRecipeIngredient((int) recipeId, "garlic", 2, "pcs");
        insertRecipeIngredient((int) recipeId, "salt", 5, "g");

        recipeId = insertRecipe("Garlic Butter Rice", "Rinse the rice under cold water until the water runs clear. Cook the rice according to package instructions until tender. In a separate pan, melt the butter on low heat and fry the chopped garlic for 1-2 minutes until golden. Mix the garlic butter through the cooked rice, add salt to taste, and serve.");
        insertRecipeIngredient((int) recipeId, "rice", 300, "g");
        insertRecipeIngredient((int) recipeId, "garlic", 2, "pcs");
        insertRecipeIngredient((int) recipeId, "butter", 30, "g");
        insertRecipeIngredient((int) recipeId, "salt", 5, "g");

        recipeId = insertRecipe("Cheesy Scrambled Eggs", "Crack the eggs into a bowl and whisk well. Melt the butter in a non-stick pan on low-medium heat. Pour in the eggs and stir slowly and continuously with a spatula. When they begin to set but are still soft, add the grated cheese and salt. Keep stirring gently until the cheese melts and the eggs are just cooked through. Serve immediately.");
        insertRecipeIngredient((int) recipeId, "egg", 3, "pcs");
        insertRecipeIngredient((int) recipeId, "cheese", 50, "g");
        insertRecipeIngredient((int) recipeId, "butter", 10, "g");
        insertRecipeIngredient((int) recipeId, "salt", 2, "g");

        recipeId = insertRecipe("Potato and Onion Fry", "Peel and slice the potato into thin rounds. Slice the onion. Heat a little oil in a pan on medium heat, add the potato slices, and cover, turning occasionally, for about 10 minutes until soft. Add the onion and salt, and fry uncovered for another 5 minutes until the onion is soft and the potato is golden.");
        insertRecipeIngredient((int) recipeId, "potato", 4, "pcs");
        insertRecipeIngredient((int) recipeId, "onion", 2, "pcs");
        insertRecipeIngredient((int) recipeId, "salt", 5, "g");

        recipeId = insertRecipe("Vegetable Stir Fry", "Wash and chop the carrot, bell pepper, and onion into thin strips, and finely chop the garlic. Heat a little oil in a pan or wok on high heat. Add the garlic first and stir for 30 seconds, then add the rest of the vegetables. Stir-fry for 5-7 minutes, keeping the vegetables moving, until they are tender but still slightly crisp.");
        insertRecipeIngredient((int) recipeId, "carrot", 2, "pcs");
        insertRecipeIngredient((int) recipeId, "bell pepper", 1, "pcs");
        insertRecipeIngredient((int) recipeId, "onion", 1, "pcs");
        insertRecipeIngredient((int) recipeId, "garlic", 1, "pcs");

        recipeId = insertRecipe("Chicken and Rice", "Rinse the rice and cook according to package instructions. While the rice cooks, cut the chicken breast into small pieces. Heat oil in a pan on medium-high heat, add the chopped onion and garlic, and fry for 2 minutes until soft. Add the chicken and cook for 8-10 minutes, stirring occasionally, until fully cooked through and no longer pink inside. Season with salt, then combine with the cooked rice.");
        insertRecipeIngredient((int) recipeId, "chicken breast", 300, "g");
        insertRecipeIngredient((int) recipeId, "rice", 200, "g");
        insertRecipeIngredient((int) recipeId, "onion", 1, "pcs");
        insertRecipeIngredient((int) recipeId, "garlic", 1, "pcs");
        insertRecipeIngredient((int) recipeId, "salt", 5, "g");

        recipeId = insertRecipe("Mashed Potatoes", "Peel the potato and cut into evenly-sized chunks. Boil in salted water for 15-20 minutes until a fork slides in easily. Drain well. Mash the potato with a fork or masher while still hot. Add the butter and milk, and mash further until smooth and creamy. Season with salt to taste.");
        insertRecipeIngredient((int) recipeId, "potato", 5, "pcs");
        insertRecipeIngredient((int) recipeId, "butter", 30, "g");
        insertRecipeIngredient((int) recipeId, "milk", 100, "ml");
        insertRecipeIngredient((int) recipeId, "salt", 5, "g");

        recipeId = insertRecipe("Egg Fried Rice", "Cook the rice ahead of time and allow it to cool slightly (day-old rice works especially well). Chop the onion and carrot into small pieces. Beat the eggs in a bowl. Heat oil in a pan or wok on high heat, scramble the eggs until just set, then remove and set aside. In the same pan, fry the onion and carrot for 2-3 minutes, then add the rice and scrambled egg back in, stirring well until heated through.");
        insertRecipeIngredient((int) recipeId, "rice", 300, "g");
        insertRecipeIngredient((int) recipeId, "egg", 2, "pcs");
        insertRecipeIngredient((int) recipeId, "onion", 1, "pcs");
        insertRecipeIngredient((int) recipeId, "carrot", 1, "pcs");

        recipeId = insertRecipe("Simple Omelette", "Crack the eggs into a bowl, add a pinch of salt, and whisk until well combined. Heat a non-stick pan on medium heat. Pour in the egg mixture and let it sit for about 1 minute until the edges start to set. Sprinkle the grated cheese over one half. Once the egg is mostly set but still slightly soft on top, fold the omelette in half and cook for another 30 seconds before serving.");
        insertRecipeIngredient((int) recipeId, "egg", 3, "pcs");
        insertRecipeIngredient((int) recipeId, "cheese", 30, "g");
        insertRecipeIngredient((int) recipeId, "salt", 2, "g");

        recipeId = insertRecipe("Tomato Soup", "Finely chop the onion and garlic, and roughly chop the tomato. Melt the butter in a pot on medium heat, and fry the onion and garlic for 2-3 minutes until soft and fragrant. Add the chopped tomato and salt, and simmer on low heat for 15-20 minutes, stirring occasionally, until the tomato breaks down. For a smoother soup, blend before serving.");
        insertRecipeIngredient((int) recipeId, "tomato", 5, "pcs");
        insertRecipeIngredient((int) recipeId, "onion", 1, "pcs");
        insertRecipeIngredient((int) recipeId, "garlic", 1, "pcs");
        insertRecipeIngredient((int) recipeId, "butter", 20, "g");
        insertRecipeIngredient((int) recipeId, "salt", 5, "g");

        recipeId = insertRecipe("Pancakes", "In a bowl, whisk together the flour and milk until there are no lumps. Beat in the egg until the batter is smooth. Melt a little of the butter in a non-stick pan on medium heat. Pour in a small amount of batter per pancake and cook for 1-2 minutes until bubbles form on the surface, then flip and cook the other side for another minute until golden.");
        insertRecipeIngredient((int) recipeId, "flour", 200, "g");
        insertRecipeIngredient((int) recipeId, "milk", 250, "ml");
        insertRecipeIngredient((int) recipeId, "egg", 2, "pcs");
        insertRecipeIngredient((int) recipeId, "butter", 20, "g");

        recipeId = insertRecipe("Chicken Alfredo Pasta", "Cook the pasta in salted water according to package instructions, then drain. Cut the chicken breast into strips and fry in a pan with a little oil on medium-high heat for 8-10 minutes until fully cooked. In the same pan, melt the butter, add the milk and cheese, and stir on low heat until it forms a smooth sauce. Add the cooked pasta and chicken back into the pan and toss well to coat.");
        insertRecipeIngredient((int) recipeId, "pasta", 200, "g");
        insertRecipeIngredient((int) recipeId, "chicken breast", 300, "g");
        insertRecipeIngredient((int) recipeId, "cheese", 100, "g");
        insertRecipeIngredient((int) recipeId, "milk", 100, "ml");
        insertRecipeIngredient((int) recipeId, "butter", 20, "g");

        recipeId = insertRecipe("Carrot Soup", "Peel and chop the carrot, and finely chop the onion. Melt the butter in a pot on medium heat and fry the onion for 2-3 minutes until soft. Add the chopped carrot and milk, and simmer on low heat for 20 minutes until the carrot is very soft. Blend until smooth before serving.");
        insertRecipeIngredient((int) recipeId, "carrot", 4, "pcs");
        insertRecipeIngredient((int) recipeId, "onion", 1, "pcs");
        insertRecipeIngredient((int) recipeId, "butter", 20, "g");
        insertRecipeIngredient((int) recipeId, "milk", 100, "ml");

        recipeId = insertRecipe("Cheese and Pepper Omelette", "Crack the eggs into a bowl with a pinch of salt and whisk well. Finely chop the bell pepper. Heat a non-stick pan on medium heat and pour in the eggs. Once the edges begin to set, sprinkle the bell pepper and grated cheese over one half. Fold the omelette once the egg is mostly set, and cook for another 30 seconds before serving.");
        insertRecipeIngredient((int) recipeId, "egg", 3, "pcs");
        insertRecipeIngredient((int) recipeId, "cheese", 40, "g");
        insertRecipeIngredient((int) recipeId, "bell pepper", 1, "pcs");
        insertRecipeIngredient((int) recipeId, "salt", 2, "g");

        recipeId = insertRecipe("Cheesy Mashed Potatoes", "Peel and chop the potato into even chunks, then boil in salted water for 15-20 minutes until soft. Drain well and mash while hot. Stir in the butter, milk, and grated cheese, mashing until smooth and creamy. Season with salt to taste.");
        insertRecipeIngredient((int) recipeId, "potato", 4, "pcs");
        insertRecipeIngredient((int) recipeId, "cheese", 40, "g");
        insertRecipeIngredient((int) recipeId, "butter", 20, "g");
        insertRecipeIngredient((int) recipeId, "milk", 50, "ml");
        insertRecipeIngredient((int) recipeId, "salt", 5, "g");

        recipeId = insertRecipe("Onion Garlic Rice", "Rinse the rice and cook according to package instructions. While it cooks, finely chop the onion and garlic. Melt the butter in a pan on medium heat and fry the onion and garlic for 2-3 minutes until soft and fragrant. Mix through the cooked rice, season with salt, and serve.");
        insertRecipeIngredient((int) recipeId, "rice", 250, "g");
        insertRecipeIngredient((int) recipeId, "onion", 1, "pcs");
        insertRecipeIngredient((int) recipeId, "garlic", 2, "pcs");
        insertRecipeIngredient((int) recipeId, "butter", 20, "g");
        insertRecipeIngredient((int) recipeId, "salt", 5, "g");

        recipeId = insertRecipe("Beef Stew", "Cut the beef into cubes and peel and chop the potato, carrot, and onion. Heat oil in a large pot on medium-high heat and brown the beef on all sides, about 5 minutes. Add the onion and cook for 2 minutes, then add the potato, carrot, and enough water to cover everything. Cover and simmer on low heat for 1-1.5 hours until the beef is tender.");
        insertRecipeIngredient((int) recipeId, "beef", 500, "g");
        insertRecipeIngredient((int) recipeId, "potato", 3, "pcs");
        insertRecipeIngredient((int) recipeId, "carrot", 2, "pcs");
        insertRecipeIngredient((int) recipeId, "onion", 1, "pcs");

        recipeId = insertRecipe("Fish Tacos", "Season the fish and cook in a pan with a little oil on medium heat for 3-4 minutes per side until cooked through and flaky. Warm the tortillas briefly in a dry pan. Shred the cabbage finely. Flake the cooked fish into the warm tortillas, top with cabbage, and squeeze fresh lime juice over the top before serving.");
        insertRecipeIngredient((int) recipeId, "fish", 300, "g");
        insertRecipeIngredient((int) recipeId, "lime", 2, "pcs");
        insertRecipeIngredient((int) recipeId, "tortilla", 4, "pcs");
        insertRecipeIngredient((int) recipeId, "cabbage", 100, "g");

        recipeId = insertRecipe("Grilled Cheese Sandwich", "Butter one side of each slice of bread. Place one slice butter-side down in a pan on medium heat, layer the cheese on top, and cover with the second slice, butter-side up. Cook for 2-3 minutes until golden on the bottom, then carefully flip and cook the other side for another 2-3 minutes until the cheese has melted.");
        insertRecipeIngredient((int) recipeId, "bread", 2, "pcs");
        insertRecipeIngredient((int) recipeId, "cheese", 50, "g");
        insertRecipeIngredient((int) recipeId, "butter", 10, "g");

        recipeId = insertRecipe("Rice Pudding", "Rinse the rice. In a pot, combine the rice, milk, and sugar. Bring to a gentle simmer on low heat, stirring frequently to prevent sticking. Cook for 25-30 minutes, stirring often, until the rice is soft and the mixture has thickened to a creamy consistency.");
        insertRecipeIngredient((int) recipeId, "rice", 150, "g");
        insertRecipeIngredient((int) recipeId, "milk", 500, "ml");
        insertRecipeIngredient((int) recipeId, "sugar", 50, "g");
    }
}