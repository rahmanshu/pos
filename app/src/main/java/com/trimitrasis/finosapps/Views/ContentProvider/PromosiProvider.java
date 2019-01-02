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
 * Created by rahman on 05/04/2017.
 */

public class PromosiProvider extends ContentProvider {

    static final String PROVIDER_NAME = "com.trimitrasis.finosapps.ContentProvider.PromosiProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/promosi";
    public static final Uri CONTENT_URI = Uri.parse(URL);


    public static final String KEY_ID                   = "_id";
    public static final String KEY_CODE                 = "code";
    public static final String KEY_KODE_PROMO           = "kode_promo";
    public static final String KEY_NAMA_PROMO           = "nama_promo";
    public static final String KEY_FROM                 = "from_";
    public static final String KEY_TO                   = "to_";
    public static final String KEY_LOC_CODE             = "loc_code";
    public static final String KEY_JENIS_PROMO          = "jenis_promo";

    static final int PROMOSI     = 1;
    static final int PROMOSI_ID  = 2;
    static final UriMatcher uriMatcher;

    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "promosi", PROMOSI);
        uriMatcher.addURI(PROVIDER_NAME, "promosi/#", PROMOSI_ID);
    }

    public static HashMap<String, String> PROMOSI_PROJECTION_MAP;
    private SQLiteDatabase db;


    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        return (db == null) ? false:true;
    }


    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();


        switch(uriMatcher.match(uri)) {
            case PROMOSI:
                qb.setTables(DatabaseHelper.TABLE_PROMO);
                qb.setProjectionMap(PROMOSI_PROJECTION_MAP);
                break;
            case PROMOSI_ID:
                qb.setTables(DatabaseHelper.TABLE_PROMO);
                qb.appendWhere(KEY_KODE_PROMO + "=" + uri.getPathSegments().get(1));
                break;
            default:
        }

        if(sortOrder == null || sortOrder == ""){
            sortOrder = KEY_NAMA_PROMO;
        }

        Cursor c = qb.query(db,	projection,	selection, selectionArgs,null, null, sortOrder);
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
    public Uri insert(Uri uri, ContentValues contentValues) {
        long rowID = db.insert(DatabaseHelper.TABLE_PROMO, "", contentValues);
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
            case PROMOSI:
                count = db.delete(DatabaseHelper.TABLE_PROMO, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }


    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }


}
