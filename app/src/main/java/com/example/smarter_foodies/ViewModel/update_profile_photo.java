package com.example.smarter_foodies.ViewModel;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Toast;

import com.example.smarter_foodies.R;
import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

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
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(intent, PICK_IMAGE_REQUEST);
                try {
                    ImagePicker.Companion.with(update_profile_photo.this)
                            .crop()                    //Crop image(Optional), Check Customization for more option
//                            .cropSquare()
                            .cropOval()
                            .compress(1024)            //Final image size will be less than 1 MB(Optional)
                            .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                            .start();
                } catch (Exception ignored) {

                }

            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uplodpic();
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closepic();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        try {
            super.onActivityResult(requestCode, resultCode, data);
            uriImage = data.getData();
            profileimageview.setImageURI(uriImage);
//            if (data != null) {
//                Uri imgUri = data.getData();
//                for (ImageView iv : imageViews) {
//                    if (iv.getDrawable() == null) {
//                        if (imgUri != null) {
//                            iv.setImageURI(imgUri);
//                            String path = uriImage.getPath();
//                            // Convert image to base64-encoded string
//                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                            Bitmap bitmap = BitmapFactory.decodeFile(path);
//                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                            byte[] imageData = baos.toByteArray();
//                            String imageDataBase64 = Base64.encodeToString(imageData, Base64.DEFAULT);
//                            uploadedImages.add(imageDataBase64);
//                            break;
//                        }
//                    }
//                }
//                if (Size + 1 == 4) {
//                    Toast.makeText(getApplicationContext(), "You have reached the limit of image uploads", Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                Toast.makeText(getApplicationContext(), "You exceeded limit of images", Toast.LENGTH_SHORT).show();
            }
//        }
//    } catch(
//    Exception ignored)
//
//    {
//
//    }

//}

//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null &&
//                data.getData() != null) {
//            uriImage = data.getData();
//            profileimageview.setImageURI(uriImage);
//        }
//    }

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
    public void closepic() {

                    Intent intent = new Intent(update_profile_photo.this, profile_page.class);
                    startActivity(intent);
                    finish();


            }





    public String getfileextension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }


}