package com.example.smarter_foodies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smarter_foodies.databinding.ActivityDashboardBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends DashboardActivity {

    ActivityDashboardBinding activityDashboardBinding;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        activityDashboardBinding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(activityDashboardBinding.getRoot());
//        setContentView(R.layout.activity_main);
        allocateActivityTitle("Home");

//        btnLogOut = findViewById(R.id.btnLogout);
//        btnAddRecipe = findViewById(R.id.btnAddRecipe);
//        btnUpdateRecipe = findViewById(R.id.btnUpdateRecipe);
//        btnSearchView = findViewById(R.id.btnSearchView);
//
//        btnAddRecipe.setOnClickListener(view -> {
//            startActivity(new Intent(MainActivity.this, AddRecipe.class));
//        });
//
//        btnUpdateRecipe.setOnClickListener(view -> {
//            startActivity(new Intent(MainActivity.this, UpdateRecipe.class));
//        });
//
//        btnSearchView.setOnClickListener(view -> {
//            startActivity(new Intent(MainActivity.this, SearchRecipe.class));
//        });
//
//        btnLogOut.setOnClickListener(view -> {
//            mAuth.signOut();
//            startActivity(new Intent(MainActivity.this, LoginGoogle.class));
//        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(MainActivity.this, LoginGoogle.class));
        }
    }
}