package com.example.smartpantrymanager;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddEditItemActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextQuantity;
    private EditText editTextUnit;
    private EditText editTextExpiry;
    private PantryDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_item);

        dataSource = new PantryDataSource(this);

        editTextName = findViewById(R.id.editTextName);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        editTextUnit = findViewById(R.id.editTextUnit);
        editTextExpiry = findViewById(R.id.editTextExpiry);

        Button buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(v -> saveItem());
    }

    private void saveItem() {
        String name = editTextName.getText().toString().trim();
        String quantityText = editTextQuantity.getText().toString().trim();
        String unit = editTextUnit.getText().toString().trim();
        String expiry = editTextExpiry.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter an ingredient name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (quantityText.isEmpty()) {
            Toast.makeText(this, "Please enter a quantity", Toast.LENGTH_SHORT).show();
            return;
        }

        double quantity;
        try {
            quantity = Double.parseDouble(quantityText);
        }
        catch (Exception e) {
            Toast.makeText(this, "Quantity must be a number", Toast.LENGTH_SHORT).show();
            return;
        }

        PantryItem item = new PantryItem();
        item.setName(name);
        item.setQuantity(quantity);
        item.setUnit(unit);
        item.setExpiryDate(expiry);

        try {
            dataSource.open();
            dataSource.insertPantryItem(item);
            dataSource.close();
            Toast.makeText(this, "Ingredient saved", Toast.LENGTH_SHORT).show();
            finish();
        }
        catch (Exception e) {
            Toast.makeText(this, "Could not save ingredient", Toast.LENGTH_LONG).show();
        }
    }
}