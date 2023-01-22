package com.example.smarter_foodies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smarter_foodies.api.AppServer;
import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import java.util.Set;

public class AddRecipe extends DashboardActivity {

    FirebaseAuth mAuth;
    // These strings role is to help us sync between what the user see in the sub category
    // AutoCompleteTextView and his choice of main category
    String[] categoriesList;
    String category = "";
    String subCategory = "";
    boolean flag;

    // Reference to the realtime database
    DatabaseReference mDatabase;
    // Text inputs from the user
    TextInputEditText etTitle;
    TextView etIngredients;
    ImageButton addIngredients;
    TextInputEditText etDirections;
    // Main & sub categories as the user wish
    AutoCompleteTextView autoCompleteCategory;
    ArrayAdapter<String> adapterCategories;
    AutoCompleteTextView autoCompleteSubCategory;
    ArrayAdapter<String> adapterSubCategories;
    // Number inputs from the user
    NumberPicker npCookingTime;
    NumberPicker npPrepTime;
    NumberPicker npServings;
    NumberPicker npCarbs;
    NumberPicker npProtein;
    NumberPicker npFat;
    NumberPicker npCalories;
    // Submit recipe button
    Button btnSubmit;

    FloatingActionButton fab;
    List<String> uploadedImages;
    List<ImageView> imageViews;
    List<ImageButton> deleteImageButtons;

    AutoCompleteTextView autoCompleteSearchView;
    ImageButton submit;
    ArrayAdapter<String> arraySearchAdapter;

    ArrayList<String> ingredientNamesList;
    static ArrayList<String> selectedIngredients;
    static ListView listView;
    static ListViewAdapter adapter;


    CRUD_RealTimeDatabaseData CRUD;
    Bundle mbBundle;
    ArrayList<String> selectedIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        View activityMainView = LayoutInflater.from(this).inflate(R.layout.activity_add_recipe, rootLayout, false);
        rootLayout.addView(activityMainView);
        setContentView(rootLayout);
        allocateActivityTitle("AddRecipe");

        etIngredients = findViewById(R.id.etIngredients);
        addIngredients = findViewById(R.id.ib_add_ingredient);


        mAuth = FirebaseAuth.getInstance();
        // Create reference to the firebase real time database
        mDatabase = FirebaseDatabase.getInstance().getReference();
        CRUD = new CRUD_RealTimeDatabaseData();
        imageViews = new ArrayList<>();
        deleteImageButtons = new ArrayList<>();
        flag = false;
        // Fill the categories list which the user can chose from
        this.fillCategoriesList();
        this.setImageButtons();

        //    ========================= Get data from user =============================================
        this.createAllNumberPickers();
        this.createAllButtons();
        this.createAllAutoCompleteTextViews();

        addIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addIngredientDialog();
            }
        });
    }




    public static String ingredients(ArrayList<String> ingredients) {
        String ingredient = "";
        if (ingredients != null) {
            for (int i = 0; i < ingredients.size(); i++) {
                ingredient = ingredient + ingredients.get(i).replace(":", " ") + "\n";
            }
            return ingredient;
        }
        return " There Are No Ingredients ";
    }

    private void fillCategoriesList() {
        Set<String> keys = CRUD.subCategoriesList.keySet();
        categoriesList = new String[keys.size() - 1];
        int i = 0;
        for (String s : keys) {
            if (!s.isEmpty()) {
                categoriesList[i++] = s;
            }
        }
    }

    public void setLongClickListeners(int i) {
        imageViews.get(i).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (imageViews.get(i).getDrawable() != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddRecipe.this, androidx.appcompat.R.style.Base_V7_Theme_AppCompat_Dialog);
                    final View customLayout = getLayoutInflater().inflate(R.layout.yes_no_dialog_layout, null);
                    builder.setView(customLayout);
                    builder.setCancelable(false);
                    builder.setTitle("do you want to delete?");
                    builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            uploadedImages.remove(i);
                            imageViews.get(i).setImageDrawable(null);
                        }
                    });

                    builder.setNegativeButton("no", (dialogInterface, which) -> {
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                return true;
            }
        });
    }

    private void setImageButtons() {
        uploadedImages = new ArrayList<>();
        imageViews.add((ImageView) findViewById(R.id.ib_upload_recipe_image));
        imageViews.add((ImageView) findViewById(R.id.ib_upload_recipe_image2));
        imageViews.add((ImageView) findViewById(R.id.ib_upload_recipe_image3));
        imageViews.add((ImageView) findViewById(R.id.ib_upload_recipe_image4));
        for (int i = 0; i < imageViews.size(); i++) {
            setLongClickListeners(i);
        }
        fab = findViewById(R.id.floatingActionButtonAddRecipe);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ImagePicker.Companion.with(AddRecipe.this)
                            .crop()                    //Crop image(Optional), Check Customization for more option
                            .cropSquare()
//                        .cropOval()
                            .compress(1024)            //Final image size will be less than 1 MB(Optional)
                            .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                            .start();
                } catch (Exception ignored) {

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (data != null) {
                int Size = uploadedImages.size();
                if (Size < 4) {
                    Uri imgUri = data.getData();
                    for (ImageView iv : imageViews) {
                        if (iv.getDrawable() == null) {
                            if (imgUri != null) {
                                iv.setImageURI(imgUri);
                                String path = imgUri.getPath();
                                // Convert image to base64-encoded string
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                Bitmap bitmap = BitmapFactory.decodeFile(path);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] imageData = baos.toByteArray();
                                String imageDataBase64 = Base64.encodeToString(imageData, Base64.DEFAULT);
                                uploadedImages.add(imageDataBase64);
                                break;
                            }
                        }
                    }
                    if (Size + 1 == 4) {
                        Toast.makeText(getApplicationContext(), "You have reached the limit of image uploads", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "You exceeded limit of images", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception ignored) {

        }
    }

    private void change_adapter() {
        autoCompleteSubCategory = findViewById(R.id.auto_complete_sub_category);
        adapterSubCategories = new ArrayAdapter<String>(this, R.layout.list_items, CRUD.subCategoriesList.get(category));
        autoCompleteSubCategory.setAdapter(adapterSubCategories);
    }

    private void createAllAutoCompleteTextViews() {
        autoCompleteCategory = findViewById(R.id.auto_complete_category);
        adapterCategories = new ArrayAdapter<String>(this, R.layout.list_items, categoriesList);
        autoCompleteCategory.setAdapter(adapterCategories);
        autoCompleteCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                category = parent.getItemAtPosition(pos).toString();
                Toast.makeText(getApplicationContext(), "Item: " + category, Toast.LENGTH_SHORT).show();
                change_adapter();
            }
        });
        autoCompleteSubCategory = findViewById(R.id.auto_complete_sub_category);
        adapterSubCategories = new ArrayAdapter<String>(this, R.layout.list_items, CRUD.subCategoriesList.get(category));
        autoCompleteSubCategory.setAdapter(adapterSubCategories);
        autoCompleteSubCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                subCategory = parent.getItemAtPosition(pos).toString();
                Toast.makeText(getApplicationContext(), "Item: " + subCategory, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createAllButtons() {
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(view -> {
            System.out.println("=============================");
            submitRecipe();
            if (!flag) {
                startActivity(new Intent(AddRecipe.this, SearchRecipe.class));
            }
            System.out.println("============================");
        });
    }

    private void createAllNumberPickers() {
        etTitle = findViewById(R.id.etTitle);
        etDirections = findViewById(R.id.etDirections);
        npCookingTime = findViewById(R.id.npCookTime);
        npCookingTime.setValue(-1);
        npCookingTime.setMaxValue(2000);
        npCookingTime.setMinValue(0);
        npCookingTime.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                npCookingTime.setValue(newValue);
            }
        });
        npPrepTime = findViewById(R.id.npPrepTime);
        npPrepTime.setValue(-1);
        npPrepTime.setMaxValue(2000);
        npPrepTime.setMinValue(0);
        npPrepTime.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                npPrepTime.setValue(newValue);
            }
        });
        npServings = findViewById(R.id.npServings);
        npServings.setValue(-1);
        npServings.setMaxValue(2000);
        npServings.setMinValue(0);
        npServings.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                npServings.setValue(newValue);
            }
        });
        npCarbs = findViewById(R.id.npCarbs);
        npCarbs.setValue(-1);
        npCarbs.setMaxValue(2000);
        npCarbs.setMinValue(0);
        npCarbs.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                npCarbs.setValue(newValue);
            }
        });
        npProtein = findViewById(R.id.npProtein);
        npProtein.setValue(-1);
        npProtein.setMaxValue(2000);
        npProtein.setMinValue(0);
        npProtein.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                npProtein.setValue(newValue);
            }
        });
        npFat = findViewById(R.id.npFat);
        npFat.setValue(-1);
        npFat.setMaxValue(2000);
        npFat.setMinValue(0);
        npFat.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                npFat.setValue(newValue);
            }
        });
        npCalories = findViewById(R.id.npCalories);
        npCalories.setValue(-1);
        npCalories.setMaxValue(2000);
        npCalories.setMinValue(0);
        npCalories.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                npCalories.setValue(newValue);
            }
        });
    }

    private void submitRecipe() {
        flag = true;
        String title = etTitle.getText().toString();
        if (TextUtils.isEmpty(title)) {
            etTitle.setError("Title cannot be empty");
            etTitle.requestFocus();
        }

        DatabaseReference mDatabaseSearchGet = FirebaseDatabase.getInstance()
                .getReference().child("recipes");
        mDatabaseSearchGet.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    for (DataSnapshot categorySnap : dataSnapshot.getChildren()) {
                        for (DataSnapshot subCategorySnapshot : categorySnap.getChildren()) {
                            for (DataSnapshot recipeSnap : subCategorySnapshot.getChildren()) {
                                recipe r = recipeSnap.getValue(recipe.class);
                                if (r != null) {
                                    System.out.println(r);
                                    if (r.getTitle().equals(title)) {
                                        etTitle.setError("Title already exist, please try a different name");
                                        etTitle.requestFocus();
                                        return;
                                    }
                                }
                            }
                        }
                    }

//                    String ingredients = etIngredients.getText().toString();
//                    System.out.println(ingredients);
//                    String[] ingredients_list = ingredients.split("\n");
//                    if (TextUtils.isEmpty(ingredients)) {
//                        etIngredients.setError("Ingredients cannot be empty");
//                        etIngredients.requestFocus();
//                        return;
//                    } else {
//                        for (String s : ingredients_list) {
//                            if (!s.equals("\n")) {
//                                String[] temp = s.trim().split(" ");
//                                try {
//                                    Double.parseDouble(temp[0]);
//                                } catch (Exception e) {
//                                    etIngredients.setError("Every Ingredient must begin with the quantity..");
//                                    etIngredients.requestFocus();
//                                    return;
//                                }
//                            }
//                        }
//                    }
//                    List<String> ingredientsArray = new ArrayList<>(Arrays.asList(ingredients_list));
                    List<String> ingredientsArray = new ArrayList<>(selectedIngredients);
                    if(selectedIngredients.isEmpty()){
                        etIngredients.setError("Ingredients cannot be empty");
                        etDirections.requestFocus();
                    }

                    String directions = etDirections.getText().toString();
                    String[] directions_list = directions.split("\n");
                    if (TextUtils.isEmpty(directions)) {
                        etDirections.setError("Directions cannot be empty");
                        etDirections.requestFocus();
                    } else if (category.isEmpty()) {
                        autoCompleteCategory.setError("Category cannot be empty");
                        autoCompleteCategory.requestFocus();
                    } else if (subCategory.isEmpty()) {
                        autoCompleteSubCategory.setError("Sub category cannot be empty");
                        autoCompleteSubCategory.requestFocus();
                    } else if (npPrepTime.getValue() == 0 || npCookingTime.getValue() == 0 ||
                            npServings.getValue() == 0 || npProtein.getValue() == 0 ||
                            npFat.getValue() == 0 || npCarbs.getValue() == 0 || npCalories.getValue() == 0) {
                        Toast.makeText(getApplicationContext(), "All bottom half must be filled too!",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        List<String> directionsArray = new ArrayList<>(Arrays.asList(directions_list));
                        recipe r = new recipe(title, category, subCategory, new ArrayList<>(), directionsArray,
                                calcTime(npPrepTime.getValue()),
                                calcTime(npCookingTime.getValue()) + "",
                                npServings.getValue() + "", npProtein.getValue() + "",
                                npCalories.getValue() + "", npFat.getValue() + "",
                                npCarbs.getValue() + "", 0, new ArrayList<>(),
                                0, new HashMap<>(), "");
                        r.setIngredients(ingredientsArray);
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        if (currentUser != null) {
                            r.setCopy_rights(currentUser.getUid());
                        }
                        if (uploadedImages.isEmpty()) {
                            imageViews.get(0).requestFocus();
                            Toast.makeText(getApplicationContext(), "Please add at least 1 photo",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        r.setImages(uploadedImages);
                        CRUD.loadDishToDatabase(r);
                        // Load recipe to database
                        if(currentUser!=null) {
                            addRecipeToUserByServer(currentUser.getUid(), r.getTitle());
                        }
//                        List<String> singleValueList = CRUD.getSingleValueList(r.getTitle());
//                        // Add recipe to the user recipes
//                        CRUD.addToUserLists(singleValueList, "recipes");
                        flag = false;
                        moveToActivity(r.getTitle());

                    }
                }
            }

            private void addRecipeToUserByServer(String uid, String title) {
                AppServer.SendToTheServer(uid,title);
            }
        });

    }

    private void moveToActivity(String title) {
        Toast.makeText(getApplicationContext(), title + "> was loaded!", Toast.LENGTH_LONG).show();
        selectedIngredients.clear();
        startActivity(new Intent(AddRecipe.this, SearchRecipe.class));
    }


//    public void dialogUploadImage(recipe r){
//        AlertDialog.Builder builder = new AlertDialog.Builder(AddRecipe.this, androidx.appcompat.R.style.Base_V7_Theme_AppCompat_Dialog);
//        final View customLayout = getLayoutInflater().inflate(R.layout.yes_no_dialog_layout, null);
//        builder.setView(customLayout);
//        builder.setCancelable(false);
//        builder.setTitle("Please upload an image");
//        builder.setPositiveButton("upload", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int which) {
//                ImagePicker.with(AddRecipe.this).start();
//                List<String> images = new ArrayList<>();
//                if (recipeImage != null) {
//                    Object tag = recipeImage.getTag();
//                    if (tag != null && tag instanceof String) {
//                        String srcUriString = (String) tag;
//                        Uri srcUri = Uri.parse(srcUriString);
//                        images.add(srcUri.toString());
//                        r.setImages(images);
//                    }
//                }
//                if (!r.getImages().isEmpty()){
//                    // Load recipe to database
//                    CRUD.loadDishToDatabase(r);
//                    List<String> singleValueList = CRUD.getSingleValueList(r.getTitle());
//                    // Add recipe to the user recipes
//                    CRUD.addToUserLists(singleValueList, "recipes");
//                    flag = false;
//                    Toast.makeText(getApplicationContext(), r.toString(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        builder.setNegativeButton("cancel", (dialogInterface, which) -> {
//            startActivity(new Intent(AddRecipe.this, SearchRecipe.class));
//        });
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }

    private String calcTime(int min) {
        if (min < 60) {
            return min + " mins";
        } else {
            return (int) min / 60 + " hrs " + min % 60 + " mins";
        }
    }

    public void addIngredientDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.insert_ingre_page);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);
        submit = dialog.findViewById(R.id.submitIngre);
        listView = dialog.findViewById(R.id.product_list);
        autoCompleteSearchView = dialog.findViewById(R.id.searchView);
        dialog.show();
        ingredientNamesList = new ArrayList<>();

        if (selectedIngredients == null || selectedIngredients.size() == 0) {
            selectedIngredients = new ArrayList<>();
        }
        InitAutoCompleteSearchView(dialog);
        adapter = new ListViewAdapter(dialog.getContext(), selectedIngredients, "add");
        listView.setAdapter(adapter);

        CRUD = new CRUD_RealTimeDatabaseData();

        submit.setOnClickListener(view -> {
            String data = ingredients(selectedIngredients);
            System.out.println(data);
            etIngredients.setText(data);
            dialog.dismiss();
        });


    }


    // Set the filter names list + set adapter for the autoCompleteSearchView
    private void InitAutoCompleteSearchView(Dialog d) {
        DatabaseReference mDatabaseSearchGet = FirebaseDatabase.getInstance()
                .getReference().child("ingredients");
        mDatabaseSearchGet.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ingredientNamesList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String name = snapshot.getValue(String.class);
                        ingredientNamesList.add(name);
                    }
                    arraySearchAdapter = new ArrayAdapter<>(AddRecipe.this, android.R.layout.simple_list_item_activated_1, ingredientNamesList);
                    autoCompleteSearchView.setAdapter(arraySearchAdapter);
                    autoCompleteSearchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                            String ingredient = parent.getItemAtPosition(pos).toString();
                            addIngredientAmountDialog(ingredient,d);
                            autoCompleteSearchView.setText("");

                        }
                    });
                    autoCompleteSearchView.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            // Hide my keyboard
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(d.getCurrentFocus().getApplicationWindowToken(), 0);
                        }
                    });
                }
            }
        });
    }

    private void addIngredientAmountDialog(String ingredient, Dialog d) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddRecipe.this, androidx.appcompat.R.style.Base_V7_Theme_AppCompat_Dialog);
        EditText editTextGrams = new EditText(this);
        editTextGrams.setInputType(InputType.TYPE_CLASS_NUMBER);
        editTextGrams.setTextColor(Color.rgb(255, 255, 255));
        builder.setView(editTextGrams);
        builder.setCancelable(false);
        builder.setTitle("The " + ingredient + "'s Amount in [g]");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
//                System.out.println("category- " + category + "\tsubCategory- " + subCategory);
                if (editTextGrams.getText().toString().isEmpty() || editTextGrams.getText().toString().charAt(0) == '0') {
                    Toast.makeText(builder.getContext(), "Please try again", Toast.LENGTH_LONG).show();
                    addIngredientAmountDialog(ingredient,d);
                } else {
                    addItem(editTextGrams.getText().toString(), ingredient);
                    Toast.makeText(builder.getContext(), ingredient + " added successfully", Toast.LENGTH_LONG).show();

                }
            }
        });

        builder.setNegativeButton("Cancel", (dialogInterface, which) -> {
            InitAutoCompleteSearchView(d);
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void removeItem(int i) {
        selectedIngredients.remove(i);
        listView.setAdapter(adapter);
    }

    private static void addItem(String amount, String ingredient) {
        selectedIngredients.add(amount + ":" + ingredient);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}
