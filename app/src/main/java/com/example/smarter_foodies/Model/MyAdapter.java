package com.example.smarter_foodies.Model;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
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

import com.example.smarter_foodies.R;
import com.example.smarter_foodies.ViewModel.RecipePage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<FoodViewHolder> {

    private Context mContext;
    private List<recipe> myFoodList;
    private int screenWidth, screenHeight;


    public MyAdapter(Context mContext, List<recipe> myFoodList, int screenWidth, int screenHeight) {
        this.mContext = mContext;
        this.myFoodList = myFoodList;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_raw_item, parent, false);
        return new FoodViewHolder(mView, screenWidth, screenHeight);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder foodViewHolder, int i) {
        recipe recipe = myFoodList.get(i);
        if (!myFoodList.get(i).getImages().isEmpty()) {
            List<String> images = recipe.getImages();
            String imgStr = images.get(images.size() - 1);
            if (imgStr.startsWith("https:")) {
                Picasso.get().load(imgStr).into(foodViewHolder.imageView);
            } else {
                // Decode the image data from base64 to a Bitmap
                byte[] imageData = Base64.decode(imgStr, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

                // Set the image for the ImageView
                foodViewHolder.imageView.setImageBitmap(bitmap);
            }
        } else {
            foodViewHolder.imageView.setImageResource(R.drawable.iv_no_images_available);
        }
        foodViewHolder.mTitle.setText(recipe.getTitle());
        foodViewHolder.mCalories.setText(recipe.getCalories());

        String uid = FirebaseAuth.getInstance().getUid();
        setImageButtons(foodViewHolder, uid, recipe);


        foodViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, RecipePage.class);
                recipe res = myFoodList.get(foodViewHolder.getAdapterPosition());
                RecipePageFunctions.setIntentContent(intent, res);
                mContext.startActivity(intent);
            }
        });


    }


    private void setDialogApproval(FoodViewHolder foodViewHolder, recipe r, String listName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, androidx.appcompat.R.style.Base_V7_Theme_AppCompat_Dialog);
        Activity activity = (Activity) mContext;
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
                } else if (listName.equals("cart")) {
                    foodViewHolder.mCart.setImageResource(R.drawable.ic_baseline_add_shopping_cart_24_not_added);
                    foodViewHolder.CRUD.removeFromUserLists(singleValueList, "cart");
                }
            }
        });

        builder.setNegativeButton("no", (dialogInterface, which) -> {

        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void setImageButtons(FoodViewHolder foodViewHolder, String uid, recipe recipe) {
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
                            } else {
                                foodViewHolder.mHeart.setImageResource(R.drawable.red_heart_not_filled);
                            }
                            if (user.getCart().contains(recipe.getTitle())) {
                                foodViewHolder.mCart.setImageResource(R.drawable.ic_baseline_shopping_cart_24);
                            } else {
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
                                    } else {
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
                                    } else {
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


class FoodViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView mTitle, mCalories;
    CardView mCardView;
    ImageButton mHeart;
    ImageButton mCart;

    CRUD_RealTimeDatabaseData CRUD;

    public FoodViewHolder(View itemView, int screenWidth, int screenHeight) {
        super(itemView);
        CRUD = new CRUD_RealTimeDatabaseData();
        imageView = itemView.findViewById(R.id.iv_image);
        mTitle = itemView.findViewById(R.id.tv_recipe_title);
        mCalories = itemView.findViewById(R.id.tv_calories);
        mHeart = itemView.findViewById(R.id.ib_search_like_recycler);
        mCart = itemView.findViewById(R.id.ib_add_to_cart_);
        mCardView = itemView.findViewById(R.id.myCardView);



        // Calculate the desired width and height for the CardView
        int desiredWidth = (int) (screenWidth * 0.45);
        int desiredHeight = (int) (screenHeight * 0.4);

        // Set the dimensions for the CardView programmatically
        ViewGroup.LayoutParams layoutParams = mCardView.getLayoutParams();
        layoutParams.width = desiredWidth;
//        layoutParams.height = desiredHeight;
        mCardView.setLayoutParams(layoutParams);
    }
}