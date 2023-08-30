package com.example.smarter_foodies.ViewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.smarter_foodies.DashboardActivity;
import com.example.smarter_foodies.Model.User;
import com.example.smarter_foodies.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Objects;

import javax.mail.MessagingException;


public class profile_page extends DashboardActivity {
    // for the profile
    TextInputEditText name_p, email_p, ischaf, fevorit_recpie_p, website_p, ranking_p;
    ImageView image_profile, likes_pages, ranking_btn, uploads_pages;
    Button update_profile_btn, update_photo_btn;
    FirebaseUser firebaseUser;
    String Uid;

    //for the popup update
    TextInputEditText name_pop, email_pop, fevorit_recpie_pop, website_pop, ranking_pop;
    Button save_btn_pop, cancel_btn_pop;

    private AlertDialog.Builder dialogbilder;
    private AlertDialog dialog;

    // the rating app
    RatingBar ratingBar;
    Button save_rate, cancel_rate, submit_rate;
    TextInputEditText text_rating;
    private AlertDialog dialog_for_ranking;
    private AlertDialog.Builder dialogbilder_for_ranking;
    private String email, nickname;
    private ImageView IV_choose_pic;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        View activityMainView = LayoutInflater.from(this).inflate(R.layout.activity_profile_page, rootLayout, false);
        rootLayout.addView(activityMainView);
        setContentView(rootLayout);

        // Inside your activity or fragment code
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            email = account.getEmail();
            nickname = account.getDisplayName();
            // Now you can use the userEmail as needed
        } else {
            // No user is currently authenticated
        }

        // init the text view
        name_p = findViewById(R.id.full_name_pp);
        ranking_p = findViewById(R.id.ranking_p);
        ischaf = findViewById(R.id.is_chaf_pp);
        website_p = findViewById(R.id.wabsite_pp);
        email_p = findViewById(R.id.email_pp);
        fevorit_recpie_p = findViewById(R.id.favorit_recipe_pp);
        IV_choose_pic = findViewById(R.id.IV_choose_pic);

        // init the buttons
        uploads_pages = findViewById(R.id.imageViewuploeds);
        ranking_btn = findViewById(R.id.Ranking_pp);
        likes_pages = findViewById(R.id.imageViewLikes);
        update_profile_btn = findViewById(R.id.update_profile_pp);
        update_photo_btn = findViewById(R.id.update_photo_pp);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        image_profile = findViewById(R.id.imageView4);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        this.Uid = user.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(Uid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot_users) {
                if (snapshot_users.exists()) {
                    User user = snapshot_users.getValue(User.class);
                    name_p.setText(nickname);
//                    name_p.setText(user.getName());
//                    email_p.setText(user.getEmail());
                    email_p.setText(email);
                    ranking_p.setText(user.getEating());
                    fevorit_recpie_p.setText(user.getFavorite());
                    website_p.setText(user.getWebsite());
                    ischaf.setText(user.isChef() + "");
                }
                // set the image profile
                Uri uri = firebaseUser.getPhotoUrl();
                Picasso.get().load(uri).into(image_profile);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        likes_pages.setOnClickListener(view -> {
            try {
                moveto_favorit_dishs();
            } catch (MessagingException | IOException | GeneralSecurityException e) {
                e.printStackTrace();
            }
        });

        uploads_pages.setOnClickListener(view -> {
            try {
                moveto_my_upload_dishs();
            } catch (MessagingException | IOException | GeneralSecurityException e) {
                e.printStackTrace();
            }
        });

        update_profile_btn.setOnClickListener(view -> {
            createNewContactDialog();
        });

//        update_photo_btn.setOnClickListener(view -> {
//            try {
//                moveto_my_update_photo();
//            } catch (MessagingException | IOException | GeneralSecurityException e) {
//                e.printStackTrace();
//            }
//        });

        IV_choose_pic.setOnClickListener(view -> {
            try {
                moveto_my_update_photo();
            } catch (MessagingException | IOException | GeneralSecurityException e) {
                e.printStackTrace();
            }
        });


        ranking_btn.setOnClickListener(view -> {
            createNewContactDialogforranking();
        });

    }


    private void moveto_favorit_dishs() throws MessagingException, IOException, GeneralSecurityException {
        startActivity(new Intent(profile_page.this, likedRecipes.class));
    }

    private void moveto_my_upload_dishs() throws MessagingException, IOException, GeneralSecurityException {
        startActivity(new Intent(profile_page.this, my_uploads.class));
    }

    private void moveto_my_update_photo() throws MessagingException, IOException, GeneralSecurityException {
        startActivity(new Intent(profile_page.this, update_profile_photo.class));
    }


    public void createNewContactDialog() {

        dialogbilder = new AlertDialog.Builder(this);
        final View popupview = getLayoutInflater().inflate(R.layout.activity_popup, null);
        name_pop = popupview.findViewById(R.id.name_up);
        email_pop = popupview.findViewById(R.id.email_uup);
        fevorit_recpie_pop = popupview.findViewById(R.id.favorit_recipe_up);
        website_pop = popupview.findViewById(R.id.wabsite_up);

        save_btn_pop = popupview.findViewById(R.id.update_profile_up);
        cancel_btn_pop = popupview.findViewById(R.id.Cancel_changing_up);

        dialogbilder.setView(popupview);
        dialog = dialogbilder.create();
        dialog.show();


        // to show the cuurent data on the scrren
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Objects.requireNonNull(name_pop).setText(nickname);
            Objects.requireNonNull(email_pop).setText(email);
            save_btn_pop.setOnClickListener(view -> {
                UpdateProfileUserTree(Objects.requireNonNull(name_pop.getText()).toString());
                startActivity(new Intent(profile_page.this, profile_page.class));
            });

//            String uid = user.getUid();
//
//            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
//
//            reference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot_users) {
//                    if (snapshot_users.exists()) {
//                        User user = snapshot_users.getValue(User.class);
//                        if (user != null) {
////                            Objects.requireNonNull(name_pop).setText(user.getName());
////                            Objects.requireNonNull(email_pop).setText(user.getEmail());
//                            Objects.requireNonNull(fevorit_recpie_pop).setText(user.getFavorite());
//                            Objects.requireNonNull(website_pop).setText(user.getWebsite());
//                            Objects.requireNonNull(ischaf).setText("" + user.isChef());
//
//                            Objects.requireNonNull(name_pop).setText(nickname);
//                            Objects.requireNonNull(email_pop).setText(email);
//                            save_btn_pop.setOnClickListener(view -> {
//                                UpdateProfileUserTree(reference, user);
//                                startActivity(new Intent(profile_page.this, profile_page.class));
//                            });
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//
//                private void UpdateProfileUserTree(DatabaseReference reference, User user) {
//                    // what we get after the changing
//                    user.setName(String.valueOf(Objects.requireNonNull(name_pop).getText()));
//                    user.setEmail(String.valueOf(Objects.requireNonNull(email_pop).getText()));
//                    user.setFavorite(String.valueOf(Objects.requireNonNull(fevorit_recpie_pop).getText()));
//                    user.setWebsite(String.valueOf(Objects.requireNonNull(website_pop).getText()));
//
//                    reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            Toast.makeText(profile_page.this, "Data updated!", Toast.LENGTH_LONG).show();
//                        }
//                    });
//
//                }
//            });
        }

        cancel_btn_pop.setOnClickListener(view -> {

            startActivity(new Intent(profile_page.this, profile_page.class));
        });

    }

    public void createNewContactDialogforranking() {
        dialogbilder_for_ranking = new AlertDialog.Builder(this);
        final View popupview_ranking = getLayoutInflater().inflate(R.layout.ranking_popup, null);
        ratingBar = popupview_ranking.findViewById(R.id.ranking_up_bar);
        save_rate = popupview_ranking.findViewById(R.id.save_ranking_up);
        cancel_rate = popupview_ranking.findViewById(R.id.Cancel_ranking_up);
        text_rating = popupview_ranking.findViewById(R.id.ranking_up_txt);
        submit_rate = popupview_ranking.findViewById(R.id.submit);

        dialogbilder_for_ranking.setView(popupview_ranking);
        dialog_for_ranking = dialogbilder_for_ranking.create();
        dialog_for_ranking.show();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot_users) {
                    if (snapshot_users.exists()) {
                        User user = snapshot_users.getValue(User.class);
                        if (user != null) {
                            Objects.requireNonNull(text_rating).setText(user.getRating());
                            submit_rate.setOnClickListener(view -> {
                                String sub = String.valueOf(ratingBar.getRating());
                                text_rating.setText(sub);
                            });
                            save_rate.setOnClickListener(view -> {
                                UpdateProfileUserTreee(reference, user);
                                startActivity(new Intent(profile_page.this, profile_page.class));
                            });
                            cancel_rate.setOnClickListener(view -> {
                                startActivity(new Intent(profile_page.this, profile_page.class));
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

                private void UpdateProfileUserTreee(DatabaseReference reference, User user) {
                    // what we get after the changing

                    user.setRating(String.valueOf(Objects.requireNonNull(text_rating).getText()));

                    reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(profile_page.this, "Data updated!", Toast.LENGTH_LONG).show();
                        }
                    });

                }
            });


        }
    }

    private void UpdateProfileUserTree(String name) {
        System.out.println("/n/nUpdateProfileUserTree -- Name = " + name + "/n/n");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build();

            firebaseUser.updateProfile(profileUpdates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("ProfileUpdate", "User profile updated.");
                        } else {
                            Log.e("ProfileUpdate", "Error updating user profile.", task.getException());
                        }
                    });
        } else {
            Log.d("ProfileUpdate", "No user is currently authenticated.");
        }

    }


}



