package com.example.smarter_foodies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smarter_foodies.databinding.ActivityApplyChefBinding;
import com.example.smarter_foodies.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import javax.mail.MessagingException;

public class ApplyChef extends AppCompatActivity {

    //ActivityApplyChefBinding binding;
    TextInputEditText etName;
    TextInputEditText etResume;
    TextView etSkip;
    Button btnApply;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_chef);
//        binding = ActivityApplyChefBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());

        etName = findViewById(R.id.etName);
        etResume = findViewById(R.id.etResume);
        etSkip = findViewById(R.id.etSkip);
        btnApply = findViewById(R.id.btnApply);


        mAuth = FirebaseAuth.getInstance();
        btnApply.setOnClickListener(view -> {
            try {
                createApplication();
            } catch (MessagingException | IOException | GeneralSecurityException e) {
                e.printStackTrace();
            }
        });

        etSkip.setOnClickListener(view -> {
            String name = etName.getText().toString().trim();
            if (name.isEmpty()) {
                etName.setError("Nickname cannot be empty");
                etName.requestFocus();
                return;
            }
            FirebaseUser firebaseUser = mAuth.getCurrentUser();
            if (firebaseUser != null) {
                insertUser(0,name);
            }
//            User user = new User(name);
//            FirebaseUser firebaseUser = mAuth.getCurrentUser();
//            if (firebaseUser != null) {
//                if (user.firstEntry) {
//                    user.firstEntry = false;
//                    FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if(task.isSuccessful()){
//                                Toast.makeText(ApplyChef.this, "User Skip Application.", Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(ApplyChef.this, MainActivity.class));
//                            }else{
//                                Toast.makeText(ApplyChef.this, "Error adding the user data", Toast.LENGTH_LONG).show();
//                                startActivity(new Intent(ApplyChef.this, LoginGoogle.class));
//                            }
//
//                        }
//                    });
//
//                }
//            } else {
//                Toast.makeText(ApplyChef.this, "Sign in Error: ", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(ApplyChef.this, LoginGoogle.class));
//            }
//            startActivity(new Intent(ApplyChef.this, MainActivity.class));
        });
    }

    private boolean containsWordsArray(String inputString, String[] words) {
        List<String> inputStringList = Arrays.asList(inputString.split(" "));
        List<String> wordsList = Arrays.asList(words);
//        String[] keyWords = {"Education", "Skills", "Chef", "kitchen", "food", "passion"};

        return inputStringList.containsAll(wordsList);
    }

    private void createApplication() throws MessagingException, IOException, GeneralSecurityException {
        String name = etName.getText().toString().trim();
        String resume = etResume.getText().toString().trim();

        if (name.isEmpty()) {
            etName.setError("Nickname cannot be empty");
            etName.requestFocus();
            return;
        } else if (resume.isEmpty()) {
            etResume.setError("Resume cannot be empty");
            etResume.requestFocus();
            return;
        }
        User user = new User(name);
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            sendEmail("smarterfoodies@gmail.com","Application as Chef", resume);

//            if (user.firstEntry) {
//                user.firstEntry = false;
//                FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if(task.isSuccessful()){
//                            Toast.makeText(ApplyChef.this, "User applied successfully as chef..", Toast.LENGTH_LONG).show();
//                            startActivity(new Intent(ApplyChef.this, MainActivity.class));
//                        }else{
//                            Toast.makeText(ApplyChef.this, "Error adding the user data", Toast.LENGTH_LONG).show();
//                            startActivity(new Intent(ApplyChef.this, LoginGoogle.class));
//                        }
//
//                    }
//                });
//
//            }
        } else {
            Toast.makeText(ApplyChef.this, "Sign in Error: ", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ApplyChef.this, LoginGoogle.class));
        }
    }


    public void sendEmail(String toEmailAddress,String subject,String body){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        //intent.setData(Uri.parse("mailto:"));
        String[] addresses = toEmailAddress.split(",");
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent,1);
        } else {
            Toast.makeText(ApplyChef.this, "There is no application that support this action",
                    Toast.LENGTH_SHORT).show();
        }
//        String name = etName.getText().toString().trim();
//        insertUser(1,name);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String name = etName.getText().toString().trim();
        insertUser(1,name);
    }

    private void insertUser(int Case, String name){
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        User user = new User(name);
        if (user.firstEntry) {
            user.firstEntry = false;
            FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        if(Case == 1) {
                            Toast.makeText(ApplyChef.this, "User applied successfully as chef..", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(ApplyChef.this, MainActivity.class));
                        }
                        else{
                                Toast.makeText(ApplyChef.this, "User Skip Application.", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(ApplyChef.this, MainActivity.class));
                            }
                    }else{
                        Toast.makeText(ApplyChef.this, "Error adding the user data", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(ApplyChef.this, LoginGoogle.class));
                    }

                }
            });

        }

    }
}


