package com.strive.maway.maway;

/**
 * Created by Winsido on 13/04/2018.
 */

public class LocationDistance {
    private double latitude;
    private double longitude;
    private float distance;
    private String placeName;
    private String vicinity;
    private  String placeID;

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    private String[] Path;


    public LocationDistance(double lat,double lng,float d,String pName,String v) {
        latitude = lat;
        longitude =lng;
        distance = d;
        placeName = pName;
        vicinity =v;
    }

    public String[] getPath() {
        return Path;
    }

    public void setPath(String[] path) {
        Path = path;
    }


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }
}
