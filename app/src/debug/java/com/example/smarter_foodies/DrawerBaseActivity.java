package com.example.smarter_foodies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class DrawerBaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;


    boolean isChef = false;
    boolean flag = false;
    ActionBarDrawerToggle toggle = null;
    NavigationView navigationView = null;
    Toolbar toolbar = null;

    @Override
    public void setContentView(View view){
        if (flag) {
            if (isChef) {
                if (navigationView != null) {
                    navigationView.getMenu().clear();
                    navigationView.inflateMenu(R.menu.main_drawer_menu_chef);
                }
            }else{
                if (navigationView != null) {
                    navigationView.getMenu().clear();
                    navigationView.inflateMenu(R.menu.main_drawer_menu_basic);
                }
            }
        }else {
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
        }
        else {
            handleBasicMenu(item);
        }
        return false;
    }

    public void setUserContentView(View view, boolean isChef){
        this.isChef = isChef;
        flag = true;
        setContentView(view);
    }

    private void handleChefMenu(MenuItem item){
        switch (item.getItemId()){
            case R.id.nav_weekly_planing:

            case R.id.nav_home:
                startActivity(new Intent(this, MainActivity.class));
                // For smooth transition
                overridePendingTransition(0,0);
                break;


            case R.id.nav_favorites:             // need to build favorites page first
                startActivity(new Intent(this, likedRecipes.class));
                // For smooth transition
                overridePendingTransition(0,0);
                break;

            case R.id.nav_profile:
                startActivity(new Intent(this, fragment_profile.class));
                // For smooth transition
                overridePendingTransition(0,0);
                break;


            case R.id.nav_search:
                startActivity(new Intent(this, SearchRecipe.class));
                overridePendingTransition(0,0);
                break;

            case R.id.nav_add_recipe:
                startActivity(new Intent(this, AddRecipe.class));
                overridePendingTransition(0,0);
                break;

            case R.id.nav_update_recipe:
                startActivity(new Intent(this, UpdateRecipe.class));
                overridePendingTransition(0,0);
                break;

            case R.id.nav_logOut:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LoginGoogle.class));
                overridePendingTransition(0,0);
                break;

        }
    }

    private void handleBasicMenu(MenuItem item){
        switch (item.getItemId()){
            case R.id.nav_weekly_planing:

            case R.id.nav_home:
                startActivity(new Intent(this, MainActivity.class));
                // For smooth transition
                overridePendingTransition(0,0);
                break;

            case R.id.nav_favorites:             // need to build favorites page first
                startActivity(new Intent(this, likedRecipes.class));
                // For smooth transition
                overridePendingTransition(0,0);
                break;

            case R.id.nav_profile:
                startActivity(new Intent(this, fragment_profile.class));
                // For smooth transition
                overridePendingTransition(0,0);
                break;


            case R.id.nav_search:
                startActivity(new Intent(this, SearchRecipe.class));
                overridePendingTransition(0,0);
                break;

            case R.id.nav_apply_as_chef:
                startActivity(new Intent(this, ApplyChef.class));
                overridePendingTransition(0,0);
                break;

            case R.id.nav_logOut:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LoginGoogle.class));
                overridePendingTransition(0,0);
                break;
        }
    }

    protected void allocateActivityTitle(String titleString){
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(titleString);
        }
    }

}