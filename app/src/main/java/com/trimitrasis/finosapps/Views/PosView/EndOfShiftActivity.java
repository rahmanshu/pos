package com.trimitrasis.finosapps.Views.PosView;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.trimitrasis.finosapps.Connection.ApiConnection;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.ContentProvider.HeaderHoldSalesProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.TransJualBayarProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.TransJualDetailProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.TransJualProvider;
import com.trimitrasis.finosapps.Views.PosView.Model.EndOfShiftModelPost;
import com.trimitrasis.finosapps.Views.PosView.Model.TransaksiJualBayarModel;
import com.trimitrasis.finosapps.Views.PosView.Model.TransaksiPenjualanModel;
import com.trimitrasis.finosapps.Views.PrintView.PrintEndOfShiftActivity_;
import com.trimitrasis.finosapps.Views.UtilView.Constants;
import com.trimitrasis.finosapps.Views.UtilView.Utils;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by rahman on 09/04/2017.
 */


@EActivity(R.layout.layout_end_of_shift)
public class EndOfShiftActivity extends AppCompatActivity {

    ProgressDialog progressDialog;

    @ViewById
    Toolbar toolbar;

    @ViewById
    EditText editIdKasir, editIdMobile, editCashCashier, textLocation;

    @ViewById
    Button btnEndOfShift;

    double cash_cashier = 0;

    ArrayList<TransaksiPenjualanModel> transaksiPenjualanModelArrayList;
    ArrayList<TransaksiJualBayarModel> transaksiJualBayarModels;
    List<String> listHeaderJual;

    @AfterViews
    void afterView(){
        initTabbar();
        customView();
        listHeaderJual = new ArrayList<>();
        listHeaderJual.clear();
        transaksiPenjualanModelArrayList = new ArrayList<>();
        transaksiPenjualanModelArrayList.clear();
        transaksiJualBayarModels = new ArrayList<>();
        transaksiJualBayarModels.clear();
        setoranCashCasirChange();
    }

    private void initTabbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_arrow_left);
    }


    @OptionsItem(android.R.id.home)
    void homeClick(){
        onBackPressed();
    }


    private void customView(){
        TextView toolbarView = (TextView) toolbar.findViewById(R.id.headerText);
        toolbarView.setText("End Of Shift");
        editIdMobile.setText(Utils.getDevId(EndOfShiftActivity.this));
        editIdMobile.setEnabled(false);
        editIdKasir.setText(Constants.userInfoModel.getEmail());
        editIdKasir.setEnabled(false);
        textLocation.setText(Constants.userInfoModel.getLocationName());
        textLocation.setEnabled(false);
        editCashCashier.setFocusableInTouchMode(true);
        editCashCashier.requestFocus();
    }


    @Click
    void btnEndOfShift(){

        if(Utils.cek_status(this)){
            if(isValidInput()){

                int orientation = this.getResources().getConfiguration().orientation;
                if(orientation == Configuration.ORIENTATION_PORTRAIT){
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }else{
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }

                startThread();
            }

        }else{
            showDialogConnection("Error Connection!");
        }
    }



    public void startThread(){

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.show();

        new Thread() {
            public void run() {
                try {
                    sleep(4000);
                    getDataJualBayar();

                } catch(Exception e) {
                    Log.e("threadmessage",e.getMessage());
                }
            }
        }.start();
    }


    private void getDataJualBayar(){

        transaksiJualBayarModels.clear();
        String URL = "content://com.trimitrasis.finosapps.ContentProvider.TransJualBayarProvider/transjualbayar";
        Uri uri_   = Uri.parse(URL);
        Cursor c   = getContentResolver().query(uri_, null, "status_sinc = " + 0, null, "id_bayar_item ASC");

        if(c.moveToFirst()){
            do{
                String id_jual_bayar  = (!c.getString(c.getColumnIndex(TransJualBayarProvider.KEY_ID_JUAL_BAYAR)).equals("") ? c.getString(c.getColumnIndex(TransJualBayarProvider.KEY_ID_JUAL_BAYAR)) : "");
                String tipe           = (!c.getString(c.getColumnIndex(TransJualBayarProvider.KEY_TIPE)).equals("") ? c.getString(c.getColumnIndex(TransJualBayarProvider.KEY_TIPE)) : "");
                String bank_id        = (!c.getString(c.getColumnIndex(TransJualBayarProvider.KEY_BANK_ID)).equals("") ? c.getString(c.getColumnIndex(TransJualBayarProvider.KEY_BANK_ID)) : "");
                String bank_name      = (!c.getString(c.getColumnIndex(TransJualBayarProvider.KEY_BANK_NAME)).equals("") ? c.getString(c.getColumnIndex(TransJualBayarProvider.KEY_BANK_NAME)) : "");
                String no_kartu       = (!c.getString(c.getColumnIndex(TransJualBayarProvider.KEY_NO_KARTU)).equals("") ? c.getString(c.getColumnIndex(TransJualBayarProvider.KEY_NO_KARTU)) : "");
                double total_bayar    = (!c.getString(c.getColumnIndex(TransJualBayarProvider.KEY_TOTAL_BAYAR)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(TransJualBayarProvider.KEY_TOTAL_BAYAR))) : 0;
                double kembalian      = (!c.getString(c.getColumnIndex(TransJualBayarProvider.KEY_KEMBALIAN)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(TransJualBayarProvider.KEY_KEMBALIAN))) : 0;
                int status_sinc       = (!c.getString(c.getColumnIndex(TransJualBayarProvider.KEY_STATUS_SINC)).equals("")) ? Integer.parseInt(c.getString(c.getColumnIndex(TransJualBayarProvider.KEY_STATUS_SINC))) : 0;

                transaksiJualBayarModels.add(new TransaksiJualBayarModel(
                        id_jual_bayar, tipe, bank_id, bank_name, no_kartu, status_sinc, total_bayar, Constants.userInfoModel.getLocation(), kembalian
                ));

            }while (c.moveToNext());
            c.close();

        }else{
            Utils.showLogI("Data jual bayar dblocal Kosong!");
        }

        getDataPenjualan();
    }


    private void getDataPenjualan(){

        String id_member = "",  shift_id = "", id_reference = "";
        transaksiPenjualanModelArrayList.clear();
        String URL = "content://com.trimitrasis.finosapps.ContentProvider.TransPenjualanProvider/transpenjualan";
        Uri uri_   = Uri.parse(URL);
        Cursor c   = getContentResolver().query(uri_, null, "status_sinc = " + 0, null, "_id ASC");

        if(c.moveToFirst()){

            do{
                String _id             = (!c.getString(c.getColumnIndex(TransJualProvider.KEY_ID)).equals("") ? c.getString(c.getColumnIndex(TransJualProvider.KEY_ID)) : "");
                String id_jual         = (!c.getString(c.getColumnIndex(TransJualProvider.KEY_ID_JUAL)).equals("") ? c.getString(c.getColumnIndex(TransJualProvider.KEY_ID_JUAL)) : "");
                String id_kasir        = (!c.getString(c.getColumnIndex(TransJualProvider.KEY_ID_KASIR)).equals("") ? c.getString(c.getColumnIndex(TransJualProvider.KEY_ID_KASIR)) : "");
                String tanggal         = (!c.getString(c.getColumnIndex(TransJualProvider.KEY_TANGGAL)).equals("") ? c.getString(c.getColumnIndex(TransJualProvider.KEY_TANGGAL)) : "");
                String time            = (!c.getString(c.getColumnIndex(TransJualProvider.KEY_TIME)).equals("") ? c.getString(c.getColumnIndex(TransJualProvider.KEY_TIME)) : "");
                double subtotal        = (!c.getString(c.getColumnIndex(TransJualProvider.KEY_SUBTOTAL)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(TransJualProvider.KEY_SUBTOTAL))) : 0;
                double tax             = (!c.getString(c.getColumnIndex(TransJualProvider.KEY_TAX)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(TransJualProvider.KEY_TAX))) : 0;
                double total_discount  = (!c.getString(c.getColumnIndex(TransJualProvider.KEY_TOTAL_DISCOUNT)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(TransJualProvider.KEY_TOTAL_DISCOUNT))) : 0;
                double kembalian       = (!c.getString(c.getColumnIndex(TransJualProvider.KEY_KEMBALIAN)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(TransJualProvider.KEY_KEMBALIAN))) : 0;
                double total_bayar     = (!c.getString(c.getColumnIndex(TransJualProvider.KEY_TOTAL_BAYAR)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(TransJualProvider.KEY_TOTAL_BAYAR))) : 0;
                String detail_id       = (!c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_DETAIL_ID)).equals("")) ? c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_DETAIL_ID)) : "";
                String id_jual_detail  = (!c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_ID_JUAL_DETAIL)).equals("")) ? c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_ID_JUAL_DETAIL)) : "";
                String item_code       = (!c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_ITEM_CODE)).equals("")) ? c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_ITEM_CODE)) : "";
                String description     = (!c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_DESCRIPTION)).equals("")) ? c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_DESCRIPTION)) : "";
                double harga           = (!c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_HARGA)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_HARGA))) : 0;
                double hargaMember     = (!c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_HARGA_MEMBER)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_HARGA_MEMBER))) : 0;
                double qty             = (!c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_QTY)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_QTY))) : 0;
                String satuan          = (!c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_SATUAN)).equals("")) ? c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_SATUAN)) : "";
                String promo           = (!c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_PROMO)).equals("")) ? c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_PROMO)) : "";
                String barcode         = (!c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_BARCODE)).equals("")) ? c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_BARCODE)) : "";
                double disc_percent    = (!c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_DISC_PERCENT)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_DISC_PERCENT))) : 0;
                double disc_amount     = (!c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_DISC_AMOUNT)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_DISC_AMOUNT))) : 0;
                String tax_type        = (!c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_TAX_TYPE)).equals("")) ? c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_TAX_TYPE)) : "";
                double hargaHpp        = (!c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_HARGA_HPP)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_HARGA_HPP))) : 0;
                double discount        = (!c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_DISCOUNT)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_DISCOUNT))) : 0;
                double total           = (!c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_TOTAL)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_TOTAL))) : 0;


                transaksiPenjualanModelArrayList.add(new TransaksiPenjualanModel(
                        id_jual, id_member, id_kasir, tanggal, time, subtotal, tax, total_discount, total_bayar, shift_id,
                        id_reference, kembalian, id_jual_detail, item_code, description, harga, hargaMember, qty,
                        satuan, discount,total, promo, barcode, disc_percent, disc_amount, tax_type, hargaHpp,
                        Constants.userInfoModel.getLocation(), detail_id
                ));

            }while(c.moveToNext());
            c.close();

        }else{
            Utils.showLogI("Data penjualan dblocal Kosong!");
        }

        postEndOfShift();

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


    private void showDialogEndOfShift(String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int id){
                        dialog.dismiss();
                        finish();
                        PrintEndOfShiftActivity_.intent(EndOfShiftActivity.this).start();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void showDialogEndOfShiftWitoutPrint(String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int id){
                        dialog.dismiss();
                        finish();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private double getCash_cashier(){
        cash_cashier = 0;
        if(editCashCashier.getText().toString().equals("")){
            cash_cashier = 0;
        }else{

            String cashCasirString = editCashCashier.getText().toString();
            if(cashCasirString.contains(",")) {
                cashCasirString = cashCasirString.replaceAll(",", "");
            }

            cash_cashier = Double.parseDouble(cashCasirString);
        }
        return  cash_cashier;
    }



    private void postEndOfShift(){

        ApiConnection.getEndOfShiftPosInterface().getEndOfShiftPos(getEndOfShiftPost(), new Callback<Response>(){
            @Override
            public void success(Response response, Response response2){
                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject sosObj = null;
                try{
                    sosObj = new JSONObject(rawResponse);
                    String jStatus  = sosObj.optString("message");

                    if(jStatus.equals("success")){
                        Constants.endOfShiftModel.setCashIncome(getCash_cashier());
                        showDialogEndOfShift("Anda berhasil melakukan End Of Shift");
                        updateDataJual();
                        updateDataJualBayar();
                    }else if(jStatus.equals("failed eos")){
                        showDialogEndOfShiftWitoutPrint("End Of Shift gagal dilakukan!");
                    }else if(jStatus.equals("not yet eos")){
                        showDialogEndOfShiftWitoutPrint("anda belum melakukan Start Of Shift!");
                    }else if(jStatus.equals("not yet sod")){
                        showDialogEndOfShiftWitoutPrint("anda belum melakukan Start Of Day!");
                    }

                }catch(JSONException e){
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

                if (progressDialog!=null)progressDialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                if (progressDialog!=null)progressDialog.dismiss();
            }

        });
    }


    private EndOfShiftModelPost getEndOfShiftPost(){
        EndOfShiftModelPost endOfShiftModelPost = new EndOfShiftModelPost(
                getCash_cashier(),
                Constants.userInfoModel,
                transaksiPenjualanModelArrayList,
                transaksiJualBayarModels
        );
        return endOfShiftModelPost;
    }


    private void updateDataJual(){

        int status_sync  = 0; int _id = 0;
        String URL = "content://com.trimitrasis.finosapps.ContentProvider.TransJualProvider/transjual";
        Uri uri_   = Uri.parse(URL);
        Cursor c   = getContentResolver().query(uri_, null, "status_sinc = " + 0, null, "_id ASC");

        if(c.moveToFirst()){

            do{
                status_sync = Integer.parseInt(c.getString(c.getColumnIndex(TransJualProvider.KEY_STATUS_SINC)));
                _id         = Integer.parseInt(c.getString(c.getColumnIndex(TransJualProvider.KEY_ID)));

                if(status_sync == 0){
                    int deleteTransJual = getContentResolver().delete(TransJualProvider.CONTENT_URI, "_id " + " = '" + _id + "'", null);
                }

            }while (c.moveToNext());

            c.close();
        }

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
                    int deleteTransJualBayar = getContentResolver().delete(TransJualBayarProvider.CONTENT_URI, "id_bayar_item " + " = '" + _id + "'", null);
                }

            }while (c.moveToNext());

            c.close();
        }

        Thread.currentThread().interrupt();

    }



    public void setoranCashCasirChange(){
        editCashCashier.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){

            }

            @Override
            public void afterTextChanged(Editable s){
                editCashCashier.removeTextChangedListener(this);

                 try{
                    String originalString = s.toString();
                    Long longval;
                    if(originalString.contains(",")){
                        originalString = originalString.replaceAll(",", "");
                    }

                    longval = Long.parseLong(originalString);
                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);

                    editCashCashier.setText(formattedString);
                    editCashCashier.setSelection(editCashCashier.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                editCashCashier.addTextChangedListener(this);
            }
        });
    }


    private boolean isValidInput(){
        if (editCashCashier.getText().toString().isEmpty() || editCashCashier.getText() == null || editCashCashier.getText().toString().equals("")){
            Utils.showToast("Kas Akhir tidak boleh kosong!", this);
            return false;
        }else return true;
    }


}
