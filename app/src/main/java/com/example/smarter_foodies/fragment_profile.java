package com.example.smarter_foodies;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Objects;

import javax.mail.MessagingException;

public class fragment_profile extends DashboardActivity {
    private FirebaseAuth mAuth;

    ImageView image_profile, options;
    TextInputLayout name, email, eating, favorite, website, isChef;
    TextView posts, num_of_dishes, following;
    Button update_profile;
    FirebaseUser firebaseUser;
    String Uid;
    ImageButton my_photos, saved_photos;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        View activityMainView = LayoutInflater.from(this).inflate(R.layout.activity_fragment_profile, rootLayout, false);
        rootLayout.addView(activityMainView);
        setContentView(rootLayout);
        allocateActivityTitle("My Profile");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        image_profile = findViewById(R.id.image_profile);
        posts = findViewById(R.id.posts);
        num_of_dishes = findViewById(R.id.num_of_dishs);
        following = findViewById(R.id.following);
        name = findViewById(R.id.full_name_pp);
        email = findViewById(R.id.email_pp);
        update_profile = findViewById(R.id.update_profile);
        my_photos = findViewById(R.id.my_fotos);
        saved_photos = findViewById(R.id.save_fotos);
        eating = findViewById(R.id.type_pp);
        favorite = findViewById(R.id.favorit_pp);
        website = findViewById(R.id.website_pp);
        isChef = findViewById(R.id.chef_pp);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String cuurentid = user.getUid();
        this.Uid = cuurentid;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(Uid);

        reference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        Objects.requireNonNull(name.getEditText()).setText(user.getName());
                        Objects.requireNonNull(email.getEditText()).setText(user.getEmail());
                        Objects.requireNonNull(eating.getEditText()).setText(user.getEating());
                        Objects.requireNonNull(favorite.getEditText()).setText(user.getFavorite());
                        Objects.requireNonNull(website.getEditText()).setText(user.getWebsite());
                        Objects.requireNonNull(isChef.getEditText()).setText(user.isChef() + "");
                    }
                }
            }
        });

        update_profile.setOnClickListener(view -> {
            try {
                UpdateProfilecaller();
            } catch (MessagingException | IOException | GeneralSecurityException e) {
                e.printStackTrace();
            }
        });

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup continer, Bundle savedinstancestate) {

        View view = inflater.inflate(R.layout.activity_fragment_profile, continer, false);

        FirebaseUser fuser = mAuth.getCurrentUser();
        if (fuser != null) {
            String uid = fuser.getUid();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
            String newName = reference.child("name").toString();
            String newEating = reference.child("eating").toString();
            String newFavorite = reference.child("favorite").toString();
            String newWebsite = reference.child("website").toString();

            Objects.requireNonNull(name.getEditText()).setText(newName);
            Objects.requireNonNull(eating.getEditText()).setText(newEating);
            Objects.requireNonNull(website.getEditText()).setText(newWebsite);
            Objects.requireNonNull(favorite.getEditText()).setText(newFavorite);

            return view;
        }
        return view;
    }



    private void UpdateProfilecaller() throws MessagingException, IOException, GeneralSecurityException {
        startActivity(new Intent(fragment_profile.this, UpdateProfile.class));
    }


}