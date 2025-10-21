package com.symbol.zebra_sdk_java_wrapper;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.symbol.zebra_sdk_java_wrapper.databinding.ActivityOsupdateBinding;
import com.zebra.zsdk_java_wrapper.MXBase;
import com.zebra.zsdk_java_wrapper.MXProfileProcessor;

import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class MainActivity extends Activity implements MXBase.EventListener {

    private MXProfileProcessor profileProcessor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Assign the profile name used in EMDKConfig.xml
        String profileName = "OSUpdateProfile";
        profileProcessor = new MXProfileProcessor(this);
        profileProcessor.connectEMDK(this);

        ActivityOsupdateBinding binding = ActivityOsupdateBinding.inflate(getLayoutInflater());
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
                    profileProcessor.processProfile(
                            com.zebra.zsdk_java_wrapper.R.raw.profile_sleep, "ProfileSleep");

                if (radioid == R.id.radioReset)
                    profileProcessor.processProfile(
                            com.zebra.zsdk_java_wrapper.R.raw.profile_reboot, "ProfileReboot");

                if (radioid == R.id.radioOSUpdate)
                    profileProcessor.processProfile(
                            com.zebra.zsdk_java_wrapper.R.raw.profile_update, "ProfileUpdate");
                    // powerManagerHelper.callFeature(MXPowerManagerHelper.Options.OS_UPDATE, zipFilePathEditText.getText().toString());
            }
        });
    }

    @Override
    public void onEMDKSessionOpened() {
        statusTextView.setText("EMDK open success.");
    }

    @Override
    public void onEMDKSessionClosed() {

    }

    @Override
    public void onEMDKError(MXBase.ErrorInfo errorInfo) {
        if (errorInfo.errorType == "File Path") {
            Toast.makeText(this.getApplicationContext(), errorInfo.errorDescription,
                    Toast.LENGTH_SHORT).show();
        } else {
            // Show dialog of Failure
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getApplicationContext());
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
}