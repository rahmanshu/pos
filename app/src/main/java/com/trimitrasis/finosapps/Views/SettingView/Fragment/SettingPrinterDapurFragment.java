package com.trimitrasis.finosapps.Views.SettingView.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.trimitrasis.finosapps.ActivityResultEventDapur;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.PrintView.DeviceListDapurActivity_;
import com.trimitrasis.finosapps.Views.UtilView.Constants;
import com.trimitrasis.finosapps.Views.UtilView.FontUtils;
import com.zj.btsdk.BluetoothService;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import de.greenrobot.event.EventBus;

/**
 * Created by rahman on 20/07/2017.
 */

@EFragment(R.layout.fragment_setting_struk_dapur)
public class SettingPrinterDapurFragment extends Fragment {

    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    BluetoothService mService = null;
    BluetoothDevice con_dev = null;

    Dialog dialogPilihPrinter;
    LayoutInflater inflater;

    TextView textPrinterBuletooth;

    @ViewById
    Button btnHubungkanPrint;

    @ViewById
    TextView textPrinterAddress, textNamaPrinter, statusPrinterDapur;


    @AfterViews
    void afterView(){


        //EventBus.getDefault().register(this);

        statusPrinterDapur.setText("Printer Dapur  -  Tidak Terhubung..");

        //old
        /*
        mService = new BluetoothService(getActivity(), mHandler);
        if(mService.isAvailable() == false ){
            Toast.makeText(getActivity(), "Bluetooth is not available", Toast.LENGTH_LONG).show();
        }

        inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(!Constants.printer_address_dapur.equals("")){
            textPrinterAddress.setText(Constants.printer_address_dapur);
            textNamaPrinter.setText(Constants.printer_name_dapur);
            statusPrinterDapur.setText("Printer Dapur  -  Terhubung");
        }
        */

        customView();

    }


    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    private void customView(){
        btnHubungkanPrint.setTypeface(FontUtils.getHelvetica_Neue_LT(getActivity()));
        textPrinterAddress.setTypeface(FontUtils.getHelvetica_Neue_LT(getActivity()));
        textNamaPrinter.setTypeface(FontUtils.getHelvetica_Neue_LT(getActivity()));
        statusPrinterDapur.setTypeface(FontUtils.getHelvetica_Neue_LT(getActivity()));
    }


    private void showDialogPilihPrinter(){
        dialogPilihPrinter = new Dialog(getActivity());
        View view = inflater.inflate(R.layout.popup_pilih_jenis_printer, null);
        declareLayout(view);
        dialogPilihPrinter.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogPilihPrinter.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#50000000")));
        dialogPilihPrinter.setContentView(view);
        dialogPilihPrinter.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogPilihPrinter.show();
    }


    private void declareLayout(View view){
        textPrinterBuletooth = (TextView) view.findViewById(R.id.textPrinterBuletooth);
        textPrinterBuletooth.setOnClickListener(onClickListener);
        textPrinterBuletooth.setTypeface(FontUtils.getHelvetica_Neue_LT(getActivity()));
    }


    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.textPrinterBuletooth:{

                    Intent intent = new Intent(getActivity(), DeviceListDapurActivity_.class);
                    getActivity().startActivityForResult(intent, REQUEST_CONNECT_DEVICE);
                    dialogPilihPrinter.dismiss();

                }break;

            }
        }
    };



    @Override
    public void onStart(){
        super.onStart();
        EventBus.getDefault().register(this);

        mService = new BluetoothService(getActivity(), mHandler);
        if(mService.isAvailable() == false ){
            Toast.makeText(getActivity(), "Bluetooth is not available", Toast.LENGTH_LONG).show();
        }

        inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(!Constants.printer_address_dapur.equals("")){
            textPrinterAddress.setText(Constants.printer_address_dapur);
            textNamaPrinter.setText(Constants.printer_name_dapur);
            statusPrinterDapur.setText("Printer Dapur  -  Terhubung");
        }

        if( mService.isBTopen() == false)
        {
            System.out.println("ane udah running...");
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            getActivity().startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
    }



    @Override
    public void onDestroy(){
        super.onDestroy();
        if (mService != null)
            mService.stop();
        System.out.println("mService struk stop..!!");
        mService = null;
    }




    @Click
    void btnHubungkanPrint(){
        showDialogPilihPrinter();
    }


    private final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1){
                        case BluetoothService.STATE_CONNECTED:
                            Toast.makeText(getActivity(), "Connect successful", Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            break;
                    }
                    break;
                case BluetoothService.MESSAGE_CONNECTION_LOST:
                    Log.d("info : ", "Device connection was lost");
                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT:
                    Log.d("info : ", "Unable to connect device");
                    break;
            }
        }
    };




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("haint", "onActivityResult in fragment Level two: request code = " + requestCode);
    }


    public void onEvent(ActivityResultEventDapur event){

        switch (event.getRequestCode()){
            case REQUEST_ENABLE_BT:
                if(event.getResultCode() == Activity.RESULT_OK){
                    Toast.makeText(getActivity(), "Bluetooth open successful", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getActivity(), "Bluetooth open failed", Toast.LENGTH_LONG).show();
                }
                break;

            case  REQUEST_CONNECT_DEVICE:
                if(event.getResultCode() == Activity.RESULT_OK){
                    String address = event.getData().getExtras().getString(DeviceListDapurActivity_.EXTRA_DEVICE_ADDRESS);
                    con_dev = mService.getDevByMac(address);
                    mService.connect(con_dev);

                    if(!Constants.printer_address_dapur.equals("")){
                        textPrinterAddress.setText(Constants.printer_address_dapur);
                        textNamaPrinter.setText(Constants.printer_name_dapur);
                        statusPrinterDapur.setText("Printer Dapur sudah -  Terhubung");
                    }

                }

                break;
        }

    }




}
