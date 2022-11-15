package com.example.smarter_foodies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;

public class ApplyChef extends AppCompatActivity {

    TextInputEditText etRegEmail;
    TextInputEditText etRegPassword;
    TextInputEditText etResume;
    TextView etBackLogin;
    Button btnApply;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_chef);

        etRegEmail = findViewById(R.id.etRegEmail);
        etRegPassword = findViewById(R.id.etRegPass);
        etResume = findViewById(R.id.etResume);
        etBackLogin = findViewById(R.id.etBackLogin);
        btnApply = findViewById(R.id.btnApply);

        mAuth = FirebaseAuth.getInstance();

        btnApply.setOnClickListener(view ->{
            createApplication();
        });

        etBackLogin.setOnClickListener(view ->{
            startActivity(new Intent(ApplyChef.this,LoginActivity.class));
        });
    }

    private boolean containsWordsArray(String inputString, String[] words) {
        List<String> inputStringList = Arrays.asList(inputString.split(" "));
        List<String> wordsList = Arrays.asList(words);

        return inputStringList.containsAll(wordsList);
    }
    private void createApplication(){
        String email = etRegEmail.getText().toString();
        String password = etRegPassword.getText().toString();
        String resume = etResume.getText().toString();

        if (TextUtils.isEmpty(email)){
            etRegEmail.setError("Email cannot be empty");
            etRegEmail.requestFocus();
        }else if (TextUtils.isEmpty(password)){
            etRegPassword.setError("Password cannot be empty");
            etRegPassword.requestFocus();
        }else if (TextUtils.isEmpty(resume)){
            etResume.setError("Resume cannot be empty");
            etResume.requestFocus();
        }else{
            String[] keyWords = {"Education", "Skills", "Chef", "kitchen", "food", "passion"};
            if(containsWordsArray(resume, keyWords)) {
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ApplyChef.this, "User registered successfully as chef..", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ApplyChef.this, LoginActivity.class));
                    } else {
                        Toast.makeText(ApplyChef.this, "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                Toast.makeText(ApplyChef.this, "Please try again in the future.. for now you can register regularly :)", Toast.LENGTH_LONG).show();
                startActivity(new Intent(ApplyChef.this, LoginActivity.class));
            }
        }
    }

}
