package com.trimitrasis.finosapps.Views.ContentProvider;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rahman on 05/04/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String LOG = "DatabaseHelper";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "FinosDbMobile";
    public static final String TABLE_BOM = "bom";
    public static final String TABLE_ITEM = "item";
    public static final String TABLE_PROMO = "promo";
    public static final String TABLE_PROMOBARANG = "promobarang";
    public static final String TABLE_PROMOHADIAH = "promohadiah";
    public static final String TABLE_TAX_GROUP = "tax_group";
    public static final String TABLE_TAX = "tax";
    public static final String TABLE_END_OF_SHIFT = "end_of_shift";
    public static final String TABLE_T_JUAL = "tjual";
    public static final String TABLE_T_JUAL_DETAIL = "tjualdetail";
    public static final String TABLE_T_JUAL_BAYAR = "tjualbayar";
    public static final String TABLE_DETAIL_HOLD_SALES = "detailholdsales";
    public static final String TABLE_HEADER_HOLD_SALES = "headerholdsales";
    public static final String TABLE_CONFIF_TRANS = "config_trans";
    public static final String TABLE_STATUS_END_OF_SHIFT = "status_end_of_shift";
    public static final String TABLE_CATEGORY_PRODUCT = "category_product";
    public static final String TABLE_LOGIN = "login";
    public static final String TABLE_STRUK_NOTE = "struk_note";
    public static final String TABLE_T_JUAL_VOID = "tjualvoid";
    public static final String TABLE_T_JUAL_DETAIL_VOID = "tjualdetailvoid";


    static final String CREATE_DB_STRUK_NOTE =
            "CREATE TABLE " + TABLE_STRUK_NOTE + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "note TEXT NULL);";

    static final String CREATE_DB_LOGIN =
            "CREATE TABLE " + TABLE_LOGIN + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "username TEXT NULL);";

    static final String CREATE_DB_CATEGORY_PRODUCT =
            "CREATE TABLE " + TABLE_CATEGORY_PRODUCT + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "id_category TEXT NULL, "
                    + "status_category INTEGER DEFAULT 0, "
                    + "description TEXT NULL);";

    static final String CREATE_DB_STATUS_END_OF_SHIFT =
            "CREATE TABLE " + TABLE_STATUS_END_OF_SHIFT + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "status_eos INTEGER DEFAULT 0, "
                    + "info_eos TEXT NULL);";

    static final String CREATE_DB_CONFIG_TRANS  =
            "CREATE TABLE " + TABLE_CONFIF_TRANS + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "status_config_trans INTEGER DEFAULT 0, "
                    + "info TEXT NULL);";


    static final String CREATE_DB_HEADER_HOLD_SALES  =
            "CREATE TABLE " + TABLE_HEADER_HOLD_SALES + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "id_jual TEXT NULL, "
                    + "status_hold INTEGER DEFAULT 0, "
                    + "tanggal TEXT NULL, "
                    + "nomor_meja TEXT NULL, "
                    + "total_bayar DOUBLE NULL, "
                    + "pajak DOUBLE NULL, "
                    + "jenis_ppn TEXT NULL, "
                    + "kasir TEXT NULL);";

    static final String CREATE_DB_DETAIL_HOLD_SALES  =
            "CREATE TABLE " + TABLE_DETAIL_HOLD_SALES + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "id_jual_detail TEXT NULL, "
                    + "item_id TEXT NULL, "
                    + "detail_id TEXT NULL, "
                    + "kode_barang TEXT NULL, "
                    + "kode_barcode TEXT NULL, "
                    + "nama_barang TEXT NULL, "
                    + "harga_jual DOUBLE NULL, "
                    + "satuan_barang TEXT NULL, "
                    + "qty DOUBLE NULL, "
                    + "diskon DOUBLE NULL, "
                    + "info TEXT NULL, "
                    + "status_hold INTEGER DEFAULT 0, "
                    + "standart_cost TEXT NULL, "
                    + "tax_group TEXT NULL, "
                    + "flag_qty BOOLEAN NULL, "
                    + "flag_bom BOOLEAN NULL, "
                    + "tax DOUBLE NULL, "
                    + "note TEXT NULL);";

    static final String CREATE_DB_END_OF_SHIFT  =
            "CREATE TABLE " + TABLE_END_OF_SHIFT + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "tanggal TEXT NULL, "
                    + "status_eos INTEGER DEFAULT 0, "
                    + "status_eod INTEGER DEFAULT 0, "
                    + "cash DOUBLE NULL, "
                    + "netsales DOUBLE NULL, "
                    + "netreturn DOUBLE NULL, "
                    + "ppn DOUBLE NULL, "
                    + "credit DOUBLE NULL, "
                    + "debit DOUBLE NULL, "
                    + "discount DOUBLE NULL, "
                    + "username TEXT NULL, "
                    + "jumlah_trans DOUBLE NULL);";


    static final String CREATE_DB_TAX_GROUP  =
            "CREATE TABLE " + TABLE_TAX_GROUP + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "id_tax_group TEXT NULL, "
                    + "description TEXT NULL);";


    static final String CREATE_DB_TAX  =
            "CREATE TABLE " + TABLE_TAX + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "id_tax TEXT NULL, "
                    + "id_tax_group TEXT NULL, "
                    + "rate TEXT NULL, "
                    + "description TEXT NULL);";

    static final String CREATE_DB_TABLE_BOM  =
            "CREATE TABLE " + TABLE_BOM + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "item_id TEXT NULL, "
                    + "item_var TEXT NULL, "
                    + "item_name TEXT NULL, "
                    + "item_bahan TEXT NULL, "
                    + "qty TEXT NULL, "
                    + "uom TEXT NULL, "
                    + "uom_name TEXT NULL, "
                    + "item_bahan_name TEXT NULL);";


    static final String CREATE_DB_TABLE_PRODUCT  =
            "CREATE TABLE " + TABLE_ITEM + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "kode_item TEXT NULL, "
                    + "stock_id TEXT NULL, "
                    + "description TEXT NULL, "
                    + "base_uom TEXT NULL, "
                    + "item_group TEXT NULL, "
                    + "item_hierarchy TEXT NULL, "
                    + "account_class TEXT NULL, "
                    + "create_by TEXT NULL, "
                    + "create_date TEXT NULL, "
                    + "create_time TEXT NULL, "
                    + "tax_group TEXT NULL, "
                    + "uom TEXT NULL, "
                    + "file_ext TEXT NULL, "
                    + "file_name TEXT NULL, "
                    + "full_path TEXT NULL, "
                    + "file_path TEXT NULL, "
                    + "detail_id TEXT NULL, "
                    + "uom_name TEXT NULL, "
                    + "item_hierarchy_ancestor TEXT NULL, "
                    + "standart_cost TEXT NULL, "
                    + "flag_qty BOOLEAN NULL, "
                    + "flag_sell BOOLEAN NULL, "
                    + "flag_bom BOOLEAN NULL, "
                    + "uom_convertion TEXT NULL, "
                    + "base_uom_convertion TEXT NULL, "
                    + "harga_jual TEXT NULL, "
                    + "barcode TEXT NULL);";



    static final String CREATE_DB_TABLE_PROMO  =
            "CREATE TABLE " + TABLE_PROMO + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "code TEXT NULL, "
                    + "kode_promo TEXT NULL, "
                    + "nama_promo TEXT NULL, "
                    + "from_ TEXT NULL, "
                    + "to_ TEXT NULL, "
                    + "loc_code TEXT NULL, "
                    + "jenis_promo TEXT NULL);";


    static final String CREATE_DB_PROMO_BARANG  =
            "CREATE TABLE " + TABLE_PROMOBARANG + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "kode_promo_barang TEXT NULL, "
                    + "kode_item_barang TEXT NULL, "
                    + "item_name_barang TEXT NULL, "
                    + "discount_percent DOUBLE NULL, "
                    + "discount_amount DOUBLE NULL, "
                    + "barcode_barang TEXT NULL, "
                    + "qty_barang DOUBLE NULL);";

    static final String CREATE_DB_PROMO_HADIAH  =
            "CREATE TABLE " + TABLE_PROMOHADIAH + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "kode_promo_hadiah TEXT NULL, "
                    + "kode_item_hadiah TEXT NULL, "
                    + "item_name_hadiah TEXT NULL, "
                    + "barcode_hadiah TEXT NULL, "
                    + "qty_hadiah DOUBLE NULL);";


    static final String CREATE_DB_T_JUAL  =
            "CREATE TABLE " + TABLE_T_JUAL + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "id_jual TEXT NULL, "
                    + "id_member TEXT NULL, "
                    + "id_kasir TEXT NULL, "
                    + "location TEXT NULL, "
                    + "tanggal TEXT NULL, "
                    + "time TEXT NULL, "
                    + "subtotal DOUBLE NULL, "
                    + "tax DOUBLE NULL, "
                    + "total_discount DOUBLE NULL, "
                    + "total_bayar DOUBLE NULL, "
                    + "status_sinc INTEGER DEFAULT 0, "
                    + "shift_id TEXT NULL, "
                    + "id_reference TEXT NULL, "
                    + "kembalian DOUBLE NULL);";



    static final String CREATE_DB_T_JUAL_DETAIL  =
            "CREATE TABLE " + TABLE_T_JUAL_DETAIL + " (id_jual_item INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "id_jual_detail TEXT NULL, "
                    + "item_code TEXT NULL, "
                    + "detail_id TEXT NULL, "
                    + "description TEXT NULL, "
                    + "harga DOUBLE NULL, "
                    + "harga_member DOUBLE NULL, "
                    + "qty DOUBLE NULL, "
                    + "satuan TEXT NULL, "
                    + "discount DOUBLE NULL, "
                    + "total DOUBLE NULL, "
                    + "promo TEXT NULL, "
                    + "barcode TEXT NULL, "
                    + "disc_percent DOUBLE NULL, "
                    + "disc_amount DOUBLE NULL, "
                    + "tax_type TEXT NULL, "
                    + "harga_hpp DOUBLE NULL);";


    static final String CREATE_DB_T_JUAL_VOID  =
            "CREATE TABLE " + TABLE_T_JUAL_VOID + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "id_jual TEXT NULL, "
                    + "id_member TEXT NULL, "
                    + "id_kasir TEXT NULL, "
                    + "location TEXT NULL, "
                    + "tanggal TEXT NULL, "
                    + "time TEXT NULL, "
                    + "subtotal DOUBLE NULL, "
                    + "tax DOUBLE NULL, "
                    + "total_discount DOUBLE NULL, "
                    + "total_bayar DOUBLE NULL, "
                    + "status_sinc INTEGER DEFAULT 0, "
                    + "shift_id TEXT NULL, "
                    + "id_reference TEXT NULL, "
                    + "kembalian DOUBLE NULL);";



    static final String CREATE_DB_T_JUAL_DETAIL_VOID  =
            "CREATE TABLE " + TABLE_T_JUAL_DETAIL_VOID + " (id_jual_item INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "id_jual_detail TEXT NULL, "
                    + "item_code TEXT NULL, "
                    + "detail_id TEXT NULL, "
                    + "description TEXT NULL, "
                    + "harga DOUBLE NULL, "
                    + "harga_member DOUBLE NULL, "
                    + "qty DOUBLE NULL, "
                    + "satuan TEXT NULL, "
                    + "discount DOUBLE NULL, "
                    + "total DOUBLE NULL, "
                    + "promo TEXT NULL, "
                    + "barcode TEXT NULL, "
                    + "disc_percent DOUBLE NULL, "
                    + "disc_amount DOUBLE NULL, "
                    + "tax_type TEXT NULL, "
                    + "harga_hpp DOUBLE NULL);";


    static final String CREATE_DB_T_JUAL_BAYAR  =
            "CREATE TABLE " + TABLE_T_JUAL_BAYAR + " (id_bayar_item INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "id_jual_bayar TEXT NULL, "
                    + "status_sinc INTEGER DEFAULT 0, "
                    + "tipe TEXT NULL, "
                    + "bank_id TEXT NULL, "
                    + "bank_name TEXT NULL, "
                    + "total_bayar DOUBLE NULL, "
                    + "kembalian DOUBLE NULL, "
                    + "no_kartu TEXT NULL);";



    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_DB_CATEGORY_PRODUCT);
        db.execSQL(CREATE_DB_TABLE_PRODUCT);
        db.execSQL(CREATE_DB_TABLE_BOM);
        db.execSQL(CREATE_DB_TABLE_PROMO);
        db.execSQL(CREATE_DB_PROMO_BARANG);
        db.execSQL(CREATE_DB_PROMO_HADIAH);
        db.execSQL(CREATE_DB_T_JUAL);
        db.execSQL(CREATE_DB_T_JUAL_DETAIL);
        db.execSQL(CREATE_DB_T_JUAL_BAYAR);
        db.execSQL(CREATE_DB_TAX_GROUP);
        db.execSQL(CREATE_DB_TAX);
        db.execSQL(CREATE_DB_END_OF_SHIFT);
        db.execSQL(CREATE_DB_HEADER_HOLD_SALES);
        db.execSQL(CREATE_DB_DETAIL_HOLD_SALES);
        db.execSQL(CREATE_DB_CONFIG_TRANS);
        db.execSQL(CREATE_DB_STATUS_END_OF_SHIFT);
        db.execSQL(CREATE_DB_LOGIN);
        db.execSQL(CREATE_DB_STRUK_NOTE);
        db.execSQL(CREATE_DB_T_JUAL_VOID);
        db.execSQL(CREATE_DB_T_JUAL_DETAIL_VOID);
        System.out.println("table created");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROMO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROMOBARANG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROMOHADIAH);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_T_JUAL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_T_JUAL_DETAIL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_T_JUAL_BAYAR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAX_GROUP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAX);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_END_OF_SHIFT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HEADER_HOLD_SALES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DETAIL_HOLD_SALES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONFIF_TRANS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATUS_END_OF_SHIFT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STRUK_NOTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_T_JUAL_VOID);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_T_JUAL_DETAIL_VOID);
        onCreate(db);
    }



}
