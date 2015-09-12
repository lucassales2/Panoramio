package com.komootchallenge;

import android.Manifest;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.komootchallenge.data.AppDataContract;

public class LocationService extends Service {
    public LocationManager locationManager;
    public InsertOnDBLocationListener listener;
    public Location previousBestLocation = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new InsertOnDBLocationListener();
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, listener);
        getContentResolver().delete(AppDataContract.AppLocationEntry.CONTENT_URI, null, null);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        // handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
        locationManager.removeUpdates(listener);
    }

    public class InsertOnDBLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(final Location loc) {
            if (previousBestLocation == null || previousBestLocation.distanceTo(loc) >= 100) {
                ContentValues values = new ContentValues();
                values.put(AppDataContract.AppLocationEntry.LATITUDE, String.valueOf(loc.getLatitude()));
                values.put(AppDataContract.AppLocationEntry.LONGITUDE, String.valueOf(loc.getLongitude()));
                values.put(AppDataContract.AppLocationEntry.TIME, String.valueOf(System.currentTimeMillis()));
                getContentResolver().insert(AppDataContract.AppLocationEntry.CONTENT_URI ,values);
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

    }
}