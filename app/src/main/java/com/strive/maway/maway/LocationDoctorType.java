package com.strive.maway.maway;

/**
 * Created by Winsido on 08/05/2018.
 */

public class LocationDoctorType {
    private String keyLoc;
    private String typeLoc;

    public LocationDoctorType(String id, String type) {
        keyLoc = id;
        typeLoc = type;

    }

    public String getKeyLoc() {
        return keyLoc;
    }

    public void setTypeLoc(String type) {
        typeLoc = type;
    }

    public String getTypeLLoc() {
        return typeLoc;
    }

    public void setKeyLoc(String id) { keyLoc = id; }

}
