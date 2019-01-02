package com.trimitrasis.finosapps.Views.PrintView;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.UtilView.Constants;
import com.zj.btsdk.BluetoothService;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by rahman on 21/07/2017.
 */

@EActivity(R.layout.layout_device_list_printer)
public class ConnectPrinterDapurActivity extends AppCompatActivity{

    @ViewById
    Button button_scan;

    @ViewById
    ListView paired_devices, new_devices;

    // Return Intent extra
    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    BluetoothService mService = null;

    @AfterViews
    void afterView(){

        setResult(Activity.RESULT_CANCELED);
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        mService = new BluetoothService(this, null);
        getConnectPrinter();

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
            System.out.println("mservice dapur matiii");
            mService.cancelDiscovery();
        }
        mService = null;
        this.unregisterReceiver(mReceiver);
    }



    private void doDiscovery(){
        setProgressBarIndeterminateVisibility(true);
        setTitle(R.string.scanning);
        findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);
        if (mService.isDiscovering()){
            mService.cancelDiscovery();
        }
        mService.startDiscovery();
    }


    private void getConnectPrinter(){
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DEVICE_ADDRESS, Constants.printer_address_dapur);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }


    private final BroadcastReceiver mReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent){
            String action = intent.getAction();

            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(device.getBondState() != BluetoothDevice.BOND_BONDED){
                    Toast.makeText(ConnectPrinterDapurActivity.this, device.getName() + "\n" + device.getAddress() , Toast.LENGTH_LONG).show();
                }
            }else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                setProgressBarIndeterminateVisibility(false);
                setTitle(R.string.select_device);
                String noDevices = getResources().getText(R.string.none_found).toString();
                Toast.makeText(ConnectPrinterDapurActivity.this, noDevices , Toast.LENGTH_LONG).show();
            }
        }

    };

}
