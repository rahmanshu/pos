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
 * Created by rahman on 06/04/2017.
 */

public class TransJualProvider extends ContentProvider {

    static final String PROVIDER_NAME = "com.trimitrasis.finosapps.ContentProvider.TransJualProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/transjual";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    public static final String KEY_ID                   = "_id";
    public static final String KEY_ID_JUAL              = "id_jual";
    public static final String KEY_ID_MEMBER            = "id_member";
    public static final String KEY_ID_KASIR             = "id_kasir";
    public static final String KEY_TANGGAL              = "tanggal";
    public static final String KEY_TIME                 = "time";
    public static final String KEY_SUBTOTAL             = "subtotal";
    public static final String KEY_TAX                  = "tax";
    public static final String KEY_TOTAL_DISCOUNT       = "total_discount";
    public static final String KEY_TOTAL_BAYAR          = "total_bayar";
    public static final String KEY_KEMBALIAN            = "kembalian";
    public static final String KEY_STATUS_SINC          = "status_sinc";
    public static final String KEY_SHIFT_ID             = "shift_id";
    public static final String KEY_ID_REFERENCE         = "id_reference";


    static final int TRANSJUAL     = 1;
    static final int TRANSJUAL_ID  = 2;
    static final UriMatcher uriMatcher;

    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "transjual", TRANSJUAL);
        uriMatcher.addURI(PROVIDER_NAME, "transjual/#",TRANSJUAL_ID);
    }

    public static HashMap<String, String> TRANSJUAL_PROJECTION_MAP;
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
        qb.setTables(DatabaseHelper.TABLE_T_JUAL);

        switch(uriMatcher.match(uri)) {
            case TRANSJUAL:
                qb.setProjectionMap(TRANSJUAL_PROJECTION_MAP);
                break;
            case TRANSJUAL_ID:
                qb.appendWhere(KEY_ID_JUAL + "=" + uri.getPathSegments().get(1));
                break;
            default:
        }


        if(sortOrder == null || sortOrder == ""){
            sortOrder = KEY_ID_JUAL;
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
        long rowID = db.insert(DatabaseHelper.TABLE_T_JUAL, "", contentValues);
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
            case TRANSJUAL:
                count = db.delete(DatabaseHelper.TABLE_T_JUAL, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        int count = 0;
        switch (uriMatcher.match(uri)){
            case TRANSJUAL:
                count = db.update(DatabaseHelper.TABLE_T_JUAL, contentValues, s, strings);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
