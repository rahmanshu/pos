package com.trimitrasis.finosapps.Views.PrintView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.ContentProvider.EndOfShiftProvider;
import com.trimitrasis.finosapps.Views.PosView.Model.EndOfShiftModel;
import com.trimitrasis.finosapps.Views.UtilView.Constants;
import com.trimitrasis.finosapps.Views.UtilView.StringUtils;
import com.trimitrasis.finosapps.Views.UtilView.Utils;
import com.zj.btsdk.BluetoothService;
import com.zj.btsdk.PrintPic;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by rahman on 11/04/2017.
 */


@EActivity(R.layout.layout_print_start_of_day)
public class PrintEndOfDayActivity extends AppCompatActivity{


    @ViewById
    TextView textCashIncomeSales, textNetSales2, textNetReturnSales, textPpnSales, textTotalSales2, textTotalActual, textReceiptCount;

    @ViewById
    TextView textCashActual, textCashIncomeActual, textCreditCardActual, textDebitActual, textDiscountActual, textVariance, textNamaPt;

    @ViewById
    TextView textNamaKasir, textTanggal, textTanggalFooter;


    @ViewById
    Button btnPrint;

    @ViewById
    Toolbar toolbar;

    private static final int REQUEST_ENABLE_BT = 2;
    BluetoothService mService = null;
    BluetoothDevice con_dev = null;
    private static final int REQUEST_CONNECT_DEVICE = 1;

    double totalAktual = 0, variance = 0;
    double totNetSales = 0, totCash = 0, totCredit = 0, totDebit = 0, totDiscount = 0, totTrans = 0, totPPn = 0, totNetReturn = 0;

    SimpleDateFormat dateFormat;
    Date date;


    @AfterViews
    void afterView(){
        mService = new BluetoothService(this, mHandler);
        if(mService.isAvailable() == false ){
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
        }

        getDataEndOfShift();
        ConnectPrinterStrukActivity_.intent(this).startForResult(REQUEST_CONNECT_DEVICE);
    }



    @Click
    void btnPrint(){
        printStruk();
    }



    @Override
    public void onStart(){

        super.onStart();
        if( mService.isBTopen() == false)
        {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

        //DeviceListActivity_.intent(this).startForResult(REQUEST_CONNECT_DEVICE);
        initTabbar();
        customView();
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


    private void customView(){
        TextView toolbarView = (TextView) toolbar.findViewById(R.id.headerText);
        toolbarView.setText("Preview End Of Day");
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
            switch(msg.what){
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch(msg.arg1){
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
                    //btnClose.setEnabled(false);
                    //btnSend.setEnabled(false);
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
                if(resultCode == Activity.RESULT_OK){
                    if(Constants.printer_address_struk != "") {
                        String address = data.getExtras().getString(DeviceListStrukActivity.EXTRA_DEVICE_ADDRESS);
                        con_dev = mService.getDevByMac(address);
                        mService.connect(con_dev);
                    }
                }

                break;
        }
    }


    @SuppressLint("SdCardPath")
    private void printImage() {
        byte[] sendData = null;
        PrintPic pg = new PrintPic();
        pg.initCanvas(384);
        pg.initPaint();
        pg.drawImage(0, 0, "/mnt/sdcard/icon.jpg");
        sendData = pg.printDraw();
        mService.write(sendData);
    }


    private void printStruk(){

        String title2 = "";
        String message = "";
        String message2 = "";
        String titleStr	= "END OF DAY";
        StringUtils utils_string = new StringUtils(32, StringUtils.Alignment.CENTER);
        StringBuilder contentSb	= new StringBuilder();

        long milis		  = System.currentTimeMillis();
        String date_now	  = timeMilisToString(milis, "yyyy-MM-dd");

        contentSb.append("================================"                           + "\n");
        contentSb.append("KASIR          : "+ Constants.userInfoModel.getEmail()      + "\n");
        contentSb.append("STORE NAME     : "+ Constants.userInfoModel.getComname()    + "\n");
        contentSb.append("DATE           : "+ date_now                                + "\n");
        contentSb.append("--------------------------------"                           + "\n");
        contentSb.append("SALES"                                                      + "\n");
        contentSb.append("CASH INCOME    : "+ Utils.getCurrencyRupiah(Constants.endOfShiftModel.getCashIncome()) + "\n");
        contentSb.append("NET SALES      : "+ Utils.getCurrencyRupiah(totNetSales)    + "\n");
        contentSb.append("NET RETURN     : "+ Utils.getCurrencyRupiah(totNetReturn)      + "\n");
        contentSb.append("PPN            : "+ Utils.getCurrencyRupiah(totPPn)         + "\n");
        contentSb.append("TOTAL SALES    : "+ Utils.getCurrencyRupiah(totNetSales)    + "\n");
        contentSb.append("--------------------------------"                           + "\n");
        contentSb.append("ACTUAL"                                                     + "\n");
        contentSb.append("CASH           : "+ Utils.getCurrencyRupiah(totCash)        + "\n");
        contentSb.append("CASH INCOME    : "+ Utils.getCurrencyRupiah(Constants.endOfShiftModel.getCashIncome()) + "\n");
        contentSb.append("CREDIT CARD    : "+ Utils.getCurrencyRupiah(totCredit)      + "\n");
        contentSb.append("DEBIT          : "+ Utils.getCurrencyRupiah(totDebit)       + "\n");
        contentSb.append("DISCOUNT       : "+ Utils.getCurrencyRupiah(totDiscount)    + "\n");
        contentSb.append("--------------------------------"                           + "\n");
        contentSb.append("TOTAL ACTUAL   : "+ Utils.getCurrencyRupiah(totalAktual)    + "\n");
        contentSb.append("VARIANCE       : "+ Utils.getCurrencyRupiah(variance)       + "\n");
        contentSb.append("RECEIPT COUNT  : "+ String.valueOf(totTrans)                + "\n");
        contentSb.append("--------------------------------"     + "\n");
        contentSb.append("Mengetahui Store Leader"              + "\n");
        contentSb.append("(                      )"             + "\n");
        contentSb.append(""                                     + "\n");

        mService.sendMessage(utils_string.format(titleStr) + utils_string.format(title2) + contentSb.toString()  + date + message + message2, "GBK");
        Constants.endOfShiftModel = new EndOfShiftModel();
        finish(); //new
    }


    public static String timeMilisToString(long milis, String format) {
        SimpleDateFormat sd = new SimpleDateFormat(format);
        Calendar calendar   = Calendar.getInstance();
        calendar.setTimeInMillis(milis);
        return sd.format(calendar.getTime());
    }


    private void getDataEndOfShift(){

        int status_eod = 0; String username = "";
        String URL   = "content://com.trimitrasis.finosapps.ContentProvider.EndOfShiftProvider/endofshift";
        Uri uri_     = Uri.parse(URL);
        Cursor c_eos = getContentResolver().query(uri_, null, "status_eod " + " = '" +  0 + "'" , null, "_id");

        if(c_eos.moveToFirst()){

            do{
                status_eod = Integer.parseInt(c_eos.getString(c_eos.getColumnIndex(EndOfShiftProvider.KEY_STATUS_EOD)));
                username   = c_eos.getString(c_eos.getColumnIndex(EndOfShiftProvider.KEY_USERNAME));

                if(status_eod == 0 && username.equals(Constants.userInfoModel.getEmail())){
                    totNetSales = totNetSales + Double.parseDouble(c_eos.getString(c_eos.getColumnIndex(EndOfShiftProvider.KEY_NET_SALES)));
                    totCash     = totCash     + Double.parseDouble(c_eos.getString(c_eos.getColumnIndex(EndOfShiftProvider.KEY_CASH)));
                    totCredit   = totCredit   + Double.parseDouble(c_eos.getString(c_eos.getColumnIndex(EndOfShiftProvider.KEY_CREDIT)));
                    totDebit    = totDebit    + Double.parseDouble(c_eos.getString(c_eos.getColumnIndex(EndOfShiftProvider.KEY_DEBIT)));
                    totDiscount = totDiscount + Double.parseDouble(c_eos.getString(c_eos.getColumnIndex(EndOfShiftProvider.KEY_DISCOUNT)));
                    totTrans    = totTrans    + Double.parseDouble(c_eos.getString(c_eos.getColumnIndex(EndOfShiftProvider.KEY_JUMLAH_TRANS)));
                    totPPn      = totPPn      + Double.parseDouble(c_eos.getString(c_eos.getColumnIndex(EndOfShiftProvider.KEY_PPN)));
                }

            }while (c_eos.moveToNext());

            totalAktual = totCash + totCredit + totDebit;
            variance    = totCash - totNetSales;
            c_eos.close();
        }

        setDataPreviewPrint();
    }



    private void setDataPreviewPrint(){
        dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        date = new Date();

        textNamaPt.setText(Constants.userInfoModel.getComname());
        textNamaKasir.setText(Constants.userInfoModel.getEmail());
        textTanggal.setText(dateFormat.format(date));
        textTanggalFooter.setText(dateFormat.format(date));
        textCashIncomeSales.setText(Utils.getCurrencyRupiah(Constants.endOfShiftModel.getCashIncome()));
        textCashIncomeActual.setText(Utils.getCurrencyRupiah(Constants.endOfShiftModel.getCashIncome()));
        textNetSales2.setText(Utils.getCurrencyRupiah(totNetSales));
        textCashActual.setText(Utils.getCurrencyRupiah(totCash));
        textCreditCardActual.setText(Utils.getCurrencyRupiah(totCredit));
        textDebitActual.setText(Utils.getCurrencyRupiah(totDebit));
        textDiscountActual.setText(Utils.getCurrencyRupiah(totDiscount));
        textTotalSales2.setText(Utils.getCurrencyRupiah(totNetSales));
        textTotalActual.setText(Utils.getCurrencyRupiah(totalAktual));
        textVariance.setText(Utils.getCurrencyRupiah(variance));
        textReceiptCount.setText(String.valueOf(totTrans));
        textPpnSales.setText(Utils.getCurrencyRupiah(totPPn));
        updateDataEndOfDay();
    }


    private void updateDataEndOfDay(){

        int status_eos  = 0; int id_eos = 0; String username = "";
        String URL   = "content://com.trimitrasis.finosapps.ContentProvider.EndOfShiftProvider/endofshift";
        Uri uri_     = Uri.parse(URL);
        Cursor c_eos = getContentResolver().query(uri_, null, "status_eod " + " = '" +  0 + "'" , null, "_id");

        if(c_eos.moveToFirst()){
            do{

                status_eos = Integer.parseInt(c_eos.getString(c_eos.getColumnIndex(EndOfShiftProvider.KEY_STATUS_EOD)));
                id_eos     = Integer.parseInt(c_eos.getString(c_eos.getColumnIndex(EndOfShiftProvider.KEY_ID)));
                username   = c_eos.getString(c_eos.getColumnIndex(EndOfShiftProvider.KEY_USERNAME));

                if(status_eos == 0 && username.equals(Constants.userInfoModel.getEmail())){
                    ContentValues values  = new ContentValues();
                    values.put(EndOfShiftProvider.KEY_STATUS_EOD, 1);
                    int uri = getContentResolver().update(EndOfShiftProvider.CONTENT_URI, values, "_id " + " = '" +  id_eos + "'", null);
                }

            }while (c_eos.moveToNext());
            c_eos.close();

        }
    }

}
