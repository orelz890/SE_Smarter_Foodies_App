package com.example.smarter_foodies.ViewModel;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.smarter_foodies.DashboardActivity;
import com.example.smarter_foodies.Model.CRUD_RealTimeDatabaseData;
import com.example.smarter_foodies.Model.MyAdapter;
import com.example.smarter_foodies.Model.MyLikedAndCartAdapter;
import com.example.smarter_foodies.Model.RecipePageFunctions;
import com.example.smarter_foodies.Model.User;
import com.example.smarter_foodies.Model.recipe;
import com.example.smarter_foodies.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class myUploads extends DashboardActivity {


    FirebaseAuth mAuth;
    CRUD_RealTimeDatabaseData CRUD;
    RecyclerView mRecyclerView;
    List<recipe> myFoodList;
    SwipeRefreshLayout swipeRefreshLayout;
    MyLikedAndCartAdapter myAdapter;
    ImageButton imageButton;
    TextView tvRecipeCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        CRUD = new CRUD_RealTimeDatabaseData();
        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        View activityMainView = LayoutInflater.from(this)
                .inflate(R.layout.activity_my_uploads, rootLayout, false);
        rootLayout.addView(activityMainView);
        setContentView(rootLayout);
        allocateActivityTitle("My uploads");

        myFoodList = new ArrayList<>();

        setSwipeRefresh();
        setTextViews();
        setRecycleView();

    }

    private void setTextViews() {
        tvRecipeCount = findViewById(R.id.tv_recipe_count_up);
        setRecipeCount();
    }

    private void setRecipeCount(){
        if (myFoodList != null) {
            tvRecipeCount.setText(myFoodList.size() + " uploads");
        }
    }


    private void setSwipeRefresh() {
        swipeRefreshLayout = findViewById(R.id.liked_recycler_layout_up);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (myAdapter != null) {
                    Collections.shuffle(myFoodList);
                    myAdapter.notifyDataSetChanged();
                    setRecipeCountViews(myFoodList.size());
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private void setRecycleView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerLikedView_up);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(myUploads.this, 1);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        myFoodList = new ArrayList<>();
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid != null) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                    .getReference().child("users").child(uid).child("myRecipes");
            databaseReference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {

                        // Create task to receive the actual recipe/s from the database
                        List<Task<Object>> tasks = CRUD.getTasksFromDataSnapshot(dataSnapshot);

                        Tasks.whenAllSuccess(tasks).addOnSuccessListener(snapshots -> {
                            // Handle the results when all tasks are successful

                            // Fill myFoodList with the recipes received.
                            for (Object snapshot : snapshots) {
                                if (snapshot instanceof DataSnapshot) {
                                    DataSnapshot dataSnapshot1 = (DataSnapshot) snapshot;
                                    // Process each user's data
                                    recipe curr_recipe = dataSnapshot1.getValue(recipe.class);
                                    if (curr_recipe != null) {
                                        myFoodList.add(curr_recipe);
                                    }
                                }
                            }

                            // Set the recipe count text view
                            setRecipeCount();

                            // Shuffle the recipe list
                            Collections.shuffle(myFoodList);

                            // Get the height and width of the screen
                            int screenWidth = getResources().getDisplayMetrics().widthPixels;
                            int screenHeight = getResources().getDisplayMetrics().heightPixels;

                            // Set the recycler view adapter
                            myAdapter = new MyLikedAndCartAdapter(myUploads.this, myFoodList, "myUploads", screenWidth, screenHeight);
                            mRecyclerView.setAdapter(myAdapter);
                        })
                        .addOnFailureListener(e -> {
                            // Handle failure
                            System.out.println("setByNameRecyclerAdapter - whenAllSuccess - Failed");
                            e.printStackTrace();
                        });
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println("setRecycleView - Failure: ");
                    e.printStackTrace();
                }
            });
        }
    }


    public void setRecipeCountViews(int num) {
        if (myFoodList != null) {
            tvRecipeCount.setText(num + " recipes");
        }
    }

}