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

public class ItemProvider extends ContentProvider {

    static final String PROVIDER_NAME = "com.trimitrasis.finosapps.ContentProvider.ItemProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/item";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    public static final String KEY_ID                   = "_id";
    public static final String KEY_KODE_ITEM            = "kode_item";
    public static final String KEY_STOCK_ID             = "stock_id";
    public static final String KEY_DESCRIPTION          = "description";
    public static final String KEY_BASE_UOM             = "base_uom";
    public static final String KEY_ITEM_GROUP           = "item_group";
    public static final String KEY_ITEM_HIERARCHY       = "item_hierarchy";
    public static final String KEY_ACCOUNT_CLASS        = "account_class";
    public static final String KEY_CREATE_BY            = "create_by";
    public static final String KEY_CREATE_DATE          = "create_date";
    public static final String KEY_CREATE_TIME          = "create_time";
    public static final String KEY_TAX_GROUP            = "tax_group";
    public static final String KEY_DETAIL_ID            = "detail_id";
    public static final String KEY_UOM                  = "uom";
    public static final String KEY_UOM_NAME             = "uom_name";
    public static final String KEY_UOM_CONVERTION       = "uom_convertion";
    public static final String KEY_BASE_UOM_CONVERTION  = "base_uom_convertion";
    public static final String KEY_HARGA_JUAL           = "harga_jual";
    public static final String KEY_BARCODE              = "barcode";
    public static final String KEY_FLAG_QTY             = "flag_qty";
    public static final String KEY_FLAG_SELL            = "flag_sell";
    public static final String KEY_FLAG_BOM             = "flag_bom";
    public static final String KEY_STANDART_COST        = "standart_cost";
    public static final String KEY_ITEM_HIERARCHY_CAT   = "item_hierarchy_ancestor";
    public static final String KEY_FILE_EXT             = "file_ext";
    public static final String KEY_FILE_NAME             = "file_name";
    public static final String KEY_FULL_PATH             = "full_path";
    public static final String KEY_FILE_PATH             = "file_path";


    static final int ITEM     = 1;
    static final int ITEM_ID  = 2;
    static final UriMatcher uriMatcher;

    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "item", ITEM);
        uriMatcher.addURI(PROVIDER_NAME, "item/#", ITEM_ID);
    }

    public static HashMap<String, String> ITEM_PROJECTION_MAP;
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
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        switch(uriMatcher.match(uri)){
            case ITEM:
                qb.setTables(DatabaseHelper.TABLE_ITEM);
                qb.setProjectionMap(ITEM_PROJECTION_MAP);
                break;
            case ITEM_ID:
                qb.setTables(DatabaseHelper.TABLE_ITEM);
                qb.appendWhere(KEY_STOCK_ID + "=" + uri.getPathSegments().get(2));
                break;
            default:
        }

        if(sortOrder == null || sortOrder == ""){
            sortOrder = KEY_DESCRIPTION;
        }

        Cursor c = qb.query(db,	projection,	selection, selectionArgs, null, null, sortOrder);
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
        long rowID = db.insert(DatabaseHelper.TABLE_ITEM, "", contentValues);
        if(rowID > 0){
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }


    /*
    all insert, ONE transaction
    public void add_cities(List<Cities> list) {
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (Cities city : list) {
                values.put(CityId, city.getCityid());
                values.put(CityName, city.getCityName());
                db.insert(TABLE_CITY, null, values);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
    */



    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)){
            case ITEM:
                count = db.delete(DatabaseHelper.TABLE_ITEM, selection, selectionArgs);
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


    public static boolean isTableExists(String tableName, boolean openDb){

        if(openDb){
            if(db == null || !db.isOpen()){
                db = dbHelper.getReadableDatabase();
            }

            if(!db.isReadOnly()){
                db.close();
                db = dbHelper.getReadableDatabase();
            }
        }

        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'", null);
        if(cursor!=null){
            if(cursor.getCount()>0){
                cursor.close();
                return true;
            }
            cursor.close();
        }

        return false;
    }


}
