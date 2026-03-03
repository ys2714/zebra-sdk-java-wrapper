package com.symbol.zsdkdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.symbol.zsdkdemo.databinding.ActivityPowerManagerBinding;
import com.zebra.zsdk_java_wrapper.mx.MXBase;
import com.zebra.zsdk_java_wrapper.mx.MXProfileProcessor;

public class PowerManagerActivity extends Activity implements MXBase.EventListener {

    private static final String TAG = PowerManagerActivity.class.getSimpleName();

    private MXProfileProcessor profileProcessor;
    private ActivityPowerManagerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPowerManagerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        addSetButtonListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        profileProcessor = new MXProfileProcessor(this);
        profileProcessor.connectEMDK(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (profileProcessor != null) {
            profileProcessor.disconnectEMDK();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void addSetButtonListener() {
        binding.buttonSet.setOnClickListener(v -> {
            int radioId = binding.radioGroupPwr.getCheckedRadioButtonId();

            if (radioId == R.id.radioSuspend) {
                profileProcessor.callPowerManagerFeature(MXBase.PowerManagerOptions.SLEEP_MODE);
            } else if (radioId == R.id.radioReset) {
                profileProcessor.callPowerManagerFeature(MXBase.PowerManagerOptions.REBOOT);
            } else if (radioId == R.id.radioOSUpdate) {
                String path = binding.etZipFilePath.getText().toString();
                profileProcessor.callPowerManagerFeature(
                        MXBase.PowerManagerOptions.OS_UPDATE,
                        path,
                        MXBase.PowerManagerSuppressRebootOptions.DO_NOTHING,
                        null
                );
            }
        });
    }



    @Override
    public void onEMDKSessionOpened() {

    }

    @Override
    public void onEMDKSessionClosed() {
        Log.d(TAG, "EMDK Session Closed");
        // binding.textViewStatus.setText("EMDK session closed.");
    }

    @Override
    public void onEMDKError(MXBase.ErrorInfo errorInfo) {
        Log.e(TAG, "EMDK Error: " + errorInfo.buildFailureMessage());
        if ("File Path".equals(errorInfo.errorType)) {
            Toast.makeText(getApplicationContext(), errorInfo.errorDescription, Toast.LENGTH_SHORT).show();
        } else {
            showErrorDialog(errorInfo);
        }
    }

    private void showErrorDialog(MXBase.ErrorInfo errorInfo) {
        new AlertDialog.Builder(this)
            .setTitle(errorInfo.errorName)
            .setMessage(errorInfo.errorDescription)
            .setPositiveButton("OK", null)
            .create()
            .show();
    }
}
