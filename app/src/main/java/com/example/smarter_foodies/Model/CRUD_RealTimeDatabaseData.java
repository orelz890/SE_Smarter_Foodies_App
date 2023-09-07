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
import java.util.Objects;

public class CRUD_RealTimeDatabaseData extends AppCompatActivity {

    public Map<String, String[]> subCategoriesList = new HashMap<String, String[]>() {{
        put("", new String[]{});
        put("breakfast", new String[]{"Breakfast And Brunch", "Breakfast Burritos", "Breakfast Casseroles", "Breakfast Potatoes", "Breakfast Strata", "Deviled Eggs", "Eggplant Parmesan", "Frittata", "Omelets", "Other"});
        put("cakes", new String[]{"Angel Cake", "Cake Recipes", "Carrot Cake", "Cheesecake", "Chocolate Cake", "Coffee Cake", "Crab Cakes", "Cupcakes", "Fruitcake", "Linguine", "Mashed Potatoes", "Pound Cake", "Shortcake", "Spice Cake", "Upside Down Cake"});
        put("carbs", new String[]{"Calzones", "Dumplings", "Egg Rolls", "Empanada Recipes", "Fried Rice", "Fries", "Gnocchi", "Homemade Pasta", "Noodle Casserole", "Pancit", "Pasta Carbonara", "Pasta Primavera", "Potato Pancakes", "Quesadillas", "Quiche", "Quinoa", "Ravioli", "Rice Casserole", "Rice Pilaf", "Risotto", "Samosa Recipes", "Shepherd's Pie", "Spaghetti", "Spanish Rice", "Tater_Tots Casserole", "Tortellini", "Tortillas", "Tostadas", "Tuna Casserole", "Ziti", "Other"});
        put("dairy", new String[]{"Macaroni & Cheese", "Chowder", "Other"});
        put("dinner", new String[]{"Dinner_Recipes", "Other"});
        put("dips", new String[]{"Artichoke Dip", "Buffalo Chicken Dip", "Cranberry Sauce", "Gravy", "Guacamole", "Pesto Sauce", "Relishes", "Salsa", "Spinach Dips", "Other"});
        put("drinks", new String[]{"Applesauce", "Bloody Marys", "Cocktails", "Eggnog", "Jell Shots", "Lemonade", "Margaritas", "Mojitos", "Punch", "Sangria", "Smoothies", "Other"});
        put("easy", new String[]{"Everyday_Leftovers", "Quick_and_Easy_Recipes", "Other"});
        put("fish & sushi", new String[]{"Ceviche", "Salmon", "Sushi", "Other"});
        put("flour", new String[]{"Bagels", "Banana Bread", "Bread Recipes", "Bruschetta", "Cornbread", "Crackers", "Flat Bread", "French Toast", "Garlic Bread", "Hushpuppies", "Monkey Bread", "Panini", "Pierogi", "Pretzels", "Pumpkin Bread", "Sandwiches", "Scones", "Wheat Bread", "Yeast Bread", "Zucchini Bread", "Other"});
        put("comfort", new String[]{"Comfort Food", "Food Gifts", "Other"});
        put("health", new String[]{"Baked Beans", "Broccoli Salad", "Chicken Salad", "Diabetic Recipes", "Egg Salad", "Fruit Salads", "Gluten-Free Recipes", "Granola", "Green Salads", "Healthy Recipes", "High-Fiber Recipes", "Keto Diet", "Low-Calorie Recipes", "Low-Cholesterol Recipes", "Low-Fat Recipes", "Low-Sodium Recipes", "Low Glycemic Impact Recipes", "Mediterranean Diet", "Oatmeal", "Overnight Oats", "Paleo Diet", "Potato Salad", "Raw Food Diet", "Refried Beans", "Salad Dressings", "Salad Recipes", "Seitan Recipes", "Sugar-Free Recipes", "Tempeh Recipes", "Tofu Recipes", "Vegan Recipes", "Vegetable Recipes", "Vegetable Side Dishes", "Vegetarian Recipes", "Other"});
        put("holidays", new String[]{"Falafel", "Paella", "Tacos", "Tamales", "Other"});
        put("lunch", new String[]{"Lunch Recipes"});
        put("main dishes", new String[]{"Casseroles", "Fajitas", "Lasagna", "Lettuce Wraps", "Main Dishes", "Manicotti", "Quiche", "Stew"});
        put("meat & chicken", new String[]{"Bulgogi", "Burgers", "Chili", "Gyros", "Jerky", "Kalbi", "Ribs", "Roasts"});
        put("per specific ingredients", new String[]{"Mushroom Recipes", "Pickles", "Stuffed Bell Peppers", "Stuffed Mushrooms", "Winter Squash_Recipes", "Yam Recipes"});
        put("pies", new String[]{"Apple Pie", "Blueberry Pie", "Cherry Pie", "Chess Pie", "Key Lime Pie", "Mincemeat Pie", "Pecan Pie", "Pie Crusts", "Pie Recipes", "Pot Pie", "Pumpkin Pie", "Rhubarb Pie", "Shepherd's Pie", "Slab Pie", "Strawberry Pie", "Sweet Potato Pie", "Whoopie Pies", "Other"});
        put("pizza", new String[]{"Pizza", "Pizza Dough And Crusts", "Other"});
        put("pork", new String[]{"Ground Pork", "Pork Chops", "Pork Recipes", "Pork Ribs", "Pork Shoulder", "Pork Tenderloin", "Pulled Pork", "Other"});
        put("preserved", new String[]{"Canning_and_Preserving", "Other"});
        put("salads", new String[]{"Broccoli Salad", "Chicken Salad", "Coleslaw", "Egg Salad", "Fruit Salads", "Green Salads", "Pasta Salad", "Potato Salad", "Salad Dressings", "Salad Recipes", "Taco Salad", "Tomato Salad", "Tuna Salad", "Waldorf Salad", "Other"});
        put("sea fruit", new String[]{"Etouffee", "Jambalaya", "Shrimp And Grits", "Shrimp Scampi", "Other"});
        put("side dishes", new String[]{"Grits", "Hummus", "Jalapeno Poppers", "Meal Prep", "Pate", "Polenta", "Side Dishes", "Tapas Recipes", "Other"});
        put("snacks & sweets", new String[]{"Appetizers And Snacks", "Bar Cookies", "Biscotti", "Biscuit", "Blintz", "Blondies", "Brownies", "Cheese Balls", "Cheese Fondue", "Chocolate Chip Cookies", "Chocolate Fudge", "Christmas Cookies", "Cinnamon Rolls", "Cobbler", "Cookies", "Creme Brulee", "Crisps And Crumbles", "Danishes", "Desserts", "Divinity", "Doughnuts", "Drop Cookies", "Energy Balls", "English Muffins", "Flan", "Fondant", "Frosting And Icing Recipes", "Fudge", "Gingerbread Cookies", "Gingersnaps", "Ice Cream", "Jams and_Jellies", "Kolache", "Lemon Bars", "Macaroons", "Mousse", "Muffins", "Nachos", "Oatmeal Cookies", "Pancakes", "Pasties", "Pastries", "Pavlova", "Peanut Butter Cookies", "Popcorn", "Popovers And Yorkshire Pudding", "Rice Pudding", "Road Trip Snacks", "Sandwich Cookies", "Seder Recipes", "Shortbread Cookies", "Snickerdoodles", "Spritz Cookies", "Strawberry Shortcake", "Sugar Cookies", "Thumbprint Cookies", "Tiramisu", "Toffee", "Tortes", "Truffles", "Waffles", "Other"});
        put("soups", new String[]{"Borscht", "Butternut Squash Soup", "Chicken Noodle Soup", "French Onion Soup", "Gazpacho", "Gumbo", "Lentil Soup", "Minestrone Soup", "Mushroom Soup", "Potato Soup", "Soup", "Split Pea Soup", "Other"});
        put("other", new String[]{"Other"});
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
                    .child("recipes").child(main_category).child(sub_category).child(getAsCategoryString(title));
            mDatabaseSearch.setValue(r).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        System.out.println(r.getTitle() + "> added successfully to Filter!");
                        FirebaseDatabase.getInstance().getReference().child("myRecipes")
                                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                                .child(title)
                                .setValue(r);
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
                .child("recipes").child(mainCategory).child(subCategory).child(title);
        mDataFilter.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    System.out.println(name + "> name removed successfully!");
                    FirebaseDatabase.getInstance().getReference().child("myRecipes")
                            .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                            .child(name)
                            .removeValue();
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


