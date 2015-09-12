package com.komootchallenge.datamodel;

/**
 * Created by Lucas on 12/09/2015.
 */
public class Panoramio {
    public int getCount() {
        return count;
    }

    public boolean isHas_more() {
        return has_more;
    }

    public MapLocation getMap_location() {
        return map_location;
    }

    public Photo[] getPhotos() {
        return photos;
    }

    private int count;
    private boolean has_more;
    private MapLocation map_location;
    private Photo[] photos;

}
