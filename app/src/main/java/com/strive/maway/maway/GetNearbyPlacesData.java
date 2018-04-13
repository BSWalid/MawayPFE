package com.strive.maway.maway;

import android.os.AsyncTask;
import android.util.Log;

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
    private String url;
    private Firebase mRef;
    private String TypeWish;
    private String typeOfPlace;

    private List<HashMap<String,String>> nearbyPlaces2 ;
    private String placeName;
    private String vicinity;
    private double lat,currentLat;
    private double lng, currentLng;
    private Firebase key;
    HashMap<String,String> durationAndDistance = new HashMap<>();



    @Override
    protected String doInBackground(Object... objects){
        mMap = (GoogleMap)objects[0];
        url = (String)objects[1];
        TypeWish=(String) objects[2];
        currentLat = (double) objects[3];
        currentLng = (double) objects [4];


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
                //we need a list where we will store our locations that we injected
                ArrayList<LocationInformations> locationInformationsList = new ArrayList<>();

                ArrayList<Location> typeList=new ArrayList<>();

                for(DataSnapshot innerData : dataSnapshot.getChildren())
                {   String lat,lng, placeName,source, vicinity;
                    String id = mRef.getKey();
                    String type;
                    Log.e("keys-----", innerData.getKey());

                    // initialise our LocationInformation that we will add to our list if it's stored only in firebase

                    LocationInformations Linfo = new LocationInformations(innerData.getKey(),"","","","","","");


                    for ( DataSnapshot innerInnerData : innerData.getChildren()) {

                        String key = innerInnerData.getKey();
                        Log.e("keys*****", key);


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
                if(nearbyPlaces2.isEmpty()){
                    Log.e("empty", "list is empty ");
                }
                else{
                    Log.d("notempty", "list not empty");
                }
                Log.d("typeList",typeList.toString());
                Log.d("locationInformations",locationInformationsList.toString());
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

    private void showNearbyPlaces(List<HashMap<String, String>> nearbyPlaceList , ArrayList<Location> Locations,ArrayList<LocationInformations> locationInformationsList)
    {


        Log.d("pleasehambouk", Locations.toString());


        for(int i = 0; i < nearbyPlaceList.size(); i++)
        {
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = nearbyPlaceList.get(i);

            placeName = googlePlace.get("place_name");
            vicinity = googlePlace.get("vicinity");
            lat = Double.parseDouble( googlePlace.get("lat"));
            lng = Double.parseDouble( googlePlace.get("lng"));

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



            key = mRef.child(keyLocation);
            typeOfPlace = getType(Locations,keyLocation);

            if (typeOfPlace==null) {

            key.child("place_name").setValue(placeName);
            key.child("vicinity").setValue(vicinity);
            key.child("latitude").setValue(lat);
            key.child("longitude").setValue(lng);
            key.child("type").setValue("Hospital");
            key.child("source").setValue("Google");

            }


            //normally we store here our data


            Log.d("typeofplace", TypeWish);
            Log.d("typeofplace2", typeOfPlace+"faregh");



            if (TypeWish.equals(typeOfPlace)){
                mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));



                Object dataTransfer[] = new Object[3];
                String urlDirection = getDirectionsUrl(currentLat,currentLng,lat,lng);
                dataTransfer[0] =mMap;
                dataTransfer[1]= urlDirection;
                dataTransfer[2] = new LatLng(lat,lng);
                GetDirectionsData getDirectionsData = new GetDirectionsData(new GetDirectionsData.AsyncResponse() {
                    @Override
                    public void processFinish(HashMap<String, String> directionsList) {
                        durationAndDistance = directionsList;
                        Log.d("duration and distance", "processFinish: "+directionsList.get("distance"));

                        //it didn't work
                    }
                });
                getDirectionsData.execute(dataTransfer);



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


    private  String getType(ArrayList<Location> listoftypes,String key){

        for (int i = 0; i<listoftypes.size();i++){

            if (key.equals(listoftypes.get(i).getID())){

                return listoftypes.get(i).getType();
            }
        }

        return null;
    }

    private String getDirectionsUrl(double currentLat, double currentLng ,double latLocation,double lngLocation){

        StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionsUrl.append("origin="+currentLat+","+currentLng);
        googleDirectionsUrl.append("&destination="+latLocation+","+lngLocation);
        googleDirectionsUrl.append("&key="+"AIzaSyB68rL3FyWgpVIPpVCc0h_wIOIsUXayBRQ");

        return googleDirectionsUrl.toString();


    }



}