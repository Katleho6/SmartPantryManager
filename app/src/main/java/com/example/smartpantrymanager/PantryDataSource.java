package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.sql.SQLException;
import java.util.ArrayList;

public class PantryDataSource {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public PantryDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // Add a new pantry item
    public boolean insertPantryItem(PantryItem item) {
        boolean didSucceed = false;
        try {
            ContentValues initialValues = new ContentValues();
            initialValues.put("name", item.getName());
            initialValues.put("quantity", item.getQuantity());
            initialValues.put("unit", item.getUnit());
            initialValues.put("expirydate", item.getExpiryDate());

            didSucceed = database.insert("pantry_items", null, initialValues) > 0;
        }
        catch (Exception e) {
            //Do nothing -will return false if there is an exception
        }
        return didSucceed;
    }

    // Update an existing pantry item
    public boolean updatePantryItem(PantryItem item) {
        boolean didSucceed = false;
        try {
            long rowId = (long) item.getItemId();
            ContentValues updateValues = new ContentValues();
            updateValues.put("name", item.getName());
            updateValues.put("quantity", item.getQuantity());
            updateValues.put("unit", item.getUnit());
            updateValues.put("expirydate", item.getExpiryDate());

            didSucceed = database.update("pantry_items", updateValues, "_id=" + rowId, null) > 0;
        }
        catch (Exception e) {
            //Do nothing -will return false if there is an exception
        }
        return didSucceed;
    }

    // Delete a pantry item by id
    public boolean deletePantryItem(int itemId) {
        boolean didDelete = false;
        try {
            didDelete = database.delete("pantry_items", "_id=" + itemId, null) > 0;
        }
        catch (Exception e) {
            //Do nothing -return value already set to false
        }
        return didDelete;
    }

    // Get every pantry item, as a list
    public ArrayList<PantryItem> getAllPantryItems() {
        ArrayList<PantryItem> pantryList = new ArrayList<>();
        try {
            String query = "SELECT * FROM pantry_items";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                PantryItem item = new PantryItem();
                item.setItemId(cursor.getInt(0));
                item.setName(cursor.getString(1));
                item.setQuantity(cursor.getDouble(2));
                item.setUnit(cursor.getString(3));
                item.setExpiryDate(cursor.getString(4));

                pantryList.add(item);
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            pantryList = new ArrayList<>();
        }
        return pantryList;
    }
}