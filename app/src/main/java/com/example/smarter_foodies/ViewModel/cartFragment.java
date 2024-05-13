package com.example.smarter_foodies.ViewModel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.smarter_foodies.Model.CRUD_RealTimeDatabaseData;
import com.example.smarter_foodies.Model.ProfileTabFragment;
import com.example.smarter_foodies.Model.recipe;
import com.example.smarter_foodies.R;

import java.util.ArrayList;
import java.util.List;


public class cartFragment extends ProfileTabFragment {

    private Context mContext;

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
        mContext = getContext();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        myFoodList = new ArrayList<>();
        CRUD = new CRUD_RealTimeDatabaseData();


        // Set up RecyclerView
        setRecycler("cart");
        setDoSomething();

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        setRecycler("cart");
    }

    @Override
    public void setDoSomething() {
        // Set export groceries list to whatsapp
        doSomethingIB.setVisibility(View.VISIBLE);
        doSomethingIB.setOnClickListener(view -> {
            if (myFoodList.size() > 0) {
                sendToWhatsappIngredientList();
            }
            else {
                Toast.makeText(mContext, "Add recipes first", Toast.LENGTH_LONG).show();
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
            Toast.makeText(mContext, "There is no application that support this action",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private StringBuilder createIngredientList() {
        StringBuilder ans = new StringBuilder();
        try {
            ArrayList<String> ingredients = new ArrayList<>();
            ArrayList<Integer> amounts = new ArrayList<>();
            ArrayList<String> titles = new ArrayList<>();

            // Fill Lists
            for (recipe recipe : myFoodList) {
                titles.add(recipe.getTitle());
                List<String> allIngredients = recipe.getIngredients();

                for (String ingredientStr : allIngredients) {
                    String[] split = ingredientStr.split(":");
                    int amount = Integer.parseInt(split[0]);
                    String currIngredient = split[1];

                    if (ingredients.contains(currIngredient)) {
                        amounts.set(ingredients.indexOf(currIngredient), amounts.get(ingredients.indexOf(currIngredient)) + amount);
                    } else {
                        amounts.add(amount);
                        ingredients.add(currIngredient);
                    }
                }

            }

            // Add recipes names to the message
            ans.append("*Your Weekly Plan Recipes:*\n");
            for (String title : titles) {
                ans.append(title).append("\n");
            }

            // Add the grocery list to the message
            ans.append("\n*Your Grocery List:*\n");
            for (int i = 0; i < amounts.size(); i++) {
                ans.append(amounts.get(i)).append("[g]").append(" ").append(ingredients.get(i)).append("\n");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mContext, "You need to get the new recipes' version",
                    Toast.LENGTH_SHORT).show();
        }


        return ans;
    }
}
