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
import java.util.HashMap;
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
            try {
                Gson gson = new Gson();

                AssetManager assetManager = getAssets();
                String[] files = assetManager.list("per_category_data");
                for (String f : files) {
                    String[] files2 = assetManager.list("per_category_data/" + f);
                    System.out.println(">>>>>>>>>>>>>>> " + f + " <<<<<<<<<<<<<<<<<<<");
                    for (String f2 : files2) {
                        String[] files3 = assetManager.list("per_category_data/" + f + "/" + f2);

//                        System.out.println(f2);
                        for (String f3 : files3) {
                            JsonParser parser = new JsonParser();
                            try {
                                String file_path = "per_category_data/" + f + "/" + f2 + "/" + f3;
                                InputStream inputStream = getAssets().open(file_path);
                                int size = inputStream.available();
                                byte[] buffer = new byte[size];
                                inputStream.read(buffer);
                                String json_str = new String(buffer);
                                JsonObject jsonObject = new JsonParser().parse(json_str).getAsJsonObject();
                                recipe curr_recipe = new recipe(jsonObject);
                                System.out.println(curr_recipe);

                            } catch (Exception e) {
                                e.printStackTrace();
                                return;
                            }

                            System.out.println("\n=====================================\n");
                        }

                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
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
        String directions = etDirections.getText().toString();
        String[] directions_list = directions.split("\n");
        if (TextUtils.isEmpty(directions)) {
            etDirections.setError("Directions cannot be empty");
            etDirections.requestFocus();
            return;
        } else if (category.isEmpty()) {
            autoCompleteCategory.setError("Category cannot be empty");
            autoCompleteCategory.requestFocus();
            return;
        } else if (subCategory.isEmpty()) {
            autoCompleteSubCategory.setError("Sub category cannot be empty");
            autoCompleteSubCategory.requestFocus();
            return;
        } else if (npPrepTime.getValue() == 0 || npCookingTime.getValue() == 0 ||
                npServings.getValue() == 0 || npProtein.getValue() == 0 ||
                npFat.getValue() == 0 || npCarbs.getValue() == 0) {
            Toast.makeText(getApplicationContext(), "All bottom half must be filled too!",
                    Toast.LENGTH_SHORT).show();
        } else {
            dish = new recipe(title, category, subCategory, ingredients_list, directions_list,
                    npPrepTime.getValue() + "", npCookingTime.getValue() + "", npServings.getValue() + "",
                    npProtein.getValue() + "", "0", npFat.getValue() + "", npCarbs.getValue() + "", 0,
                    new ArrayList<String>(), 0, new HashMap<>());
            Toast.makeText(getApplicationContext(), dish.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void loadDishToDatabase(recipe r) {
        String name = r.getTitle();
        int len = name.length();
        if (len > 0) {
            mDatabase.child("recipes").child(r.getMain_category()).child(r.getCategory());
            for (int i = 0; i < len; i++) {
                mDatabase.child(name.charAt(i) + "");
            }
            mDatabase.setValue(r);
        }
    }

    public recipe getDishFromDatabase(String mainCategory, String subCategory, String name) {
        int len = name.length();
        recipe[] r = new recipe[1];
        if (len > 0) {
            mDatabase.child("recipes").child(mainCategory).child(subCategory);
            for (int i = 0; i < len; i++) {
                mDatabase.child(name.charAt(i) + "");
            }
            mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    } else {
                        Log.d("firebase", String.valueOf(task.getResult().getValue()));
                        r[0] = (recipe) task.getResult().getValue();
                    }
                }
            });
        }
        return r[0];
    }

}
