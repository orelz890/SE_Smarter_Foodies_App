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
import android.widget.Toast;

import com.example.smarter_foodies.DashboardActivity;
import com.example.smarter_foodies.Model.CRUD_RealTimeDatabaseData;
import com.example.smarter_foodies.Model.MyLikedAndCartAdapter;
import com.example.smarter_foodies.Model.RecipePageFunctions;
import com.example.smarter_foodies.Model.User;
import com.example.smarter_foodies.Model.recipe;
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
import java.util.List;
import java.util.Map;
import java.util.Random;


public class likedRecipes extends DashboardActivity {

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
                .inflate(R.layout.activity_liked_recipes, rootLayout, false);
        rootLayout.addView(activityMainView);
        setContentView(rootLayout);
        allocateActivityTitle("Liked");

        myFoodList = new ArrayList<>();

        setSwipeRefresh();
        setTextViews();
        setRecycleView();
        setImageButtons();

    }

    private void setTextViews() {
        tvRecipeCount = findViewById(R.id.tv_recipe_count);
        if (myFoodList != null) {
            tvRecipeCount.setText(myFoodList.size() + " recipes");
        }
    }

    private void setImageButtons() {
        imageButton = findViewById(R.id.ib_mystery_box_up);
        imageButton.setOnClickListener(view -> {
            int size = myFoodList.size();
            Random ran = new Random();
            int index = ran.nextInt(size);
            Intent intent = new Intent(likedRecipes.this, RecipePage.class);
            recipe res= myFoodList.get(index);
            RecipePageFunctions.setIntentContent(intent,res);
            startActivity(intent);
        });

    }


    private void setSwipeRefresh() {
        swipeRefreshLayout = findViewById(R.id.liked_recycler_layout);
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
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerLikedView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(likedRecipes.this, 1);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        setRecycler();
    }


    private void setRecycler() {
        myFoodList = new ArrayList<>();
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid != null) {
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                    .getReference().child("users").child(uid);
            databaseReference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            List<Task<Object>> tasks = CRUD.getTasksFromRefMap(user.getLiked());

                            Tasks.whenAllSuccess(tasks).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                                @Override
                                public void onSuccess(List<Object> snapshots) {
                                    // Handle the results when all tasks are successful

                                    handleSuccess(tasks, databaseReference.child("liked"), user.getLiked());
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
                }
            });
        }
    }

    private void handleSuccess(List<Task<Object>> tasks, DatabaseReference databaseRef, Map<String,String> liked) {
        // Fill myFoodList with the recipes received.
        myFoodList.clear();
        for (int i = 0; i < tasks.size(); i++) {
            Object snapshot = tasks.get(i).getResult();
            if (snapshot instanceof DataSnapshot) {
                DataSnapshot dataSnapshot = (DataSnapshot) snapshot;
                // Process each user's data
                recipe r = dataSnapshot.getValue(recipe.class);
                if (r != null) {
                    myFoodList.add(r);
                    liked.remove(r.getTitle());
                }
            }
        }
        // Remove redundant child's - their actual recipe was deleted
        for (String k : liked.keySet()) {
            databaseRef.child(k).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    System.out.println("Removed redundant child");
                }
            });
        }
        // Show result in view
        if (myFoodList != null && myFoodList.size() > 0){

            tvRecipeCount.setText(myFoodList.size() + " recipes");
            Collections.shuffle(myFoodList);

            // Get the height and width of the screen
            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            int screenHeight = getResources().getDisplayMetrics().heightPixels;

            myAdapter = new MyLikedAndCartAdapter(likedRecipes.this, myFoodList, "liked", screenWidth, screenHeight);
            mRecyclerView.setAdapter(myAdapter);
        }

    }


    public void setRecipeCountViews(int num) {
        if (myFoodList != null) {
            tvRecipeCount.setText(num + " recipes");
        }
    }


}
