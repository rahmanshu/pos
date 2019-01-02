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
 * Created by rahman on 8/29/2017.
 */

public class BomProvider extends ContentProvider {

    static final String PROVIDER_NAME = "com.trimitrasis.finosapps.ContentProvider.BomProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/bom";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    public static final String KEY_ID                   = "_id";
    public static final String KEY_ITEM_ID              = "item_id";
    public static final String KEY_ITEM_VAR             = "item_var";
    public static final String KEY_ITEM_NAME            = "item_name";
    public static final String KEY_ITEM_BAHAN           = "item_bahan";
    public static final String KEY_QTY                  = "qty";
    public static final String KEY_UOM                  = "uom";
    public static final String KEY_UOM_NAME             = "uom_name";
    public static final String KEY_ITEM_BAHAN_NAME      = "item_bahan_name";

    static final int BOM     = 1;
    static final int BOM_ID  = 2;
    static final UriMatcher uriMatcher;

    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "bom", BOM);
        uriMatcher.addURI(PROVIDER_NAME, "bom/#", BOM_ID);
    }

    public static HashMap<String, String> BOM_PROJECTION_MAP;
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
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        switch(uriMatcher.match(uri)){
            case BOM:
                qb.setTables(DatabaseHelper.TABLE_BOM);
                qb.setProjectionMap(BOM_PROJECTION_MAP);
                break;
            case BOM_ID:
                qb.setTables(DatabaseHelper.TABLE_BOM);
                qb.appendWhere(KEY_ITEM_ID + "=" + uri.getPathSegments().get(2));
                break;
            default:
        }

        if(sortOrder == null || sortOrder == ""){
            sortOrder = KEY_ITEM_ID;
        }

        Cursor c = qb.query(db,	projection,	selection, selectionArgs, null, null, sortOrder);
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
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values){
        long rowID = db.insert(DatabaseHelper.TABLE_BOM, "", values);
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
            case BOM:
                count = db.delete(DatabaseHelper.TABLE_BOM, selection, selectionArgs);
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
