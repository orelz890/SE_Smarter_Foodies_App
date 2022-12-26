package com.example.smarter_foodies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class UpdateProfile extends AppCompatActivity {
    Button button;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    TextInputLayout fullName, email, eating, favorite, website, isChef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        fullName = findViewById(R.id.full_name_up);
        email = findViewById(R.id.email_up);
        eating = findViewById(R.id.type_up);
        favorite = findViewById(R.id.favorite_up);
        website = findViewById(R.id.website_up);
        isChef = findViewById(R.id.chef_up);
        button = findViewById(R.id.btn_up);

        setTextViewContent();

    }

    private void setTextViewContent(){
        // to show the cuurent data on the scrren
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
            reference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            Objects.requireNonNull(fullName.getEditText()).setText(user.getName());
                            Objects.requireNonNull(email.getEditText()).setText(user.getEmail());
                            Objects.requireNonNull(eating.getEditText()).setText(user.getEating());
                            Objects.requireNonNull(favorite.getEditText()).setText(user.getFavorite());
                            Objects.requireNonNull(website.getEditText()).setText(user.getWebsite());
                            Objects.requireNonNull(isChef.getEditText()).setText("" + user.isChef());

                            button.setOnClickListener(view -> {
                                UpdateProfileUserTree(reference, user);
                            });
                        }
                    }
                }
            });
        }
    }

    private void UpdateProfileUserTree(DatabaseReference reference, User user) {
        // what we get after the changing
        user.setName(String.valueOf(Objects.requireNonNull(this.fullName.getEditText()).getText()));
        user.setEmail(String.valueOf(Objects.requireNonNull(this.email.getEditText()).getText()));
        user.setEating(String.valueOf(Objects.requireNonNull(this.eating.getEditText()).getText()));
        user.setFavorite(String.valueOf(Objects.requireNonNull(this.favorite.getEditText()).getText()));
        user.setWebsite(String.valueOf(Objects.requireNonNull(this.website.getEditText()).getText()));

        reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(UpdateProfile.this, "Data updated!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(UpdateProfile.this, profile_page.class));

            }
        });
    }
}
