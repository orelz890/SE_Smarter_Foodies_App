package com.example.smarter_foodies.ViewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.smarter_foodies.Model.User;
import com.example.smarter_foodies.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class UpdateProfile extends AppCompatActivity {
    Button button;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    TextInputLayout fullName, email, eating, favorite, website, isChef;

    private String myEmail, myNickname;


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

        // Inside your activity or fragment code
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            myEmail = account.getEmail();
            myNickname = account.getDisplayName();
            // Now you can use the userEmail as needed
        } else {
            // No user is currently authenticated
        }

        setTextViewContent();

    }

    private void setTextViewContent(){
        // to show the cuurent data on the scrren
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();

//            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
//            reference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
//                @Override
//                public void onSuccess(DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.exists()) {
//                        User user = dataSnapshot.getValue(User.class);
//                        if (user != null) {
////                            Objects.requireNonNull(fullName.getEditText()).setText(user.getName());
//                            Objects.requireNonNull(fullName.getEditText()).setText(myNickname);
//                            Objects.requireNonNull(email.getEditText()).setText(myEmail);
////                            Objects.requireNonNull(email.getEditText()).setText(user.getEmail());
////                            Objects.requireNonNull(eating.getEditText()).setText(user.getEating());
////                            Objects.requireNonNull(favorite.getEditText()).setText(user.getFavorite());
////                            Objects.requireNonNull(website.getEditText()).setText(user.getWebsite());
////                            Objects.requireNonNull(isChef.getEditText()).setText("" + user.isChef());
//
//                            button.setOnClickListener(view -> {
//                                UpdateProfileUserTree(reference, user);
//                            });
//                        }
//                    }
//                }
//            });
                Objects.requireNonNull(fullName.getEditText()).setText(myNickname);
                Objects.requireNonNull(email.getEditText()).setText(myEmail);
                button.setOnClickListener(view -> {
                        UpdateProfileUserTree();
                    });
            }
//        }
    }

    private void UpdateProfileUserTree() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(String.valueOf(Objects.requireNonNull(this.fullName.getEditText()).getText()))
                    .build();

            firebaseUser.updateProfile(profileUpdates);
        }
    }
}
