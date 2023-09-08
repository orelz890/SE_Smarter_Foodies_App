package com.example.smarter_foodies.ViewModel;

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
import com.example.smarter_foodies.Model.User;
import com.example.smarter_foodies.Model.recipe;
import com.example.smarter_foodies.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class WeeklyPlan extends DashboardActivity {

    FirebaseAuth mAuth;
    CRUD_RealTimeDatabaseData CRUD;
    RecyclerView mRecyclerView;
    List<recipe> myFoodList;
    SwipeRefreshLayout swipeRefreshLayout;
    MyLikedAndCartAdapter myAdapter;
    TextView tvRecipeCount;
    ImageButton ingredientList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        CRUD = new CRUD_RealTimeDatabaseData();
        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        View activityMainView = LayoutInflater.from(this)
                .inflate(R.layout.activity_cart, rootLayout, false);
        rootLayout.addView(activityMainView);
        setContentView(rootLayout);
        allocateActivityTitle("My Cart");

        ingredientList = findViewById(R.id.ib_create_cart_list);
        myFoodList = new ArrayList<>();

        setSwipeRefresh();
        setTextViews();
        setRecycleView();
        setImageButtons();

        ingredientList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToWhatsappIngredientList();
            }
        });

    }

    private void sendToWhatsappIngredientList() {

        String list = String.valueOf(createIngredientList());
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, list);
        intent.setType("text/plain");
        intent.setPackage("com.whatsapp");
        try {
            startActivity(intent);
        } catch (Exception exception) {
            Toast.makeText(WeeklyPlan.this, "There is no application that support this action",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private StringBuilder createIngredientList() {
        StringBuilder ans = new StringBuilder();
        try {
            ArrayList<String> ingredients = new ArrayList<>();
            ArrayList<Integer> amounts = new ArrayList<>();
            ArrayList<String> titles = new ArrayList<>();
            for (recipe recipe:myFoodList) {
                titles.add(recipe.getTitle());
                List<String> r_Ingre = recipe.getIngredients();
                for (String ingredient:r_Ingre) {
                    if (contain(ingredients, (ingredient.split(":")[1]))){
                        amounts.set(ingredients.indexOf(ingredient.split(":")[1]), amounts.get(ingredients.indexOf(ingredient.split(":")[1])) + Integer.parseInt(ingredient.split(":")[0]));
                    }else{
                        amounts.add(Integer.parseInt(ingredient.split(":")[0]));
                        ingredients.add(ingredient.split(":")[1]);
                    }
                }

            }
            ans.append("*Your Weekly Plan Recipes:*\n");
            for (String title:titles) {
                ans.append(title).append("\n");

            }
            ans.append("\n*Your Grocery List:*\n");
            for(int i = 0; i < amounts.size(); i++){
                ans.append(amounts.get(i)).append("[g]").append(" ").append(ingredients.get(i)).append("\n");
            }
        }catch (Exception e){
            System.out.println(e);
            Toast.makeText(WeeklyPlan.this, "You need to get the new recipes' version",
                    Toast.LENGTH_SHORT).show();
        }


        return ans;
    }

    private boolean contain(ArrayList<String> ingredients, String s) {
        for (String ingredient:ingredients) {
            if(ingredient.equals(s)){
                return true;
            }
        }
        return false;
    }


    private void setTextViews() {
        tvRecipeCount = findViewById(R.id.tv_cart_recipe_count);
        if (myFoodList != null) {
            tvRecipeCount.setText(myFoodList.size() + " recipes");
        }
    }

    private void setImageButtons() {
//        ImageButton ingredientList = findViewById(R.id.ib_list);

    }



    private void setSwipeRefresh () {
        swipeRefreshLayout = findViewById(R.id.cart_recycler_layout);
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

    private void setRecycleView () {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerCartView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(WeeklyPlan.this, 1);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        setRecycler();
    }


    private void setRecycler () {
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
                            setRecyclerAdapter(user.getCart());
                        }
                    }
                }
            });
        }
    }


    private void setRecyclerAdapter (List < String > cart) {
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
                                if (r != null && cart.contains(r.getTitle()) && !myFoodList.contains(r)) {
                                    myFoodList.add(r);
                                }
                            }
                        }
                    }
                    if (myFoodList != null) {
                        tvRecipeCount.setText(myFoodList.size() + " recipes");
                    }
                    Collections.shuffle(myFoodList);
                    myAdapter = new MyLikedAndCartAdapter(WeeklyPlan.this, myFoodList, "cart");
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
