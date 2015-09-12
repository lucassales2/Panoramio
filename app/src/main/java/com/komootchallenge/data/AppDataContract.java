package com.komootchallenge.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Lucas on 12/09/2015.
 */
public class AppDataContract {

    public static final String CONTENT_AUTHORITY = "com.komootchallenge";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_LOCATION = "pathLocation";

    public static class AppLocationEntry implements BaseColumns {
        public static final String TABLE_NAME = "AppLocations";
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_LOCATION).build();
        public static final String TIME = "time";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
    }
}
