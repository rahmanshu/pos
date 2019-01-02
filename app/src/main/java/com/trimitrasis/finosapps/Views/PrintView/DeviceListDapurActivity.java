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
public class DeviceListDapurActivity extends AppCompatActivity{

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

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        mService = new BluetoothService(this, null);

        // Get a set of currently paired devices
        Set<BluetoothDevice> pairedDevices = mService.getPairedDev();

        // If there are paired devices, add each one to the ArrayAdapter
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

        // Indicate scanning in the title
        setProgressBarIndeterminateVisibility(true);
        setTitle(R.string.scanning);

        // Turn on sub-title for new devices
        title_new_devices.setVisibility(View.VISIBLE);

        // If we're already discovering, stop it
        if (mService.isDiscovering()) {
            mService.cancelDiscovery();
        }

        // Request discover from BluetoothAdapter
        mService.startDiscovery();
    }



    // The on-click listener for all devices in the ListViews
    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener(){
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {

            // Cancel discovery because it's costly and we're about to connect
            mService.cancelDiscovery();

            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            Constants.printer_name_dapur = info;
            Constants.printer_address_dapur = address;

            // Create the result Intent and include the MAC address
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);

            // Set result and finish this Activity
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };



    // The BroadcastReceiver that listens for discovered devices and
    // changes the title when discovery is finished
    private final BroadcastReceiver mReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent){
            String action = intent.getAction();

            // When discovery finds a device
            if(BluetoothDevice.ACTION_FOUND.equals(action)){

                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                // If it's already paired, skip it, because it's been listed already
                if(device.getBondState() != BluetoothDevice.BOND_BONDED){
                    mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }

                // When discovery is finished, change the Activity title
            }else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                setProgressBarIndeterminateVisibility(false);
                setTitle(R.string.select_device);
                if(mNewDevicesArrayAdapter.getCount() == 0){
                    String noDevices = getResources().getText(R.string.none_found).toString();
                    mNewDevicesArrayAdapter.add(noDevices);
                }
            }
        }
    };


}
