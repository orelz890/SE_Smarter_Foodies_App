package com.example.smarter_foodies.ViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.smarter_foodies.Model.CRUD_RealTimeDatabaseData;
import com.example.smarter_foodies.Model.ProfileTabFragment;
import com.example.smarter_foodies.R;
import java.util.ArrayList;



public class myRecipesFragment extends ProfileTabFragment {

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
        setRecycler("myRecipes");
        setDoSomething();
        setTextViews();

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        setRecycler("myRecipes");
    }

    private void setTextViews(){
        applyTV.setVisibility(View.VISIBLE);
        applyTV.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, ApplyChef.class);
            startActivity(intent);
        });
    }


}
