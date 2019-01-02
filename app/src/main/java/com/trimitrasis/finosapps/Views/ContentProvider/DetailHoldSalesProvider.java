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
 * Created by rahman on 09/06/2017.
 */

public class DetailHoldSalesProvider extends ContentProvider {

    static final String PROVIDER_NAME = "com.trimitrasis.finosapps.Views.ContentProvider.DetailHoldSalesProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/detailholdsales";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    public static final String KEY_ID             = "_id";
    public static final String KEY_ID_JUAL_DETAIL = "id_jual_detail";
    public static final String KEY_ITEM_ID        = "item_id";
    public static final String KEY_DETAL_ID       = "detail_id";
    public static final String KEY_KODE_BARANG    = "kode_barang";
    public static final String KEY_KODE_BARCODE   = "kode_barcode";
    public static final String KEY_NAMA_BARANG    = "nama_barang";
    public static final String KEY_SATUAN_BARANG  = "satuan_barang";
    public static final String KEY_HARGA_JUAL     = "harga_jual";
    public static final String KEY_QTY            = "qty";
    public static final String KEY_DISKON         = "diskon";
    public static final String KEY_INFO           = "info";
    public static final String KEY_TAX            = "tax";
    public static final String KEY_STATUS_HOLD    = "status_hold";
    public static final String KEY_STANDART_COST  = "standart_cost";
    public static final String KEY_FLAG_QTY       = "flag_qty";
    public static final String KEY_FLAG_BOM       = "flag_bom";
    public static final String KEY_TAX_GROUP      = "tax_group";
    public static final String KEY_NOTE           = "note";

    static final int DETAILHOLDSALES     = 1;
    static final int DETAILHOLDSALES_ID  = 2;
    static final UriMatcher uriMatcher;

    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "detailholdsales", DETAILHOLDSALES);
        uriMatcher.addURI(PROVIDER_NAME, "detailholdsales/#", DETAILHOLDSALES_ID);
    }

    public static HashMap<String, String> DETAILHOLDSALES_PROJECTION_MAP;
    private SQLiteDatabase db;


    @Override
    public boolean onCreate(){
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
            case DETAILHOLDSALES:
                qb.setTables(DatabaseHelper.TABLE_DETAIL_HOLD_SALES);
                qb.setProjectionMap(DETAILHOLDSALES_PROJECTION_MAP);
                break;
            case DETAILHOLDSALES_ID:
                qb.setTables(DatabaseHelper.TABLE_DETAIL_HOLD_SALES);
                qb.appendWhere(KEY_KODE_BARANG + "=" + uri.getPathSegments().get(1));
                break;
            default:
        }

        if(sortOrder == null || sortOrder == ""){
            sortOrder = KEY_NAMA_BARANG;
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
        long rowID = db.insert(DatabaseHelper.TABLE_DETAIL_HOLD_SALES, "", values);
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
            case DETAILHOLDSALES:
                count = db.delete(DatabaseHelper.TABLE_DETAIL_HOLD_SALES, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

}
