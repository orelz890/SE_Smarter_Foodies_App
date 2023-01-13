package com.example.smarter_foodies;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

//
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.text.InputType;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.AutoCompleteTextView;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import java.util.ArrayList;
//
public class IngrePage extends AppCompatActivity {
    //
//    AutoCompleteTextView autoCompleteSearchView;
//    ImageButton submit;
//    ArrayAdapter<String> arraySearchAdapter;
//
//    ArrayList<String> ingredientNamesList;
//    static ArrayList<String> selectedIngredients;
//    static ListView listView;
//    static ListViewAdapter adapter;
//
//
//    CRUD_RealTimeDatabaseData CRUD;
//    Bundle mbBundle;
//
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        LinearLayout rootLayout = new LinearLayout(this);
//        rootLayout.setOrientation(LinearLayout.VERTICAL);
//        View activityMainView = LayoutInflater.from(this).inflate(R.layout.insert_ingre_page, rootLayout, false);
//        rootLayout.addView(activityMainView);
//        setContentView(rootLayout);
//        ingredientNamesList = new ArrayList<>();
//        mbBundle = getIntent().getExtras();
//
//        if (mbBundle == null || mbBundle.getStringArrayList("IngredientsFromAddPage") == null) {
//            selectedIngredients = new ArrayList<>();
//        } else {
//            selectedIngredients = new ArrayList<>(mbBundle.getStringArrayList("IngredientsFromAddPage"));
//
//        }
//
////        selectedIngredients = new ArrayList<>();
//        submit = findViewById(R.id.submitIngre);
//        listView = findViewById(R.id.product_list);
//        adapter = new ListViewAdapter(getApplicationContext(), selectedIngredients);
//        listView.setAdapter(adapter);
//
//        CRUD = new CRUD_RealTimeDatabaseData();
//
//        submit.setOnClickListener(view -> {
//            sendSelectedIngredients();
//        });
//
//        InitAutoCompleteSearchView();
//
    }
}
//
//
//    private void sendSelectedIngredients() {
//        String key = mbBundle.getString("key");
//        if (key.equals("add")) {
//            Intent intent = new Intent(IngrePage.this, AddRecipe.class);
//            intent.putExtra("IngredientsFromIngrePage", selectedIngredients);
//            System.out.println("Ingre " + selectedIngredients.toString());
//            startActivity(intent);
//        }else{
//            Intent intent = new Intent(IngrePage.this, UpdateRecipe.class);
//            intent.putExtra("IngredientsFromIngrePage", selectedIngredients);
//            System.out.println("Ingre " + selectedIngredients.toString());
//            startActivity(intent);
//        }
//    }
//
//    // Set the filter names list + set adapter for the autoCompleteSearchView
//    private void InitAutoCompleteSearchView() {
//        autoCompleteSearchView = findViewById(R.id.searchView);
//        DatabaseReference mDatabaseSearchGet = FirebaseDatabase.getInstance()
//                .getReference().child("ingredients");
//        mDatabaseSearchGet.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
//            @Override
//            public void onSuccess(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    ingredientNamesList.clear();
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        String name = snapshot.getValue(String.class);
//                        ingredientNamesList.add(name);
//                    }
//                    arraySearchAdapter = new ArrayAdapter<>(IngrePage.this, android.R.layout.simple_list_item_activated_1, ingredientNamesList);
//                    autoCompleteSearchView.setAdapter(arraySearchAdapter);
//                    autoCompleteSearchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
//                            String ingredient = parent.getItemAtPosition(pos).toString();
//                            addIngredientAmountDialog(ingredient);
//                            autoCompleteSearchView.setText("");
//
//                        }
//                    });
//                    autoCompleteSearchView.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
//                        @Override
//                        public void onDismiss() {
//                            // Hide my keyboard
//                            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), 0);
//                        }
//                    });
//                }
//            }
//        });
//    }
//
//    private void addIngredientAmountDialog(String ingredient) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(IngrePage.this, androidx.appcompat.R.style.Base_V7_Theme_AppCompat_Dialog);
//        EditText editTextGrams = new EditText(this);
//        editTextGrams.setInputType(InputType.TYPE_CLASS_NUMBER);
//        editTextGrams.setTextColor(Color.rgb(255,255,255));
//        builder.setView(editTextGrams);
//        builder.setCancelable(false);
//        builder.setTitle("The " + ingredient + " Amount in [g]" );
//        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int which) {
////                System.out.println("category- " + category + "\tsubCategory- " + subCategory);
//                if (editTextGrams.getText().toString().isEmpty() || editTextGrams.getText().toString().charAt(0) == '0' ) {
//                    Toast.makeText(builder.getContext(),"Please try again",Toast.LENGTH_LONG).show();
//                    addIngredientAmountDialog(ingredient);
//                }else
//                {
//                    addItem(editTextGrams.getText().toString(),ingredient);
//                    Toast.makeText(builder.getContext(),"added successfully",Toast.LENGTH_LONG).show();
//
//                }
//            }
//        });
//
//        builder.setNegativeButton("Cancel", (dialogInterface, which) -> {
//            InitAutoCompleteSearchView();
//        });
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }
//
//    public static void removeItem(int i) {
//        selectedIngredients.remove(i);
//        listView.setAdapter(adapter);
//    }
//
//    private static void addItem(String amount, String ingredient) {
//        selectedIngredients.add(amount + ":" + ingredient);
//        adapter.notifyDataSetChanged();
//    }
//
//
//}
