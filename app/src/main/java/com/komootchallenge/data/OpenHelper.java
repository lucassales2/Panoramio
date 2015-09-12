package com.komootchallenge.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Lucas on 12/09/2015.
 */
public class OpenHelper extends SQLiteOpenHelper {

    private Context mContext;
    private AtomicInteger mOpenCounter = new AtomicInteger();
    private SQLiteDatabase mDatabase;
    private static OpenHelper instance;

    private OpenHelper(Context context) {
        super(context, "komoot", null,
                1);
        this.mContext = context;
    }

    public static OpenHelper getInstance(Context context) {
        if (instance == null) {
            instance = new OpenHelper(context);
        }
        return instance;
    }

    public synchronized SQLiteDatabase openDatabase() {
        if (mOpenCounter.incrementAndGet() == 1) {
            // Opening new database
            mDatabase = getWritableDatabase();
        }
        return mDatabase;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createLocationTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void createLocationTable(SQLiteDatabase db) {
        db.execSQL(String.format(
                "CREATE TABLE %s (" +
                        "%s INTEGER PRIMARY KEY," +
                        "%s TEXT," +
                        "%s TEXT," +
                        "%s TEXT);",
                AppDataContract.AppLocationEntry.TABLE_NAME,
                AppDataContract.AppLocationEntry._ID,
                AppDataContract.AppLocationEntry.TIME,
                AppDataContract.AppLocationEntry.LATITUDE,
                AppDataContract.AppLocationEntry.LONGITUDE));
    }
}
