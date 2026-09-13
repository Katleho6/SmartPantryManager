package com.example.smartpantrymanager;

public class Recipe {
    private int recipeId;
    private String name;
    private String steps;

    public Recipe() {
        recipeId = -1;
    }

    public int getRecipeId() {
        return recipeId;
    }
    public void setRecipeId(int i) {
        recipeId = i;
    }
    public String getName() {
        return name;
    }
    public void setName(String s) {
        name = s;
    }
    public String getSteps() {
        return steps;
    }
    public void setSteps(String s) {
        steps = s;
    }
}