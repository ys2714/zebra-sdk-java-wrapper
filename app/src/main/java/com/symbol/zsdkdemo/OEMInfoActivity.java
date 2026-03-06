package com.symbol.zsdkdemo;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.symbol.zsdkdemo.databinding.ActivityOeminfoBinding;
import com.zebra.zsdk_java_wrapper.mx.MXBase;
import com.zebra.zsdk_java_wrapper.mx.MXProfileProcessor;
import com.zebra.zsdk_java_wrapper.utils.DeviceBootTimeHelper;

public class OEMInfoActivity extends AppCompatActivity {

    private static final String TAG = PowerManagerActivity.class.getSimpleName();

    private ActivityOeminfoBinding binding;
    private MXProfileProcessor profileProcessor;

    private MXBase.TouchPanelSensitivityOptions currentTouchPanelSensitivity = MXBase.TouchPanelSensitivityOptions.DO_NOTHING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOeminfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupViews(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        profileProcessor = new MXProfileProcessor(new MXEventListener());
        profileProcessor.connectEMDK(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (profileProcessor != null) {
            profileProcessor.disconnectEMDK();
        }
    }

    private void fetchTouchMode(Context context) {
        profileProcessor.fetchTouchModeInBackground(context, (result, errorInfo) -> {
            if (result != null && errorInfo == null) {
                MXBase.TouchPanelSensitivityOptions option = MXBase.PersistTouchMode.fromValue(result).convert();
                binding.persistTouchMode.setText("Persist Touch Mode: " + option.getXmlValue());
            } else {
                binding.persistTouchMode.setText("Error to fetch Persist Touch Mode");
            }
        });
    }

    private void fetchSerialNumber(Context context, Runnable completion) {
        profileProcessor.fetchSerialNumberInBackground(context, (result, errorInfo) -> {
            if (result != null && errorInfo == null) {
                runOnUiThread(() -> {
                    handleFetchSerialNumberSuccess(result);
                    completion.run();
                });
            } else {
                Log.d(TAG, "Failed to fetch serial number");
                runOnUiThread(() -> {
                    Toast.makeText(context, "Failed to fetch Serial Number", Toast.LENGTH_SHORT).show();
                    completion.run();
                });
            }
        });
    }

    private void fetchIMEI(Context context, Runnable completion) {
        profileProcessor.fetchIMEIInBackground(context, (result, errorInfo) -> {
            if (result != null && errorInfo == null) {
                runOnUiThread(() -> {
                    handleFetchIMEISuccess(result);
                    completion.run();
                });
            } else {
                Log.d(TAG, "Failed to fetch IMEI");
                runOnUiThread(() -> {
                    Toast.makeText(OEMInfoActivity.this, "Failed to fetch IMEI", Toast.LENGTH_SHORT).show();
                    completion.run();
                });
            }
        });
    }

    private void setupViews(Context context) {
        binding.textViewStatus.setText("");
        binding.textViewSerialNumber.setText("");
        binding.textViewIMEI.setText("");

        binding.fetchTouchModeButton.setOnClickListener(v -> fetchTouchMode(context));

        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButton1) {
                    currentTouchPanelSensitivity = MXBase.TouchPanelSensitivityOptions.FINGER_ONLY;
                }
                else if (checkedId == R.id.radioButton2) {
                    currentTouchPanelSensitivity = MXBase.TouchPanelSensitivityOptions.GLOVE_AND_FINGER;
                }
            }
        });

        binding.setTouchModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.textCurrentTouchMode.setText("Current Touch Mode: " + currentTouchPanelSensitivity.getXmlValue());
                profileProcessor.configTouchPanelSensitivity(
                        context,
                        currentTouchPanelSensitivity,
                        (errorInfo) -> {
                            if (errorInfo != null) {
                                Toast.makeText(context, errorInfo.errorDescription, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(context, "Set Touch Mode Success", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }

    private class MXEventListener implements MXBase.EventListener {
        @Override
        public void onEMDKSessionOpened() {
            binding.textViewStatus.setText("EMDK open success.");

            if (DeviceBootTimeHelper.shared().isOEMInfoUpdated()) {
                fetchTouchMode(OEMInfoActivity.this) ;
            } else {
                binding.persistTouchMode.setText("Persist Touch Mode: waiting OEM info update complete");
                DeviceBootTimeHelper.shared().waitOEMInfoUpdateCompletedOneShot(new Runnable() {
                    @Override
                    public void run() {
                        fetchTouchMode(OEMInfoActivity.this);
                    }
                });
            }

            if (DeviceBootTimeHelper.shared().isBootCompleted()) {
                fetchSerialNumber(OEMInfoActivity.this, () -> {
                    fetchIMEI(OEMInfoActivity.this, () -> {});
                });
            } else {
                DeviceBootTimeHelper.shared().waitBootCompletedOneShot(
                        () -> {
                            fetchSerialNumber(OEMInfoActivity.this, () -> {
                                fetchIMEI(OEMInfoActivity.this, () -> {});
                            });
                        }
                );
            }
        }

        @Override
        public void onEMDKSessionClosed() {

        }

        @Override
        public void onEMDKError(MXBase.ErrorInfo errorInfo) {

        }
    }

    public void handleFetchSerialNumberSuccess(String result) {
        String text = "Serial:" + result;
        binding. textViewSerialNumber.setText(text);
        setTitle(text);
        Toast.makeText(OEMInfoActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    public void handleFetchIMEISuccess(String result) {
        String text = "IMEI:" + result;
        binding.textViewIMEI.setText(text);
        Toast.makeText(OEMInfoActivity.this, text, Toast.LENGTH_SHORT).show();
    }
}