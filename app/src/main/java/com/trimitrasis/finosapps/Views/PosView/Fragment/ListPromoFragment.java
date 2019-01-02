package com.trimitrasis.finosapps.Views.PosView.Fragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import com.squareup.otto.Subscribe;
import com.trimitrasis.finosapps.BusStation;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.ContentProvider.ItemProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.PromosiBarangProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.PromosiHadiahProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.PromosiProvider;
import com.trimitrasis.finosapps.Views.PosView.Adapter.ListPromoAdapterList;
import com.trimitrasis.finosapps.Views.PosView.Model.ListPromosiBarangModel;
import com.trimitrasis.finosapps.Views.PosView.Model.RingkasanOrderModel;
import com.trimitrasis.finosapps.Views.UtilView.Utils;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by rahman on 29/03/2017.
 */


@EFragment(R.layout.fragment_column_promo)
public class ListPromoFragment extends Fragment {

    @ViewById
    Button btnClearPromo;

    @ViewById
    RecyclerView listPromo;

    RecyclerView.LayoutManager mLayoutManager;

    ArrayList<ListPromosiBarangModel> listPromosiBarangModels;

    List<String> listDiskons;

    @AfterViews
    void afterView(){
        listDiskons = new ArrayList<>();
        listDiskons.clear();
        listPromosiBarangModels = new ArrayList<>();
        listPromosiBarangModels.clear();
        initSetTableView();
        initList();
    }


    private void initSetTableView(){
        listPromo.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        listPromo.setLayoutManager(mLayoutManager);
    }


    private void initList(){
        ListPromoAdapterList adapter = new ListPromoAdapterList(getActivity(), listDiskons);
        listPromo.setAdapter(adapter);
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
            cekPromoDiskon_test(data);
        }catch (NullPointerException e){
            e.getMessage();
        }
    }



    private void cekPromoDiskon_test(RingkasanOrderModel ringkasanOrderModel){

        Date dateNow = new Date();
        String kode_promo = ""; String jenis_promo = ""; String from_ = ""; String to_ = ""; String barcode = "";
        double qty = 0.0; double discount_amount  = 0.0; double discount_percent = 0.0; String barcode_hadiah = "";
        double qty_hadiah = 0.0; double totDiscPercent = 0; double totalDiscount = 0;double totDiscAmount = 0;
        String name_hadiah = ""; String satuan_hadiah = ""; String formatBarangDiskon = ""; String formatHadiah = "";
        String formatDiskon = ""; String formatBuyXgetY = "";  String item_name = ""; String kodePromo = "";
        boolean isReady = true; String barcodeHadiah = "";String formatBarangBuyX = "";

        String URLBARANG = "content://com.trimitrasis.finosapps.Views.ContentProvider.PromoJualProvider/promojual";
        Uri uri_barang  = Uri.parse(URLBARANG);
        Cursor c_barang = getActivity().getContentResolver().query(uri_barang, null, "barcode_barang " + " = '" +  ringkasanOrderModel.getKodeBarcode() + "'", null, "_id ASC");

        if(c_barang.moveToFirst()){
            do{
                kodePromo = (!c_barang.getString(c_barang.getColumnIndex(PromosiProvider.KEY_KODE_PROMO)).equals("") ? c_barang.getString(c_barang.getColumnIndex(PromosiProvider.KEY_KODE_PROMO)) : "");
            }while(c_barang.moveToNext());
            c_barang.close();
        }


        String URL = "content://com.trimitrasis.finosapps.Views.ContentProvider.PromoJualProvider/promojual";
        Uri  uri_  = Uri.parse(URL);
        Cursor c_promo   = getActivity().getContentResolver().query(uri_, null, "kode_promo_barang " + " = '" +  kodePromo + "'", null, "_id ASC");

        formatHadiah = ""; formatBarangDiskon = ""; formatBarangBuyX = "";
        if(c_promo.moveToFirst()) {
            do {

                kode_promo       = (!c_promo.getString(c_promo.getColumnIndex(PromosiProvider.KEY_KODE_PROMO)).equals("") ? c_promo.getString(c_promo.getColumnIndex(PromosiProvider.KEY_KODE_PROMO)) : "");
                jenis_promo      = (!c_promo.getString(c_promo.getColumnIndex(PromosiProvider.KEY_JENIS_PROMO)).equals("") ? c_promo.getString(c_promo.getColumnIndex(PromosiProvider.KEY_JENIS_PROMO)) : "");
                from_            = (!c_promo.getString(c_promo.getColumnIndex(PromosiProvider.KEY_FROM)).equals("") ? c_promo.getString(c_promo.getColumnIndex(PromosiProvider.KEY_FROM)) : "");
                to_              = (!c_promo.getString(c_promo.getColumnIndex(PromosiProvider.KEY_TO)).equals("") ? c_promo.getString(c_promo.getColumnIndex(PromosiProvider.KEY_TO)) : "");
                qty              = (c_promo.getString(c_promo.getColumnIndex(PromosiBarangProvider.KEY_QTY_BARANG)) != null) ? Double.parseDouble(c_promo.getString(c_promo.getColumnIndex(PromosiBarangProvider.KEY_QTY_BARANG))) : 0.0;
                discount_amount  = (c_promo.getString(c_promo.getColumnIndex(PromosiBarangProvider.KEY_DISCOUNT_AMOUNT)) != null) ? Double.parseDouble(c_promo.getString(c_promo.getColumnIndex(PromosiBarangProvider.KEY_DISCOUNT_AMOUNT))) : 0.0;
                discount_percent = (c_promo.getString(c_promo.getColumnIndex(PromosiBarangProvider.KEY_DISCOUNT_PERCENT)) != null) ? Double.parseDouble(c_promo.getString(c_promo.getColumnIndex(PromosiBarangProvider.KEY_DISCOUNT_PERCENT))) : 0.0;
                item_name        = (!c_promo.getString(c_promo.getColumnIndex(PromosiBarangProvider.KEY_ITEM_NAME)).equals("") ? c_promo.getString(c_promo.getColumnIndex(PromosiBarangProvider.KEY_ITEM_NAME)) : "");


                if(dateNow.after(Utils.getDateRange(from_, -1)) && dateNow.before(Utils.getDateRange(to_, 1))){

                    if(jenis_promo.equals("DISCOUNT")){

                        if(discount_amount != 0.0){totDiscAmount = discount_amount * 1;}
                        if(discount_percent != 0.0){totDiscPercent = ((ringkasanOrderModel.getHargaJual()) * (qty * 1)) * (discount_percent / 100);}
                        totalDiscount = (totDiscAmount + totDiscPercent);
                        formatBarangDiskon = formatBarangDiskon + " "+ item_name  + " qty : " + String.valueOf(qty)   + " " + ringkasanOrderModel.getSatuanBarang() + ", ";

                    }else if(jenis_promo.equals("BUY X GET Y")){

                        formatBarangBuyX = formatBarangBuyX + " "+ item_name + " qty : " + String.valueOf(qty) + " " + ringkasanOrderModel.getSatuanBarang() + ", ";
                    }
                }

            } while (c_promo.moveToNext());
        }


        for(int i = 0; i < listPromosiBarangModels.size(); i++){
            if(listPromosiBarangModels.get(i).getKodePromo().toString().equals(kode_promo)) {
                isReady = false;
            }
        }

        listPromosiBarangModels.add(new ListPromosiBarangModel(kodePromo));
        if(isReady == true){

            if(dateNow.after(Utils.getDateRange(from_, -1)) && dateNow.before(Utils.getDateRange(to_, 1))){

                if(jenis_promo.equals("DISCOUNT")){

                    formatDiskon = "Dengan membeli " + formatBarangDiskon + " " + " Anda lebih hemat sebesar " + totalDiscount;
                    listDiskons.add(formatDiskon);

                }else if (jenis_promo.equals("BUY X GET Y")){

                    String URL_HADIAH = "content://com.trimitrasis.finosapps.Views.ContentProvider.PromosiHadiahProvider/promosihadiah";
                    Uri uri_hadiah = Uri.parse(URL_HADIAH);
                    Cursor c_hadiah = getActivity().getContentResolver().query(uri_hadiah, null, "kode_promo_hadiah " + " = '" + kodePromo + "'", null, "_id ASC");

                    if(c_hadiah.moveToFirst()){
                        do{

                            qty_hadiah     = (c_hadiah.getString(c_hadiah.getColumnIndex(PromosiHadiahProvider.KEY_QTY_HADIAH)) != null) ? Double.parseDouble(c_hadiah.getString(c_hadiah.getColumnIndex(PromosiHadiahProvider.KEY_QTY_HADIAH))) : 0;
                            name_hadiah    = (c_hadiah.getString(c_hadiah.getColumnIndex(PromosiHadiahProvider.KEY_ITEM_NAME)) != null) ? c_hadiah.getString(c_hadiah.getColumnIndex(PromosiHadiahProvider.KEY_ITEM_NAME)) : "";
                            barcodeHadiah  = (c_hadiah.getString(c_hadiah.getColumnIndex(PromosiHadiahProvider.KEY_BARCODE)) != null) ? c_hadiah.getString(c_hadiah.getColumnIndex(PromosiHadiahProvider.KEY_BARCODE)) : "";

                            String URLITEM = "content://com.trimitrasis.finosapps.ContentProvider.ItemProvider/item";
                            Uri uri_item = Uri.parse(URLITEM);
                            Cursor citem = getActivity().getContentResolver().query(uri_item, null, "barcode " + " = '" + barcodeHadiah + "'", null, "_id");

                            if(citem.moveToFirst()){
                                do{
                                    satuan_hadiah = (!citem.getString(citem.getColumnIndex(ItemProvider.KEY_UOM_NAME)).equals("")) ? citem.getString(citem.getColumnIndex(ItemProvider.KEY_UOM_NAME)) : "";
                                }while(citem.moveToNext());
                                citem.close();
                            }

                            formatHadiah = formatHadiah + name_hadiah + " qty : " + qty_hadiah + " " + satuan_hadiah + ", ";

                        }while(c_hadiah.moveToNext());
                        c_hadiah.close();
                    }

                    formatBuyXgetY = "Dengan membeli " + formatBarangBuyX + " Anda mendapatkan Hadiah " + formatHadiah;
                    listDiskons.add(formatBuyXgetY);
                }
            }

        }

        initList();

    }




    @Click
    void btnClearPromo(){
        if(listDiskons != null){
            showDialogClearPromo();
        }else{
            Utils.showToast("Data promo kosong!", getActivity());
        }
    }


    private void showDialogClearPromo(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("Hapus Info data promo ?").setCancelable(false).setPositiveButton("Ya",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int id) {
                listDiskons = new ArrayList<>();
                listDiskons.clear();
                listPromosiBarangModels.clear();
                initList();
            }
        })


        .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public void clearInfoPromo(){
        listDiskons = new ArrayList<>();
        listDiskons.clear();
        listPromosiBarangModels.clear();
            initList();
    }




}
