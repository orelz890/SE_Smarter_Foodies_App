package com.example.smarter_foodies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static java.lang.Thread.sleep;
import static java.util.Map.entry;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.nfc.Tag;
import android.os.Build;
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
import java.util.Set;

public class AddRecipe extends AppCompatActivity {

    Map<String, String[]> subCategoriesList = new HashMap<String, String[]>() {{
        put("", new String[]{});
        put("animals", new String[]{"Pet_Food"});
        put("breakfast", new String[]{"Breakfast_and_Brunch", "Breakfast_Burritos", "Breakfast_Casseroles", "Breakfast_Potatoes", "Breakfast_Strata", "Deviled_Eggs", "Eggplant_Parmesan", "Frittata", "Omelets"});
        put("cakes", new String[]{"Angel_Food_Cake", "Cake_Recipes", "Carrot_Cake", "Cheesecake", "Chocolate_Cake", "Coffee_Cake", "Crab_Cakes", "Cupcakes", "Fruitcake", "Linguine", "Mashed_Potatoes", "Pound_Cake", "Shortcake", "Spice_Cake", "Upside-Down_Cake"});
        put("carbs", new String[]{"Calzones", "Dumplings", "Egg_Rolls", "Empanada_Recipes", "Fried_Rice", "Fries", "Gnocchi", "Homemade_Pasta", "Noodle_Casserole", "Pancit", "Pasta_Carbonara", "Pasta_Primavera", "Potato_Pancakes", "Quesadillas", "Quiche", "Quinoa", "Ravioli", "Rice_Casserole", "Rice_Pilaf", "Risotto", "Samosa_Recipes", "Shepherd's_Pie", "Spaghetti", "Spanish_Rice", "Tater_Tots®_Casserole", "Tortellini", "Tortillas", "Tostadas", "Tuna_Casserole", "Ziti"});
        put("cooking_technique_&_channels", new String[]{"Air_Fryer_Recipes", "Allrecipes_Allstars", "Cooking_for_a_Crowd", "Cooking_for_One", "Cooking_for_Two", "Copycat_Recipes", "Food_Wishes®", "Instant_Pot®_Recipes", "Passover_Recipes", "Potluck_Recipes", "Slow_Cooker_Recipes", "Sous_Vide_Recipes", "Stir-Fries", "TVP_Recipes", "Whole30_Recipes"});
        put("dairy", new String[]{"Macaroni_and_Cheese", "Chowder"});
        put("dinner", new String[]{"Dinner_Recipes"});
        put("dips", new String[]{"Artichoke_Dip", "Buffalo_Chicken_Dip", "Cranberry_Sauce", "Gravy", "Guacamole", "Pesto_Sauce", "Relishes", "Salsa", "Spinach_Dips "});
        put("drinks", new String[]{"Applesauce", "Bloody_Marys", "Cocktails", "Eggnog", "Jell-O®_Shots", "Lemonade", "Margaritas", "Mojitos", "Punch", "Sangria", "Smoothies"});
        put("easy_recipies_&_leftovers", new String[]{"Everyday_Leftovers", "Quick_and_Easy_Recipes"});
        put("fish_&_sushi", new String[]{"Ceviche", "Salmon_Recipes", "Sushi"});
        put("flour", new String[]{"Bagels", "Banana_Bread", "Bread_Recipes", "Bruschetta", "Cornbread", "Crackers", "Flat_Bread", "French_Toast", "Garlic_Bread", "Hushpuppies", "Monkey_Bread", "Panini", "Pierogi", "Pretzels", "Pumpkin_Bread", "Sandwiches", "Scones", "Wheat_Bread", "Yeast_Bread", "Zucchini_Bread"});
        put("gifts_&_comfort_food", new String[]{"Comfort_Food", "Food_Gifts"});
        put("healty_&_diets", new String[]{"Baked_Beans", "Broccoli_Salad", "Chicken_Salad", "Diabetic_Recipes", "Egg_Salad", "Fruit_Salads", "Gluten-Free_Recipes", "Granola", "Green_Salads", "Healthy_Recipes", "High-Fiber_Recipes", "Keto_Diet", "Low-Calorie_Recipes", "Low-Cholesterol_Recipes", "Low-Fat_Recipes", "Low-Sodium_Recipes", "Low_Glycemic_Impact_Recipes", "Mediterranean_Diet", "Oatmeal", "Overnight_Oats", "Paleo_Diet", "Potato_Salad", "Raw_Food_Diet", "Refried_Beans", "Salad_Dressings", "Salad_Recipes", "Seitan_Recipes", "Sugar-Free_Recipes", "Tempeh_Recipes", "Tofu_Recipes", "Vegan_Recipes", "Vegetable_Recipes", "Vegetable_Side_Dishes", "Vegetarian_Recipes"});
        put("holidays_and_traditional_food", new String[]{"Falafel", "Paella", "Tacos", "Tamales"});
        put("lunch", new String[]{"Lunch_Recipes"});
        put("main_dishes", new String[]{"Casseroles", "Fajitas", "Lasagna", "Lettuce_Wraps", "Main_Dishes", "Manicotti", "Quiche", "Stew"});
        put("meat_&_chicken", new String[]{"Bulgogi", "Burgers", "Chili", "Gyros", "Jerky", "Kalbi", "Ribs", "Roasts"});
        put("per_spesipic_ingridiant", new String[]{"Mushroom_Recipes", "Pickles", "Stuffed_Bell_Peppers", "Stuffed_Mushrooms", "Winter_Squash_Recipes", "Yam_Recipes"});
        put("pies", new String[]{"Apple_Pie", "Blueberry_Pie", "Cherry_Pie", "Chess_Pie", "Key_Lime_Pie", "Mincemeat_Pie", "Pecan_Pie", "Pie_Crusts", "Pie_Recipes", "Pot_Pie", "Pumpkin_Pie", "Rhubarb_Pie", "Shepherd's_Pie", "Slab_Pie", "Strawberry_Pie", "Sweet_Potato_Pie", "Whoopie_Pies"});
        put("pizza", new String[]{"Pizza", "Pizza_Dough_and_Crusts"});
        put("pork", new String[]{"Ground_Pork", "Pork_Chops", "Pork_Recipes", "Pork_Ribs", "Pork_Shoulder", "Pork_Tenderloin", "Pulled_Pork"});
        put("preserved", new String[]{"Canning_and_Preserving"});
        put("salads", new String[]{"Broccoli_Salad", "Chicken_Salad", "Coleslaw", "Egg_Salad", "Fruit_Salads", "Green_Salads", "Jell-O®_Salad", "Pasta_Salad", "Potato_Salad", "Salad_Dressings", "Salad_Recipes", "Taco_Salad", "Tomato_Salad", "Tuna_Salad", "Waldorf_Salad"});
        put("sea_fruit", new String[]{"Etouffee", "Jambalaya", "Shrimp_and_Grits", "Shrimp_Scampi"});
        put("side_dishes", new String[]{"Grits", "Hummus", "Jalapeno_Poppers", "Meal_Prep", "Pate", "Polenta", "Side_Dishes", "Tapas_Recipes"});
        put("snacks_&_sweets", new String[]{"Appetizers_and_Snacks", "Bar_Cookies", "Biscotti", "Biscuit", "Blintz", "Blondies", "Brownies", "Cheese_Balls", "Cheese_Fondue", "Chocolate_Chip_Cookies", "Chocolate_Fudge", "Christmas_Cookies", "Cinnamon_Rolls", "Cobbler", "Cookies", "Creme_Brulee", "Crisps_and_Crumbles", "Danishes", "Desserts", "Divinity", "Doughnuts", "Drop_Cookies", "Energy_Balls", "English_Muffins", "Flan", "Fondant", "Frosting_and_Icing_Recipes", "Fudge", "Gingerbread_Cookies", "Gingersnaps", "Ice_Cream", "Jams_and_Jellies", "Kolache", "Lemon_Bars", "Macaroons", "Mousse", "Muffins", "Nachos", "Oatmeal_Cookies", "Pancakes", "Pasties", "Pastries", "Pavlova", "Peanut_Butter_Cookies", "Popcorn", "Popovers_and_Yorkshire_Pudding", "Rice_Pudding", "Road_Trip_Snacks", "Sandwich_Cookies", "Seder_Recipes", "Shortbread_Cookies", "Snickerdoodles", "Spritz_Cookies", "Strawberry_Shortcake", "Sugar_Cookies", "Thumbprint_Cookies", "Tiramisu", "Toffee", "Tortes", "Truffles", "Waffles"});
        put("soups", new String[]{"Borscht", "Butternut_Squash_Soup", "Chicken_Noodle_Soup", "French_Onion_Soup", "Gazpacho", "Gumbo", "Lentil_Soup", "Minestrone_Soup", "Mushroom_Soup", "Potato_Soup", "Soup", "Split_Pea_Soup"});
    }};

    // These strings role is to help us sync between what the user see in the sub category
    // AutoCompleteTextView and his choice of main category
    String[] categoriesList;
    String category = "";
    String subCategory = "";

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

    // Arrays to store data received from the realtime database
    List<recipe> recipe_list_search = Collections.synchronizedList(new ArrayList<>());
    List<recipe> recipe_list_filter = Collections.synchronizedList(new ArrayList<>());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        // Create reference to the firebase real time database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Fill the categories list which the user can chose from
        this.fillCategoriesList();

        //    ========================= Get data from user =============================================
        this.createAllNumberPickers();
        this.createAllButtons();
        this.createAllAutoCompleteTextViews();
    }

    private void fillCategoriesList() {
        Set<String> keys = subCategoriesList.keySet();
        categoriesList = new String[keys.size() - 1];
        int i = 0;
        for (String s : keys) {
            if (!s.isEmpty()) {
                categoriesList[i++] = s;
            }
        }
    }

    private void change_adapter() {
        autoCompleteSubCategory = findViewById(R.id.auto_complete_sub_category);
        adapterSubCategories = new ArrayAdapter<String>(this, R.layout.list_items, subCategoriesList.get(category));
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
        adapterSubCategories = new ArrayAdapter<String>(this, R.layout.list_items, subCategoriesList.get(category));
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
            submitRecipe();
//            deleteAllInitData();
//            init_database_with_existing_scraped_data();
//            deleteRecipe("2-Ingredient Pineapple Angel Food Cake");
//            removeDataFromFilterTree("animals", "Pet_Food", "Best Friend Doggie Biscuits");
//            removeDataFromSearchTree("Best Friend Doggie Biscuits");

//            System.out.println("=============================");
////            System.out.println(getDishFromFilterTree("animals", "Pet_Food", "Best Friend Doggie Biscuits"));
//            List<recipe> recipes = getDishFromSearchTree("Air Fryer Mini Breakfast Burritos");
////            List<recipe> recipes = getDishFromSearchTree("Best Friend Doggie Biscuits");
//            System.out.println(recipes.size());
//            for (recipe r : recipes) {
//                System.out.println(r);
//            }
//            List<recipe> recipes2 = getDishFromFilterTree("animals", "Pet_Food", "Best Friend Doggie Biscuits");
//            System.out.println(recipes2.size());
//            for (recipe r : recipes2) {
//                System.out.println(r);
//            }
//            System.out.println("============================");
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
                    npPrepTime.getValue() + "", npCookingTime.getValue() + "", npServings.getValue() + "",
                    npProtein.getValue() + "", "0", npFat.getValue() + "", npCarbs.getValue() + "", 0,
                    new ArrayList<>(), 0, new HashMap<>(), "");
            r.setIngredientsFromList(ingredientsArray);
            Toast.makeText(getApplicationContext(), r.toString(), Toast.LENGTH_SHORT).show();
//            loadDishToFilterTree(r);
//            loadDishToSearchTree(r);
        }
    }

    private String getCleanStringForSearch(String input_str) {
        input_str = input_str.replace("\"", "").replace(" ", "");
        StringBuilder new_str = new StringBuilder();
        for (int i = 0; i < input_str.length(); i++) {
            if (Character.isDigit(input_str.charAt(i)) || Character.isAlphabetic(input_str.charAt(i))) {
                new_str.append(input_str.charAt(i));
            }
        }
        return new_str.toString().toLowerCase(Locale.ROOT);

    }

    private DatabaseReference getToRecipeDepth(DatabaseReference DataRef, String name) {
        DataRef = FirebaseDatabase.getInstance().getReference();
        if (name.length() > 0) {
            String new_name = getCleanStringForSearch(name);
//            System.out.println(new_name);
            int len = new_name.length();
            DataRef = DataRef.child("search");
            // Max tree depth is 32
            for (int i = 0; i < len && i < 27; i++) {
                DataRef = DataRef.child(new_name.charAt(i) + "");
            }
        }
        return DataRef;
    }

    private boolean deleteAllInitData() {
        mDatabase.child("filter").removeValue();
        mDatabase.child("search").removeValue();
        return true;
    }

    public void loadDishToSearchTree(recipe r) {
        if (r != null) {
            String title = getAsCategoryString(r.getTitle());
            DatabaseReference mDatabaseSearch = FirebaseDatabase.getInstance().getReference();
            getToRecipeDepth(mDatabaseSearch, r.getTitle()).child(title).setValue(r)
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

    private String getAsCategoryString(String input_str) {
        return input_str.replace(" ", "_").replace("\"", "");
    }

    public void loadDishToFilterTree(recipe r) {
        if (r != null) {
            String title = r.getTitle();
            String main_category = getAsCategoryString(r.getMain_category());
            String sub_category = getAsCategoryString(r.getCategory());
            DatabaseReference mDatabaseSearch = FirebaseDatabase.getInstance().getReference()
                    .child("filter").child(main_category).child(sub_category);
            mDatabaseSearch.child(getAsCategoryString(title)).setValue(title).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), r.getTitle() + "> added successfully to Filter!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void clearFilterArray() {
        this.recipe_list_filter.clear();
    }

    private void clearSearchArray() {
        this.recipe_list_search.clear();
    }

    private void addToFilerArray(recipe r) {
        this.recipe_list_filter.add(new recipe(r));
    }

    private void addToSearchArray(recipe r) {
        this.recipe_list_search.add(new recipe(r));
    }

    public List<recipe> getDishFromSearchTree(String name) {
        if (!name.isEmpty()) {
            DatabaseReference mDatabaseSearchGet = FirebaseDatabase.getInstance().getReference();
            mDatabaseSearchGet = getToRecipeDepth(mDatabaseSearchGet, name);
            mDatabaseSearchGet.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        DataSnapshot result = task.getResult();
                        if (result.exists()) {
                            clearSearchArray();
                            Iterable<DataSnapshot> childrens = result.getChildren();
                            for (DataSnapshot curr_child : childrens) {
                                try {
                                    recipe current_recipe = curr_child.getValue(recipe.class);
//                                    System.out.println(">>>>>>>" + current_recipe + "<<<<<<<");
                                    if (current_recipe != null && current_recipe.getTitle().equals(name)) {
                                        addToSearchArray(current_recipe);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } else {
                        System.out.println("getDishFromSearchTree - data don't exist");
                    }
                }
            });
        }
        return recipe_list_search;
    }


    public List<recipe> getDishFromFilterTree(String mainCategory, String subCategory, String name) {
        int len = name.length();
        if (!name.isEmpty()) {
            DatabaseReference mDatabaseFilterGet = FirebaseDatabase.getInstance().getReference()
                    .child("filter").child(getAsCategoryString(mainCategory))
                    .child(getAsCategoryString(subCategory));
            mDatabaseFilterGet.child(getAsCategoryString(name)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        clearFilterArray();
                        DataSnapshot snapshot_filter = task.getResult();
                        if (snapshot_filter.exists()) {
                            String curr_recipe_name = snapshot_filter.getValue(String.class);
                            try {
                                if (curr_recipe_name != null && curr_recipe_name.equals(name)) {
                                    for (recipe r : getDishFromSearchTree(curr_recipe_name)) {
//                                        System.out.println(">>>>>>>>>" + r + "<<<<<<<<<<");
                                        addToFilerArray(r);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        System.out.println("getDishFromSearchTree - data don't exist");
                    }
                }
            });
        }
        return recipe_list_filter;
    }


    private void removeDataFromSearchTree(String name){
        DatabaseReference mDataSearchDelete = FirebaseDatabase.getInstance().getReference();
        mDataSearchDelete = getToRecipeDepth(mDataSearchDelete, name);
        mDataSearchDelete.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),
                            name + "> class removed successfully!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void removeDataFromFilterTree(String main_category, String sub_category, String name){
        String mainCategory = getAsCategoryString(main_category);
        String subCategory = getAsCategoryString(sub_category);
        String title = getAsCategoryString(name);

        DatabaseReference mDataFilter = FirebaseDatabase.getInstance().getReference()
                .child("filter").child(mainCategory).child(subCategory);
        mDataFilter.child(title).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),
                            name + "> name removed successfully!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    public void deleteRecipe(String name) {
        DatabaseReference mDataSearch = FirebaseDatabase.getInstance().getReference();
        mDataSearch = getToRecipeDepth(mDataSearch, name);
        mDataSearch.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot_delete = task.getResult();
                    if (snapshot_delete.exists()) {
                        Iterable<DataSnapshot> childrens = snapshot_delete.getChildren();
                        for (DataSnapshot filter_child : childrens) {
                            try {
                                recipe r = filter_child.getValue(recipe.class);
//                                System.out.println(">>>>>>>" + r + "<<<<<<<<");
                                if (r != null && getAsCategoryString(name).equals(getAsCategoryString(r.getTitle()))) {
                                    removeDataFromSearchTree(r.getTitle());
                                    removeDataFromFilterTree(r.getMain_category(), r.getCategory(), r.getTitle());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else{
                        System.out.println("deleteRecipe - data don't exist");
                    }
                }
            }
        });
    }

}
