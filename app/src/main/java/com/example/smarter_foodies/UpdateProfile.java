package com.example.smarter_foodies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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
    TextInputLayout fullname, email, eating, favrorit, wabsite, ischef;
    String Uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        fullname = findViewById(R.id.full_name_up);
        email = findViewById(R.id.email_up);
        eating = findViewById(R.id.type_up);
        favrorit = findViewById(R.id.favorit_up);
        wabsite = findViewById(R.id.website_up);
        ischef = findViewById(R.id.chef_up);
        button = findViewById(R.id.btn_up);


        // to show the cuurent data on the scrren
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String cuurentid = user.getUid();
        this.Uid = cuurentid;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(Uid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot_filter) {
                if (snapshot_filter.exists()) {
                    System.out.println(snapshot_filter);
                    User user = snapshot_filter.getValue(User.class);
                    System.out.println(user);
                    if (user != null) {
                        Objects.requireNonNull(fullname.getEditText()).setText(user.getName());
                        Objects.requireNonNull(email.getEditText()).setText(user.getEmail());
                        Objects.requireNonNull(eating.getEditText()).setText(user.getEating());
                        Objects.requireNonNull(favrorit.getEditText()).setText(user.getFavorite());
                        Objects.requireNonNull(wabsite.getEditText()).setText(user.getWebsite());
                        Objects.requireNonNull(ischef.getEditText()).setText("" + user.isChef());
                    }

                    button.setOnClickListener(view -> {
                        UpdateProfileUserTree(reference, user);
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void UpdateProfileUserTree(DatabaseReference reference, User user) {
        // what we get after the changing
        user.setName(String.valueOf(Objects.requireNonNull(this.fullname.getEditText()).getText()));
        user.setEmail(String.valueOf(Objects.requireNonNull(this.email.getEditText()).getText()));
        user.setEating(String.valueOf(Objects.requireNonNull(this.eating.getEditText()).getText()));
        user.setFavorite(String.valueOf(Objects.requireNonNull(this.favrorit.getEditText()).getText()));
        user.setWebsite(String.valueOf(Objects.requireNonNull(this.wabsite.getEditText()).getText()));

        reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(UpdateProfile.this, "Data updated!", Toast.LENGTH_LONG).show();
            }
        });

    }
}






