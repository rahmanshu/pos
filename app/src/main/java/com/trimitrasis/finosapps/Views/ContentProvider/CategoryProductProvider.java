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
 * Created by rahman on 8/31/2017.
 */

public class CategoryProductProvider extends ContentProvider {

    static final String PROVIDER_NAME = "com.trimitrasis.finosapps.ContentProvider.CategoryProduct";
    static final String URL = "content://" + PROVIDER_NAME + "/categoryproduct";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    public static final String KEY_ID           = "_id";
    public static final String KEY_ID_CATEGORY  = "id_category";
    public static final String KEY_DESCRIPTION  = "description";
    public static final String KEY_STATUS       = "status_category";


    static final int CATEGORYPRODUCT     = 1;
    static final int CATEGORYPRODUCT_ID  = 2;
    static final UriMatcher uriMatcher;


    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "categoryproduct", CATEGORYPRODUCT);
        uriMatcher.addURI(PROVIDER_NAME, "categoryproduct/#", CATEGORYPRODUCT_ID);
    }

    public static HashMap<String, String> CAT_PROJECTION_MAP;
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
            case CATEGORYPRODUCT:
                qb.setTables(DatabaseHelper.TABLE_CATEGORY_PRODUCT);
                qb.setProjectionMap(CAT_PROJECTION_MAP);
                break;
            case CATEGORYPRODUCT_ID:
                qb.setTables(DatabaseHelper.TABLE_CATEGORY_PRODUCT);
                qb.appendWhere(KEY_ID_CATEGORY + "=" + uri.getPathSegments().get(2));
                break;
            default:
        }

        if(sortOrder == null || sortOrder == ""){
            sortOrder = KEY_ID_CATEGORY;
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
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long rowID = db.insert(DatabaseHelper.TABLE_CATEGORY_PRODUCT, "", values);
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
            case CATEGORYPRODUCT:
                count = db.delete(DatabaseHelper.TABLE_CATEGORY_PRODUCT, selection, selectionArgs);
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
