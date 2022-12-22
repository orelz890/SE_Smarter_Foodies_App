package com.example.smarter_foodies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
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
    TextInputLayout fullname, email, eating, favrorit, wabsite, ischef;
    TextView posts, num_of_dishs, following;
    Button update_profile;
    FirebaseUser firebaseUser;
    String Uid;
    ImageButton my_fotos, saved_fotos;


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
        num_of_dishs = findViewById(R.id.num_of_dishs);
        following = findViewById(R.id.following);
        fullname = findViewById(R.id.full_name_pp);
        email = findViewById(R.id.email_pp);
        update_profile = findViewById(R.id.update_profile);
        my_fotos = findViewById(R.id.my_fotos);
        saved_fotos = findViewById(R.id.save_fotos);
        eating = findViewById(R.id.type_pp);
        favrorit = findViewById(R.id.favorit_pp);
        wabsite = findViewById(R.id.website_pp);
        ischef = findViewById(R.id.chaf_pp);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String cuurentid = user.getUid();
        this.Uid = cuurentid;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(Uid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot_filter) {

                String nameresult = "",emailresult = " ",eatingresult = " ",
                        Favoritrecpieresult = " ",wabsiteresult =" " ,isChefresult = "";
                if (snapshot_filter.exists()) {
                    for (DataSnapshot child : snapshot_filter.getChildren()) {
                        if (child.getKey().equals("Nickname")) {
                            nameresult = child.getValue(String.class);
                        }
                        else if (child.getKey().equals("Email")) {
                            emailresult = child.getValue(String.class);
                        }
                        else if (child.getKey().equals("Eating")) {
                            eatingresult = child.getValue(String.class);
                        }
                        else if (child.getKey().equals("Favrorit")) {
                            Favoritrecpieresult = child.getValue(String.class);
                        }
                        else if (child.getKey().equals("Wabsite")) {
                            wabsiteresult = child.getValue(String.class);
                        }

                        else if (child.getKey().equals("isChef")) {
                            boolean symbol = child.getValue(boolean.class);
                            if(symbol){
                                isChefresult = "True";
                            }
                            else{
                                isChefresult = "False";
                            }
                        }
                    }
                    fullname.getEditText().setText(nameresult);
                    email.getEditText().setText(emailresult);
                    eating.getEditText().setText(eatingresult);
                    favrorit.getEditText().setText(Favoritrecpieresult);
                    wabsite.getEditText().setText(wabsiteresult);
                    ischef.getEditText().setText(isChefresult);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

    public View onCreatView(LayoutInflater inflater, ViewGroup continer, Bundle savedinstancestate) {

        View view = inflater.inflate(R.layout.activity_fragment_profile, continer, false);

        FirebaseUser fuser = mAuth.getCurrentUser();
        if (fuser != null) {
            String uid = fuser.getUid();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
            String name = reference.child("Nickname").toString();
            String eemail = reference.child("Email").toString();
            String ttype_food = reference.child("Type_food").toString();
            String ffavorite = reference.child("favorite").toString();
            String wwab = reference.child("Wabsite").toString();

            Objects.requireNonNull(fullname.getEditText()).setText(name);
            Objects.requireNonNull(email.getEditText()).setText(eemail);
            Objects.requireNonNull(eating.getEditText()).setText(ttype_food);
            Objects.requireNonNull(wabsite.getEditText()).setText(wwab);
            Objects.requireNonNull(favrorit.getEditText()).setText(ffavorite);

            return view;
        }


        return view;
    }



    private void UpdateProfilecaller() throws MessagingException, IOException, GeneralSecurityException {
        startActivity(new Intent(fragment_profile.this, UpdateProfile.class));
    }


}