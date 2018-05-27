package com.strive.maway.maway;

import android.graphics.Color;
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
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private String typeWish;
    private String docWish;
    private String doctorType;
    private String typeOfPlace;
    ArrayList<LocationDistance> listNearbyplaces;
    ArrayList<LocationDistance> listLocationDistance;

    private List<HashMap<String,String>> nearbyPlaces2 ;
    private String placeName;
    private String vicinity;
    private double lat,currentLat;
    private double lng, currentLng;
    private Firebase key;
    int n;
    HashMap<String,String> durationAndDistance = new HashMap<>();



    @Override
    protected String doInBackground(Object... objects){
        mMap = (GoogleMap)objects[0];
        url = (String)objects[1];
        typeWish=(String) objects[2];
        currentLat = (double) objects[3];
        currentLng = (double) objects [4];
        doctorType =(String) objects[5];


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

        listNearbyplaces = new ArrayList<>();
        listLocationDistance = new ArrayList<>();

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
                ArrayList<LocationDoctorType> typeDoctorList=new ArrayList<>();



                for(DataSnapshot innerData : dataSnapshot.getChildren())
                {   String lat,lng, placeName,source, vicinity;
                    String id = mRef.getKey();
                    String type,docType;
                    Log.e("keys-----", innerData.getKey());

                    // initialise our LocationInformation that we will add to our list if it's stored only in firebase

                    LocationInformations Linfo = new LocationInformations(innerData.getKey(),"","","","","","","");


                    for ( DataSnapshot innerInnerData : innerData.getChildren()) {

                        String key = innerInnerData.getKey();
                        Log.e("keys*****", key);


                        switch (key)
                        {
                            case "doctorType" :{
                                docType = innerInnerData.getValue(String.class);
                                Linfo.setDoctorType(docType);
                                LocationDoctorType lD = new LocationDoctorType(innerData.getKey(),docType);
                                typeDoctorList.add(lD);
                                break;

                            }
                            case "type" :

                                type = innerInnerData.getValue(String.class);
                                Location L  = new Location(innerData.getKey(),type);
                                typeList.add(L);
                                Linfo.setType(type);
                                break;
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
                showNearbyPlaces(nearbyPlaces2,typeList,typeDoctorList,locationInformationsList);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }

    public GetNearbyPlacesData() {
        super();
    }

    private void showNearbyPlaces(List<HashMap<String, String>> nearbyPlaceList , ArrayList<Location> Locations,ArrayList<LocationDoctorType> typeDoctorList,ArrayList<LocationInformations> locationInformationsList) {

        for (int i = 0; i < nearbyPlaceList.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = nearbyPlaceList.get(i);

            placeName = googlePlace.get("place_name");
            vicinity = googlePlace.get("vicinity");
            lat = Double.parseDouble(googlePlace.get("lat"));
            lng = Double.parseDouble(googlePlace.get("lng"));

            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName + " : " + vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));


            // Firebase information about the place

            //adding locations to Firebase

            String keyLocation = googlePlace.get("place_id");

            key = mRef.child(keyLocation);
            typeOfPlace = getType(Locations, keyLocation);
            docWish = getDoctorType(typeDoctorList,keyLocation);

            //normally we store here our data

            if (typeOfPlace == null) {

                key.child("place_name").setValue(placeName);
                key.child("vicinity").setValue(vicinity);
                key.child("latitude").setValue(lat);
                key.child("longitude").setValue(lng);
                key.child("type").setValue("Hospital");
                key.child("source").setValue("Google");
                key.child("doctorType").setValue("notype");

            }

            //end of storing


            Log.d("typeofplace", typeWish);
            Log.d("typeofplace2", typeOfPlace + "faregh");


            if (typeWish.equals(typeOfPlace)&& doctorType.equals(docWish)) {
                //get distance between two points
                float results[] = new float[10];
                android.location.Location.distanceBetween(currentLat, currentLng, lat, lng, results);
                float distance = results[0] / 1000;

                LocationDistance locationDistance = new LocationDistance(lat, lng, distance, placeName, vicinity);
                listNearbyplaces.add(locationDistance);
            }

        }
        for (int j = 0; j < Locations.size(); j++) {


            Log.d("pleaseEmchiListDoctor", Locations.get(j).getType());
            Log.d("pleaseEmchiListDoctor", Locations.get(j).getID());
        }

        for (int k = 0; k < locationInformationsList.size(); k++) {

            if (typeWish.equals(locationInformationsList.get(k).getType())&& doctorType.equals(locationInformationsList.get(k).getDoctorType())) {


                double latitude = Double.parseDouble(locationInformationsList.get(k).getLatitude());
                double longitude = Double.parseDouble(locationInformationsList.get(k).getLongitude());
                LatLng latLng = new LatLng(latitude, longitude);
                String placeName = locationInformationsList.get(k).getPlaceName();
                String vicinity = locationInformationsList.get(k).getVicinity();

                //get distance between two points
                float results[] = new float[10];
                android.location.Location.distanceBetween(currentLat, currentLng, latitude, longitude, results);
                float distance = results[0] / 1000;
                if (distance < 40) {
                    LocationDistance locationDistance = new LocationDistance(latitude, longitude, distance, placeName, vicinity);
                    listNearbyplaces.add(locationDistance);
                }

            }
        }          //sorting our list

        Collections.sort(listNearbyplaces, new Comparator<LocationDistance>() {
            @Override
            public int compare(LocationDistance ld1, LocationDistance ld2) {

                return Float.valueOf((ld1.getDistance())).compareTo(Float.valueOf(ld2.getDistance()));
            }
        });
        //TEST
        for (LocationDistance d : listNearbyplaces) {
            Log.e("sorted? ", "" + d.getDistance() + " " + d.getPlaceName());
            //sorted correctly here

        }
        //calculate distance of first 15 locations
        caculateDistance(listNearbyplaces);
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

        Log.e("ulrofDirection",googleDirectionsUrl.toString() );

        return googleDirectionsUrl.toString();


    }

    public void caculateDistance(ArrayList<LocationDistance> list){
        int i =0;
        n = list.size();
        for (final LocationDistance locationDistance : list) {
            if(i<15){
                //request Distance

                Object dataTransfer[] = new Object[3];
                String urlDirection = getDirectionsUrl(currentLat,currentLng, locationDistance.getLatitude(),locationDistance.getLongitude());
                dataTransfer[0] =mMap;
                dataTransfer[1]= urlDirection;
                dataTransfer[2] = new LatLng(locationDistance.getLatitude(),locationDistance.getLongitude());

                GetDirectionsData getDirectionsData = new GetDirectionsData(new GetDirectionsData.AsyncResponse() {
                    @Override
                    public void processFinish(HashMap<String, String> directionsList,String [] paths) {

                        durationAndDistance = directionsList;
                        String[] distanceParts = directionsList.get("distance").split(" ");


                        float distance = Float.parseFloat(distanceParts[0]);
                        LocationDistance l = new LocationDistance(locationDistance.getLatitude(),locationDistance.getLongitude(),distance,locationDistance.getPlaceName(),locationDistance.getVicinity());
                        l.setPath(paths);
                        addToList(l,n);
                    }
                });
                getDirectionsData.execute(dataTransfer);

                i = i+1;
            }
            else{
                break;
            }
        }

    }

    public void addToList(LocationDistance l,int n){
        LocationDistance nearestLocation;
        Log.e("location added", "addToList: "+l.getDistance()+" "+l.getPlaceName() );
        listLocationDistance.add(l);
        if((listLocationDistance.size()==15)|| (listLocationDistance.size()==n)){
            // sort my list and get the nearest location and then call a methode to display all my locations
            Collections.sort(listLocationDistance, new Comparator<LocationDistance>() {
                @Override
                public int compare(LocationDistance d1, LocationDistance d2) {
                    return Float.valueOf(d1.getDistance()).compareTo(Float.valueOf(d2.getDistance()));
                }
            });
            //TEST
            for (LocationDistance m : listLocationDistance) {

                Log.e("SORTED REAL DISTANCE", "SORTED REAL DISTANCE"+m.getDistance()+" "+m.getPlaceName() );
            }

            nearestLocation = listLocationDistance.get(0);
            displayLocations(listNearbyplaces,nearestLocation);



        }

    }
    public void displayDirection(String[] directionsList)
    {

        int count = directionsList.length;

        for(int i = 0;i<count;i++)
        {
            PolylineOptions options = new PolylineOptions();
            options.color(Color.rgb(102, 179, 255));
            options.width(10);
            Log.d("m3amarplz",directionsList[i]);
            options.addAll(PolyUtil.decode(directionsList[i]));

            mMap.addPolyline(options);
        }
    }

    public void displayLocations( ArrayList<LocationDistance> listPlaces, LocationDistance nearestLocation){

        if( listLocationDistance.isEmpty()){
            Log.e("mysortedlistisEMPTY D:", "SORTED EMPTY D: " );
        }
        else{
            Log.e("SORTED LIST", "NOT EMPTY :D" );
        }



        for (LocationDistance l : listPlaces) {
            String placeName = l.getPlaceName();
            String vicinity = l.getVicinity();
            double lat = l.getLatitude();
            double lng = l.getLongitude();
            LatLng latLng = new LatLng(lat,lng);

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(placeName + " : "+ vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).alpha(0.7f);
            mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

        }
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(currentLat,currentLng));
        markerOptions.title("Your position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).alpha(0.7f);
        mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng( new LatLng(nearestLocation.getLatitude(),nearestLocation.getLongitude())));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15f));
        displayDirection(nearestLocation.getPath());
    }
    private  String getDoctorType(ArrayList<LocationDoctorType> listOfDoctorTypes,String key){

        for (int i = 0; i<listOfDoctorTypes.size();i++){

            if (key.equals(listOfDoctorTypes.get(i).getKeyLoc())){

                return listOfDoctorTypes.get(i).getTypeLLoc();
            }
        }

        return null;
    }

}