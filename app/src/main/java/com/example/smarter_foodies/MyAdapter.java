package com.example.smarter_foodies;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
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

        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row_item, parent, false);
        return new FoodViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder foodViewHolder, int i) {
//        try {
//            URL url = new URL("https://www.vectortemplates.com/raster/batman-logo-big.gif");
//            InputStream inputStream = url.openConnection().getInputStream();
//            foodViewHolder.imageView.setImageBitmap(BitmapFactory.decodeStream(inputStream));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        foodViewHolder.mTitle.setText(myFoodList.get(i).getTitle());
        foodViewHolder.mCalories.setText(myFoodList.get(i).getCalories());

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

    public FoodViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.iv_image);
        mTitle = itemView.findViewById(R.id.tv_recipe_title);
        mCalories = itemView.findViewById(R.id.tv_calories);
        mCardView = itemView.findViewById(R.id.myCardView);


    }
}