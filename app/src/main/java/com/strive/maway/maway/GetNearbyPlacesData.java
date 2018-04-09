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
        boolean initialized = false;
        String TypeWish;
        String typeOfPlace="";
        ArrayList<Location> typeList=new ArrayList<>(); ;


        @Override
        protected String doInBackground(Object... objects){
                Log.d("doInBackground", "doInBackground: called");
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

                mRef= new Firebase("https://maway-1520842395181.firebaseio.com/Locations");
                Log.d("onPostExecute","onPostExecute : called");


                      /* readData(new FirebaseCallBack() {


                               @Override
                               public void onCallBack(ArrayList<Location> list) {
                                       Log.d("onCallBack", "onCallBack: called");
                                       fillList(list);

                               }
                       });*/
                Log.d("onPostExecute", "onPostExecute: before calling showNearbyPlaces");
                showNearbyPlaces(nearbyPlaceList);
        }

        public GetNearbyPlacesData() {
                super();
        }

        private void showNearbyPlaces(List<HashMap<String, String>> nearbyPlaceList) {

                Log.d("showNearbyPlaces", "showNearbyPlaces: called");
                for (int i = 0; i < nearbyPlaceList.size(); i++) {
                        MarkerOptions markerOptions = new MarkerOptions();
                        HashMap<String, String> googlePlace = nearbyPlaceList.get(i);

                        String placeName = googlePlace.get("place_name");
                        String vicinity = googlePlace.get("vicinity");
                        double lat = Double.parseDouble(googlePlace.get("lat"));
                        double lng = Double.parseDouble(googlePlace.get("lng"));

                        LatLng latLng = new LatLng(lat, lng);
                        markerOptions.position(latLng);
                        markerOptions.title(placeName + " : " + vicinity);
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));


                        // Firebase information about the place

                      /*  for(int k =0;k<typeList.size();k++){
                                Log.d("insideNearbyPlace", typeList.get(k).getID()+" "+typeList.get(k).getType());
                        }*/
                        //adding locations to Firebase

                        String keyLocation = googlePlace.get("place_id");
                        Log.d("pathKey", keyLocation);
                        final String typeofplace = googlePlace.get("type_of_place");
                        Log.d("typeofplace2", typeofplace + "faregh");


                        Firebase mRef2 = mRef.child(keyLocation);

                        // get the type of the  place

                        Firebase key = mRef.child(keyLocation);
                        if (key == null) {
                                key.child("place_name").setValue(placeName);
                                key.child("vicinity").setValue(vicinity);
                                key.child("latitude").setValue(lat);
                                key.child("longitude").setValue(lng);
                                key.child("type").setValue("Hospital");
                                //normally we store here our data

                        }
                                Log.d("typeofplace", TypeWish);
                                Log.d("typeofplace2", typeOfPlace + "faregh");


                                mMap.addMarker(markerOptions);
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));



                }
        }
        /*private void readData(final FirebaseCallBack firebaseCallBack){
                ValueEventListener valueEventListener= new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds:dataSnapshot.getChildren()){


                                        String Type= ds.child("type").getValue(String.class);
                                        String ID =ds.getKey();
                                        Location L = new Location(ID,Type);

                                        typeList.add(L);

                                }
                                Log.d("OnDataChange", "onDataChange: ");
                                firebaseCallBack.onCallBack(typeList);
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                };
                mRef.addListenerForSingleValueEvent(valueEventListener);*


        }
      /*  //create interface First
        public  interface FirebaseCallBack{
                void onCallBack(ArrayList<Location> list);

        }

        public void fillList(ArrayList<Location> list){
                Log.d("fillList", "fillList: begining");
                System.out.println("LIST SIZE IS "+list.size());
                for(int i=0;i<list.size();i++){
                        Log.d("fillList", "fillList: insideLoop");
                        String id =  list.get(i).getID();
                        String type = list.get(i).getType();
                        Location l = new Location(id,type);
                        typeList.add(l);

                }
                Log.d("fillList", "list filled");
        }*/

}

