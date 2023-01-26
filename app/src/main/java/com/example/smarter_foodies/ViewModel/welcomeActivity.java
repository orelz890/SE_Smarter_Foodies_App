package com.example.smarter_foodies.ViewModel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.smarter_foodies.R;

public class welcomeActivity extends AppCompatActivity {

    ImageView image;
    Animation topAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcom);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.anim_top);
        //Set animation to elements
        image = findViewById(R.id.imageView);

        image.setAnimation(topAnim);

        Intent intent = new Intent(getApplicationContext(), LoginGoogle.class);
        startActivity(intent);

    }
}