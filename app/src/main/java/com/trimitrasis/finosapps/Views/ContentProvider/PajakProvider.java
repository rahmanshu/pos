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
 * Created by rahman on 26/05/2017.
 */


public class PajakProvider extends ContentProvider {

    static final String PROVIDER_NAME = "com.trimitrasis.finosapps.ContentProvider.PajakProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/pajak";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    static final String KD_TAX_GROUP   =  DatabaseHelper.TABLE_TAX_GROUP + "." + TaxGroupProvider.KEY_ID_TAX_GROUP;
    static final String KD_TAX         =  DatabaseHelper.TABLE_TAX + "." + TaxProvider.KEY_ID_TAX_GROUP;

    static final int PAJAK     = 1;
    static final int PAJAK_ID  = 2;
    static final UriMatcher uriMatcher;


    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "pajak", PAJAK);
        uriMatcher.addURI(PROVIDER_NAME, "pajak/#", PAJAK_ID);
    }

    public static HashMap<String, String> PAJAK_PROJECTION_MAP;
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
            case PAJAK:
                qb.setTables(DatabaseHelper.TABLE_TAX_GROUP + " LEFT JOIN " + DatabaseHelper.TABLE_TAX + " ON " + KD_TAX_GROUP + " = " + KD_TAX);
                qb.setProjectionMap(PAJAK_PROJECTION_MAP);
                break;

            case PAJAK_ID:
                qb.setTables(DatabaseHelper.TABLE_TAX_GROUP + " LEFT JOIN " + DatabaseHelper.TABLE_TAX + " ON " + KD_TAX_GROUP + " = " + KD_TAX);
                qb.appendWhere(TaxGroupProvider.KEY_ID_TAX_GROUP + "=" + uri.getPathSegments().get(1));
                break;
            default:
        }


        if(sortOrder == null || sortOrder == ""){
            sortOrder = TaxGroupProvider.KEY_ID;
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
