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
import android.support.annotation.Nullable;
import java.util.HashMap;

/**
 * Created by rahman on 03/07/2017.
 */

public class StatusEosProvider extends ContentProvider {

    static final String PROVIDER_NAME = "com.trimitrasis.finosapps.ContentProvider.StatusEosProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/statuseos";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    public static final String KEY_ID                   = "_id";
    public static final String KEY_STATUS_EOS           = "status_eos";
    public static final String KEY_INFO                 = "info_eos";


    static final int STATUSEOS     = 1;
    static final int STATUSEOS_ID  = 2;
    static final UriMatcher uriMatcher;

    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "statuseos", STATUSEOS);
        uriMatcher.addURI(PROVIDER_NAME, "statuseos/#", STATUSEOS_ID);
    }

    public static HashMap<String, String> STATUSEOS_PROJECTION_MAP;
    static SQLiteDatabase db;
    static DatabaseHelper dbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        return (db == null) ? false:true;
    }


    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        switch(uriMatcher.match(uri)){
            case STATUSEOS:
                qb.setTables(DatabaseHelper.TABLE_STATUS_END_OF_SHIFT);
                qb.setProjectionMap(STATUSEOS_PROJECTION_MAP);
                break;
            case STATUSEOS_ID:
                qb.setTables(DatabaseHelper.TABLE_STATUS_END_OF_SHIFT);
                qb.appendWhere(KEY_ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
        }

        if(sortOrder == null || sortOrder == ""){
            sortOrder = KEY_ID;
        }

        Cursor c = qb.query(db,	projection,	selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }


    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = db.insert(DatabaseHelper.TABLE_STATUS_END_OF_SHIFT, "", values);
        if(rowID > 0){
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)){
            case STATUSEOS:
                count = db.delete(DatabaseHelper.TABLE_STATUS_END_OF_SHIFT, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs){
        int count = 0;
        switch (uriMatcher.match(uri)){
            case STATUSEOS:
                count = db.update(DatabaseHelper.TABLE_STATUS_END_OF_SHIFT, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }




}
