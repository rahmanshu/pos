package com.trimitrasis.finosapps.Views.PosView;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.ContentProvider.TransJualProvider;
import com.trimitrasis.finosapps.Views.PosView.Adapter.RiwayatPenjualanAdapter;
import com.trimitrasis.finosapps.Views.PosView.Model.TransaksiPenjualanModel;
import com.trimitrasis.finosapps.Views.UtilView.Constants;
import com.trimitrasis.finosapps.Views.UtilView.Utils;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;
import java.util.ArrayList;

/**
 * Created by rahman on 19/04/2017.
 */

@EActivity(R.layout.layout_riwayat_penjualan)
public class RiwayatPenjualanActivity extends AppCompatActivity {

    @ViewById
    Toolbar toolbar;

    @ViewById
    RecyclerView listRiwayatPenjualan;

    RecyclerView.LayoutManager mLayoutManager;

    ArrayList<TransaksiPenjualanModel> transaksiPenjualanModelArrayList;


    @AfterViews
    void afterView(){
        transaksiPenjualanModelArrayList = new ArrayList<>();
        transaksiPenjualanModelArrayList.clear();

        initSetTableView();
        customView();
        initTabbar();
        getDataPenjualan();
    }


    private void customView(){
        TextView toolbarView = (TextView) toolbar.findViewById(R.id.headerText);
        toolbarView.setText("Riwayat Penjualan");
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

    private void initSetTableView(){
        listRiwayatPenjualan.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        listRiwayatPenjualan.setLayoutManager(mLayoutManager);
    }


    private void riwayatJualAdapter(){
        RiwayatPenjualanAdapter adapter = new RiwayatPenjualanAdapter(this, transaksiPenjualanModelArrayList);
        listRiwayatPenjualan.setAdapter(adapter);
    }


    private void getDataPenjualan(){

        String id_member = "",  shift_id = "", id_reference = "", detail_id = "", promo = "", barcode = "";
        String id_jual_detail = "", item_code = "", description = "", satuan = "", tax_type = "";
        double harga = 0.0, hargaMember = 0.0, qty = 0.0, discount = 0.0, total = 0.0;
        double disc_percent = 0.0, disc_amount = 0.0, hargaHpp = 0.0;

        transaksiPenjualanModelArrayList.clear();
        String URL = "content://com.trimitrasis.finosapps.ContentProvider.TransJualProvider/transjual";
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

        riwayatJualAdapter();

    }


}
