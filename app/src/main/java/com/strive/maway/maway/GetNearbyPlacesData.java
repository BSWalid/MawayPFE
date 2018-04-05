package com.strive.maway.maway;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by willy on 04/04/2018.
 */

public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

private String googlePlacesData;
private GoogleMap mMap;
        String url;
        private Firebase mRef;
        String TypeWish;
        String typeOfPlace="";


@Override
protected String doInBackground(Object... objects){
        mMap = (GoogleMap)objects[0];
        url = (String)objects[1];
        TypeWish=(String) objects[2];


        DownloadURL downloadURL = new DownloadURL();
        try {
        googlePlacesData = downloadURL.readUrl(url);
        } catch (IOException e) {
        e.printStackTrace();
        }

        return googlePlacesData;
        }

@Override
protected void onPostExecute(String s){

        List<HashMap<String, String>> nearbyPlaceList;
        DataParser parser = new DataParser();
        nearbyPlaceList = parser.parse(s);
        Log.d("nearbyplacesdata","called parse method");
        showNearbyPlaces(nearbyPlaceList);
        }

        public GetNearbyPlacesData() {
                super();
        }

        private void showNearbyPlaces(List<HashMap<String, String>> nearbyPlaceList)
        {

                mRef= new Firebase("https://maway-1520842395181.firebaseio.com/Locations");



        for(int i = 0; i < nearbyPlaceList.size(); i++)
        {
        MarkerOptions markerOptions = new MarkerOptions();
        HashMap<String, String> googlePlace = nearbyPlaceList.get(i);

        String placeName = googlePlace.get("place_name");
        String vicinity = googlePlace.get("vicinity");
        double lat = Double.parseDouble( googlePlace.get("lat"));
        double lng = Double.parseDouble( googlePlace.get("lng"));

        LatLng latLng = new LatLng( lat, lng);
        markerOptions.position(latLng);
        markerOptions.title(placeName + " : "+ vicinity);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));


        // Firebase information about the place



                //adding locations to Firebase

                String keyLocation = googlePlace.get("place_id");
                Log.d("pathKey", keyLocation);
                final String typeofplace = googlePlace.get("type_of_place");
                Log.d("typeofplace2", typeofplace+"faregh");


                Firebase mRef2= mRef.child(keyLocation);

                // get the type of the  place

                mRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                                setthelocation(dataSnapshot);

                                for(DataSnapshot innerData : dataSnapshot.getChildren())
                                {
                                        String key = innerData.getKey();

                                        switch (key)
                                        {
                                                case "type":
                                                        //code here
                                                        typeOfPlace   = innerData.getValue(String.class);
                                                        //Log.d("hedatype", typeOfPlace);
                                                        break;
                                        }

                                }

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                });



                Firebase key = mRef.child(keyLocation);
                key.child("place_name").setValue(placeName);
                key.child("vicinity").setValue(vicinity);
                key.child("latitude").setValue(lat);
                key.child("longitude").setValue(lng);

                //normally we store here our data


                Log.d("typeofplace", TypeWish);
                Log.d("typeofplace2", typeOfPlace+"faregh");






                mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

        }

        }
        public void setthelocation(DataSnapshot s){
                for(DataSnapshot innerData : s.getChildren())
                {
                        String key = innerData.getKey();

                        switch (key)
                        {
                                case "type":
                                        //code here
                                        typeOfPlace   = innerData.getValue(String.class);
                                        Log.d("hedatype", typeOfPlace);
                                        break;
                        }

                }



        }
        }

