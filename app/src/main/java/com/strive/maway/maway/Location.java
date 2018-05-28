package com.strive.maway.maway;

/**
 * Created by willy on 05/04/2018.
 */

public class Location {

   private String ID;
   private String Type;


    public String getID() {
        return ID;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getType() {
        return Type;
    }

    public void setID(String ID) {
        this.ID = ID;

    }

    public Location(String ID, String type) {
        this.ID = ID;

        Type = type;

    }
}
