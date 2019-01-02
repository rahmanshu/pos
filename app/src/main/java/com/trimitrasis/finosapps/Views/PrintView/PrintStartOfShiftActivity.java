package com.trimitrasis.finosapps.Views.PrintView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.UtilView.Constants;
import com.trimitrasis.finosapps.Views.UtilView.StringUtils;
import com.trimitrasis.finosapps.Views.UtilView.Utils;
import com.zj.btsdk.BluetoothService;
import com.zj.btsdk.PrintPic;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by rahman on 11/04/2017.
 */

@EActivity(R.layout.layout_print_start_of_shift_)
public class PrintStartOfShiftActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 2;
    BluetoothService mService = null;
    BluetoothDevice con_dev = null;
    private static final int REQUEST_CONNECT_DEVICE = 1;

    @Extra
    double setoranAwal_;

    @ViewById
    TextView textSetoranAwal, textTotalActual;

    @ViewById
    Toolbar toolbar;

    @ViewById
    Button btnPrint;

    @ViewById
    Button btnSearch;

    @ViewById
    TextView textNamaPt, textNamaKasir, textTanggal, textTanggalFooter;

    SimpleDateFormat dateFormat;
    Date date;

    @AfterViews
    void afterView(){
        mService = new BluetoothService(this, mHandler);

        if(mService.isAvailable() == false){
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
        }

        initTabbar();
        customView();
        ConnectPrinterStrukActivity_.intent(this).startForResult(REQUEST_CONNECT_DEVICE);
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
        toolbarView.setText("Preview Start Of Shift");
        textSetoranAwal.setText(Utils.getCurrencyRupiah(setoranAwal_));
        textTotalActual.setText(Utils.getCurrencyRupiah(setoranAwal_));

        dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        date = new Date();
        textNamaPt.setText(Constants.userInfoModel.getComname());
        textNamaKasir.setText(Constants.userInfoModel.getEmail());
        textTanggal.setText(dateFormat.format(date));
        textTanggalFooter.setText(dateFormat.format(date));
    }


    @Override
    public void onStart(){
        super.onStart();
        if(mService.isBTopen() == false)
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

    @Click
    void btnPrint(){
        printStruk();
    }


    @Click
    void btnSearch(){
        ConnectPrinterStrukActivity_.intent(PrintStartOfShiftActivity.this).startForResult(REQUEST_CONNECT_DEVICE);
    }

    private final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch(msg.what){
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch(msg.arg1){
                        case BluetoothService.STATE_CONNECTED:
                            Toast.makeText(getApplicationContext(), "Connect successful", Toast.LENGTH_SHORT).show();
                            btnPrint.setEnabled(true);
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
                    btnPrint.setEnabled(false);
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
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(this, "Bluetooth open successful", Toast.LENGTH_LONG).show();
                } else {
                    finish();
                }
                break;

            case  REQUEST_CONNECT_DEVICE:
                if (resultCode == Activity.RESULT_OK) {

                    if(Constants.printer_address_struk != ""){
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

        String titleStr	= "START OF SHIFT";
        StringUtils utils_string = new StringUtils(32, StringUtils.Alignment.CENTER);
        String title2 = "";
        String message = "";
        String message2 = "";

        StringBuilder contentSb	= new StringBuilder();
        StringBuilder content2Sb = new StringBuilder();

        long milis		  = System.currentTimeMillis();
        String date_now	  = timeMilisToString(milis, "dd-MM-yyyy HH:mm");

        contentSb.append("================================"     + "\n");
        contentSb.append("KASIR          : "+ Constants.userInfoModel.getEmail()      + "\n");
        contentSb.append("STORE NAME     : "+ Constants.userInfoModel.getComname()    + "\n");
        contentSb.append("DATE           : "+ date_now                                + "\n");
        contentSb.append("--------------------------------"     + "\n");
        contentSb.append("SETORAN AWAL   : "+ Utils.getCurrencyRupiah(setoranAwal_)   + "\n");
        contentSb.append("--------------------------------"     + "\n");
        contentSb.append("TOTAL ACTUAL   : "+ Utils.getCurrencyRupiah(setoranAwal_)   + "\n");
        contentSb.append("--------------------------------"                           + "\n");
        contentSb.append("Mengetahui Store Leader"                                    + "\n");
        contentSb.append(""                                     + "\n");
        contentSb.append(""                                     + "\n");
        contentSb.append(""                                     + "\n");
        mService.sendMessage(utils_string.format(titleStr) + utils_string.format(title2) + contentSb.toString() + message + content2Sb.toString() + message2 + date, "GBK");
        finish(); //new
    }


    public static String timeMilisToString(long milis, String format) {
        SimpleDateFormat sd = new SimpleDateFormat(format);
        Calendar calendar   = Calendar.getInstance();
        calendar.setTimeInMillis(milis);
        return sd.format(calendar.getTime());
    }



}
