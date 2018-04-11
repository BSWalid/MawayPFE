package com.strive.maway.maway;

/**
 * Created by Winsido on 10/04/2018.
 */

public class LocationInformations {
    private String key;
    private String latitude;
    private String longitude;
    private String placeName;
    private String type;
    private String source;
    private String vicinity;


    public LocationInformations(String key,String latitude, String longitude,String placeName,String type, String source,String vicinity) {

    this.key = key;
    this.latitude = latitude;
    this.longitude = longitude;
    this.placeName = placeName;
    this.type = type;
    this.source = source;
    this.vicinity = vicinity;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }
}
