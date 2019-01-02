package com.trimitrasis.finosapps.Views.HistoryView.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.ContentProvider.TransJualProvider;
import com.trimitrasis.finosapps.Views.HistoryView.Adapter.RiwayatPenjualanAdapterSuccess;
import com.trimitrasis.finosapps.Views.PosView.Model.TransaksiPenjualanModel;
import com.trimitrasis.finosapps.Views.UtilView.Constants;
import com.trimitrasis.finosapps.Views.UtilView.FontUtils;
import com.trimitrasis.finosapps.Views.UtilView.Utils;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import java.util.ArrayList;

/**
 * Created by rahman on 10/25/2017.
 */

@EFragment(R.layout.fragment_history_success)
public class FragmentHistorySuccess extends Fragment{

    static ArrayList<TransaksiPenjualanModel> transaksiPenjualanModelArrayList;
    ArrayList<TransaksiPenjualanModel> transaksiPenjualanModels;

    @ViewById
    static RecyclerView listRiwayatPenjualan;

    @ViewById
    EditText editTextSearch;

    @ViewById
    TextView textTanggal, textStatus, textIdJual;

    RecyclerView.LayoutManager mLayoutManager;

    public static Context contextOfApplication;

    @AfterViews
    void afterView(){
        contextOfApplication = getActivity();
        transaksiPenjualanModelArrayList = new ArrayList<>();
        transaksiPenjualanModelArrayList.clear();
        transaksiPenjualanModels = new ArrayList<>();
        transaksiPenjualanModels.clear();
        initSetTableView();
        getDataPenjualan();
        addTextListener();
        setFontView();
    }


    private void initSetTableView(){
        listRiwayatPenjualan.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        listRiwayatPenjualan.setLayoutManager(mLayoutManager);
    }


    private void initListSearch(){
        RiwayatPenjualanAdapterSuccess adapter = new RiwayatPenjualanAdapterSuccess(getActivity(), transaksiPenjualanModels);
        listRiwayatPenjualan.setAdapter(adapter);
    }

    private static void riwayatJualAdapter(){
        RiwayatPenjualanAdapterSuccess adapter = new RiwayatPenjualanAdapterSuccess(contextOfApplication, transaksiPenjualanModelArrayList);
        listRiwayatPenjualan.setAdapter(adapter);
    }


    public static void getDataPenjualan(){

        String id_member = "",  shift_id = "", id_reference = "", detail_id = "", promo = "", barcode = "";
        String id_jual_detail = "", item_code = "", description = "", satuan = "", tax_type = "";
        double harga = 0.0, hargaMember = 0.0, qty = 0.0, discount = 0.0, total = 0.0;
        double disc_percent = 0.0, disc_amount = 0.0, hargaHpp = 0.0;

        transaksiPenjualanModelArrayList.clear();
        String URL = "content://com.trimitrasis.finosapps.ContentProvider.TransJualProvider/transjual";
        Uri uri_   = Uri.parse(URL);
        Cursor c   = contextOfApplication.getContentResolver().query(uri_, null, "status_sinc = " + 0, null, "_id DESC");

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

                transaksiPenjualanModelArrayList.add(new TransaksiPenjualanModel(
                        id_jual, id_member, id_kasir, tanggal, time, subtotal, tax, total_discount, total_bayar,
                        shift_id, id_reference, kembalian, id_jual_detail, item_code, description, harga,
                        hargaMember, qty, satuan, discount,total, promo, barcode, disc_percent, disc_amount,
                        tax_type, hargaHpp, Constants.userInfoModel.getLocation(), detail_id
                ));

            }while(c.moveToNext());
            c.close();

        }else{
            Utils.showLogI("Data penjualan dblocal Kosong!");
        }

        riwayatJualAdapter();
    }


    public void addTextListener(){

        editTextSearch.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){

            }


            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count){
                query = query.toString().toLowerCase();
                transaksiPenjualanModels = new ArrayList<>();
                transaksiPenjualanModels.clear();
                for(int i = 0; i < transaksiPenjualanModelArrayList.size(); i++){
                    final String idJual = transaksiPenjualanModelArrayList.get(i).getId_jual().toLowerCase();
                    final String tanggal = transaksiPenjualanModelArrayList.get(i).getTanggal().toLowerCase();
                    if(idJual.contains(query) || tanggal.contains(query)){
                        transaksiPenjualanModels.add(transaksiPenjualanModelArrayList.get(i));
                    }
                }

                initListSearch();
            }


            @Override
            public void afterTextChanged(Editable s){

            }
        });
    }

    public static void getDataPenjualanSuccess(){
        getDataPenjualan();
    }

    private void setFontView(){
        textTanggal.setTypeface(FontUtils.getFontHeaderToolbar(getActivity()), Typeface.BOLD);
        textStatus.setTypeface(FontUtils.getFontHeaderToolbar(getActivity()), Typeface.BOLD);
        textIdJual.setTypeface(FontUtils.getFontHeaderToolbar(getActivity()), Typeface.BOLD);
    }


}
