package com.komootchallenge.datamodel;

/**
 * Created by Lucas on 12/09/2015.
 */
public class Photo {
    private double latitude;
    private double longitude;
    private long owner_id;
    private String owner_name;
    private String owner_url;
    private String photo_file_url;
    private long photo_id;
    private String photo_title;
    private String photo_url;
    private String upload_date;
    private int width;
    private int height;

    public int getHeight() {
        return height;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public long getOwner_id() {
        return owner_id;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public String getOwner_url() {
        return owner_url;
    }

    public String getPhoto_file_url() {
        return photo_file_url;
    }

    public long getPhoto_id() {
        return photo_id;
    }

    public String getPhoto_title() {
        return photo_title;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public String getUpload_date() {
        return upload_date;
    }

    public int getWidth() {
        return width;
    }
}
