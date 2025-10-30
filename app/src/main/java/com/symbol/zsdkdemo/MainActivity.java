package com.symbol.zsdkdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.symbol.zsdkdemo.databinding.ActivityMainBinding;
import com.zebra.zsdk_java_wrapper.mx.MXBase;
import com.zebra.zsdk_java_wrapper.mx.MXProfileProcessor;

public class MainActivity extends Activity implements MXBase.EventListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private MXProfileProcessor profileProcessor;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
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
    protected void onDestroy() {
        super.onDestroy();
        if (profileProcessor != null) {
            profileProcessor.disconnectEMDK();
        }
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
                profileProcessor.callPowerManagerFeature(MXBase.PowerManagerOptions.OS_UPDATE, path, null);
            }
        });
    }

    public void handleFetchSerialNumberSuccess(String result) {
        String text = "Serial:" + result;
        binding.textViewSerialNumber.setText(text);
        setTitle(text);
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    public void handleFetchIMEISuccess(String result) {
        String text = "IMEI:" + result;
        binding.textViewIMEI.setText(text);
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEMDKSessionOpened() {
        binding.textViewStatus.setText("EMDK open success.");
        
        profileProcessor.fetchSerialNumberInBackground(this, new MXBase.FetchOEMInfoCallback() {
            @Override
            public void onSuccess(String result) {
                runOnUiThread(() -> handleFetchSerialNumberSuccess(result));
            }

            @Override
            public void onError() {
                Log.d(TAG, "Failed to fetch serial number");
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Failed to fetch Serial Number", Toast.LENGTH_SHORT).show());
            }
        });
        
        profileProcessor.fetchIMEIInBackground(this, new MXBase.FetchOEMInfoCallback() {
            @Override
            public void onSuccess(String result) {
                runOnUiThread(() -> handleFetchIMEISuccess(result));
            }

            @Override
            public void onError() {
                Log.d(TAG, "Failed to fetch IMEI");
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Failed to fetch IMEI", Toast.LENGTH_SHORT).show());
            }
        });
    }

    @Override
    public void onEMDKSessionClosed() {
        Log.d(TAG, "EMDK Session Closed");
        binding.textViewStatus.setText("EMDK session closed.");
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
