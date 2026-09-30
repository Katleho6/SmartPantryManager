package com.example.smartpantrymanager;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.content.Intent;
import android.content.SharedPreferences;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private PantryDataSource dataSource;
    private ListView listViewPantry;
    private ArrayList<PantryItem> pantryItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataSource = new PantryDataSource(this);

        try {
            RecipeDataSource recipeDataSource = new RecipeDataSource(this);
            recipeDataSource.open();
            recipeDataSource.seedRecipesIfEmpty();
            recipeDataSource.close();
        }
        catch (Exception e) {
            Toast.makeText(this, "Could not load recipes", Toast.LENGTH_LONG).show();
        }

        listViewPantry = findViewById(R.id.listViewPantry);

        Button buttonAddItem = findViewById(R.id.buttonAddItem);
        buttonAddItem.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditItemActivity.class);
            startActivity(intent);
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_recipes) {
                Intent intent = new Intent(MainActivity.this, SuggestedRecipesActivity.class);
                startActivity(intent);
                return true;
            }
            else if (itemId == R.id.nav_settings) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            }
            else {
                // nav_pantry - already on this screen, do nothing
                return true;
            }
        });

        // When a list item is tapped, open it for editing
        listViewPantry.setOnItemClickListener((parent, view, position, id) -> {
            PantryItem selectedItem = pantryItems.get(position);

            Intent intent = new Intent(MainActivity.this, AddEditItemActivity.class);
            intent.putExtra("itemId", selectedItem.getItemId());
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPantryItems();
    }

    private void loadPantryItems() {
        try {
            dataSource.open();
            pantryItems = dataSource.getAllPantryItems();
            dataSource.close();
        }
        catch (Exception e) {
            Toast.makeText(this, "Could not load pantry items", Toast.LENGTH_LONG).show();
        }

        SharedPreferences preferences = getSharedPreferences("SmartPantryPrefs", MODE_PRIVATE);
        boolean alertsEnabled = preferences.getBoolean("expiryAlertsEnabled", true);

        ArrayList<String> detailsList = new ArrayList<>();
        ArrayList<Boolean> expiringSoonFlags = new ArrayList<>();

        for (PantryItem item : pantryItems) {
            String formattedQuantity = formatQuantity(item.getQuantity(), item.getUnit());
            detailsList.add(item.getName() + " - " + formattedQuantity + " " + item.getUnit());

            boolean isExpiring = alertsEnabled && isExpiringSoon(item.getExpiryDate());
            expiringSoonFlags.add(isExpiring);
        }

        PantryListAdapter adapter = new PantryListAdapter(this, detailsList, expiringSoonFlags);
        listViewPantry.setAdapter(adapter);
    }

    // Checks if a date string (YYYY-MM-DD) is today or within the next 3 days
    private boolean isExpiringSoon(String expiryDate) {
        if (expiryDate == null || expiryDate.isEmpty()) {
            return false;
        }

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date expiry = format.parse(expiryDate);
            Date today = new Date();

            long millisecondsUntilExpiry = expiry.getTime() - today.getTime();
            long daysUntilExpiry = millisecondsUntilExpiry / (1000 * 60 * 60 * 24);

            return daysUntilExpiry >= 0 && daysUntilExpiry <= 3;
        }
        catch (Exception e) {
            return false;
        }
    }

    private String formatQuantity(double quantity, String unit) {
        return String.valueOf((int) quantity);
    }
}