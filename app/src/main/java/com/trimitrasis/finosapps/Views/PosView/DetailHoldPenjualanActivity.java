package com.trimitrasis.finosapps.Views.PosView;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import com.trimitrasis.finosapps.Connection.ApiConnection;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.ContentProvider.DetailHoldSalesProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.ItemProvider;
import com.trimitrasis.finosapps.Views.PosView.Adapter.ListDetailHoldSalesAdapter;
import com.trimitrasis.finosapps.Views.PosView.Model.RingkasanOrderModel;
import com.trimitrasis.finosapps.Views.UtilView.Constants;
import com.trimitrasis.finosapps.Views.UtilView.Utils;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rahman on 09/06/2017.
 */

@EActivity(R.layout.layout_detail_hold_penjualan)
public class DetailHoldPenjualanActivity extends AppCompatActivity{

    ProgressDialog progressDialog;

    @ViewById
    RecyclerView listDetailHoldSales;

    @ViewById
    Toolbar toolbar;

    @ViewById
    Button btnResume;

    @Extra
    String idJual;

    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<RingkasanOrderModel> ringkasanOrderModels;




    @AfterViews
    void afterView(){
        ringkasanOrderModels = new ArrayList<>();
        ringkasanOrderModels.clear();

        initTabbar();
        customView();
        initSetTableView();

        if(!idJual.equals("")){
            getDetailHoldSales(idJual);
        }

    }


    @OptionsItem(android.R.id.home)
    void homeClick(){
        onBackPressed();
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
        toolbarView.setText("Detail Data Hold Sales");
    }

    private void initSetTableView(){
        listDetailHoldSales.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        listDetailHoldSales.setLayoutManager(mLayoutManager);
    }


    private void initList(){
        ListDetailHoldSalesAdapter adapter = new ListDetailHoldSalesAdapter(this, ringkasanOrderModels);
        listDetailHoldSales.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    private void getDetailHoldSales(String idJual){

        int flag_qty; int flag_bom;
        String kode_barang; String kode_barcode; String nama_barang; String satuan;
        String tax_group; String detail_id; String item_id;
        double harga_jual; double qty; double diskon; String info; double tax, standart_cost;
        boolean flag_quantity = false; boolean flag_billofmat = false;

        String URL = "content://com.trimitrasis.finosapps.Views.ContentProvider.DetailHoldSalesProvider/detailholdsales";
        Uri uri_   = Uri.parse(URL);
        Cursor c   = getContentResolver().query(uri_, null, "id_jual_detail " + " = '" +  idJual + "'", null, "_id ASC");

        if(c.moveToFirst()){

            do{

                item_id         = (!c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_ITEM_ID)).equals("") ? c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_ITEM_ID)) : "");
                detail_id       = (!c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_DETAL_ID)).equals("") ? c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_DETAL_ID)) : "");
                kode_barang     = (!c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_KODE_BARANG)).equals("") ? c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_KODE_BARANG)) : "");
                kode_barcode    = (!c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_KODE_BARCODE)).equals("") ? c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_KODE_BARCODE)) : "");
                nama_barang     = (!c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_NAMA_BARANG)).equals("") ? c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_NAMA_BARANG)) : "");
                satuan          = (!c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_SATUAN_BARANG)).equals("") ? c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_SATUAN_BARANG)) : "");
                qty             = (!c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_QTY)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_QTY))) : 0;
                harga_jual      = (!c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_HARGA_JUAL)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_HARGA_JUAL))) : 0;
                diskon          = (!c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_DISKON)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_DISKON))) : 0;
                tax             = (!c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_TAX)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_TAX))) : 0;
                info            = (!c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_INFO)).equals("") ? c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_INFO)) : "");
                tax_group       = (!c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_TAX_GROUP)).equals("") ? c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_TAX_GROUP)) : "");
                standart_cost   = (!c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_STANDART_COST)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_STANDART_COST))) : 0;
                flag_qty        = c.getInt(c.getColumnIndex(ItemProvider.KEY_FLAG_QTY));
                flag_bom        = c.getInt(c.getColumnIndex(ItemProvider.KEY_FLAG_BOM));
                flag_quantity   = (flag_qty == 1) ? true : false;
                flag_billofmat  = (flag_bom == 1) ? true : false;


                ringkasanOrderModels.add(new RingkasanOrderModel(item_id, kode_barcode, kode_barang, nama_barang, satuan, harga_jual, qty, diskon, tax, info, standart_cost, flag_quantity,flag_billofmat, tax_group, "", detail_id));
            }while (c.moveToNext());
            c.close();
        }else{
            Utils.showLogI("Data Header HoldSales dblocal Kosong!");
        }

        initList();
    }


    @Click
    void btnResume(){
        cekStartOfShift();
    }


    private void showDialogStartOfShift(String message, final boolean status){
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



    private void cekStartOfShift(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.show();

        ApiConnection.getCheckSosHoldInterface().getCheckSos(Constants.userInfoModel, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2){
                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject sosObj = null;
                try{
                    sosObj = new JSONObject(rawResponse);
                    String jStatus  = sosObj.optString("message");
                    if(jStatus.equals("available")){
                        CreateOrderActivity_.intent(DetailHoldPenjualanActivity.this).getRingkasanOrderModels(ringkasanOrderModels).id_jual(idJual).start();
                        finish();
                    }else if(jStatus.equals("not available sos")){
                        showDialogStartOfShift("Anda belum melakukan Start Of Shift!!", false);
                    }else if(jStatus.equals("not available sod")){
                        showDialogStartOfShift("Anda belum melakukan Start Of Day!!", false);
                    }

                }catch(JSONException e){
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

                if(progressDialog!=null)progressDialog.dismiss();

            }


            @Override
            public void failure(RetrofitError error) {
                if (progressDialog!=null)progressDialog.dismiss();
            }

        });
    }



}
