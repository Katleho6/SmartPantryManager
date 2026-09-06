package com.example.smartpantrymanager;

public class PantryItem {
    private int itemId;
    private String name;
    private double quantity;
    private String unit;
    private String expiryDate;

    public PantryItem() {
        itemId = -1;
    }

    public int getItemId() {
        return itemId;
    }
    public void setItemId(int i) {
        itemId = i;
    }
    public String getName() {
        return name;
    }
    public void setName(String s) {
        name = s;
    }
    public double getQuantity() {
        return quantity;
    }
    public void setQuantity(double q) {
        quantity = q;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String s) {
        unit = s;
    }
    public String getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(String s) {
        expiryDate = s;
    }
}