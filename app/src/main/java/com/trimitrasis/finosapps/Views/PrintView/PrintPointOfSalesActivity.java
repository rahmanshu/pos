package com.trimitrasis.finosapps.Views.PrintView;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.ContentProvider.StrukNoteProvider;
import com.trimitrasis.finosapps.Views.PosView.CustomView.ViewItemPrintPos;
import com.trimitrasis.finosapps.Views.PosView.Model.RingkasanOrderModel;
import com.trimitrasis.finosapps.Views.UtilView.Constants;
import com.trimitrasis.finosapps.Views.UtilView.StringUtils;
import com.trimitrasis.finosapps.Views.UtilView.Utils;
import com.zj.btsdk.BluetoothService;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by rahman on 09/06/2017.
 */

@EActivity(R.layout.layout_print_point_of_sales)
public class PrintPointOfSalesActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 2;
    BluetoothService mService = null;
    BluetoothDevice con_dev = null;
    private static final int REQUEST_CONNECT_DEVICE = 1;

    @ViewById
    LinearLayout contentViewItem;

    @ViewById
    Toolbar toolbar;

    @ViewById
    Button btnPrint, btnSearch;

    @Extra
    ArrayList<RingkasanOrderModel> ringkasanOrderModels;

    @Extra
    double cash;

    @Extra
    double kembalian;

    @Extra
    double totalBayar;

    @Extra
    double subTotalBayar;

    @Extra
    double tax;

    @Extra
    double discount;

    @Extra
    double debit;

    @Extra
    double kredit;


    @ViewById
    TextView textNamaKasir, textNamaPt, textTanggal, textTotalSales2, textPajak, textDiskon, textTunai, textKembalian, textTanggalFooter;

    @ViewById
    TextView textDebit, textKredit;

    SimpleDateFormat dateFormat;
    Date date;

    ViewItemPrintPos viewItemPrintPos;
    ArrayList<ViewItemPrintPos> viewItemPrintPosArrayList;
    int incremen_item_pos = 0;

    @AfterViews
    void afterView(){
        viewItemPrintPosArrayList = new ArrayList<>();
        mService = new BluetoothService(this, mHandler);

        if(mService.isAvailable() == false ){
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
        }

        initTabbar();
        customView();
        ConnectPrinterStrukActivity_.intent(this).startForResult(REQUEST_CONNECT_DEVICE);
        getDataItemPrint();
    }


    private void initTabbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_arrow_left);
    }


    private void getDataItemPrint(){

        dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        date = new Date();

        textNamaPt.setText(Constants.userInfoModel.getComname());
        textNamaKasir.setText(Constants.userInfoModel.getEmail());
        textTanggal.setText(dateFormat.format(date));
        textTanggalFooter.setText(dateFormat.format(date));

        if(ringkasanOrderModels != null){
            for (int i = 0; i < ringkasanOrderModels.size(); i++) {
                int qtyInteger = (int) ringkasanOrderModels.get(i).getQty();
                double subTott = ringkasanOrderModels.get(i).getQty() * ringkasanOrderModels.get(i).getHargaJual();
                String subTottal = Utils.getCurrencyRupiahTanpaSimbol(subTott);
                setViewBranchItem(ringkasanOrderModels.get(i).getNamaBarang(), qtyInteger, Utils.getCurrencyRupiahTanpaSimbol(ringkasanOrderModels.get(i).getHargaJual()), subTottal);
            }
        }

        textTotalSales2.setText("" + Utils.getCurrencyRupiah(totalBayar));
        textDiskon.setText("Diskon : " + Utils.getCurrencyRupiah(discount));
        textPajak.setText("PPn : " + Utils.getCurrencyRupiah(tax));
        textDebit.setText("" + Utils.getCurrencyRupiah(debit));
        textKredit.setText("" + Utils.getCurrencyRupiah(kredit));
        textTunai.setText("" + Utils.getCurrencyRupiah(cash));
        textKembalian.setText("" + Utils.getCurrencyRupiah(kembalian));

    }


    private void setViewBranchItem(String nama, int qty, String hargaJual, String subTotal){
        viewItemPrintPos = new ViewItemPrintPos(this);
        viewItemPrintPos.setTextNamaBarang(nama);
        viewItemPrintPos.setTextQty(String.valueOf(qty));
        viewItemPrintPos.setTextHargaJual(String.valueOf(hargaJual));
        viewItemPrintPos.setTextSubTotal(String.valueOf(subTotal));
        viewItemPrintPosArrayList.add(incremen_item_pos, viewItemPrintPos);
        contentViewItem.addView(viewItemPrintPosArrayList.get(incremen_item_pos));
        incremen_item_pos++;
    }


    @OptionsItem(android.R.id.home)
    void homeClick(){
        onBackPressed();
    }


    private void customView(){
        TextView toolbarView = (TextView) toolbar.findViewById(R.id.headerText);
        toolbarView.setText("Preview Point Of Sales");
    }

    @Override
    public void onStart(){
        super.onStart();
        if( mService.isBTopen() == false)
        {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (mService != null)
            mService.stop();
        mService = null;
    }


    private final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            Toast.makeText(getApplicationContext(), "Connect successful", Toast.LENGTH_SHORT).show();
                            printStruk();
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            Log.d("��������", "��������.....");
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            Log.d("��������", "�ȴ�����.....");
                            break;
                    }
                    break;
                case BluetoothService.MESSAGE_CONNECTION_LOST:
                    Toast.makeText(getApplicationContext(), "Device connection was lost", Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT:
                    Toast.makeText(getApplicationContext(), "Unable to connect device", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case REQUEST_ENABLE_BT:
                if(resultCode == Activity.RESULT_OK){
                    Toast.makeText(this, "Bluetooth open successful", Toast.LENGTH_LONG).show();
                }else{
                    finish();
                }
                break;

            case  REQUEST_CONNECT_DEVICE:
                if(resultCode == Activity.RESULT_OK) {

                    if(Constants.printer_address_struk != "") {
                        String address = data.getExtras().getString(DeviceListStrukActivity.EXTRA_DEVICE_ADDRESS);
                        con_dev = mService.getDevByMac(address);
                        mService.connect(con_dev);
                    }
                }

                break;
        }
    }



    private void printStruk(){

        String message = "";
        String message2 = "";
        String titleStr	 = String.valueOf(Constants.userInfoModel.getLocationAddress());
        String titleFooter	= "Terima Kasih dan Selamat Belanja Kembali" + "\n";
        StringUtils utils_string   = new StringUtils(32, StringUtils.Alignment.CENTER);

        StringBuilder contentSb	 = new StringBuilder();
        StringBuilder contentSb2 = new StringBuilder();


        long milis		  = System.currentTimeMillis();
        String date_now	  = timeMilisToString(milis, "yyyy-MM-dd");

        contentSb.append("================================"                         + "\n");
        contentSb.append("DATE         : "+ date_now                                + "\n");
        contentSb.append("KASIR        : "+ Constants.userInfoModel.getEmail()      + "\n");
        contentSb.append("STORE NAME   : "+ Constants.userInfoModel.getComname()    + "\n");
        contentSb.append("================================"                         + "\n");

        double totQty = 0; double totTunai = 0;

        if(ringkasanOrderModels != null){
            for(int i = 0; i < ringkasanOrderModels.size(); i++){

                double subtotal    = ringkasanOrderModels.get(i).getHargaJual() * ringkasanOrderModels.get(i).getQty();
                String nmBarang    = String.valueOf(ringkasanOrderModels.get(i).getNamaBarang());
                double qty         = ringkasanOrderModels.get(i).getQty();
                int qtyInt         = (int) qty;
                String qtyString   = String.valueOf(qtyInt);

                if(nmBarang.length() > 5){
                    nmBarang = nmBarang.substring(0, 5);
                }

                contentSb.append( fill(nmBarang, 5, " ") + " " +
                                  pad(qtyString, 3, " ") + " " +
                                  pad(Utils.getCurrencyRupiahTanpaSimbol(ringkasanOrderModels.get(i).getHargaJual()), 8, " ")  + "  " +
                                  pad(Utils.getCurrencyRupiahTanpaSimbol(subtotal), 11, " ") + "\n");

                totQty = totQty + ringkasanOrderModels.get(i).getQty();
                totTunai = totTunai + subtotal;
            }
        }

        int totItemInt   = (int) totQty;
        contentSb2.append("================================"                        + "\n");

        contentSb2.append("TOTAL ITEM   : " + pad(String.valueOf(totItemInt), 17, " ")                               + "\n");
        contentSb2.append("Tunai        : " + "Rp." + pad(Utils.getCurrencyRupiahTanpaSimbol(cash), 14, " ")         + "\n");

        if(debit != 0){
            contentSb2.append("Debit        : " + "Rp." + pad(Utils.getCurrencyRupiahTanpaSimbol(debit), 14, " ")    + "\n");
        }

        if(kredit != 0){
            contentSb2.append("Kredit       : " + "Rp." + pad(Utils.getCurrencyRupiahTanpaSimbol(kredit), 14, " ")   + "\n");
        }

        if(kembalian != 0){
            contentSb2.append("Kembalian    : " + "Rp." + pad(Utils.getCurrencyRupiahTanpaSimbol(kembalian), 14, " ")    + "\n");
        }

        if(tax != 0){
            contentSb2.append("PPN          : " + "Rp." + pad(Utils.getCurrencyRupiahTanpaSimbol(tax), 14, " ")      + "\n");
        }

        if(discount != 0){
            contentSb2.append("Discount     : " + "Rp." + pad(Utils.getCurrencyRupiahTanpaSimbol(discount), 14, " ") + "\n");
        }

        contentSb2.append("================================" + "\n");
        message = "*" + getFootnote() + "*" + "\n";
        mService.sendMessage(utils_string.format(titleStr)  + contentSb.toString() + contentSb2.toString()  + utils_string.format(titleFooter) + utils_string.format(message) + message2, "GBK");
        finish(); //new
    }


    public static String timeMilisToString(long milis, String format){
        SimpleDateFormat sd = new SimpleDateFormat(format);
        Calendar calendar   = Calendar.getInstance();
        calendar.setTimeInMillis(milis);
        return sd.format(calendar.getTime());
    }


    public String pad(String value, int length, String with) {
        StringBuilder result = new StringBuilder(length);
        result.append(value);

        while (result.length() < length) {
            result.insert(0, with);
        }

        return result.toString();
    }

    public String fill(String value, int length, String with){
        StringBuilder result = new StringBuilder(length);
        result.append(value);
        result.append(fill_(Math.max(0, length - value.length()), with));
        return result.toString();
    }


    public String fill_(int length, String with) {
        StringBuilder sb = new StringBuilder(length);
        while (sb.length() < length) {
            sb.append(with);
        }
        return sb.toString();
    }


    private String getFootnote(){

        String footnote = "";
        String URL = "content://com.trimitrasis.finosapps.Views.ContentProvider.StrukNoteProvider/struknote";
        Uri uri = Uri.parse(URL);
        Cursor c_login = getContentResolver().query(uri, null, null, null, "_id");

        if(c_login.moveToFirst()){
            do{
                footnote = (c_login.getString(c_login.getColumnIndex(StrukNoteProvider.KEY_STRUK_NOTE)) != null) ? c_login.getString(c_login.getColumnIndex(StrukNoteProvider.KEY_STRUK_NOTE)) : "";
            } while (c_login.moveToNext());
            c_login.close();
        }

        return footnote;
    }

}
