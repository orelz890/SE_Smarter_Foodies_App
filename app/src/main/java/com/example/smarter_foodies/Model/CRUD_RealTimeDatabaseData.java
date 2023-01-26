package com.example.smarter_foodies.Model;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CRUD_RealTimeDatabaseData extends AppCompatActivity {

    public Map<String, String[]> subCategoriesList = new HashMap<String, String[]>() {{
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

    DatabaseReference mDatabase;
    // Arrays to store data received from the realtime database
    private final List<recipe> recipe_list_search = new ArrayList<>();
    private final List<recipe> recipe_list_filter = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create reference to the firebase real time database
        mDatabase = FirebaseDatabase.getInstance().getReference();
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
                            System.out.println(curr_recipe);
                            loadDishToDatabase(curr_recipe);
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

    public DatabaseReference getToRecipeDepth(DatabaseReference DataRef, String name) {
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
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("filter").removeValue();
        mDatabase.child("search").removeValue();
        mDatabase.child("recipes").removeValue();
        return true;
    }

    private void loadDishToSearchTree(recipe r) {
        if (r != null) {
            String title = getAsCategoryString(r.getTitle());
            DatabaseReference mDatabaseSearch = FirebaseDatabase.getInstance().getReference();
            getToRecipeDepth(mDatabaseSearch, r.getTitle()).child(title).setValue(r)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                System.out.println(r.getTitle() + "> added successfully to search!");
                            }
                        }
                    });
        }
    }

    public String getAsCategoryString(String input_str) {
        return input_str.replace(" ", "_").replace("\"", "");
    }

    private void loadDishToFilterTree(recipe r) {
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
                        System.out.println(r.getTitle() + "> added successfully to Filter!");
                    }
                }
            });
        }
    }

    private void loadDishToRecipesTree(recipe r) {
        if (r != null) {
            String title = r.getTitle();
            String main_category = getAsCategoryString(r.getMain_category());
            String sub_category = getAsCategoryString(r.getCategory());
            DatabaseReference mDatabaseSearch = FirebaseDatabase.getInstance().getReference()
                    .child("recipes").child(main_category).child(sub_category);
            mDatabaseSearch.child(getAsCategoryString(title)).setValue(r).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        System.out.println(r.getTitle() + "> added successfully to Filter!");
                    }
                }
            });
        }
    }

    public void loadDishToDatabase(recipe r) {
        loadDishToFilterTree(r);
        loadDishToSearchTree(r);
        loadDishToRecipesTree(r);
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


//    // how to get data from the database- search
//        public List<recipe> getDishFromSearchTree(String name) {
//            List<recipe> r = new ArrayList<>();
//            DatabaseReference mDatabaseSearchGet = FirebaseDatabase.getInstance().getReference();
//            mDatabaseSearchGet = getToRecipeDepth(mDatabaseSearchGet, name);
//            mDatabaseSearchGet.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
//                @Override
//                public void onSuccess(DataSnapshot snapshotSearch) {
//                    if (snapshotSearch.exists()) {
//                        for (DataSnapshot child : snapshotSearch.getChildren()) {
//                            System.out.println(child);
//                            recipe curr_recipe = child.getValue(recipe.class);
//                            System.out.println(curr_recipe);
//                            if (curr_recipe != null) {
//                                r.add(new recipe(curr_recipe));
//                            }
//                        }
//                    }
//                }
//            });
//            return r;
//        }


//    // how to get data from the database - filter
//    public void getDishFromFilterTree(String mainCategory, String subCategory, String name) {
//        DatabaseReference mDatabaseSearchGet = FirebaseDatabase.getInstance().getReference()
//                .child("filter").child(getAsCategoryString(mainCategory))
//                .child(getAsCategoryString(subCategory));
//        mDatabaseSearchGet.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
//            @Override
//            public void onSuccess(DataSnapshot snapshotFilter) {
//                if (snapshotFilter.exists()) {
//                    for (DataSnapshot child : snapshotFilter.getChildren()) {
//                        String curr_recipe = child.getValue(String.class);
//                        System.out.println(curr_recipe);
//                    }
//                }
//            }
//        });
//    }


    private void removeDataFromSearchTree(String name) {
        DatabaseReference mDataSearchDelete = FirebaseDatabase.getInstance().getReference();
        mDataSearchDelete = getToRecipeDepth(mDataSearchDelete, name);
        mDataSearchDelete.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    System.out.println(name + "> class removed successfully!");
                }
            }
        });
    }

    private void removeDataFromFilterTree(String main_category, String sub_category, String name) {
        String mainCategory = getAsCategoryString(main_category);
        String subCategory = getAsCategoryString(sub_category);
        String title = getAsCategoryString(name);

        DatabaseReference mDataFilter = FirebaseDatabase.getInstance().getReference()
                .child("filter").child(mainCategory).child(subCategory);
        mDataFilter.child(title).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    System.out.println(name + "> name removed successfully!");
                }
            }
        });

    }

    private void removeDataFromRecipesTree(String main_category, String sub_category, String name) {
        String mainCategory = getAsCategoryString(main_category);
        String subCategory = getAsCategoryString(sub_category);
        String title = getAsCategoryString(name);

        DatabaseReference mDataFilter = FirebaseDatabase.getInstance().getReference()
                .child("recipes").child(mainCategory).child(subCategory);
        mDataFilter.child(title).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    System.out.println(name + "> name removed successfully!");
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
                                    removeDataFromRecipesTree(r.getMain_category(), r.getCategory(), r.getTitle());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        System.out.println("deleteRecipe - data don't exist");
                    }
                }
            }
        });
    }

    public void updateRecipe(String former_name, recipe newRecipe) {
        if (newRecipe != null) {
//            deleteRecipe(former_name);
            loadDishToDatabase(newRecipe);

        }
    }

    public static void updateUserAsChef(String email) {
        String preUserEmail = email.replace(".", "{*}");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("email").child(preUserEmail);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    System.out.println("email doesn't exist");
                } else {
                    String uid = dataSnapshot.getValue(String.class);
                    updateUser(uid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG", error.getMessage());
            }
        });

    }

    private static void updateUser(String uid) {
        Map<String, Object> userUpdates = new HashMap<>();
        userUpdates.put("isChef", true);
        FirebaseDatabase.getInstance().getReference().child("users").child(uid)
                .updateChildren(userUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("success to update user");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.err.println("error to update user");
                    }
                });
    }

    public void addToUserLists(List<String> newRecipes, String listName) {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid != null) {
            DatabaseReference usersRef = FirebaseDatabase.getInstance()
                    .getReference().child("users").child(uid);
            usersRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            switch (listName) {
                                case "recipes":
                                    user.addToUserRecipes(newRecipes);
                                    break;
                                case "cart":
                                    user.addToCart(newRecipes);
                                    break;
                                case "liked":
                                    user.addToLiked(newRecipes);
                                    break;
                            }
                            DatabaseReference usersRef = FirebaseDatabase.getInstance()
                                    .getReference().child("users").child(uid);
                            usersRef.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });
                        }
                    }
                }
            });

        }

    }

    public void removeFromUserLists(List<String> delList, String listName) {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid != null) {
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference()
                    .child("users").child(uid);
            usersRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            switch (listName) {
                                case "recipes":
                                    user.removeFromUserRecipes(delList);
                                    break;
                                case "cart":
                                    user.removeFromCart(delList);
                                    break;
                                case "liked":
                                    user.removeFromLiked(delList);
                                    break;
                            }
                            DatabaseReference usersRef = FirebaseDatabase.getInstance()
                                    .getReference().child("users").child(uid);
                            usersRef.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });
                        }
                    }
                }
            });
        }
    }


    public List<String> getSingleValueAsCategoryList(String title) {
        List<String> strings = new ArrayList<>();
        strings.add(getAsCategoryString(title));
        return strings;
    }

    public List<String> getSingleValueList(String title) {
        List<String> strings = new ArrayList<>();
        strings.add(title);
        return strings;
    }


//    public void uploadImageToImageStorage(ImageView iv, recipe r, int i, Uri imgUri) {
//
//
//        if (iv.getDrawable() == null) {
//            // First, create a reference to the image file in Firebase Storage
//            FirebaseStorage storage = FirebaseStorage.getInstance();
//            StorageReference storageRef = storage.getReference()
//                    .child("images")
//                    .child(getAsCategoryString(r.getTitle()))
//                    .child("" + i);
//            // Upload the file to Firebase Storage
//            storageRef.putFile(imgUri)
//                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            StorageMetadata metadata = taskSnapshot.getMetadata();
//                            if (metadata != null) {
//                                StorageReference reference = metadata.getReference();
//                                if (reference != null) {
//                                    // Get the URL of the image
//                                    String imageUrl = reference.getDownloadUrl().toString();
//                                    // Save the URL to the Firebase Realtime Database
//                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
//                                    DatabaseReference myRef = database.getReference("image_url");
//                                    myRef.setValue(imageUrl);
//                                    uploadImageToRecipeImages(r.getMain_category(), r.getCategory(),
//                                            r.getTitle(), imageUrl, i);
//                                }
//                            }
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            // Handle the error
//                            System.out.println(e.getStackTrace());
//                        }
//                    });
//        }
//    }

//    public void uploadImageToRecipeImages(String category, String subCategory, String title, Uri imgUri, int pos) {
//        String path = imgUri.getPath();
//        // Convert image to base64-encoded string
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        Bitmap bitmap = BitmapFactory.decodeFile(path);
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] imageData = baos.toByteArray();
//        String imageDataBase64 = Base64.encodeToString(imageData, Base64.DEFAULT);
//        String main_category = getAsCategoryString(category);
//        String sub_category = getAsCategoryString(subCategory);
//        DatabaseReference mDatabaseSearch = FirebaseDatabase.getInstance().getReference()
//                .child("recipes").child(main_category).child(sub_category);
//        mDatabaseSearch.child(getAsCategoryString(title)).child("images").child("" + pos)
//                .setValue(imageDataBase64).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        e.printStackTrace();
//                    }
//                });
//        mDatabaseSearch = FirebaseDatabase.getInstance().getReference()
//                .child("search").child(main_category).child(sub_category);
//        mDatabaseSearch.child(getAsCategoryString(title)).child("images").child("" + pos)
//                .setValue(imageDataBase64).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        e.printStackTrace();
//                    }
//                });
//    }
}


