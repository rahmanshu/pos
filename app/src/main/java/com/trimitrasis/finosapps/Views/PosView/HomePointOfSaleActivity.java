package com.trimitrasis.finosapps.Views.PosView;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import com.google.gson.Gson;
import com.roger.catloadinglibrary.CatLoadingView;
import com.trimitrasis.finosapps.Connection.ApiConnection;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.SessionManager;
import com.trimitrasis.finosapps.Views.ContentProvider.ItemProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.PromosiBarangProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.PromosiHadiahProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.PromosiProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.TaxProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.TransJualBayarProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.TransJualDetailProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.TransJualProvider;
import com.trimitrasis.finosapps.Views.HomeView.HomeFragment;
import com.trimitrasis.finosapps.Views.HomeView.HomeFragment_;
import com.trimitrasis.finosapps.Views.LoginView.Models.LoginModel;
import com.trimitrasis.finosapps.Views.PosView.Model.SyncTransModel;
import com.trimitrasis.finosapps.Views.PosView.Model.TransaksiJualBayarModel;
import com.trimitrasis.finosapps.Views.PosView.Model.TransaksiPenjualanModel;
import com.trimitrasis.finosapps.Views.UtilView.Constants;
import com.trimitrasis.finosapps.Views.UtilView.Utils;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rahman on 03/05/2017.
 */

@EActivity(R.layout.layout_home_pos)
public class HomePointOfSaleActivity extends AppCompatActivity implements HomeFragment.MenuButtonListener{


    @ViewById
    Toolbar toolbar;

    Fragment currentFragment;

    CatLoadingView catLoadingItem, catLoadingPromo, catLoadingPromoBarang, catLoadingPromoHadiah, catLoadingTax, catLoading;

    SessionManager sessionManager;

    ArrayList<TransaksiPenjualanModel> transaksiPenjualanModels;
    ArrayList<TransaksiJualBayarModel> transaksiJualBayarModels;

    @AfterViews
    void afterView(){
        transaksiPenjualanModels = new ArrayList<>();
        transaksiPenjualanModels.clear();
        transaksiJualBayarModels = new ArrayList<>();
        transaksiJualBayarModels.clear();

        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.checkLogin();
        customView();
        initTabbar();
        attachHomeFragment();
        //cekUserLogin();
    }


    private void initTabbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_arrow_left);
    }

    private void customView(){
        TextView toolbarView = (TextView) toolbar.findViewById(R.id.headerText);
        toolbarView.setText("Point Of Sales");
    }

    @OptionsItem(android.R.id.home)
    void homeClick(){
        onBackPressed();
    }


    private void attachHomeFragment(){
        if (currentFragment == null || !(currentFragment instanceof HomeFragment_)){
            attachFragment(HomeFragment_.class, "home_fragment");
        }
    }

    private void attachFragment(Class c,String tag) {

        Object newFragment = null;
        try {
            newFragment = c.newInstance();
        } catch (InstantiationException ex) {
            Log.e("InstantiationException", "Cannot create new Fragment " + c.getName());
        } catch (IllegalAccessException ex) {
            Log.e("IllegalAccessException", "Cannot create new Fragment " + c.getName());
        } catch (NullPointerException e) {
            Log.e("NullPointerException","null pointer : "+e.getMessage());
        }
        if (newFragment != null) {
            currentFragment = (Fragment) newFragment;

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.menuContentLayout, (Fragment) newFragment, tag)
                    .commit();

            invalidateOptionsMenu();
        }
    }

    @Override
    public void onClickHome(int idMenu){
        switch (idMenu){
            case Constants.START_OF_DAY_MENU:
                StartOfDayActivity_.intent(HomePointOfSaleActivity.this).start();
                break;
            case Constants.START_OF_SHIFT_MENU:
                StartOfShiftNewActivity_.intent(HomePointOfSaleActivity.this).start();
                break;
            case Constants.END_OF_SHIFT_MENU:
                EndOfShiftActivity_.intent(HomePointOfSaleActivity.this).start();
                break;
            case Constants.END_OF_DAY_MENU:
                EndOfDayActivity_.intent(HomePointOfSaleActivity.this).start();
                break;
            case Constants.POS_MENU:
                StartOfShiftNewActivity_.intent(HomePointOfSaleActivity.this).start();
                break;
            case Constants.SYNC_TRANS_MENU:
                //SyncTransaksiActivity_.intent(HomePointOfSaleActivity.this).start();
                getDataJualBayar();
                getDataTransFromLocal();
                break;
            case Constants.HOLD_SALES_MENU:
                HoldPenjualanActivity_.intent(HomePointOfSaleActivity.this).start();
                break;
            case Constants.PENGATURAN_MENU:
                break;
            case Constants.SINKRON_MENU:

                if(Utils.cek_status((this))){
                    getAllProduk();
                }else{
                    showDialogConnection("Error Connection!");
                }
                break;
        }
    }


    //item
    private void getAllProduk(){
        catLoadingItem = new CatLoadingView();
        catLoadingItem.show(getSupportFragmentManager(), "");
        ApiConnection.getViewDataItemInterface().getAllDataProduct(Constants.userInfoModel, new Callback<Response>(){
            @Override
            public void success(Response response, Response response2){

                double uom_convertion = 0, base_uom_convertion = 0, harga_jual = 0;
                double standart_cost = 0;
                boolean flag_qty;
                String id = "", s_description = "", base_uom = "", item_group = "", item_hierarchy = "", uom = "";
                String barcode = "", tax_group = "", strMongoId = "", uom_name = "";

                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject vendorObj = null;
                try {
                    vendorObj = new JSONObject(rawResponse);
                    String jStatus  = vendorObj.optString("message");
                    JSONArray jResult = vendorObj.optJSONArray("result");

                    if(jStatus.equals("success")){
                        for(int i = 0; i < jResult.length(); i ++){
                            ContentValues values  = new ContentValues();
                            JSONObject jsonObject = jResult.optJSONObject(i);

                            JSONObject idObj = (JSONObject) jsonObject.get("_id");
                            strMongoId       = (String) idObj.get("$id");
                            id               = jsonObject.optString("id");
                            s_description    = jsonObject.optString("s_description");
                            base_uom         = jsonObject.optString("base_uom");
                            item_group       = jsonObject.optString("item_group");
                            item_hierarchy   = jsonObject.optString("item_hierarchy");
                            tax_group        = jsonObject.optString("tax_group");
                            standart_cost    = jsonObject.optDouble("standart_cost");
                            flag_qty         = jsonObject.optBoolean("flag_qty");

                            JSONArray jsonArrItemConvertion = jsonObject.optJSONArray("item_convertion");
                            if(jsonArrItemConvertion != null){
                                for(int j = 0; j < jsonArrItemConvertion.length(); j++){
                                    JSONObject jsonObjectItem = jsonArrItemConvertion.optJSONObject(j);

                                    uom                 = jsonObjectItem.optString("uom");
                                    uom_name            = jsonObjectItem.optString("uom_name");
                                    uom_convertion      = jsonObjectItem.optDouble("uom_convertion");
                                    base_uom_convertion = jsonObjectItem.optDouble("base_uom_convertion");
                                    harga_jual          = jsonObjectItem.optDouble("harga_jual");
                                    barcode             = jsonObjectItem.optString("barcode");

                                    int emptyTableProduk = getContentResolver().delete(ItemProvider.CONTENT_URI, "barcode " + " = '" +  barcode + "'", null);

                                    values.put(ItemProvider.KEY_KODE_ITEM, strMongoId);
                                    values.put(ItemProvider.KEY_STOCK_ID, id);
                                    values.put(ItemProvider.KEY_DESCRIPTION, s_description);
                                    values.put(ItemProvider.KEY_BASE_UOM, base_uom);
                                    values.put(ItemProvider.KEY_ITEM_GROUP, item_group);
                                    values.put(ItemProvider.KEY_ITEM_HIERARCHY, item_hierarchy);
                                    values.put(ItemProvider.KEY_TAX_GROUP, tax_group);
                                    values.put(ItemProvider.KEY_UOM, uom);
                                    values.put(ItemProvider.KEY_UOM_NAME, uom_name);
                                    values.put(ItemProvider.KEY_UOM_CONVERTION, uom_convertion);
                                    values.put(ItemProvider.KEY_BASE_UOM_CONVERTION, base_uom_convertion);
                                    values.put(ItemProvider.KEY_HARGA_JUAL, harga_jual);
                                    values.put(ItemProvider.KEY_BARCODE, barcode);
                                    values.put(ItemProvider.KEY_STANDART_COST, standart_cost);
                                    values.put(ItemProvider.KEY_FLAG_QTY, flag_qty);

                                    Uri uri = getContentResolver().insert(ItemProvider.CONTENT_URI, values);
                                }
                            }
                        }

                        if(catLoadingItem!=null)catLoadingItem.dismiss();
                        getAllPromo();

                    }else if(jStatus.equals("failed")){
                        Utils.showToast("Input data Item terlebih dahulu!", HomePointOfSaleActivity.this);
                    }

                }catch(JSONException e){
                    e.printStackTrace();
                    Utils.showToast("Syncronisasi Item Failed!", HomePointOfSaleActivity.this);
                    if(catLoadingItem!=null)catLoadingItem.dismiss();
                }

                if(catLoadingItem!=null)catLoadingItem.dismiss();

            }

            @Override
            public void failure(RetrofitError error) {
                Utils.showToast("Syncronisasi Item Failed!", HomePointOfSaleActivity.this);
                if(catLoadingItem!=null)catLoadingItem.dismiss();
            }
        });
    }



    private void getAllPromo(){
        catLoadingPromo = new CatLoadingView();
        catLoadingPromo.show(getSupportFragmentManager(), "");
        ApiConnection.getViewDataPromoInterface().getViewDataPromo(Constants.userInfoModel, new Callback<Response>(){

            @Override
            public void success(Response response, Response response2){
                String  nama_promosi, from_, to_, loc_code, jenis_promo, code, strMongoId;

                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject vendorObj = null;
                try {
                    vendorObj = new JSONObject(rawResponse);
                    String jStatus  = vendorObj.optString("message");
                    JSONArray jResult = vendorObj.optJSONArray("result");

                    if(jStatus.equals("success")){
                        int emptyTablePromo = getContentResolver().delete(PromosiProvider.CONTENT_URI, null, null);
                        for(int i = 0; i < jResult.length(); i ++){
                            ContentValues values  = new ContentValues();
                            JSONObject jsonObject = jResult.optJSONObject(i);

                            JSONObject idObj  = (JSONObject) jsonObject.get("_id");
                            strMongoId        = (String) idObj.get("$id");
                            code              = jsonObject.optString("code");
                            nama_promosi      = jsonObject.optString("description");
                            from_             = jsonObject.optString("start_date");
                            to_               = jsonObject.optString("end_date");
                            loc_code          = jsonObject.optString("location");
                            jenis_promo       = jsonObject.optString("type");

                            values.put(PromosiProvider.KEY_CODE, code);
                            values.put(PromosiProvider.KEY_KODE_PROMO, strMongoId);
                            values.put(PromosiProvider.KEY_NAMA_PROMO, nama_promosi);
                            values.put(PromosiProvider.KEY_FROM, from_);
                            values.put(PromosiProvider.KEY_TO, to_);
                            values.put(PromosiProvider.KEY_LOC_CODE, loc_code);
                            values.put(PromosiProvider.KEY_JENIS_PROMO, jenis_promo);

                            Uri uri = getContentResolver().insert(PromosiProvider.CONTENT_URI, values);
                            System.out.println("insert ke promo" + i);
                        }

                        if(catLoadingPromo!=null)catLoadingPromo.dismiss();
                        getAllPromoBarang();
                    }else if(jStatus.equals("failed")){
                        Utils.showToast("Data Promo Kosong !", HomePointOfSaleActivity.this);
                    }

                }catch(JSONException e){
                    e.printStackTrace();
                    Utils.showToast("Syncronisasi Promo Failed!", HomePointOfSaleActivity.this);
                    if(catLoadingPromo!=null)catLoadingPromo.dismiss();
                }

                if(catLoadingPromo!=null)catLoadingPromo.dismiss();

            }


            @Override
            public void failure(RetrofitError error) {
                Utils.showToast("Syncronisasi Item Failed!", HomePointOfSaleActivity.this);
                if(catLoadingPromo!=null)catLoadingPromo.dismiss();
            }
        });
    }



    private void getAllPromoBarang(){

        catLoadingPromoBarang = new CatLoadingView();
        catLoadingPromoBarang.show(getSupportFragmentManager(), "");

        ApiConnection.getViewDataPromoBarangInterface().getViewDataPromoBarang(Constants.userInfoModel, new Callback<Response>(){
            @Override
            public void success(Response response, Response response2){
                String  kode_promosi, kode_item, item_name, discount_percent, discount_amount, qty, barcode;

                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject vendorObj = null;
                try{
                    vendorObj = new JSONObject(rawResponse);
                    String jStatus  = vendorObj.optString("message");
                    JSONArray jResult = vendorObj.optJSONArray("result");

                    if(jStatus.equals("success")){
                        int emptyTablePromo = getContentResolver().delete(PromosiBarangProvider.CONTENT_URI, null, null);

                        for(int i = 0; i < jResult.length(); i ++){
                            ContentValues values  = new ContentValues();
                            JSONObject jsonObject = jResult.optJSONObject(i);

                            kode_promosi     = jsonObject.optString("promo");
                            kode_item        = jsonObject.optString("item");
                            qty              = jsonObject.optString("qty");
                            item_name        = jsonObject.optString("item_name");
                            discount_amount  = jsonObject.optString("discount_amount");
                            discount_percent = jsonObject.optString("discount_percent");
                            barcode          = jsonObject.optString("barcode");

                            values.put(PromosiBarangProvider.KEY_KODE_PROMO, kode_promosi);
                            values.put(PromosiBarangProvider.KEY_KODE_ITEM, kode_item);
                            values.put(PromosiBarangProvider.KEY_ITEM_NAME, item_name);
                            values.put(PromosiBarangProvider.KEY_BARCODE, barcode);

                            if(!qty.toString().equals("")){
                                values.put(PromosiBarangProvider.KEY_QTY_BARANG, Double.parseDouble(qty));
                            }else{
                                values.put(PromosiBarangProvider.KEY_QTY_BARANG, 0);
                            }

                            if(!discount_percent.toString().equals("")){
                                values.put(PromosiBarangProvider.KEY_DISCOUNT_PERCENT, Double.parseDouble(discount_percent));
                            }else{
                                values.put(PromosiBarangProvider.KEY_DISCOUNT_PERCENT, 0);
                            }

                            if(!discount_amount.toString().equals("")){
                                values.put(PromosiBarangProvider.KEY_DISCOUNT_AMOUNT, Double.parseDouble(discount_amount));
                            }else{
                                values.put(PromosiBarangProvider.KEY_DISCOUNT_AMOUNT, 0);
                            }

                            System.out.println("insert ke promo barang : " + kode_promosi + " == " + i);
                            Uri uri = getContentResolver().insert(PromosiBarangProvider.CONTENT_URI, values);
                        }

                        if(catLoadingPromoBarang != null)catLoadingPromoBarang.dismiss();
                        getAllPromoHadiah();

                    }else if(jStatus.equals("failed")){
                        Utils.showToast("Data Promo Barang Kosong !", HomePointOfSaleActivity.this);
                    }

                }catch(JSONException e){
                    e.printStackTrace();
                    Utils.showToast("Syncronisasi Promo Barang Failed!", HomePointOfSaleActivity.this);
                    catLoadingPromoBarang.dismiss();
                }

                if (catLoadingPromoBarang!=null)catLoadingPromoBarang.dismiss();
            }


            @Override
            public void failure(RetrofitError error) {
                Utils.showToast("Syncronisasi Promo Barang Failed!", HomePointOfSaleActivity.this);
                catLoadingPromoBarang.dismiss();
            }
        });
    }



    private void getAllPromoHadiah(){

        catLoadingPromoHadiah = new CatLoadingView();
        catLoadingPromoHadiah.show(getSupportFragmentManager(), "");
        ApiConnection.getViewDataPromoHadiahInterface().getViewDataPromoHadiah(Constants.userInfoModel, new Callback<Response>(){
            @Override
            public void success(Response response, Response response2){

                String  kode_promosi, kode_item, item_name, barcode, qty;

                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject vendorObj = null;
                try{
                    vendorObj = new JSONObject(rawResponse);
                    String jStatus  = vendorObj.optString("message");
                    JSONArray jResult = vendorObj.optJSONArray("result");

                    if(jStatus.equals("success")){
                        int emptyTablePromo = getContentResolver().delete(PromosiHadiahProvider.CONTENT_URI, null, null);

                        for(int i = 0; i < jResult.length(); i ++){
                            ContentValues values  = new ContentValues();
                            JSONObject jsonObject = jResult.optJSONObject(i);

                            kode_promosi    = jsonObject.optString("promo");
                            kode_item       = jsonObject.optString("item");
                            qty             = jsonObject.optString("qty");
                            item_name       = jsonObject.optString("name");
                            barcode         = jsonObject.optString("barcode");

                            values.put(PromosiHadiahProvider.KEY_KODE_PROMO, kode_promosi);
                            values.put(PromosiHadiahProvider.KEY_KODE_ITEM, kode_item);
                            values.put(PromosiHadiahProvider.KEY_ITEM_NAME, item_name);
                            values.put(PromosiHadiahProvider.KEY_BARCODE, barcode);

                            if(!qty.equals("")){
                                values.put(PromosiHadiahProvider.KEY_QTY_HADIAH, Double.parseDouble(qty));
                            }

                            System.out.println("insert ke promo hadiah" + i);
                            Uri uri = getContentResolver().insert(PromosiHadiahProvider.CONTENT_URI, values);
                        }

                        if(catLoadingPromoHadiah!=null)catLoadingPromoHadiah.dismiss();
                        getDataTax();

                    }else if(jStatus.equals("failed")){
                        Utils.showToast("Data Promo Hadiah Kosong !", HomePointOfSaleActivity.this);
                    }

                }catch(JSONException e){
                    e.printStackTrace();
                    Utils.showToast("Syncronisasi Promo Hadiah Failed!", HomePointOfSaleActivity.this);
                    if (catLoadingPromoHadiah!=null)catLoadingPromoHadiah.dismiss();
                }

                if (catLoadingPromoHadiah!=null)catLoadingPromoHadiah.dismiss();
            }


            @Override
            public void failure(RetrofitError error) {
                Utils.showToast("Syncronisasi Promo Hadiah Failed!", HomePointOfSaleActivity.this);
                if (catLoadingPromoHadiah!=null)catLoadingPromoHadiah.dismiss();
            }
        });
    }


    private void getDataTax(){
        catLoadingTax = new CatLoadingView();
        catLoadingTax.show(getSupportFragmentManager(), "");
        ApiConnection.getTaxPosInterface().getDataTaxPos(Constants.userInfoModel, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2){
                String id_tax, id_tax_group, id_group, desc_tax_group, description, rate;
                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject vendorObj = null;

                try{
                    vendorObj = new JSONObject(rawResponse);
                    String jStatus  = vendorObj.optString("message");
                    JSONArray jResult = vendorObj.optJSONArray("result");

                    if(jStatus.equals("success")){
                        int emptyTableTax = getContentResolver().delete(TaxProvider.CONTENT_URI, null, null);
                        for(int i = 0; i < jResult.length(); i ++){
                            ContentValues values  = new ContentValues();
                            JSONObject jsonObject = jResult.optJSONObject(i);

                            id_tax        = jsonObject.optString("id_tax");
                            id_tax_group  = jsonObject.optString("id_tax_group");
                            description   = jsonObject.optString("description");
                            rate          = jsonObject.optString("rate");

                            values.put(TaxProvider.KEY_ID_TAX, id_tax);
                            values.put(TaxProvider.KEY_ID_TAX_GROUP, id_tax_group);
                            values.put(TaxProvider.KEY_DESCRIPTION, description);
                            values.put(TaxProvider.KEY_RATE, rate);
                            Uri uri = getContentResolver().insert(TaxProvider.CONTENT_URI, values);
                        }

                        showDialogStartEndDay("Syncron Data Success!");
                        if(catLoadingTax!=null)catLoadingTax.dismiss();

                    }else if(jStatus.equals("failed")){
                        Utils.showToast("Syncron Data Tax Failed!", HomePointOfSaleActivity.this);
                    }

                }catch(JSONException e){
                    e.printStackTrace();
                    if(catLoadingTax!=null)catLoadingTax.dismiss();
                }

                if(catLoadingTax!=null)catLoadingTax.dismiss();
            }


            @Override
            public void failure(RetrofitError error) {
                if(catLoadingTax!=null)catLoadingTax.dismiss();
            }
        });
    }

    private void showDialogStartEndDay(String message){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int id){
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }




    private void showDialogConnection(String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int id){
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }



    private void getDataJualBayar(){

        int status_sinc;
        double total_bayar; double kembalian;
        String id_jual_bayar, tipe, bank_id, bank_name, no_kartu;

        String URL = "content://com.trimitrasis.finosapps.ContentProvider.TransJualBayarProvider/transjualbayar";
        Uri uri_   = Uri.parse(URL);
        Cursor c   = getContentResolver().query(uri_, null, "status_sinc = " + 0, null, "id_bayar_item ASC");

        if(c.moveToFirst()){
            do{
                id_jual_bayar  = (!c.getString(c.getColumnIndex(TransJualBayarProvider.KEY_ID_JUAL_BAYAR)).equals("") ? c.getString(c.getColumnIndex(TransJualBayarProvider.KEY_ID_JUAL_BAYAR)) : "");
                tipe           = (!c.getString(c.getColumnIndex(TransJualBayarProvider.KEY_TIPE)).equals("") ? c.getString(c.getColumnIndex(TransJualBayarProvider.KEY_TIPE)) : "");
                bank_id        = (!c.getString(c.getColumnIndex(TransJualBayarProvider.KEY_BANK_ID)).equals("") ? c.getString(c.getColumnIndex(TransJualBayarProvider.KEY_BANK_ID)) : "");
                bank_name      = (!c.getString(c.getColumnIndex(TransJualBayarProvider.KEY_BANK_NAME)).equals("") ? c.getString(c.getColumnIndex(TransJualBayarProvider.KEY_BANK_NAME)) : "");
                no_kartu       = (!c.getString(c.getColumnIndex(TransJualBayarProvider.KEY_NO_KARTU)).equals("") ? c.getString(c.getColumnIndex(TransJualBayarProvider.KEY_NO_KARTU)) : "");
                total_bayar    = (!c.getString(c.getColumnIndex(TransJualBayarProvider.KEY_TOTAL_BAYAR)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(TransJualBayarProvider.KEY_TOTAL_BAYAR))) : 0;
                kembalian      = (!c.getString(c.getColumnIndex(TransJualBayarProvider.KEY_KEMBALIAN)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(TransJualBayarProvider.KEY_KEMBALIAN))) : 0;
                status_sinc    = (!c.getString(c.getColumnIndex(TransJualBayarProvider.KEY_STATUS_SINC)).equals("")) ? Integer.parseInt(c.getString(c.getColumnIndex(TransJualBayarProvider.KEY_STATUS_SINC))) : 0;

                transaksiJualBayarModels.add(new TransaksiJualBayarModel(
                        id_jual_bayar, tipe, bank_id, bank_name, no_kartu, status_sinc, total_bayar,
                        Constants.userInfoModel.getLocation(), kembalian
                ));

            }while (c.moveToNext());
            c.close();
        }else{
            Utils.showLogI("Data jual bayar dblocal Kosong!");
        }
    }


    private void getDataTransFromLocal(){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String date_trans = dateFormat.format(date);

        String _id = "", id_jual = "", id_member = "", id_kasir = "", tanggal = "", shift_id = "", id_reference = "", time = "", detail_id = "";
        double subtotal = 0.0, tax = 0.0, total_discount = 0.0, total_bayar = 0.0, kembalian = 0.0;

        String id_jual_item = "", id_jual_detail = "", item_code = "", description = "", satuan = "", promo = "", barcode = "", tax_type = "";
        double harga = 0.0, hargaMember = 0.0, qty = 0.0, discount = 0.0, total = 0.0, disc_percent = 0.0, disc_amount = 0.0, hargaHpp = 0.0;

        transaksiPenjualanModels.clear();
        String URL = "content://com.trimitrasis.finosapps.ContentProvider.TransPenjualanProvider/transpenjualan";
        Uri uri_   = Uri.parse(URL);
        Cursor c   = getContentResolver().query(uri_, null, "status_sinc = " + 0, null, "_id ASC");

        if(c.moveToFirst()){

            do{
                _id             = (!c.getString(c.getColumnIndex(TransJualProvider.KEY_ID)).equals("") ? c.getString(c.getColumnIndex(TransJualProvider.KEY_ID)) : "");
                id_jual         = (!c.getString(c.getColumnIndex(TransJualProvider.KEY_ID_JUAL)).equals("") ? c.getString(c.getColumnIndex(TransJualProvider.KEY_ID_JUAL)) : "");
                id_kasir        = (!c.getString(c.getColumnIndex(TransJualProvider.KEY_ID_KASIR)).equals("") ? c.getString(c.getColumnIndex(TransJualProvider.KEY_ID_KASIR)) : "");
                tanggal         = (!c.getString(c.getColumnIndex(TransJualProvider.KEY_TANGGAL)).equals("") ? c.getString(c.getColumnIndex(TransJualProvider.KEY_TANGGAL)) : "");
                time            = (!c.getString(c.getColumnIndex(TransJualProvider.KEY_TIME)).equals("") ? c.getString(c.getColumnIndex(TransJualProvider.KEY_TIME)) : "");
                subtotal        = (!c.getString(c.getColumnIndex(TransJualProvider.KEY_SUBTOTAL)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(TransJualProvider.KEY_SUBTOTAL))) : 0;
                tax             = (!c.getString(c.getColumnIndex(TransJualProvider.KEY_TAX)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(TransJualProvider.KEY_TAX))) : 0;
                total_discount  = (!c.getString(c.getColumnIndex(TransJualProvider.KEY_TOTAL_DISCOUNT)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(TransJualProvider.KEY_TOTAL_DISCOUNT))) : 0;
                kembalian       = (!c.getString(c.getColumnIndex(TransJualProvider.KEY_KEMBALIAN)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(TransJualProvider.KEY_KEMBALIAN))) : 0;
                total_bayar     = (!c.getString(c.getColumnIndex(TransJualProvider.KEY_TOTAL_BAYAR)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(TransJualProvider.KEY_TOTAL_BAYAR))) : 0;
                detail_id       = (!c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_DETAIL_ID)).equals("")) ? c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_DETAIL_ID)) : "";
                id_jual_detail  = (!c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_ID_JUAL_DETAIL)).equals("")) ? c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_ID_JUAL_DETAIL)) : "";
                item_code       = (!c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_ITEM_CODE)).equals("")) ? c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_ITEM_CODE)) : "";
                description     = (!c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_DESCRIPTION)).equals("")) ? c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_DESCRIPTION)) : "";
                harga           = (!c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_HARGA)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_HARGA))) : 0;
                hargaMember     = (!c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_HARGA_MEMBER)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_HARGA_MEMBER))) : 0;
                qty             = (!c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_QTY)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_QTY))) : 0;
                satuan          = (!c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_SATUAN)).equals("")) ? c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_SATUAN)) : "";
                promo           = (!c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_PROMO)).equals("")) ? c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_PROMO)) : "";
                barcode         = (!c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_BARCODE)).equals("")) ? c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_BARCODE)) : "";
                disc_percent    = (!c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_DISC_PERCENT)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_DISC_PERCENT))) : 0;
                disc_amount     = (!c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_DISC_AMOUNT)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_DISC_AMOUNT))) : 0;
                tax_type        = (!c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_TAX_TYPE)).equals("")) ? c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_TAX_TYPE)) : "";
                hargaHpp        = (!c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_HARGA_HPP)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_HARGA_HPP))) : 0;
                discount        = (!c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_DISCOUNT)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_DISCOUNT))) : 0;
                total           = (!c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_TOTAL)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_TOTAL))) : 0;

                transaksiPenjualanModels.add(new TransaksiPenjualanModel(
                        id_jual, id_member, id_kasir, tanggal, time, subtotal, tax, total_discount, total_bayar, shift_id,
                        id_reference, kembalian, id_jual_detail, item_code, description, harga, hargaMember, qty,
                        satuan, discount,total, promo, barcode, disc_percent, disc_amount, tax_type, hargaHpp, Constants.userInfoModel.getLocation(), detail_id
                ));

            }while(c.moveToNext());
            c.close();

        }else{
            Utils.showLogI("Data penjualan dblocal Kosong!");
        }


        if(Utils.cek_status(this)){
            postTransaksiManual();
        }else{
            showDialogConnection("Error Connection!");
        }

    }


    private SyncTransModel getSyncTrans(){
        SyncTransModel syncTransModel = new SyncTransModel(
                Constants.userInfoModel,
                transaksiPenjualanModels,
                transaksiJualBayarModels
        );
        return syncTransModel;
    }



    private void postTransaksiManual(){

        catLoadingTax = new CatLoadingView();
        catLoadingTax.show(getSupportFragmentManager(), "");
        ApiConnection.getSyncTransInterface().getSyncTrans(getSyncTrans(), new Callback<Response>(){
            @Override
            public void success(Response response, Response response2){
                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject sosObj = null;
                try{
                    sosObj = new JSONObject(rawResponse);
                    String jStatus  = sosObj.optString("message");

                    if(jStatus.equals("success")){
                        updateDataJual();
                        updateDataJualBayar();
                    }

                }catch(JSONException e){
                    e.printStackTrace();
                    catLoadingTax.dismiss();
                }

                if (catLoadingTax!=null)catLoadingTax.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                if (catLoadingTax!=null)catLoadingTax.dismiss();
            }

        });
    }


    private void showDialogSyncTrans(String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int id){
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }



    private void updateDataJualBayar(){

        int status_sync  = 0; int _id = 0;
        String URL = "content://com.trimitrasis.finosapps.ContentProvider.TransJualBayarProvider/transjualbayar";
        Uri uri_   = Uri.parse(URL);
        Cursor c   = getContentResolver().query(uri_, null, "status_sinc = " + 0, null, "id_bayar_item ASC");

        if(c.moveToFirst()){

            do{
                status_sync = Integer.parseInt(c.getString(c.getColumnIndex(TransJualBayarProvider.KEY_STATUS_SINC)));
                _id         = Integer.parseInt(c.getString(c.getColumnIndex(TransJualBayarProvider.KEY_ID_BAYAR_ITEM)));

                if(status_sync == 0){
                    ContentValues values  = new ContentValues();
                    values.put(TransJualBayarProvider.KEY_STATUS_SINC, 1);
                    int uri = getContentResolver().update(TransJualBayarProvider.CONTENT_URI, values, "id_bayar_item " + " = '" +  _id + "'", null);
                }

            }while (c.moveToNext());

            c.close();
        }

    }

    private void updateDataJual(){

        int status_sync  = 0; int _id = 0;
        String URL = "content://com.trimitrasis.finosapps.ContentProvider.TransJualProvider/transjual";
        Uri uri_   = Uri.parse(URL);
        Cursor c   = getContentResolver().query(uri_, null, "status_sinc = " + 0, null, "_id ASC");

        if(c.moveToFirst()) {

            do{
                status_sync = Integer.parseInt(c.getString(c.getColumnIndex(TransJualProvider.KEY_STATUS_SINC)));
                _id         = Integer.parseInt(c.getString(c.getColumnIndex(TransJualProvider.KEY_ID)));

                if(status_sync == 0){
                    ContentValues values  = new ContentValues();
                    values.put(TransJualProvider.KEY_STATUS_SINC, 1);
                    int uri = getContentResolver().update(TransJualProvider.CONTENT_URI, values, "_id " + " = '" +  _id + "'", null);
                }

            }while(c.moveToNext());
            c.close();

        }

        showDialogSyncTrans("Berhasil melakukan Sync Transaksi!");

    }

}
