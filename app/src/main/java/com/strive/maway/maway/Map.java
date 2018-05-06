package com.strive.maway.maway;


import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Location;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;



/* A simple {@link Fragment} subclass.
 */
public class Map extends Fragment implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LocationListener {
    GoogleMap mGoogleMap;
    MapView mMapView;
    private GoogleApiClient client;
    View mView;
    LinearLayout hospital;
    LinearLayout doctor;

    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 60* 1000 *5;  /*  5 d9aye9  */
    private long FASTEST_INTERVAL = 60* 1000 *5;  //2 seconds


    private static final float DEFAULT_ZOOM = 15f;
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final String FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private static String COARSE_LOCATION = android.Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private Boolean mLocationPermissionGranted = false;
    LocationCallback locationCallback;
    int PROXIMITY_RADIUS = 20000;
    double latitude,longitude;




    public Map() {
        // Required empty public constructor
    }


    private void getLocationPermission(){

        Log.d(TAG, "getLocationPermission: getting Location permissions");
        String[] permissions ={Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(this.getActivity().getApplicationContext(),FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getActivity().getApplicationContext(),COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                mLocationPermissionGranted = true;
                if(client==null){
                    buildGoogleApiClient();
                }
                getDeviceLocation();

            }
            else{
                ActivityCompat.requestPermissions(this.getActivity(),permissions,LOCATION_PERMISSION_REQUEST_CODE);
            }

        }
        else{
            ActivityCompat.requestPermissions(this.getActivity(),permissions,LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called");
        mLocationPermissionGranted = false;
        switch(requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }

                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionGranted = true;
                    if(client==null){
                        buildGoogleApiClient();
                    }
                    getDeviceLocation();

                    //initialize our map

                }

            }
        }
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        isServicesOK();

        mMapView = (MapView) mView.findViewById(R.id.map);
        getLocationPermission();

        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }



        hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Object dataTransfer[] = new Object[5];
                GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
                Toast.makeText(getContext(), " Hospital clicked", Toast.LENGTH_SHORT).show();
                mGoogleMap.clear();
                String hospital = "hospital";
                String url = getUrl(latitude, longitude, hospital);
                dataTransfer[0] = mGoogleMap;
                dataTransfer[1] = url;
                dataTransfer[2]="Hospital";
                dataTransfer[3]=latitude;
                dataTransfer[4]=longitude;



                getNearbyPlacesData.execute(dataTransfer);
                Toast.makeText(getContext(), "Showing Nearby Hospitals", Toast.LENGTH_SHORT).show();
            }
        });
        doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Doctor clicked", Toast.LENGTH_SHORT).show();

                Object dataTransfer[] = new Object[5];

                GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();

                mGoogleMap.clear();
                String hospital = "hospital";
                String url = getUrl(latitude, longitude, hospital);

                dataTransfer[0] = mGoogleMap;
                dataTransfer[1] = url;
                dataTransfer[2]="Doctor";
                dataTransfer[3]=latitude;
                dataTransfer[4]=longitude;

                getNearbyPlacesData.execute(dataTransfer);

            }
        });
    }


    private String getUrl(double latitude , double longitude , String nearbyPlace)
    {

        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type="+nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+"AIzaSyD0_7Gef4EHp5_yD4hssK_wryzUe0Zie-A");

        return googlePlaceUrl.toString();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_map, container, false);
        hospital = mView.findViewById(R.id.hospitalclick);
        doctor = mView.findViewById(R.id.doctorclick);
        return mView;
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapsInitializer.initialize(getContext());
        mGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        // hna ndirou centralisation nta3 Current location of the phone
        buildGoogleApiClient();
        getDeviceLocation();


        if (ActivityCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        mGoogleMap.setMyLocationEnabled(true);

    }



    //Method From Github to check if the user has the right Version of the services

    public boolean isServicesOK(){

        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getActivity());

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(getContext(), "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
    // change   Camera position Method
    private void moveCamera(LatLng latLng, float zoom){
        Log.d(TAG , "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    //la methode pour la position actuel

    private void getDeviceLocation(){


        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        try{

            if( mLocationPermissionGranted){
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();
                            if(currentLocation !=null)
                            {moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM);
                            }else
                            {
                                Toast.makeText(getContext(), "Enable your Gps", Toast.LENGTH_SHORT).show();

                            }

                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(getContext(), "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                }); }

        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }
    // Trigger new location updates at interval

    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        SettingsClient settingsClient = LocationServices.getSettingsClient(getActivity());
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if((ContextCompat.checkSelfPermission(this.getActivity().getApplicationContext(),FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
            &&(ContextCompat.checkSelfPermission(this.getActivity().getApplicationContext(),COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED))
        {
            LocationServices.getFusedLocationProviderClient(getContext()).requestLocationUpdates(mLocationRequest,createLocationCallBack() ,
                    Looper.myLooper());
        }
    }
    private LocationCallback createLocationCallBack(){

      locationCallback=  new LocationCallback() {
          @Override
          public void onLocationResult(LocationResult locationResult) {
              // do work here
              onLocationChanged(locationResult.getLastLocation());
          }
      };
      return locationCallback;
    }


    @Override
    public void onLocationChanged(Location location) {


        // New location has now been determined
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());

        Log.d(TAG, "onLocationChanged:"+msg);
        // You can now create a LatLng Object for use with maps
        latitude=location.getLatitude();
        longitude = location.getLongitude();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,DEFAULT_ZOOM));

    }

    @Override
    public void onResume() {
        super.onResume();
        if (client != null && mFusedLocationProviderClient!= null) {
            startLocationUpdates();
        } else {
            buildGoogleApiClient();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        client = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
       client.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(getActivity(), "Connection suspended", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getActivity(), "Connection failed", Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onPause() {
        super.onPause();
        if (mFusedLocationProviderClient != null) {
            mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
    }
}