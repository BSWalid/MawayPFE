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

    List<HashMap<String,String>> nearbyPlaces2 ;

   //in objects we obtain our map, url and type that we sent in map activity

    @Override
    protected String doInBackground(Object... objects){
        mMap = (GoogleMap)objects[0];
        url = (String)objects[1];
        TypeWish=(String) objects[2];


        DownloadURL downloadURL = new DownloadURL();
        try {
            googlePlacesData = downloadURL.readUrl(url);
            }
        catch (IOException e) {
            e.printStackTrace();
        }

        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String s){

        List<HashMap<String, String>> nearbyPlaceList;
        //nearbyPlaceList contains google Map locations
        DataParser parser = new DataParser();
        nearbyPlaceList = parser.parse(s);
        nearbyPlaces2=nearbyPlaceList;

                 //initialisation of Firebase reference

        mRef= new Firebase("https://maway-1520842395181.firebaseio.com/Locations");

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<LocationInformations> locationInformationsList = new ArrayList<>();
                ArrayList<Location> typeList=new ArrayList<>();
                for(DataSnapshot innerData : dataSnapshot.getChildren())
                {   String lat,lng, placeName,source, vicinity;
                    String id = mRef.getKey();
                    String type;
                // initialise our LocationInformation that we will add to our list if it's stored only in firebase

                    LocationInformations Linfo = new LocationInformations(innerData.getKey(),"","","","","","");


                    for ( DataSnapshot innerInnerData : innerData.getChildren()) {

                        String key = innerInnerData.getKey();
                        switch (key)
                        {
                            case "type" :

                                type = innerInnerData.getValue(String.class);
                                Location L  = new Location(innerData.getKey(),type);
                                typeList.add(L);
                                Linfo.setType(type);
                        }
                        if(key.equals("latitude")){
                            lat = innerInnerData.getValue(String.class);
                            Linfo.setLatitude(lat);
                        }
                        if(key.equals("longitude")){
                            lng = innerInnerData.getValue(String.class);
                            Linfo.setLongitude(lng);
                        }
                        if(key.equals("place_name")){
                            placeName = innerInnerData.getValue(String.class);
                            Linfo.setPlaceName(placeName);
                        }
                        if(key.equals("vicinity")){
                            vicinity =innerInnerData.getValue(String.class);
                            Linfo.setVicinity(vicinity);
                        }
                        if(key.equals("source")){
                            source=innerInnerData.getValue(String.class);
                            Linfo.setSource(source);
                        }
                    }
                     // If that location is injected then add to our list
                   if(Linfo.getSource().equals("injected")){
                       locationInformationsList.add(Linfo);
                   }
                }

                showNearbyPlaces(nearbyPlaces2,typeList,locationInformationsList);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    public GetNearbyPlacesData() {
        super();
    }

    private void showNearbyPlaces(List<HashMap<String, String>> nearbyPlaceList , ArrayList<Location> Locations, ArrayList<LocationInformations> locationInformationsList)
    {

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


            //adding locations to Firebase

            String keyLocation = googlePlace.get("place_id");

            Firebase key = mRef.child(keyLocation);
            if (key==null){
                key.child("place_name").setValue(placeName);
                key.child("vicinity").setValue(vicinity);
                key.child("latitude").setValue(lat);
                key.child("longitude").setValue(lng);
                key.child("source").setValue("google");
                key.child("type").setValue("Hospital");
            }
            //before displaying locations i  need to get a list containing distance of each
            //location from my current location

            /*
            Object dataTransfer[] = new Object[3];
            String urlDirection = getDirectionsUrl(currentlat,currentlng,lat,lng);
            dataTransfer[0] =mMap;
            dataTransfer[1]= urlDirection;
            dataTransfer[2] = new LatLng(lat,lng);
            getDirectionsData.execute(dataTransfer); (it should return me a hashmap containing duration & distance)
            //AND MAYBE after execute we do

            HashMap<String,String> durationAndDistance = new HashMap<>();
            durationAndDistance = getDirectionsData.getDurationAndDistance;
             //then we store it in a list containing latlng and hashmap
             listDurationAndDistanceOfLocations.add(new LatLng(lat,lng),durationAndDistance);

             //of course here it will be a customized type, then in the end we call a methode which
             //finds the minimum of distance and show direction of that particular place
             */


            //display locations from google

            if (TypeWish.equals(getType(Locations,keyLocation))){
                mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

            }
        }

        for(int k=0;k<locationInformationsList.size();k++){

            if (TypeWish.equals(getType(Locations,locationInformationsList.get(k).getKey()))){
                double latitude = Double.parseDouble(locationInformationsList.get(k).getLatitude());
                double longitude = Double.parseDouble(locationInformationsList.get(k).getLongitude());
                 LatLng latLng = new LatLng(latitude,longitude);
                String placeName = locationInformationsList.get(k).getPlaceName();
                String vicinity = locationInformationsList.get(k).getVicinity();

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(placeName + " : "+ vicinity);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
            }
        }



    }


    private  String getType(ArrayList<Location> listOfTypes,String key){

        for (int i = 0; i<listOfTypes.size();i++){

            if (key.equals(listOfTypes.get(i).getID())){

                return listOfTypes.get(i).getType();
            }
        }

        return "";
    }

    private String getDirectionsUrl(String currentLat, String currentLng ,String latLocation,String lngLocation){

        StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionsUrl.append("origin="+currentLat+","+currentLng);
        googleDirectionsUrl.append("&destination="+latLocation+","+lngLocation);
        googleDirectionsUrl.append("&key="+"AIzaSyB68rL3FyWgpVIPpVCc0h_wIOIsUXayBRQ");

        return googleDirectionsUrl.toString();

    }


}