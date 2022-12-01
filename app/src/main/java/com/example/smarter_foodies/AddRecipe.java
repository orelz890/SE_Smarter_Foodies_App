package com.example.smarter_foodies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.smarter_foodies.recipe;

import java.util.ArrayList;
import java.util.HashMap;

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
        //    ========================= EditText =============================================
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
            submitRecipe();
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
                }catch (Exception e){
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
                    npPrepTime.getValue(), npCookingTime.getValue(), npServings.getValue(),
                    npProtein.getValue(), npFat.getValue(), npCarbs.getValue(), 0,
                    new ArrayList<String>(), 0, new HashMap<>());
            Toast.makeText(getApplicationContext(), dish.toString(), Toast.LENGTH_SHORT).show();
        }
    }

}