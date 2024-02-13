package com.example.smarter_foodies.ViewModel;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.smarter_foodies.Model.CRUD_RealTimeDatabaseData;
import com.example.smarter_foodies.Model.ProfileTabFragment;
import com.example.smarter_foodies.R;
import java.util.ArrayList;


public class cartFragment extends ProfileTabFragment {

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
        setRecycler("cart");

        return view;
    }

    
    @Override
    public void onResume() {
        super.onResume();
        setRecycler("cart");
    }
}
