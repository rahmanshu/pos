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

public class TransJualDetailVoidProvider extends ContentProvider {

    static final String PROVIDER_NAME = "com.trimitrasis.finosapps.ContentProvider.TransJualDetailVoidProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/transjualdetailvoid";
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

    static final int TRANSJUALDETAILVOID     = 1;
    static final int TRANSJUALDETAILVOID_ID  = 2;
    static final UriMatcher uriMatcher;

    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "transjualdetailvoid", TRANSJUALDETAILVOID);
        uriMatcher.addURI(PROVIDER_NAME, "transjualdetailvoid/#",TRANSJUALDETAILVOID_ID);
    }

    public static HashMap<String, String> TRANSJUALDETAILVOID_PROJECTION_MAP;
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
        qb.setTables(DatabaseHelper.TABLE_T_JUAL_DETAIL_VOID);

        switch(uriMatcher.match(uri)) {
            case TRANSJUALDETAILVOID:
                qb.setProjectionMap(TRANSJUALDETAILVOID_PROJECTION_MAP);
                break;
            case TRANSJUALDETAILVOID_ID:
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
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long rowID = db.insert(DatabaseHelper.TABLE_T_JUAL_DETAIL_VOID, "", values);
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
            case TRANSJUALDETAILVOID:
                count = db.delete(DatabaseHelper.TABLE_T_JUAL_DETAIL_VOID, selection, selectionArgs);
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
