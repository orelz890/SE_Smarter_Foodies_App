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

import com.example.smarter_foodies.Fragment.HomeFragment;
import com.example.smarter_foodies.Fragment.NotificationFragment;
import com.example.smarter_foodies.Fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment selectFragment = null;
    Button btnLogOut;
    Button btnAddRecipe;
    Button btnUpdateRecipe;
    Button btnSearchView;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.btn_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        btnLogOut = findViewById(R.id.btnLogout);
        btnAddRecipe = findViewById(R.id.btnAddRecipe);
        btnUpdateRecipe = findViewById(R.id.btnUpdateRecipe);
        btnSearchView = findViewById(R.id.btnSearchView);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment()).commit();

        btnAddRecipe.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, AddRecipe.class));
        });

        btnUpdateRecipe.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, UpdateRecipe.class));
        });

        btnSearchView.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, SearchRecipe.class));
        });

        mAuth = FirebaseAuth.getInstance();
        btnLogOut.setOnClickListener(view -> {
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, LoginGoogle.class));
        });

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.nav_home:
                            selectFragment = new HomeFragment();
                            break;
                        case R.id.nav_search:
//                            selectFragment = new SearchFragment();
                            startActivity(new Intent(MainActivity.this, SearchRecipe.class));
                            break;
                        case R.id.nav_add:
                            selectFragment = null;
                            startActivity(new Intent(MainActivity.this, AddRecipe.class));
                            break;
                        case R.id.nav_favorites:
                            selectFragment = new NotificationFragment();
                            break;
                        case R.id.nav_profile:
                            SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                            editor.putString("Profileid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            editor.apply();
                            selectFragment = new ProfileFragment();
                            break;
                    }
                    if (selectFragment != null){
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, selectFragment).commit();
                    }
                    return true;
                }
            };

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(MainActivity.this, LoginGoogle.class));
        }
    }
}