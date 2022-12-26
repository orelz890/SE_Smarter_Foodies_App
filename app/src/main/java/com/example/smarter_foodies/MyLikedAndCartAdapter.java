package com.example.smarter_foodies;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyLikedAndCartAdapter extends RecyclerView.Adapter<LikedAndCartFoodViewHolder> {

    private Context mContext;
    private List<recipe> myFoodList;
    private Activity activity;
    private String userListName;

    public MyLikedAndCartAdapter(Context mContext, List<recipe> myFoodList, String userListName) {
        this.mContext = mContext;
        this.myFoodList = myFoodList;
        this.userListName = userListName;
        this.activity = (Activity) mContext;
    }

    @Override
    public LikedAndCartFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_raw_liked_item, parent, false);
        return new LikedAndCartFoodViewHolder(mView);
    }


    @Override
    public void onBindViewHolder(@NonNull LikedAndCartFoodViewHolder foodViewHolder, int i) {
        recipe recipe = myFoodList.get(i);
        if (!myFoodList.get(i).getImages().isEmpty()) {
            List<String> images = recipe.getImages();
//            setBestImage(foodViewHolder, i);
            Picasso.get().load(images.get(images.size() - 1)).resize(220, 220).into(foodViewHolder.imageView);
        }
        else{
            foodViewHolder.imageView.setImageResource(R.drawable.iv_no_images_available);
        }
        foodViewHolder.mTitle.setText(recipe.getTitle());
        foodViewHolder.mAdditional.setText(recipe.getMain_category() + "- " + recipe.getCategory());

        String uid = FirebaseAuth.getInstance().getUid();
        setImageButtons(foodViewHolder, uid, recipe);

        foodViewHolder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, RecipePage.class);
                List<String> images = myFoodList.get(foodViewHolder.getAdapterPosition()).getImages();
                intent.putExtra("recipeImage", images.get(images.size() - 1));
                intent.putExtra("CategoryAndSub", myFoodList.get(foodViewHolder.getAdapterPosition()).getCategory());
                intent.putExtra("name", myFoodList.get(foodViewHolder.getAdapterPosition()).getTitle());
                intent.putExtra("copyRights", myFoodList.get(foodViewHolder.getAdapterPosition()).getCopy_rights());
                intent.putExtra("carbs", myFoodList.get(foodViewHolder.getAdapterPosition()).getCarbs());
                intent.putExtra("protein", myFoodList.get(foodViewHolder.getAdapterPosition()).getProtein());
                intent.putExtra("fats", myFoodList.get(foodViewHolder.getAdapterPosition()).getFat());
                intent.putExtra("calories", myFoodList.get(foodViewHolder.getAdapterPosition()).getCalories());
                intent.putExtra("ingardiants", myFoodList.get(foodViewHolder.getAdapterPosition()).getIngredients().toString());
                intent.putExtra("HowToMake", myFoodList.get(foodViewHolder.getAdapterPosition()).getDirections().toString());
                intent.putExtra("prepTime", myFoodList.get(foodViewHolder.getAdapterPosition()).getPrepTime());
                intent.putExtra("cookTime", myFoodList.get(foodViewHolder.getAdapterPosition()).getCookingTime());
                intent.putExtra("totalTime", myFoodList.get(foodViewHolder.getAdapterPosition()).getTotalTime());
                intent.putExtra("servings", myFoodList.get(foodViewHolder.getAdapterPosition()).getServings());

                mContext.startActivity(intent);
            }
        });
    }

    private void setDialogApproval(LikedAndCartFoodViewHolder foodViewHolder, recipe r, String listName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, androidx.appcompat.R.style.Base_V7_Theme_AppCompat_Dialog);
        final View customLayout = activity.getLayoutInflater().inflate(R.layout.yes_no_dialog_layout, null);
        builder.setView(customLayout);
        builder.setCancelable(false);
        builder.setTitle("Are you sure?");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                List<String> singleValueList = foodViewHolder.CRUD.getSingleValueList(r.getTitle());
                if (listName.equals("liked")) {
                    foodViewHolder.mHeart.setImageResource(R.drawable.red_heart_not_filled);
                    foodViewHolder.CRUD.removeFromUserLists(singleValueList, "liked");
                    if (userListName.equals("liked")){
                        myFoodList.remove(r);
                        MyLikedAndCartAdapter.super.notifyDataSetChanged();
                        likedRecipes likedActivity = (likedRecipes) activity;
                        likedActivity.setRecipeCountViews(myFoodList.size());
                    }
                    else{
                        foodViewHolder.mHeart.setImageResource(R.drawable.red_heart_not_filled);
                    }
                }
                else if (listName.equals("cart")){
                    foodViewHolder.mCart.setImageResource(R.drawable.ic_baseline_add_shopping_cart_24_not_added);
                    foodViewHolder.CRUD.removeFromUserLists(singleValueList, "cart");
                    if (userListName.equals("cart")){
                        myFoodList.remove(r);
                        MyLikedAndCartAdapter.super.notifyDataSetChanged();
                        Cart cartActivity = (Cart) activity;
                        cartActivity.setRecipeCountViews(myFoodList.size());
                    }
                    else{
                        foodViewHolder.mCart.setImageResource(R.drawable.ic_baseline_add_shopping_cart_24_not_added);
                    }
                }
            }
        });

        builder.setNegativeButton("no", (dialogInterface, which) -> {

        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void setImageButtons(LikedAndCartFoodViewHolder foodViewHolder, String uid, recipe recipe){
        if (uid != null) {
            DatabaseReference usersRef = FirebaseDatabase.getInstance()
                    .getReference().child("users").child(uid);
            usersRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            if (user.getLiked().contains(recipe.getTitle())) {
                                foodViewHolder.mHeart.setImageResource(R.drawable.red_heart_filled);
                            }else{
                                foodViewHolder.mHeart.setImageResource(R.drawable.red_heart_not_filled);
                            }
                            if (user.getCart().contains(recipe.getTitle())){
                                foodViewHolder.mCart.setImageResource(R.drawable.ic_baseline_shopping_cart_24);
                            }else{
                                foodViewHolder.mCart.setImageResource(R.drawable.ic_baseline_add_shopping_cart_24_not_added);
                            }
                        }
                    }
                }
            });
        } // The right image is set

        foodViewHolder.mHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uid != null) {
                    DatabaseReference usersRef = FirebaseDatabase.getInstance()
                            .getReference().child("users").child(uid);
                    usersRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                User user = dataSnapshot.getValue(User.class);
                                if (user != null) {
                                    if (user.getLiked().contains(recipe.getTitle())) {
                                        setDialogApproval(foodViewHolder, recipe, "liked");
                                    }
                                    else{
                                        foodViewHolder.mHeart.setImageResource(R.drawable.red_heart_filled);
                                        List<String> singleValueList = foodViewHolder.CRUD.getSingleValueList(recipe.getTitle());
                                        foodViewHolder.CRUD.addToUserLists(singleValueList, "liked");
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });

        foodViewHolder.mCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uid != null) {
                    DatabaseReference usersRef = FirebaseDatabase.getInstance()
                            .getReference().child("users").child(uid);
                    usersRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                User user = dataSnapshot.getValue(User.class);
                                if (user != null) {
                                    if (user.getCart().contains(recipe.getTitle())) {
                                        setDialogApproval(foodViewHolder, recipe, "cart");
                                    }
                                    else{
                                        foodViewHolder.mCart.setImageResource(R.drawable.ic_baseline_shopping_cart_24);
                                        List<String> singleValueList = foodViewHolder.CRUD.getSingleValueList(recipe.getTitle());
                                        foodViewHolder.CRUD.addToUserLists(singleValueList, "cart");
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return myFoodList.size();
    }

}


class LikedAndCartFoodViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView mTitle, mAdditional;
    CardView mCardView;
    ImageButton mHeart;
    ImageButton mCart;
    CRUD_RealTimeDatabaseData CRUD;

    public LikedAndCartFoodViewHolder(View itemView) {
        super(itemView);
        CRUD = new CRUD_RealTimeDatabaseData();
        imageView = itemView.findViewById(R.id.iv_liked_image);
        mTitle = itemView.findViewById(R.id.tv_liked_title);
        mAdditional = itemView.findViewById(R.id.tv_liked_addition_data);
        mCardView = itemView.findViewById(R.id.myCardLikedView);
        mHeart = itemView.findViewById(R.id.ib_like_recycler);
        mCart = itemView.findViewById(R.id.ib_add_to_cart);
    }
}