package com.example.smarter_foodies;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.smarter_foodies.databinding.ActivityDashboardBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class DashboardActivity extends DrawerBaseActivity {

    ActivityDashboardBinding activityDashboardBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDashboardBinding = ActivityDashboardBinding.inflate(getLayoutInflater());

        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fUser != null) {
            String uid = fUser.getUid();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        User user1 = dataSnapshot.getValue(User.class);
                        if (user1 != null) {
                            setUserContentView(activityDashboardBinding.getRoot(), user1.isChef());
//                            setUserContentView(activityDashboardBinding.getRoot(), true);
                        }
                        else{
                            setUserContentView(activityDashboardBinding.getRoot(), false);
                        }
                    }else{
                        Toast.makeText(DashboardActivity.this, "Please sign in first", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(DashboardActivity.this, LoginGoogle.class));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("TAG", error.getMessage());
                }
            });
            allocateActivityTitle("Dashboard");

        }
    }
}
