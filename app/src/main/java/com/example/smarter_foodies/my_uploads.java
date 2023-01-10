package com.example.smarter_foodies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class my_uploads extends DashboardActivity {


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
        setImageButtons();

    }

    private void setTextViews() {
        tvRecipeCount = findViewById(R.id.tv_recipe_count_up);
        if (myFoodList != null) {
            tvRecipeCount.setText(myFoodList.size() + " uploads");
        }
    }

    private void setImageButtons() {
        imageButton = findViewById(R.id.ib_mystery_box_up);
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

        GridLayoutManager gridLayoutManager = new GridLayoutManager(my_uploads.this, 1);
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
                            setRecyclerAdapter(user.getMyRecipes());
                        }
                    }
                }
            });
        }
    }


    private void setRecyclerAdapter(List<String> my_upload) {

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
                                if (r != null && my_upload.contains(r.getTitle()) && !myFoodList.contains(r)) {
                                    myFoodList.add(r);
                                }
                            }
                        }
                    }
                    if (myFoodList != null) {
                        tvRecipeCount.setText(myFoodList.size() + " recipes");
                    }
                    Collections.shuffle(myFoodList);
                    myAdapter = new MyLikedAndCartAdapter(my_uploads.this, myFoodList, "my_uploads");
                    mRecyclerView.setAdapter(myAdapter);
                }
            }
        });
    }

    public void setRecipeCountViews(int num) {
        if (myFoodList != null) {
            tvRecipeCount.setText(num + " recipes");
        }
    }

}