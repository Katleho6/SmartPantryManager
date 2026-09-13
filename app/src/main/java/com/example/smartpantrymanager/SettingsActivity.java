package com.example.smartpantrymanager;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    private Switch switchExpiryAlerts;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        preferences = getSharedPreferences("SmartPantryPrefs", MODE_PRIVATE);

        switchExpiryAlerts = findViewById(R.id.switchExpiryAlerts);

        // Load the saved setting, defaulting to "on" if never set before
        boolean alertsEnabled = preferences.getBoolean("expiryAlertsEnabled", true);
        switchExpiryAlerts.setChecked(alertsEnabled);

        switchExpiryAlerts.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("expiryAlertsEnabled", isChecked);
            editor.apply();
        });
    }
}