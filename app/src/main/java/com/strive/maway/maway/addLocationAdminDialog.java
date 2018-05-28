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
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Winsido on 04/05/2018.
 */

public class addLocationAdminDialog extends AppCompatDialogFragment {

    private EditText locationNameEditText, locationVicinityEditText, locationTypeEditText,locationLatEditText,locationLngEditText;
    Firebase requestRef;
    String latitude,longitude,placename,type,vicinity,RequestID;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        latitude = getArguments().getString("latitude");
        longitude = getArguments().getString("longitude");
        placename = getArguments().getString("placename");
         type = getArguments().getString("placetype");

        vicinity=getArguments().getString("vicinity");
        RequestID=getArguments().getString("RequestID");
        Firebase.setAndroidContext(getContext());

        requestRef = new Firebase("https://maway-1520842395181.firebaseio.com/Locations");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.admin_add_dialog,null); //3awed dir synchronize
        builder.setView(view).setTitle("ADD LOCATION")
                .setNegativeButton("REFUSE", new DialogInterface.OnClickListener() {
                    //here u delete it from there
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Firebase mref2= new Firebase("https://maway-1520842395181.firebaseio.com/Requests");
                        mref2.child(RequestID).setValue(null);

                    }
                })
                .setPositiveButton("REQUEST ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Log.e("bla ba", "clicked:");
            Firebase mref= new Firebase("https://maway-1520842395181.firebaseio.com/Locations");
            Firebase mref2= new Firebase("https://maway-1520842395181.firebaseio.com/Requests");
            Firebase key = mref.child(RequestID);
                        key.child("doctorType").setValue("notype");
                        key.child("latitude").setValue(latitude);
                        key.child("longitude").setValue(longitude);
                        key.child("place_name").setValue(placename);
                        key.child("type").setValue(type);
                        key.child("vicinity").setValue(vicinity);
                        key.child("source").setValue("injected");
                        mref2.child(RequestID).removeValue();



                    }
                } );
        locationNameEditText = view.findViewById(R.id.dialog_LocationName);
        locationTypeEditText = view.findViewById(R.id.dialog_LocationType);
        locationVicinityEditText = view.findViewById(R.id.dialog_LocationVicinity);
        locationLatEditText = view.findViewById(R.id.dialog_locationLat);
        locationLngEditText = view.findViewById(R.id.dialog_LocationLng);

        locationNameEditText.setText(placename);
        locationTypeEditText.setText(type);
        locationVicinityEditText.setText(vicinity);
        locationLatEditText.setText(latitude);
        locationLngEditText.setText(longitude);






        return builder.create();
    }



}
