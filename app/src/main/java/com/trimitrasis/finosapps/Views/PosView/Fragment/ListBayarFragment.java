package com.trimitrasis.finosapps.Views.PosView.Fragment;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.squareup.okhttp.internal.Util;
import com.squareup.otto.Subscribe;
import com.trimitrasis.finosapps.BusStation;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.ContentProvider.DetailHoldSalesProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.HeaderHoldSalesProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.ItemProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.PromosiBarangProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.PromosiHadiahProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.PromosiProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.TaxGroupProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.TaxProvider;
import com.trimitrasis.finosapps.Views.PosView.Adapter.ListBayarAdapterList;
import com.trimitrasis.finosapps.Views.PosView.BayarActivity_;
import com.trimitrasis.finosapps.Views.PosView.CreateOrderActivity;
import com.trimitrasis.finosapps.Views.PosView.Model.ListCekPromo;
import com.trimitrasis.finosapps.Views.PosView.Model.ListPromoBarangQty;
import com.trimitrasis.finosapps.Views.PosView.Model.PpnModel;
import com.trimitrasis.finosapps.Views.PosView.Model.RingkasanOrderModel;
import com.trimitrasis.finosapps.Views.UtilView.Constants;
import com.trimitrasis.finosapps.Views.UtilView.FontUtils;
import com.trimitrasis.finosapps.Views.UtilView.UtilPos;
import com.trimitrasis.finosapps.Views.UtilView.Utils;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;



/**
 * Created by rahman on 29/03/2017.
 */


@EFragment(R.layout.fragment_column_bayar)
public class ListBayarFragment extends Fragment{

    @ViewById
    RecyclerView listBarangOrder;

    @ViewById
    Button btnCancelSales, btnBayar, btnHoldSale;

    @ViewById
    TextView textTotalBayar, textSubTotal, textPpnTab, textDiscountItem;

    @ViewById
    TextView labelDiscount, labelTax, labelSubTotal, labelTotal;

    String key_id_jual = "";

    Dialog dialogSetHold;
    LayoutInflater inflater;
    Spinner spinnerPpn;
    EditText edittextNomermeja;
    Button btnCancelHold; Button btnHold;
    ArrayList<String> listPpn = new ArrayList<>();
    ArrayList<PpnModel> ppnModels;
    String ppnNameString;

    double totalBayarAfTax = 0;
    double totalPajakPpn = 0;

    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<RingkasanOrderModel> ringkasanOrderModels;

    public static final int RINGKASAN_ORDER_FINISH = 20;

    OnDeleteInfoPromoListener deleteInfoPromoListener;

    List<String> listRingkasan = new ArrayList<>();
    List<String> listPromo = new ArrayList<>();

    List<ListPromoBarangQty> listPromoBarangQtyList = new ArrayList<>();
    List<ListCekPromo> listCekPromos = new ArrayList<>();
    ArrayList<String> listIndexPromo = new ArrayList<String>();
    int min = 1000;
    int idxPromo = 0; int qtyListPromo = 0;

    @OnActivityResult(RINGKASAN_ORDER_FINISH)
    void onFinishRingkasanOrder(){
        if(Constants.flak_bayar == 1){
            ringkasanOrderModels = new ArrayList<RingkasanOrderModel>();
            ringkasanOrderModels.clear();
            initList();
            setTextLabelNull();
            Constants.flak_bayar = 0;
        }
    }


    @AfterViews
    void afterView(){
        inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ringkasanOrderModels = new ArrayList<>();
        ringkasanOrderModels.clear();
        ppnModels = new ArrayList<>();
        ppnModels.clear();
        initSetTableView();
        initList();
        customView();
        getDataTax("");
    }


    private void customView(){
        labelDiscount.setTypeface(FontUtils.getHelvetica_Neue_LT(getActivity()));
        labelTax.setTypeface(FontUtils.getHelvetica_Neue_LT(getActivity()));
        labelSubTotal.setTypeface(FontUtils.getHelvetica_Neue_LT(getActivity()));
        textSubTotal.setTypeface(FontUtils.getHelvetica_Neue_LT(getActivity()));
        textPpnTab.setTypeface(FontUtils.getHelvetica_Neue_LT(getActivity()));
        textDiscountItem.setTypeface(FontUtils.getHelvetica_Neue_LT(getActivity()));
    }


    private void initSetTableView(){
        listBarangOrder.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        listBarangOrder.setLayoutManager(mLayoutManager);
    }


    private void initList(){
        ListBayarAdapterList adapter = new ListBayarAdapterList(getActivity(), ringkasanOrderModels);
        listBarangOrder.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onResume(){
        super.onResume();
        BusStation.getBus().register(this);
    }


    @Override
    public void onStop(){
        super.onStop();
        BusStation.getBus().unregister(this);
    }


    @Subscribe
    public void getMessage(final RingkasanOrderModel data){
        try{
            insertDataOrder(data);
        }catch (NullPointerException e){
            e.getMessage();
        }
    }


    private void insertDataOrder(RingkasanOrderModel ringkasanOrder){

        boolean k = true; double totQty = 0;
        for(int i = 0; i < ringkasanOrderModels.size(); i ++){

            if(ringkasanOrderModels.get(i).getKodeBarcode().equals(ringkasanOrder.getKodeBarcode())){

                if(ringkasanOrder.getInfo().equals("delete")){

                    if(ringkasanOrderModels.size() == 1){
                        CreateOrderActivity.jenisPajak = "";
                    }

                    String URL = "content://com.trimitrasis.finosapps.Views.ContentProvider.PromoJualProvider/promojual";
                    String kodePromo = UtilPos.getKodePromoFromBarcode("barcode_barang", URL, getActivity(), ringkasanOrder.getKodeBarcode());
                    ringkasanOrderModels.remove(i);

                    if(!kodePromo.toString().equals("")){
                        cekLastDataPromo(kodePromo, ringkasanOrderModels);
                    }

                }else{

                    if(ringkasanOrder.getQty() == 0){
                        Utils.showToast("Failed, tolong inputkan selain 0", getActivity());
                        k = false;
                        break;
                    }

                    if(ringkasanOrder.getQty() == 1.0){
                        if(ringkasanOrder.getInfo().equals("insert")){
                            totQty = ringkasanOrderModels.get(i).getQty() + ringkasanOrder.getQty();
                        }else{
                            totQty = ringkasanOrder.getQty();
                        }
                    }else{
                        totQty = ringkasanOrder.getQty();
                    }

                    ringkasanOrderModels.set(i, new RingkasanOrderModel(
                            ringkasanOrder.getItemId(),
                            ringkasanOrder.getKodeBarcode(),
                            ringkasanOrder.getKodeBarang(),
                            ringkasanOrder.getNamaBarang(),
                            ringkasanOrder.getSatuanBarang(),
                            ringkasanOrder.getHargaJual(),
                            totQty,
                            ringkasanOrderModels.get(i).getDiskon(),
                            ringkasanOrder.getTax() * totQty,
                            ringkasanOrderModels.get(i).getInfo(),
                            ringkasanOrder.getStandart_cost(),
                            ringkasanOrder.isFlag_qty(),
                            ringkasanOrder.isFlag_bom(),
                            ringkasanOrder.getTaxGroup(),
                            ringkasanOrder.getNote(),
                            ringkasanOrder.getDetailId()
                    ));


                }

                k = false;
                break;
            }
        }


        if(k == true){
            ringkasanOrderModels.add(new RingkasanOrderModel(
                    ringkasanOrder.getItemId(),
                    ringkasanOrder.getKodeBarcode(),
                    ringkasanOrder.getKodeBarang(),
                    ringkasanOrder.getNamaBarang(),
                    ringkasanOrder.getSatuanBarang(),
                    ringkasanOrder.getHargaJual(),
                    ringkasanOrder.getQty(),
                    ringkasanOrder.getDiskon(),
                    ringkasanOrder.getTax(),
                    "",
                    ringkasanOrder.getStandart_cost(),
                    ringkasanOrder.isFlag_qty(),
                    ringkasanOrder.isFlag_bom(),
                    ringkasanOrder.getTaxGroup(),
                    ringkasanOrder.getNote(),
                    ringkasanOrder.getDetailId()
            ));
        }


        cekPromoDiskon_test(ringkasanOrderModels);
        initList();
        textTotalBayar.setText(Utils.getCurrencyRupiahTanpaSimbol(UtilPos.getTotalBayar(ringkasanOrderModels) + getTotTaxPpn(CreateOrderActivity.jenisPajak)));
        textSubTotal.setText(Utils.getCurrencyRupiahTanpaSimbol(UtilPos.getTotalBayar(ringkasanOrderModels) - UtilPos.getTotalPajak(ringkasanOrderModels)));
        textPpnTab.setText(Utils.getCurrencyRupiahTanpaSimbol(UtilPos.getTotalPajak(ringkasanOrderModels) + getTotTaxPpn(CreateOrderActivity.jenisPajak)));
        textDiscountItem.setText(Utils.getCurrencyRupiahTanpaSimbol(UtilPos.getTotalDiscount(ringkasanOrderModels)));

    }


    private void setTextLabelNull(){
        textTotalBayar.setText("0");
        textSubTotal.setText("0");
        textDiscountItem.setText("0");
        textPpnTab.setText("0");
    }


    @Click
    void btnCancelSales(){

        if(ringkasanOrderModels.size() > 0){
            showDialogCancelSales();
        }else{
            Utils.showToast("Lakukan Transaksi terlebih dahulu !", getActivity());
        }

    }



    @Click
    void btnBayar(){

        key_id_jual = Utils.getIdJual(getActivity());
        if(ringkasanOrderModels.size() > 0){

            if(!CreateOrderActivity.idJualHold.toString().equals("")){

                BayarActivity_.intent(this).infoBayar("hold")
                        .idPenjualan(CreateOrderActivity.idJualHold)
                        .totalBayar(UtilPos.getTotalBayar(ringkasanOrderModels))
                        .tax(UtilPos.getTotalPajak(ringkasanOrderModels))
                        .discount(UtilPos.getTotalDiscount(ringkasanOrderModels))
                        .subTotalBayar(UtilPos.getTotalBayar(ringkasanOrderModels) - UtilPos.getTotalPajak(ringkasanOrderModels)).
                        ringkasanOrderModels(ringkasanOrderModels).jenisPajak(CreateOrderActivity.jenisPajak).startForResult(RINGKASAN_ORDER_FINISH);

            }else{
                    BayarActivity_.intent(this).infoBayar("pos")
                            .idPenjualan(key_id_jual)
                            .totalBayar(UtilPos.getTotalBayar(ringkasanOrderModels))
                            .tax(UtilPos.getTotalPajak(ringkasanOrderModels))
                            .discount(UtilPos.getTotalDiscount(ringkasanOrderModels))
                            .subTotalBayar(UtilPos.getTotalBayar(ringkasanOrderModels) - UtilPos.getTotalPajak(ringkasanOrderModels)).
                            ringkasanOrderModels(ringkasanOrderModels).startForResult(RINGKASAN_ORDER_FINISH);
            }

            getDeletePromo();
            CreateOrderActivity.idJualHold = "";
            CreateOrderActivity.jenisPajak = "";
            listCekPromos = new ArrayList<>();
            listCekPromos.clear();
            listIndexPromo = new ArrayList<>();
            listIndexPromo.clear();

        }else{
            Utils.showToast("Lakukan transaksi terlebih dahulu!", getActivity());
        }
    }



    private void showDialogCancelSales(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("Apakah anda yakin cancel penjualan ?").setCancelable(false).setPositiveButton("Ya",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int id) {
                ringkasanOrderModels = new ArrayList<RingkasanOrderModel>();
                ringkasanOrderModels.clear();
                initList();
                setTextLabelNull();
                dialog.dismiss();
                getDeletePromo();
                CreateOrderActivity.idJualHold = "";
                CreateOrderActivity.jenisPajak = "";
                listCekPromos = new ArrayList<ListCekPromo>();
                listCekPromos.clear();
                listIndexPromo = new ArrayList<String>();
                listIndexPromo.clear();
            }
        })

        .setNegativeButton("Tidak", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog,int id){
                    dialog.cancel();
                }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }



    private void cekPromoDiskon_test(ArrayList<RingkasanOrderModel> arrRingkasanOrderModels){
        for(int i = 0; i < arrRingkasanOrderModels.size(); i++){
            String URL = "content://com.trimitrasis.finosapps.Views.ContentProvider.PromoJualProvider/promojual";
            String kodePromo = UtilPos.getKodePromoFromBarcode("barcode_barang", URL, getActivity(), arrRingkasanOrderModels.get(i).getKodeBarcode());
            if(!kodePromo.toString().equals("")){
                cekPromo(kodePromo, arrRingkasanOrderModels);
            }else{
                cekDataPromoHadiah(arrRingkasanOrderModels.get(i), i, arrRingkasanOrderModels);
            }
        }
    }


    private void cekPromo(String kdPromo, ArrayList<RingkasanOrderModel> ringkasanOrderModels){

        String kode_promo = ""; String jenis_promo = ""; String from_ = ""; String to_ = ""; String barcode = "";
        double qty = 0.0; double discount_amount  = 0.0; double discount_percent = 0.0; String barcode_hadiah = "";
        double qtyBarang = 0; int qtyPromo = 0; int p = 0; double qty_hadiah = 0.0;  double totalDiskonPromo = 0;
        double totDiscPercent = 0; double totalDiscount = 0; double amount_hadiah = 0; double totDiscAmount = 0;
        int totCount = 0; int totItem = 0; double qtyHadiahBef = 0;

        String URLP = "content://com.trimitrasis.finosapps.Views.ContentProvider.PromoJualProvider/promojual";
        totCount = UtilPos.getJumlahDataFromKodePromo("kode_promo_barang", URLP, getActivity(), kdPromo);

        String URL = "content://com.trimitrasis.finosapps.Views.ContentProvider.PromoJualProvider/promojual";
        Uri uri_  = Uri.parse(URL);
        Cursor c_promo = getActivity().getContentResolver().query(uri_, null, "kode_promo_barang " + " = '" +  kdPromo + "'", null, "_id ASC");
        listPromo.clear(); listRingkasan.clear(); listPromoBarangQtyList.clear();

        if(c_promo.moveToFirst()){
            do{
                kode_promo       = (!c_promo.getString(c_promo.getColumnIndex(PromosiProvider.KEY_KODE_PROMO)).equals("") ? c_promo.getString(c_promo.getColumnIndex(PromosiProvider.KEY_KODE_PROMO)) : "");
                jenis_promo      = (!c_promo.getString(c_promo.getColumnIndex(PromosiProvider.KEY_JENIS_PROMO)).equals("") ? c_promo.getString(c_promo.getColumnIndex(PromosiProvider.KEY_JENIS_PROMO)) : "");
                from_            = (!c_promo.getString(c_promo.getColumnIndex(PromosiProvider.KEY_FROM)).equals("") ? c_promo.getString(c_promo.getColumnIndex(PromosiProvider.KEY_FROM)) : "");
                to_              = (!c_promo.getString(c_promo.getColumnIndex(PromosiProvider.KEY_TO)).equals("") ? c_promo.getString(c_promo.getColumnIndex(PromosiProvider.KEY_TO)) : "");
                barcode          = (!c_promo.getString(c_promo.getColumnIndex(PromosiBarangProvider.KEY_BARCODE)).equals("") ? c_promo.getString(c_promo.getColumnIndex(PromosiBarangProvider.KEY_BARCODE)) : "");
                qty              = (c_promo.getString(c_promo.getColumnIndex(PromosiBarangProvider.KEY_QTY_BARANG)) != null) ? Double.parseDouble(c_promo.getString(c_promo.getColumnIndex(PromosiBarangProvider.KEY_QTY_BARANG))) : 0.0;
                discount_amount  = (c_promo.getString(c_promo.getColumnIndex(PromosiBarangProvider.KEY_DISCOUNT_AMOUNT)) != null) ? Double.parseDouble(c_promo.getString(c_promo.getColumnIndex(PromosiBarangProvider.KEY_DISCOUNT_AMOUNT))) : 0.0;
                discount_percent = (c_promo.getString(c_promo.getColumnIndex(PromosiBarangProvider.KEY_DISCOUNT_PERCENT)) != null) ? Double.parseDouble(c_promo.getString(c_promo.getColumnIndex(PromosiBarangProvider.KEY_DISCOUNT_PERCENT))) : 0.0;
                listPromo.add(barcode);
                totItem++;

                Date dateNow = new Date();
                for(int i = 0; i < ringkasanOrderModels.size(); i++){
                    if(barcode.equals(ringkasanOrderModels.get(i).getKodeBarcode())){
                        if(dateNow.after(Utils.getDateRange(from_, -1)) && dateNow.before(Utils.getDateRange(to_, 1))){
                            qtyBarang = ringkasanOrderModels.get(i).getQty() / qty;
                            qtyPromo  = (int) qtyBarang;
                            if(qtyPromo >= 1){

                                listRingkasan.add(ringkasanOrderModels.get(i).getKodeBarcode());
                                if(jenis_promo.equals("DISCOUNT")){

                                    if(discount_amount != 0.0){totDiscAmount = discount_amount * qtyPromo;}
                                    if(discount_percent != 0.0){totDiscPercent = ((ringkasanOrderModels.get(i).getHargaJual()) * (qty * qtyPromo)) * (discount_percent/100);}
                                    totalDiscount = (totDiscAmount + totDiscPercent);

                                }else if(jenis_promo.equals("BUY X GET Y")){

                                    listPromoBarangQtyList.add(new ListPromoBarangQty(kode_promo, barcode, qtyPromo));
                                    if(listPromoBarangQtyList.size() > 0){
                                        for(int j = 0; j < listPromoBarangQtyList.size(); j++){
                                            if(listPromoBarangQtyList.get(j).getBarcodeBarang().equals(barcode)){
                                                listPromoBarangQtyList.set(j, new ListPromoBarangQty(kode_promo, barcode, qtyPromo));
                                            }
                                        }
                                    }

                                    if(totCount == totItem){
                                        for(int k = 0; k < listPromoBarangQtyList.size(); k++){
                                            if(k > 0){
                                                if(listPromoBarangQtyList.get(k).getQtyBarang() < min){
                                                    min = listPromoBarangQtyList.get(k).getQtyBarang();
                                                }
                                            }

                                            if(k == 0){
                                                min = listPromoBarangQtyList.get(k).getQtyBarang();
                                            }
                                        }
                                    }

                                }

                                p = i;
                                totalDiskonPromo = totalDiskonPromo + totalDiscount;

                            }
                        }
                    }
                }

            }while(c_promo.moveToNext());
            c_promo.close();
        }



        boolean result = Arrays.equals(listPromo.toArray(), listRingkasan.toArray());
        if(result == true && (listPromo.size() == listRingkasan.size()) && listPromo.size() != 0){

            if(jenis_promo.toString().equals("DISCOUNT")){

                ringkasanOrderModels.set(p, new RingkasanOrderModel(
                        ringkasanOrderModels.get(p).getItemId(),
                        ringkasanOrderModels.get(p).getKodeBarcode(),
                        ringkasanOrderModels.get(p).getKodeBarang(),
                        ringkasanOrderModels.get(p).getNamaBarang(),
                        ringkasanOrderModels.get(p).getSatuanBarang(),
                        ringkasanOrderModels.get(p).getHargaJual(),
                        ringkasanOrderModels.get(p).getQty(),
                        totalDiskonPromo,
                        ringkasanOrderModels.get(p).getTax(),
                        "set discount",
                        ringkasanOrderModels.get(p).getStandart_cost(),
                        ringkasanOrderModels.get(p).isFlag_qty(),
                        ringkasanOrderModels.get(p).isFlag_bom(),
                        ringkasanOrderModels.get(p).getTaxGroup(),
                        ringkasanOrderModels.get(p).getNote(),
                        ringkasanOrderModels.get(p).getDetailId()
                ));

            }else if(jenis_promo.toString().equals("BUY X GET Y")){

                int totQtyAqum = 0;
                for(int m = 0; m < listPromoBarangQtyList.size(); m++){
                    if(listPromoBarangQtyList.get(m).getKodePromo().equals(kode_promo)){
                        if(listPromoBarangQtyList.get(m).getQtyBarang() >= min){
                            totQtyAqum = totQtyAqum + min;
                        }
                    }
                }

                if(listCekPromos.size() > 0){
                    idxPromo = listIndexPromo.indexOf(kode_promo);
                    if(idxPromo != -1){
                        qtyListPromo = listCekPromos.get(idxPromo).getQtyBarang();
                    }else{
                        qtyListPromo = 0;
                    }
                }else{
                    qtyListPromo = 0;
                }


                if(totQtyAqum%totCount==0 && totQtyAqum > qtyListPromo){

                    if(listCekPromos.size() > 0){

                        int k = 0;
                        for(int i = 0; i < listCekPromos.size(); i++){
                            if(listCekPromos.get(i).getKodePromo().equals(kode_promo)){
                                idxPromo = listIndexPromo.indexOf(kode_promo);
                                listCekPromos.set(idxPromo, new ListCekPromo(kode_promo, totQtyAqum));
                                k = 1;
                                break;
                            }
                        }

                        if(k == 0){
                            listCekPromos.add(new ListCekPromo(kode_promo, totQtyAqum));
                            listIndexPromo.add(kode_promo);
                        }

                    }else{
                        listCekPromos.add(new ListCekPromo(kode_promo, totQtyAqum));
                        listIndexPromo.add(kode_promo);
                    }


                    String URL_HADIAH = "content://com.trimitrasis.finosapps.Views.ContentProvider.PromosiHadiahProvider/promosihadiah";
                    Uri uri_hadiah = Uri.parse(URL_HADIAH);
                    Cursor c_hadiah = getActivity().getContentResolver().query(uri_hadiah, null, "kode_promo_hadiah " + " = '" + kode_promo + "'", null, "_id ASC");

                    if(c_hadiah.moveToFirst()){

                        do{
                            qty_hadiah     = (c_hadiah.getString(c_hadiah.getColumnIndex(PromosiHadiahProvider.KEY_QTY_HADIAH)) != null) ? Double.parseDouble(c_hadiah.getString(c_hadiah.getColumnIndex(PromosiHadiahProvider.KEY_QTY_HADIAH))) : 0;
                            barcode_hadiah = (c_hadiah.getString(c_hadiah.getColumnIndex(PromosiHadiahProvider.KEY_BARCODE)) != null) ? c_hadiah.getString(c_hadiah.getColumnIndex(PromosiHadiahProvider.KEY_BARCODE)) : "";

                            String URLITEM = "content://com.trimitrasis.finosapps.ContentProvider.ItemProvider/item";
                            Uri uri_item   = Uri.parse(URLITEM);
                            Cursor citem   = getActivity().getContentResolver().query(uri_item, null, "barcode " + " = '" + barcode_hadiah + "'", null, "_id");

                            if(citem.moveToFirst()){
                                do{
                                    amount_hadiah       = citem.getDouble(citem.getColumnIndex(ItemProvider.KEY_HARGA_JUAL));
                                    String itemId       = citem.getString(citem.getColumnIndex(ItemProvider.KEY_KODE_ITEM));
                                    String kodeBarang   = citem.getString(citem.getColumnIndex(ItemProvider.KEY_STOCK_ID));
                                    String namaBarang   = citem.getString(citem.getColumnIndex(ItemProvider.KEY_DESCRIPTION));
                                    String satuanBarang = citem.getString(citem.getColumnIndex(ItemProvider.KEY_UOM_NAME));
                                    double standartCost = citem.getDouble(citem.getColumnIndex(ItemProvider.KEY_STANDART_COST));
                                    int flakQty         = citem.getInt(citem.getColumnIndex(ItemProvider.KEY_FLAG_QTY));
                                    int flakBom         = citem.getInt(citem.getColumnIndex(ItemProvider.KEY_FLAG_BOM));
                                    String taxGroup     = citem.getString(citem.getColumnIndex(ItemProvider.KEY_TAX_GROUP));
                                    String detilId      = citem.getString(citem.getColumnIndex(ItemProvider.KEY_DETAIL_ID));
                                    boolean flag_bom    = (flakBom == 1) ? true : false;
                                    boolean flag_quan   = (flakQty == 1) ? true : false;
                                    totalDiscount       = ((amount_hadiah) * (qty_hadiah * (totQtyAqum/totCount)));

                                    int n = 0;
                                    for(int k = 0; k < ringkasanOrderModels.size(); k++){
                                        if(barcode_hadiah.equals(ringkasanOrderModels.get(k).getKodeBarcode())){

                                            if(ringkasanOrderModels.get(k).getQty() > 0){
                                                qtyHadiahBef = ringkasanOrderModels.get(k).getQty() + 1.0;
                                            }else{
                                                qtyHadiahBef = ringkasanOrderModels.get(k).getQty();
                                            }

                                            ringkasanOrderModels.set(k, new RingkasanOrderModel(
                                                    ringkasanOrderModels.get(k).getItemId(),
                                                    ringkasanOrderModels.get(k).getKodeBarcode(),
                                                    ringkasanOrderModels.get(k).getKodeBarang(),
                                                    ringkasanOrderModels.get(k).getNamaBarang(),
                                                    ringkasanOrderModels.get(k).getSatuanBarang(),
                                                    ringkasanOrderModels.get(k).getHargaJual(),
                                                    qtyHadiahBef,
                                                    totalDiscount,
                                                    ringkasanOrderModels.get(k).getTax(),
                                                    "buy x get y",
                                                    ringkasanOrderModels.get(k).getStandart_cost(),
                                                    ringkasanOrderModels.get(k).isFlag_qty(),
                                                    ringkasanOrderModels.get(k).isFlag_bom(),
                                                    ringkasanOrderModels.get(k).getTaxGroup(),
                                                    ringkasanOrderModels.get(k).getNote(),
                                                    ringkasanOrderModels.get(k).getDetailId()
                                            ));

                                            n = 1;
                                            break;
                                        }
                                    }


                                    if(n == 0){
                                        System.out.println("add barang");
                                        ringkasanOrderModels.add(new RingkasanOrderModel(
                                                itemId,
                                                barcode_hadiah,
                                                kodeBarang,
                                                namaBarang,
                                                satuanBarang,
                                                amount_hadiah,
                                                qty_hadiah * (totQtyAqum/totCount),
                                                totalDiscount, 0,
                                                "buy x get y",
                                                standartCost,
                                                flag_quan,
                                                flag_bom,
                                                taxGroup,
                                                "",
                                                detilId));
                                    }


                                }while(citem.moveToNext());
                                citem.close();
                            }


                        } while (c_hadiah.moveToNext());
                        c_hadiah.close();
                    }
                }
            }

        }else if(result == false && (listPromo.size() != listRingkasan.size()) || listRingkasan.size() == 0 || listRingkasan == null){

            if(jenis_promo.toString().equals("BUY X GET Y")){

                String URL_HADIAH = "content://com.trimitrasis.finosapps.Views.ContentProvider.PromosiHadiahProvider/promosihadiah";
                Uri uri_had  = Uri.parse(URL_HADIAH);
                Cursor c_had = getActivity().getContentResolver().query(uri_had, null, "kode_promo_hadiah " + " = '" +  kode_promo + "'", null, "_id ASC");

                if(c_had.moveToFirst()){
                    do{
                        barcode_hadiah = (c_had.getString(c_had.getColumnIndex(PromosiHadiahProvider.KEY_BARCODE)) != null) ? c_had.getString(c_had.getColumnIndex(PromosiHadiahProvider.KEY_BARCODE)) : "";
                        for(int i = 0; i < ringkasanOrderModels.size(); i++){
                            if(ringkasanOrderModels.get(i).getKodeBarcode().equals(barcode_hadiah)){
                                if(ringkasanOrderModels.get(i).getInfo().equals("buy x get y")){
                                    if(ringkasanOrderModels.get(i).getQty() > 1.0){
                                        double qtyMin = ringkasanOrderModels.get(i).getQty() - 1.0;
                                        ringkasanOrderModels.set(i, new RingkasanOrderModel(
                                                ringkasanOrderModels.get(i).getItemId(),
                                                ringkasanOrderModels.get(i).getKodeBarcode(),
                                                ringkasanOrderModels.get(i).getKodeBarang(),
                                                ringkasanOrderModels.get(i).getNamaBarang(),
                                                ringkasanOrderModels.get(i).getSatuanBarang(),
                                                ringkasanOrderModels.get(i).getHargaJual(),
                                                qtyMin,
                                                0,
                                                ringkasanOrderModels.get(i).getTax(),
                                                "",
                                                ringkasanOrderModels.get(i).getStandart_cost(),
                                                ringkasanOrderModels.get(i).isFlag_qty(),
                                                ringkasanOrderModels.get(i).isFlag_bom(),
                                                ringkasanOrderModels.get(i).getTaxGroup(),
                                                ringkasanOrderModels.get(i).getNote(),
                                                ringkasanOrderModels.get(i).getDetailId()
                                        ));

                                    }else{
                                        ringkasanOrderModels.remove(i);
                                    }

                                    if(listCekPromos.size() > 0){
                                        idxPromo = listIndexPromo.indexOf(kode_promo);
                                        if(idxPromo != -1) {
                                            listIndexPromo.remove(idxPromo);
                                            listCekPromos.remove(idxPromo);
                                        }
                                    }

                                }
                            }
                        }
                    }while(c_had.moveToNext());
                    c_had.close();

                }
            }
        }

    }



    private void cekDataPromoHadiah(RingkasanOrderModel ringkasanOrderModel, int index, ArrayList<RingkasanOrderModel> arrRingkasanOrderModels){

        String kode_promo_hadiah = "";
        String URL_HADIAH = "content://com.trimitrasis.finosapps.Views.ContentProvider.PromosiHadiahProvider/promosihadiah";
        Uri uri_had  = Uri.parse(URL_HADIAH);
        Cursor c_had = getActivity().getContentResolver().query(uri_had, null, "barcode_hadiah " + " = '" +  ringkasanOrderModel.getKodeBarcode() + "'", null, "_id ASC");

        int c = 0;
        if(c_had.moveToFirst()){
            do{
                kode_promo_hadiah = (c_had.getString(c_had.getColumnIndex(PromosiHadiahProvider.KEY_KODE_PROMO)) != null) ? c_had.getString(c_had.getColumnIndex(PromosiHadiahProvider.KEY_KODE_PROMO)) : "";
                String URL = "content://com.trimitrasis.finosapps.Views.ContentProvider.PromoJualProvider/promojual";
                Uri uri_  = Uri.parse(URL);
                Cursor c_promo = getActivity().getContentResolver().query(uri_, null, "kode_promo_barang " + " = '" +  kode_promo_hadiah + "'", null, "_id ASC");

                if(c_promo.moveToFirst()){
                    do{
                        String barcode = (!c_promo.getString(c_promo.getColumnIndex(PromosiBarangProvider.KEY_BARCODE)).equals("") ? c_promo.getString(c_promo.getColumnIndex(PromosiBarangProvider.KEY_BARCODE)) : "");
                        for(int i = 0; i < arrRingkasanOrderModels.size(); i++){
                            if(barcode.equals(arrRingkasanOrderModels.get(i).getKodeBarcode())){
                                c = 1;
                            }
                        }

                    }while(c_promo.moveToNext());
                    c_promo.close();
                }

            }while(c_had.moveToNext());
            c_had.close();
        }


        if(c == 0){
            if(ringkasanOrderModel.getInfo().equals("buy x get y")){
                if(ringkasanOrderModel.getQty() > 1){ //System.out.println("remove lebih dari 1");

                    double qtyOrder = ringkasanOrderModels.get(index).getQty() - 1.0;
                    arrRingkasanOrderModels.set(index, new RingkasanOrderModel(
                            ringkasanOrderModels.get(index).getItemId(),
                            ringkasanOrderModels.get(index).getKodeBarcode(),
                            ringkasanOrderModels.get(index).getKodeBarang(),
                            ringkasanOrderModels.get(index).getNamaBarang(),
                            ringkasanOrderModels.get(index).getSatuanBarang(),
                            ringkasanOrderModels.get(index).getHargaJual(),
                            qtyOrder,
                            0,
                            ringkasanOrderModels.get(index).getTax(),
                            "",
                            ringkasanOrderModels.get(index).getStandart_cost(),
                            ringkasanOrderModels.get(index).isFlag_qty(),
                            ringkasanOrderModels.get(index).isFlag_bom(),
                            ringkasanOrderModels.get(index).getTaxGroup(),
                            ringkasanOrderModels.get(index).getNote(),
                            ringkasanOrderModels.get(index).getDetailId()
                    ));
                }else{ //qty == 1
                    ringkasanOrderModels.remove(index);
                }

                if(listCekPromos.size() > 0){
                    idxPromo = listIndexPromo.indexOf(kode_promo_hadiah);
                    if(idxPromo != -1) {
                        listIndexPromo.remove(idxPromo);
                        listCekPromos.remove(idxPromo);
                    }
                }

            }
        }
    }



    private void cekLastDataPromo(String kodePromo,  ArrayList<RingkasanOrderModel> arrRingkasanOrderModels){

        String URL = "content://com.trimitrasis.finosapps.Views.ContentProvider.PromoJualProvider/promojual";
        Uri uri_  = Uri.parse(URL);
        Cursor c_promo = getActivity().getContentResolver().query(uri_, null, "kode_promo_barang " + " = '" +  kodePromo + "'", null, "_id ASC");

        int countBarang = 0; int countPromoBarang = 0;
        if(c_promo.moveToFirst()){
            do{
                countPromoBarang++;
                String barcode = (!c_promo.getString(c_promo.getColumnIndex(PromosiBarangProvider.KEY_BARCODE)).equals("") ? c_promo.getString(c_promo.getColumnIndex(PromosiBarangProvider.KEY_BARCODE)) : "");
                for(int i = 0; i < arrRingkasanOrderModels.size(); i++){
                    if(barcode.equals(arrRingkasanOrderModels.get(i).getKodeBarcode())){
                        countBarang++;
                    }
                }

            }while(c_promo.moveToNext());
            c_promo.close();
        }

        if(countBarang != countPromoBarang){
            if(listCekPromos.size() > 0){
                idxPromo = listIndexPromo.indexOf(kodePromo);
                if(idxPromo != -1){
                    listIndexPromo.remove(idxPromo);
                    listCekPromos.remove(idxPromo);
                }
            }
        }

    }



    @Click
    void btnHoldSale(){
        if(ringkasanOrderModels.size() > 0){
            showDialogSetHold();
        }else{
            Utils.showToast("Lakukan Transaksi terlebih dahulu !", getActivity());
        }
    }



    private void cekDataHoldSales(){

        String nomerMeja = "";
        nomerMeja        = edittextNomermeja.getText().toString();
        String URL       = "content://com.trimitrasis.finosapps.Views.ContentProvider.HeaderHoldSalesProvider/headerholdsales";
        Uri uri_         = Uri.parse(URL);
        Cursor holdSales = getActivity().getContentResolver().query(uri_, null, "nomor_meja " + " = '" +  nomerMeja + "'" , null, "_id");

        if(holdSales.moveToFirst()){
            do{
                String idJualHold = (!holdSales.getString(holdSales.getColumnIndex(HeaderHoldSalesProvider.KEY_ID_JUAL)).equals("") ? holdSales.getString(holdSales.getColumnIndex(HeaderHoldSalesProvider.KEY_ID_JUAL)) : "");
                if(!CreateOrderActivity.idJualHold.toString().equals("")){
                    addDataHoldSales(idJualHold);
                }else{
                    showDialogInfoHold("Meja yang anda pesan sudah digunakan.");
                }

            }while (holdSales.moveToNext());
            holdSales.close();

        }else{
            addDataHoldSales("");
        }

        getDeletePromo();
    }


    private void addDataHoldSales(String idJual){
        if(ringkasanOrderModels != null){

            if(!idJual.toString().equals("")){
                int emptyHeaderHoldSales = getActivity().getContentResolver().delete(HeaderHoldSalesProvider.CONTENT_URI, "id_jual " + " = '" + idJual + "'", null);
                int emptyDetailHoldSales = getActivity().getContentResolver().delete(DetailHoldSalesProvider.CONTENT_URI, "id_jual_detail " + " = '" + idJual + "'", null);
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            key_id_jual = Utils.getIdJual(getActivity());

            ContentValues valuesHeader  = new ContentValues();
            if(!idJual.toString().equals("")){
                valuesHeader.put(HeaderHoldSalesProvider.KEY_ID_JUAL, idJual);
            }else{
                valuesHeader.put(HeaderHoldSalesProvider.KEY_ID_JUAL, key_id_jual);
            }

            valuesHeader.put(HeaderHoldSalesProvider.KEY_KASIR, Constants.userInfoModel.getEmail());
            valuesHeader.put(HeaderHoldSalesProvider.KEY_TANGGAL, dateFormat.format(date));
            valuesHeader.put(HeaderHoldSalesProvider.KEY_TOTAL_BAYAR, totalBayarAfTax);
            valuesHeader.put(HeaderHoldSalesProvider.KEY_NOMOR_MEJA, edittextNomermeja.getText().toString());
            valuesHeader.put(HeaderHoldSalesProvider.KEY_PAJAK, totalPajakPpn);
            valuesHeader.put(HeaderHoldSalesProvider.KEY_JENIS_PAJAK, ppnNameString);
            Uri uriHeader = getActivity().getContentResolver().insert(HeaderHoldSalesProvider.CONTENT_URI, valuesHeader);

            ContentValues values  = new ContentValues();
            for(int i = 0; i < ringkasanOrderModels.size(); i++){

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
                values.put(DetailHoldSalesProvider.KEY_TAX, ringkasanOrderModels.get(i).getTax());
                values.put(DetailHoldSalesProvider.KEY_STANDART_COST, ringkasanOrderModels.get(i).getStandart_cost());
                values.put(DetailHoldSalesProvider.KEY_FLAG_QTY, ringkasanOrderModels.get(i).isFlag_qty());
                values.put(DetailHoldSalesProvider.KEY_FLAG_BOM, ringkasanOrderModels.get(i).isFlag_bom());
                values.put(DetailHoldSalesProvider.KEY_TAX_GROUP, ringkasanOrderModels.get(i).getTaxGroup());
                values.put(DetailHoldSalesProvider.KEY_NOTE, ringkasanOrderModels.get(i).getNote());

                if(!idJual.toString().equals("")){
                    values.put(DetailHoldSalesProvider.KEY_ID_JUAL_DETAIL, idJual);
                }else{
                    values.put(DetailHoldSalesProvider.KEY_ID_JUAL_DETAIL, key_id_jual);
                }

                Uri uri = getActivity().getContentResolver().insert(DetailHoldSalesProvider.CONTENT_URI, values);
            }

            ringkasanOrderModels = new ArrayList<RingkasanOrderModel>();
            ringkasanOrderModels.clear();
            totalPajakPpn = 0;
            initList();
            setTextLabelNull();
            Utils.showToast("Success Melakukan tunda Penjualan !", getActivity());



        }else{
            Utils.showToast("Data Penjualan Kosong !", getActivity());
        }
    }


    public interface OnDeleteInfoPromoListener{
        public void onDeleteInfoPromo();
    }


    private void getDeletePromo(){
        deleteInfoPromoListener.onDeleteInfoPromo();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            deleteInfoPromoListener = (OnDeleteInfoPromoListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnDeleteInfoPromoListener");
        }
    }


    private void showDialogSetHold(){
        dialogSetHold = new Dialog(getActivity());
        View view = inflater.inflate(R.layout.popup_set_table, null);
        declareLayout(view);
        getDataTax("hold");
        dialogSetHold.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSetHold.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#50000000")));
        dialogSetHold.setContentView(view);
        dialogSetHold.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogSetHold.show();
    }


    private void declareLayout(View view){
        btnCancelHold      = (Button)   view.findViewById(R.id.btnCancelHold);
        btnHold            = (Button)   view.findViewById(R.id.btnHold);
        spinnerPpn         = (Spinner)  view.findViewById(R.id.spinnerPajak);
        edittextNomermeja  = (EditText) view.findViewById(R.id.edittextNomermeja);
        btnCancelHold.setOnClickListener(onClickListener);
        btnHold.setOnClickListener(onClickListener);
    }


    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.btnCancelHold:{
                    textTotalBayar.setText(Utils.getCurrencyRupiah(UtilPos.getTotalBayar(ringkasanOrderModels)));
                    dialogSetHold.dismiss();
                    CreateOrderActivity.idJualHold = "";
                    CreateOrderActivity.jenisPajak = "";

                }break;
                case R.id.btnHold:{
                    cekDataHoldSales();
                    dialogSetHold.dismiss();
                }break;


            }
        }
    };


    private void getDataTax(String info){
        String taxGroupName; String idTaxGroup;
        ppnModels.clear();
        String URL = "content://com.trimitrasis.finosapps.ContentProvider.TaxGroupProvider/taxgroup";
        Uri uri_   = Uri.parse(URL);
        Cursor c   = getActivity().getContentResolver().query(uri_, null, null , null, "_id");

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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, listPpn){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(getActivity()));
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                TextView view = (TextView)super.getDropDownView(position, convertView, parent);
                view.setTypeface(FontUtils.getFontHeaderToolbar(getActivity()));
                return view;
            }
        };


        spinnerPpn.setAdapter(adapter);
        spinnerPpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                ppnNameString = listPpn.get(position);
                if(getTotTaxPpn(ppnNameString) != 0){
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
                Cursor c   = getActivity().getContentResolver().query(uri_, null, "id_tax_group " + " = '" + ppnModels.get(i).getIdPPn() + "'" , null, "_id");

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



    private void showDialogInfoHold(String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
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



}
