package com.example.smarter_foodies;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.concurrent.atomic.AtomicMarkableReference;

import de.hdodenhof.circleimageview.CircleImageView;


public class update_profile_photo extends AppCompatActivity {

    private CircleImageView profileimageview;
    private Button closeButton, saveButton, profileCangebtn;
    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    private FirebaseUser Firebaseuser;
    private static int PICK_IMAGE_REQUEST = 1;
    private Uri uriImage;


//    private DatabaseReference databaseReference;
//
//    private Uri imageUri;
//    private String myUri =  "";
//    private StorageTask uploadtask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile_photo);

        // init
        mAuth = FirebaseAuth.getInstance();
        Firebaseuser = mAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("Displaypics");
        Uri uri = Firebaseuser.getPhotoUrl();
//        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
//        Storageprofilepiccsref = FirebaseStorage.getInstance().getReference().child("Profile Pic");

        profileimageview = findViewById(R.id.profile_image);
        closeButton = findViewById(R.id.btnClose);
        saveButton = findViewById(R.id.btnSave);
        profileCangebtn = findViewById(R.id.change_prpfile_btn);
        Picasso.get().load(uri).into(profileimageview);


        profileCangebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);

            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uplodpic();
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null &&
                data.getData() != null) {
            uriImage = data.getData();
            profileimageview.setImageURI(uriImage);
        }
    }

    public void Uplodpic() {
        if (uriImage != null) {
            StorageReference filerefernce = storageReference.child(mAuth.getCurrentUser().getUid()
                    + "." + getfileextension(uriImage));

            //upload the image
            filerefernce.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filerefernce.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloaduri = uri;
                            Firebaseuser = mAuth.getCurrentUser();

                            UserProfileChangeRequest profileypdates = new UserProfileChangeRequest.Builder().
                                    setPhotoUri(downloaduri).build();
                            Firebaseuser.updateProfile(profileypdates);
                        }
                    });
                    Toast.makeText(update_profile_photo.this, "Upload successful!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(update_profile_photo.this, profile_page.class);
                    startActivity(intent);
                    finish();

                }
            });

        }

    }

    public String getfileextension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }


}