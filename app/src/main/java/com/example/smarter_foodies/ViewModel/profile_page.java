package com.example.smarter_foodies.ViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.smarter_foodies.DashboardActivity;
import com.example.smarter_foodies.Model.User;
import com.example.smarter_foodies.R;
import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Objects;

import javax.mail.MessagingException;


public class profile_page extends DashboardActivity {
    // for the profile
    private TextInputEditText name_p, email_p, isChef, fevorit_recpie_p, website_p, ranking_p;
    private ImageView image_profile, likes_pages, uploads_pages, IV_choose_pic;
    private String Uid;

    private StorageReference storageReference;
    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;

    private String email, nickname;
    private Uri uriImage;


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

        storageReference = FirebaseStorage.getInstance().getReference("Displaypics");
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        // init the text view
        name_p = findViewById(R.id.full_name_pp);
        ranking_p = findViewById(R.id.ranking_p);
        isChef = findViewById(R.id.is_chaf_pp);
        website_p = findViewById(R.id.wabsite_pp);
        email_p = findViewById(R.id.email_pp);
        fevorit_recpie_p = findViewById(R.id.favorit_recipe_pp);
        IV_choose_pic = findViewById(R.id.IV_choose_pic);

        // init the buttons
        uploads_pages = findViewById(R.id.imageViewuploeds);
        likes_pages = findViewById(R.id.imageViewLikes);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        image_profile = findViewById(R.id.CIV_pic);

        assert user != null;
        this.Uid = user.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(Uid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot_users) {
                if (snapshot_users.exists()) {
                    User user = snapshot_users.getValue(User.class);
                    name_p.setText(nickname);
                    email_p.setText(email);
                    ranking_p.setText(user.getEating());
                    fevorit_recpie_p.setText(user.getFavorite());
                    website_p.setText(user.getWebsite());
                    isChef.setText(user.isChef() + "");
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
                startActivity(new Intent(profile_page.this, likedRecipes.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        uploads_pages.setOnClickListener(view -> {
            try {
                startActivity(new Intent(profile_page.this, my_uploads.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        IV_choose_pic.setOnClickListener(view -> {
            try {
                ImagePicker.Companion.with(profile_page.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
//                            .cropSquare()
                        .cropOval()
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            } catch (Exception ignored) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uriImage = data.getData();
        image_profile.setImageURI(uriImage);
        uploadPicToStorage();

    }

    public void uploadPicToStorage() {
        if (uriImage != null) {
            StorageReference fileReference = storageReference.child(mAuth.getCurrentUser().getUid()
                    + "." + getFileExtension(uriImage));

            //upload the image
            fileReference.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            firebaseUser = mAuth.getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().
                                    setPhotoUri(uri).build();
                            firebaseUser.updateProfile(profileUpdates);
                            Toast.makeText(profile_page.this, "Upload successful!", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });
        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }


//    private void UpdateProfilePic(Uri imageUri) {
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (firebaseUser != null) {
//            if (imageUri != null) {
//                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                        .setPhotoUri(imageUri)
//                        .build();
//                firebaseUser.updateProfile(profileUpdates)
//                        .addOnCompleteListener(task -> {
//                            if (task.isSuccessful()) {
//                                Log.d("ProfileUpdate", "User profile updated.");
//                            } else {
//                                Log.e("ProfileUpdate", "Error updating user profile.", task.getException());
//                            }
//                        });
//            }
//        } else {
//            Log.d("ProfileUpdate", "No user is currently authenticated.");
//        }
//    }

}



