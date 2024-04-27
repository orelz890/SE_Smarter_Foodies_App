package com.example.smarter_foodies.ViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.smarter_foodies.DashboardActivity;
import com.example.smarter_foodies.Model.CRUD_RealTimeDatabaseData;
import com.example.smarter_foodies.Model.MyLikedAndCartAdapter;
import com.example.smarter_foodies.Model.RecipePageFunctions;
import com.example.smarter_foodies.Model.ReportsAdapter;
import com.example.smarter_foodies.Model.User;
import com.example.smarter_foodies.Model.recipe;
import com.example.smarter_foodies.Model.report;
import com.example.smarter_foodies.R;
import com.google.android.gms.tasks.OnCompleteListener;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class reportedRecipes extends DashboardActivity {

    FirebaseAuth mAuth;
    CRUD_RealTimeDatabaseData CRUD;
    RecyclerView mRecyclerView;
    List<recipe> myReportedList;
    SwipeRefreshLayout swipeRefreshLayout;
    ReportsAdapter myAdapter;
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
                .inflate(R.layout.activity_reported_recipes, rootLayout, false);
        rootLayout.addView(activityMainView);
        setContentView(rootLayout);
        allocateActivityTitle("Reports");

        myReportedList = new ArrayList<>();

        setSwipeRefresh();
        setTextViews();
        setRecycleView();
    }

    private void setTextViews() {
        tvRecipeCount = findViewById(R.id.tv_recipe_count);
        if (myReportedList != null) {
            tvRecipeCount.setText(myReportedList.size() + " recipes");
        }
    }


    private void setSwipeRefresh() {
        swipeRefreshLayout = findViewById(R.id.liked_recycler_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (myAdapter != null) {
                    Collections.shuffle(myReportedList);
                    myAdapter.notifyDataSetChanged();
                    setRecipeCountViews(myReportedList.size());
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private void setRecycleView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerReportedView);


        // Creating spacing between items
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        mRecyclerView.setLayoutManager(layoutManager);
//
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
//                layoutManager.getOrientation());
//        mRecyclerView.addItemDecoration(dividerItemDecoration);

        int horizontalSpacing = getResources().getDimensionPixelSize(R.dimen.horizontal_spacing); // Adjust as needed
        int verticalSpacing = getResources().getDimensionPixelSize(R.dimen.vertical_spacing); // Adjust as needed
        boolean includeEdge = true; // Adjust based on your layout


        GridLayoutManager gridLayoutManager = new GridLayoutManager(reportedRecipes.this, 1);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        GridSpacingItemDecoration itemDecoration = new GridSpacingItemDecoration(1, horizontalSpacing, verticalSpacing, includeEdge);
        mRecyclerView.addItemDecoration(itemDecoration);


        // Set the items
        setRecycler();
    }


    private void setRecycler() {
        myReportedList = new ArrayList<>();
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid != null) {
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                    .getReference().child("reports");
            databaseReference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        System.out.println("\n\n" + dataSnapshot + "\n\n");
                        List<Task<Object>> tasks = CRUD.getTasksFromDataSnapshot(dataSnapshot);

                        Tasks.whenAllSuccess(tasks).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                            @Override
                            public void onSuccess(List<Object> snapshots) {
                                // Handle the results when all tasks are successful

                                handleSuccess(tasks);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle failure
                                System.out.println("likedRecipes - setRecycler - whenAllSuccess - Failed");
                                e.printStackTrace();
                            }
                        });


                    }
                }
            });
        }
    }

    private void handleSuccess(List<Task<Object>> tasks) {
        // Fill myFoodList with the recipes received.
        myReportedList.clear();
        for (int i = 0; i < tasks.size(); i++) {
            Object snapshot = tasks.get(i).getResult();
            if (snapshot instanceof DataSnapshot) {
                DataSnapshot dataSnapshot = (DataSnapshot) snapshot;
                // Process each user's data
                recipe r = dataSnapshot.getValue(recipe.class);
                if (r != null) {
                    myReportedList.add(r);
                }
            }
        }

        // Show result in view
        if (myReportedList != null && myReportedList.size() > 0){

            tvRecipeCount.setText(myReportedList.size() + " recipes");
            Collections.shuffle(myReportedList);

            // Get the height and width of the screen
            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            int screenHeight = getResources().getDisplayMetrics().heightPixels;

            myAdapter = new ReportsAdapter(reportedRecipes.this, myReportedList, screenWidth, screenHeight, CRUD);
            mRecyclerView.setAdapter(myAdapter);
        }

    }


    public void setRecipeCountViews(int num) {
        if (myReportedList != null) {
            tvRecipeCount.setText(num + " recipes");
        }
    }



}
