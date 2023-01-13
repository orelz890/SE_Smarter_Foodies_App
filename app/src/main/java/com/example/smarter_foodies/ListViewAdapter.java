package com.example.smarter_foodies;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

class ListViewAdapter extends ArrayAdapter<String> {
    ArrayList<String> list;
    Context context;
    String key;

    // The ListViewAdapter Constructor
    // @param context: the Context from the MainActivity
    // @param items: The list of items in our Grocery List
    public ListViewAdapter(Context context, ArrayList<String> items, String key) {
        super(context, R.layout.ingredient_item, items);
        this.context = context;
        list = items;
        this.key = key;
    }

    // The method we override to provide our own layout for each View (row) in the ListView
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.ingredient_item, null);
            TextView amount = convertView.findViewById(R.id.amount);
            ImageButton remove = convertView.findViewById(R.id.ib_remove_ingre);
            TextView title = convertView.findViewById(R.id.ingredient_title);

            String[] arr = list.get(position).split(":");
            String a = arr[0] + " [g]";
            amount.setText(a);
            title.setText(arr[1]);

            // Listeners for duplicating and removing an item.
            // They use the static removeItem and addItem methods created in MainActivity.
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (key.equals("add")) {
                        AddRecipe.removeItem(position);
                    }else{
                        UpdateRecipe.removeItem(position);
                    }
                }
            });
        }
        return convertView;
    }

}
