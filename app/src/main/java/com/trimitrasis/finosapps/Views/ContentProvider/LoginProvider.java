package com.trimitrasis.finosapps.Views.ContentProvider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.HashMap;

/**
 * Created by rahman on 9/19/2017.
 */

public class LoginProvider extends ContentProvider {

    static final String PROVIDER_NAME = "com.trimitrasis.finosapps.Views.ContentProvider.LoginProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/login";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    public static final String KEY_ID        = "_id";
    public static final String KEY_USERNAME  = "username";

    static final int LOGIN     = 1;
    static final int LOGIN_ID  = 2;
    static final UriMatcher uriMatcher;

    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "login", LOGIN);
        uriMatcher.addURI(PROVIDER_NAME, "login/#", LOGIN_ID);
    }

    public static HashMap<String, String> LOGIN_PROJECTION_MAP;
    private SQLiteDatabase db;

    @Override
    public boolean onCreate(){
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        return (db == null) ? false:true;
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        switch(uriMatcher.match(uri)){
            case LOGIN:
                qb.setTables(DatabaseHelper.TABLE_LOGIN);
                qb.setProjectionMap(LOGIN_PROJECTION_MAP);
                break;
            case LOGIN_ID:
                qb.setTables(DatabaseHelper.TABLE_LOGIN);
                qb.appendWhere(KEY_ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
        }

        if(sortOrder == null || sortOrder == ""){
            sortOrder = KEY_USERNAME;
        }

        Cursor c = qb.query(db,	projection,	selection, selectionArgs,null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long rowID = db.insert(DatabaseHelper.TABLE_LOGIN, "", values);
        if(rowID > 0){
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)){
            case LOGIN:
                count = db.delete(DatabaseHelper.TABLE_LOGIN, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }


}