package com.symbol.zsdkdemo;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import com.symbol.zsdkdemo.databinding.ActivityMainBinding;
import com.zebra.zsdk_java_wrapper.mx.MXBase;
import com.zebra.zsdk_java_wrapper.mx.MXProfileProcessor;
import com.zebra.zsdk_java_wrapper.oeminfo.OEMInfoHelper;
import com.zebra.zsdk_java_wrapper.oeminfo.PackageManagerHelper;

import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;

import java.io.File;
import java.util.Objects;

public class MainActivity extends Activity implements MXBase.EventListener {

    private MXProfileProcessor profileProcessor = null;
    private OEMInfoHelper oemInfoHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // References of the UI elements
        statusTextView = (TextView) findViewById(R.id.textViewStatus);
        pwrRadioGroup = (RadioGroup) findViewById(R.id.radioGroupPwr);
        zipFilePathEditText = (EditText) findViewById(R.id.et_zip_file_path);

        // Set on Click listener to the set button to execute Power Manager
        // operations
        addSetButtonListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        oemInfoHelper = new OEMInfoHelper(this);
        profileProcessor = new MXProfileProcessor(this);
        profileProcessor.connectEMDK(this);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        //Clean up the objects created by EMDK manager
        profileProcessor.disconnectEMDK();
    }

    // Text View for displaying status of EMDK operations
    private TextView statusTextView = null;

    // Radio Group to hold Radio Buttons for Power Manager Options
    private RadioGroup pwrRadioGroup = null;

    // Edit Text that allows user to enter the path of the update package from
    // external SD Card
    private EditText zipFilePathEditText;

    private void getSerialPermission() {
        String hex = PackageManagerHelper.getPackageSignatureHex(this);
        String name = this.getPackageName();
        profileProcessor.callAccessManagerAllowCallService(
           "content://oem_info/oem.zebra.secure/build_serial",
           name,
           hex
        );
    }

    private void getWriteExternalPermission() {
        String hex = PackageManagerHelper.getPackageSignatureHex(this);
        String name = this.getPackageName();
        profileProcessor.callAccessManagerAllowPermission(
                MXBase.EPermissionType.ALL_DANGEROUS_PERMISSIONS.toString(),
               name,
               this.getApplication().getClass().getName(),
               hex
        );
    }

    // Method to set on click listener on the Set Button
    private void addSetButtonListener() {

        // Get Reference to the Set Button
        Button setButton = (Button) findViewById(R.id.buttonSet);

        // On Click Listener
        setButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Get Reference to the Radio Buttons that show various Power
                // Manager Options
                int radioid = pwrRadioGroup.getCheckedRadioButtonId();

                if (radioid == R.id.radioSuspend)
                    profileProcessor.callPowerManagerFeature(MXBase.PowerManagerOptions.SLEEP_MODE);

                if (radioid == R.id.radioReset)
                    profileProcessor.callPowerManagerFeature(MXBase.PowerManagerOptions.REBOOT);

                if (radioid == R.id.radioOSUpdate)
                    profileProcessor.callPowerManagerFeature(MXBase.PowerManagerOptions.OS_UPDATE);
            }
        });
    }

    @Override
    public void onEMDKSessionOpened() {
        statusTextView.setText("EMDK open success.");
        getWriteExternalPermission();
    }

    @Override
    public void onEMDKSessionClosed() {

    }

    @Override
    public void onEMDKProcessProfileSuccess(String profileName) {
        statusTextView.setText("EMDK process profile success: " + profileName);
        if ("AccessManagerAllowCallService" == profileName) {
            oemInfoHelper.getSerialNumber(this);

        } else if ("AccessManagerAllowPermission" == profileName) {
            getSerialPermission();
        }
    }

    @Override
    public void onEMDKError(MXBase.ErrorInfo errorInfo) {
        if (errorInfo.errorType == "File Path") {
            Toast.makeText(this.getApplicationContext(), errorInfo.errorDescription,
                    Toast.LENGTH_SHORT).show();
        } else {
            // Show dialog of Failure
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(errorInfo.errorName);
            builder.setMessage(errorInfo.errorDescription).setPositiveButton(
                    "OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    @Override
    public void onEMDKFetchContentProviderSuccess(String uri, String value) {
       if (uri == "content://oem_info/oem.zebra.secure/build_serial") {
           setTitle("Device Serial:" + value);
           statusTextView.setText("get device serial number success:" + value);
           if (checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED) {
               Toast.makeText(this, "Granted WRITE_EXTERNAL_STORAGE permission", Toast.LENGTH_LONG).show();
               oemInfoHelper.writeDataToExternalStorage(this, "serial-number.txt", value);
           }
       }
    }


}