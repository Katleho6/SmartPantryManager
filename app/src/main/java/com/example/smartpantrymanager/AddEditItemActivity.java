package com.example.smartpantrymanager;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Calendar;

public class AddEditItemActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextQuantity;
    private AutoCompleteTextView dropdownUnit;
    private EditText editTextExpiry;
    private PantryDataSource dataSource;
    private PantryItem currentItem;

    // The fixed list of units the dropdown will show
    private String[] unitOptions = {"kg", "g", "L", "ml", "pcs", "cups"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_item);

        dataSource = new PantryDataSource(this);

        editTextName = findViewById(R.id.editTextName);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        dropdownUnit = findViewById(R.id.dropdownUnit);
        editTextExpiry = findViewById(R.id.editTextExpiry);
        ImageButton buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> finish());

        Button buttonSave = findViewById(R.id.buttonSave);
        Button buttonDelete = findViewById(R.id.buttonDelete);

        // Fill the dropdown with our list of units
        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, unitOptions);
        dropdownUnit.setAdapter(unitAdapter);

        editTextExpiry.setOnClickListener(v -> openDatePicker());

        buttonSave.setOnClickListener(v -> saveItem());
        buttonDelete.setOnClickListener(v -> deleteItem());

        // Check if we were passed an existing item's id (edit mode)
        int itemId = getIntent().getIntExtra("itemId", -1);

        if (itemId != -1) {
            loadExistingItem(itemId);
        } else {
            currentItem = new PantryItem();
            buttonDelete.setVisibility(android.view.View.GONE);
        }
    }

    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Month is 0-based in Android, so we add 1 for a normal calendar month
                    String monthText = String.valueOf(selectedMonth + 1);
                    if (monthText.length() == 1) {
                        monthText = "0" + monthText;
                    }
                    String dayText = String.valueOf(selectedDay);
                    if (dayText.length() == 1) {
                        dayText = "0" + dayText;
                    }

                    String pickedDate = selectedYear + "-" + monthText + "-" + dayText;
                    editTextExpiry.setText(pickedDate);
                },
                year, month, day);

        datePickerDialog.show();
    }

    private void loadExistingItem(int itemId) {
        try {
            dataSource.open();
            currentItem = dataSource.getSpecificPantryItem(itemId);
            dataSource.close();

            editTextName.setText(currentItem.getName());
            editTextQuantity.setText(String.valueOf(currentItem.getQuantity()));
            editTextExpiry.setText(currentItem.getExpiryDate());
            dropdownUnit.setText(currentItem.getUnit(), false);
        }
        catch (Exception e) {
            Toast.makeText(this, "Could not load item", Toast.LENGTH_LONG).show();
        }
    }

    private void saveItem() {
        String name = editTextName.getText().toString().trim();
        String quantityText = editTextQuantity.getText().toString().trim();
        String unit = dropdownUnit.getText().toString().trim();
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

        currentItem.setName(name);
        currentItem.setQuantity(quantity);
        currentItem.setUnit(unit);
        currentItem.setExpiryDate(expiry);

        try {
            dataSource.open();
            if (currentItem.getItemId() == -1) {
                dataSource.insertPantryItem(currentItem);
            } else {
                dataSource.updatePantryItem(currentItem);
            }
            dataSource.close();
            Toast.makeText(this, "Ingredient saved", Toast.LENGTH_SHORT).show();
            finish();
        }
        catch (Exception e) {
            Toast.makeText(this, "Could not save ingredient", Toast.LENGTH_LONG).show();
        }
    }

    private void deleteItem() {
        try {
            dataSource.open();
            dataSource.deletePantryItem(currentItem.getItemId());
            dataSource.close();
            Toast.makeText(this, "Ingredient deleted", Toast.LENGTH_SHORT).show();
            finish();
        }
        catch (Exception e) {
            Toast.makeText(this, "Could not delete ingredient", Toast.LENGTH_LONG).show();
        }
    }
}