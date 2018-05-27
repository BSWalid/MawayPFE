package com.strive.maway.maway;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Winsido on 08/05/2018.
 */

public class ResetPasswordDialog extends AppCompatDialogFragment {

    private EditText emailText;
    View view;
    private FirebaseAuth firebaseAuth;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        firebaseAuth = FirebaseAuth.getInstance();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.reset_password_dialog,null);
        builder.setView(view)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("RESET", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String email = emailText.getText().toString().trim();
                        if(email.equals("")){
                           /* Snackbar.make(view, "Email is invalid", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();*/
                            //Toast.makeText(getContext(), "Email shouldn't be empty", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                // Toast.makeText(getContext(),"Email entered invalid",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            //
                                        }



                                        else{
                                            //   Toast.makeText(getContext(),"We couldn't send you an email to the email you entered, please retry",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }


                    }
                });
        emailText = view.findViewById(R.id.email_reset_password);

        return builder.create();
    }



}

