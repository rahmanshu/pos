package com.trimitrasis.finosapps.Views.ContentProvider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import java.util.HashMap;

/**
 * Created by rahman on 05/04/2017.
 */

public class PromosiPenjualanProvider extends ContentProvider {

    static final String PROVIDER_NAME = "com.trimitrasis.finosapps.Views.ContentProvider.PromosiPenjualanProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/promosipenjualan";
    public static final Uri CONTENT_URI = Uri.parse(URL);


    static final String KD_PROMO        =  DatabaseHelper.TABLE_PROMO + "." + PromosiProvider.KEY_KODE_PROMO;
    static final String KD_PROMO_BARANG =  DatabaseHelper.TABLE_PROMOBARANG + "." + PromosiBarangProvider.KEY_KODE_PROMO;
    static final String KD_PROMO_HADIAH =  DatabaseHelper.TABLE_PROMOHADIAH + "." + PromosiHadiahProvider.KEY_KODE_PROMO;

    static final int PROMOSIPENJUALAN     = 1;
    static final int PROMOSIPENJUALAN_ID  = 2;
    static final UriMatcher uriMatcher;

    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "promosipenjualan", PROMOSIPENJUALAN);
        uriMatcher.addURI(PROVIDER_NAME, "promosipenjualan/#", PROMOSIPENJUALAN_ID);
    }

    public static HashMap<String, String> PROMOSIPENJUALAN_PROJECTION_MAP;
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
            case PROMOSIPENJUALAN:

                qb.setTables(DatabaseHelper.TABLE_PROMO + " LEFT JOIN " + DatabaseHelper.TABLE_PROMOBARANG +
                        " ON " + KD_PROMO + " = " + KD_PROMO_BARANG + " LEFT JOIN " + DatabaseHelper.TABLE_PROMOHADIAH +
                        " ON " + KD_PROMO + " = " + KD_PROMO_HADIAH);
                        qb.setProjectionMap(PROMOSIPENJUALAN_PROJECTION_MAP);
                break;
            case PROMOSIPENJUALAN_ID:
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
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
