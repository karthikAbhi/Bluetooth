package com.nash.basicbluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 1;
    Button mConnectButton;
    public static BluetoothAdapter mBluetoothAdapter;
    private static final String TAG = "Bluetooth APP";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mConnectButton = findViewById(R.id.connectButton);

        Log.i(TAG,"MainActivity Started...");

        // Check the Bluetooth Radio Support for the device
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            Toast.makeText(getApplicationContext(),
                    "Device doesn't support bluetooth connectivity",
                    Toast.LENGTH_SHORT).show();
            mConnectButton.setEnabled(false);
            Log.i(TAG,"No bluetooth support for this device");
        }
        else{
            Toast.makeText(getApplicationContext(),
                    "Device support bluetooth connectivity",
                    Toast.LENGTH_SHORT).show();
            mConnectButton.setEnabled(true);

            if (!mBluetoothAdapter.isEnabled()) {
                Log.i(TAG,"Bluetooth off... Turning on now...");
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
            else{
                Log.i(TAG,"Bluetooth is already turned on...");
            }
        }


        //Button Callback Methods
        mConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"Started new activity: BTSelectionActivity...");
                startBTActivity();
            }
        });

    }

    private void startBTActivity(){
        Intent intent = new Intent(this, BTSelectionActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_ENABLE_BT){
            Log.i("Info","Request code correct");

            if(resultCode == RESULT_OK){
                Log.i("Info","Result code incorrect");
                Toast.makeText(getApplicationContext(),"Bluetooth State: On",
                        Toast.LENGTH_SHORT).show();
            }
            else{
                Log.i("Info","Result code incorrect");
                Toast.makeText(getApplicationContext(),"Bluetooth State: Off",
                        Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Log.i("Info","Improper request code");
        }
    }
}
