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
 * Created by rahman on 10/25/2017.
 */

public class TransJualVoidProvider extends ContentProvider {

    static final String PROVIDER_NAME = "com.trimitrasis.finosapps.ContentProvider.TransJualVoidProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/transjualvoid";
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


    static final int TRANSJUALVOID     = 1;
    static final int TRANSJUALVOID_ID  = 2;
    static final UriMatcher uriMatcher;

    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "transjualvoid", TRANSJUALVOID);
        uriMatcher.addURI(PROVIDER_NAME, "transjualvoid/#",TRANSJUALVOID_ID);
    }

    public static HashMap<String, String> TRANSJUALVOID_PROJECTION_MAP;
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
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(DatabaseHelper.TABLE_T_JUAL_VOID);

        switch(uriMatcher.match(uri)) {
            case TRANSJUALVOID:
                qb.setProjectionMap(TRANSJUALVOID_PROJECTION_MAP);
                break;
            case TRANSJUALVOID_ID:
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
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long rowID = db.insert(DatabaseHelper.TABLE_T_JUAL_VOID, "", values);
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
            case TRANSJUALVOID:
                count = db.delete(DatabaseHelper.TABLE_T_JUAL_VOID, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }


    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String s, @Nullable String[] strings) {
        int count = 0;
        switch (uriMatcher.match(uri)){
            case TRANSJUALVOID:
                count = db.update(DatabaseHelper.TABLE_T_JUAL_VOID, values, s, strings);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
