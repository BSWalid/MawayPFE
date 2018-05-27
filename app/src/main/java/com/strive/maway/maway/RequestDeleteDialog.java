package com.strive.maway.maway;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Winsido on 04/05/2018.
 */

public class RequestDeleteDialog extends AppCompatDialogFragment {

    private EditText justificationEditText;
    Firebase requestRef;
    double latitude,longitude;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        latitude = getArguments().getDouble("latitude");
        longitude= getArguments().getDouble("longitude");

        Firebase.setAndroidContext(getContext());

        requestRef = new Firebase("https://maway-1520842395181.firebaseio.com/DeleteRequests");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.deletedialog,null);
        builder.setView(view)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("REPORT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        String justification = justificationEditText.getText().toString();

                        Firebase key = requestRef.child(mUid).push();
                        key.child("latitude").setValue(latitude);
                        key.child("longitude").setValue(longitude);
                        key.child("justification").setValue(justification);
                      ;
                    }
                });
        justificationEditText = view.findViewById(R.id.justification);


        return builder.create();
    }



}
