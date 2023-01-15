package com.example.smarter_foodies;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.EventLogTags;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.mail.MessagingException;

public class RecipePage extends AppCompatActivity {
    CRUD_RealTimeDatabaseData CRUD;

    TextView ingardiants;
    TextView howToMake;
    TextView prepTime;
    TextView cookTime;
    TextView totalTime;
    TextView carbs;
    TextView protein;
    TextView fats;
    TextView caloreis;
    TextView servings;
    TextView recipeName;
    TextView catagoryAndSub;
    TextView copyRights;

    Button listExtract;

    ImageView recipeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_page);

        setFindByIds();

        Bundle mBundle = getIntent().getExtras();
        setBundleContent(mBundle);

        listExtract.setOnClickListener(view -> {
            //mBundle.getString("ingardiants")
            createWhatsappDialog(mBundle.getString("name") + "\n\nIngeridants:\n\n" + ingardiants.getText() );
        });
    }

    private void setFindByIds() {
        recipeImage = (ImageView) findViewById(R.id.recipeImageDisplay);

        catagoryAndSub = (TextView) findViewById(R.id.recipeCatAndSub);
        recipeName = (TextView) findViewById(R.id.recipeName);
        copyRights = (TextView) findViewById(R.id.recipeHowToMake);
        protein = (TextView) findViewById(R.id.recipeProtein);
        carbs = (TextView) findViewById(R.id.recipeCarbs);
        fats = (TextView) findViewById(R.id.recipeFats);
        caloreis = (TextView) findViewById(R.id.recipeCalories);
        ingardiants = (TextView) findViewById(R.id.recipeIngradiants);
        howToMake = (TextView) findViewById(R.id.recipeHowToMake);
        prepTime = (TextView) findViewById(R.id.recipePrepTime);
        cookTime = (TextView) findViewById(R.id.recipeCookTime);
        totalTime = (TextView) findViewById(R.id.recipeTotalTime);
        servings = (TextView) findViewById(R.id.recipeServings);


        listExtract = (Button) findViewById(R.id.recipeListButton);
    }

    private void setBundleContent(Bundle mBundle) {
        if (mBundle != null) {

            catagoryAndSub.setText(mBundle.getString("CategoryAndSub"));

            recipeName.setText(mBundle.getString("name"));
            getDishFromSearchTree(recipeName.getText().toString());

            copyRights.setText(mBundle.getString("copyRights"));

            carbs.setText(RecipePageFunctions.is_full(mBundle.getString("carbs")));
            protein.setText(RecipePageFunctions.is_full(mBundle.getString("protein")));
            fats.setText(RecipePageFunctions.is_full(mBundle.getString("fats")));
            caloreis.setText(RecipePageFunctions.is_full(mBundle.getString("calories")));

            ingardiants.setText(RecipePageFunctions.ingaridiants(mBundle.getStringArray("ingardiants")));
            howToMake.setText(RecipePageFunctions.directions(mBundle.getStringArray("HowToMake")));

            int prep = RecipePageFunctions.Proper_time_int(mBundle.getString("prepTime"));
            if (prep == -1) {
                prepTime.setText("---");
            } else prepTime.setText(prep + " m");

            int cook = RecipePageFunctions.Proper_time_int(mBundle.getString("cookTime"));
            if (cook == -1) {
                cookTime.setText("---");
            } else cookTime.setText(cook + " m");

            if (cook == -1 || prep == -1) {
                totalTime.setText("---");
            } else totalTime.setText((cook + prep) + " m");

            servings.setText(mBundle.getString("servings"));
        }
    }


    private void createWhatsappDialog(String itemList) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, androidx.appcompat.R.style.Base_Widget_AppCompat_ActionBar_TabBar);
        final View customLayout = getLayoutInflater().inflate(R.layout.whatsapp_dialog, null);
        builder.setView(customLayout);
        builder.setCancelable(false);
        ImageView imageView = customLayout.findViewById(R.id.btnWhatsapp);

        imageView.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, itemList);
            intent.setType("text/plain");
            intent.setPackage("com.whatsapp");
            try {
                startActivity(intent);
            } catch (Exception exception) {
                Toast.makeText(RecipePage.this, "There is no application that support this action",
                        Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }


    private void getDishFromSearchTree(String recipeName) {
        // how to get data from the database- search
        List<recipe> r = new ArrayList<>();
        DatabaseReference mDatabaseSearchGet = FirebaseDatabase.getInstance().getReference();
        mDatabaseSearchGet = getToRecipeDepth(mDatabaseSearchGet, recipeName);
        mDatabaseSearchGet.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    System.out.println("snap yay ");
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        System.out.println("in for yay");
                        System.out.println(child);
                        recipe curr_recipe = child.getValue(recipe.class);
                        System.out.println(curr_recipe);
                        if (curr_recipe != null) {
                            r.add(curr_recipe);
                        }
                    }
                    System.out.println(r.get(0));
                    String[] ImageUrl = RecipePageFunctions.List_of_string_to_array(r.get(0).getImages());
                    System.out.println("image list:   " + r.get(0).getImages());
                    System.out.println("image arr:   " + (RecipePageFunctions.List_of_string_to_array(r.get(0).getImages())).toString());
                    int size = ImageUrl.length;
                    if (size > 0){
                        if (ImageUrl[size - 1].startsWith("https:")) {
                            Picasso.get().load(ImageUrl[size - 1]).into(recipeImage);
                        } else {
                            // Decode the image data from base64 to a Bitmap
                            byte[] imageData = Base64.decode(ImageUrl[size - 1], Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

                            // Set the image for the ImageView
                            recipeImage.setImageBitmap(bitmap);
                        }
                    }
                    else{
                        recipeImage.setImageResource(R.drawable.iv_no_images_available);
                    }


                }
            }
        });
    }

    public DatabaseReference getToRecipeDepth(DatabaseReference DataRef, String name) {
        DataRef = FirebaseDatabase.getInstance().getReference();
        if (name.length() > 0) {
            String new_name = getCleanStringForSearch(name);
            //            System.out.println(new_name);
            int len = new_name.length();
            DataRef = DataRef.child("search");
            // Max tree depth is 32
            for (int i = 0; i < len && i < 27; i++) {
                DataRef = DataRef.child(new_name.charAt(i) + "");
            }
        }
        return DataRef;
    }

    private String getCleanStringForSearch(String input_str) {
        input_str = input_str.replace("\"", "").replace(" ", "");
        StringBuilder new_str = new StringBuilder();
        for (int i = 0; i < input_str.length(); i++) {
            if (Character.isDigit(input_str.charAt(i)) || Character.isAlphabetic(input_str.charAt(i))) {
                new_str.append(input_str.charAt(i));
            }
        }
        return new_str.toString().toLowerCase(Locale.ROOT);

    }


}
