package com.example.smarter_foodies.ViewModel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.smarter_foodies.Model.CRUD_RealTimeDatabaseData;
import com.example.smarter_foodies.Model.ProfileTabFragment;
import com.example.smarter_foodies.Model.RecipePageFunctions;
import com.example.smarter_foodies.Model.recipe;
import com.example.smarter_foodies.R;
import java.util.ArrayList;
import java.util.Random;

public class likedFragment extends ProfileTabFragment {

    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.profile_frame_liked, container, false);

        mContext = getContext();

        // Initialize RecyclerView
        mRecyclerView = view.findViewById(R.id.recyclerLikedView);
        emptyView = view.findViewById(R.id.tv_empty_recipe_list);
        applyTV = view.findViewById(R.id.tv_apply_as_chef);
        doSomethingIB = view.findViewById(R.id.ib_do_something);

        emptyView.setVisibility(View.GONE);
        applyTV.setVisibility(View.GONE);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        myFoodList = new ArrayList<>();
        CRUD = new CRUD_RealTimeDatabaseData();


        // Set up RecyclerView
        setRecycler("liked");
        setDoSomething();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setRecycler("liked");
    }

    @Override
    public void setDoSomething() {
        // Set mystery box image button - gives a random recipe

        doSomethingIB.setImageResource(R.mipmap.ic_mystery_box);
        doSomethingIB.setBackgroundResource(R.drawable.ic_round_green_oval);
        doSomethingIB.setLayoutParams(new LinearLayout.LayoutParams(150, 150));


        doSomethingIB.setVisibility(View.VISIBLE);
        doSomethingIB.setOnClickListener(view -> {
            int size = myFoodList.size();
            if (size > 0) {
                Random ran = new Random();
                int index = ran.nextInt(size);
                Intent intent = new Intent(getContext(), RecipePage.class);
                recipe res = myFoodList.get(index);
                RecipePageFunctions.setIntentContent(intent, res);
                startActivity(intent);
            }
            else {
                Toast.makeText(mContext, "Add recipes first", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
