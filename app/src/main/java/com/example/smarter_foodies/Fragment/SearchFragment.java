package com.example.smarter_foodies.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.example.smarter_foodies.R;
import com.example.smarter_foodies.SearchRecipe;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {
    AutoCompleteTextView autoCompleteSearchView;
    ArrayAdapter<String> arraySearchAdapter;
    List<String> recipes;
    View view;
    Activity act;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);

        autoCompleteSearchView = view.findViewById(R.id.ac_searchView);
        recipes = new ArrayList<>();
//        InitAutoCompleteSearchView();

        System.out.println(recipes.size());
//        startActivity(new Intent(getContext() , SearchRecipe.class));
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        autoCompleteSearchView.setAdapter(new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_list_item_1 , recipes));
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
//                    arraySearchAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_activated_1, recipes);
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
//
//
//                        @Override
//                        public void onDismiss() {
//                            // Hide my keyboard
//                            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getApplicationWindowToken(), 0);
//                        }
//                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
