package com.example.smartpantrymanager;

public class RecipeIngredient {
    private int id;
    private int recipeId;
    private String ingredientName;
    private double requiredQuantity;
    private String unit;

    public RecipeIngredient() {
        id = -1;
    }

    public int getId() {
        return id;
    }
    public void setId(int i) {
        id = i;
    }
    public int getRecipeId() {
        return recipeId;
    }
    public void setRecipeId(int i) {
        recipeId = i;
    }
    public String getIngredientName() {
        return ingredientName;
    }
    public void setIngredientName(String s) {
        ingredientName = s;
    }
    public double getRequiredQuantity() {
        return requiredQuantity;
    }
    public void setRequiredQuantity(double q) {
        requiredQuantity = q;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String s) {
        unit = s;
    }
}