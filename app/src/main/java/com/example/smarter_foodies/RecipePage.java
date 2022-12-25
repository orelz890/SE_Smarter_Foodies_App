package com.example.smarter_foodies;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.EventLogTags;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.mail.MessagingException;

public class RecipePage extends AppCompatActivity {
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

    //Button like;
    //Button addToCart;
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
            createWhatsappDialog(mBundle.getString("name")+"\n\nIngeridants:\n\n"+ mBundle.getString("ingardiants"));});


    }




    private void setFindByIds(){
        recipeImage = (ImageView)findViewById(R.id.recipeImageDisplay);

        catagoryAndSub = (TextView)findViewById(R.id.recipeCatAndSub);
        recipeName = (TextView)findViewById(R.id.recipeName);
        copyRights = (TextView)findViewById(R.id.recipeHowToMake);
        protein = (TextView)findViewById(R.id.recipeProtein);
        carbs = (TextView)findViewById(R.id.recipeCarbs);
        fats = (TextView)findViewById(R.id.recipeFats);
        caloreis = (TextView)findViewById(R.id.recipeCalories);
        ingardiants = (TextView)findViewById(R.id.recipeIngradiants);
        howToMake = (TextView)findViewById(R.id.recipeHowToMake);
        prepTime = (TextView)findViewById(R.id.recipePrepTime);
        cookTime = (TextView)findViewById(R.id.recipeCookTime);
        totalTime = (TextView)findViewById(R.id.recipeTotalTime);
        servings = (TextView)findViewById(R.id.recipeServings);

        //like = (Button)findViewById(R.id.recipeLikeButton) ;
        //addToCart = (Button)findViewById(R.id.recipeAddToCart) ;
        listExtract = (Button)findViewById(R.id.recipeListButton) ;
    }
    private void setBundleContent(Bundle mBundle){
        if(mBundle != null){
            String ImageUrl = mBundle.getString("recipeImage");
            if (ImageUrl != null && ImageUrl != ""){
                Picasso.get().load(ImageUrl).into(recipeImage);}


        catagoryAndSub.setText(mBundle.getString("CategoryAndSub"));
            recipeName.setText(mBundle.getString("name"));
            copyRights.setText(mBundle.getString("copyRights"));
            carbs.setText(mBundle.getString("carbs"));
            protein.setText(mBundle.getString("protein"));
            fats.setText(mBundle.getString("fats"));
            caloreis.setText(mBundle.getString("calories"));
            ingardiants.setText(mBundle.getString("ingardiants"));
            howToMake.setText(mBundle.getString("HowToMake"));
            prepTime.setText(mBundle.getString("prepTime"));
            cookTime.setText(mBundle.getString("cookTime"));
            totalTime.setText(mBundle.getString("totalTime"));
            servings.setText(mBundle.getString("servings"));
        }
    }


    private void createWhatsappDialog(String itemList){

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
            }catch (Exception exception)
            {
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
}