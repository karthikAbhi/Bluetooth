package com.nash.basicbluetooth;

import java.io.UnsupportedEncodingException;

public class ExecuteCommand {

    //TODO - create an interface for only the connection - transfer
    /**
     * Transfer user input string or command to BT buffer by Converting input data to Byte array
     * @param dataToPrintInString - Input data
     */
    static boolean transfer(String dataToPrintInString){

        byte[] printerInput = null;

        try {
            if(dataToPrintInString.isEmpty()){
                return false;
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
        return true;
    }

    /**
     * Transfer WriterBuffer data to the WriteStream
     * @param writeData
     */
     static void transfer(byte[] writeData){
        try {
            CommandActivity.getConnectThread().writeData(writeData);
            //Thread.sleep(1000); //If, Required.
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }


}
