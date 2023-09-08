package com.example.smarter_foodies.ViewModel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smarter_foodies.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText etLoginEmail;
    TextInputEditText etLoginPassword;
    TextView tvRegisterHere;
    TextView tvRegisterAsChef;
    Button btnLogin;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etLoginEmail = findViewById(R.id.etLoginEmail);
        etLoginPassword = findViewById(R.id.etLoginPass);
        tvRegisterHere = findViewById(R.id.tvRegisterHere);
        tvRegisterAsChef = findViewById(R.id.tvRegisterAsChef);
        btnLogin = findViewById(R.id.btnLogin);

        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(view -> {
            loginUser();
        });
        tvRegisterHere.setOnClickListener(view ->{
            startActivity(new Intent(LoginActivity.this, LoginGoogle.class));
        });
        tvRegisterAsChef.setOnClickListener(view ->{
            startActivity(new Intent(LoginActivity.this, ApplyChef.class));
        });
    }

    private void loginUser(){
        String email = etLoginEmail.getText().toString();
        String password = etLoginPassword.getText().toString();

        if (TextUtils.isEmpty(email)){
            etLoginEmail.setError("Email cannot be empty");
            etLoginEmail.requestFocus();
        }else if (TextUtils.isEmpty(password)){
            etLoginPassword.setError("Password cannot be empty");
            etLoginPassword.requestFocus();
        }else{
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "User logged in successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }else{
                    Toast.makeText(LoginActivity.this, "Log in Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}