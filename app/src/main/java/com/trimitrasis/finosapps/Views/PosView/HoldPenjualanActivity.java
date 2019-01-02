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
import android.widget.ImageView;
import android.widget.TextView;
import com.trimitrasis.finosapps.Connection.ApiConnection;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.ContentProvider.DetailHoldSalesProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.HeaderHoldSalesProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.ItemProvider;
import com.trimitrasis.finosapps.Views.PosView.Adapter.ListHoldSalesAdapter;
import com.trimitrasis.finosapps.Views.PosView.Model.HeaderHoldSalesModel;
import com.trimitrasis.finosapps.Views.PosView.Model.RingkasanOrderModel;
import com.trimitrasis.finosapps.Views.PrintView.PrintBillActivity_;
import com.trimitrasis.finosapps.Views.PrintView.PrintDapurActivity_;
import com.trimitrasis.finosapps.Views.UtilView.Constants;
import com.trimitrasis.finosapps.Views.UtilView.Utils;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rahman on 04/04/2017.
 */


@EActivity(R.layout.layout_hold_penjualan)
public class HoldPenjualanActivity extends AppCompatActivity implements ListHoldSalesAdapter.CallbackHoldSales,
                                                        ListHoldSalesAdapter.CallbackCetakPesanan {

    @ViewById
    RecyclerView listHoldSales;

    @ViewById
    Toolbar toolbar;

    TextView textCetakStruk, textCetakDapur;
    String id_penjualan = "", nomor_meja = "", jenis_pajak = "";
    double total_bayar_ = 0;

    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<HeaderHoldSalesModel> headerHoldSalesModels;
    ArrayList<RingkasanOrderModel> ringkasanOrderModels;

    Dialog dialogPilihPrinter;
    LayoutInflater inflater;

    public static final int HOLD_PENJUALAN_FINISH = 10;

    ProgressDialog progressDialog;

    @OnActivityResult(HOLD_PENJUALAN_FINISH)
    void onFinishHoldSales(){

        if(Constants.flak_hold == 1){
            getHeaderHoldSales();
            Constants.flak_hold = 0;
        }
    }



    @AfterViews
    void afterView(){
        headerHoldSalesModels = new ArrayList<>();
        headerHoldSalesModels.clear();
        ringkasanOrderModels = new ArrayList<>();
        ringkasanOrderModels.clear();
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initTabbar();
        customView();
        initSetTableView();
        initList();
        getHeaderHoldSales();
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
        toolbarView.setText("Hold Sales");
        ImageView imageView = (ImageView) toolbar.findViewById(R.id.iconHoldSales);
        imageView.setImageResource(R.mipmap.ic_sales);
        imageView.setOnClickListener(onClickListener);
    }

    private void initSetTableView(){
        listHoldSales.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        listHoldSales.setLayoutManager(mLayoutManager);
    }

    private void initList(){
        ListHoldSalesAdapter adapter = new ListHoldSalesAdapter(this, headerHoldSalesModels);
        listHoldSales.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    private void getHeaderHoldSales(){

        headerHoldSalesModels.clear();
        String URL = "content://com.trimitrasis.finosapps.Views.ContentProvider.HeaderHoldSalesProvider/headerholdsales";
        Uri uri_   = Uri.parse(URL);
        Cursor c   = getContentResolver().query(uri_, null, "status_hold = " + 0, null, "id_jual DESC");

        if(c.moveToFirst()){
            do{
                String id_jual     = (!c.getString(c.getColumnIndex(HeaderHoldSalesProvider.KEY_ID_JUAL)).equals("") ? c.getString(c.getColumnIndex(HeaderHoldSalesProvider.KEY_ID_JUAL)) : "");
                String tanggal     = (!c.getString(c.getColumnIndex(HeaderHoldSalesProvider.KEY_TANGGAL)).equals("") ? c.getString(c.getColumnIndex(HeaderHoldSalesProvider.KEY_TANGGAL)) : "");
                double total       = (!c.getString(c.getColumnIndex(HeaderHoldSalesProvider.KEY_TOTAL_BAYAR)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(HeaderHoldSalesProvider.KEY_TOTAL_BAYAR))) : 0;
                String kasir       = (!c.getString(c.getColumnIndex(HeaderHoldSalesProvider.KEY_KASIR)).equals("") ? c.getString(c.getColumnIndex(HeaderHoldSalesProvider.KEY_KASIR)) : "");
                String nomor_meja  = (!c.getString(c.getColumnIndex(HeaderHoldSalesProvider.KEY_NOMOR_MEJA)).equals("") ? c.getString(c.getColumnIndex(HeaderHoldSalesProvider.KEY_NOMOR_MEJA)) : "");
                String jenis_pajak = (!c.getString(c.getColumnIndex(HeaderHoldSalesProvider.KEY_JENIS_PAJAK)).equals("") ? c.getString(c.getColumnIndex(HeaderHoldSalesProvider.KEY_JENIS_PAJAK)) : "");
                double pajak       = (!c.getString(c.getColumnIndex(HeaderHoldSalesProvider.KEY_PAJAK)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(HeaderHoldSalesProvider.KEY_PAJAK))) : 0;
                headerHoldSalesModels.add(new HeaderHoldSalesModel(id_jual, tanggal, total, kasir, nomor_meja, jenis_pajak, pajak));
            }while (c.moveToNext());
            c.close();
        }else{
            Utils.showLogI("Data Header HoldSales dblocal Empty !");
        }

        initList();
    }



    @Override
    public void getDataHoldSales(HeaderHoldSalesModel holdSalesModel){
        id_penjualan = holdSalesModel.getId_jual();
        jenis_pajak  = holdSalesModel.getJenis_pajak();
        cekStartOfShift();
    }


    private void showDialogCetak(HeaderHoldSalesModel headerHoldSalesModel){
        dialogPilihPrinter = new Dialog(this);
        View view = inflater.inflate(R.layout.popup_pilih_printer, null);
        declareLayout(view);
        ringkasanOrderModels.clear();
        getDetailHoldSales(headerHoldSalesModel.getId_jual(), "cetak");
        dialogPilihPrinter.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogPilihPrinter.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#50000000")));
        dialogPilihPrinter.setContentView(view);
        dialogPilihPrinter.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogPilihPrinter.show();
    }


    private void declareLayout(View view){
        textCetakStruk = (TextView) view.findViewById(R.id.textCetakStruk);
        textCetakDapur = (TextView) view.findViewById(R.id.textCetakDapur);
        textCetakStruk.setOnClickListener(onClickListener);
        textCetakDapur.setOnClickListener(onClickListener);
    }


    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.textCetakStruk:{
                    PrintBillActivity_.intent(HoldPenjualanActivity.this).totab_bayar_(total_bayar_).nomorMeja(nomor_meja).ringkasanOrderModels(ringkasanOrderModels).start();
                    dialogPilihPrinter.dismiss();
                }break;
                case R.id.textCetakDapur:{
                    PrintDapurActivity_.intent(HoldPenjualanActivity.this).total_bayar_(total_bayar_).nomorMeja(nomor_meja).ringkasanOrderModels(ringkasanOrderModels).start();
                    dialogPilihPrinter.dismiss();
                }break;
                case R.id.iconHoldSales:{
                    CreateOrderActivity_.intent(HoldPenjualanActivity.this).start();
                }break;

            }
        }
    };


    @Override
    public void getCetakPesanan(HeaderHoldSalesModel holdSalesModel){
        id_penjualan = holdSalesModel.getId_jual();
        nomor_meja   = holdSalesModel.getNomo_meja();
        total_bayar_ = holdSalesModel.getTotal();
        showDialogCetak(holdSalesModel);
    }


    private void getDetailHoldSales(String idJual, String status){

        ringkasanOrderModels.clear();
        String URL = "content://com.trimitrasis.finosapps.Views.ContentProvider.DetailHoldSalesProvider/detailholdsales";
        Uri uri_   = Uri.parse(URL);
        Cursor c   = getContentResolver().query(uri_, null, "id_jual_detail" + " = '" +  idJual + "'", null, "_id ASC");

        if(c.moveToFirst()){
            do{
                String item_id         = (!c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_ITEM_ID)).equals("") ? c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_ITEM_ID)) : "");
                String detail_id       = (!c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_DETAL_ID)).equals("") ? c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_DETAL_ID)) : "");
                String kode_barang     = (!c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_KODE_BARANG)).equals("") ? c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_KODE_BARANG)) : "");
                String kode_barcode    = (!c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_KODE_BARCODE)).equals("") ? c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_KODE_BARCODE)) : "");
                String nama_barang     = (!c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_NAMA_BARANG)).equals("") ? c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_NAMA_BARANG)) : "");
                String satuan          = (!c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_SATUAN_BARANG)).equals("") ? c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_SATUAN_BARANG)) : "");
                double qty             = (!c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_QTY)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_QTY))) : 0;
                double harga_jual      = (!c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_HARGA_JUAL)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_HARGA_JUAL))) : 0;
                double diskon          = (!c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_DISKON)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_DISKON))) : 0;
                double tax             = (!c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_TAX)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_TAX))) : 0;
                String info            = (!c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_INFO)).equals("") ? c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_INFO)) : "");
                String tax_group       = (!c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_TAX_GROUP)).equals("") ? c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_TAX_GROUP)) : "");
                double standart_cost   = (!c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_STANDART_COST)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_STANDART_COST))) : 0;
                int flag_qty           = c.getInt(c.getColumnIndex(ItemProvider.KEY_FLAG_QTY));
                int flag_bom           = c.getInt(c.getColumnIndex(ItemProvider.KEY_FLAG_BOM));
                String note            = (!c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_NOTE)).equals("") ? c.getString(c.getColumnIndex(DetailHoldSalesProvider.KEY_NOTE)) : "");
                boolean flag_quantity  = (flag_qty == 1) ? true : false;
                boolean flag_billofmat = (flag_bom == 1) ? true : false;
                ringkasanOrderModels.add(new RingkasanOrderModel(item_id, kode_barcode, kode_barang, nama_barang, satuan, harga_jual, qty, diskon, tax, info, standart_cost, flag_quantity, flag_billofmat, tax_group, note, detail_id));

            }while (c.moveToNext());
            c.close();
        }else{
            Utils.showLogI("Data Detail HoldSales dblocal Kosong <<< .!");
        }


        if(status.equals("holding")){
            CreateOrderActivity_.intent(HoldPenjualanActivity.this).jenis_pajak(jenis_pajak).getRingkasanOrderModels(ringkasanOrderModels).id_jual(id_penjualan).startForResult(HOLD_PENJUALAN_FINISH); //.flags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP)
            ringkasanOrderModels.clear();
            finish();
        }
    }



    private void cekStartOfShift(){

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.show();

        ApiConnection.getCheckSosHoldInterface().getCheckSos(Constants.userInfoModel, new Callback<Response>(){
            @Override
            public void success(Response response, Response response2){
                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject sosObj = null;
                try{
                    sosObj = new JSONObject(rawResponse);
                    String jStatus  = sosObj.optString("message");
                    if(jStatus.equals("available")){
                        getDetailHoldSales(id_penjualan, "holding");
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





}
