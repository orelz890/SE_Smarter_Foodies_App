package com.example.smarter_foodies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class fragment_profile extends Fragment {
    private FirebaseAuth mAuth;

    ImageView image_profile,options;
    TextView posts,followers,following,fullname,bio,username;
    Button update_profile;
    FirebaseUser firebaseUser;
    String profileid;
    ImageButton my_fotos,saved_fotos;

    public View onCreatView(LayoutInflater inflater, ViewGroup continer, Bundle savedinstancestate){
    View view = inflater.inflate(R.layout.activity_fragment_profile,continer,false);
    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
    profileid =prefs.getString("profileid","none");
    image_profile = view.findViewById(R.id.image_profile);

    posts = view.findViewById(R.id.posts);
    followers = view.findViewById(R.id.followers);
    following = view.findViewById(R.id.following);
    fullname = view.findViewById(R.id.full_name_pp);

    username = view.findViewById(R.id.username);
    update_profile = view.findViewById(R.id.update_profile);
    my_fotos = view.findViewById(R.id.my_fotos);
    saved_fotos = view.findViewById(R.id.save_fotos);



    update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser fuser = mAuth.getCurrentUser();
                if (fuser != null) {
                    String uid = fuser.getUid();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
                    String name  =  reference.child("Nickname").toString();
                    String email = reference.child("Email").toString();
                    String isChef = reference.child("isChef").toString();
                    String type_food = reference.child("Type_food").toString();
                    String favorite = reference.child("favorite").toString();



                }

            }
        });{





        }

        return view;
    }
}
