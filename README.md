# Smart Pantry Manager
A Java Android application that helps reduce food waste by tracking the ingredients a user currently has in their pantry, and suggesting recipes that can be made using strictly and only those ingredients — no shopping trip required, and no recipe suggested unless the user genuinely already has everything it needs.

## Features
- Add, edit, and delete pantry ingredients (name, quantity, unit, optional expiry date)
- A pantry list showing all current ingredients
- A pre-loaded collection of 20 recipes, each with a full ingredient list and preparation steps
- A Suggested Recipes screen that applies a strict-matching rule: a recipe is only suggested if every single required ingredient is present in the pantry, in sufficient quantity
- Matching is robust to simple unit differences (e.g. kilograms vs grams, litres vs millilitres) and simple plural ingredient names (e.g. "tomato" vs "tomatoes")
- A Recipe Detail screen showing full ingredients and numbered preparation steps
- A Settings screen with a toggle for expiring-soon alerts, which highlights pantry items due to expire within 3 days
- Bottom navigation between Pantry, Recipes, and Settings

## Database Choice: SQLite
This app uses **SQLite**, implemented locally on-device using `SQLiteOpenHelper`, rather than Firebase or PostgreSQL.

This was chosen because:
- It is built directly into Android, requiring no external services, accounts, or network connection
- It works fully offline, which suits an app that only needs to store one user's own pantry and recipe data
- It is the persistence approach covered in this module's database chapter
- It avoids the added complexity and potential connectivity issues of a cloud-based or externally-hosted database, which are unnecessary for this app's scope

## Database Structure
The app uses three tables:
- `pantry_items` — stores the user's current ingredients (standalone table)
- `recipes` — stores each recipe's name and preparation steps
- `recipe_ingredients` — stores each ingredient a recipe requires, linked to `recipes` by `recipe_id` (a one-to-many relationship: one recipe can have many required ingredients)

## Setup / Run Instructions
1. Clone this repository, or download it as a ZIP and extract it.
2. Open **Android Studio** (this project was built using a recent stable version of Android Studio).
3. Select **Open**, and choose the `SmartPantryManager` project folder.
4. Allow Android Studio to sync the project with Gradle (this happens automatically and may take a few minutes on first open).
5. Connect an Android device via USB with Developer Options and USB Debugging enabled, or start an emulator from **Device Manager**.
6. Click the green **Run** button to build and launch the app.
7. On first launch, the app automatically seeds its recipe database with 20 starting recipes — no further setup is required.

## Built With
- Java
- Android Studio
- SQLite (via `SQLiteOpenHelper`)