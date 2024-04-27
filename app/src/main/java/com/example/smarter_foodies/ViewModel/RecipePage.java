package com.example.smarter_foodies.ViewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smarter_foodies.Model.CRUD_RealTimeDatabaseData;
import com.example.smarter_foodies.Model.RecipePageFunctions;
import com.example.smarter_foodies.Model.recipe;
import com.example.smarter_foodies.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class RecipePage extends AppCompatActivity {

    private TextView ingredients, howToMake, prepTime, cookTime, totalTime, carbs, protein, fats, calories, servings, recipeName, categoryAndSub, copyRights;
    private ImageView recipeImage;
    private ImageButton listExtract;
    private CRUD_RealTimeDatabaseData CRUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_page);

        CRUD = new CRUD_RealTimeDatabaseData();

        setFindByIds();

        Bundle mBundle = getIntent().getExtras();
        setBundleContent(mBundle);

        listExtract.setOnClickListener(view -> {
            sendRecipeToWhatsapp(mBundle.getString("name") + "\n\ningredients:\n\n" + ingredients.getText());
        });
    }

    private void setFindByIds() {
        recipeImage = (ImageView) findViewById(R.id.recipeImageDisplay);


        categoryAndSub = (TextView) findViewById(R.id.recipeCatAndSub);
        recipeName = (TextView) findViewById(R.id.recipeName);
        copyRights = (TextView) findViewById(R.id.recipeHowToMake);
        protein = (TextView) findViewById(R.id.recipeProtein);
        carbs = (TextView) findViewById(R.id.recipeCarbs);
        fats = (TextView) findViewById(R.id.recipeFats);
        calories = (TextView) findViewById(R.id.recipeCalories);
        ingredients = (TextView) findViewById(R.id.recipeIngredients);
        howToMake = (TextView) findViewById(R.id.recipeHowToMake);
        prepTime = (TextView) findViewById(R.id.recipePrepTime);
        cookTime = (TextView) findViewById(R.id.recipeCookTime);
        totalTime = (TextView) findViewById(R.id.recipeTotalTime);
        servings = (TextView) findViewById(R.id.recipeServings);


        listExtract = (ImageButton) findViewById(R.id.ib_create_cart_list);
    }

    private void setBundleContent(Bundle mBundle) {
        if (mBundle != null) {

            categoryAndSub.setText(mBundle.getString("CategoryAndSub"));

            recipeName.setText(mBundle.getString("name"));
            getDishFromSearchTree(recipeName.getText().toString());

            copyRights.setText(mBundle.getString("copyRights"));

            carbs.setText(RecipePageFunctions.is_full(mBundle.getString("carbs")));
            protein.setText(RecipePageFunctions.is_full(mBundle.getString("protein")));
            fats.setText(RecipePageFunctions.is_full(mBundle.getString("fats")));
            calories.setText(RecipePageFunctions.is_full(mBundle.getString("calories")));

            ingredients.setText(RecipePageFunctions.recipeIngredients(mBundle.getStringArray("ingredients")));
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


    private void sendRecipeToWhatsapp(String itemList) {

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
    }


    private void getDishFromSearchTree(String recipeName) {

        List<recipe> r = new ArrayList<>();

        // Create the reference to the specific recipe reference in search tree
        final DatabaseReference mDatabaseSearchGet = CRUD.getToRecipeDepth(recipeName);

        // Get the actual recipe reference
        mDatabaseSearchGet.get().addOnSuccessListener(dataSnapshot -> {
            if (dataSnapshot.exists()) {
                List<Task<Object>> tasks = new ArrayList<>();
                String recipeRefString = dataSnapshot.getValue(String.class);
                if (recipeRefString != null) {
                    DatabaseReference recipesNodeReference = FirebaseDatabase.getInstance().getReference().child(recipeRefString);
                    Task<Object> task = CRUD.fetchDataTask(recipesNodeReference);
                    tasks.add(task);
                }

                Tasks.whenAllSuccess(tasks).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                    @Override
                    public void onSuccess(List<Object> snapshots) {
                        handleSuccess(r, tasks);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                        Log.d("RecipePage", "setByNameRecyclerAdapter - whenAllSuccess - Failed");
                        e.printStackTrace();
                    }
                });
            }
            else {
                Log.d("RecipePage", "\n\nRecipe page - Data snap do not exist\n\n");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void handleSuccess(List<recipe> recipes, List<Task<Object>> tasks) {

        // Fill recipes list with the recipes received.
        for (int i = 0; i < tasks.size(); i++) {
            Object snapshot = tasks.get(i).getResult();
            if (snapshot instanceof DataSnapshot) {
                DataSnapshot dataSnapshot = (DataSnapshot) snapshot;
                // Process each user's data
                recipe curr_recipe = dataSnapshot.getValue(recipe.class);
                if (curr_recipe != null) {
                    recipes.add(curr_recipe);
                }
            }
        }

        // Show result/image in view
        if (recipes.size() > 0) {
            // Now, handle the logic for successful data retrieval here
            String[] ImageUrl = RecipePageFunctions.List_of_string_to_array(recipes.get(0).getImages());
            int size = ImageUrl.length;

            if (size > 0) {

                if (ImageUrl[size - 1].startsWith("https:")) {
                    Picasso.get().load(ImageUrl[size - 1]).into(recipeImage);
                } else {
                    // Decode the image data from base64 to a Bitmap
                    byte[] imageData = Base64.decode(ImageUrl[size - 1], Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

                    // Set the image for the ImageView
                    recipeImage.setImageBitmap(bitmap);
                }
            } else {
                recipeImage.setImageResource(R.drawable.iv_no_images_available);
            }
        }
    }


}
