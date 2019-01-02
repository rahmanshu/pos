package com.trimitrasis.finosapps.Views.PosView;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.gson.Gson;
import com.trimitrasis.finosapps.Connection.ApiConnection;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.ContentProvider.BomProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.CategoryProductProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.ItemProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.PromosiBarangProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.PromosiHadiahProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.PromosiProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.TaxGroupProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.TaxProvider;
import com.trimitrasis.finosapps.Views.PosView.Model.StartOfDayModel;
import com.trimitrasis.finosapps.Views.UtilView.Constants;
import com.trimitrasis.finosapps.Views.UtilView.Utils;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rahman on 08/04/2017.
 */

@EActivity(R.layout.layout_start_of_day)
public class StartOfDayActivity extends AppCompatActivity {

    @ViewById
    Toolbar toolbar;

    @ViewById
    Button btnStartOfDay;

    @ViewById
    EditText textLocation, textDeviceId;

    ProgressDialog progressDialog;


    @AfterViews
    void afterView(){
        initTabbar();
        customView();
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
        toolbarView.setText("Start Of Day");
        textLocation.setText(Constants.userInfoModel.getLocationName());
        textLocation.setEnabled(false);
        textDeviceId.setText(Utils.getDevId(this));
        textDeviceId.setEnabled(false);
        btnStartOfDay.requestFocus();
    }



    @OptionsItem(android.R.id.home)
    void homeClick(){
        onBackPressed();
    }


    @Click
    void btnStartOfDay(){

        if(isValidInput()) {

            if(Utils.cek_status(this)){

                int orientation=this.getResources().getConfiguration().orientation;
                if(orientation== Configuration.ORIENTATION_PORTRAIT){
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }else{
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }

                startThread();

            } else {
                showDialogConnection("Error Connection!");
            }
        }
    }


    public void startThread() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.show();

        new Thread() {
            public void run() {
                try {
                    sleep(4000);
                    getCategoryProduct();

                } catch(Exception e) {
                    Log.e("threadmessage",e.getMessage());
                }
            }
        }.start();
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


    private void getCategoryProduct(){
        ApiConnection.getDataItemHierarchyInterface().getItemHierarchy(Constants.userInfoModel, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject vendorObj = null;
                try {
                    vendorObj = new JSONObject(rawResponse);
                    String jStatus    = vendorObj.optString("message");
                    JSONArray jResult = vendorObj.optJSONArray("data");

                    if(jStatus.equals("success")){
                        int emptyTableCategory = getContentResolver().delete(CategoryProductProvider.CONTENT_URI, null, null);
                        if(jResult != null) {
                            for (int i = 0; i < jResult.length(); i++) {
                                ContentValues values = new ContentValues();
                                JSONObject jsonObject = jResult.optJSONObject(i);

                                JSONObject idObj = (JSONObject) jsonObject.get("_id");
                                String idCat     = (String) idObj.get("$id");
                                String desc      = jsonObject.optString("description");

                                values.put(CategoryProductProvider.KEY_ID_CATEGORY, idCat);
                                values.put(CategoryProductProvider.KEY_DESCRIPTION, desc);
                                Uri uri = getContentResolver().insert(CategoryProductProvider.CONTENT_URI, values);
                            }
                        }

                    }else if(jStatus.equals("failed")){
                        int emptyTableCategory = getContentResolver().delete(CategoryProductProvider.CONTENT_URI, null, null);
                        Utils.showLogI("Syncronisasi category Empty!");
                    }

                }catch(JSONException e){
                    e.printStackTrace();
                    Utils.showLogI("Error JSON : " + e.getMessage());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Utils.showLogI("Error retrofit : " + error.getMessage());
            }
        });

        getAllBom();
    }


    private void getAllBom(){
        ApiConnection.getViewDataBomInterface().getAllDataBom(Constants.userInfoModel, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {

                String itemId = "", itemName = "", itemVar = "", itemBahan = "", qty = "";
                String uom = "", uomName = "", itemBahanName = "";
                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject vendorObj = null;

                try{
                    vendorObj         = new JSONObject(rawResponse);
                    String jStatus    = vendorObj.optString("message");
                    JSONArray jResult = vendorObj.optJSONArray("result");

                    if(jStatus.equals("success")){

                        int emptyTableBom = getContentResolver().delete(BomProvider.CONTENT_URI, null, null);
                        if(jResult != null){
                            for(int i = 0; i < jResult.length(); i++){

                                ContentValues values   = new ContentValues();
                                JSONObject jsonObject  = jResult.optJSONObject(i);
                                JSONArray jsonArrItemDetail = jsonObject.optJSONArray("detail");

                                itemId             = jsonObject.optString("item_id");
                                itemVar            = jsonObject.optString("item_var");
                                itemName           = jsonObject.optString("item_name");

                                if(jsonArrItemDetail != null){
                                    for (int j = 0; j < jsonArrItemDetail.length(); j++) {
                                        JSONObject jsonObjectItem = jsonArrItemDetail.optJSONObject(j);

                                        itemBahan             = jsonObjectItem.optString("item_bahan");
                                        qty                   = jsonObjectItem.optString("qty");
                                        uom                   = jsonObjectItem.optString("uom");
                                        uomName               = jsonObjectItem.optString("uom_name");
                                        itemBahanName         = jsonObjectItem.optString("item_bahan_name");

                                        values.put(BomProvider.KEY_ITEM_ID, itemId);
                                        values.put(BomProvider.KEY_ITEM_VAR, itemVar);
                                        values.put(BomProvider.KEY_ITEM_NAME, itemName);
                                        values.put(BomProvider.KEY_ITEM_BAHAN, itemBahan);
                                        values.put(BomProvider.KEY_QTY, qty);
                                        values.put(BomProvider.KEY_UOM, uom);
                                        values.put(BomProvider.KEY_UOM_NAME, uomName);
                                        values.put(BomProvider.KEY_ITEM_BAHAN_NAME, itemBahanName);
                                        Uri uri = getContentResolver().insert(BomProvider.CONTENT_URI, values);
                                    }
                                }
                            }
                        }

                    }else if(jStatus.equals("failed")){
                        int emptyTableProduk = getContentResolver().delete(BomProvider.CONTENT_URI, null, null);
                        Utils.showLogI("Syncronisasi Bom Empty!");
                    }

                }catch(JSONException e){
                    e.printStackTrace();
                    Utils.showLogI("Error Json : " + e.getMessage());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Utils.showLogI("Error retrofit : " + error.getMessage());
            }
        });

        getAllProduk();
    }



    private void getAllProduk(){
        ApiConnection.getViewDataItemInterface().getAllDataProduct(Constants.userInfoModel, new Callback<Response>(){
            @Override
            public void success(Response response, Response response2){

                double uom_convertion, base_uom_convertion, harga_jual;
                boolean flag_qty, flag_sell, flag_bom;
                String fileExt = "", file_name = "", fullPath = "",  filePath = "";
                String id = "", s_description = "", base_uom = "", item_group = "", detail_id = "";
                String item_hierarchy = "", uom = "", uom_name = "",  barcode = "", tax_group = "";
                String strMongoId = "", item_hierarchy_cat = "", standart_cost = "";
                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject vendorObj = null;

                try{
                    vendorObj         = new JSONObject(rawResponse);
                    String jStatus    = vendorObj.optString("message");
                    JSONArray jResult = vendorObj.optJSONArray("result");

                    if(jStatus.equals("success")){

                        int emptyTableProduk = getContentResolver().delete(ItemProvider.CONTENT_URI, null, null);
                        if(jResult != null){
                            for(int i = 0; i < jResult.length(); i++){

                                ContentValues values   = new ContentValues();
                                JSONObject jsonObject  = jResult.optJSONObject(i);
                                JSONArray jsonArrItemConvertion = jsonObject.optJSONArray("item_convertion");
                                JSONObject jsonObjItemImage = jsonObject.optJSONObject("main_image");

                                JSONObject idObj   = (JSONObject) jsonObject.get("_id");
                                strMongoId         = (String) idObj.get("$id");
                                id                 = jsonObject.optString("id");
                                s_description      = jsonObject.optString("s_description");
                                base_uom           = jsonObject.optString("base_uom");
                                item_group         = jsonObject.optString("item_group");
                                item_hierarchy     = jsonObject.optString("item_hierarchy");
                                tax_group          = jsonObject.optString("tax_group");
                                standart_cost      = jsonObject.optString("standart_cost");
                                flag_qty           = jsonObject.optBoolean("flag_qty");
                                flag_sell          = jsonObject.optBoolean("flag_sell");
                                flag_bom           = jsonObject.optBoolean("flag_bom");
                                item_hierarchy_cat = jsonObject.optString("item_hierarchy_ancestor");

                                if(jsonObjItemImage != null){
                                    fileExt   = jsonObjItemImage.optString("file_ext");
                                    file_name = jsonObjItemImage.optString("file_name");
                                    fullPath  = jsonObjItemImage.optString("full_path");
                                    filePath  = jsonObjItemImage.optString("file_path");
                                }else{
                                    fileExt   = "";
                                    file_name = "";
                                    fullPath  = "";
                                    filePath  = "";
                                }

                                if(jsonArrItemConvertion != null){
                                    for (int j = 0; j < jsonArrItemConvertion.length(); j++) {
                                        JSONObject jsonObjectItem = jsonArrItemConvertion.optJSONObject(j);

                                        JSONObject detail_id_ = (JSONObject) jsonObjectItem.get("id");
                                        detail_id             = (String) detail_id_.get("$id");
                                        uom                   = jsonObjectItem.optString("uom");
                                        uom_name              = jsonObjectItem.optString("uom_name");
                                        uom_convertion        = jsonObjectItem.optDouble("uom_convertion");
                                        base_uom_convertion   = jsonObjectItem.optDouble("base_uom_convertion");
                                        harga_jual            = jsonObjectItem.optDouble("harga_jual");
                                        barcode               = jsonObjectItem.optString("barcode");

                                        values.put(ItemProvider.KEY_KODE_ITEM, strMongoId);
                                        values.put(ItemProvider.KEY_STOCK_ID, id);
                                        values.put(ItemProvider.KEY_DESCRIPTION, s_description);
                                        values.put(ItemProvider.KEY_BASE_UOM, base_uom);
                                        values.put(ItemProvider.KEY_ITEM_GROUP, item_group);
                                        values.put(ItemProvider.KEY_ITEM_HIERARCHY, item_hierarchy);
                                        values.put(ItemProvider.KEY_TAX_GROUP, tax_group);
                                        values.put(ItemProvider.KEY_DETAIL_ID, detail_id);
                                        values.put(ItemProvider.KEY_UOM, uom);
                                        values.put(ItemProvider.KEY_UOM_NAME, uom_name);
                                        values.put(ItemProvider.KEY_UOM_CONVERTION, uom_convertion);
                                        values.put(ItemProvider.KEY_BASE_UOM_CONVERTION, base_uom_convertion);
                                        values.put(ItemProvider.KEY_HARGA_JUAL, harga_jual);
                                        values.put(ItemProvider.KEY_BARCODE, barcode);
                                        values.put(ItemProvider.KEY_STANDART_COST, standart_cost);
                                        values.put(ItemProvider.KEY_FLAG_QTY, flag_qty);
                                        values.put(ItemProvider.KEY_FLAG_SELL, flag_sell);
                                        values.put(ItemProvider.KEY_FLAG_BOM, flag_bom);
                                        values.put(ItemProvider.KEY_ITEM_HIERARCHY_CAT, item_hierarchy_cat);
                                        values.put(ItemProvider.KEY_FILE_EXT, fileExt);
                                        values.put(ItemProvider.KEY_FILE_NAME, file_name);
                                        values.put(ItemProvider.KEY_FULL_PATH, fullPath);
                                        values.put(ItemProvider.KEY_FILE_PATH, filePath);
                                        Uri uri = getContentResolver().insert(ItemProvider.CONTENT_URI, values);
                                    }
                                }
                            }
                        }

                    }else if(jStatus.equals("failed")){
                        int emptyTableProduk = getContentResolver().delete(ItemProvider.CONTENT_URI, null, null);
                        Utils.showLogI("Syncronisasi Item Product Empty!");
                    }

                }catch(JSONException e){
                    e.printStackTrace();
                    Utils.showLogI("Error Json : " + e.getMessage());
                }

            }


            @Override
            public void failure(RetrofitError error){
                Utils.showLogI("Error retrofit : " + error.getMessage());
            }
        });

        getAllPromo();
    }



    private void getAllPromo(){
        ApiConnection.getViewDataPromoInterface().getViewDataPromo(Constants.userInfoModel, new Callback<Response>(){
            @Override
            public void success(Response response, Response response2){
                String  nama_promosi = "", from_ = "", to_ = "", loc_code = "";
                String  jenis_promo = "", code = "", strMongoId = "";
                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject vendorObj = null;
                try {
                    vendorObj = new JSONObject(rawResponse);
                    String jStatus  = vendorObj.optString("message");
                    JSONArray jResult = vendorObj.optJSONArray("result");

                    if(jStatus.equals("success")){
                        int emptyTablePromo = getContentResolver().delete(PromosiProvider.CONTENT_URI, null, null);
                        if(jResult != null) {
                            for (int i = 0; i < jResult.length(); i++) {
                                ContentValues values = new ContentValues();
                                JSONObject jsonObject = jResult.optJSONObject(i);

                                JSONObject idObj = (JSONObject) jsonObject.get("_id");
                                strMongoId       = (String) idObj.get("$id");
                                code             = jsonObject.optString("code");
                                nama_promosi     = jsonObject.optString("description");
                                from_            = jsonObject.optString("start_date");
                                to_              = jsonObject.optString("end_date");
                                loc_code         = jsonObject.optString("location");
                                jenis_promo      = jsonObject.optString("type");

                                values.put(PromosiProvider.KEY_CODE, code);
                                values.put(PromosiProvider.KEY_KODE_PROMO, strMongoId);
                                values.put(PromosiProvider.KEY_NAMA_PROMO, nama_promosi);
                                values.put(PromosiProvider.KEY_FROM, from_);
                                values.put(PromosiProvider.KEY_TO, to_);
                                values.put(PromosiProvider.KEY_LOC_CODE, loc_code);
                                values.put(PromosiProvider.KEY_JENIS_PROMO, jenis_promo);

                                Uri uri = getContentResolver().insert(PromosiProvider.CONTENT_URI, values);
                            }
                        }

                    }else if(jStatus.equals("failed")){
                        int emptyTablePromo = getContentResolver().delete(PromosiProvider.CONTENT_URI, null, null);
                        Utils.showLogI("Syncronisasi promo Empty!");
                    }



                }catch(JSONException e){
                    e.printStackTrace();
                    Utils.showLogI("Error JSON : " + e.getMessage());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Utils.showLogI("Error Retrofit : " + error.getMessage());
            }
        });

        getAllPromoBarang();
    }




    private void getAllPromoBarang(){

        ApiConnection.getViewDataPromoBarangInterface().getViewDataPromoBarang(Constants.userInfoModel, new Callback<Response>(){
            @Override
            public void success(Response response, Response response2){

                String kode_promosi = "", kode_item = "", item_name = "", discount_percent = "";
                String discount_amount = "", qty = "", barcode = "";
                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject vendorObj = null;
                try{

                    vendorObj       = new JSONObject(rawResponse);
                    String jStatus  = vendorObj.optString("message");
                    JSONArray jResult = vendorObj.optJSONArray("result");

                    if(jStatus.equals("success")){
                        int emptyTablePromo = getContentResolver().delete(PromosiBarangProvider.CONTENT_URI, null, null);
                        if(jResult != null){
                            for (int i = 0; i < jResult.length(); i++) {
                                ContentValues values = new ContentValues();
                                JSONObject jsonObject = jResult.optJSONObject(i);

                                kode_promosi     = jsonObject.optString("promo");
                                kode_item        = jsonObject.optString("item");
                                item_name        = jsonObject.optString("item_name");
                                qty              = jsonObject.optString("qty");
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

                                Uri uri = getContentResolver().insert(PromosiBarangProvider.CONTENT_URI, values);
                            }
                        }

                    }else if(jStatus.equals("failed")){
                        int emptyTablePromo = getContentResolver().delete(PromosiBarangProvider.CONTENT_URI, null, null);
                        Utils.showLogI("Syncronisasi promosi barang Empty!");
                    }


                }catch(JSONException e){
                    e.printStackTrace();
                    Utils.showLogI("Error JSON : " + e.getMessage());
                }
            }


            @Override
            public void failure(RetrofitError error) {
                Utils.showLogI("Error Retrofit : " + error.getMessage());
            }
        });

        getAllPromoHadiah();
    }



    private void getAllPromoHadiah(){

        ApiConnection.getViewDataPromoHadiahInterface().getViewDataPromoHadiah(Constants.userInfoModel, new Callback<Response>(){
            @Override
            public void success(Response response, Response response2){

                String  kode_promosi = "", kode_item = "", item_name = "", barcode = "", qty = "";
                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject vendorObj = null;

                try{
                    vendorObj = new JSONObject(rawResponse);
                    String jStatus  = vendorObj.optString("message");
                    JSONArray jResult = vendorObj.optJSONArray("result");

                    if(jStatus.equals("success")){
                        int emptyTablePromo = getContentResolver().delete(PromosiHadiahProvider.CONTENT_URI, null, null);
                        if(jResult != null){
                            for (int i = 0; i < jResult.length(); i++){

                                ContentValues values = new ContentValues();
                                JSONObject jsonObject = jResult.optJSONObject(i);

                                kode_promosi = jsonObject.optString("promo");
                                kode_item    = jsonObject.optString("item");
                                qty          = jsonObject.optString("qty");
                                item_name    = jsonObject.optString("item_name");
                                barcode      = jsonObject.optString("barcode");

                                values.put(PromosiHadiahProvider.KEY_KODE_PROMO, kode_promosi);
                                values.put(PromosiHadiahProvider.KEY_KODE_ITEM, kode_item);
                                values.put(PromosiHadiahProvider.KEY_ITEM_NAME, item_name);
                                values.put(PromosiHadiahProvider.KEY_BARCODE, barcode);

                                if (!qty.equals("")) {
                                    values.put(PromosiHadiahProvider.KEY_QTY_HADIAH, Double.parseDouble(qty));
                                }

                                Uri uri = getContentResolver().insert(PromosiHadiahProvider.CONTENT_URI, values);
                            }
                        }

                    }else if(jStatus.equals("failed")){
                        int emptyTablePromo = getContentResolver().delete(PromosiHadiahProvider.CONTENT_URI, null, null);
                        Utils.showLogI("Syncronisasi promosi hadiah Empty!");
                    }


                }catch(JSONException e){
                    e.printStackTrace();
                    Utils.showLogI("Error JSON : " + e.getMessage());
                }
            }

            @Override
            public void failure(RetrofitError error){
                Utils.showLogI("Error Retrofit : " + error.getMessage());
            }
        });

        getDataTax();

    }



    private void getDataTax(){

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
                        if(jResult != null){
                            for (int i = 0; i < jResult.length(); i++) {
                                ContentValues values = new ContentValues();
                                JSONObject jsonObject = jResult.optJSONObject(i);

                                id_tax       = jsonObject.optString("id_tax");
                                id_tax_group = jsonObject.optString("id_tax_group");
                                description  = jsonObject.optString("description");
                                rate         = jsonObject.optString("rate");

                                values.put(TaxProvider.KEY_ID_TAX, id_tax);
                                values.put(TaxProvider.KEY_ID_TAX_GROUP, id_tax_group);
                                values.put(TaxProvider.KEY_DESCRIPTION, description);
                                values.put(TaxProvider.KEY_RATE, rate);
                                Uri uri = getContentResolver().insert(TaxProvider.CONTENT_URI, values);
                            }
                        }

                    }else if(jStatus.equals("failed")){
                        int emptyTableTax = getContentResolver().delete(TaxProvider.CONTENT_URI, null, null);
                        Utils.showLogI("Syncronisasi data tax Empty!");
                    }



                }catch(JSONException e){
                    e.printStackTrace();
                    Utils.showLogI("Error JSON : " + e.getMessage());
                }

            }


            @Override
            public void failure(RetrofitError error) {
                Utils.showLogI("Error Retrofit : " + error.getMessage());
            }
        });

        getDataTaxGroupOnly();
    }



    private void getDataTaxGroupOnly(){

        ApiConnection.getTaxGroupPosNewInterface().getDataTaxGroupPosNew(Constants.userInfoModel, new Callback<Response>(){
            @Override
            public void success(Response response, Response response2){
                String id_tax_group, description, strMongoId;
                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject vendorObj = null;

                try{
                    vendorObj = new JSONObject(rawResponse);
                    String jStatus  = vendorObj.optString("message");
                    JSONArray jResult = vendorObj.optJSONArray("result");

                    if(jStatus.equals("success")){

                        int emptyTableTax = getContentResolver().delete(TaxGroupProvider.CONTENT_URI, null, null);
                        if(jResult != null){
                            for (int i = 0; i < jResult.length(); i++) {
                                ContentValues values = new ContentValues();
                                JSONObject jsonObject = jResult.optJSONObject(i);

                                JSONObject idObj = (JSONObject) jsonObject.get("_id");
                                strMongoId       = (String) idObj.get("$id");
                                description      = jsonObject.optString("description");

                                values.put(TaxGroupProvider.KEY_ID_TAX_GROUP, strMongoId);
                                values.put(TaxGroupProvider.KEY_DESCRIPTION, description);
                                Uri uri = getContentResolver().insert(TaxGroupProvider.CONTENT_URI, values);
                            }
                        }

                    }else if(jStatus.equals("failed")){
                        int emptyTableTax = getContentResolver().delete(TaxGroupProvider.CONTENT_URI, null, null);
                        Utils.showLogI("Syncronisasi tax group Empty!");
                    }

                }catch(JSONException e){
                    e.printStackTrace();
                    Utils.showLogI("Error JSON : " + e.getMessage());
                }
            }


            @Override
            public void failure(RetrofitError error) {
                Utils.showLogI("Error Retrofit : " + error.getMessage());
            }
        });

        startOfDay();
    }


    private void startOfDay(){
        ApiConnection.getStartOfDayInterface().startOfDay(getStartOfDayModel(), new Callback<Response>() {
            @Override
            public void success(Response response, Response response2){
                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject vendorObj = null;

                try{
                    vendorObj = new JSONObject(rawResponse);
                    String jStatus  = vendorObj.optString("message");

                    if(jStatus.equals("success")){
                        showDialogStartEndDay("Start Of Day Success");
                    }else if(jStatus.equals("already now")){
                        showDialogStartEndDay("Maaf, Start Of Day sudah dilakukan hari ini!");
                    }else if(jStatus.equals("already yesterday")){
                        showDialogStartEndDay("Maaf, Anda belum melakukan End Of Day!");
                    }else if(jStatus.equals("sync data not yet")){
                        showDialogStartEndDay("Lakukan syncronisasi data terlebih dahulu!");
                    }

                }catch(JSONException e){
                    e.printStackTrace();
                    if(progressDialog != null) progressDialog.dismiss();
                }

                if(progressDialog !=null) progressDialog.dismiss();

            }

            @Override
            public void failure(RetrofitError error){
                if(progressDialog != null) progressDialog.dismiss();
            }

        });

        Thread.currentThread().interrupt();

    }




    private StartOfDayModel getStartOfDayModel(){
        StartOfDayModel startEndDayModel = new StartOfDayModel(
                Constants.userInfoModel.getEmail(),
                Constants.userInfoModel.getLocation(),
                Constants.userInfoModel
        );
        return startEndDayModel;
    }


    private void showDialogStartEndDay(String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int id){
                        dialog.dismiss();
                        finish();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private boolean isValidInput(){
        if (textLocation.getText().toString().isEmpty() || textLocation.getText() == null || textLocation.getText().toString().equals("")){
            Utils.showToast("Location tidak boleh kosong, tolong lakukan konfigurasi location terlebih dahulu!", this);
            return false;
        }else return true;
    }



}
