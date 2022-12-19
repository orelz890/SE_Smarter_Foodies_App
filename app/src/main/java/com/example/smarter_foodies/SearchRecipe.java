package com.example.smarter_foodies;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smarter_foodies.databinding.ActivityDashboardBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchRecipe extends DashboardActivity {
    AutoCompleteTextView autoCompleteSearchView;
    ArrayAdapter<String> arraySearchAdapter;
    List<String> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        View activityMainView = LayoutInflater.from(this).inflate(R.layout.activity_search, rootLayout, false);
        rootLayout.addView(activityMainView);
        setContentView(rootLayout);
        allocateActivityTitle("SearchRecipe");

        autoCompleteSearchView = findViewById(R.id.ac_searchView);
        recipes = new ArrayList<>();
        InitAutoCompleteSearchView();

    }

    private void setFirstDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SearchRecipe.this, androidx.appcompat.R.style.Base_V7_Theme_AppCompat_Dialog);
        final View customLayout = getLayoutInflater().inflate(R.layout.filter_search_dialog, null);
        builder.setView(customLayout);
        builder.setCancelable(false);
        builder.setTitle("Recipe name");
//            startActivity(new Intent(MainActivity.this, UpdateRecipe.class));
        builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                AutoCompleteTextView mainCategory = customLayout.findViewById(R.id.auto_complete_category);
                AutoCompleteTextView subCategory = customLayout.findViewById(R.id.auto_complete_sub_category);
            }
        });

        builder.setNegativeButton("cancel", (dialogInterface, which) -> {
            startActivity(new Intent(SearchRecipe.this, MainActivity.class));
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setSecondDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SearchRecipe.this, androidx.appcompat.R.style.Base_V7_Theme_AppCompat_Dialog);
        final View customLayout = getLayoutInflater().inflate(R.layout.filter_search_dialog, null);
        builder.setView(customLayout);
        builder.setCancelable(false);
        builder.setTitle("Filter");
//            startActivity(new Intent(MainActivity.this, UpdateRecipe.class));
        builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                AutoCompleteTextView mainCategory = customLayout.findViewById(R.id.auto_complete_category);
                AutoCompleteTextView subCategory = customLayout.findViewById(R.id.auto_complete_sub_category);
            }
        });

        builder.setNegativeButton("cancel", (dialogInterface, which) -> {
            startActivity(new Intent(SearchRecipe.this, MainActivity.class));
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void InitAutoCompleteSearchView() {
        DatabaseReference mDatabaseSearchGet = FirebaseDatabase.getInstance()
                .getReference().child("filter");
        mDatabaseSearchGet.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot_filter) {
                if (snapshot_filter.exists()) {
                    recipes.clear();
                    for (DataSnapshot snapshot : snapshot_filter.getChildren()) {
                        Iterable<DataSnapshot> categorySnapshot = snapshot.getChildren();
                        for (DataSnapshot subCategorySnapshot : categorySnapshot) {
                            Iterable<DataSnapshot> recipeNamesSnapshot = subCategorySnapshot.getChildren();
                            for (DataSnapshot recipeNameSnap : recipeNamesSnapshot) {
                                String name = recipeNameSnap.getValue(String.class);
                                recipes.add(name);
//                                System.out.println(name);
                            }
                        }
                    }
                    arraySearchAdapter = new ArrayAdapter<>(SearchRecipe.this, android.R.layout.simple_list_item_activated_1, recipes);
                    autoCompleteSearchView.setAdapter(arraySearchAdapter);
                    autoCompleteSearchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                            String recipeName = parent.getItemAtPosition(pos).toString();
                            System.out.println(recipeName);
                            // >>>>>>>>>>>>>>>>>>>> Here we refer the user to the recipe page.. <<<<<<<<<<<<<<<<<<<<<<<<
//                            Toast.makeText(getApplicationContext(), "Item: " + category, Toast.LENGTH_SHORT).show();
                        }
                    });
                    autoCompleteSearchView.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            // Hide my keyboard
                            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), 0);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void InitAutoCompleteFilterView(String category, String subCategory) {
//        DatabaseReference mDatabaseSearchGet = FirebaseDatabase.getInstance()
//                .getReference().child("filter").child(category).child(subCategory);
//        mDatabaseSearchGet.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot_filter) {
//                if (snapshot_filter.exists()) {
//                    recipes.clear();
//                    for (DataSnapshot snapshot : snapshot_filter.getChildren()) {
//                        Iterable<DataSnapshot> categorySnapshot = snapshot.getChildren();
//                        for (DataSnapshot subCategorySnapshot : categorySnapshot) {
//                            Iterable<DataSnapshot> recipeNamesSnapshot = subCategorySnapshot.getChildren();
//                            for (DataSnapshot recipeNameSnap : recipeNamesSnapshot) {
//                                String name = recipeNameSnap.getValue(String.class);
//                                recipes.add(name);
////                                System.out.println(name);
//                            }
//                        }
//                    }
//                    arraySearchAdapter = new ArrayAdapter<>(SearchRecipe.this, android.R.layout.simple_list_item_activated_1, recipes);
//                    autoCompleteSearchView.setAdapter(arraySearchAdapter);
//                    autoCompleteSearchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
//                            String recipeName = parent.getItemAtPosition(pos).toString();
//                            System.out.println(recipeName);
//                            // >>>>>>>>>>>>>>>>>>>> Here we refer the user to the recipe page.. <<<<<<<<<<<<<<<<<<<<<<<<
////                            Toast.makeText(getApplicationContext(), "Item: " + category, Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                    autoCompleteSearchView.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
//                        @Override
//                        public void onDismiss() {
//                            // Hide my keyboard
//                            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), 0);
//                        }
//                    });
    }


}