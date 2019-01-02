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

public class TransJualDetailProvider extends ContentProvider {

    static final String PROVIDER_NAME = "com.trimitrasis.finosapps.ContentProvider.TransJualDetailProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/transjualdetail";
    public static final Uri CONTENT_URI = Uri.parse(URL);


    public static final String KEY_ID_JUAL_ITEM         = "id_jual_item";
    public static final String KEY_ID_JUAL_DETAIL       = "id_jual_detail";
    public static final String KEY_DETAIL_ID            = "detail_id";
    public static final String KEY_ITEM_CODE            = "item_code";
    public static final String KEY_DESCRIPTION          = "description";
    public static final String KEY_HARGA                = "harga";
    public static final String KEY_HARGA_MEMBER         = "harga_member";
    public static final String KEY_QTY                  = "qty";
    public static final String KEY_SATUAN               = "satuan";
    public static final String KEY_DISCOUNT             = "discount";
    public static final String KEY_TOTAL                = "total";
    public static final String KEY_PROMO                = "promo";
    public static final String KEY_BARCODE              = "barcode";
    public static final String KEY_DISC_PERCENT         = "disc_percent";
    public static final String KEY_DISC_AMOUNT          = "disc_amount";
    public static final String KEY_TAX_TYPE             = "tax_type";
    public static final String KEY_HARGA_HPP            = "harga_hpp";

    static final int TRANSJUALDETAIL     = 1;
    static final int TRANSJUALDETAIL_ID  = 2;
    static final UriMatcher uriMatcher;

    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "transjualdetail", TRANSJUALDETAIL);
        uriMatcher.addURI(PROVIDER_NAME, "transjualdetail/#",TRANSJUALDETAIL_ID);
    }

    public static HashMap<String, String> TRANSJUALDETAIL_PROJECTION_MAP;
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
        qb.setTables(DatabaseHelper.TABLE_T_JUAL_DETAIL);

        switch(uriMatcher.match(uri)) {
            case TRANSJUALDETAIL:
                qb.setProjectionMap(TRANSJUALDETAIL_PROJECTION_MAP);
                break;
            case TRANSJUALDETAIL_ID:
                qb.appendWhere(KEY_ID_JUAL_DETAIL + "=" + uri.getPathSegments().get(1));
                break;
            default:
        }


        if(sortOrder == null || sortOrder == ""){
            sortOrder = KEY_ID_JUAL_DETAIL;
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
        long rowID = db.insert(DatabaseHelper.TABLE_T_JUAL_DETAIL, "", contentValues);
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
            case TRANSJUALDETAIL:
                count = db.delete(DatabaseHelper.TABLE_T_JUAL_DETAIL, selection, selectionArgs);
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
