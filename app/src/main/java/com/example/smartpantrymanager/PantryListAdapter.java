package com.example.smartpantrymanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class PantryListAdapter extends ArrayAdapter<String> {

    private ArrayList<String> details;
    private ArrayList<Boolean> expiringSoonFlags;

    public PantryListAdapter(Context context, ArrayList<String> details, ArrayList<Boolean> expiringSoonFlags) {
        super(context, 0, details);
        this.details = details;
        this.expiringSoonFlags = expiringSoonFlags;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.pantry_list_item, parent, false);
        }

        TextView textItemDetails = listItemView.findViewById(R.id.textItemDetails);
        TextView textExpiryWarning = listItemView.findViewById(R.id.textExpiryWarning);

        textItemDetails.setText(details.get(position));

        if (expiringSoonFlags.get(position)) {
            textExpiryWarning.setVisibility(View.VISIBLE);
        } else {
            textExpiryWarning.setVisibility(View.GONE);
        }

        return listItemView;
    }
}