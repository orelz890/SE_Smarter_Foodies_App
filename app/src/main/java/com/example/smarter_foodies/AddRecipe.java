package com.example.smarter_foodies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.smarter_foodies.recipe;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

public class AddRecipe extends AppCompatActivity {
    DatabaseReference mDatabase;
    recipe dish;
    TextInputEditText etTitle;
    TextInputEditText etIngredients;
    TextInputEditText etDirections;
    NumberPicker npCookingTime;
    NumberPicker npPrepTime;
    NumberPicker npServings;
    NumberPicker npCarbs;
    NumberPicker npProtein;
    NumberPicker npFat;


    Button btnSubmit;

    String[] categoriesList = {"animals", "breakfast", "cakes"};
    String[] subCategoriesList = {"aaa", "bbb", "ccc"};
    String category = "";
    String subCategory = "";
    AutoCompleteTextView autoCompleteCategory;
    ArrayAdapter<String> adapterCategories;
    AutoCompleteTextView autoCompleteSubCategory;
    ArrayAdapter<String> adapterSubCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        mDatabase = FirebaseDatabase.getInstance().getReference();
//        ValueEventListener recipeListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                recipe r = snapshot.getValue(recipe.class);
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.w("Activity", "Cancelled", error.toException());
//            }
//        };
        //    ========================= Get data from user =============================================
        etTitle = findViewById(R.id.etTitle);
        etIngredients = findViewById(R.id.etIngredients);
        etDirections = findViewById(R.id.etDirections);
        npCookingTime = findViewById(R.id.npCookTime);
        npCookingTime.setValue(-1);
        npCookingTime.setMaxValue(2000);
        npCookingTime.setMinValue(0);
        npCookingTime.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                npCookingTime.setValue(newValue);
            }
        });
        npPrepTime = findViewById(R.id.npPrepTime);
        npPrepTime.setValue(-1);
        npPrepTime.setMaxValue(2000);
        npPrepTime.setMinValue(0);
        npPrepTime.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                npPrepTime.setValue(newValue);
            }
        });
        npServings = findViewById(R.id.npServings);
        npServings.setValue(-1);
        npServings.setMaxValue(2000);
        npServings.setMinValue(0);
        npServings.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                npServings.setValue(newValue);
            }
        });
        npCarbs = findViewById(R.id.npCarbs);
        npCarbs.setValue(-1);
        npCarbs.setMaxValue(2000);
        npCarbs.setMinValue(0);
        npCarbs.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                npCarbs.setValue(newValue);
            }
        });
        npProtein = findViewById(R.id.npProtein);
        npProtein.setValue(-1);
        npProtein.setMaxValue(2000);
        npProtein.setMinValue(0);
        npProtein.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                npProtein.setValue(newValue);
            }
        });
        npFat = findViewById(R.id.npFat);
        npFat.setValue(-1);
        npFat.setMaxValue(2000);
        npFat.setMinValue(0);
        npFat.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                npFat.setValue(newValue);
            }
        });

        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(view -> {
//            submitRecipe();
//            init_database_with_existing_scraped_data();
//            deleteAllInitData();
//            deleteRecipe("2_Ingredient_Pineapple_Angel_Food_Cake");
            System.out.println("=============================");
            List<recipe> recipes = getDishFromDatabase("Air Fryer Mini Breakfast Burritos");
            System.out.println(recipes.size());
            for (recipe r : recipes){
                System.out.println(r);
            }
            System.out.println("============================");
        });

        //    ========================= AutoCompleteTextView =================================
        autoCompleteCategory = findViewById(R.id.auto_complete_category);
        adapterCategories = new ArrayAdapter<String>(this, R.layout.list_items, categoriesList);
        autoCompleteCategory.setAdapter(adapterCategories);
        autoCompleteCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                category = parent.getItemAtPosition(pos).toString();
                Toast.makeText(getApplicationContext(), "Item: " + category, Toast.LENGTH_SHORT).show();
            }
        });
        autoCompleteSubCategory = findViewById(R.id.auto_complete_sub_category);
        adapterSubCategories = new ArrayAdapter<String>(this, R.layout.list_items, subCategoriesList);
        autoCompleteSubCategory.setAdapter(adapterSubCategories);
        autoCompleteSubCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                subCategory = parent.getItemAtPosition(pos).toString();
                Toast.makeText(getApplicationContext(), "Item: " + subCategory, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean init_database_with_existing_scraped_data() {
        try {
            AssetManager assetManager = getAssets();
            String[] files = assetManager.list("per_category_data2");
            for (String f : files) {
                String[] files2 = assetManager.list("per_category_data2/" + f);
                System.out.println(">>>>>>>>>>>>>>> " + f + " <<<<<<<<<<<<<<<<<<<");
                for (String f2 : files2) {
                    String[] files3 = assetManager.list("per_category_data2/" + f + "/" + f2);

//                        System.out.println(f2);
                    for (String f3 : files3) {
                        try {
                            String file_path = "per_category_data2/" + f + "/" + f2 + "/" + f3;
                            InputStream inputStream = getAssets().open(file_path);
                            int size = inputStream.available();
                            byte[] buffer = new byte[size];
                            inputStream.read(buffer);
                            String json_str = new String(buffer);
                            JsonObject jsonObject = new JsonParser().parse(json_str).getAsJsonObject();
                            String copy_rights = "https://www.allrecipes.com/";
                            recipe curr_recipe = new recipe(jsonObject, copy_rights);
                            loadDishToSearchTree(curr_recipe);
                            loadDishToFilterTree(curr_recipe);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                    System.out.println("\n=====================================\n");
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }


    private void submitRecipe() {
        String title = etTitle.getText().toString();
        if (TextUtils.isEmpty(title)) {
            etTitle.setError("Title cannot be empty");
            etTitle.requestFocus();
            return;
        }
        String ingredients = etIngredients.getText().toString();
        String[] ingredients_list = ingredients.split("\n");
        if (TextUtils.isEmpty(ingredients)) {
            etIngredients.setError("Ingredients cannot be empty");
            etIngredients.requestFocus();
            return;
        } else {
            for (String s : ingredients_list) {
                String[] temp = s.trim().split(" ");
                try {
                    Double.parseDouble(temp[0]);
                } catch (Exception e) {
                    etIngredients.setError("Every Ingredient must begin with the quantity..");
                    etIngredients.requestFocus();
                    return;
                }
            }
        }
        List<String> ingredientsArray = new ArrayList<>(Arrays.asList(ingredients_list));

        String directions = etDirections.getText().toString();
        String[] directions_list = directions.split("\n");
        if (TextUtils.isEmpty(directions)) {
            etDirections.setError("Directions cannot be empty");
            etDirections.requestFocus();
        } else if (category.isEmpty()) {
            autoCompleteCategory.setError("Category cannot be empty");
            autoCompleteCategory.requestFocus();
        } else if (subCategory.isEmpty()) {
            autoCompleteSubCategory.setError("Sub category cannot be empty");
            autoCompleteSubCategory.requestFocus();
        } else if (npPrepTime.getValue() == 0 || npCookingTime.getValue() == 0 ||
                npServings.getValue() == 0 || npProtein.getValue() == 0 ||
                npFat.getValue() == 0 || npCarbs.getValue() == 0) {
            Toast.makeText(getApplicationContext(), "All bottom half must be filled too!",
                    Toast.LENGTH_SHORT).show();
        } else {
            List<String> directionsArray = new ArrayList<>(Arrays.asList(directions_list));
            dish = new recipe(title, category, subCategory, ingredientsArray, directionsArray,
                    npPrepTime.getValue() + "", npCookingTime.getValue() + "", npServings.getValue() + "",
                    npProtein.getValue() + "", "0", npFat.getValue() + "", npCarbs.getValue() + "", 0,
                    new ArrayList<>(), 0, new HashMap<>(), "");
            Toast.makeText(getApplicationContext(), dish.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private DatabaseReference getToRecipeDepth(DatabaseReference DataRef, String name) {
        DataRef = mDatabase;
        name = name.replace("\"", "").replace(" ", "");
        if (name.length() > 0) {
            String new_name = "";
            for (int i = 0; i < name.length(); i++) {
                if (Character.isDigit(name.charAt(i)) || Character.isAlphabetic(name.charAt(i))) {
                    new_name += name.charAt(i);
                }
            }
            new_name = new_name.toLowerCase(Locale.ROOT);
            int len = new_name.length();
            DataRef = DataRef.child("search");
            // Max tree depth is 32
            for (int i = 0; i < len && i < 27; i++) {
                DataRef = DataRef.child(new_name.charAt(i) + "");
            }
        }
        return DataRef;
    }

//    private boolean deleteAllInitData() {
//        mDatabase.child("filter").removeValue();
//        mDatabase.child("search").removeValue();
//        return true;
//    }

    public void loadDishToSearchTree(recipe r) {
        if (r != null) {
            DatabaseReference mDatabaseSearch = mDatabase;
            getToRecipeDepth(mDatabaseSearch, r.getTitle()).setValue(r)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(),
                                        r.getTitle() + "> added successfully to search!",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    public void loadDishToFilterTree(recipe r) {
        if (r != null) {
            DatabaseReference mDatabaseSearch = mDatabase.child("filter")
                    .child(r.getMain_category()).child(r.getCategory()).child(r.getTitle());
            mDatabaseSearch.setValue(r).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), r.getTitle() + "> added successfully to Filter!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public List<recipe> getDishFromDatabase(String name) {
        int len = name.length();
        List<recipe> recipe_list = new ArrayList<>();
        if (len > 0) {
            DatabaseReference mDatabaseSearchGet = mDatabase;
            mDatabaseSearchGet = getToRecipeDepth(mDatabaseSearchGet, name);
            mDatabaseSearchGet.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Iterable<DataSnapshot> childrens = snapshot.getChildren();
                        for (DataSnapshot child : childrens) {
                            try {
                                recipe current_recipe = child.getValue(recipe.class);
                                if (current_recipe != null) {
                                    recipe_list.add(current_recipe);
                                }
                            } catch (Exception ignored) {

                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("TAG", error.getMessage());
                }
            });
        }
        return recipe_list;
    }

    public void deleteRecipe(String name) {
        DatabaseReference mDataSearchDelete = mDatabase;
        DatabaseReference mDataFilterRef = mDatabase.child("filter");
        Query searchQuery = getToRecipeDepth(mDataSearchDelete, name);
        searchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSearch : dataSnapshot.getChildren()) {
                    recipe r_s = childSearch.getValue(recipe.class);
                    if (r_s != null && r_s.getTitle().equals(name)) {
                        childSearch.getRef().removeValue();
                        Query filterQuery = mDataFilterRef.child(r_s.getMain_category()).child(r_s.getCategory());
                        filterQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot childFilter : dataSnapshot.getChildren()) {
                                    recipe r_f = childFilter.getValue(recipe.class);
                                    if (r_f != null && r_f.getTitle().equals(name)) {
                                        childFilter.getRef().removeValue();
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("TAG", "onCancelled", error.toException());
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("TAG", "onCancelled", databaseError.toException());
            }
        });
    }
}