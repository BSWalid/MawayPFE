package com.strive.maway.maway;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements View.OnClickListener {
    Button loginbtn;
    TextView sigupbtn;
    EditText email;
    ProgressBar progressBar;
    EditText password;
    FirebaseAuth mAuth;


    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initializing mAuth, from this auth we can call signIn method

        mAuth = FirebaseAuth.getInstance();
        password = (EditText) findViewById(R.id.passwordEditText);
        email = (EditText) findViewById(R.id.emailEditText);
        progressBar = (ProgressBar) findViewById(R.id.signInProgressBar);

        //setting on click listeners

        findViewById(R.id.sigupbtn).setOnClickListener(this);
        findViewById(R.id.login).setOnClickListener(this);


      /* Our old methode
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Home.class);
                startActivity(intent);
            }
        });*/


    }
    private void userLogin(){

        final String emailText = email.getText().toString().trim();
        final String passwordText = password.getText().toString().trim();


        //check if email is not empty

        if (emailText.isEmpty()){
            email.setError("Email is required");
            email.requestFocus(); //request the focus to the email
            return; //it stops the execution
        }

        //check if email is valid

        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
            email.setError("Please enter a valid email");
            email.requestFocus(); //request the focus to the email
            return; //it stops the execution
        }
        //check if password is empty

        if(passwordText.isEmpty()){
            password.setError("Password is required");
            password.requestFocus(); //request the focus to the password
            return; //it stops the execution
        }

        //check if password has a valid length

        if(passwordText.length()<6) {
            password.setError("Minimum length of password should be six");
            password.requestFocus(); //request the focus to the password
            return; //it stops the execution
        }
           //set progressbar visible
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(emailText,passwordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                 Intent intent = new Intent(Login.this,Home.class);
                 intent.putExtra("password",passwordText);
                 intent.putExtra("email",emailText);
                 startActivity(intent);
                 finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void Login(){



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sigupbtn:

                startActivity(new Intent(this, SignUp.class));
                break;
            case R.id.login :
//                LogUser();
                userLogin();
               // startActivity(new Intent(this, Home.class));
                break;
        }
    }
}