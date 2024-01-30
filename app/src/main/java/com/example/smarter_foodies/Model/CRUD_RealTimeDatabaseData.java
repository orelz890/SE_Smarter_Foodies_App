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
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
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

    public Map<String, String[]> subCategoriesList = new HashMap<>() {{
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

    private DatabaseReference mDatabase;
    // Arrays to store data received from the realtime database
    private final List<recipe> recipe_list_search = new ArrayList<>();
    private final List<recipe> recipe_list_filter = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create reference to the firebase real time database
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

//    private boolean init_database_with_existing_scraped_data() {
//        try {
//            AssetManager assetManager = getAssets();
//            String[] files = assetManager.list("per_category_data2");
//            for (String f : files) {
//                String[] files2 = assetManager.list("per_category_data2/" + f);
//                System.out.println(">>>>>>>>>>>>>>> " + f + " <<<<<<<<<<<<<<<<<<<");
//                for (String f2 : files2) {
//                    String[] files3 = assetManager.list("per_category_data2/" + f + "/" + f2);
//
////                        System.out.println(f2);
//                    for (String f3 : files3) {
//                        try {
//                            String file_path = "per_category_data2/" + f + "/" + f2 + "/" + f3;
//                            InputStream inputStream = getAssets().open(file_path);
//                            int size = inputStream.available();
//                            byte[] buffer = new byte[size];
//                            inputStream.read(buffer);
//                            String json_str = new String(buffer);
//                            JsonObject jsonObject = new JsonParser().parse(json_str).getAsJsonObject();
//                            String copy_rights = "https://www.allrecipes.com/";
//                            recipe curr_recipe = new recipe(jsonObject, copy_rights);
//                            System.out.println(curr_recipe);
//                            loadDishToDatabase(curr_recipe);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            return false;
//                        }
//                    }
//                    System.out.println("\n=====================================\n");
//                }
//            }
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return true;
//    }

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


    public DatabaseReference getToRecipeDepth(String name) {
        DatabaseReference DataRef = FirebaseDatabase.getInstance().getReference();
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


    public String getAsCategoryString(String input_str) {
        return input_str.replace(" ", "_").replace("\"", "");
    }


    public void loadDishToDatabase(recipe r) {
        addRecipeToRecipes(r);
    }


    private void addRecipeToFilter(recipe r) {
        if (r != null) {
            String title = r.getTitle();
            String main_category = getAsCategoryString(r.getMain_category());
            String sub_category = getAsCategoryString(r.getCategory());
            DatabaseReference mDatabaseSearch = FirebaseDatabase.getInstance().getReference()
                    .child("filter").child(main_category).child(sub_category);
            mDatabaseSearch.child(getAsCategoryString(title)).setValue(title).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    System.out.println(title + "> added successfully to Filter!");
                } else {
                    System.out.println("Failed to add - " + title + " to Filter!");
                }
            });
        }
    }


    private void addRecipeToRecipes(recipe r) {
        if (r != null) {
            String title = r.getTitle();
            String main_category = getAsCategoryString(r.getMain_category());
            String sub_category = getAsCategoryString(r.getCategory());

            String recipeRefString = "recipes/" + main_category + "/" + sub_category + "/" + getAsCategoryString(title);
            r.setDatabaseRef(recipeRefString);

            DatabaseReference mDatabaseRecipes = FirebaseDatabase.getInstance().getReference().child(recipeRefString);


            mDatabaseRecipes.setValue(r).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    System.out.println(r.getTitle() + "> added successfully to recipes!");
                    addRecipeToSearch(r, recipeRefString);
                } else {
                    System.out.println(r.getTitle() + "> failed to add to recipes!");
                    removeDataFromRecipesTree(r.getMain_category(), r.getCategory(), title);
                }
            }).addOnFailureListener(e -> {
                System.out.println("addRecipeToRecipes - Failed");
                e.printStackTrace();
                removeDataFromRecipesTree(r.getMain_category(), r.getCategory(), title);
            });
        }
    }


    private void addRecipeToSearch(recipe r, String mDatabaseRecipes) {
        if (mDatabaseRecipes != null) {
            String title = r.getTitle();
            String cleanTitle = getAsCategoryString(title);
            getToRecipeDepth(title).child(cleanTitle).setValue(mDatabaseRecipes)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            System.out.println(title + "> added successfully to search!");
                            addRecipeToUserUploads(r, mDatabaseRecipes);
                        } else {
                            removeDataFromRecipesTree(r.getMain_category(), r.getCategory(), title);
                        }
                    }).addOnFailureListener(e -> {
                        System.out.println("removeDataFromFilterTree - Failed");
                        e.printStackTrace();
                        removeDataFromRecipesTree(r.getMain_category(), r.getCategory(), title);
                    });
        }
    }


    private void addRecipeToUserUploads(recipe r, String mDatabaseRecipes) {
        String title = r.getTitle();
        FirebaseDatabase.getInstance().getReference().child("users")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .child("myRecipes")
                .child(title)
                .setValue(mDatabaseRecipes).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        System.out.println("Recipe added to the user uploaded recipes.");
                        addRecipeToFilter(r);
                    } else {
//                            removeDataFromSearchTree(title);
                        removeDataFromRecipesTree(r.getMain_category(), r.getCategory(), title);
                    }
                }).addOnFailureListener(e -> {
                    System.out.println("addRecipeToUserUploads - Failed");
                    e.printStackTrace();
                    removeDataFromRecipesTree(r.getMain_category(), r.getCategory(), title);
                });
    }


    private void removeDataFromUserUploads(String main_category, String sub_category, String title) {

        FirebaseDatabase.getInstance().getReference().child("users")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .child("myRecipes")
                .child(title).removeValue().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        System.out.println(title + "> class removed successfully from user uploads!");
                        removeDataFromFilterTree(main_category, sub_category, title);
                    }
                }).addOnFailureListener(e -> {
                    System.out.println("removeDataFromUserUploads - Failed");
                    e.printStackTrace();
                });
    }

    private void removeDataFromSearchTree(String main_category, String sub_category, String title) {
        DatabaseReference mDataSearchDelete = FirebaseDatabase.getInstance().getReference();
        mDataSearchDelete = getToRecipeDepth(title);
        mDataSearchDelete.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                System.out.println(title + "> class removed successfully from search!");
                removeDataFromUserUploads(main_category, sub_category, title);
            }
        }).addOnFailureListener(e -> {
            System.out.println("removeDataFromSearchTree - Failed");
            e.printStackTrace();
        });

    }

    private void removeDataFromFilterTree(String main_category, String sub_category, String name) {
        String mainCategory = getAsCategoryString(main_category);
        String subCategory = getAsCategoryString(sub_category);
        String title = getAsCategoryString(name);

        DatabaseReference mDataFilter = FirebaseDatabase.getInstance().getReference()
                .child("filter").child(mainCategory).child(subCategory);
        mDataFilter.child(title).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                System.out.println(name + "> name removed successfully from filter!");
            }
        }).addOnFailureListener(e -> {
            System.out.println("removeDataFromFilterTree - Failed");
            e.printStackTrace();
        });

    }

    private void removeDataFromRecipesTree(String main_category, String sub_category, String title) {
        String mainCategory = getAsCategoryString(main_category);
        String subCategory = getAsCategoryString(sub_category);
        String cleanTitle = getAsCategoryString(title);

        DatabaseReference mDataFilter = FirebaseDatabase.getInstance().getReference()
                .child("recipes").child(mainCategory).child(subCategory).child(cleanTitle);
        mDataFilter.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                System.out.println(title + "> name removed successfully!");
                removeDataFromSearchTree(main_category, sub_category, title);
            }
        }).addOnFailureListener(e -> {
            System.out.println("removeDataFromRecipesTree - Failed");
            e.printStackTrace();
        });

    }


    public void deleteRecipe(String name) {
        final DatabaseReference mDataSearch = getToRecipeDepth(name);
        mDataSearch.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot_delete = task.getResult();
                if (snapshot_delete.exists()) {
                    Iterable<DataSnapshot> childrens = snapshot_delete.getChildren();

                    List<Task<Object>> refToRemove = new ArrayList<>();

                    // Add all the references under this name in the search tree
                    for (DataSnapshot filter_child : childrens) {
                        String recipeRefString = filter_child.getValue(String.class);
                        if (recipeRefString != null){
                            DatabaseReference recipeRef =  FirebaseDatabase.getInstance().getReference().child(recipeRefString);
                            Task<Object> getRefTask = fetchDataTask(recipeRef);
//                            if (!getRefTask.isSuccessful())
                            refToRemove.add(getRefTask);
                        }
                    }

                    Tasks.whenAllSuccess(refToRemove)
                            .addOnSuccessListener(snapshots -> {
                                // Handle success, all removal tasks completed successfully
                                for (Object snapshot : snapshots) {
                                    if (snapshot instanceof DataSnapshot) {
                                        DataSnapshot dataSnapshot = (DataSnapshot) snapshot;
                                        // Process each user's data
                                        recipe r = dataSnapshot.getValue(recipe.class);
                                        if (r != null) {
                                            removeDataFromRecipesTree(r.getMain_category(), r.getCategory(), r.getTitle());
                                        }
                                    }
                                }
                            })
                            .addOnFailureListener(e -> {
                                // Handle failure, at least one removal task failed
                                System.out.println("deleteRecipe - whenAllSuccess - Failed");
                                e.printStackTrace();
                            });


                } else {
                    System.out.println("deleteRecipe - data don't exist");
                }
            }
        });
    }

    public Task<Object> fetchDataTask(DatabaseReference recipeRef) {
        if (recipeRef != null) {
            final TaskCompletionSource<Object> taskCompletionSource = new TaskCompletionSource<>();

            recipeRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    taskCompletionSource.setResult(task.getResult());
                } else {
                    taskCompletionSource.setException(Objects.requireNonNull(task.getException()));
                }
            });

            return taskCompletionSource.getTask();
        }
        return Tasks.forResult(null); // Return a completed task if recipeRef is null
    }

    private void handleRemoveDataTask(DatabaseReference reference, TaskCompletionSource<Object> taskCompletionSource) {
        if (reference != null) {
            reference.removeValue((databaseError, databaseReference) -> {
                if (databaseError == null) {
                    taskCompletionSource.setResult(null); // Task completed successfully
                } else {
                    taskCompletionSource.setException(databaseError.toException()); // Task failed
                }
            });
        } else {
            taskCompletionSource.setResult(null); // Return a completed task if reference is null
        }
    }


    public void addToUserLists(Map<String,String> newRecipes, String listName) {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid != null) {
            DatabaseReference usersRef = FirebaseDatabase.getInstance()
                    .getReference().child("users").child(uid);
            usersRef.get().addOnSuccessListener(dataSnapshot -> {
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
                        DatabaseReference usersRef1 = FirebaseDatabase.getInstance()
                                .getReference().child("users").child(uid);
                        usersRef1.setValue(user).addOnCompleteListener(task -> {

                        });
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
            usersRef.get().addOnSuccessListener(dataSnapshot -> {
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
                        DatabaseReference usersRef1 = FirebaseDatabase.getInstance()
                                .getReference().child("users").child(uid);
                        usersRef1.setValue(user).addOnCompleteListener(task -> {

                        });
                    }
                }
            });
        }
    }

    public List<String> getSingleValueList(String title) {
        List<String> strings = new ArrayList<>();
        strings.add(title);
        return strings;
    }

    public Map<String,String> getSingleValueMap(String k,String v) {
        Map<String,String> m = new HashMap<>();
        m.put(k,v);
        return m;
    }

    public List<Task<Object>> getTasksFromRefMap(Map<String,String> m){
        List<Task<Object>> tasks = new ArrayList<>();

        for (String v : m.values()) {
        // Convert the saved string back to DatabaseReference
            if (v != null) {
                DatabaseReference restoredDatabaseReference = FirebaseDatabase.getInstance().getReference().child(v);//                        Task<DataSnapshot> task = getUserDataTask(userReference);
                Task<Object> task = fetchDataTask(restoredDatabaseReference);

                tasks.add(task);
            }
        }
        return tasks;
    }

    public List<Task<Object>> getTasksFromDataSnapshot(DataSnapshot dataSnapshot, DatabaseReference databaseRef){
        List<Task<Object>> tasks = new ArrayList<>();
        if (dataSnapshot.exists()) {

            for (DataSnapshot child : dataSnapshot.getChildren()) {
                DatabaseReference ref;
                if (child.getValue() instanceof String){
                    String refString = child.getValue(String.class);
                    if (refString != null){
                        ref = FirebaseDatabase.getInstance().getReference().child(refString);
                    }
                    else {
                        continue;
                    }
                }
                else if (child.getValue() instanceof DatabaseReference) {
                    ref = child.getValue(DatabaseReference.class);

                }
                else {
                    continue;
                }
                Task<Object> task = fetchDataTask(ref);
                tasks.add(task);
            }
        }
        return tasks;
    }
}


