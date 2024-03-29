package com.nash.basicbluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

/*
 *  Child class executing another thread for bluetooth connection since it's a blocking call
 */
class ConnectThread extends Thread {
    private static final String TAG = "ConnectThread" ;
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private final BluetoothAdapter mmBTAdapter;
    private MyBluetoothService.ConnectedThread myConnectedThread;

    public ConnectThread(BluetoothDevice device, BluetoothAdapter bluetoothAdapter) {
        // Use a temporary object that is later assigned to mmSocket
        // because mmSocket is final.
        BluetoothSocket tmp = null;
        mmBTAdapter = bluetoothAdapter;
        mmDevice = device;

        try {
            // Get a BluetoothSocket to connect with the given BluetoothDevice.
            // MY_UUID is the app's UUID string, also used in the server code.
            UUID uuid = mmDevice.getUuids()[0].getUuid();
            tmp = device.createRfcommSocketToServiceRecord(uuid);
        } catch (IOException e) {
            Log.e(TAG, "Socket's create() method failed", e);
        }
        mmSocket = tmp;
    }

    public void run() {
        // Cancel discovery because it otherwise slows down the connection.
        mmBTAdapter.cancelDiscovery();

        try {
            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception.
            mmSocket.connect();

            if(mmSocket.isConnected()){
                Log.i(TAG,"Socket connected!");
            }

        } catch (IOException connectException) {
            // Unable to connect; close the socket and return.
            try {
                mmSocket.close();
            } catch (IOException closeException) {
                Log.e(TAG, "Could not close the client socket", closeException);
            }
            return;
        }

        // The connection attempt succeeded. Perform work associated with
        // the connection in a separate thread.
        manageMyConnectedSocket(mmSocket);
    }

    public void manageMyConnectedSocket(BluetoothSocket mmSocket) {

        myConnectedThread = new MyBluetoothService.ConnectedThread(mmSocket);

        new Thread(myConnectedThread).start();

        //cancel();
    }

    public void writeData(byte[] writeBuffer) throws IOException{
        myConnectedThread.write(writeBuffer);
    }

    // Closes the client socket and causes the thread to finish.
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the client socket", e);
        }
    }
}
