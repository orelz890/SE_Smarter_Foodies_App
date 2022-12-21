package com.example.smarter_foodies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Context;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarter_foodies.databinding.ActivityDashboardBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends DashboardActivity {

    FirebaseAuth mAuth;
    CRUD_RealTimeDatabaseData CRUD;
    RecyclerView mRecyclerView;
    List<recipe> myFoodList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        CRUD = new CRUD_RealTimeDatabaseData();
        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        View activityMainView = LayoutInflater.from(this)
                .inflate(R.layout.activity_main, rootLayout, false);
        rootLayout.addView(activityMainView);
        setContentView(rootLayout);
        allocateActivityTitle("Home");

//        deleteAllInitData();
//        init_database_with_existing_scraped_data();

        setRecycleView();

    }

    private boolean deleteAllInitData() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("filter").removeValue();
        mDatabase.child("search").removeValue();
        mDatabase.child("recipes").removeValue();
        return true;
    }

    public boolean init_database_with_existing_scraped_data() {
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
                            JsonObject jsonObject = new JsonParser().parse(json_str)
                                    .getAsJsonObject();
                            String copy_rights = "https://www.allrecipes.com/";
                            recipe curr_recipe = new recipe(jsonObject, copy_rights);
                            System.out.println(curr_recipe);
                            CRUD.loadDishToDatabase(curr_recipe);
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


    private void setRecycleView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 1);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        setRecyclerAdapter();
    }

    private void setRecyclerAdapter() {
        myFoodList = new ArrayList<>();
        DatabaseReference mDatabaseSearchGet = FirebaseDatabase.getInstance()
                .getReference().child("recipes");
        mDatabaseSearchGet.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot_filter) {
                if (snapshot_filter.exists()) {
                    myFoodList.clear();
                    for (DataSnapshot snapshot : snapshot_filter.getChildren()) {
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
                    MyAdapter myAdapter = new MyAdapter(MainActivity.this, myFoodList);
                    mRecyclerView.setAdapter(myAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(MainActivity.this, LoginGoogle.class));
        }
    }
}