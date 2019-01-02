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
 * Created by rahman on 09/06/2017.
 */

public class HeaderHoldSalesProvider extends ContentProvider {

    static final String PROVIDER_NAME = "com.trimitrasis.finosapps.Views.ContentProvider.HeaderHoldSalesProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/headerholdsales";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    public static final String KEY_ID              = "_id";
    public static final String KEY_ID_JUAL         = "id_jual";
    public static final String KEY_TANGGAL         = "tanggal";
    public static final String KEY_KASIR           = "kasir";
    public static final String KEY_TOTAL_BAYAR     = "total_bayar";
    public static final String KEY_STATUS_HOLD     = "status_hold";
    public static final String KEY_NOMOR_MEJA      = "nomor_meja";
    public static final String KEY_PAJAK           = "pajak";
    public static final String KEY_JENIS_PAJAK     = "jenis_ppn";


    static final int HEADERHOLDSALES     = 1;
    static final int HEADERHOLDSALES_ID  = 2;
    static final UriMatcher uriMatcher;

    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "headerholdsales", HEADERHOLDSALES);
        uriMatcher.addURI(PROVIDER_NAME, "headerholdsales/#", HEADERHOLDSALES_ID);
    }

    public static HashMap<String, String> HEADERHOLDSALES_PROJECTION_MAP;
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

        switch(uriMatcher.match(uri)){
            case HEADERHOLDSALES:
                qb.setTables(DatabaseHelper.TABLE_HEADER_HOLD_SALES);
                qb.setProjectionMap(HEADERHOLDSALES_PROJECTION_MAP);
                break;
            case HEADERHOLDSALES_ID:
                qb.setTables(DatabaseHelper.TABLE_HEADER_HOLD_SALES);
                qb.appendWhere(KEY_ID_JUAL + "=" + uri.getPathSegments().get(1));
                break;
            default:
        }

        if(sortOrder == null || sortOrder == ""){
            sortOrder = KEY_ID;
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
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = db.insert(DatabaseHelper.TABLE_HEADER_HOLD_SALES, "", values);
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
            case HEADERHOLDSALES:
                count = db.delete(DatabaseHelper.TABLE_HEADER_HOLD_SALES, selection, selectionArgs);
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
            case HEADERHOLDSALES:
                count = db.update(DatabaseHelper.TABLE_HEADER_HOLD_SALES, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
