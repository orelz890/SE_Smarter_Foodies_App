package com.example.smarter_foodies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.example.smarter_foodies.ViewModel.AddRecipe;
import com.example.smarter_foodies.ViewModel.ApplyChef;
import com.example.smarter_foodies.ViewModel.LoginGoogle;
import com.example.smarter_foodies.ViewModel.MainActivity;
import com.example.smarter_foodies.ViewModel.UpdateRecipe;
import com.example.smarter_foodies.ViewModel.WeeklyPlan;
import com.example.smarter_foodies.ViewModel.likedRecipes;
import com.example.smarter_foodies.ViewModel.profile_page;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class DrawerBaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;


    boolean isChef = false;
    boolean flag = false;
    ActionBarDrawerToggle toggle = null;
    NavigationView navigationView = null;
    Toolbar toolbar = null;

    @Override
    public void setContentView(View view) {
        if (flag) {
            if (isChef) {
                if (navigationView != null) {
                    navigationView.getMenu().clear();
                    navigationView.inflateMenu(R.menu.main_drawer_menu_chef);
                }
            } else {
                if (navigationView != null) {
                    navigationView.getMenu().clear();
                    navigationView.inflateMenu(R.menu.main_drawer_menu_basic);
                }
            }
        } else {
            drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer_base_basic, null);
            FrameLayout container = drawerLayout.findViewById(R.id.activityContainer);
            container.removeAllViews();
            container.addView(view);
            super.setContentView(drawerLayout);
            invalidateOptionsMenu();

            if (toolbar != null) {
                toolbar.removeAllViews();
            }
            toolbar = drawerLayout.findViewById(R.id.tool_bar);

            if (toolbar != null) {
                toolbar.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white)); // Set background color to white
                toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.black)); // Set title text color to black
            }
            setSupportActionBar(toolbar);


            if (navigationView != null) {
                navigationView.removeAllViews();
            }
            navigationView = drawerLayout.findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.menu_drawer_open, R.string.menu_drawer_close);

            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        if (isChef) {
            handleChefMenu(item);
        } else {
            handleBasicMenu(item);
        }
        return false;
    }

    public void setUserContentView(View view, boolean isChef) {
        this.isChef = isChef;
        flag = true;
        setContentView(view);
    }

    private void handleChefMenu(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_weekly_planing:
                startActivity(new Intent(this, WeeklyPlan.class));
                // For smooth transition
                overridePendingTransition(0, 0);
                break;

            case R.id.nav_home:
                startActivity(new Intent(this, MainActivity.class));
                // For smooth transition
                overridePendingTransition(0, 0);
                break;


            case R.id.nav_favorites:             // need to build favorites page first
                startActivity(new Intent(this, likedRecipes.class));
                // For smooth transition
                overridePendingTransition(0, 0);
                break;
//
//            case R.id.nav_cart:
//                startActivity(new Intent(this, Cart.class));
//                overridePendingTransition(0,0);
//                break;

            case R.id.nav_profile:
                startActivity(new Intent(this, profile_page.class));
                // For smooth transition
                overridePendingTransition(0, 0);
                break;

            case R.id.nav_add_recipe:
                startActivity(new Intent(this, AddRecipe.class));
                overridePendingTransition(0, 0);
                break;

            case R.id.nav_update_recipe:
                startActivity(new Intent(this, UpdateRecipe.class));
                overridePendingTransition(0, 0);
                break;

            case R.id.nav_logOut:
                FirebaseAuth.getInstance().signOut();

                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id)) // Replace with your web client ID
                        .requestEmail()
                        .build();
                GoogleSignInClient client = GoogleSignIn.getClient(this, gso);
                client.signOut();

                startActivity(new Intent(this, LoginGoogle.class));
                overridePendingTransition(0, 0);
                break;

        }
    }

    private void handleBasicMenu(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_weekly_planing:
                startActivity(new Intent(this, WeeklyPlan.class));
                // For smooth transition
                overridePendingTransition(0, 0);
                break;
            case R.id.nav_home:
                startActivity(new Intent(this, MainActivity.class));
                // For smooth transition
                overridePendingTransition(0, 0);
                break;

            case R.id.nav_favorites:             // need to build favorites page first
                startActivity(new Intent(this, likedRecipes.class));
                // For smooth transition
                overridePendingTransition(0, 0);
                break;

//            case R.id.nav_cart:
//                startActivity(new Intent(this, Cart.class));
//                overridePendingTransition(0,0);
//                break;

            case R.id.nav_profile:
                startActivity(new Intent(this, profile_page.class));
                // For smooth transition
                overridePendingTransition(0, 0);
                break;

            case R.id.nav_apply_as_chef:
                startActivity(new Intent(this, ApplyChef.class));
                overridePendingTransition(0, 0);
                break;

            case R.id.nav_logOut:
                FirebaseAuth.getInstance().signOut();

                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id)) // Replace with your web client ID
                        .requestEmail()
                        .build();
                GoogleSignInClient client = GoogleSignIn.getClient(this, gso);
                client.signOut();

                startActivity(new Intent(this, LoginGoogle.class));
                overridePendingTransition(0, 0);
                break;
        }
    }

    protected void allocateActivityTitle(String titleString) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(titleString);
        }
    }

}