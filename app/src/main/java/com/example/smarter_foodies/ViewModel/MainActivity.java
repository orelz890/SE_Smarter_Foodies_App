package com.example.smarter_foodies.ViewModel;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.smarter_foodies.DashboardActivity;
import com.example.smarter_foodies.Model.CRUD_RealTimeDatabaseData;
import com.example.smarter_foodies.Model.MyAdapter;
import com.example.smarter_foodies.Model.recipe;
import com.example.smarter_foodies.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class MainActivity extends DashboardActivity {
    private AutoCompleteTextView autoCompleteSearchView;
    private ImageButton ib_filter;
    private ImageButton ib_refresh;
    private ArrayAdapter<String> arraySearchAdapter;
    private List<String> recipesNamesList;
    private CRUD_RealTimeDatabaseData CRUD;
    private RecyclerView mRecyclerView;
    private List<recipe> myFoodList;
    private MyAdapter myAdapter;

    private AutoCompleteTextView autoCompleteCategory;
    private ArrayAdapter<String> adapterCategories;
    private AutoCompleteTextView autoCompleteSubCategory;
    private ArrayAdapter<String> adapterSubCategories;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String category;
    private String subCategory;
    private String[] categoriesList;
    private int screenWidth;
    private int screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        View activityMainView = LayoutInflater.from(this).inflate(R.layout.activity_main, rootLayout, false);
        rootLayout.addView(activityMainView);
        setContentView(rootLayout);
        allocateActivityTitle("Home");

        CRUD = new CRUD_RealTimeDatabaseData();
        recipesNamesList = new ArrayList<>();
        myFoodList = new ArrayList<>();
        category = "";
        subCategory = "";
        // Get the height and width of the screen
        this.screenWidth = getResources().getDisplayMetrics().widthPixels;
        this.screenHeight = getResources().getDisplayMetrics().heightPixels;

        defineRecycleView();
        setMainRecyclerAdapter();
        setSwipeRefresh();
        InitAutoCompleteSearchView();
        InitManageSearchImageButton();
    }


    private void setSwipeRefresh() {
        swipeRefreshLayout = findViewById(R.id.search_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (myAdapter != null) {
                    Collections.shuffle(myFoodList);
                    myAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private void InitManageSearchImageButton() {
        ib_filter = findViewById(R.id.ib_manage_search);
        ib_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click event here
                setFirstDialog();
            }
        });

        ib_refresh = findViewById(R.id.ib_refresh_filter);
        ib_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click event here
                if (autoCompleteSearchView != null) {
                    autoCompleteSearchView.setText("");
                }
                setMainRecyclerAdapter();
            }
        });
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
        adapterSubCategories = new ArrayAdapter<String>(this, R.layout.list_items, CRUD.subCategoriesList.get(category));
        autoCompleteSubCategory.setAdapter(adapterSubCategories);
    }

    private void createAllAutoCompleteTextViews() {
        fillCategoriesList();
        category = "";
        subCategory = "";
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

    private void setFirstDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Light_Dialog);
        final View customLayout = getLayoutInflater().inflate(R.layout.filter_search_dialog, null);
        builder.setView(customLayout);
        builder.setCancelable(false);
        builder.setTitle("Filter");
        autoCompleteCategory = customLayout.findViewById(R.id.auto_complete_category);
        autoCompleteSubCategory = customLayout.findViewById(R.id.auto_complete_sub_category);
        createAllAutoCompleteTextViews();
        builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
//                System.out.println("category- " + category + "\tsubCategory- " + subCategory);
                if (!category.isEmpty()) {
                    adjustAutoCompleteFilterView();
                }
            }
        });

        builder.setNegativeButton("cancel", (dialogInterface, which) -> {
            InitAutoCompleteSearchView();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Set the filter names list + set adapter for the autoCompleteSearchView
    private void InitAutoCompleteSearchView() {
        autoCompleteSearchView = findViewById(R.id.ac_searchView);

        DatabaseReference mDatabaseSearchGet = FirebaseDatabase.getInstance()
                .getReference().child("filter");
        mDatabaseSearchGet.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    recipesNamesList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Iterable<DataSnapshot> categorySnapshot = snapshot.getChildren();
                        for (DataSnapshot subCategorySnapshot : categorySnapshot) {
                            Iterable<DataSnapshot> recipeNamesSnapshot = subCategorySnapshot.getChildren();
                            for (DataSnapshot recipeNameSnap : recipeNamesSnapshot) {
                                String name = recipeNameSnap.getValue(String.class);
                                recipesNamesList.add(name);
                            }
                        }
                    }
                    arraySearchAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_activated_1, recipesNamesList);
                    autoCompleteSearchView.setAdapter(arraySearchAdapter);
                    autoCompleteSearchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                            String recipeName = parent.getItemAtPosition(pos).toString();
                            setByNameRecyclerAdapter(recipeName);
                        }
                    });
                    autoCompleteSearchView.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            // Hide my keyboard
                            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), 0);
                        }
                    });
                }
            }
        });
    }

    private void adjustAutoCompleteFilterView() {
        autoCompleteSearchView = findViewById(R.id.ac_searchView);
        DatabaseReference mDatabaseSearchGet = FirebaseDatabase.getInstance()
                .getReference().child("filter");
        if (!this.category.isEmpty()) {
            mDatabaseSearchGet = mDatabaseSearchGet.child(CRUD.getAsCategoryString(category));
            setByCategoryRecyclerAdapter(category);
        }
        if (!this.subCategory.isEmpty()) {
            mDatabaseSearchGet = mDatabaseSearchGet.child(CRUD.getAsCategoryString(subCategory));
            setBySubCategoryRecyclerAdapter(category, subCategory);
        }

        mDatabaseSearchGet.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    recipesNamesList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Iterable<DataSnapshot> categorySnapshot = snapshot.getChildren();
                        if (subCategory.isEmpty()) {
                            for (DataSnapshot recipeNameSnap : categorySnapshot) {
                                String name = recipeNameSnap.getValue(String.class);
                                recipesNamesList.add(name);
                            }
                        } else {
                            String name = snapshot.getValue(String.class);
                            recipesNamesList.add(name);
                        }
                    }

                    arraySearchAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_activated_1, recipesNamesList);
                    autoCompleteSearchView.setAdapter(arraySearchAdapter);
                    autoCompleteSearchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                            String recipeName = parent.getItemAtPosition(pos).toString();
                            setByNameRecyclerAdapter(recipeName);
                        }
                    });
                    autoCompleteSearchView.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            // Hide my keyboard
                            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), 0);
                        }
                    });
                }
            }
        });
    }

    public void defineRecycleView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerSearchView);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(SearchRecipe.this, 1);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);

        int horizontalSpacingInPixels = getResources().getDimensionPixelSize(R.dimen.horizontal_spacing);
        int spacing = getResources().getDimensionPixelSize(R.dimen.spacing);
        int recipeCrdWidth = getResources().getDimensionPixelSize(R.dimen.main_activity_recipe_width);

// Calculate the number of columns based on available width considering both card width and spacing
        int totalItemWidth = recipeCrdWidth + horizontalSpacingInPixels;
        int numCols = (this.screenWidth - spacing) / totalItemWidth;


        int verticalSpacingInPixels = getResources().getDimensionPixelSize(R.dimen.vertical_spacing);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, numCols);


        mRecyclerView.setLayoutManager(gridLayoutManager);

        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(numCols, horizontalSpacingInPixels, verticalSpacingInPixels, true));
    }

    private void setByNameRecyclerAdapter(String recipeName) {
        // how to get data from the database- search
        DatabaseReference mDatabaseSearchGet = FirebaseDatabase.getInstance().getReference();
        mDatabaseSearchGet = CRUD.getToRecipeDepth(recipeName);
        mDatabaseSearchGet.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    myFoodList.clear();
                    List<Task<DataSnapshot>> tasks = CRUD.getTasksFromDataSnapshot(dataSnapshot);

                    Tasks.whenAllSuccess(tasks).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                        @Override
                        public void onSuccess(List<Object> snapshots) {
                            // Handle the results when all tasks are successful
                            for (Object snapshot : snapshots) {
                                if (snapshot instanceof DataSnapshot) {
                                    DataSnapshot dataSnapshot = (DataSnapshot) snapshot;
                                    // Process each user's data
                                    recipe curr_recipe = dataSnapshot.getValue(recipe.class);
                                    if (curr_recipe != null) {
                                        myFoodList.add(curr_recipe);
                                    }
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle failure
                            System.out.println("setByNameRecyclerAdapter - whenAllSuccess - Failed");
                            e.printStackTrace();
                        }
                    });

                    Collections.shuffle(myFoodList);

                    myAdapter = new MyAdapter(MainActivity.this, myFoodList, screenWidth, screenHeight);
                    mRecyclerView.setAdapter(myAdapter);
                }
            }

        });

    }




//    private Task<DataSnapshot> getUserDataTask(DatabaseReference userReference /* , String type */) {
//        if (userReference != null) {
//            final TaskCompletionSource<DataSnapshot> taskCompletionSource = new TaskCompletionSource<>();
//            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    taskCompletionSource.setResult(dataSnapshot);
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    taskCompletionSource.setException(databaseError.toException());
//                }
//            });
//
////            switch (type) {
////                case "recipes":
////                    return taskCompletionSource.getTask();
////
////            }
//            return taskCompletionSource.getTask();
//        }
//        return null;
//    }

    private void setMainRecyclerAdapter() {
        DatabaseReference mDatabaseSearchGet = FirebaseDatabase.getInstance()
                .getReference().child("recipes");
        mDatabaseSearchGet.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    myFoodList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String category = snapshot.getKey();
                        if (category != null && !category.equals("animals")) {
                            Iterable<DataSnapshot> categorySnapshot = snapshot.getChildren();
                            for (DataSnapshot subCategorySnapshot : categorySnapshot) {
                                Iterable<DataSnapshot> recipeNamesSnapshot
                                        = subCategorySnapshot.getChildren();
                                for (DataSnapshot recipeNameSnap : recipeNamesSnapshot) {
                                    recipe name = recipeNameSnap.getValue(recipe.class);
                                    myFoodList.add(name);
                                }
                            }
                        }
                    }
                    Collections.shuffle(myFoodList);
                    // Get the height and width of the screen
                    int screenWidth = getResources().getDisplayMetrics().widthPixels;
                    int screenHeight = getResources().getDisplayMetrics().heightPixels;

                    myAdapter = new MyAdapter(MainActivity.this, myFoodList, screenWidth, screenHeight);
                    mRecyclerView.setAdapter(myAdapter);
                }
            }
        });
    }

    private void setByCategoryRecyclerAdapter(String category) {
        DatabaseReference mDatabaseSearchGet = FirebaseDatabase.getInstance()
                .getReference().child("recipes").child(CRUD.getAsCategoryString(category));
        mDatabaseSearchGet.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    myFoodList.clear();
                    for (DataSnapshot subCategorySnapshot : dataSnapshot.getChildren()) {
                        Iterable<DataSnapshot> recipeNamesSnapshot
                                = subCategorySnapshot.getChildren();
                        for (DataSnapshot recipeNameSnap : recipeNamesSnapshot) {
                            recipe name = recipeNameSnap.getValue(recipe.class);
                            myFoodList.add(name);
                        }
                    }
                    Collections.shuffle(myFoodList);
                    // Get the height and width of the screen
                    int screenWidth = getResources().getDisplayMetrics().widthPixels;
                    int screenHeight = getResources().getDisplayMetrics().heightPixels;

                    myAdapter = new MyAdapter(MainActivity.this, myFoodList, screenWidth, screenHeight);
                    mRecyclerView.setAdapter(myAdapter);
                }
            }
        });
    }

    private void setBySubCategoryRecyclerAdapter(String category, String subCategory) {
        DatabaseReference mDatabaseSearchGet = FirebaseDatabase.getInstance()
                .getReference().child("recipes").child(CRUD.getAsCategoryString(category))
                .child(CRUD.getAsCategoryString(subCategory));
        mDatabaseSearchGet.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    myFoodList.clear();
                    for (DataSnapshot recipeNameSnap : dataSnapshot.getChildren()) {
                        recipe name = recipeNameSnap.getValue(recipe.class);
                        myFoodList.add(name);
                    }
                    Collections.shuffle(myFoodList);
                    // Get the height and width of the screen
                    int screenWidth = getResources().getDisplayMetrics().widthPixels;
                    int screenHeight = getResources().getDisplayMetrics().heightPixels;

                    myAdapter = new MyAdapter(MainActivity.this, myFoodList, screenWidth, screenHeight);
                    mRecyclerView.setAdapter(myAdapter);
                }
            }
        });
    }
}
