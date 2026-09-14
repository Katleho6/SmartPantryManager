package com.example.smartpantrymanager;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Toast;
import java.util.ArrayList;

public class RecipeDetailActivity extends AppCompatActivity {

    private RecipeDataSource recipeDataSource;
    private TextView textViewRecipeName;
    private TextView textViewIngredients;
    private TextView textViewSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        recipeDataSource = new RecipeDataSource(this);

        textViewRecipeName = findViewById(R.id.textViewRecipeName);
        textViewIngredients = findViewById(R.id.textViewIngredients);
        textViewSteps = findViewById(R.id.textViewSteps);

        ImageButton buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> finish());

        int recipeId = getIntent().getIntExtra("recipeId", -1);
        String recipeName = getIntent().getStringExtra("recipeName");
        String recipeSteps = getIntent().getStringExtra("recipeSteps");

        textViewRecipeName.setText(recipeName);
        textViewSteps.setText(formatStepsAsNumberedList(recipeSteps));

        loadIngredients(recipeId);
    }

    // Splits the steps text into sentences and numbers them, one per line
    private String formatStepsAsNumberedList(String steps) {
        if (steps == null || steps.isEmpty()) {
            return "";
        }

        // Split the text into sentences wherever a period is followed by a space
        String[] sentences = steps.split("\\. ");

        StringBuilder numberedSteps = new StringBuilder();
        int stepNumber = 1;

        for (String sentence : sentences) {
            String cleanSentence = sentence.trim();

            if (!cleanSentence.isEmpty()) {
                // Add a period back at the end if it's missing (the split removes it)
                if (!cleanSentence.endsWith(".")) {
                    cleanSentence = cleanSentence + ".";
                }

                numberedSteps.append(stepNumber)
                        .append(". ")
                        .append(cleanSentence)
                        .append("\n\n");

                stepNumber = stepNumber + 1;
            }
        }

        return numberedSteps.toString();
    }

    private void loadIngredients(int recipeId) {
        try {
            recipeDataSource.open();
            ArrayList<RecipeIngredient> ingredients = recipeDataSource.getIngredientsForRecipe(recipeId);
            recipeDataSource.close();

            StringBuilder ingredientsText = new StringBuilder();
            for (RecipeIngredient ingredient : ingredients) {
                String formattedQuantity = formatQuantity(ingredient.getRequiredQuantity(), ingredient.getUnit());
                ingredientsText.append("- ")
                        .append(ingredient.getIngredientName())
                        .append(" (")
                        .append(formattedQuantity)
                        .append(" ")
                        .append(ingredient.getUnit())
                        .append(")\n");
            }

            textViewIngredients.setText(ingredientsText.toString());
        }
        catch (Exception e) {
            Toast.makeText(this, "Could not load ingredients", Toast.LENGTH_LONG).show();
        }
    }

    // Quantities are always shown as whole numbers, for simplicity
    private String formatQuantity(double quantity, String unit) {
        return String.valueOf((int) quantity);
    }
}