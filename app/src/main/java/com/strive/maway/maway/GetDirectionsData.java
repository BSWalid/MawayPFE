package com.strive.maway.maway;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by Winsido on 12/04/2018.
 */



    public class GetDirectionsData extends AsyncTask<Object,String,String> {

        GoogleMap mMap;
        private String url;
        String googleDirectionsData;
        protected String duration;
        protected String distance;
        LatLng latLng;
        public AsyncResponse listOfDistance = null;



    public interface AsyncResponse {
        void processFinish(HashMap<String,String> directionsList);
    }


    public GetDirectionsData(AsyncResponse delegate){
        listOfDistance = delegate;
    }
        @Override
        protected String doInBackground(Object... objects) {

            mMap = (GoogleMap) objects[0];
            url = (String) objects[1];
            latLng  = (LatLng) objects[2]; //contains end_lat and end_lng
            DownloadURL downloadURL = new DownloadURL();

            try {
                googleDirectionsData = downloadURL.readUrl(url);
            }
            catch (IOException e) {
                e.printStackTrace();
            }


            return googleDirectionsData;
        }
        //this interface will make us able to return directionsList


    //s represents jsonData
        @Override
        protected void onPostExecute(String s) {

            //we need hashmap :

            HashMap<String,String> directionsList = null;
            DataParser parser = new DataParser();
            directionsList = parser.parseDirections(s);
            listOfDistance.processFinish(directionsList);


            //i need somewhat to return this in getNearbyPlacesData
        }


}





