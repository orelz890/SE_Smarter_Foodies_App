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

        }else{
            foodViewHolder.imageView.setImageResource(R.drawable.iv_no_images_available);
        }
        foodViewHolder.mTitle.setText(recipe.getTitle());
        foodViewHolder.mCalories.setText(recipe.getCalories());

        String uid = FirebaseAuth.getInstance().getUid();
        setImageButtons(foodViewHolder, uid, recipe);



        foodViewHolder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,RecipePage.class);
                setIntentContent(intent,foodViewHolder);
                mContext.startActivity(intent);
            }
        });


    }



    private void setIntentContent(Intent intent,FoodViewHolder foodViewHolder){
        List<String> images = myFoodList.get(foodViewHolder.getAdapterPosition()).getImages();

        intent.putExtra("recipeImage",images.get(images.size() - 1));
        intent.putExtra("CategoryAndSub",myFoodList.get(foodViewHolder.getAdapterPosition()).getCategory());
        intent.putExtra("name",myFoodList.get(foodViewHolder.getAdapterPosition()).getTitle());
        intent.putExtra("copyRights",myFoodList.get(foodViewHolder.getAdapterPosition()).getCopy_rights());
        intent.putExtra("carbs",is_full(myFoodList.get(foodViewHolder.getAdapterPosition()).getCarbs()));
        intent.putExtra("protein",is_full(myFoodList.get(foodViewHolder.getAdapterPosition()).getProtein()));
        intent.putExtra("fats",is_full(myFoodList.get(foodViewHolder.getAdapterPosition()).getFat()));
        intent.putExtra("calories",is_full(myFoodList.get(foodViewHolder.getAdapterPosition()).getCalories()));
        intent.putExtra("ingardiants",ingaridiants(myFoodList.get(foodViewHolder.getAdapterPosition()).getIngredients()));
        intent.putExtra("HowToMake",directions(myFoodList.get(foodViewHolder.getAdapterPosition()).getDirections()));

        int prep = Proper_time_int(myFoodList.get(foodViewHolder.getAdapterPosition()).getPrepTime());
        if (prep == -1){
            intent.putExtra("prepTime","---");
        }
        else intent.putExtra("prepTime",(prep+" m"));

        int cook = Proper_time_int(myFoodList.get(foodViewHolder.getAdapterPosition()).getCookingTime());
        if (cook == -1){
            intent.putExtra("cookTime","---");
        }
        else intent.putExtra("cookTime",cook+" m");

        if ( cook == -1 || prep == -1){
            intent.putExtra("totalTime","---");
        }
        else intent.putExtra("totalTime",(cook+prep)+" m");


        intent.putExtra("servings",myFoodList.get(foodViewHolder.getAdapterPosition()).getServings());
    }

    private String is_full(String x){
        if (x != null)
            return x;
        else
            return "---";
    }


    private int Proper_time_int(String time) {
        if (time != null){
            try{
                String[] temp= time.split(" ");
                if (temp.length < 3){
                    if (temp[1].equals("hrs")){
                        return ((Integer.parseInt(temp[0]))*60);
                    }
                    return Integer.parseInt(temp[0]);
                }

                else{
                    return ((Integer.parseInt(temp[1])*60) +Integer.parseInt(temp[3]));
                }
            }
            catch (Exception e){
                System.out.println("error:could not convert- "+time);
            }
        }
        return -1;
    }


    private String ingaridiants(List<String> ingardiants){
        String ingardiant ="";
        for (int i = 0; i < ingardiants.size();i++){
            ingardiant = ingardiant +ingardiants.get(i)+"\n";
        }
        return ingardiant;
    }
    private String directions(List<String> direcations){
        String direcation ="";
        for (int i = 0; i < direcations.size();i++){
            direcation = direcation +"STEP " +i +": "+ direcations.get(i)+"\n\n";
        }
        return direcation;
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
                }
                else if (listName.equals("cart")){
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


    public void setImageButtons(FoodViewHolder foodViewHolder, String uid, recipe recipe){
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



class FoodViewHolder extends RecyclerView.ViewHolder{

    ImageView imageView;
    TextView mTitle, mCalories;
    CardView mCardView;
    ImageButton mHeart;
    ImageButton mCart;

    CRUD_RealTimeDatabaseData CRUD;

    public FoodViewHolder(View itemView) {
        super(itemView);
        CRUD = new CRUD_RealTimeDatabaseData();
        imageView = itemView.findViewById(R.id.iv_image);
        mTitle = itemView.findViewById(R.id.tv_recipe_title);
        mCalories = itemView.findViewById(R.id.tv_calories);
        mCardView = itemView.findViewById(R.id.myCardView);
        mHeart = itemView.findViewById(R.id.ib_search_like_recycler);
        mCart = itemView.findViewById(R.id.ib_add_to_cart_);
    }
}