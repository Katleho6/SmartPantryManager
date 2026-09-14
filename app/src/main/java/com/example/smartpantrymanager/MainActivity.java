package com.example.smartpantrymanager;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.content.Intent;
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

        ArrayList<String> displayList = new ArrayList<>();
        for (PantryItem item : pantryItems) {
            String formattedQuantity = formatQuantity(item.getQuantity(), item.getUnit());
            displayList.add(item.getName() + " - " + formattedQuantity + " " + item.getUnit());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, displayList);
        listViewPantry.setAdapter(adapter);
    }

    private String formatQuantity(double quantity, String unit) {
        return String.valueOf((int) quantity);
    }
}