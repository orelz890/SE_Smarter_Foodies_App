package com.example.smarter_foodies;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smarter_foodies.databinding.ActivityDashboardBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class UpdateRecipe extends DashboardActivity {

    ActivityDashboardBinding activityDashboardBinding;
    FirebaseAuth mAuth;
    // These strings role is to help us sync between what the user see in the sub category
    // AutoCompleteTextView and his choice of main category
    String[] categoriesList;
    String category = "";
    String subCategory = "";
    String recipeToUpdateName = "";
    // Reference to the realtime database
    DatabaseReference mDatabase;
    // Text inputs from the user
    TextInputEditText etTitle;
    TextInputEditText etIngredients;
    TextInputEditText etDirections;
    // Main & sub categories as the user wish
    AutoCompleteTextView autoCompleteCategory;
    ArrayAdapter<String> adapterCategories;
    AutoCompleteTextView autoCompleteSubCategory;
    ArrayAdapter<String> adapterSubCategories;
    // Number inputs from the user
    NumberPicker npCookingTime;
    NumberPicker npPrepTime;
    NumberPicker npServings;
    NumberPicker npCarbs;
    NumberPicker npProtein;
    NumberPicker npFat;
    // Submit recipe button
    Button btnSubmit;

    CRUD_RealTimeDatabaseData CRUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_recipe);
        mAuth = FirebaseAuth.getInstance();
        // Create reference to the firebase real time database
        mDatabase = FirebaseDatabase.getInstance().getReference();
        CRUD = new CRUD_RealTimeDatabaseData();
        // Get the name of the recipe
        setDialog();
        // Fill the categories list which the user can chose from
        this.fillCategoriesList();
        this.createAllAutoCompleteTextViews("", "");
        //    ========================= Get data from user =============================================
        this.createAllNumberPickers();
    }

    private void fillCategoriesList() {
        Set<String> keys = CRUD.subCategoriesList.keySet();
        categoriesList = new String[keys.size() - 1];
        int i = 0;
        for (String s : keys) {
            if (!s.isEmpty()) {
                categoriesList[i++] = s;
            }
        }
    }

    private void change_adapter() {
        autoCompleteSubCategory.setText("");
        adapterSubCategories = new ArrayAdapter<String>(this, R.layout.list_items, CRUD.subCategoriesList.get(category));
        autoCompleteSubCategory.setAdapter(adapterSubCategories);
    }

    private void change_adapter_to_original_values() {
        autoCompleteCategory.setText(this.category);
        adapterCategories = new ArrayAdapter<>(this, R.layout.list_items, categoriesList);
        autoCompleteCategory.setAdapter(adapterCategories);
        autoCompleteSubCategory.setText(this.subCategory);
        adapterSubCategories = new ArrayAdapter<String>(this, R.layout.list_items, CRUD.subCategoriesList.get(category));
        autoCompleteSubCategory.setAdapter(adapterSubCategories);
    }

    private void createAllAutoCompleteTextViews(String main_category, String sub_category) {
        System.out.println(main_category);
        System.out.println(sub_category);
        autoCompleteCategory = findViewById(R.id.auto_complete_category);
        if (!main_category.isEmpty()) {
            autoCompleteCategory.setText(main_category);
        }
        adapterCategories = new ArrayAdapter<>(this, R.layout.list_items, categoriesList);
        autoCompleteCategory.setAdapter(adapterCategories);
        autoCompleteCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                category = parent.getItemAtPosition(pos).toString();
                Toast.makeText(getApplicationContext(), "Item: " + category, Toast.LENGTH_LONG).show();
                change_adapter();
            }
        });

        autoCompleteSubCategory = findViewById(R.id.auto_complete_sub_category);
//        if (!sub_category.isEmpty()) {
//        }
        adapterSubCategories = new ArrayAdapter<String>(this, R.layout.list_items, CRUD.subCategoriesList.get(category));
        autoCompleteSubCategory.setAdapter(adapterSubCategories);
        autoCompleteSubCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                subCategory = parent.getItemAtPosition(pos).toString();
                Toast.makeText(getApplicationContext(), "Item: " + subCategory, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateRecipe.this, androidx.appcompat.R.style.Base_V7_Theme_AppCompat_Dialog);
        final View customLayout = getLayoutInflater().inflate(R.layout.custom_dialog_layout, null);
        builder.setView(customLayout);
        builder.setCancelable(false);
        builder.setTitle("Recipe name");
//            startActivity(new Intent(MainActivity.this, UpdateRecipe.class));
        builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                EditText editText = customLayout.findViewById(R.id.etRecipeToUpdate);
                recipeToUpdateName = editText.getText().toString();
                createAllButtons();
            }
        });

        builder.setNegativeButton("cancel", (dialogInterface, which) -> {
            startActivity(new Intent(UpdateRecipe.this, MainActivity.class));
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void createAllButtons() {
        if (this.recipeToUpdateName.isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "Name cant be empty",
                    Toast.LENGTH_LONG).show();
            setDialog();
        } else {
            setKnownDataToTextViews(this.recipeToUpdateName);
            btnSubmit = findViewById(R.id.btnSubmit);
            btnSubmit.setOnClickListener(view -> {
                System.out.println("=============================");
                submitRecipe();
                startActivity(new Intent(UpdateRecipe.this, MainActivity.class));
                System.out.println("============================");
            });
        }
    }

    private void createAllNumberPickers() {
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
    }

    private void submitRecipe() {
        String title = etTitle.getText().toString();
        if (TextUtils.isEmpty(title)) {
            etTitle.setError("Title cannot be empty");
            etTitle.requestFocus();
            return;
        }
        String ingredients = etIngredients.getText().toString();
        System.out.println(ingredients);
        String[] ingredients_list = ingredients.split("\n");
        if (TextUtils.isEmpty(ingredients)) {
            etIngredients.setError("Ingredients cannot be empty");
            etIngredients.requestFocus();
            return;
        } else {
            for (String s : ingredients_list) {
                if (!s.equals("\n")) {
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
                    Toast.LENGTH_LONG).show();
        } else {
            List<String> directionsArray = new ArrayList<>(Arrays.asList(directions_list));
            recipe r = new recipe(title, category, subCategory, new ArrayList<>(), directionsArray,
                    npPrepTime.getValue() + "", npCookingTime.getValue() + "", npServings.getValue() + "",
                    npProtein.getValue() + "", "0", npFat.getValue() + "", npCarbs.getValue() + "", 0,
                    new ArrayList<>(), 0, new HashMap<>(), "");
            r.setIngredients(ingredientsArray);

            r.setCopy_rights(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
            Toast.makeText(getApplicationContext(), r.toString(), Toast.LENGTH_LONG).show();
            CRUD.loadDishToFilterTree(r);
            CRUD.loadDishToSearchTree(r);
        }
    }

    private void setCategories(String main, String sub) {
        this.category = main;
        this.subCategory = sub;
    }

    private void setKnownDataToTextViews(String name) {
        //// how to get data from the database- search
        DatabaseReference mDatabaseSearchGet = FirebaseDatabase.getInstance().getReference();
        mDatabaseSearchGet = CRUD.getToRecipeDepth(mDatabaseSearchGet, name);
        mDatabaseSearchGet.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot_search) {
                if (snapshot_search.exists()) {
                    for (DataSnapshot child : snapshot_search.getChildren()) {
                        System.out.println(child);
                        try {
                            recipe r = child.getValue(recipe.class);
                            System.out.println(r);
                            if (r != null && r.getCopy_rights()
                                    .equals(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())) {
                                etTitle.setText(r.getTitle());
                                List<String> directions = r.getDirections();
                                StringBuilder directionsView = new StringBuilder();
                                for (String s : directions) {
                                    directionsView.append(s).append("\n");
                                }
                                etDirections.setText(directionsView);
                                List<String> ingredients = r.getIngredients();
                                StringBuilder ingredientsView = new StringBuilder();
                                for (String s : ingredients) {
                                    ingredientsView.append(s).append("\n");
                                }
                                etIngredients.setText(ingredientsView);
                                category = r.getMain_category();
                                subCategory = r.getCategory();
                                change_adapter_to_original_values();
                                npCookingTime.setValue(Integer.parseInt(r.getCookingTime()));
                                npPrepTime.setValue(Integer.parseInt(r.getPrepTime()));
                                npServings.setValue(Integer.parseInt(r.getServings()));
                                npCarbs.setValue(Integer.parseInt(r.getCarbs()));
                                npProtein.setValue(Integer.parseInt(r.getProtein()));
                                npFat.setValue(Integer.parseInt(r.getFat()));
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "You are not authorized to change this recipe..",
                                        Toast.LENGTH_LONG).show();
                                setDialog();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "We had a problem please try again..",
                                    Toast.LENGTH_LONG).show();
                            setDialog();
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "This recipe don't exist..",
                            Toast.LENGTH_LONG).show();
                    setDialog();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG", "getDishFromSearchTree- " + error.getMessage());
            }
        });
    }

}