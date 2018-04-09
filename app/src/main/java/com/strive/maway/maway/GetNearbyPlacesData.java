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
import java.util.ArrayList;
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

    List<HashMap<String,String>> nearbyPlaces2 ;



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
        nearbyPlaces2=nearbyPlaceList;
        mRef= new Firebase("https://maway-1520842395181.firebaseio.com/Locations");
        Log.d("nearbyplacesdata","called parse method");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<Location> typeList=new ArrayList<>();
                for(DataSnapshot innerData : dataSnapshot.getChildren())
                {
                    String id = mRef.getKey();
                    String type;

                    Log.e("keys-----", innerData.getKey());


                    for ( DataSnapshot innerInnerData : innerData.getChildren()) {

                        String key = innerInnerData.getKey();
                        Log.e("keys*****", key);
                        switch (key)
                        {

                            case "type" :
                                //code
                                //typeOfPlace   = innerInnerData.getValue(String.class);

                                type = innerInnerData.getValue(String.class);
                                Location L  = new Location(innerData.getKey(),type);
                                typeList.add(L);
                        }
                    }





                }
                Log.e("hambouk",typeList.toString());
                showNearbyPlaces(nearbyPlaces2,typeList);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

       /* readData(new FirebaseCallBack() {

            @Override
            public void onCallBack(List<Location> list) {

            }
        });*/


        //showNearbyPlaces(nearbyPlaceList,typeList);
    }

    public GetNearbyPlacesData() {
        super();
    }

    private void showNearbyPlaces(List<HashMap<String, String>> nearbyPlaceList , ArrayList<Location> Locations)
    {


        Log.d("pleasehambouk", Locations.toString());


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

            for (int j=0;j<Locations.size();j++){


                Log.d("pleaseEmchiListLoca", Locations.get(j).getType());
                Log.d("pleaseEmchiListLoca", Locations.get(j).getID());

                Log.d("position", "112");
            }

            //adding locations to Firebase

            String keyLocation = googlePlace.get("place_id");
            Log.d("pathKey", keyLocation);
            final String typeofplace = googlePlace.get("type_of_place");
            Log.d("typeofplace2", typeofplace+"faregh");


            Firebase mRef2= mRef.child(keyLocation);

            // get the type of the  place

          /*  mRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    for(DataSnapshot innerData : dataSnapshot.getChildren())
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

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });*/



            Firebase key = mRef.child(keyLocation);
            if (key==null){
            key.child("place_name").setValue(placeName);
            key.child("vicinity").setValue(vicinity);
            key.child("latitude").setValue(lat);
            key.child("longitude").setValue(lng);
            key.child("type").setValue("Hospital");
            }


            //normally we store here our data


            Log.d("typeofplace", TypeWish);
            Log.d("typeofplace2", typeOfPlace+"faregh");



            if (TypeWish.equals(getType(Locations,keyLocation))){
                mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

            }




        }

    }

    /*
    private void readData(final FirebaseCallBack firebaseCallBack){
        ValueEventListener valueEventListener= new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren()){


                    String Type= ds.child("type").getValue(String.class);
                    String ID =ds.getKey();
                    Location L = new Location(ID,Type);

                    typeList.add(L);


                }
                firebaseCallBack.onCallBack(typeList);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        mRef.addListenerForSingleValueEvent(valueEventListener);


    }
    //create interface First
    public  interface FirebaseCallBack{
        void onCallBack(List<Location> list);

    }*/

    private  String getType(ArrayList<Location> listoftypes,String key){

        for (int i = 0; i<listoftypes.size();i++){

            if (key.equals(listoftypes.get(i).getID())){

                return listoftypes.get(i).getType();
            }
        }

        return "";
    }

}