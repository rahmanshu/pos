package com.trimitrasis.finosapps;
import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.trimitrasis.finosapps.Connection.ApiConnection;
import com.trimitrasis.finosapps.Connection.Models.UserInfoModel;
import com.trimitrasis.finosapps.Views.ContentProvider.BomProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.CategoryProductProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.ItemProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.PromosiBarangProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.PromosiHadiahProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.PromosiProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.TaxGroupProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.TaxProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.TransJualBayarProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.TransJualDetailProvider;
import com.trimitrasis.finosapps.Views.ContentProvider.TransJualProvider;
import com.trimitrasis.finosapps.Views.DeliveryOrderView.DeliveryOrderActivity_;
import com.trimitrasis.finosapps.Views.HistoryView.HistoryActivity_;
import com.trimitrasis.finosapps.Views.HomeView.Adater.SidebarAdapter;
import com.trimitrasis.finosapps.Views.HomeView.HomeFragment;
import com.trimitrasis.finosapps.Views.HomeView.HomeFragment_;
import com.trimitrasis.finosapps.Views.HomeView.SidebarObj;
import com.trimitrasis.finosapps.Views.InvoiceReceiptView.InvoiceReceiptActivity_;
import com.trimitrasis.finosapps.Views.MasterDataView.MainCurrencyActivity_;
import com.trimitrasis.finosapps.Views.MasterDataView.MainCustomerActivity_;
import com.trimitrasis.finosapps.Views.MasterDataView.MainItemActivity_;
import com.trimitrasis.finosapps.Views.MasterDataView.MainVendorActivity_;
import com.trimitrasis.finosapps.Views.MasterDataView.MasterVendorActivity_;
import com.trimitrasis.finosapps.Views.PosView.EndOfDayActivity_;
import com.trimitrasis.finosapps.Views.PosView.EndOfShiftActivity_;
import com.trimitrasis.finosapps.Views.PosView.HoldPenjualanActivity_;
import com.trimitrasis.finosapps.Views.PosView.Model.SyncTransModel;
import com.trimitrasis.finosapps.Views.PosView.Model.TransaksiJualBayarModel;
import com.trimitrasis.finosapps.Views.PosView.Model.TransaksiPenjualanModel;
import com.trimitrasis.finosapps.Views.PosView.RiwayatPenjualanActivity_;
import com.trimitrasis.finosapps.Views.PosView.StartOfDayActivity_;
import com.trimitrasis.finosapps.Views.PosView.StartOfShiftNewActivity_;
import com.trimitrasis.finosapps.Views.PrintView.ConnectPrinterStrukActivity_;
import com.trimitrasis.finosapps.Views.PrintView.DeviceListStrukActivity;
import com.trimitrasis.finosapps.Views.SalesOrderView.SalesOrderActivity_;
import com.trimitrasis.finosapps.Views.SalesQuotationView.SalesQuotationActivity_;
import com.trimitrasis.finosapps.Views.ServiceProvider.DownloadService;
import com.trimitrasis.finosapps.Views.SettingView.SettingActivity_;
import com.trimitrasis.finosapps.Views.UserProfileView.UserProfileActivity_;
import com.trimitrasis.finosapps.Views.UtilView.Constants;
import com.trimitrasis.finosapps.Views.UtilView.FontUtils;
import com.trimitrasis.finosapps.Views.UtilView.Utils;
import com.zj.btsdk.BluetoothService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.util.ArrayList;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;



/**
 * Created by rahman on 03/03/2017.
 */


@EActivity(R.layout.layout_dashboard)
@OptionsMenu(R.menu.menu_main)
public class MainActivity extends AppCompatActivity implements HomeFragment.MenuButtonListener{

    @ViewById
    Toolbar toolbar;

    @ViewById
    DrawerLayout drawerLayout;

    @ViewById
    ListView listSidebar;

    @ViewById
    ListView notifList;

    @ViewById
    View headerSidebar;

    @ViewById
    ProgressBar progress;

    @ViewById
    TextView progress_text;

    @Extra
    UserInfoModel userInfoModel;


    Fragment currentFragment;

    EditText editSearch;
    FrameLayout thumbImageLayout;
    TextView textUsernameProfil, textEmailProfil;

    ArrayList<SidebarObj> sidebarObjs;
    ArrayList<SidebarObj> arrSidebarObjs;
    ActionBarDrawerToggle drawerToggle;
    SessionManager sessionManager;

    ArrayList<TransaksiPenjualanModel> transaksiPenjualanModels;
    ArrayList<TransaksiJualBayarModel> transaksiJualBayarModels;

    ProgressDialog progressDialog;   ProgressDialog progressDialog2;

    public static final String MESSAGE_PROGRESS = "message_progress";
    private static final int PERMISSION_REQUEST_CODE = 1;


    @AfterViews
    void afterView(){

        registerReceiver();
        transaksiPenjualanModels = new ArrayList<>();
        transaksiPenjualanModels.clear();
        transaksiJualBayarModels = new ArrayList<>();
        transaksiJualBayarModels.clear();

        sidebarObjs = new ArrayList<>();
        sidebarObjs.clear();
        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.checkLogin();
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open,R.string.drawer_close);
        drawerToggle.syncState();
        setSupportActionBar(toolbar);
        initSidebarFirst();
        customView();
        setDataProfile();
        addTextListener();
        attachHomeFragment();

        progress.setVisibility(View.GONE);
        progress_text.setVisibility(View.GONE);

        //new
        cekPrinter();
    }



    private void customView(){
        editSearch         = (EditText)    headerSidebar.findViewById(R.id.searchHeaderSidebar);
        thumbImageLayout   = (FrameLayout) headerSidebar.findViewById(R.id.thumbImageLayout);
        textUsernameProfil = (TextView)    headerSidebar.findViewById(R.id.textUsernameProfil);
        textEmailProfil    = (TextView)    headerSidebar.findViewById(R.id.textEmailProfil);
        thumbImageLayout.setOnClickListener(onClickListener);
    }


    private void setDataProfile(){
        textUsernameProfil.setText(Constants.userInfoModel.getComname());
        textEmailProfil.setText(Constants.userInfoModel.getEmail());
    }


    private void cekPrinter(){
        if(Constants.printer_address_struk != ""){

        }else{
            showAlertDialog("Silahkan lakukan setting printer terlebih dahulu pada menu setting !");
        }
    }





    long lastPress;
    @Override
    public void onBackPressed(){
        long currentTime = System.currentTimeMillis();
        if(currentTime - lastPress > 5000){
            Utils.showToastLong("Tekan sekali lagi untuk menutup aplikasi.",this);
            lastPress = currentTime;
        }else super.onBackPressed();
    }


    private void initSidebarFirst(){
        final SidebarAdapter adapter = new SidebarAdapter(this,getSidebarObjs());
        listSidebar.setAdapter(adapter);
        listSidebar.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                switch(adapter.getItem(position).getLabel()){
                    case "Start Of Day":
                        StartOfDayActivity_.intent(MainActivity.this).start();
                        break;
                    case "Start Of Shift":
                        StartOfShiftNewActivity_.intent(MainActivity.this).start();
                        break;
                    case "End Of Shift":
                        EndOfShiftActivity_.intent(MainActivity.this).start();
                        break;
                    case "End Of Day":
                        EndOfDayActivity_.intent(MainActivity.this).start();
                        break;
                    case "Hold Sales":
                        HoldPenjualanActivity_.intent(MainActivity.this).start();
                        break;
                    case "Invoice Receipt":
                        InvoiceReceiptActivity_.intent(MainActivity.this).start();
                        break;
                    case "Sales Quotation":
                        SalesQuotationActivity_.intent(MainActivity.this).start();
                        break;
                    case "Sales Order":
                        SalesOrderActivity_.intent(MainActivity.this).start();
                        break;
                    case "Delivery Order":
                        DeliveryOrderActivity_.intent(MainActivity.this).start();
                        break;
                    case "Master Customer":
                        MainCustomerActivity_.intent(MainActivity.this).start();
                        break;
                    case "Master Vendor":
                        MainVendorActivity_.intent(MainActivity.this).start();
                        break;
                    case "Master Currency":
                        MainCurrencyActivity_.intent(MainActivity.this).start();
                        break;
                    case "Master Item":
                        MainItemActivity_.intent(MainActivity.this).start();
                        break;
                    case "Point Of Sales":
                        StartOfShiftNewActivity_.intent(MainActivity.this).start();
                        break;
                    case "Setting":
                        SettingActivity_.intent(MainActivity.this).start();
                        break;
                    case "History":
                        HistoryActivity_.intent(MainActivity.this).start();
                        break;
                    case "Sync Image":
                        if(checkPermission()){
                            startDownload();
                        }else{
                            requestPermission();
                        }
                        break;
                    case "Log Out":
                        sessionManager.logoutUser();
                        finish();
                        break;
                    default:
                        break;
                }

                homeClick();

            }
        });
    }


    private void initSidebarSearch(){
        final SidebarAdapter adapter = new SidebarAdapter(this, arrSidebarObjs);
        listSidebar.setAdapter(adapter);
        listSidebar.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                switch(adapter.getItem(position).getLabel()){
                    case "Start Of Day":
                        StartOfDayActivity_.intent(MainActivity.this).start();
                        break;
                    case "Start Of Shift":
                        StartOfShiftNewActivity_.intent(MainActivity.this).start();
                        break;
                    case "End Of Shift":
                        EndOfShiftActivity_.intent(MainActivity.this).start();
                        break;
                    case "End Of Day":
                        EndOfDayActivity_.intent(MainActivity.this).start();
                        break;
                    case "Hold Sales":
                        HoldPenjualanActivity_.intent(MainActivity.this).start();
                        break;
                    case "Invoice Receipt":
                        InvoiceReceiptActivity_.intent(MainActivity.this).start();
                        break;
                    case "Sales Quotation":
                        SalesQuotationActivity_.intent(MainActivity.this).start();
                        break;
                    case "Sales Order":
                        SalesOrderActivity_.intent(MainActivity.this).start();
                        break;
                    case "Delivery Order":
                        DeliveryOrderActivity_.intent(MainActivity.this).start();
                        break;
                    case "Master Customer":
                        MainCustomerActivity_.intent(MainActivity.this).start();
                        break;
                    case "Master Vendor":
                        MasterVendorActivity_.intent(MainActivity.this).start();
                        break;
                    case "Master Currency":
                        MainCurrencyActivity_.intent(MainActivity.this).start();
                        break;
                    case "Master Item":
                        MainItemActivity_.intent(MainActivity.this).start();
                        break;
                    case "Point Of Sales":
                        StartOfShiftNewActivity_.intent(MainActivity.this).start();
                        break;
                    case "Setting":
                        SettingActivity_.intent(MainActivity.this).start();
                        break;
                    case "History":
                        HistoryActivity_.intent(MainActivity.this).start();
                        break;
                    case "Sync Image":
                        if(checkPermission()){
                            startDownload();
                        }else{
                            requestPermission();
                        }
                        break;
                    case "Log Out":

                        sessionManager.logoutUser();
                        finish();
                        break;
                    default:
                        break;
                }

                homeClick();

            }
        });
    }


    private ArrayList<SidebarObj> getSidebarObjs(){
        //sidebarObjs.add(new SidebarObj(R.mipmap.images_start_of_day2_hover, "Start Of Day", FontUtils.getHelvetica_Neue_LT(this)));
        //sidebarObjs.add(new SidebarObj(R.mipmap.images_start_of_shift_hover, "Start Of Shift", FontUtils.getHelvetica_Neue_LT(this)));
        //sidebarObjs.add(new SidebarObj(R.mipmap.images_end_of_shift_hover, "End Of Shift", FontUtils.getHelvetica_Neue_LT(this)));
        //sidebarObjs.add(new SidebarObj(R.mipmap.images_end_of_shift_hover, "End Of Day", FontUtils.getHelvetica_Neue_LT(this)));
        //sidebarObjs.add(new SidebarObj(R.mipmap.images_hold_sales_hover, "Hold Sales", FontUtils.getHelvetica_Neue_LT(this)));
        //sidebarObjs.add(new SidebarObj(R.mipmap.images_point_of_sales_hover, "Point Of Sales", FontUtils.getHelvetica_Neue_LT(this)));
        //sidebarObjs.add(new SidebarObj(R.mipmap.images_settings_hover, "Setting", FontUtils.getHelvetica_Neue_LT(this)));
        sidebarObjs.add(new SidebarObj(R.mipmap.img_history, "History", FontUtils.getHelvetica_Neue_LT(this)));
        sidebarObjs.add(new SidebarObj(R.mipmap.images_sincronize_hover, "Sync Image", FontUtils.getHelvetica_Neue_LT(this)));
        //sidebarObjs.add(new SidebarObj(R.mipmap.ic_menu_sidebar_invoice_receipt, "Invoice Receipt", FontUtils.getHelvetica_Neue_LT(this)));
        //sidebarObjs.add(new SidebarObj(R.mipmap.ic_menu_sidebar_register_arap, "Register AR & AP", FontUtils.getHelvetica_Neue_LT(this)));
        //sidebarObjs.add(new SidebarObj(R.mipmap.ic_menu_sidebar_monitoring_arap, "Monitoring AR & AP", FontUtils.getHelvetica_Neue_LT(this)));
        //sidebarObjs.add(new SidebarObj(R.mipmap.ic_menu_sidebar_report_fi, "Reporting", FontUtils.getHelvetica_Neue_LT(this)));
        sidebarObjs.add(new SidebarObj(R.mipmap.ic_menu_sidebar_sales_quotation, "Sales Quotation", FontUtils.getHelvetica_Neue_LT(this)));
        sidebarObjs.add(new SidebarObj(R.mipmap.ic_menu_sidebar_sales_order_, "Sales Order", FontUtils.getHelvetica_Neue_LT(this)));
        sidebarObjs.add(new SidebarObj(R.mipmap.ic_menu_sidebar_sales_quotation, "Delivery Order", FontUtils.getHelvetica_Neue_LT(this)));
        sidebarObjs.add(new SidebarObj(R.mipmap.ic_menu_sidebar_master_customer, "Master Customer", FontUtils.getHelvetica_Neue_LT(this)));
        sidebarObjs.add(new SidebarObj(R.mipmap.ic_menu_sidebar_currency, "Master Currency", FontUtils.getHelvetica_Neue_LT(this)));
        sidebarObjs.add(new SidebarObj(R.mipmap.ic_menu_sidebar_master_contact, "Master Vendor", FontUtils.getHelvetica_Neue_LT(this)));
        sidebarObjs.add(new SidebarObj(R.mipmap.ic_menu_sidebar_item, "Master Item", FontUtils.getHelvetica_Neue_LT(this)));
        //sidebarObjs.add(new SidebarObj(R.mipmap.ic_menu_sidebar_reporting_mm, "Reporting", FontUtils.getHelvetica_Neue_LT(this)));
        //sidebarObjs.add(new SidebarObj(R.mipmap.ic_menu_sidebar_point_of_sales, "Point Of Sales", FontUtils.getHelvetica_Neue_LT(this)));
        //sidebarObjs.add(new SidebarObj(R.mipmap.ic_menu_sidebar_finance_dashboard, "Finance Dashboard", FontUtils.getHelvetica_Neue_LT(this)));
        //sidebarObjs.add(new SidebarObj(R.mipmap.ic_menu_sidebar_sales_achivment, "Sales Achievement", FontUtils.getHelvetica_Neue_LT(this)));
        //sidebarObjs.add(new SidebarObj(R.mipmap.ic_sales_quotation, "Stock Control", FontUtils.getHelvetica_Neue_LT(this)));
        //sidebarObjs.add(new SidebarObj(R.mipmap.ic_menu_sidebar_setting, "Setting", FontUtils.getHelvetica_Neue_LT(this)));
        sidebarObjs.add(new SidebarObj(R.mipmap.ic_menu_sidebar_close, "Log Out", FontUtils.getHelvetica_Neue_LT(this)));
        return sidebarObjs;
    }


    @OptionsItem(android.R.id.home)
    void homeClick(){
        if(drawerLayout.isDrawerOpen(Gravity.LEFT)){
            drawerLayout.closeDrawer(Gravity.LEFT);
        }else{
            if (drawerLayout.isDrawerOpen(Gravity.RIGHT)){
                drawerLayout.closeDrawer(Gravity.RIGHT);
            }
            drawerLayout.openDrawer(Gravity.LEFT);
        }
    }


    @OptionsItem(R.id.actionShoping)
    void shoppingBar(){
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)){
            drawerLayout.closeDrawer(Gravity.RIGHT);
        }else {
            if (drawerLayout.isDrawerOpen(Gravity.LEFT)){
                drawerLayout.closeDrawer(Gravity.LEFT);
            }
            drawerLayout.openDrawer(Gravity.RIGHT);
        }
    }


    public void addTextListener(){
        editSearch.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count){
                query = query.toString().toLowerCase();
                arrSidebarObjs = new ArrayList<>();
                arrSidebarObjs.clear();
                for(int i = 0; i < sidebarObjs.size(); i++){
                    final String labelName = sidebarObjs.get(i).getLabel().toLowerCase();
                    if(labelName.contains(query)){
                        arrSidebarObjs.add(new SidebarObj(sidebarObjs.get(i).getResIcon(), sidebarObjs.get(i).getLabel(), sidebarObjs.get(i).getTypeface()));
                    }
                }

                initSidebarSearch();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            switch (v.getId()) {
                case R.id.thumbImageLayout:{
                    UserProfileActivity_.intent(MainActivity.this).userInfoModel(userInfoModel).start();
                }break;
            }
            homeClick();
        }
    };



    private void attachHomeFragment(){
        if (currentFragment == null || !(currentFragment instanceof HomeFragment_)){
            attachFragment(HomeFragment_.class, "home_fragment");
        }
    }



    private void attachFragment(Class c,String tag) {

        Object newFragment = null;
        try{
            newFragment = c.newInstance();
        }catch (InstantiationException ex) {
            Log.e("InstantiationException", "Cannot create new Fragment " + c.getName());
        }catch (IllegalAccessException ex) {
            Log.e("IllegalAccessException", "Cannot create new Fragment " + c.getName());
        }catch (NullPointerException e) {
            Log.e("NullPointerException","null pointer : "+e.getMessage());
        }

        if(newFragment != null){
            currentFragment = (Fragment) newFragment;

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.menuContentLayout, (Fragment) newFragment, tag)
                    .commit();

            invalidateOptionsMenu();
        }
    }


    @Override
    public void onClickHome(int idMenu){
        switch (idMenu){
            case Constants.START_OF_DAY_MENU:
                StartOfDayActivity_.intent(MainActivity.this).start();
                break;
            case Constants.START_OF_SHIFT_MENU:
                StartOfShiftNewActivity_.intent(MainActivity.this).start();
                break;
            case Constants.END_OF_SHIFT_MENU:
                EndOfShiftActivity_.intent(MainActivity.this).start();
                break;
            case Constants.END_OF_DAY_MENU:
                EndOfDayActivity_.intent(MainActivity.this).start();
                break;
            case Constants.POS_MENU:
                StartOfShiftNewActivity_.intent(MainActivity.this).start();
                break;
            case Constants.SYNC_TRANS_MENU:
                getDataJualBayar();
                getDataTransFromLocal();
                break;
            case Constants.HOLD_SALES_MENU:
                HoldPenjualanActivity_.intent(MainActivity.this).start();
                break;
            case Constants.PENGATURAN_MENU:
                SettingActivity_.intent(MainActivity.this).start();
                break;
            case Constants.SINKRON_MENU:

                if(Utils.cek_status((this))){
                    startThread();
                }else{
                    showDialogConnection("Error Connection!");
                }
                break;
        }
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
                        id_jual_bayar, tipe, bank_id, bank_name, no_kartu,
                        status_sinc, total_bayar, Constants.userInfoModel.getLocation(), kembalian
                ));


            }while (c.moveToNext());
            c.close();
        }else{
            Utils.showLogI("Data jual bayar dblocal Kosong!");
        }
    }



    private void getDataTransFromLocal(){

        String id_member = "", shift_id = "", id_reference = "";
        transaksiPenjualanModels.clear();
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

                transaksiPenjualanModels.add(new TransaksiPenjualanModel(
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


        if(Utils.cek_status(this)){
            postTransaksiManual();
        }else{
            showDialogConnection("Error Connection!");
        }

    }


    private void postTransaksiManual(){

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.show();

        ApiConnection.getSyncTransInterface().getSyncTrans(getSyncTrans(), new Callback<Response>(){
            @Override
            public void success(Response response, Response response2){
                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject sosObj = null;
                try{
                    sosObj = new JSONObject(rawResponse);
                    String jStatus  = sosObj.optString("message");

                    if(jStatus.equals("success")){
                        updateDataJual();
                        updateDataJualBayar();
                    }

                }catch(JSONException e){
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

                if (progressDialog !=null) progressDialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                if (progressDialog !=null) progressDialog.dismiss();
            }

        });
    }



    private SyncTransModel getSyncTrans(){
        SyncTransModel syncTransModel = new SyncTransModel(
                Constants.userInfoModel,
                transaksiPenjualanModels,
                transaksiJualBayarModels
        );
        return syncTransModel;
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

            }while(c.moveToNext());
            c.close();
        }

        showDialogSyncTrans("Berhasil melakukan Sync Transaksi!");
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

    }


    private void showDialogSyncTrans(String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
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


    public void startThread() {

        progressDialog2 = new ProgressDialog(this);
        progressDialog2.setMessage("Loading..");
        //progressDialog2.setCancelable(false);
        progressDialog2.show();

        new Thread(){
            public void run(){
                try{
                    sleep(4000);
                    getCategoryProduct();

                }catch(Exception e){
                    Log.e("threadmessage",e.getMessage());
                }
            }
        }.start();
    }


    private void getCategoryProduct(){
        ApiConnection.getDataItemHierarchyInterface().getItemHierarchy(Constants.userInfoModel, new Callback<Response>(){
            @Override
            public void success(Response response, Response response2){
                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject vendorObj = null;
                try {
                    vendorObj = new JSONObject(rawResponse);
                    String jStatus    = vendorObj.optString("message");
                    JSONArray jResult = vendorObj.optJSONArray("data");

                    if(jStatus.equals("success")){
                        int emptyTableCategory = getContentResolver().delete(CategoryProductProvider.CONTENT_URI, null, null);
                        if(jResult != null) {
                            for(int i = 0; i < jResult.length(); i++){
                                ContentValues values  = new ContentValues();
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
                        Utils.showLogI("Syncronisasi category Empty!");
                        int emptyTableCategory = getContentResolver().delete(CategoryProductProvider.CONTENT_URI, null, null);
                    }

                }catch(JSONException e){
                    e.printStackTrace();
                    Utils.showLogI("Error JSON : " + e.getMessage());
                }
            }

            @Override
            public void failure(RetrofitError error){
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



    public void getAllProduk(){
        ApiConnection.getViewDataItemInterface().getAllDataProduct(Constants.userInfoModel, new Callback<Response>(){

            @Override
            public void success(Response response, Response response2){

                boolean flag_qty, flag_sell, flag_bom;
                double uom_convertion = 0, base_uom_convertion = 0, harga_jual = 0;
                String fileExt = "", file_name = "", fullPath = "",  filePath = "", detail_id = "";
                String id = "", s_description = "", base_uom = "", item_group = "", item_hierarchy = "", uom = "";
                String barcode = "", tax_group = "", strMongoId = "", uom_name = "", item_hierarchy_cat = "";
                String standart_cost = "";

                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject vendorObj = null;

                try {
                    vendorObj = new JSONObject(rawResponse);
                    String jStatus  = vendorObj.optString("message");
                    JSONArray jResult = vendorObj.optJSONArray("result");

                    if(jStatus.equals("success")){

                        try{
                            int emptyTableProduk = getContentResolver().delete(ItemProvider.CONTENT_URI, null, null);
                            if(jResult != null){
                                for (int i = 0; i < jResult.length(); i++){

                                    ContentValues values = new ContentValues();
                                    JSONObject jsonObject = jResult.optJSONObject(i);
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
                                        for (int j = 0; j < jsonArrItemConvertion.length(); j++){
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

                        }catch (Exception e){
                            e.getMessage();
                            Utils.showLogI("Error Exception : " + e.getMessage());
                        }

                    }else if(jStatus.equals("failed")){
                        int emptyTableProduk = getContentResolver().delete(ItemProvider.CONTENT_URI, null, null);
                        Utils.showLogI("Error Failed : " + "Data Product Empty");
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

        getAllPromo();
    }



    public void getAllPromo(){
        ApiConnection.getViewDataPromoInterface().getViewDataPromo(Constants.userInfoModel, new Callback<Response>(){

            @Override
            public void success(Response response, Response response2){
                String  nama_promosi, from_, to_, loc_code, jenis_promo, code, strMongoId;

                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject vendorObj = null;
                try {
                    vendorObj = new JSONObject(rawResponse);
                    String jStatus  = vendorObj.optString("message");
                    JSONArray jResult = vendorObj.optJSONArray("result");

                    if(jStatus.equals("success")){

                        int emptyTablePromo = getContentResolver().delete(PromosiProvider.CONTENT_URI, null, null);
                        if(jResult != null) {
                            for(int i = 0; i < jResult.length(); i++) {
                                ContentValues values = new ContentValues();
                                JSONObject jsonObject = jResult.optJSONObject(i);

                                JSONObject idObj = (JSONObject) jsonObject.get("_id");
                                strMongoId   = (String) idObj.get("$id");
                                code         = jsonObject.optString("code");
                                nama_promosi = jsonObject.optString("description");
                                from_        = jsonObject.optString("start_date");
                                to_          = jsonObject.optString("end_date");
                                loc_code     = jsonObject.optString("location");
                                jenis_promo  = jsonObject.optString("type");

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
                        Utils.showLogI("Error Failed : " + "Data Promo Empty");
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




    public void getAllPromoBarang(){

        ApiConnection.getViewDataPromoBarangInterface().getViewDataPromoBarang(Constants.userInfoModel, new Callback<Response>(){
            @Override
            public void success(Response response, Response response2){
                String  kode_promosi, kode_item, item_name, discount_percent, discount_amount, qty, barcode;

                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject vendorObj = null;
                try{
                    vendorObj = new JSONObject(rawResponse);
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
                                    qty              = jsonObject.optString("qty");
                                    item_name        = jsonObject.optString("item_name");
                                    discount_amount  = jsonObject.optString("discount_amount");
                                    discount_percent = jsonObject.optString("discount_percent");
                                    barcode          = jsonObject.optString("barcode");

                                    values.put(PromosiBarangProvider.KEY_KODE_PROMO, kode_promosi);
                                    values.put(PromosiBarangProvider.KEY_KODE_ITEM, kode_item);
                                    values.put(PromosiBarangProvider.KEY_ITEM_NAME, item_name);
                                    values.put(PromosiBarangProvider.KEY_BARCODE, barcode);

                                    if (!qty.toString().equals("")) {
                                        values.put(PromosiBarangProvider.KEY_QTY_BARANG, Double.parseDouble(qty));
                                    } else {
                                        values.put(PromosiBarangProvider.KEY_QTY_BARANG, 0);
                                    }

                                    if (!discount_percent.toString().equals("")) {
                                        values.put(PromosiBarangProvider.KEY_DISCOUNT_PERCENT, Double.parseDouble(discount_percent));
                                    } else {
                                        values.put(PromosiBarangProvider.KEY_DISCOUNT_PERCENT, 0);
                                    }

                                    if (!discount_amount.toString().equals("")) {
                                        values.put(PromosiBarangProvider.KEY_DISCOUNT_AMOUNT, Double.parseDouble(discount_amount));
                                    } else {
                                        values.put(PromosiBarangProvider.KEY_DISCOUNT_AMOUNT, 0);
                                    }

                                    Uri uri = getContentResolver().insert(PromosiBarangProvider.CONTENT_URI, values);
                                }
                            }


                    }else if(jStatus.equals("failed")){
                        int emptyTablePromo = getContentResolver().delete(PromosiBarangProvider.CONTENT_URI, null, null);
                        Utils.showLogI("Error Failed : " + "Promo barang empty");
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

                String  kode_promosi, kode_item, item_name, barcode, qty;
                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject vendorObj = null;
                try{
                    vendorObj = new JSONObject(rawResponse);
                    String jStatus  = vendorObj.optString("message");
                    JSONArray jResult = vendorObj.optJSONArray("result");

                    if(jStatus.equals("success")){

                            int emptyTablePromo = getContentResolver().delete(PromosiHadiahProvider.CONTENT_URI, null, null);
                            if(jResult != null) {
                                for (int i = 0; i < jResult.length(); i++) {
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
                        Utils.showLogI("Error Failed : Promo Hadiah Empty");
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
                String description, strMongoId;
                String rawResponse = ApiConnection.getRawResponse(response);
                JSONObject vendorObj = null;

                try{
                    vendorObj = new JSONObject(rawResponse);
                    String jStatus  = vendorObj.optString("message");
                    JSONArray jResult = vendorObj.optJSONArray("result");

                    if(jStatus.equals("success")){

                        int emptyTableTax = getContentResolver().delete(TaxGroupProvider.CONTENT_URI, null, null);
                        if(jResult != null) {
                            for (int i = 0; i < jResult.length(); i++) {
                                ContentValues values = new ContentValues();
                                JSONObject jsonObject = jResult.optJSONObject(i);

                                JSONObject idObj = (JSONObject) jsonObject.get("_id");
                                strMongoId = (String) idObj.get("$id");
                                description = jsonObject.optString("description");

                                values.put(TaxGroupProvider.KEY_ID_TAX_GROUP, strMongoId);
                                values.put(TaxGroupProvider.KEY_DESCRIPTION, description);
                                Uri uri = getContentResolver().insert(TaxGroupProvider.CONTENT_URI, values);
                            }
                        }

                    }else{
                        int emptyTableTax = getContentResolver().delete(TaxGroupProvider.CONTENT_URI, null, null);
                        Utils.showLogI("Error Failed : " + "Tax Group Empty");
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

                        try {
                            int emptyTableTax = getContentResolver().delete(TaxProvider.CONTENT_URI, null, null);
                            if(jResult != null) {
                                for (int i = 0; i < jResult.length(); i++) {
                                    ContentValues values = new ContentValues();
                                    JSONObject jsonObject = jResult.optJSONObject(i);

                                    id_tax       = jsonObject.optString("id_tax");
                                    id_tax_group = jsonObject.optString("id_tax_group");
                                    description = jsonObject.optString("description");
                                    rate = jsonObject.optString("rate");

                                    values.put(TaxProvider.KEY_ID_TAX, id_tax);
                                    values.put(TaxProvider.KEY_ID_TAX_GROUP, id_tax_group);
                                    values.put(TaxProvider.KEY_DESCRIPTION, description);
                                    values.put(TaxProvider.KEY_RATE, rate);
                                    Uri uri = getContentResolver().insert(TaxProvider.CONTENT_URI, values);
                                }
                            }


                        }catch (Exception e){
                            Utils.showLogI("Error Exception : " + e.getMessage());
                        }

                        showAlertDialog("Syncron Data Success!");
                        if(progressDialog2 != null) progressDialog2.dismiss();

                    }else if(jStatus.equals("failed")){
                        int emptyTableTax = getContentResolver().delete(TaxProvider.CONTENT_URI, null, null);
                        Utils.showLogI("Error Failed : " + "Tax Empty");
                    }


                }catch(JSONException e){
                    e.printStackTrace();
                    Utils.showLogI("Error JSON : " + e.getMessage());
                    if(progressDialog2 != null) progressDialog2.dismiss();
                }

            }


            @Override
            public void failure(RetrofitError error) {
                Utils.showLogI("Error Retrofit : " + error.getMessage());
                if(progressDialog != null) progressDialog.dismiss();
            }
        });

        Thread.currentThread().interrupt();

    }


    private void showAlertDialog(String message){
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



    private void startDownload(){
        progress.setVisibility(View.VISIBLE);
        progress_text.setVisibility(View.VISIBLE);

        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);
    }


    private void registerReceiver(){
        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MESSAGE_PROGRESS);
        bManager.registerReceiver(broadcastReceiver, intentFilter);
    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent){
            if(intent.getAction().equals(MESSAGE_PROGRESS)){

                Download download = intent.getParcelableExtra("download");
                progress.setProgress(download.getProgress());
                if(download.getProgress() == 100){

                    String zipFile       = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) +"/"+ Constants.userInfoModel.getDbname() + ".zip";
                    String unzipLocation = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) +"/"+ Constants.userInfoModel.getDbname() + "/";


                    Decompress d = new Decompress(zipFile, unzipLocation);
                    d.unzip();

                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), Constants.userInfoModel.getDbname() + ".zip");
                    file.delete();

                    progress_text.setText("File Download Complete");
                    progress.setVisibility(View.GONE);
                    progress_text.setVisibility(View.GONE);

                }else{
                    progress_text.setText(String.format("Downloaded (%d/%d) MB",download.getCurrentFileSize(),download.getTotalFileSize()));
                }

            }
        }
    };



    private void requestPermission(){
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
    }


    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startDownload();
                }else{
                    Utils.showToast("Permission Denied, Please allow to proceed !", this);
                }
                break;
        }
    }




}
