package com.komootchallenge;

import android.app.ActivityManager;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.GsonBuilder;
import com.komootchallenge.data.AppDataContract;
import com.komootchallenge.datamodel.Panoramio;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, Response.Listener<String>, Response.ErrorListener {

    private static RequestQueue queue;
    private double maxX = 0;
    private double maxY = 0;
    private double minX = 0;
    private double minY = 0;
    private int prevStart = 0;

    private PhotoAdapter adapter;
    private Intent service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        service = new Intent(this, LocationService.class);
        getLoaderManager().initLoader(R.id.loader, null, this);
        ListView listView = (ListView) findViewById(R.id.listview);
        adapter = new PhotoAdapter(this);
        listView.setAdapter(adapter);
    }

    private boolean isLocationServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (LocationService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_start) {
            if (isLocationServiceRunning()) {
                stopService(service);
                item.setIcon(android.R.drawable.ic_media_play);
            } else {
                adapter.clear();
                startService(service);
                minY = 0;
                minX = 0;
                maxY = 0;
                maxX = 0;
                item.setIcon(android.R.drawable.ic_media_pause);

            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                AppDataContract.AppLocationEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor.moveToFirst()) {
            boolean hasToUpdate = false;
            do {
                double longitude = Double.parseDouble(cursor.getString(cursor.getColumnIndex(AppDataContract.AppLocationEntry.LONGITUDE)));
                double latitude = Double.parseDouble(cursor.getString(cursor.getColumnIndex(AppDataContract.AppLocationEntry.LATITUDE)));
                if (maxX == 0 || maxX < longitude) {
                    maxX = longitude;
                    hasToUpdate = true;
                }

                if (maxY == 0 || maxY < latitude) {
                    maxY = latitude;
                    hasToUpdate = true;
                }

                if (minX == 0 || minX >= longitude) {
                    minX = longitude;
                    hasToUpdate = true;
                }

                if (minY == 0 || minY >= latitude) {
                    minY = latitude;
                    hasToUpdate = true;
                }

            } while (cursor.moveToNext());
            if (hasToUpdate)
                getFromPanoramio();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void getFromPanoramio() {
        if (queue == null) {
            queue = Volley.newRequestQueue(this);
        } else {
            queue.cancelAll(new RequestQueue.RequestFilter() {
                @Override
                public boolean apply(Request<?> request) {
                    return true;
                }
            });
        }
        prevStart = 0;
        String url = String.format("http://www.panoramio.com/map/get_panoramas.php?set=public&from=%d&to=%d&minx=%s&miny=%s&maxx=%s&maxy=%s&size=medium&mapfilter=true",
                prevStart, prevStart + 100,
                String.valueOf(minX), String.valueOf(minY), String.valueOf(maxX), String.valueOf(maxY));
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, this, this);
        queue.add(stringRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(String response) {
        GsonBuilder gson = new GsonBuilder();
        Panoramio panoramio = gson.create().fromJson(response, Panoramio.class);
        if (panoramio.isHas_more()) {
            prevStart += 100;
            String url = String.format("http://www.panoramio.com/map/get_panoramas.php?set=public&from=%d&to=%d&minx=%s&miny=%s&maxx=%s&maxy=%s&size=medium&mapfilter=true",
                    prevStart, prevStart + 100,
                    String.valueOf(minX), String.valueOf(minY), String.valueOf(maxX), String.valueOf(maxY));
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, this, this);
            queue.add(stringRequest);
        }
        adapter.addAll(panoramio.getPhotos());
    }
}
