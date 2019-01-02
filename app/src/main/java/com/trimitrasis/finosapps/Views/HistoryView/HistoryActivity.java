package com.trimitrasis.finosapps.Views.HistoryView;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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
import android.widget.EditText;
import android.widget.TextView;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.ContentProvider.TransJualDetailProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.TransJualDetailVoidProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.TransJualProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.TransJualVoidProvider;
import com.trimitrasis.finosapps.Views.HistoryView.Adapter.RiwayatDetailAdapter;
import com.trimitrasis.finosapps.Views.HistoryView.Adapter.RiwayatPenjualanAdapterSuccess;
import com.trimitrasis.finosapps.Views.HistoryView.Adapter.RiwayatPenjualanAdapterVoid;
import com.trimitrasis.finosapps.Views.HistoryView.Fragment.FragmentHistorySuccess;
import com.trimitrasis.finosapps.Views.HistoryView.Fragment.FragmentHistorySuccess_;
import com.trimitrasis.finosapps.Views.HistoryView.Fragment.FragmentHistoryVoid;
import com.trimitrasis.finosapps.Views.HistoryView.Fragment.FragmentHistoryVoid_;
import com.trimitrasis.finosapps.Views.PosView.Adapter.RiwayatPenjualanAdapter;
import com.trimitrasis.finosapps.Views.PosView.Model.RingkasanOrderModel;
import com.trimitrasis.finosapps.Views.PosView.Model.TransaksiPenjualanModel;
import com.trimitrasis.finosapps.Views.PrintView.PrintBillActivity_;
import com.trimitrasis.finosapps.Views.PrintView.PrintDapurActivity_;
import com.trimitrasis.finosapps.Views.UtilView.Constants;
import com.trimitrasis.finosapps.Views.UtilView.FontUtils;
import com.trimitrasis.finosapps.Views.UtilView.Utils;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by rahman on 10/24/2017.
 */

@EActivity(R.layout.layout_riwayat_penjualan_new)
public class HistoryActivity extends AppCompatActivity implements
        RiwayatPenjualanAdapterSuccess.CallbackListRiwayatSuccess,
        RiwayatPenjualanAdapterVoid.CallbackListRiwayatVoid{


    @ViewById
    Toolbar toolbar;

    @ViewById
    EditText textInfoRiwayat;

    @ViewById
    TextView labelInfoTrans, textKasir, textNoTrans, textJenisBayar, labelPesanan, textPpnTab, textSubTotal, textTotalBayar;

    @ViewById
    RecyclerView  listDetailJual;

    @ViewById
    RecyclerView listRiwayatPenjualan;

    @ViewById
    Button btnVoidTrans, btnPrintHistory;

    @ViewById
    TabLayout tabs;

    @ViewById
    ViewPager viewpager;

    int currentFragmentIdx = 0;
    Adapter adapter;

    Dialog dialogPilihPrinter;
    LayoutInflater inflater;
    TextView textCetakStruk, textCetakDapur;

    RecyclerView.LayoutManager mLayoutManager;

    ArrayList<RingkasanOrderModel> ringkasanOrderModels;
    TransaksiPenjualanModel transJualObject = new TransaksiPenjualanModel();

    ArrayList<TransaksiPenjualanModel> transaksiPenjualanModelArrayList; //hand set

    String idJual = "";
    double total_bayar_ = 0;

    @AfterViews
    void afterView(){
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ringkasanOrderModels = new ArrayList<>();
        ringkasanOrderModels.clear();
        transaksiPenjualanModelArrayList = new ArrayList<>();
        transaksiPenjualanModelArrayList.clear();
        initTabbar();
        customView();

        if(listDetailJual == null){ //handset
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            initSetTableView();
            getDataPenjualanHandset();
        }else{ //tab
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            initPager();
            initSetTableViewDetail();
            setFontViewTab();
        }


    }


    private void customView(){
        TextView toolbarView = (TextView) toolbar.findViewById(R.id.headerText);
        toolbarView.setText("Riwayat Penjualan");
    }


    private void setFontViewTab(){
        textInfoRiwayat.setTypeface(FontUtils.getFontHeaderToolbar(this));
        labelInfoTrans.setTypeface(FontUtils.getFontHeaderToolbar(this));
        textKasir.setTypeface(FontUtils.getFontHeaderToolbar(this), Typeface.BOLD);
        textNoTrans.setTypeface(FontUtils.getFontHeaderToolbar(this), Typeface.BOLD);
        textJenisBayar.setTypeface(FontUtils.getFontHeaderToolbar(this), Typeface.BOLD);
        labelPesanan.setTypeface(FontUtils.getFontHeaderToolbar(this));

        if(idJual.toString().equals("")){
            btnVoidTrans.setEnabled(false);
        }
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



    private void initSetTableView(){ //hand set
        listRiwayatPenjualan.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        listRiwayatPenjualan.setLayoutManager(mLayoutManager);
    }


    private void riwayatJualAdapter(){  //hand set
        RiwayatPenjualanAdapter adapter = new RiwayatPenjualanAdapter(this, transaksiPenjualanModelArrayList);
        listRiwayatPenjualan.setAdapter(adapter);
    }


    private void initSetTableViewDetail(){   //tab
        listDetailJual.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        listDetailJual.setLayoutManager(mLayoutManager);
    }


    private void initListHistoryDetail(){
        RiwayatDetailAdapter adapter = new RiwayatDetailAdapter(this, ringkasanOrderModels);
        listDetailJual.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    private void getDetailTransSuccess(String idJual){

        String nama_barang = "", itemId = "", kodeBarcode = "", kodeBarang = "", satuanBarang = "";
        String information = "", taxGroup = "", note = "", detailId = ""; String URL = "";
        double harga_jual = 0; double qty = 0; double diskon = 0; double tax = 0; double standart_cost = 0;
        boolean flag_qty = false, flag_bom = false;
        ringkasanOrderModels.clear();

        URL = "content://com.trimitrasis.finosapps.ContentProvider.TransJualDetailProvider/transjualdetail";
        Uri uri_   = Uri.parse(URL);
        Cursor c   = getContentResolver().query(uri_, null, "id_jual_detail " + " = '" +  idJual + "'", null, "id_jual_item ASC");

        if(c.moveToFirst()){
            do{
                itemId          = (!c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_ITEM_CODE)).equals("") ? c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_ITEM_CODE)) : "");
                kodeBarcode     = (!c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_BARCODE)).equals("") ? c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_BARCODE)) : "");
                kodeBarang      = (!c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_ITEM_CODE)).equals("") ? c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_ITEM_CODE)) : "");
                satuanBarang    = (!c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_SATUAN)).equals("") ? c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_SATUAN)) : "");
                nama_barang     = (!c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_DESCRIPTION)).equals("") ? c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_DESCRIPTION)) : "");
                qty             = (!c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_QTY)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_QTY))) : 0;
                harga_jual      = (!c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_HARGA)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_HARGA))) : 0;
                diskon          = (!c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_DISCOUNT)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_DISCOUNT))) : 0;
                standart_cost   = (!c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_HARGA_HPP)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_HARGA_HPP))) : 0;

                ringkasanOrderModels.add(new RingkasanOrderModel(itemId, kodeBarcode, kodeBarang, nama_barang, satuanBarang, harga_jual,
                        qty, diskon, tax, information, standart_cost, flag_qty, flag_bom, taxGroup, note, detailId));

            }while (c.moveToNext());
            c.close();
        }else{
            Utils.showLogI("Data Detail History dblocal Kosong!");
        }

        initListHistoryDetail();

    }

    private void getDetailTransVoid(String idJual){

        String nama_barang = "", itemId = "", kodeBarcode = "", kodeBarang = "", satuanBarang = "";
        String information = "", taxGroup = "", note = "", detailId = ""; String URL = "";
        double harga_jual = 0; double qty = 0; double diskon = 0; double tax = 0; double standart_cost = 0;
        boolean flag_qty = false, flag_bom = false;
        ringkasanOrderModels.clear();


        URL = "content://com.trimitrasis.finosapps.ContentProvider.TransJualDetailVoidProvider/transjualdetailvoid";
        Uri uri_   = Uri.parse(URL);
        Cursor c   = getContentResolver().query(uri_, null, "id_jual_detail " + " = '" +  idJual + "'", null, "id_jual_item ASC");

        if(c.moveToFirst()){
            do{
                itemId          = (!c.getString(c.getColumnIndex(TransJualDetailVoidProvider.KEY_ITEM_CODE)).equals("") ? c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_ITEM_CODE)) : "");
                kodeBarcode     = (!c.getString(c.getColumnIndex(TransJualDetailVoidProvider.KEY_BARCODE)).equals("") ? c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_BARCODE)) : "");
                kodeBarang      = (!c.getString(c.getColumnIndex(TransJualDetailVoidProvider.KEY_ITEM_CODE)).equals("") ? c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_ITEM_CODE)) : "");
                satuanBarang    = (!c.getString(c.getColumnIndex(TransJualDetailVoidProvider.KEY_SATUAN)).equals("") ? c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_SATUAN)) : "");
                nama_barang     = (!c.getString(c.getColumnIndex(TransJualDetailVoidProvider.KEY_DESCRIPTION)).equals("") ? c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_DESCRIPTION)) : "");
                qty             = (!c.getString(c.getColumnIndex(TransJualDetailVoidProvider.KEY_QTY)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_QTY))) : 0;
                harga_jual      = (!c.getString(c.getColumnIndex(TransJualDetailVoidProvider.KEY_HARGA)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_HARGA))) : 0;
                diskon          = (!c.getString(c.getColumnIndex(TransJualDetailVoidProvider.KEY_DISCOUNT)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_DISCOUNT))) : 0;
                standart_cost   = (!c.getString(c.getColumnIndex(TransJualDetailVoidProvider.KEY_HARGA_HPP)).equals("")) ? Double.parseDouble(c.getString(c.getColumnIndex(TransJualDetailProvider.KEY_HARGA_HPP))) : 0;

                ringkasanOrderModels.add(new RingkasanOrderModel(itemId, kodeBarcode, kodeBarang, nama_barang, satuanBarang, harga_jual,
                        qty, diskon, tax, information, standart_cost, flag_qty, flag_bom, taxGroup, note, detailId));

            }while (c.moveToNext());
            c.close();
        }else{
            Utils.showLogI("Data Detail History dblocal Kosong!");
        }

        initListHistoryDetail();

    }



    private void viewTotal(TransaksiPenjualanModel transaksiPenjualanModel){
        textKasir.setText(transaksiPenjualanModel.getId_kasir());
        textNoTrans.setText(transaksiPenjualanModel.getId_jual());
        textJenisBayar.setText(transaksiPenjualanModel.getTanggal() + " " + transaksiPenjualanModel.getTime());
        textPpnTab.setText("" + Utils.getCurrencyRupiahTanpaSimbol(transaksiPenjualanModel.getTax()));
        textSubTotal.setText("" + Utils.getCurrencyRupiahTanpaSimbol(transaksiPenjualanModel.getSubTotal()));
        textTotalBayar.setText(""+ Utils.getCurrencyRupiahTanpaSimbol(transaksiPenjualanModel.getTotal_bayar()));
    }


    private void viewDefault(){
        textKasir.setText("Kasir");
        textNoTrans.setText("ID Jual");
        textJenisBayar.setText("Time");
        textPpnTab.setText("0");
        textSubTotal.setText("0");
        textTotalBayar.setText("0");
    }


    @Click
    void btnVoidTrans(){
        showDialogVoidTrans();
    }


    @Click
    void btnPrintHistory(){
        showDialogCetak();
    }


    private void showDialogVoidTrans(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah anda yakin ingin melakukan void transaksi ?")
           .setCancelable(false).setPositiveButton("Ya",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int id){

                if(!idJual.toString().equals("")){

                    int deleteTransJual       = getContentResolver().delete(TransJualProvider.CONTENT_URI, "id_jual " + " = '" + idJual + "'", null);
                    int deleteTransJualDetail = getContentResolver().delete(TransJualDetailProvider.CONTENT_URI, "id_jual_detail " + " = '" + idJual + "'", null);
                    addDataTransVoid();
                    addDataTransVoidDetail(idJual);
                    getDetailTransSuccess(idJual);
                    getDetailTransVoid(idJual);
                    viewDefault();
                    btnVoidTrans.setEnabled(false);
                    ringkasanOrderModels.clear();
                    FragmentHistoryVoid.getDataPenjualanVoid();
                    FragmentHistorySuccess.getDataPenjualanSuccess();

                }
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


    private void showDialogCetak(){
        dialogPilihPrinter = new Dialog(this);
        View view = inflater.inflate(R.layout.popup_pilih_printer, null);
        declareLayout(view);
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
                    PrintBillActivity_.intent(HistoryActivity.this).totab_bayar_(total_bayar_).nomorMeja("").ringkasanOrderModels(ringkasanOrderModels).start();
                    dialogPilihPrinter.dismiss();
                }break;
                case R.id.textCetakDapur:{
                    PrintDapurActivity_.intent(HistoryActivity.this).total_bayar_(total_bayar_).nomorMeja("").ringkasanOrderModels(ringkasanOrderModels).start();
                    dialogPilihPrinter.dismiss();
                }break;


            }
        }
    };

    @Override
    public void showListRiwayatSuccess(TransaksiPenjualanModel transaksiPenjualanModel) {
        btnVoidTrans.setEnabled(true);
        getDetailTransSuccess(transaksiPenjualanModel.getId_jual());
        viewTotal(transaksiPenjualanModel);
        idJual       = transaksiPenjualanModel.getId_jual();
        total_bayar_ = transaksiPenjualanModel.getTotal_bayar();
        transJualObject = transaksiPenjualanModel;
    }

    @Override
    public void showListRiwayatVoid(TransaksiPenjualanModel transaksiPenjualanModel){
        btnVoidTrans.setEnabled(false);
        getDetailTransVoid(transaksiPenjualanModel.getId_jual());
        viewTotal(transaksiPenjualanModel);
        idJual       = transaksiPenjualanModel.getId_jual();
        total_bayar_ = transaksiPenjualanModel.getTotal_bayar();
        transJualObject = transaksiPenjualanModel;
    }


    static class Adapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();
        Context c;

        public Adapter(Context c,FragmentManager fm) {
            super(fm);
            this.c = c;
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position){
            return mFragments.get(position);
        }


        @Override
        public int getCount(){
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position){
            return mFragmentTitles.get(position);
        }

    }


    private void initPager(){

        if(viewpager != null){
            setupViewPager(viewpager);
        }

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentFragmentIdx = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabs.setupWithViewPager(viewpager);
    }


    private void setupViewPager(ViewPager viewPager){
        adapter = new Adapter(this, getSupportFragmentManager());
        adapter.addFragment(FragmentHistorySuccess_.builder().build(), "Success");
        adapter.addFragment(FragmentHistoryVoid_.builder().build(), "Void");
        viewPager.setAdapter(adapter);
    }


    private void addDataTransVoid(){
        ContentValues values = new ContentValues();
        values.put(TransJualVoidProvider.KEY_ID_JUAL, transJualObject.getId_jual());
        values.put(TransJualVoidProvider.KEY_TOTAL_BAYAR, transJualObject.getTotal_bayar());
        values.put(TransJualVoidProvider.KEY_TIME, transJualObject.getTime());
        values.put(TransJualVoidProvider.KEY_TANGGAL, transJualObject.getTanggal());
        values.put(TransJualVoidProvider.KEY_ID_KASIR, transJualObject.getId_kasir());
        values.put(TransJualVoidProvider.KEY_SUBTOTAL, transJualObject.getSubTotal());
        values.put(TransJualVoidProvider.KEY_TAX, transJualObject.getTax());
        values.put(TransJualVoidProvider.KEY_TOTAL_DISCOUNT, transJualObject.getTotal_discount());
        values.put(TransJualVoidProvider.KEY_KEMBALIAN, transJualObject.getKembalian());
        Uri uri = getContentResolver().insert(TransJualVoidProvider.CONTENT_URI, values);
    }


    private void addDataTransVoidDetail(String idJual){
        if(ringkasanOrderModels != null){
            for(int i = 0; i < ringkasanOrderModels.size(); i++){
                ContentValues values = new ContentValues();
                values.put(TransJualDetailVoidProvider.KEY_ID_JUAL_DETAIL, idJual);
                values.put(TransJualDetailVoidProvider.KEY_BARCODE, ringkasanOrderModels.get(i).getKodeBarcode());
                values.put(TransJualDetailVoidProvider.KEY_ITEM_CODE, ringkasanOrderModels.get(i).getItemId());
                values.put(TransJualDetailVoidProvider.KEY_QTY, ringkasanOrderModels.get(i).getQty());
                values.put(TransJualDetailVoidProvider.KEY_HARGA, ringkasanOrderModels.get(i).getHargaJual());
                values.put(TransJualDetailVoidProvider.KEY_SATUAN, ringkasanOrderModels.get(i).getSatuanBarang());
                values.put(TransJualDetailVoidProvider.KEY_DESCRIPTION, ringkasanOrderModels.get(i).getNamaBarang());
                values.put(TransJualDetailVoidProvider.KEY_DISCOUNT, ringkasanOrderModels.get(i).getDiskon());
                values.put(TransJualDetailVoidProvider.KEY_HARGA_HPP, ringkasanOrderModels.get(i).getStandart_cost());
                Uri uri = getContentResolver().insert(TransJualDetailVoidProvider.CONTENT_URI, values);
            }
        }
    }


    private void getDataPenjualanHandset(){

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
