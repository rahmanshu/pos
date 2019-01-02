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

public class PromosiBarangProvider extends ContentProvider {

    static final String PROVIDER_NAME = "com.trimitrasis.finosapps.Views.ContentProvider.PromosiBarangProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/promobarang";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    public static final String KEY_KODE_PROMO       = "kode_promo_barang";
    public static final String KEY_KODE_ITEM        = "kode_item_barang";
    public static final String KEY_ITEM_NAME        = "item_name_barang";
    public static final String KEY_DISCOUNT_PERCENT = "discount_percent";
    public static final String KEY_DISCOUNT_AMOUNT  = "discount_amount";
    public static final String KEY_QTY_BARANG       = "qty_barang";
    public static final String KEY_BARCODE          = "barcode_barang";

    static final int PROMOSIBARANG     = 1;
    static final int PROMOSIBARANG_ID  = 2;
    static final UriMatcher uriMatcher;

    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "promobarang", PROMOSIBARANG);
        uriMatcher.addURI(PROVIDER_NAME, "promobarang/#", PROMOSIBARANG_ID);
    }

    public static HashMap<String, String> PROMOSIBARANG_PROJECTION_MAP;
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
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(DatabaseHelper.TABLE_PROMOBARANG);

        switch(uriMatcher.match(uri)){
            case PROMOSIBARANG:
                qb.setProjectionMap(PROMOSIBARANG_PROJECTION_MAP);
                break;
            case PROMOSIBARANG_ID:
                qb.appendWhere(KEY_KODE_PROMO + "=" + uri.getPathSegments().get(1));
                break;
            default:
        }

        if(sortOrder == null || sortOrder == ""){
            sortOrder = KEY_KODE_PROMO;
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
        long rowID = db.insert(DatabaseHelper.TABLE_PROMOBARANG, "", contentValues);
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
            case PROMOSIBARANG:
                count = db.delete(DatabaseHelper.TABLE_PROMOBARANG, selection, selectionArgs);
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
