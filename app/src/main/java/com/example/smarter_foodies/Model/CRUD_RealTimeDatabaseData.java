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



//    <TODO> Get filter data from database instead of locally.



    /*
     * Returns a DatabaseReference of the recipe in the search tree.
     * @param name - Recipe name.
     * */
    public DatabaseReference getToRecipeDepth(String name) {
        DatabaseReference DataRef = FirebaseDatabase.getInstance().getReference();
        if (name.length() > 0) {
            DataRef = DataRef.child("search").child(name);
        }
        return DataRef;
    }


    public String getAsCategoryString(String input_str) {
        return input_str.replace(" ", "_").replace("\"", "");
    }


    /*
     * Add the recipe object and necessary data to the database(pipe).
     * @param recipe - only value
     * */
    public void loadDishToDatabase(recipe r) {
        addRecipeToRecipes(r);
    }


    /*
     * Add the recipe name to the filter tree.
     * If the operation fails - remove already saved data related.
     * @param recipe - only value
     * */
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


    /*
     * Add the recipe object to the recipe tree + pass it on to addRecipeToSearch.
     * If the operation fails - remove already saved data related.
     * @param recipe - only value
     * */
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

    /*
     * Add the recipe reference to the search tree + pass it on to addRecipeToUserUploads.
     * If the operation fails - remove already saved data related.
     * @param recipe - object
     * @param recipe - recipe reference in the database
     * */
    private void addRecipeToSearch(recipe r, String mDatabaseRecipes) {
        if (mDatabaseRecipes != null) {
            String title = r.getTitle();
            getToRecipeDepth(title).setValue(mDatabaseRecipes)
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

    /*
     * Add the recipe reference to the user own recipes tree + pass it on to addRecipeToFilter.
     * If the operation fails - remove already saved data related.
     * @param recipe - object
     * @param recipe - recipe reference in the database
     * */
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


    /*
     * Remove the recipe reference from the user own recipes tree + pass it on to removeDataFromFilterTree.
     * @param main_category - recipe main category
     * @param sub_category - recipe sub category
     * @param title - recipe name
     * */
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


    /*
     * Remove the recipe reference from the search tree + pass it on to removeDataFromFilterTree.
     * @param main_category - recipe main category
     * @param sub_category - recipe sub category
     * @param title - recipe name
     * */
    private void removeDataFromSearchTree(String main_category, String sub_category, String title) {
        DatabaseReference mDataSearchDelete = FirebaseDatabase.getInstance().getReference().child("search").child(title);
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


    /*
     * Remove the recipe name from the filter tree.
     * @param main_category - recipe main category
     * @param sub_category - recipe sub category
     * @param title - recipe name     * */
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


    /*
     * Remove the recipe object from the recipes tree + pass it on to removeDataFromSearchTree.
     * @param main_category - recipe main category
     * @param sub_category - recipe sub category
     * @param title - recipe name     * */
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


    /*
     * Removes the recipe and related data from the database(pipe).
     * @param name - recipe name
     * */
    public void deleteRecipe(String name) {
        final DatabaseReference mDataSearch = FirebaseDatabase.getInstance().getReference().child("search").child(name);
        mDataSearch.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot_delete = task.getResult();

                if (snapshot_delete.exists()) {

                    // Add all the references under this name in the search tree
                    String recipeRefString = snapshot_delete.getValue(String.class);
                    if (recipeRefString != null) {
                        String[] spited = recipeRefString.split("/");
                        System.out.println("recipeRefString = " + recipeRefString);
                        removeDataFromRecipesTree(spited[1],spited[2],name);
                    }

                } else {
                    System.out.println("deleteRecipe - data don't exist");
                }
            }
        });
    }


    /*
     * Create task from a request in order to fetch more then on request in a single query.
     * @param recipeRef - database reference to get from.
     * */
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


//    private void handleRemoveDataTask(DatabaseReference reference, TaskCompletionSource<Object> taskCompletionSource) {
//        if (reference != null) {
//            reference.removeValue((databaseError, databaseReference) -> {
//                if (databaseError == null) {
//                    taskCompletionSource.setResult(null); // Task completed successfully
//                } else {
//                    taskCompletionSource.setException(databaseError.toException()); // Task failed
//                }
//            });
//        } else {
//            taskCompletionSource.setResult(null); // Return a completed task if reference is null
//        }
//    }


    /*
     * Add the recipes to the right list and update the dataset.
     * @param newRecipes - recipes to add
     * @param listName - which list to add to
     * */
    public void addToUserLists(Map<String, String> newRecipes, String listName) {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid != null) {
            DatabaseReference usersRef = FirebaseDatabase.getInstance()
                    .getReference().child("users").child(uid);
            usersRef.get().addOnSuccessListener(dataSnapshot -> {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {

                        DatabaseReference userRef = FirebaseDatabase.getInstance()
                                .getReference().child("users").child(uid);

                        switch (listName) {
                            case "recipes":
                                user.addToUserRecipes(newRecipes);
                                userRef.child("myRecipes").setValue(user.getMyRecipes());
                                break;
                            case "cart":
                                user.addToCart(newRecipes);
                                userRef.child("cart").setValue(user.getCart());
                                break;
                            case "liked":
                                user.addToLiked(newRecipes);
                                userRef.child("liked").setValue(user.getLiked());
                                break;
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

    public Map<String, String> getSingleValueMap(String k, String v) {
        Map<String, String> m = new HashMap<>();
        m.put(k, v);
        return m;
    }


    /*
     * Create tasks list from a map in order to fetch more then on request in a single query.
     * @param m - Map with string of database references as values.
     * */
    public List<Task<Object>> getTasksFromRefMap(Map<String, String> m) {
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


    /*
     * Create tasks list from a DataSnapshot in order to fetch more then on request in a single query.
     * @param m - DataSnapshot.
     * */
    public List<Task<Object>> getTasksFromDataSnapshot(DataSnapshot dataSnapshot) {
        List<Task<Object>> tasks = new ArrayList<>();
        if (dataSnapshot.exists()) {

            for (DataSnapshot child : dataSnapshot.getChildren()) {
                DatabaseReference ref;
                if (child.getValue() instanceof String) {
                    String refString = child.getValue(String.class);
                    if (refString != null) {
                        ref = FirebaseDatabase.getInstance().getReference().child(refString);
                    } else {
                        continue;
                    }
                } else if (child.getValue() instanceof DatabaseReference) {
                    ref = child.getValue(DatabaseReference.class);

                } else {
                    continue;
                }
                Task<Object> task = fetchDataTask(ref);
                tasks.add(task);
            }
        }
        return tasks;
    }
}


