package com.strive.maway.maway.AccountSettings;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.strive.maway.maway.Login;
import com.strive.maway.maway.R;

/**
 * Created by Winsido on 31/05/2018.
 */

public class DeleteAccount extends android.support.v4.app.Fragment{

    private String email,emailInserted;
    private String password , passwordInserted;
    private View mView;
    private EditText passwordField, emailField;
    Button btnDelete;
    private String mUid;
    private Firebase mRef;

    public DeleteAccount() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_delet_account, container, false);
        emailField = mView.findViewById(R.id.deleteEmail);
        passwordField = mView.findViewById(R.id.deletePassword);
        btnDelete = mView.findViewById(R.id.deleteBtn);
        email = getArguments().getString("email");
        password = getArguments().getString("password");
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        mRef = new Firebase("https://maway-1520842395181.firebaseio.com/");
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emailInserted = emailField.getText().toString();
                passwordInserted = passwordField.getText().toString();
                if(!emailInserted.equals(email)|| ! passwordInserted.equals(password)){

                    Toast.makeText(getContext(), "email can't be deleted", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(), "email can be deleted", Toast.LENGTH_SHORT).show();

                    //delete account

                   final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(email, password);

                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            //if reauthentication is successful
                            if(task.isSuccessful()){

                                mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            //delete all informations related to user

                                            mRef.child("Users").child(mUid).removeValue();

                                            //start login activity

                                           Intent intent = new Intent(getActivity(),Login.class);
                                           startActivity(intent);
                                           getActivity().finish();

                                        }
                                    }
                                });
                            }
                            else{

                            }

                        }
                    });
                }

            }
        });
    }
    public void openDialog(){

        Bundle args = new Bundle();
        args.putString("email",email);
        args.putString("password",password);
        DeleteAccountDialog deleteAccountDialog = new DeleteAccountDialog();
        deleteAccountDialog.setArguments(args);
        deleteAccountDialog.show(getActivity().getSupportFragmentManager(),"deleteAccountDialog");
    }
}
