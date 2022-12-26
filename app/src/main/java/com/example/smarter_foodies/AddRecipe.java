package com.example.smarter_foodies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static java.lang.Thread.sleep;
import static java.util.Map.entry;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smarter_foodies.databinding.ActivityDashboardBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Set;
import com.example.smarter_foodies.CRUD_RealTimeDatabaseData;

public class AddRecipe extends DashboardActivity {

    FirebaseAuth mAuth;
    // These strings role is to help us sync between what the user see in the sub category
    // AutoCompleteTextView and his choice of main category
    String[] categoriesList;
    String category = "";
    String subCategory = "";
    boolean flag;

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

    ImageView recipeImage;
    FloatingActionButton fab;
    Uri uri;

    CRUD_RealTimeDatabaseData CRUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        View activityMainView = LayoutInflater.from(this).inflate(R.layout.activity_add_recipe, rootLayout, false);
        rootLayout.addView(activityMainView);
        setContentView(rootLayout);
        allocateActivityTitle("AddRecipe");

        mAuth = FirebaseAuth.getInstance();
        // Create reference to the firebase real time database
        mDatabase = FirebaseDatabase.getInstance().getReference();
        CRUD = new CRUD_RealTimeDatabaseData();
        flag = false;
        // Fill the categories list which the user can chose from
        this.fillCategoriesList();
//        this.setImageButtons();

        //    ========================= Get data from user =============================================
        this.createAllNumberPickers();
        this.createAllButtons();
        this.createAllAutoCompleteTextViews();
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

//    private void setImageButtons(){
//        recipeImage = findViewById(R.id.ib_upload_recipe_image);
//        fab = findViewById(R.id.floatingActionButtonAddRecipe);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ImagePicker.with(AddRecipe.this)
////                        .crop()	    			//Crop image(Optional), Check Customization for more option
////                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
////                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
//                        .start();
//            }
//        });
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uri = data.getData();
        recipeImage.setImageURI(uri);
    }

    private void change_adapter() {
        autoCompleteSubCategory = findViewById(R.id.auto_complete_sub_category);
        adapterSubCategories = new ArrayAdapter<String>(this, R.layout.list_items, CRUD.subCategoriesList.get(category));
        autoCompleteSubCategory.setAdapter(adapterSubCategories);
    }

    private void createAllAutoCompleteTextViews() {
        autoCompleteCategory = findViewById(R.id.auto_complete_category);
        adapterCategories = new ArrayAdapter<String>(this, R.layout.list_items, categoriesList);
        autoCompleteCategory.setAdapter(adapterCategories);
        autoCompleteCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                category = parent.getItemAtPosition(pos).toString();
                Toast.makeText(getApplicationContext(), "Item: " + category, Toast.LENGTH_SHORT).show();
                change_adapter();
            }
        });
        autoCompleteSubCategory = findViewById(R.id.auto_complete_sub_category);
        adapterSubCategories = new ArrayAdapter<String>(this, R.layout.list_items, CRUD.subCategoriesList.get(category));
        autoCompleteSubCategory.setAdapter(adapterSubCategories);
        autoCompleteSubCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                subCategory = parent.getItemAtPosition(pos).toString();
                Toast.makeText(getApplicationContext(), "Item: " + subCategory, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createAllButtons() {
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(view -> {
            System.out.println("=============================");
            submitRecipe();
            if (!flag) {
                startActivity(new Intent(AddRecipe.this, SearchRecipe.class));
            }
            System.out.println("============================");
        });
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
        flag = true;
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
                    Toast.LENGTH_SHORT).show();
        } else {
            List<String> directionsArray = new ArrayList<>(Arrays.asList(directions_list));
            recipe r = new recipe(title, category, subCategory, new ArrayList<>(), directionsArray,
                    calcTime(npPrepTime.getValue()), calcTime(npCookingTime.getValue()) + "", npServings.getValue() + "",
                    npProtein.getValue() + "", "0", npFat.getValue() + "", npCarbs.getValue() + "", 0,
                    new ArrayList<>(), 0, new HashMap<>(), "");
            r.setIngredients(ingredientsArray);
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                r.setCopy_rights(currentUser.getUid());
            }
//            List<String> images = new ArrayList<>();
//            if (recipeImage != null) {
//                Object tag = recipeImage.getTag();
//                if (tag != null && tag instanceof String) {
//                    String srcUriString = (String) tag;
//                    Uri srcUri = Uri.parse(srcUriString);
//                    images.add(srcUri.toString());
//                    r.setImages(images);
//                }
//            }
//            if (uri != null){
//                images.add(uri.toString());
//                r.setImages(images);
//            }

            // Load recipe to database
            CRUD.loadDishToDatabase(r);
            List<String> singleValueList = CRUD.getSingleValueList(r.getTitle());
            // Add recipe to the user recipes
            CRUD.addToUserLists(singleValueList, "recipes");
            flag = false;
            Toast.makeText(getApplicationContext(), r.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private String calcTime(int min){
        if (min < 60){
            return min + " mins";
        }
        else{
            return (int)min/60 + " hrs " + min%60 + " mins";
        }
    }

}
