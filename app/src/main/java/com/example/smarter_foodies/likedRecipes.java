package com.example.smarter_foodies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class likedRecipes extends DashboardActivity {

    FirebaseAuth mAuth;
    CRUD_RealTimeDatabaseData CRUD;
    RecyclerView mRecyclerView;
    List<recipe> myFoodList;
    SwipeRefreshLayout swipeRefreshLayout;
    MyLikedAdapter myAdapter;
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
        allocateActivityTitle("Likes");

        setSwipeRefresh();
        setImageButtons();
        setTextViews();
        setRecycleView();
    }

    private void setTextViews() {
        tvRecipeCount = findViewById(R.id.tv_recipe_count);
        if (myFoodList != null) {
            tvRecipeCount.setText(myFoodList.size() + " recipes");
        }
    }

    private void setImageButtons() {
        imageButton = findViewById(R.id.ib_manage_search);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!myFoodList.isEmpty()) {
                    Random random = new Random();
                    // Generate a random index
                    int index = random.nextInt(myFoodList.size());
                    // Get the element at the random index
                    recipe randomRecipe = myFoodList.get(index);
                    setRecipeDialog(randomRecipe);

                }
            }
        });
    }


    private void setRecipeDialog(recipe randomRecipe) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(likedRecipes.this, androidx.appcompat.R.style.Base_V7_Theme_AppCompat_Dialog);
//        final View customLayout = getLayoutInflater().inflate(R.layout.yes_no_dialog_layout, null);
//        builder.setView(customLayout);
//        builder.setCancelable(false);
//        builder.setTitle("Are you sure?");
//        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int which) {
//                CRUD.deleteRecipe(name);
//                CRUD.removeFromUserLists(CRUD.getSingleValueList(name), "recipes");
//                startActivity(new Intent(UpdateRecipe.this, MainActivity.class));
//            }
//        });
//
//        builder.setNegativeButton("no", (dialogInterface, which) -> {
//
//        });
//        AlertDialog dialog = builder.create();
//        dialog.show();
    }


    private void setSwipeRefresh() {
        swipeRefreshLayout = findViewById(R.id.liked_recycler_layout);
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
            DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                    .getReference().child("users").child(uid);
            databaseReference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            setRecyclerAdapter(user.getLiked());
                        }
                    }
                }
            });
        }
    }


    private void setRecyclerAdapter(List<String> liked) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference().child("recipes");
        databaseReference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    myFoodList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Iterable<DataSnapshot> categorySnapshot = snapshot.getChildren();
                        for (DataSnapshot subCategorySnapshot : categorySnapshot) {
                            Iterable<DataSnapshot> recipeNamesSnapshot
                                    = subCategorySnapshot.getChildren();
                            for (DataSnapshot recipeNameSnap : recipeNamesSnapshot) {
                                recipe r = recipeNameSnap.getValue(recipe.class);
                                if (r != null && liked.contains(r.getTitle())) {
                                    myFoodList.add(r);
                                }
                            }
                        }
                    }
                    if (myFoodList != null) {
                        tvRecipeCount.setText(myFoodList.size() + " recipes");
                    }
                    Collections.shuffle(myFoodList);
                    myAdapter = new MyLikedAdapter(likedRecipes.this, myFoodList);
                    mRecyclerView.setAdapter(myAdapter);
                }
            }
        });
    }


}
