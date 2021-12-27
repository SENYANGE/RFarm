package com.example.rfarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Home extends AppCompatActivity {

    private EditText SignInMail, SignInPass;
    private FirebaseAuth auth;
    private Button SignInButton;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //persistent

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        // set the view now
        setContentView(R.layout.activity_home);
        SignInMail = (EditText) findViewById(R.id.editTextTextEmailAddress);
        SignInPass = (EditText) findViewById(R.id.editTextTextPassword);
        SignInButton = (Button) findViewById(R.id.login_btn);
        progressDialog= new ProgressDialog(this);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        SignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String email = SignInMail.getText().toString();
                final String password = SignInPass.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Enter your mail address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Enter your password", Toast.LENGTH_SHORT).show();
                    return;
                }
                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Home.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 8) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(),"Password must be more than 8 digit",Toast.LENGTH_SHORT).show();

                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Intent intent = new Intent(Home.this, Dahboard.class);
                                    startActivity(intent);
                                    progressDialog.dismiss();
                                    finish();
                                }
                            }
                        });
            }
        });
    }

    public void NavigateSignUp(View v) {
        Intent inent = new Intent(this, Registration.class);
        startActivity(inent);
    }
    public void NavigateForgetMyPassword(View v) {
        Intent inent = new Intent(this, ResetPasswordActivity.class);
        startActivity(inent);
    }
}