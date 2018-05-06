package com.strive.maway.maway.AccountSettings;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.strive.maway.maway.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditAccount extends Fragment {
    String email;
    String password;
    View mView;
    EditText emailFiled,name,passwordCheck,passwordfield,usernameField;
    Button btnSubmit;

    private FirebaseAuth mAuth;
    private Firebase mRef;


    public EditAccount() {
        // Required empty public constructor

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emailFiled.setText(email);
        SetUsername();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String check = passwordCheck.getText().toString();
                if(!check.equals(password)){

                    Toast.makeText(getActivity().getApplicationContext(),"Password Doesn't Match", Toast.LENGTH_SHORT).show();
                }else{

                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

// Get auth credentials from the user for re-authentication. The example below shows
// email and password credentials but there are multiple possible providers,
// such as GoogleAuthProvider or FacebookAuthProvider.
                AuthCredential credential = EmailAuthProvider
                        .getCredential(email, password);

                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            //update Password
                            user.updatePassword(passwordfield.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getContext(), "password Done ", Toast.LENGTH_SHORT).show();


                                    }else{

                                        Toast.makeText(getContext(), "failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            //update the email
                            user.updateEmail(emailFiled.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(getContext(), "EmailDone", Toast.LENGTH_SHORT).show();



                                    }else {

                                        Toast.makeText(getContext(), "email Faild", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                        }else {

                            Toast.makeText(getContext(), "Auth faild", Toast.LENGTH_SHORT).show();


                        }
                        updateUsername();
                    }
                });}

// Prompt the user to re-provide their sign-in credentials




            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Firebase.setAndroidContext(getActivity().getApplicationContext());
        mRef = new Firebase("https://maway-1520842395181.firebaseio.com/");
        mView =inflater.inflate(R.layout.fragment_edit_account, container, false);
        emailFiled = mView.findViewById(R.id.editEmail);
        passwordfield = mView.findViewById(R.id.editPassword);
        usernameField = mView.findViewById(R.id.editUsername);
        btnSubmit = mView.findViewById(R.id.submitBtn);
        name = mView.findViewById(R.id.editUsername);
        passwordCheck = mView.findViewById(R.id.passwordConfirmation);


        email = getArguments().getString("email");
        password = getArguments().getString("password");

        return mView;
    }









private void updateUsername(){

    String mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    Firebase mRef2= mRef.child("Users").child(mUid);
    mRef2.child("username").setValue(usernameField.getText().toString());



}

    private void SetUsername(){
        String mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Firebase mRef2= mRef.child("Users").child(mUid);

        mRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for(DataSnapshot innerData : dataSnapshot.getChildren())
                {

                    String key = innerData.getKey();

                    switch (key)
                    {
                        case "username":
                            //code here
                            String username = innerData.getValue(String.class);
                            name.setText(username);

                            break;

                    }


                }
                //name.setText("Welcome "+username);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



    }


}
