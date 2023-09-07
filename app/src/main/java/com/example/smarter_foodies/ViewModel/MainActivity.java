package com.example.smarter_foodies.ViewModel;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.smarter_foodies.DashboardActivity;
import com.example.smarter_foodies.Model.CRUD_RealTimeDatabaseData;
import com.example.smarter_foodies.Model.MyAdapter;
import com.example.smarter_foodies.Model.recipe;
import com.example.smarter_foodies.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class MainActivity extends DashboardActivity {
    AutoCompleteTextView autoCompleteSearchView;
    ImageButton ib_filter;
    ImageButton ib_refresh;
    ArrayAdapter<String> arraySearchAdapter;
    List<String> recipesNamesList;
    CRUD_RealTimeDatabaseData CRUD;
    RecyclerView mRecyclerView;
    List<recipe> myFoodList;
    MyAdapter myAdapter;

    AutoCompleteTextView autoCompleteCategory;
    ArrayAdapter<String> adapterCategories;
    AutoCompleteTextView autoCompleteSubCategory;
    ArrayAdapter<String> adapterSubCategories;
    SwipeRefreshLayout swipeRefreshLayout;
    String category;
    String subCategory;
    String[] categoriesList;


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
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, androidx.appcompat.R.style.Base_V7_Theme_AppCompat_Dialog);
        final View customLayout = getLayoutInflater().inflate(R.layout.filter_search_dialog, null);
        builder.setView(customLayout);
        builder.setCancelable(false);
        builder.setTitle("Recipe name");
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
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);

// Adjust the spacing as needed
        int horizontalSpacingInPixels = getResources().getDimensionPixelSize(R.dimen.horizontal_spacing);
        int verticalSpacingInPixels = getResources().getDimensionPixelSize(R.dimen.vertical_spacing);
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, horizontalSpacingInPixels, verticalSpacingInPixels, true));
    }

    private void setByNameRecyclerAdapter(String recipeName) {
        // how to get data from the database- search
        DatabaseReference mDatabaseSearchGet = FirebaseDatabase.getInstance().getReference();
        mDatabaseSearchGet = CRUD.getToRecipeDepth(mDatabaseSearchGet, recipeName);
        mDatabaseSearchGet.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    myFoodList.clear();
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        recipe curr_recipe = child.getValue(recipe.class);
                        myFoodList.add(curr_recipe);
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
