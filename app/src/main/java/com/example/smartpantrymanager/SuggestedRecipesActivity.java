package com.example.smartpantrymanager;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Intent;
import java.util.ArrayList;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private PantryDataSource pantryDataSource;
    private RecipeDataSource recipeDataSource;
    private ListView listViewSuggestedRecipes;
    private ArrayList<Recipe> matchingRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);

        pantryDataSource = new PantryDataSource(this);
        recipeDataSource = new RecipeDataSource(this);

        listViewSuggestedRecipes = findViewById(R.id.listViewSuggestedRecipes);

        findSuggestedRecipes();

        listViewSuggestedRecipes.setOnItemClickListener((parent, view, position, id) -> {
            if (matchingRecipes.isEmpty()) {
                return;
            }
            Recipe selectedRecipe = matchingRecipes.get(position);

            Intent intent = new Intent(SuggestedRecipesActivity.this, RecipeDetailActivity.class);
            intent.putExtra("recipeId", selectedRecipe.getRecipeId());
            intent.putExtra("recipeName", selectedRecipe.getName());
            intent.putExtra("recipeSteps", selectedRecipe.getSteps());
            startActivity(intent);
        });
    }

    private void findSuggestedRecipes() {
        matchingRecipes = new ArrayList<>();

        try {
            pantryDataSource.open();
            recipeDataSource.open();

            ArrayList<PantryItem> pantryItems = pantryDataSource.getAllPantryItems();
            ArrayList<Recipe> allRecipes = recipeDataSource.getAllRecipes();

            // Check every recipe, one at a time
            for (Recipe recipe : allRecipes) {
                ArrayList<RecipeIngredient> requiredIngredients =
                        recipeDataSource.getIngredientsForRecipe(recipe.getRecipeId());

                if (pantryHasAllIngredients(pantryItems, requiredIngredients)) {
                    matchingRecipes.add(recipe);
                }
            }

            pantryDataSource.close();
            recipeDataSource.close();
        }
        catch (Exception e) {
            Toast.makeText(this, "Could not load suggested recipes", Toast.LENGTH_LONG).show();
        }

        displayMatchingRecipes();
    }

    // This is the strict-matching rule: every single required ingredient
    // must be found in the pantry, in enough quantity, or the recipe fails
    private boolean pantryHasAllIngredients(ArrayList<PantryItem> pantryItems,
                                            ArrayList<RecipeIngredient> requiredIngredients) {

        for (RecipeIngredient required : requiredIngredients) {

            boolean thisIngredientFound = false;

            for (PantryItem pantryItem : pantryItems) {

                boolean namesMatch = normalizeName(pantryItem.getName())
                        .equals(normalizeName(required.getIngredientName()));

                String pantryCategory = getUnitCategory(pantryItem.getUnit());
                String requiredCategory = getUnitCategory(required.getUnit());
                boolean sameUnitFamily = pantryCategory.equals(requiredCategory);

                double pantryAmount = convertToBaseAmount(pantryItem.getQuantity(), pantryItem.getUnit());
                double requiredAmount = convertToBaseAmount(required.getRequiredQuantity(), required.getUnit());
                boolean enoughQuantity = pantryAmount >= requiredAmount;

                if (namesMatch && sameUnitFamily && enoughQuantity) {
                    thisIngredientFound = true;
                    break;
                }
            }

            // If even ONE required ingredient was not found, the whole recipe fails
            if (!thisIngredientFound) {
                return false;
            }
        }

        // Every required ingredient was found - this recipe qualifies
        return true;
    }

    // Groups units into families, so kg/g are treated as the same type,
    // and L/ml are treated as the same type
    private String getUnitCategory(String unit) {
        if (unit == null) {
            return "";
        }
        String u = unit.toLowerCase().trim();

        if (u.equals("kg") || u.equals("g")) {
            return "mass";
        }
        else if (u.equals("l") || u.equals("ml")) {
            return "volume";
        }
        else {
            return u; // pcs, cups etc. stay their own category
        }
    }

    // Converts kg to grams, and L to millilitres, so amounts can be compared fairly.
    // Units that are not mass or volume (like pcs) are returned unchanged.
    private double convertToBaseAmount(double quantity, String unit) {
        if (unit == null) {
            return quantity;
        }
        String u = unit.toLowerCase().trim();

        if (u.equals("kg")) {
            return quantity * 1000;
        }
        else if (u.equals("l")) {
            return quantity * 1000;
        }
        else {
            return quantity;
        }
    }

    // Makes ingredient names easier to compare:
    // lowercase, trimmed, and simple plural "s" removed
    private String normalizeName(String name) {
        String result = name.toLowerCase().trim();

        if (result.endsWith("es")) {
            result = result.substring(0, result.length() - 2);
        }
        else if (result.endsWith("s")) {
            result = result.substring(0, result.length() - 1);
        }

        return result;
    }

    private void displayMatchingRecipes() {
        ArrayList<String> displayList = new ArrayList<>();

        for (Recipe recipe : matchingRecipes) {
            displayList.add(recipe.getName());
        }

        if (displayList.isEmpty()) {
            displayList.add("No recipes match your pantry yet - add more ingredients");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, displayList);
        listViewSuggestedRecipes.setAdapter(adapter);
    }
}