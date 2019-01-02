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
 * Created by rahman on 09/06/2017.
 */

public class HoldSalesProvider extends ContentProvider {

    static final String PROVIDER_NAME = "com.trimitrasis.finosapps.Views.ContentProvider.HoldSalesProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/holdsales";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    static final String KD_ID_JUAL        =  DatabaseHelper.TABLE_HEADER_HOLD_SALES + "." + HeaderHoldSalesProvider.KEY_ID_JUAL;
    static final String KD_ID_JUAL_DETAIL =  DatabaseHelper.TABLE_DETAIL_HOLD_SALES + "." + DetailHoldSalesProvider.KEY_ID_JUAL_DETAIL;

    static final int HOLDSALES     = 1;
    static final int HOLDSALES_ID  = 2;
    static final UriMatcher uriMatcher;

    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "holdsales", HOLDSALES);
        uriMatcher.addURI(PROVIDER_NAME, "holdsales/#", HOLDSALES_ID);
    }

    public static HashMap<String, String> HOLDSALES_PROJECTION_MAP;
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

        switch (uriMatcher.match(uri)) {
            case HOLDSALES:
                qb.setTables(DatabaseHelper.TABLE_HEADER_HOLD_SALES + " LEFT JOIN " + DatabaseHelper.TABLE_DETAIL_HOLD_SALES +
                        " ON " + KD_ID_JUAL + " = " + KD_ID_JUAL_DETAIL);
                qb.setProjectionMap(HOLDSALES_PROJECTION_MAP);
                break;
            case HOLDSALES_ID:
                qb.setTables(DatabaseHelper.TABLE_HEADER_HOLD_SALES + " LEFT JOIN " + DatabaseHelper.TABLE_DETAIL_HOLD_SALES +
                        " ON " + KD_ID_JUAL + " = " + KD_ID_JUAL_DETAIL);
                qb.appendWhere(HeaderHoldSalesProvider.KEY_ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
        }

        if(sortOrder == null || sortOrder == ""){
            sortOrder = HeaderHoldSalesProvider.KEY_ID;
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
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }


}
