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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.ContentProvider.BomProvider;
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
 * Created by rahman on 25/07/2017.
 */

@EActivity(R.layout.layout_print_dapur)
public class PrintDapurActivity extends AppCompatActivity {

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
    String nomorMeja;

    @Extra
    double total_bayar_;

    @ViewById
    TextView textNamaKasir, textNamaPt, textTanggal, textNomorMeja;

    SimpleDateFormat dateFormat;
    Date date;

    ViewItemPrintPos viewItemPrintPos;
    ArrayList<ViewItemPrintPos> viewItemPrintPosArrayList;
    int incremen_item_pos = 0;


    @AfterViews
    void afterView(){
        viewItemPrintPosArrayList = new ArrayList<>();
        mService = new BluetoothService(this, mHandler);

        if(mService.isAvailable() == false){
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
        }

        initTabbar();
        customView();
        ConnectPrinterDapurActivity_.intent(this).startForResult(REQUEST_CONNECT_DEVICE);
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
        textNomorMeja.setText(""+nomorMeja);

        if(ringkasanOrderModels != null){
            for (int i = 0; i < ringkasanOrderModels.size(); i++) {
                int qtyInteger = (int) ringkasanOrderModels.get(i).getQty();
                double subTott = ringkasanOrderModels.get(i).getQty() * ringkasanOrderModels.get(i).getHargaJual();
                String subTottal = Utils.getCurrencyRupiahTanpaSimbol(subTott);
                setViewBranchItem(ringkasanOrderModels.get(i).getNamaBarang(), qtyInteger, Utils.getCurrencyRupiahTanpaSimbol(ringkasanOrderModels.get(i).getHargaJual()), subTottal);
            }
        }

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
                if(resultCode == Activity.RESULT_OK){

                    if(Constants.printer_address_struk != ""){
                        String address = data.getExtras().getString(DeviceListDapurActivity_.EXTRA_DEVICE_ADDRESS);
                        con_dev = mService.getDevByMac(address);
                        System.out.println("mac address printer : " + address);
                        mService.connect(con_dev);
                    }
                }

                break;
        }
    }



    private void printStruk(){

        String message     = "";
        String message2    = "";
        String titleStr	   = String.valueOf(Constants.userInfoModel.getLocationAddress());
        String titleFooter = "";
        StringUtils utils_string  = new StringUtils(32, StringUtils.Alignment.CENTER);
        StringBuilder contentSb	  = new StringBuilder();
        StringBuilder contentSb2  = new StringBuilder();

        long milis		  = System.currentTimeMillis();
        String date_now	  = timeMilisToString(milis, "yyyy-MM-dd");

        contentSb.append("================================"                         + "\n");
        contentSb.append("DATE         : "+ date_now                                + "\n");
        contentSb.append("KASIR        : "+ Constants.userInfoModel.getEmail()      + "\n");
        contentSb.append("STORE NAME   : "+ Constants.userInfoModel.getComname()    + "\n");
        contentSb.append("NOMOR MEJA   : "+ nomorMeja                               + "\n");
        contentSb.append("================================"                         + "\n");

        double totQty  = 0;
        if(ringkasanOrderModels != null){
            for(int i = 0; i < ringkasanOrderModels.size(); i++){

                String nmBarang    = String.valueOf(ringkasanOrderModels.get(i).getNamaBarang());
                double qty         = ringkasanOrderModels.get(i).getQty();
                int qtyInt         = (int) qty;
                String qtyString   = String.valueOf(qtyInt);
                contentSb.append(fill(nmBarang, 20, " ") + " " +
                                 pad("X", 3, " ") + " " +
                                 pad(qtyString, 3, " ") +  "\n");


                if(!ringkasanOrderModels.get(i).getNote().equals("")){
                    contentSb.append("NOTE :" + fill(String.valueOf(ringkasanOrderModels.get(i).getNote()), 23, " ") + "\n");
                }

                if(ringkasanOrderModels.get(i).isFlag_bom() == true){

                    String itemId  = ringkasanOrderModels.get(i).getItemId();
                    String URL = "content://com.trimitrasis.finosapps.ContentProvider.BomProvider/bom";
                    Uri uri_   = Uri.parse(URL);
                    Cursor c   = getContentResolver().query(uri_, null, "item_id" + " = '" +  itemId + "'", null, "_id ASC");
                    if(c.moveToFirst()){
                        do{

                            String namaBahan  = (!c.getString(c.getColumnIndex(BomProvider.KEY_ITEM_BAHAN_NAME)).equals("") ? c.getString(c.getColumnIndex(BomProvider.KEY_ITEM_BAHAN_NAME)) : "");
                            String qtyBahan   = (!c.getString(c.getColumnIndex(BomProvider.KEY_QTY)).equals("") ? c.getString(c.getColumnIndex(BomProvider.KEY_QTY)) : "");
                            String uomName    = (!c.getString(c.getColumnIndex(BomProvider.KEY_UOM_NAME)).equals("") ? c.getString(c.getColumnIndex(BomProvider.KEY_UOM_NAME)) : "");
                            contentSb.append( "***" + fill(namaBahan, 18, " ") + " " +
                                                      pad(qtyBahan, 3, " ")    + " " +
                                                      uomName +  "\n");

                        }while (c.moveToNext());
                        c.close();
                    }
                }

                totQty = totQty + ringkasanOrderModels.get(i).getQty();

            }
        }

        contentSb2.append("================================"                        + "\n");
        mService.sendMessage(utils_string.format(titleStr)  + contentSb.toString() + contentSb2.toString()  + utils_string.format(titleFooter) + message + message2, "GBK");
        finish();

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

}
