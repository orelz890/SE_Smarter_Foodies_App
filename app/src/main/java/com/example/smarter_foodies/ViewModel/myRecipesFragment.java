package com.example.smarter_foodies.ViewModel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarter_foodies.Model.CRUD_RealTimeDatabaseData;
import com.example.smarter_foodies.Model.MyLikedAndCartAdapter;
import com.example.smarter_foodies.Model.ProfileTabFragment;
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


public class myRecipesFragment extends ProfileTabFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.profile_frame_liked, container, false);

        // Initialize RecyclerView
        mRecyclerView = view.findViewById(R.id.recyclerLikedView);
        emptyView = view.findViewById(R.id.tv_empty_recipe_list);
        emptyView.setVisibility(View.GONE);
        applyTV = view.findViewById(R.id.tv_apply_as_chef);
        applyTV.setVisibility(View.GONE);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        myFoodList = new ArrayList<>();
        CRUD = new CRUD_RealTimeDatabaseData();


        // Set up RecyclerView
        setRecycler("myRecipes");

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        setRecycler("myRecipes");
    }
}
