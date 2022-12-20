package com.example.smarter_foodies;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;


import android.content.Intent;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginGoogle extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;

    ImageView logo;
    ImageView login;
    Animation topAnim;
    Animation bottomAnim;


    @Override
    protected void onStart() {
        super.onStart();
        checkUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_google);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Set animation to elements
        topAnim = AnimationUtils.loadAnimation(this, R.anim.anim_top);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.anim_bottom);


        logo = findViewById(R.id.imageView);
        login = findViewById(R.id.btnLogin);

        login.setAnimation(bottomAnim);
        logo.setAnimation(topAnim);



        mAuth = FirebaseAuth.getInstance();

        createRequest();

        login.setOnClickListener(view -> signIn());


    }


    private void createRequest() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                // ...
                Toast.makeText(LoginGoogle.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginGoogle.this, "Sign in success", Toast.LENGTH_LONG).show();
//                        Intent intent = new Intent(getApplicationContext(), ApplyChef.class);
//                        startActivity(intent);
                        checkUser();
                        // Sign in success, update UI with the signed-in user's information
                        //FirebaseUser fuser = mAuth.getCurrentUser();
//                            User user = new User();
//                        if (fuser != null) {
//                            String uid = fuser.getUid();
//                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
//                            reference.addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                    if (!dataSnapshot.exists()) {
//                                        Intent intent = new Intent(getApplicationContext(), ApplyChef.class);
//                                        startActivity(intent);
//                                    } else {
//                                        User user1 = dataSnapshot.getValue(User.class);
//                                        if (user1 != null && !user1.firstEntry) {
//                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                                            startActivity(intent);
//                                        }
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError error) {
//                                    Log.d("TAG", error.getMessage());
//                                }
//                            });
//
//                        }

                    } else {
                        Toast.makeText(LoginGoogle.this, "Sorry auth failed.", Toast.LENGTH_LONG).show();
                    }


                    // ...
                });


    }

    private void checkUser(){
        FirebaseUser fuser = mAuth.getCurrentUser();
        if (fuser != null) {
            String uid = fuser.getUid();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        AddEmailToEmailTree();
                        Intent intent = new Intent(getApplicationContext(), ApplyChef.class);
                        startActivity(intent);
                    } else {
                        User user1 = dataSnapshot.getValue(User.class);
                        if (user1 != null && !user1.firstEntry) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("TAG", error.getMessage());
                }
            });

        }

    }

    private void AddEmailToEmailTree()
    {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            String userEmail = firebaseUser.getEmail();
//            int index = userEmail.indexOf("@");
//            String preUserEmail = userEmail.substring(0,index);
            String preUserEmail = userEmail.replace(".", "{*}");
            String uid = firebaseUser.getUid();
            FirebaseDatabase.getInstance().getReference("email").child(preUserEmail).setValue(uid).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginGoogle.this, "Success on adding the email ", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(LoginGoogle.this, "Error adding the email ", Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    }


}

