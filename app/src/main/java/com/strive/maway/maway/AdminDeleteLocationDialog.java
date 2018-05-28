package com.strive.maway.maway;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Winsido on 04/05/2018.
 */

public class AdminDeleteLocationDialog extends AppCompatDialogFragment {

    private EditText locationNameEditText, locationVicinityEditText, locationTypeEditText,locationLatEditText,locationLngEditText;
    Firebase requestRef;
    private TextView justification;
    String latitude,longitude,placename,placeid,vicinity,RequestID,justificationText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        latitude = getArguments().getString("latitude");
        longitude = getArguments().getString("longitude");
        placename = getArguments().getString("placename");
        placeid = getArguments().getString("placeID");
        justificationText= getArguments().getString("Justification");

        vicinity=getArguments().getString("vicinity");
        RequestID=getArguments().getString("RequestID");
        Firebase.setAndroidContext(getContext());

        requestRef = new Firebase("https://maway-1520842395181.firebaseio.com/Locations");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.admin_delete_dialog,null); //3awed dir synchronize
        builder.setView(view).setTitle("DELETE LOCATION")
                .setNegativeButton("REFUSE", new DialogInterface.OnClickListener() {
                    //here u delete it from there
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Firebase mref2= new Firebase("https://maway-1520842395181.firebaseio.com/DeleteRequests");
                        mref2.child(RequestID).setValue(null);

                    }
                })
                .setPositiveButton("Accept Report", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        Firebase mref= new Firebase("https://maway-1520842395181.firebaseio.com/Locations");
                        Firebase mref2= new Firebase("https://maway-1520842395181.firebaseio.com/DeleteRequests");
                        Firebase key = mref.child(placeid);

                        key.child("type").setValue("Deleted");


                        mref2.child(RequestID).setValue(null);



                    }
                } );
        justification = view.findViewById(R.id.deletejustification);
        justification.setText(justificationText);








        return builder.create();
    }



}
