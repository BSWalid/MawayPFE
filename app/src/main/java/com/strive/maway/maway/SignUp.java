package com.strive.maway.maway;


import android.content.Intent;
import android.nfc.Tag;
import android.os.PatternMatcher;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    Button btnsignup ;
    EditText username;
    EditText password;
    EditText email;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private Firebase mRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Firebase.setAndroidContext(this);
        mRef = new Firebase("https://maway-1520842395181.firebaseio.com/Users");


        username =(EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.email);
        progressBar = (ProgressBar) findViewById(R.id.signInProgressBar);
        //instantiate auth
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.btnsignup).setOnClickListener(this);


       /* btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailText = email.getText().toString();
                String passwordText = password.getText().toString();
                String usernameText = username.getText().toString();
                 Firebase key = mRef.push();
                 key.child("email").setValue(emailText);
                 key.child("password").setValue(passwordText);
                 key.child("username").setValue(usernameText);
            }
        }); */







    }
 private void registerUser(){

        final String usernameText = username.getText().toString().trim();
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
     //the complete Listener detects the completion, it will take a complete listener interface object
     mAuth.createUserWithEmailAndPassword(emailText,passwordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
         @Override
         //when the registration is completed the method onComplete() is called
         //here we can check if it was successful or not, using the task object
         public void onComplete(@NonNull Task<AuthResult> task) {
             //set progressbar invisible
             progressBar.setVisibility(View.GONE);

             if(task.isSuccessful()){
                 Toast.makeText(getApplicationContext(),"You have been successfully registered",Toast.LENGTH_SHORT).show();

                 login(emailText,passwordText,usernameText);


             }
             else{
                 if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                     Toast.makeText(getApplicationContext(),"Email already used",Toast.LENGTH_SHORT).show();
                 }
                 else {
                     Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();                }

             }
         }
     });

 }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case (R.id.btnsignup):
                  registerUser();
                break;
        }

    }

    public void login(String email, String password , final String username){

     mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                     String id = mAuth.getCurrentUser().getUid();
                     Firebase key = mRef.child(id);
                     key.child("username").setValue(username);

                    Intent intent = new Intent(SignUp.this,Home.class);
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

}