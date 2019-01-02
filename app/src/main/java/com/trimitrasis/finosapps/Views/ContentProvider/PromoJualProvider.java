package com.trimitrasis.finosapps.Views.ContentProvider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;

/**
 * Created by rahman on 9/14/2017.
 */

public class PromoJualProvider extends ContentProvider {

    static final String PROVIDER_NAME = "com.trimitrasis.finosapps.Views.ContentProvider.PromoJualProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/promojual";
    public static final Uri CONTENT_URI = Uri.parse(URL);


    static final String KD_PROMO        =  DatabaseHelper.TABLE_PROMO + "." + PromosiProvider.KEY_KODE_PROMO;
    static final String KD_PROMO_BARANG =  DatabaseHelper.TABLE_PROMOBARANG + "." + PromosiBarangProvider.KEY_KODE_PROMO;


    static final int PROMOJUAL     = 1;
    static final int PROMOJUAL_ID  = 2;
    static final UriMatcher uriMatcher;

    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "promojual", PROMOJUAL);
        uriMatcher.addURI(PROVIDER_NAME, "promojual/#", PROMOJUAL_ID);
    }

    public static HashMap<String, String> PROMOJUAL_PROJECTION_MAP;
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

        switch(uriMatcher.match(uri)){
            case PROMOJUAL:
                qb.setTables(DatabaseHelper.TABLE_PROMO + " LEFT JOIN " + DatabaseHelper.TABLE_PROMOBARANG + " ON " + KD_PROMO + " = " + KD_PROMO_BARANG);
                qb.setProjectionMap(PROMOJUAL_PROJECTION_MAP);
                break;
            case PROMOJUAL_ID:
                qb.setTables(DatabaseHelper.TABLE_PROMO + " LEFT JOIN " + DatabaseHelper.TABLE_PROMOBARANG + " ON " + KD_PROMO + " = " + KD_PROMO_BARANG);
                qb.appendWhere(PromosiProvider.KEY_ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
        }

        if(sortOrder == null || sortOrder == ""){
            sortOrder = PromosiProvider.KEY_NAMA_PROMO;
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
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
