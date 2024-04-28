package com.example.smarter_foodies.Model;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarter_foodies.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProfileTabFragment extends Fragment {

    protected List<recipe> myFoodList;
    protected CRUD_RealTimeDatabaseData CRUD;
    protected RecyclerView mRecyclerView;
    protected MyLikedAndCartAdapter myAdapter;
    protected TextView emptyView, applyTV;
    protected int screenWidth, screenHeight;
    protected ImageButton doSomethingIB;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.profile_frame_liked, container, false);

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

    public void setDoSomething(){
        doSomethingIB.setVisibility(View.GONE);
    }

    public void setRecycler(String userListName) {
        applyTV.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);

        myFoodList = new ArrayList<>();
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid != null) {
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                    .getReference().child("users").child(uid).child(userListName);

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    setView(snapshot, databaseReference, userListName);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void setView(DataSnapshot snapshot, DatabaseReference databaseReference, String userListName){
        try {
            applyTV.setVisibility(View.GONE);
            emptyView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);

            Map<String, String> refMap = (Map<String, String>) snapshot.getValue();
            if (refMap != null && !refMap.isEmpty()) {
//                                    System.out.println("\n\nrefMap.values()\n\n" + refMap.values() + "\n\n");
                List<Task<Object>> tasks = CRUD.getTasksFromRefMap(refMap);

                Tasks.whenAllSuccess(tasks).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                    @Override
                    public void onSuccess(List<Object> snapshots) {
                        // Handle the results when all tasks are successful

                        handleSuccess(tasks, databaseReference, refMap, userListName);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                        System.out.println(userListName + " Recipes - setRecycler - whenAllSuccess - Failed");
                        e.printStackTrace();
                    }
                });
            }
            // Map is empty
            else {
                System.out.println("\n\nLike recipes to save them here\n\n");
                if (userListName.equals("liked")) {
                    emptyView.setText("Like recipes to save them here");
                }
                else if (userListName.equals("cart")) {
                    emptyView.setText("Make your grocery list by adding recipes to cart");
                }
                else if (userListName.equals("myRecipes")){
                    emptyView.setText("Share your talent with fellow foodies");
                    applyTV.setVisibility(View.VISIBLE);
                }
                emptyView.setVisibility(View.VISIBLE);

            }
        }
        catch (Exception e){
            System.out.println("\n\nException\n\n");
            e.printStackTrace();
        }
    }
    private void handleSuccess(List<Task<Object>> tasks, DatabaseReference databaseRef, Map<String,String> liked, String userListName) {
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

            Collections.shuffle(myFoodList);

            System.out.println("\n\n"+screenWidth + "\n\n" + screenHeight + "\n\n");
            try {
                // Get the height and width of the screen
                screenWidth = (int)(getResources().getDisplayMetrics().widthPixels);
                screenHeight = (int)(getResources().getDisplayMetrics().heightPixels);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            mRecyclerView.setVisibility(View.VISIBLE);
            myAdapter = new MyLikedAndCartAdapter(getContext(), myFoodList, userListName + "Fragment", screenWidth, screenHeight);
            mRecyclerView.setAdapter(myAdapter);

        }

    }

    public int getRecipeCount() {
        return myFoodList != null? myFoodList.size(): 0;
    }

}
