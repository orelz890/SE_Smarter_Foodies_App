package com.example.smarter_foodies;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<FoodViewHolder>{

    private Context mContext;
    private List<recipe> myFoodList;

    public MyAdapter(Context mContext, List<recipe> myFoodList) {
        this.mContext = mContext;
        this.myFoodList = myFoodList;
    }

    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_raw_item, parent, false);
        return new FoodViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder foodViewHolder, int i) {
        recipe recipe = myFoodList.get(i);
        if (!myFoodList.get(i).getImages().isEmpty()) {
            List<String> images = recipe.getImages();
//            setBestImage(foodViewHolder, i);
            Picasso.get().load(images.get(images.size() - 1)).into(foodViewHolder.imageView);

        }
        foodViewHolder.mTitle.setText(recipe.getTitle());
        foodViewHolder.mCalories.setText(recipe.getCalories());

        String uid = FirebaseAuth.getInstance().getUid();
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
                        }
                    }
                }
            });
        }
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
                                        foodViewHolder.mHeart.setImageResource(R.drawable.red_heart_not_filled);
                                        List<String> singleValueList = foodViewHolder.CRUD.getSingleValueList(recipe.getTitle());
                                        foodViewHolder.CRUD.removeFromUserLists(singleValueList, "liked");
                                    }
//                                    else{
                                        foodViewHolder.mHeart.setImageResource(R.drawable.red_heart_filled);
                                        List<String> singleValueList = foodViewHolder.CRUD.getSingleValueList(recipe.getTitle());
                                        foodViewHolder.CRUD.addToUserLists(singleValueList, "liked");
//                                    }
                                }
                            }
                        }
                    });
                }
            }
        });

        foodViewHolder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,RecipePage.class);
                List<String> images = myFoodList.get(foodViewHolder.getAdapterPosition()).getImages();
                intent.putExtra("recipeImage",images.get(images.size() - 1));
                intent.putExtra("CategoryAndSub",myFoodList.get(foodViewHolder.getAdapterPosition()).getCategory());
                intent.putExtra("name",myFoodList.get(foodViewHolder.getAdapterPosition()).getTitle());
                intent.putExtra("copyRights",myFoodList.get(foodViewHolder.getAdapterPosition()).getCopy_rights());
                intent.putExtra("carbs",myFoodList.get(foodViewHolder.getAdapterPosition()).getCarbs());
                intent.putExtra("protein",myFoodList.get(foodViewHolder.getAdapterPosition()).getProtein());
                intent.putExtra("fats",myFoodList.get(foodViewHolder.getAdapterPosition()).getFat());
                intent.putExtra("calories",myFoodList.get(foodViewHolder.getAdapterPosition()).getCalories());
                intent.putExtra("ingardiants",myFoodList.get(foodViewHolder.getAdapterPosition()).getIngredients().toString());
                intent.putExtra("HowToMake",myFoodList.get(foodViewHolder.getAdapterPosition()).getDirections().toString());
                intent.putExtra("prepTime",myFoodList.get(foodViewHolder.getAdapterPosition()).getPrepTime());
                intent.putExtra("cookTime",myFoodList.get(foodViewHolder.getAdapterPosition()).getCookingTime());
                intent.putExtra("totalTime",myFoodList.get(foodViewHolder.getAdapterPosition()).getTotalTime());
                intent.putExtra("servings",myFoodList.get(foodViewHolder.getAdapterPosition()).getServings());

                mContext.startActivity(intent);
            }
        });


    }

//    private void setBestImage(FoodViewHolder foodViewHolder, int i) {
//        int width = 0;
//        int height = 0;
//        int bestImage = 0;
//        List<String> images = myFoodList.get(i).getImages();
//        for (int j = 0; j < images.size(); j++){
//            Picasso.get().load(images.get(j)).into(foodViewHolder.imageView);
//            // Get the dimensions of the image
//            int currWidth = foodViewHolder.imageView.getWidth();
//            int currHeight = foodViewHolder.imageView.getHeight();
//            // Check if the image is too small
//            if (currHeight*currWidth > width*height) {
//                // Image is likely to be blurry when displayed at a larger size
//                width = currWidth;
//                height = currHeight;
//                bestImage = j;
//            }
//        }
//        Picasso.get().load(images.get(bestImage)).into(foodViewHolder.imageView);
//    }

    @Override
    public int getItemCount() {
        return myFoodList.size();
    }

}



class FoodViewHolder extends RecyclerView.ViewHolder{

    ImageView imageView;
    TextView mTitle, mCalories;
    CardView mCardView;
    ImageButton mHeart;
    CRUD_RealTimeDatabaseData CRUD;

    public FoodViewHolder(View itemView) {
        super(itemView);
        CRUD = new CRUD_RealTimeDatabaseData();
        imageView = itemView.findViewById(R.id.iv_image);
        mTitle = itemView.findViewById(R.id.tv_recipe_title);
        mCalories = itemView.findViewById(R.id.tv_calories);
        mCardView = itemView.findViewById(R.id.myCardView);
        mHeart = itemView.findViewById(R.id.ib_search_like_recycler);
    }
}