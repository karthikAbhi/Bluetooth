package com.nash.basicbluetooth;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 1;
    private static final UUID MY_UUID = UUID.fromString("187e655e-3a2d-4506-ae0b-eded9a822072");
    Button mConnectButton, mSendButton, mCloseButton;
    EditText mUserEditText;
    BluetoothAdapter mBluetoothAdapter;
    private static final String TAG = "Bluetooth APP";
    private ConnectThread mConnectThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mConnectButton = findViewById(R.id.connectButton);
        mSendButton = findViewById(R.id.sendButton);
        mCloseButton = findViewById(R.id.closeButton);
        mUserEditText = findViewById(R.id.UserEditText);

        /*
         * Check the Bluetooth Radio Support for the device
         */
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            Toast.makeText(getApplicationContext(),
                    "Device doesnot support bluetooth connectivity",
                    Toast.LENGTH_SHORT).show();
            mConnectButton.setEnabled(false);
            mUserEditText.setEnabled(false);
            mSendButton.setEnabled(false);
            mCloseButton.setEnabled(false);
        }
        else{
            Toast.makeText(getApplicationContext(),
                    "Device support bluetooth connectivity",
                    Toast.LENGTH_SHORT).show();
            mConnectButton.setEnabled(true);
            mUserEditText.setVisibility(View.INVISIBLE);
            mSendButton.setVisibility(View.INVISIBLE);
            mCloseButton.setVisibility(View.INVISIBLE);
        }

        /*
        Button Callback Methods
         */
        mConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }
                connectBTDevice();
            }
        });

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transfer(mUserEditText.getText().toString());
            }
        });

        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    mConnectThread.cancel();
                    mConnectButton.setEnabled(true);
                    mUserEditText.setVisibility(View.INVISIBLE);
                    mSendButton.setVisibility(View.INVISIBLE);
                    mCloseButton.setVisibility(View.INVISIBLE);
                }catch(Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });

    }

    private void connectBTDevice(){

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address

                        /*Toast.makeText(getApplicationContext(),
                                "Name: "+deviceName +" "+deviceHardwareAddress,
                                Toast.LENGTH_SHORT).show();*/

                if(device.getAddress().matches("^CC:93:4A:00:67:FE$")){
                    Toast.makeText(getApplicationContext(),"Device Connected!",
                            Toast.LENGTH_SHORT).show();

                    mConnectThread = new ConnectThread(device, mBluetoothAdapter);
                    new Thread(mConnectThread).start();

                    Toast.makeText(getApplicationContext(),"Ready for communication!",
                            Toast.LENGTH_SHORT).show();
                    mConnectButton.setEnabled(false);
                    mUserEditText.setVisibility(View.VISIBLE);
                    mSendButton.setVisibility(View.VISIBLE);
                    mCloseButton.setVisibility(View.VISIBLE);
                }

            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_ENABLE_BT){
            Log.i("Info","Request code correct");

            if(resultCode == RESULT_OK){
                Log.i("Info","Result code incorrect");
                Toast.makeText(getApplicationContext(),"Bluetooth On!",
                        Toast.LENGTH_SHORT).show();
            }
            else{
                Log.i("Info","Result code incorrect");
                Toast.makeText(getApplicationContext(),"Bluetooth Off!",
                        Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Log.i("Info","Improper request code");
        }
    }

    //Convert to Byte array
    private void transfer(String dataToPrintInString){

        byte[] printerInput = null;

        try {
            if(dataToPrintInString.isEmpty()){
                Toast.makeText(getApplicationContext(),"Oops! Your string is empty..",
                        Toast.LENGTH_SHORT).show();
            }
            else{
                printerInput = dataToPrintInString.getBytes("UTF-8");
                transfer(printerInput);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    //Transfer WriterBuffer data to the WriteStream
    private void transfer(byte[] writeData){
        mConnectThread.writeData(writeData);
    }

}
