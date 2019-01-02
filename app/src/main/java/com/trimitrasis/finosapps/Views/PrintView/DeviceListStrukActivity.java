package com.trimitrasis.finosapps.Views.PrintView;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.UtilView.Constants;
import com.zj.btsdk.BluetoothService;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;
import java.util.Set;

/**
 * Created by rahman on 21/07/2017.
 */

@EActivity(R.layout.layout_device_list_printer)
public class DeviceListStrukActivity extends AppCompatActivity {



    @ViewById
    Toolbar toolbar;

    @ViewById
    Button button_scan;

    @ViewById
    ListView paired_devices, new_devices;

    @ViewById
    TextView title_paired_devices, title_new_devices;

    // Return Intent extra
    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    BluetoothService mService = null;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private ArrayAdapter<String> mNewDevicesArrayAdapter;

    @AfterViews
    void afterView(){

        initTabbar();
        customView();

        setResult(Activity.RESULT_CANCELED);
        mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);

        paired_devices.setAdapter(mPairedDevicesArrayAdapter);
        paired_devices.setOnItemClickListener(mDeviceClickListener);

        new_devices.setAdapter(mNewDevicesArrayAdapter);
        new_devices.setOnItemClickListener(mDeviceClickListener);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        mService = new BluetoothService(this, null);

        Set<BluetoothDevice> pairedDevices = mService.getPairedDev();

        if(pairedDevices.size() > 0){
            title_paired_devices.setVisibility(View.VISIBLE);
            for(BluetoothDevice device : pairedDevices){
                mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                System.out.println("device printer name : " + device.getName() + " device address : " + device.getAddress());
            }
        }else{
            String noDevices = getResources().getText(R.string.none_paired).toString();
            mPairedDevicesArrayAdapter.add(noDevices);
        }


        //getConnectPrinter();
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
        toolbarView.setText("List Printer");
    }


    @Click
    void button_scan(){
        doDiscovery();
        button_scan.setVisibility(View.GONE);
    }



    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (mService != null) {
            System.out.println("device list matiii");
            mService.cancelDiscovery();
        }
        mService = null;
        this.unregisterReceiver(mReceiver);
    }



    private void doDiscovery(){
        setProgressBarIndeterminateVisibility(true);
        setTitle(R.string.scanning);
        title_new_devices.setVisibility(View.VISIBLE);
        if (mService.isDiscovering()) {
            mService.cancelDiscovery();
        }
        mService.startDiscovery();
    }




    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener(){   //����б�������豸
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            mService.cancelDiscovery();

            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            System.out.println("info : " + info);
            System.out.println("address : " + address);
            Constants.printer_name_struk = info;
            Constants.printer_address_struk = address;

            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
            Log.d("���ӵ�ַ", address);

            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };



    private void getConnectPrinter(){
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DEVICE_ADDRESS, Constants.printer_address);
        Log.d("���ӵ�ַ", Constants.printer_address);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }


    private final BroadcastReceiver mReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent){
            String action = intent.getAction();

            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
                System.out.println("aaaa");
            }else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                setProgressBarIndeterminateVisibility(false);
                setTitle(R.string.select_device);
                if (mNewDevicesArrayAdapter.getCount() == 0){
                    String noDevices = getResources().getText(R.string.none_found).toString();
                    mNewDevicesArrayAdapter.add(noDevices);
                }
                System.out.println("bbbb");
            }
        }

    };


}
