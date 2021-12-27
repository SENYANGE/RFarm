package com.example.rfarm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //persistent


        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (user != null) {
                    // User is signed in
                    // Start home activity
                    startActivity(new Intent(MainActivity.this,Dahboard.class));

                } else {
                    // No user is signed in
                    // start login activity
                    startActivity(new Intent(MainActivity.this, Home.class));
                }

                // close splash activity
                finish();
            }
        },3000);

    }
}