package com.example.smarter_foodies;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import org.w3c.dom.Document;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.mail.MessagingException;

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
        ischef = findViewById(R.id.chaf_up);
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


                    button.setOnClickListener(view -> {
                        UpdateProfilecaller();
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void UpdateProfilecaller() {
        // what we get after the changing
        String nname = String.valueOf(fullname.getEditText().getText());
        String eemail = String.valueOf(email.getEditText().getText());
        String eeating = String.valueOf(eating.getEditText().getText());
        String ffavrorit = String.valueOf(favrorit.getEditText().getText());
        String wwabsite = String.valueOf(wabsite.getEditText().getText());

        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (fuser != null) {
            String uid = fuser.getUid();

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

            //update the name
            DatabaseReference hopperRef = reference;
            Map<String, Object> hopperUpdates = new HashMap<>();
            hopperUpdates.put("Nickname", nname);
            hopperRef.updateChildren(hopperUpdates);

            //update the email
            DatabaseReference a = reference;
            Map<String, Object> b = new HashMap<>();
            b.put("Email", eemail);
            a.updateChildren(b);

            //update the Eating
            DatabaseReference c = reference;
            Map<String, Object> d = new HashMap<>();
            d.put("Eating", eeating);
            c.updateChildren(d);

            //update the Favrorit
            DatabaseReference e = reference;
            Map<String, Object> f = new HashMap<>();
            f.put("Favrorit", ffavrorit);
            e.updateChildren(f);

            //update the Wabsite
            DatabaseReference g = reference;
            Map<String, Object> h = new HashMap<>();
            h.put("Wabsite", wwabsite);
            g.updateChildren(h);


        }
    }
}






