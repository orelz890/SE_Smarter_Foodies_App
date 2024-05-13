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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarter_foodies.R;
import com.example.smarter_foodies.ViewModel.RecipePage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.collect.ImmutableMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

//import com.google.firebase.auth.UserRecord;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.C;

import java.util.List;
import java.util.Map;

public class ReportsAdapter extends RecyclerView.Adapter<ReportedFoodViewHolder> {

    private Context mContext;
    private List<recipe> myReportedList;
    private int screenWidth, screenHeight;
    private CRUD_RealTimeDatabaseData CRUD;


    public ReportsAdapter(Context mContext, List<recipe> myReportedList, int screenWidth, int screenHeight, CRUD_RealTimeDatabaseData CRUD) {
        this.mContext = mContext;
        this.myReportedList = myReportedList;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.CRUD = CRUD;
    }

    @Override
    public ReportedFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_report_item, parent, false);
        return new ReportedFoodViewHolder(mView, screenWidth, screenHeight);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportedFoodViewHolder foodViewHolder, int i) {

        // Setting recipe image
        recipe recipe = myReportedList.get(i);
        if (!myReportedList.get(i).getImages().isEmpty()) {
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

        foodViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, RecipePage.class);
                recipe res = myReportedList.get(foodViewHolder.getAdapterPosition());
                RecipePageFunctions.setIntentContent(intent, res);
                mContext.startActivity(intent);
            }
        });

        // Setting recipe title
        foodViewHolder.mTitle.setText(recipe.getTitle());

        setButtons(foodViewHolder, i);
    }

    private void setButtons(ReportedFoodViewHolder foodViewHolder, int pos) {
        foodViewHolder.mBanButton.setOnClickListener(view -> {
            setDialogApproval(foodViewHolder, pos);
        });

        foodViewHolder.mApprovedButton.setOnClickListener(view -> {
            try {
                String uid = myReportedList.get(pos).getCopy_rights();
                String name = myReportedList.get(pos).getTitle();

                if (uid != null) {
                    CRUD.approveUser(mContext, uid, name);
                }
                // Remove from reports list
                myReportedList.remove(pos);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    private void setDialogApproval(ReportedFoodViewHolder foodViewHolder, int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, androidx.appcompat.R.style.Base_V7_Theme_AppCompat_Dialog);
        Activity activity = (Activity) mContext;
        final View customLayout = activity.getLayoutInflater().inflate(R.layout.yes_no_dialog_layout, null);
        builder.setView(customLayout);
        builder.setCancelable(false);
        builder.setTitle("Are you sure?");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                try {
                    String uid = myReportedList.get(pos).getCopy_rights();
                    String name = myReportedList.get(pos).getTitle();

                    if (uid != null) {
                        CRUD.banUser(mContext, uid, name);
                    }
                    // Remove from reports list
                    myReportedList.remove(pos);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        builder.setNegativeButton("no", (dialogInterface, which) -> {

        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public int getItemCount() {
        return myReportedList.size();
    }

}


class ReportedFoodViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView mTitle;
    CardView mCardView;
    AppCompatButton mBanButton, mApprovedButton;
    CRUD_RealTimeDatabaseData CRUD;

    public ReportedFoodViewHolder(View itemView, int screenWidth, int screenHeight) {
        super(itemView);
        CRUD = new CRUD_RealTimeDatabaseData();
        imageView = itemView.findViewById(R.id.iv_image);
        mTitle = itemView.findViewById(R.id.tv_recipe_title);
        mCardView = itemView.findViewById(R.id.myCardView);
        mBanButton = itemView.findViewById(R.id.button_ban_account);
        mApprovedButton = itemView.findViewById(R.id.button_approved);




        // Calculate the desired width and height for the CardView
        int desiredWidth = (int) (screenWidth * 0.4);
        int desiredHeight = (int) (screenHeight * 0.2);

//        // Set the dimensions for the CardView programmatically
//        ViewGroup.LayoutParams layoutParams = mCardView.getLayoutParams();
//        layoutParams.width = desiredWidth;

//        layoutParams.height = desiredHeight;
//        mCardView.setLayoutParams(layoutParams);
    }
}