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

public class RequestAddDialog extends AppCompatDialogFragment {

    private EditText locationNameEditText, locationVicinityEditText, locationTypeEditText;
    Firebase requestRef;
    double latitude,longitude;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        latitude = getArguments().getDouble("latitude");
        longitude= getArguments().getDouble("longitude");

        Firebase.setAndroidContext(getContext());

        requestRef = new Firebase("https://maway-1520842395181.firebaseio.com/Requests");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_location_dialog,null);
        builder.setView(view).setTitle("ADD LOCATION")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("REQUEST ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        String locationName = locationNameEditText.getText().toString();
                        String locationType = locationTypeEditText.getText().toString();
                        String locationVicinity = locationVicinityEditText.getText().toString();
                        Firebase key = requestRef.push();

                        key.child("latitude").setValue(latitude);
                        key.child("longitude").setValue(longitude);
                        key.child("placeName").setValue(locationName);
                        key.child("type").setValue(locationType);
                        key.child("vicinity").setValue(locationVicinity);
                        key.child("RequestSender").setValue(mUid);
                        key.child("requestID").setValue(key.getKey());
    }
});
        locationNameEditText = view.findViewById(R.id.dialog_LocationName);
        locationTypeEditText = view.findViewById(R.id.dialog_LocationType);
        locationVicinityEditText = view.findViewById(R.id.dialog_LocationVicinity);


        return builder.create();
    }



}
