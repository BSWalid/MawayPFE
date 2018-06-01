package com.strive.maway.maway.AccountSettings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.strive.maway.maway.R;

/**
 * Created by Winsido on 31/05/2018.
 */

public class DeleteAccountDialog  extends AppCompatDialogFragment {

    private View view;
    private String email, password, mUid;
    private Firebase mRef;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        email = getArguments().getString("email");
        password = getArguments().getString("password");
        mRef = new Firebase("https://maway-1520842395181.firebaseio.com/");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.delete_account_dialog,null);
        builder.setView(view)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

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
                });

        return builder.create();
    }



}

