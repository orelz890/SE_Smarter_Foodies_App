package com.example.smarter_foodies;

import android.content.Context;
import android.os.Bundle;
import android.view.ContentInfo;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class SearchRecipe extends AppCompatActivity {
    CRUD_RealTimeDatabaseData CRUD;
    AutoCompleteTextView autoCompleteSearchView;
    ArrayAdapter<String> arraySearchAdapter;
    List<String> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        autoCompleteSearchView = findViewById(R.id.ac_searchView);
        CRUD = new CRUD_RealTimeDatabaseData();
        recipes = new ArrayList<>();

        InitAutoCompleteSearchView();
        System.out.println(recipes.size());

    }

    private void InitAutoCompleteSearchView() {
        // how to get data from the database - filter
        DatabaseReference mDatabaseSearchGet = FirebaseDatabase.getInstance()
                .getReference().child("filter");
        mDatabaseSearchGet.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot_filter) {
                if (snapshot_filter.exists()) {
                    recipes.clear();
                    for (DataSnapshot snapshot : snapshot_filter.getChildren()) {
                        Iterable<DataSnapshot> categorySnapshot = snapshot.getChildren();
                        for (DataSnapshot subCategorySnapshot: categorySnapshot) {
                            Iterable<DataSnapshot> recipeNamesSnapshot = subCategorySnapshot.getChildren();
                            for (DataSnapshot recipeNameSnap: recipeNamesSnapshot){
                                String name = recipeNameSnap.getValue(String.class);
                                recipes.add(name);
//                                System.out.println(name);
                            }
                        }
                    }
                    arraySearchAdapter = new ArrayAdapter<>(SearchRecipe.this, android.R.layout.simple_list_item_activated_1, recipes);
                    autoCompleteSearchView.setAdapter(arraySearchAdapter);
                    autoCompleteSearchView.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            // Hide my keyboard
                            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
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
}
