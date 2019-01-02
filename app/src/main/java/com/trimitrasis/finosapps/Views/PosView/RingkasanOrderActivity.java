package com.trimitrasis.finosapps.Views.PosView;
import android.app.Dialog;
import android.content.ContentValues;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.ContentProvider.DetailHoldSalesProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.HeaderHoldSalesProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.TaxGroupProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.TaxProvider;
import com.trimitrasis.finosapps.Views.PosView.Adapter.RingkasanOrderAdapter;
import com.trimitrasis.finosapps.Views.PosView.Model.PpnModel;
import com.trimitrasis.finosapps.Views.PosView.Model.RingkasanOrderModel;
import com.trimitrasis.finosapps.Views.UtilView.Constants;
import com.trimitrasis.finosapps.Views.UtilView.FontUtils;
import com.trimitrasis.finosapps.Views.UtilView.UtilPos;
import com.trimitrasis.finosapps.Views.UtilView.Utils;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by rahman on 03/04/2017.
 */



@EActivity(R.layout.layout_ringkasan_order)
public class RingkasanOrderActivity extends AppCompatActivity implements
        RingkasanOrderAdapter.CallbackRingkasanOrderHandset{


    @ViewById
    Toolbar toolbar;

    @ViewById
    RecyclerView listRingkasanOrder;

    RecyclerView.LayoutManager mLayoutManager;

    @ViewById
    TextView textTotalBayar, textSubTotal, textDiscountItem, textPajakTerbayar;

    @Extra
    ArrayList<RingkasanOrderModel> ringkasanOrderModels;

    @Extra
    String idPenjualan;

    @Extra
    String infoBay;

    @Extra
    String jenisPajak;

    @ViewById
    Button btnHapus, btnHold, btnBayar;

    View toolbar_popup;

    double totalPajakPpn = 0;
    Dialog dialogDetailOrder;
    LayoutInflater inflater;

    TextView headerText;
    EditText textHargaBarang, textQtyBarang, textDiskon, textNote;
    Button buttonBatal, buttonHapus, buttonSimpan;
    int position;

    double qtyBarang, hargaBarang;

    RingkasanOrderModel lisOrderModel;
    int totalQty = 0;

    public static final int RINGKASAN_ORDER_FINISH = 10;

    Dialog dialogSetHold;
    Button btnCancelHold, btnHoldDialog;
    Spinner spinnerPpn;
    EditText edittextNomermeja;
    ArrayList<String> listPpn = new ArrayList<>();
    ArrayList<PpnModel> ppnModels;
    String ppnNameString;
    double totalBayarAfTax = 0;


    @OnActivityResult(RINGKASAN_ORDER_FINISH)
    void onFinishRingkasanOrder(){
        if(Constants.flak_bayar == 1){
            ringkasanOrderModels = new ArrayList<>();
            ringkasanOrderModels.clear();
            CreateOrderActivity.total_qty = 0;
            CreateOrderActivity.updateShopingQty();
            CreateOrderActivity.updateDataOrder(ringkasanOrderModels);
            finish();
            Constants.flak_bayar = 0;
        }
    }



    @AfterViews
    void afterView(){
        initSetTableView();

        ppnModels = new ArrayList<>();
        getDataTax("");

        if(ringkasanOrderModels != null){
            ringkasanOrderAdapter();
        }

        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initTabbar();
        customView();
    }


    private void customView(){
        TextView toolbarView = (TextView) toolbar.findViewById(R.id.headerText);
        toolbarView.setText("Ringkasan Order");
        textTotalBayar.setText(Utils.getCurrencyRupiah(UtilPos.getTotalBayar(ringkasanOrderModels) + getTotTaxPpn(jenisPajak)));
        textSubTotal.setText(Utils.getCurrencyRupiah(UtilPos.getTotalBayar(ringkasanOrderModels) - UtilPos.getTotalPajak(ringkasanOrderModels)));
        textDiscountItem.setText(Utils.getCurrencyRupiah(UtilPos.getTotalDiscount(ringkasanOrderModels)));
        textPajakTerbayar.setText(Utils.getCurrencyRupiah(UtilPos.getTotalPajak(ringkasanOrderModels) + getTotTaxPpn(jenisPajak)));
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
        jenisPajak = "";
    }


    private void initSetTableView(){
        listRingkasanOrder.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        listRingkasanOrder.setLayoutManager(mLayoutManager);
    }

    private void ringkasanOrderAdapter(){
        RingkasanOrderAdapter adapter = new RingkasanOrderAdapter(this, ringkasanOrderModels);
        listRingkasanOrder.setAdapter(adapter);
    }


    View.OnClickListener onClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.buttonBatal:{
                    dialogDetailOrder.dismiss();
                }break;
                case R.id.buttonHapus:{
                   showDialogDeleteOneOrder();
                }break;
                case R.id.buttonSimpan:{

                    qtyBarang = Double.parseDouble(textQtyBarang.getText().toString());
                    if(!textHargaBarang.getText().toString().equals("")){
                        hargaBarang = Double.parseDouble(textHargaBarang.getText().toString());
                    }

                    setDataOrder(lisOrderModel, position);

                }break;
                case R.id.btnHold:{
                    addDataHoldSales();
                    dialogSetHold.dismiss();
                }break;
                case R.id.btnCancelHold:{
                    dialogSetHold.dismiss();
                }break;
            }
        }
    };


    private void showDialogDetailOrder(RingkasanOrderModel ringkasanOrderModel, int position){
        dialogDetailOrder = new Dialog(this);
        View view = inflater.inflate(R.layout.popup_detail_order, null);
        declareLayout(view);
        this.position = position;
        setListOrderModel(ringkasanOrderModel);
        setDataRingkasanOrder(ringkasanOrderModel, position);
        dialogDetailOrder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogDetailOrder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#50000000")));
        dialogDetailOrder.setContentView(view);
        dialogDetailOrder.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogDetailOrder.show();
    }


    private void declareLayout(View view){
        toolbar_popup    = (View)     view.findViewById(R.id.toolbar);
        headerText       = (TextView) toolbar_popup.findViewById(R.id.headerText);
        textHargaBarang  = (EditText) view.findViewById(R.id.textHargaBarang);
        textQtyBarang    = (EditText) view.findViewById(R.id.textQtyBarang);
        textDiskon       = (EditText) view.findViewById(R.id.textDiskon);
        textNote         = (EditText) view.findViewById(R.id.textNote);
        buttonBatal      = (Button)   view.findViewById(R.id.buttonBatal);
        buttonHapus      = (Button)   view.findViewById(R.id.buttonHapus);
        buttonSimpan     = (Button)   view.findViewById(R.id.buttonSimpan);
        buttonBatal.setOnClickListener(onClickListener);
        buttonHapus.setOnClickListener(onClickListener);
        buttonSimpan.setOnClickListener(onClickListener);
        headerText.setText("Detail Order");
    }


    private void setDataRingkasanOrder(RingkasanOrderModel ringkasanOrderModel, int position){

        qtyBarang   = ringkasanOrderModel.getQty();
        hargaBarang = ringkasanOrderModel.getHargaJual();
        textHargaBarang.setText(String.valueOf(ringkasanOrderModel.getHargaJual()));
        textQtyBarang.setText(String.valueOf(ringkasanOrderModel.getQty()));
        textDiskon.setText(String.valueOf(ringkasanOrderModel.getDiskon()));
        textNote.setText(String.valueOf(ringkasanOrderModel.getNote()));

        if(ringkasanOrderModel.isFlag_qty() == false){
            textHargaBarang.setEnabled(true);
        }else if(ringkasanOrderModel.isFlag_qty() == true){
            textHargaBarang.setEnabled(false);
        }
    }


    @Override
    public void showDialogRingkasaOrderHandset(RingkasanOrderModel ringkasanOrderModel, int position){
        showDialogDetailOrder(ringkasanOrderModel, position);
    }


    private void showDialogDeleteAllOrder(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setMessage("Hapus semua order ?")
                .setCancelable(false)
                .setPositiveButton("Ya",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int id){
                        ringkasanOrderModels = new ArrayList<>();
                        ringkasanOrderModels.clear();
                        ringkasanOrderAdapter();
                        CreateOrderActivity.total_qty = 0;
                        CreateOrderActivity.updateShopingQty();
                        CreateOrderActivity.updateDataOrder(ringkasanOrderModels);
                        getViewTotal();
                        jenisPajak = "";
                        CreateOrderActivity.listCekPromos.clear();
                        CreateOrderActivity.listIndexPromo.clear();
                        dialog.dismiss();
                    }
                })

                .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id){
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }



    private void showDialogDeleteOneOrder(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setMessage("Hapus order ?")
                .setCancelable(false)
                .setPositiveButton("Ya",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int id){

                        String URL = "content://com.trimitrasis.finosapps.Views.ContentProvider.PromoJualProvider/promojual";
                        String kodePromo = UtilPos.getKodePromoFromBarcode("barcode_barang", URL, RingkasanOrderActivity.this, ringkasanOrderModels.get(position).getKodeBarcode());

                        ringkasanOrderModels.remove(position);

                        if(!kodePromo.toString().equals("")){
                            CreateOrderActivity.cekLastDataPromo(kodePromo, ringkasanOrderModels);
                        }

                        if(ringkasanOrderModels.size() == 1){
                            jenisPajak = "";
                            CreateOrderActivity.listCekPromos.clear();
                            CreateOrderActivity.listIndexPromo.clear();
                        }

                        ringkasanOrderAdapter();
                        updateQtyBarang();
                        CreateOrderActivity.updateShopingQty();
                        CreateOrderActivity.updateDataOrder(ringkasanOrderModels);
                        getViewTotal();
                        dialogDetailOrder.dismiss();
                    }
                })

                .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id){
                        dialogDetailOrder.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void setListOrderModel(RingkasanOrderModel ringkasanOrderModel){
        lisOrderModel = new RingkasanOrderModel(
                ringkasanOrderModel.getItemId(),
                ringkasanOrderModel.getKodeBarcode(),
                ringkasanOrderModel.getKodeBarang(),
                ringkasanOrderModel.getNamaBarang(),
                ringkasanOrderModel.getSatuanBarang(),
                ringkasanOrderModel.getHargaJual(),
                ringkasanOrderModel.getQty(),
                ringkasanOrderModel.getDiskon(),
                ringkasanOrderModel.getTax(),
                "set order",
                ringkasanOrderModel.getStandart_cost(),
                ringkasanOrderModel.isFlag_qty(),
                ringkasanOrderModel.isFlag_bom(),
                ringkasanOrderModel.getTaxGroup(),
                ringkasanOrderModel.getNote(),
                ringkasanOrderModel.getDetailId()
        );
    }



    private void setDataOrder(RingkasanOrderModel ringkasanOrder, int position){

        String noteBarang   = String.valueOf(textNote.getText().toString());
        if(ringkasanOrder.isFlag_qty() == false){

            ringkasanOrderModels.set(position, new RingkasanOrderModel(
                    ringkasanOrder.getItemId(),
                    ringkasanOrder.getKodeBarcode(),
                    ringkasanOrder.getKodeBarang(),
                    ringkasanOrder.getNamaBarang(),
                    ringkasanOrder.getSatuanBarang(),
                    hargaBarang,
                    qtyBarang,
                    ringkasanOrder.getDiskon(),
                    ringkasanOrder.getTax(),
                    "",
                    ringkasanOrder.getStandart_cost(),
                    ringkasanOrder.isFlag_qty(),
                    ringkasanOrder.isFlag_bom(),
                    ringkasanOrder.getTaxGroup(),
                    noteBarang,
                    ringkasanOrder.getDetailId()
            ));

        }else if(ringkasanOrder.isFlag_qty() == true){

            ringkasanOrderModels.set(position, new RingkasanOrderModel(
                    ringkasanOrder.getItemId(),
                    ringkasanOrder.getKodeBarcode(),
                    ringkasanOrder.getKodeBarang(),
                    ringkasanOrder.getNamaBarang(),
                    ringkasanOrder.getSatuanBarang(),
                    ringkasanOrder.getHargaJual(),
                    qtyBarang,
                    ringkasanOrder.getDiskon(),
                    ringkasanOrder.getTax(),
                    "",
                    ringkasanOrder.getStandart_cost(),
                    ringkasanOrder.isFlag_qty(),
                    ringkasanOrder.isFlag_bom(),
                    ringkasanOrder.getTaxGroup(),
                    noteBarang,
                    ringkasanOrder.getDetailId()
            ));
        }

        ringkasanOrderAdapter();
        updateQtyBarang();
        CreateOrderActivity.updateShopingQty();
        CreateOrderActivity.updateDataOrder(ringkasanOrderModels);
        getViewTotal();
        dialogDetailOrder.dismiss();
    }


    private void updateQtyBarang(){
        for (int a = 0; a < ringkasanOrderModels.size(); a++){
            totalQty = totalQty + (int) ringkasanOrderModels.get(a).getQty();
        }
        CreateOrderActivity.total_qty = totalQty;
        totalQty = 0;
    }


    private void addDataHoldSales(){

        if(ringkasanOrderModels.size() > 0){

            String key_id_jual = "";
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            key_id_jual = Utils.getIdJual(this);

            ContentValues valuesHeader  = new ContentValues();
            valuesHeader.put(HeaderHoldSalesProvider.KEY_ID_JUAL, key_id_jual);
            valuesHeader.put(HeaderHoldSalesProvider.KEY_KASIR, Constants.userInfoModel.getEmail());
            valuesHeader.put(HeaderHoldSalesProvider.KEY_TANGGAL, dateFormat.format(date));
            valuesHeader.put(HeaderHoldSalesProvider.KEY_TOTAL_BAYAR, UtilPos.getTotalBayar(ringkasanOrderModels));
            valuesHeader.put(HeaderHoldSalesProvider.KEY_NOMOR_MEJA, edittextNomermeja.getText().toString());
            valuesHeader.put(HeaderHoldSalesProvider.KEY_JENIS_PAJAK, ppnNameString);
            valuesHeader.put(HeaderHoldSalesProvider.KEY_PAJAK, totalPajakPpn);
            Uri uriHeader = getContentResolver().insert(HeaderHoldSalesProvider.CONTENT_URI, valuesHeader);

            ContentValues values  = new ContentValues();
            for(int i = 0; i < ringkasanOrderModels.size(); i++){

                values.put(DetailHoldSalesProvider.KEY_ID_JUAL_DETAIL, key_id_jual);
                values.put(DetailHoldSalesProvider.KEY_ITEM_ID, ringkasanOrderModels.get(i).getItemId());
                values.put(DetailHoldSalesProvider.KEY_DETAL_ID, ringkasanOrderModels.get(i).getDetailId());
                values.put(DetailHoldSalesProvider.KEY_KODE_BARANG, ringkasanOrderModels.get(i).getKodeBarang());
                values.put(DetailHoldSalesProvider.KEY_KODE_BARCODE, ringkasanOrderModels.get(i).getKodeBarcode());
                values.put(DetailHoldSalesProvider.KEY_NAMA_BARANG, ringkasanOrderModels.get(i).getNamaBarang());
                values.put(DetailHoldSalesProvider.KEY_HARGA_JUAL, ringkasanOrderModels.get(i).getHargaJual());
                values.put(DetailHoldSalesProvider.KEY_QTY, ringkasanOrderModels.get(i).getQty());
                values.put(DetailHoldSalesProvider.KEY_DISKON, ringkasanOrderModels.get(i).getDiskon());
                values.put(DetailHoldSalesProvider.KEY_SATUAN_BARANG, ringkasanOrderModels.get(i).getSatuanBarang());
                values.put(DetailHoldSalesProvider.KEY_INFO, ringkasanOrderModels.get(i).getInfo());
                values.put(DetailHoldSalesProvider.KEY_TAX_GROUP, ringkasanOrderModels.get(i).getTaxGroup());
                values.put(DetailHoldSalesProvider.KEY_TAX, ringkasanOrderModels.get(i).getTax());
                values.put(DetailHoldSalesProvider.KEY_STANDART_COST, ringkasanOrderModels.get(i).getStandart_cost());
                values.put(DetailHoldSalesProvider.KEY_NOTE, ringkasanOrderModels.get(i).getNote());
                values.put(DetailHoldSalesProvider.KEY_FLAG_QTY, ringkasanOrderModels.get(i).isFlag_qty());
                values.put(DetailHoldSalesProvider.KEY_FLAG_BOM, ringkasanOrderModels.get(i).isFlag_bom());
                Uri uri = getContentResolver().insert(DetailHoldSalesProvider.CONTENT_URI, values);
            }


            ringkasanOrderModels = new ArrayList<RingkasanOrderModel>();
            ringkasanOrderModels.clear();
            ringkasanOrderAdapter();
            CreateOrderActivity.total_qty = 0;
            CreateOrderActivity.updateShopingQty();
            CreateOrderActivity.updateDataOrder(ringkasanOrderModels);
            setViewTotalNull();
            jenisPajak = "";
            Utils.showToast("Success Melakukan tunda Penjualan !", this);

        }else{
            Utils.showToast("Data Penjualan Kosong !", this);
        }
    }


    @Click
    void btnHapus(){
        if(ringkasanOrderModels.size() > 0){
            showDialogDeleteAllOrder();
        }
    }



    @Click
    void btnBayar(){
        if(ringkasanOrderModels.size() > 0){
            if(infoBay.toString().equals("hold")){
                BayarActivity_.intent(this).jenisPajak(jenisPajak).idPenjualan(idPenjualan).infoBayar("hold")
                        .totalBayar(UtilPos.getTotalBayar(ringkasanOrderModels))
                        .tax(UtilPos.getTotalPajak(ringkasanOrderModels))
                        .discount(UtilPos.getTotalDiscount(ringkasanOrderModels))
                        .subTotalBayar(UtilPos.getTotalBayar(ringkasanOrderModels) - UtilPos.getTotalPajak(ringkasanOrderModels))
                        .ringkasanOrderModels(ringkasanOrderModels).startForResult(RINGKASAN_ORDER_FINISH);
            }else{
                BayarActivity_.intent(this).jenisPajak("").idPenjualan(idPenjualan).infoBayar("pos")
                        .totalBayar(UtilPos.getTotalBayar(ringkasanOrderModels))
                        .tax(UtilPos.getTotalPajak(ringkasanOrderModels))
                        .discount(UtilPos.getTotalDiscount(ringkasanOrderModels))
                        .subTotalBayar(UtilPos.getTotalBayar(ringkasanOrderModels) - UtilPos.getTotalPajak(ringkasanOrderModels))
                        .ringkasanOrderModels(ringkasanOrderModels).startForResult(RINGKASAN_ORDER_FINISH);
            }
        }

        CreateOrderActivity.listCekPromos.clear();
        CreateOrderActivity.listIndexPromo.clear();
    }

    
    @Click
    void btnHold(){
        if(ringkasanOrderModels.size() > 0){
            showDialogSetHold();
        }
    }


    private void showDialogSetHold(){
        dialogSetHold = new Dialog(this);
        View view = inflater.inflate(R.layout.popup_set_table, null);
        declareLayoutHold(view);
        getDataTax("hold");
        dialogSetHold.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSetHold.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#50000000")));
        dialogSetHold.setContentView(view);
        dialogSetHold.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogSetHold.show();
    }


    private void declareLayoutHold(View view){
        btnCancelHold      = (Button)   view.findViewById(R.id.btnCancelHold);
        btnHoldDialog      = (Button)   view.findViewById(R.id.btnHold);
        spinnerPpn         = (Spinner)  view.findViewById(R.id.spinnerPajak);
        edittextNomermeja  = (EditText) view.findViewById(R.id.edittextNomermeja);
        btnCancelHold.setOnClickListener(onClickListener);
        btnHoldDialog.setOnClickListener(onClickListener);
    }


    private void getDataTax(String info){
        String taxGroupName; String idTaxGroup;
        ppnModels.clear();
        String URL = "content://com.trimitrasis.finosapps.ContentProvider.TaxGroupProvider/taxgroup";
        Uri uri_   = Uri.parse(URL);
        Cursor c   = getContentResolver().query(uri_, null, null , null, "_id");

        if(c.moveToFirst()){
            do{
                idTaxGroup   = c.getString(c.getColumnIndex(TaxGroupProvider.KEY_ID_TAX_GROUP));
                taxGroupName = c.getString(c.getColumnIndex(TaxGroupProvider.KEY_DESCRIPTION));
                ppnModels.add(new PpnModel(idTaxGroup, taxGroupName));
            }while(c.moveToNext());
            c.close();
        }else{
            System.out.println("data tidak ditemukan!");
        }

        if(info.toString().equals("hold")){
            spinnerPpn();
        }

    }


    private void spinnerPpn(){

        if(ppnModels != null){
            listPpn.clear();
            listPpn.add("--No Tax--");
            for(int i = 0; i < ppnModels.size(); i++){
                listPpn.add(ppnModels.get(i).getDescPpn());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listPpn){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(RingkasanOrderActivity.this));
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView)super.getDropDownView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(RingkasanOrderActivity.this));
                return view;
            }
        };


        spinnerPpn.setAdapter(adapter);
        spinnerPpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                ppnNameString = listPpn.get(position);
                if(getTotTaxPpn(ppnNameString) != 0){
                    totalPajakPpn   = getTotTaxPpn(ppnNameString);
                    totalBayarAfTax = UtilPos.getTotalBayar(ringkasanOrderModels) + getTotTaxPpn(ppnNameString);
                    textTotalBayar.setText(Utils.getCurrencyRupiah(totalBayarAfTax));
                }else{
                    totalBayarAfTax = UtilPos.getTotalBayar(ringkasanOrderModels);
                    textTotalBayar.setText(Utils.getCurrencyRupiah(UtilPos.getTotalBayar(ringkasanOrderModels)));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){

            }
        });
    }


    private double getTotTaxPpn(String taxDesc){
        double totTaxPpn = 0; double rate = 0; double totRate = 0;
        for(int i = 0; i < ppnModels.size(); i++){
            if(taxDesc.equals(ppnModels.get(i).getDescPpn())){

                String URL = "content://com.trimitrasis.finosapps.ContentProvider.TaxProvider/tax";
                Uri uri_   = Uri.parse(URL);
                Cursor c   = getContentResolver().query(uri_, null, "id_tax_group " + " = '" + ppnModels.get(i).getIdPPn() + "'" , null, "_id");

                if(c.moveToFirst()){
                    do{
                        rate = Double.parseDouble(c.getString(c.getColumnIndex(TaxProvider.KEY_RATE)));
                        totRate = totRate + rate;
                    }while(c.moveToNext());
                    c.close();
                    totTaxPpn   = ((totRate/100) * UtilPos.getTotalBayar(ringkasanOrderModels));
                }
            }
        }

        return totTaxPpn;
    }



    private void getViewTotal(){
        textTotalBayar.setText(Utils.getCurrencyRupiah(UtilPos.getTotalBayar(ringkasanOrderModels)));
        textSubTotal.setText(Utils.getCurrencyRupiah(UtilPos.getTotalBayar(ringkasanOrderModels) - UtilPos.getTotalPajak(ringkasanOrderModels)));
        textDiscountItem.setText(Utils.getCurrencyRupiah(UtilPos.getTotalDiscount(ringkasanOrderModels)));
        textPajakTerbayar.setText(Utils.getCurrencyRupiah(UtilPos.getTotalPajak(ringkasanOrderModels)));
    }


    private void setViewTotalNull(){
        textTotalBayar.setText("Rp 0");
        textSubTotal.setText("Rp 0");
        textDiscountItem.setText("Rp 0");
        textPajakTerbayar.setText("Rp 0");
    }



}
