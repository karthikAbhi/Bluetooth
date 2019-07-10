package com.nash.basicbluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BTSelectionActivity extends AppCompatActivity {

    //Selected Device
    private static BluetoothDevice mBluetoothDevice;

    //List of all Bluetooth devices
    List<BluetoothDevice> mActiveDevices = new ArrayList<BluetoothDevice>();

    //List of bonded devices
    Set<BluetoothDevice> mBondedDevices;

    //Adapter String - Names of all the BT Devices
    List<String> mDeviceList = new ArrayList<String>();

    //Intent Filter to register new BT devices
    IntentFilter mBTDeviceIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);

    //Array Adapter for BTDevices
    private ArrayAdapter<String> mBTDevicesListAdapter;

    //List view representing all the BT Devices
    private ListView mDevicesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btselection);

        registerReceiver(mReceiver, mBTDeviceIntent);

        mBondedDevices = MainActivity.mBluetoothAdapter.getBondedDevices();

        mDevicesListView = findViewById(R.id.select_bt_device_listview);

        mBTDevicesListAdapter = new ArrayAdapter<String>(this,
                R.layout.single_btlist, R.id.sample, mDeviceList);

        for(BluetoothDevice device : mBondedDevices){
            mDeviceList.add(device.getName());
        }
        mDevicesListView.setAdapter(mBTDevicesListAdapter);

        mDevicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String value = mBTDevicesListAdapter.getItem(position);
                connectBTDevice(value);

            }

            private void connectBTDevice(String DeviceName) {
                try{
                    if(MainActivity.mBluetoothAdapter.isDiscovering()){
                        MainActivity.mBluetoothAdapter.cancelDiscovery();
                    }
                    //Check if Bluetooth is enabled!
                    if(!MainActivity.mBluetoothAdapter.isEnabled()){
                        throw new Exception("Bluetooth is not connected!");
                    }
                    for(BluetoothDevice device : mBondedDevices){
                        if(device.getName().equals(DeviceName)){
                            Toast.makeText(getApplicationContext(),"Voila!" + device.getName() + "\n"+ device.getAddress()
                                    ,Toast.LENGTH_SHORT).show();
                            mBluetoothDevice = device;
                            startCommandActivity();
                            break;
                        }
                    }

                }catch (Exception ex){
                    ex.printStackTrace();
                    //TODO - Catch bluetooth off exception and handle it carefully
                }
            }

        });

        /*for (BluetoothDevice device : mActiveDevices) {
            String deviceName = device.getName();
            String deviceHardwareAddress = device.getAddress(); // MAC address

                        *//*Toast.makeText(getApplicationContext(),
                                "Name: "+deviceName +" "+deviceHardwareAddress,
                                Toast.LENGTH_SHORT).show();*//*

            if(device.getAddress().matches("^CC:93:4A:00:67:FE$")){
                Toast.makeText(getApplicationContext(),"Device Connected!",
                        Toast.LENGTH_SHORT).show();

                //mConnectThread = new ConnectThread(device, MainActivity.mBluetoothAdapter);
                //new Thread(mConnectThread).start();

                Toast.makeText(getApplicationContext(),"Ready for communication!",
                        Toast.LENGTH_SHORT).show();
            }

        }*/
    }

    private void startCommandActivity(){
        Intent intent = new Intent(getApplicationContext(), CommandActivity.class);
        startActivity(intent);
        this.finish();
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND == action){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(device != null){
                    mActiveDevices.add(device);
                    //mDeviceList.add(device.getName()+"");
                    //mDevicesListView.setAdapter(mBTDevicesListAdapter);
                }
            }
        }
    };

    public static BluetoothDevice getBluetoothDevice(){
        return mBluetoothDevice;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
