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
 * Created by rahman on 14/04/2017.
 */

public class TransPenjualanProvider extends ContentProvider {

    static final String PROVIDER_NAME = "com.trimitrasis.finosapps.ContentProvider.TransPenjualanProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/transpenjualan";
    public static final Uri CONTENT_URI = Uri.parse(URL);


    static final String KD_JUAL        =  DatabaseHelper.TABLE_T_JUAL + "." + TransJualProvider.KEY_ID_JUAL;
    static final String KD_JUAL_DETAIL =  DatabaseHelper.TABLE_T_JUAL_DETAIL + "." + TransJualDetailProvider.KEY_ID_JUAL_DETAIL;

    static final int TRANSPENJUALAN     = 1;
    static final int TRANSPENJUALAN_ID  = 2;
    static final UriMatcher uriMatcher;


    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "transpenjualan", TRANSPENJUALAN);
        uriMatcher.addURI(PROVIDER_NAME, "transpenjualan/#", TRANSPENJUALAN_ID);
    }

    public static HashMap<String, String> TRANSPENJUALAN_PROJECTION_MAP;
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
            case TRANSPENJUALAN:
                qb.setTables(DatabaseHelper.TABLE_T_JUAL + " LEFT JOIN " + DatabaseHelper.TABLE_T_JUAL_DETAIL + " ON " +
                        KD_JUAL + " = " + KD_JUAL_DETAIL);
                qb.setProjectionMap(TRANSPENJUALAN_PROJECTION_MAP);
                break;

            case TRANSPENJUALAN_ID:
                qb.setTables(DatabaseHelper.TABLE_T_JUAL + " LEFT JOIN " + DatabaseHelper.TABLE_T_JUAL_DETAIL + " ON " +
                        KD_JUAL + " = " + KD_JUAL_DETAIL);
                qb.appendWhere(TransJualProvider.KEY_ID_JUAL + "=" + uri.getPathSegments().get(1));
                break;
            default:

        }

        if(sortOrder == null || sortOrder == ""){
            sortOrder = TransJualProvider.KEY_ID;
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
