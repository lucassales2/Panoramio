package com.komootchallenge.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by Lucas on 12/09/2015.
 */
public class Provider extends ContentProvider {

    private static final int LOCATION = 100;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private OpenHelper mOpenHelper;

    private static UriMatcher buildUriMatcher() {
        final String authority = AppDataContract.CONTENT_AUTHORITY;
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(authority, AppDataContract.PATH_LOCATION, LOCATION);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = OpenHelper.getInstance(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase sqLiteDatabase = mOpenHelper.openDatabase();
        switch (sUriMatcher.match(uri)) {
            case LOCATION:
                Cursor cursor = sqLiteDatabase.query(AppDataContract.AppLocationEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null,
                        null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
        }
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase sqLiteDatabase = mOpenHelper.openDatabase();
        switch (sUriMatcher.match(uri)) {
            case LOCATION:
                long _id = sqLiteDatabase.insertWithOnConflict(AppDataContract.AppLocationEntry.TABLE_NAME, null,
                        contentValues,
                        SQLiteDatabase.CONFLICT_REPLACE);
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase = mOpenHelper.openDatabase();
        int rowsDeleted = 0;
        switch (sUriMatcher.match(uri)) {
            case LOCATION: {
                // Deletes cascading
                rowsDeleted = sqLiteDatabase.delete(
                        AppDataContract.AppLocationEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            }
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
