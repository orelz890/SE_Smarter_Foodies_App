package com.example.smarter_foodies.ViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.smarter_foodies.DashboardActivity;
import com.example.smarter_foodies.Model.User;
import com.example.smarter_foodies.R;
import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
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


public class profile_page extends DashboardActivity {
    // for the profile
    private TextInputEditText name_p, email_p;
    private ImageView image_profile, IV_choose_pic;
    private String Uid;

    private StorageReference storageReference;
    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;

    private String email, nickname;
    private Uri uriImage;
    protected int tabsLayoutWidth, tabsLayoutHeight;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        View activityMainView = LayoutInflater.from(this).inflate(R.layout.activity_profile_page, rootLayout, false);
        rootLayout.addView(activityMainView);
        setContentView(rootLayout);
        allocateActivityTitle("Profile");

        LinearLayout layout = findViewById(R.id.linearLayout);
        ViewGroup.LayoutParams layoutParams = layout.getLayoutParams();
        this.tabsLayoutHeight = layoutParams.height;
        this.tabsLayoutWidth = layoutParams.width;

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
        email_p = findViewById(R.id.email_pp);
        IV_choose_pic = findViewById(R.id.IV_choose_pic);


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
                }
                // set the image profile
                Uri uri = firebaseUser.getPhotoUrl();
                Picasso.get().load(uri).into(image_profile);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

        setTabsAndViewPager();


    }

    public int getTabsLayoutWidth(){
        return this.tabsLayoutWidth;
    }

    public int getTabsLayoutHeight(){
        return this.tabsLayoutHeight;
    }

    private void setTabsAndViewPager() {
        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        FragmentManager supportFragmentManager = getSupportFragmentManager();
        // Create an adapter to supply the ViewPager with the Fragments
        PagerAdapter pagerAdapter = new PagerAdapter(supportFragmentManager);
        viewPager.setAdapter(pagerAdapter);

        // Connect the ViewPager to the TabLayout
        tabLayout.setupWithViewPager(viewPager);

    }


    // PagerAdapter class with titles
    private static class PagerAdapter extends FragmentStatePagerAdapter {

        private final String[] tabTitles = {"Likes", "Uploads", "Cart"};

        PagerAdapter(FragmentManager fm) {

            super(fm, BEHAVIOR_SET_USER_VISIBLE_HINT);
//            super.
        }



        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new likedFragment();
                case 1:
                    return new myRecipesFragment();
                case 2:
                    return new cartFragment();

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Return the title for each tab
            return tabTitles[position];
        }

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


}



