package com.nash.basicbluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nash.mybluetoothprinterlibrary.MyBluetoothPrinter;

import java.io.IOException;
import java.util.Calendar;

public class CommandActivity extends AppCompatActivity {


    private BluetoothDevice mBTDevice;
    private static ConnectThread mConnectThread;
    private MyBluetoothPrinter mMyBluetoothPrinter;
    private Context mContext;

    // --- UI - Button References ---

    private Button mCloseBTN, mDiscoverBTN;
    private Button mPrintButton;
    private Button mLFCommandButton;
    private Button mFFCommandButton;
    private Button mPMCommandButton;
    private Button mSMCommandButton;
    private Button mHTCommandButton;
    private Button mInitializePrinterCommandButton;
    private Button mGSMFCommandButton;
    private Button mCutFormCommandButton;
    private Button mFFIPIXELSCommandButton;
    private Button mPDFF2FCommandButton;
    private Button mPDPMCommandButton;
    private Button mPDFNLCommandButton;
    private Button mSLFCommandButton;
    private Button mSGRSCCommandButton;
    private Button mSPPMCommandButton;
    private Button mSelectFontCommandButton;
    private Button mSelCharCodeTableCommandButton;
    private Button mSpecifyDownldCharCommandButton;
    private Button mSetOrUnsetDCSCommandButton;
    private Button mSetLeftMarginCommandButton;
    private Button mSetWidthOfPrintAreaCommandButton;
    private Button mSpecifyPrintAreaOnPageModeCommandButton;
    private Button mSetPhysicalPositionCommandButton;
    private Button mSetLogicalPositionCommandButton;
    private Button mSetVerticalPhysicalPositionOnPageModeCommandButton;
    private Button mSelectLSB_MSBDirectionInImageCommandButton;
    private Button mSetVerticalLogicalPositionOnPageModeCommandButton;
    private Button mSetPrintPositionOfHRICharCommandButton;
    private Button mSelectHRICharacterSizeCommandButton;
    private Button mSetBarcodeHeightCommandButton;
    private Button mSetBarcodeWidthCommandButton;
    private Button mSetNWAspectBarcode;
    private Button mPrintBarcodeButton;
    private Button mSetPrintDensity;
    //private Button mSpecifyResponseParameterCommandButton;
    private Button mInformSysTimeOfHostCommandButton;
    private Button mPrintBitImageCommandButton;
    private Button mPrintRasterBitImageCommandButton;
    private Button mPrintNVBitImageCommandButton;
    private Button mSelectNVBitImageCommandButton;
    private Button mDefineNVBitImageCommandButton;

    // --- UI - EditText References ---

    private EditText mSampleTextEditText;
    private EditText mPDFF2FEditText;
    private EditText mPDFNLEditText;
    private EditText mSLFEditText;
    private EditText mSGRSCEditText;
    private EditText mSPPMEditText;
    private EditText mSelectFontEditText;
    private EditText mSelCharCodeTableEditText;
    private EditText mSpecifyDownldCharEditText;
    private EditText mSetOrUnsetDCSEditText;
    private EditText mSetLeftMarginEditText;
    private EditText mSetWidthOfPrintAreaEditText;
    private EditText mSPAPMxaxisEditText,mSPAPMyaxisEditText,mSPAPMxlengthEditText,
            mSPAPMylengthEditText;//mSpecifyPrintAreaOnPageModeEditText
    private EditText mSetPhysicalPositionEditText;
    private EditText mSetLogicalPositionEditText;
    private EditText mSetVerticalPhysicalPositionOnPageModeEditText;
    private EditText mSetVerticalLogicalPositionOnPageModeEditText;
    //Raster Bit - Printing
    private EditText mModeOfRasterBitEditText;
    //NV Bit - Printing
    private EditText mSelectPNVBitEditText, mModePNVBitEditText;
    //NV Bit - Selection
    private EditText mSelectSNVBitEditText, mModeSNVBitEditText, mOffsetSNVBitEditText;
    private EditText mNumberOfNVBitImagesEditText;
    private EditText mSelectLSB_MSBDirectionInImageEditText;
    private EditText mSetPrintPositionOfHRICharEditText;
    private EditText mSelectHRICharacterSizeEditText;
    private EditText mSetBarcodeHeightEditText;
    private EditText mSetBarcodeWidthEditText;
    private EditText mSetNWAspectBarcodeEditText;
    private EditText mSetPrintDensityEditText;
    private EditText mSpecifyResponseParameterEditText;
    private EditText mInformSysTimeOfHostEditText;
    private EditText mFFIPIXELSEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command);

        mContext = getApplicationContext();

        mMyBluetoothPrinter = new MyBluetoothPrinter(mContext) {
            @Override
            public void transfer(byte[] dataToPrintInBytes) {
                try {
                    CommandActivity.getConnectThread().writeData(dataToPrintInBytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        connectBTDevice();

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        this.registerReceiver(mReceiver, filter);

        mCloseBTN = findViewById(R.id.closeBTN);

        mDiscoverBTN = findViewById(R.id.discoverBTN);
        mDiscoverBTN.setVisibility(View.INVISIBLE);

        // --- UI Components - EditText Reference Creation ---

        mSampleTextEditText = findViewById(R.id.sampleTextEditText);
        mPDFF2FEditText = findViewById(R.id.pdff2fEditText);
        mPDFNLEditText = findViewById(R.id.pdfnlEditText);
        mSLFEditText = findViewById(R.id.slfEditText);
        mSGRSCEditText = findViewById(R.id.sgrscEditText);
        mSPPMEditText = findViewById(R.id.sppmEditText);
        mSelectFontEditText =findViewById(R.id.selFontEditText);
        mSetOrUnsetDCSEditText = findViewById(R.id.setOrUnsetDCSetEditText);
        mSetLeftMarginEditText = findViewById(R.id.setLeftMarginEditText);
        mSetWidthOfPrintAreaEditText = findViewById(R.id.setWidthOfPrintAreaEditText);
        mSPAPMxaxisEditText = findViewById(R.id.spapmxaxisEditText);
        mSPAPMyaxisEditText = findViewById(R.id.spapmyaxisEditText);
        mSPAPMxlengthEditText = findViewById(R.id.spapmxlengthEditText);
        mSPAPMylengthEditText = findViewById(R.id.spapmylengthEditText);
        mSetPhysicalPositionEditText = findViewById(R.id.setPhysicalPositionEditText);
        mSetLogicalPositionEditText = findViewById(R.id.setLogicalPositionEditText);
        mSetVerticalPhysicalPositionOnPageModeEditText = findViewById(R.id.setVPPOnPageModeEditText);
        mSetVerticalLogicalPositionOnPageModeEditText = findViewById(R.id.setVLPOnPageModeEditText);
        mModeOfRasterBitEditText = findViewById(R.id.modeOfRasterBitEditText);
        mSelectPNVBitEditText = findViewById(R.id.selectPNVBitEditText);
        mModePNVBitEditText = findViewById(R.id.modePNVBitEditText);
        mSelectSNVBitEditText = findViewById(R.id.selectSNVBitEditText);
        mModeSNVBitEditText = findViewById(R.id.modeSNVBitEditText);
        mOffsetSNVBitEditText = findViewById(R.id.offsetSNVBitEditText);
        mNumberOfNVBitImagesEditText = findViewById(R.id.numberOfNVBitImagesEditText);
        mSelectLSB_MSBDirectionInImageEditText = findViewById(R.id.selectLSB_MSBDirInImgEditText);
        mSetPrintPositionOfHRICharEditText = findViewById(R.id.setPosHRICharEditText);
        mSelectHRICharacterSizeEditText = findViewById(R.id.selectHRICharSizeEditText);
        mSetBarcodeHeightEditText = findViewById(R.id.setBarcodeHeightEditText);
        mSetBarcodeWidthEditText = findViewById(R.id.setBarcodeWidthEditText);
        mSetNWAspectBarcodeEditText = findViewById(R.id.setNWAspectOfBarcodeEditText);
        mSetPrintDensityEditText = findViewById(R.id.setPrintDensityEditText);
        mFFIPIXELSEditText = findViewById(R.id.ffinPixelsEditText);

        // --- UI Components - Button Reference Creation ---
        //Print Text
        mPrintButton = findViewById(R.id.printButton);
        //Print Barcode
        mPrintBarcodeButton = findViewById(R.id.printBarcodeButton);
        //LF Command
        mLFCommandButton = findViewById(R.id.lfCommandButton);
        //FF Command
        mFFCommandButton = findViewById(R.id.ffCommandButton);
        //Print data and Feed form to forward Command
        mPDFF2FCommandButton = findViewById(R.id.pdff2fButton);
        //Print data on feed n lines Command
        mPDFNLCommandButton = findViewById(R.id.pdfnlButton);
        //Print data on page mode Command
        mPDPMCommandButton = findViewById(R.id.pdpmButton);
        //Set the line feed amount Command
        mSLFCommandButton = findViewById(R.id.slfButton);
        //Set the gap of right side of the characters Command (14.07)
        mSGRSCCommandButton = findViewById(R.id.sgrscButton);
        //Set parameters of Print mode Command (14.08)
        mSPPMCommandButton = findViewById(R.id.sppmButton);
        //Select the font Command (14.09)
        mSelectFontCommandButton = findViewById(R.id.selFontButton);
        //Select character code table Command (14.10)
        mSelCharCodeTableCommandButton = findViewById(R.id.selCharCodeTableButton);
        //Specify download character Command (14.11)
        mSpecifyDownldCharCommandButton = findViewById(R.id.specifyDownldCharButton);
        //Set or unset download character set Command (14.12)
        mSetOrUnsetDCSCommandButton = findViewById(R.id.setOrUnsetDCSetButton);
        //Page Mode Command (14.13)
        mPMCommandButton = findViewById(R.id.pageModeButton);
        //Standard Mode Command (14.14)
        mSMCommandButton = findViewById(R.id.standardModeButton);
        //Horizontal Tab Command (14.15)
        mHTCommandButton = findViewById(R.id.horizontalTabButton);
        //Set the left side margin Command (14.16)
        mSetLeftMarginCommandButton = findViewById(R.id.setLeftMarginButton);
        //Set the width of print area Command (14.17)
        mSetWidthOfPrintAreaCommandButton = findViewById(R.id.setWidthOfPrintAreaButton);
        //Set the print area on page mode (14.18)
        mSpecifyPrintAreaOnPageModeCommandButton = findViewById(R.id.specifyPrintAreaOnPageModeButton);
        //Set the physical position (14.19)
        mSetPhysicalPositionCommandButton = findViewById(R.id.setPhysicalPositionButton);
        //Set the logical position (14.20)
        mSetLogicalPositionCommandButton = findViewById(R.id.setLogicalPositionButton);
        //Set the vertical physical position in page mode (14.21)
        mSetVerticalPhysicalPositionOnPageModeCommandButton = findViewById(R.id.setVPPOnPageModeButton);
        //Set the vertical logical position in page mode (14.22)
        mSetVerticalLogicalPositionOnPageModeCommandButton = findViewById(R.id.setVLPOnPageModeButton);
        //Print bit image (14.23)
        mPrintBitImageCommandButton = findViewById(R.id.printBitImageButton);
        //Print raster bit image (14.24)
        mPrintRasterBitImageCommandButton = findViewById(R.id.printRasterBitImageButton);
        //Print NV Bit Image (14.25)
        mPrintNVBitImageCommandButton = findViewById(R.id.printNVBitImageButton);
        //Select NV Bit Image (14.25)
        mSelectNVBitImageCommandButton = findViewById(R.id.selectNVBitImageButton);
        //Define NV Bit Image (14.26)
        mDefineNVBitImageCommandButton = findViewById(R.id.defineNVBitImageButton);
        //Select LSB-MSB direction in image (14.27)
        mSelectLSB_MSBDirectionInImageCommandButton = findViewById(R.id.selectLSB_MSBDirInImgButton);
        //Set print position of HRI characters (14.28)
        mSetPrintPositionOfHRICharCommandButton = findViewById(R.id.setPosHRICharButton);
        //Select HRI character size (14.29)
        mSelectHRICharacterSizeCommandButton = findViewById(R.id.selectHRICharSizeButton);
        //Set barcode height (14.30)
        mSetBarcodeHeightCommandButton = findViewById(R.id.setBarcodeHeightButton);
        //Set barcode width (14.31)
        mSetBarcodeWidthCommandButton = findViewById(R.id.setBarcodeWidthButton);
        //Set N:W aspect of the barcode (14.32)
        mSetNWAspectBarcode = findViewById(R.id.setNWAspectOfBarcodeButton);
        //Initialize Printer Command
        mInitializePrinterCommandButton = findViewById(R.id.initializePrinterButton);
        //Set print density (14.40)
        mSetPrintDensity = findViewById(R.id.setPrintDensityButton);
        //Cue the Marked form Command
        mGSMFCommandButton = findViewById(R.id.gsMFButton);
        //Cut form Command
        mCutFormCommandButton = findViewById(R.id.cutFormButton);
        //Inform system time of the host (14.51)
        mInformSysTimeOfHostCommandButton = findViewById(R.id.infoSysTimeHostButton);
        //Feed form in pixels Command (14.54)
        mFFIPIXELSCommandButton = findViewById(R.id.ffinPixelsButton);

        // --- IMPLEMENTATION ---

        mCloseBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeIntent();
            }
        });

        mDiscoverBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discoverBTDevices();
            }
        });

        //Basic Print Command
        mPrintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyBluetoothPrinter.printText(mSampleTextEditText.getText().toString());
            }
        });


        mPDFF2FEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyBluetoothPrinter.ESC_J(mPDFF2FEditText.getText().toString());
            }
        });//Print data and feed a line (14.01)
        mLFCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyBluetoothPrinter.LF();
            }
        });
        //Print data and feed a page on page mode (14.02)
        mFFCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyBluetoothPrinter.FF();
            }
        });
        //Print data on page mode (14.03)
        mPDPMCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyBluetoothPrinter.ESC_FF();
            }
        });
        //Print data and feed the form to forward (14.04)
        mPDFF2FCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyBluetoothPrinter.ESC_J(mPDFF2FEditText.getText().toString());
            }
        });
        //Print data and feed n lines (14.05)
        mPDFNLCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyBluetoothPrinter.ESC_D(mPDFNLEditText.getText().toString());
            }
        });
        mSLFCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyBluetoothPrinter.ESC_3(mSLFEditText.getText().toString());
            }
        });
        mSGRSCCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyBluetoothPrinter.ESC_SP(mSGRSCEditText.getText().toString());
            }
        });
        //Need to implement parameters
        mSPPMCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyBluetoothPrinter.ESC_PM(mSPPMEditText.getText().toString());
            }
        });
        //Need to implement parameters
        mSelectFontCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyBluetoothPrinter.ESC_M(mSelectFontEditText.getText().toString());
            }
        });
        //TODO - Praveen need to provide more information
        mSelCharCodeTableCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] a = new byte[1];
                a[0] = (byte)0;//Default - 0
                mMyBluetoothPrinter.ESC_T(a);
            }
        });
        //TODO - Need to talk to Reddy sir
        mSpecifyDownldCharCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Will be implemented later
                Toast.makeText(getApplicationContext(),"Will be implemented later",
                        Toast.LENGTH_SHORT).show();
            }
        });
        mSetOrUnsetDCSCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyBluetoothPrinter.ESC_SDC(mSetOrUnsetDCSEditText.getText().toString());
            }
        });


        mPMCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyBluetoothPrinter.PAGE_MODE();
            }
        });

        mSMCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyBluetoothPrinter.STANDARD_MODE();
            }
        });
        mHTCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyBluetoothPrinter.HT();
            }
        });
        //Set the left side margin Command (14.16)
        mSetLeftMarginCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyBluetoothPrinter.GS_L(mSetLeftMarginEditText.getText().toString());
            }
        });
        //Set the width of print area Command (14.17)
        mSetWidthOfPrintAreaCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyBluetoothPrinter.GS_W(mSetWidthOfPrintAreaEditText.getText().toString());
            }
        });
        //Set the print area on page mode (14.18)
        mSpecifyPrintAreaOnPageModeCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMyBluetoothPrinter.ESC_W(mSPAPMxaxisEditText.getText().toString(),
                        mSPAPMyaxisEditText.getText().toString(),
                        mSPAPMxlengthEditText.getText().toString(),
                        mSPAPMylengthEditText.getText().toString());
            }
        });
        //Set the physical position (14.19)
        mSetPhysicalPositionCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyBluetoothPrinter.ESC_PP(mSetPhysicalPositionEditText.getText().toString());
            }
        });
        //Set the logical position (14.20)
        mSetLogicalPositionCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyBluetoothPrinter.ESC_LP(mSetLogicalPositionEditText.getText().toString());
            }
        });
        //Set the vertical physical position in page mode (14.21)
        mSetVerticalPhysicalPositionOnPageModeCommandButton.
                setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyBluetoothPrinter.GS_VPP(mSetVerticalPhysicalPositionOnPageModeEditText.
                        getText().toString());
            }
        });
        //Set the vertical logical position on page mode (14.22)
        mSetVerticalLogicalPositionOnPageModeCommandButton.
                setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyBluetoothPrinter.GS_VLP(mSetVerticalLogicalPositionOnPageModeEditText.
                        getText().toString());
            }
        });
        //Print bit image (14.23)
        mPrintBitImageCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickBitImage();
            }
        });

        //Print raster bit image (14.24)
        mPrintRasterBitImageCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickRasterBitImage();
            }
        });
        //Print NV Bit Image (14.25)
        mPrintNVBitImageCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyBluetoothPrinter.FS_PP(mSelectPNVBitEditText.getText().toString(),
                        mModePNVBitEditText.getText().toString());
            }
        });
        //Select NV Bit Image (14.25)
        mSelectNVBitImageCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyBluetoothPrinter.FS_PS(mSelectSNVBitEditText.getText().toString(),
                        mModeSNVBitEditText.getText().toString(),
                        mOffsetSNVBitEditText.getText().toString());
            }
        });
        //Define NV Bit Image (14.26)
        mDefineNVBitImageCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(Integer.parseInt(mNumberOfNVBitImagesEditText.getText().toString()) > 0){
                        pickNVBitImage();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),
                                "Enter number of NV bit Images to be defined",
                                Toast.LENGTH_SHORT).show();
                    }
                }catch (NumberFormatException e){
                    Log.e("Error",e.getMessage());
                }


            }
        });

        //Select LSB-MSB direction in image (14.27)
        mSelectLSB_MSBDirectionInImageCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyBluetoothPrinter.DC2_DIR(mSelectLSB_MSBDirectionInImageEditText.
                        getText().toString());
            }
        });

        //Set print position of HRI characters (14.28)
        mSetPrintPositionOfHRICharCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyBluetoothPrinter.GS_H(mSetPrintPositionOfHRICharEditText.getText().toString());
            }
        });

        //Select HRI character size (14.29)
        mSelectHRICharacterSizeCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyBluetoothPrinter.GS_F(mSelectHRICharacterSizeEditText.getText().toString());
            }
        });

        //Set barcode height (14.30)
        mSetBarcodeHeightCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyBluetoothPrinter.GS_h(mSetBarcodeHeightEditText.getText().toString());
            }
        });

        //Set barcode width (14.31)
        mSetBarcodeWidthCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyBluetoothPrinter.GS_w(mSetBarcodeWidthEditText.getText().toString());
            }
        });

        //Set N:W aspect of the barcode (14.32)
        mSetNWAspectBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyBluetoothPrinter.DC2_ARB(mSetNWAspectBarcodeEditText.getText().toString());
            }
        });

        //Print Barcode (14.33)
        mPrintBarcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //printBarcode();
            }
        });

        //Initialize the mMyBluetoothPrinter (14.34)
        mInitializePrinterCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyBluetoothPrinter.ESC_INIT();
            }
        });

        //Set print density (14.40)
        mSetPrintDensity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyBluetoothPrinter.DC2_PD(mSetPrintDensityEditText.getText().toString());
            }
        });

        /*Cue the marked form(feed the form to print start position)
         * (14.41)
         */
        mGSMFCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyBluetoothPrinter.GS_MF();
            }
        });

        //Cut the form (14.43)
        mCutFormCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyBluetoothPrinter.ESC_I();
            }
        });

        //Inform system time of the host (14.51)
        mInformSysTimeOfHostCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] a = new byte[6];
                a[0] = (byte) Calendar.getInstance().get(Calendar.SECOND);//d1 - second
                a[1] = (byte)Calendar.getInstance().get(Calendar.MINUTE);//d2 - minute
                a[2] = (byte)Calendar.getInstance().get(Calendar.HOUR);//d3 - hour
                a[3] = (byte)Calendar.getInstance().get(Calendar.DAY_OF_MONTH);//d4 - day
                a[4] = (byte)Calendar.getInstance().get(Calendar.MONTH);//d5 - month
                a[5] = (byte)Calendar.getInstance().get(Calendar.YEAR);//d6 - year

                mMyBluetoothPrinter.GS_i(a);
            }
        });

        //Download the firmware (14.52)

        //Feed the form in pixels (14.54)
        mFFIPIXELSCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyBluetoothPrinter.GS_d(mFFIPIXELSEditText.getText().toString());
            }
        });
    }

    /**
     * Discover Devices and connect automatically if device connection is lost unexpectedly
     */
    private void discoverBTDevices(){
        if(MainActivity.mBluetoothAdapter.isEnabled()){
            if(MainActivity.mBluetoothAdapter.isDiscovering()){
                MainActivity.mBluetoothAdapter.cancelDiscovery();
            }
            else{
                try {
                    MainActivity.mBluetoothAdapter.startDiscovery();
                    Thread.sleep(3000);
                    MainActivity.mBluetoothAdapter.cancelDiscovery();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Broadcast Receiver to listen for all the actions specified in the Intent Filter
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                //Device Found
                if(device.equals(mBTDevice)){
                    Log.i("BT", "Found");
                    Toast.makeText(getApplicationContext(),"Device Found!",
                            Toast.LENGTH_SHORT)
                            .show();
                    establishConnection(device);

                }
            }
            else if(BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)){
                mDiscoverBTN.setVisibility(View.INVISIBLE);
                Log.i("BT", "ACTION_ACL_CONNECTED");
                Toast.makeText(getApplicationContext(),"Action ACL Connected!",
                        Toast.LENGTH_SHORT).show();
                if(device.equals(mBTDevice)){
                    Log.i("BT", "Found");
                    Toast.makeText(getApplicationContext(),"Device Connected!",
                            Toast.LENGTH_SHORT).show();
                }
            }
            else if(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)){
                Log.i("BT", "ACTION_ACL_DISCONNECT_REQUESTED");
                Toast.makeText(getApplicationContext(),"ACTION_ACL_DISCONNECT_REQUESTED!",
                        Toast.LENGTH_SHORT).show();
            }
            else if(BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)){
                mDiscoverBTN.setVisibility(View.VISIBLE);
                Log.i("BT", "ACTION_ACL_DISCONNECTED");
                Toast.makeText(getApplicationContext(),"ACTION_ACL_DISCONNECTED",
                        Toast.LENGTH_SHORT).show();
                if(device.equals(mBTDevice)){
                    if(device.equals(mBTDevice)){
                        Log.i("BT", "Found");
                        Toast.makeText(getApplicationContext(),
                                "Device Disconnected!\n" + mBTDevice.getName(), Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            }
            else{
                Toast.makeText(getApplicationContext(),"Something is broadcasted!",
                        Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * Finish the BT connection by releasing all the resources and go to MainActivity
     */
    private void closeIntent(){
        mConnectThread.cancel();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    /**
     * Get the selected BT Device from BTSelection Activity and BT establishConnection
     */
    private void connectBTDevice(){
        mBTDevice = BTSelectionActivity.getBluetoothDevice();
        establishConnection(mBTDevice);
    }

    /**
     * Establish BT connection with BT device provided
     * @param device - BT Device
     */
    private void establishConnection(BluetoothDevice device){
        mConnectThread = new ConnectThread(device, MainActivity.mBluetoothAdapter);
        new Thread(mConnectThread).start();
    }

    protected static ConnectThread getConnectThread(){
        return mConnectThread;
    }

    /***
     * Supporting Methods
     */
    private void pickBitImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra("crop","true");
        intent.putExtra("scale",false);
        intent.putExtra("return-data",true);
        startActivityForResult(intent, 1);
    }

    private void pickRasterBitImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra("crop","true");
        intent.putExtra("scale",true);
        intent.putExtra("return-data",true);
        startActivityForResult(intent, 2);
    }

    private void pickNVBitImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        //intent.putExtra("return-data",true);
        startActivityForResult(intent, 3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK){
            return;
        }
        //TODO - Bit Image Selection
        if(requestCode == 1){
            final Bundle extras = data.getExtras();
            if(extras != null){
                //Get Image from Gallery
                //mBmp = extras.getParcelable("data");
            }
        }
        //Raster Bit Image Selection
        else if(requestCode == 2){
            final Bundle extras = data.getExtras();
            if(extras != null){
                //Get Image from Gallery
                Bitmap bmp = extras.getParcelable("data");
                mMyBluetoothPrinter.GS_V(bmp, mModeOfRasterBitEditText.getText().toString());
            }
        }
        //NV Bit Image Selection
        else if(requestCode == 3){
            /**
             * Case 1: Number of NV Bit Images = 1
             * Case 2: Number of NV Bit Images > 1
             */
            //Case 1:
            if(data.getData() != null) {
                Uri uri = data.getData();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.
                            getBitmap(getApplicationContext().getContentResolver(), uri);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                mMyBluetoothPrinter.FS_Q(new Bitmap[]{bitmap}, mNumberOfNVBitImagesEditText.
                        getText().toString());
            }
            //Case 2:
            else if(data.getClipData() != null){

                ClipData clipData = data.getClipData();
                int numberOfImages = clipData.getItemCount();
                Bitmap[] bitmapArray = new Bitmap[numberOfImages];
                for(int i = 0; i < numberOfImages; i++){
                    ClipData.Item item = clipData.getItemAt(i);
                    Uri uri = item.getUri();
                    try{
                        bitmapArray[i] = MediaStore.Images.Media.
                                getBitmap(getApplicationContext().getContentResolver(),uri);

                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                mMyBluetoothPrinter.FS_Q(bitmapArray, mNumberOfNVBitImagesEditText.
                        getText().toString());
            }
            else{
                Toast.makeText(getApplicationContext(),"Invalid!",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
