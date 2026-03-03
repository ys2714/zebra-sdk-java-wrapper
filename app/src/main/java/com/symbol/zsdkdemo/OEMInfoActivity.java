package com.symbol.zsdkdemo;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.symbol.zsdkdemo.databinding.ActivityOeminfoBinding;
import com.zebra.zsdk_java_wrapper.mx.MXBase;
import com.zebra.zsdk_java_wrapper.mx.MXProfileProcessor;

public class OEMInfoActivity extends AppCompatActivity {

    private static final String TAG = PowerManagerActivity.class.getSimpleName();

    private ActivityOeminfoBinding binding;
    private MXProfileProcessor profileProcessor;

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

    private void setupViews(Context context) {
        binding.textViewStatus.setText("");
        binding.textViewSerialNumber.setText("");
        binding.textViewIMEI.setText("");
        binding.fetchButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fetch SN
                profileProcessor.fetchSerialNumberInBackground(context, new MXBase.FetchOEMInfoCallback() {
                    @Override
                    public void onSuccess(String result) {
                        binding.textViewSerialNumber.setText(result);
                        // Fetch IMEI
                        profileProcessor.fetchIMEIInBackground(context, new MXBase.FetchOEMInfoCallback() {

                            @Override
                            public void onSuccess(String result) {
                                binding.textViewIMEI.setText(result);
                            }

                            @Override
                            public void onError() {
                                binding.textViewIMEI.setText("Error to fetch IMEI");
                            }
                        });
                    }

                    @Override
                    public void onError() {
                        binding.textViewSerialNumber.setText("Error to fetch SN");
                    }
                });
            }
        });

        binding.fetchButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // first try
                profileProcessor.fetchTouchModeInBackground(context, new MXBase.FetchOEMInfoCallback() {

                    @Override
                    public void onSuccess(String result) {
                        if (!result.isEmpty()) {
                            binding.textTouchMode.setText(result);
                        } else {
                            // second try
                            profileProcessor.fetchVendorTouchModeInBackground(context, new MXBase.FetchOEMInfoCallback() {

                                @Override
                                public void onSuccess(String result) {
                                    binding.textTouchMode.setText(result);
                                }

                                @Override
                                public void onError() {
                                    binding.textTouchMode.setText("Error to fetch Touch Mode");
                                }
                            });

                        }
                    }

                    @Override
                    public void onError() {
                        binding.textTouchMode.setText("Error to fetch Touch Mode");
                    }
                });
            }
        });
    }

    private class MXEventListener implements MXBase.EventListener {
        @Override
        public void onEMDKSessionOpened() {
            binding.textViewStatus.setText("EMDK open success.");

//            profileProcessor.fetchSerialNumberInBackground(OEMInfoActivity.this, new MXBase.FetchOEMInfoCallback() {
//                @Override
//                public void onSuccess(String result) {
//                    runOnUiThread(() -> handleFetchSerialNumberSuccess(result));
//
//                    profileProcessor.fetchIMEIInBackground(OEMInfoActivity.this, new MXBase.FetchOEMInfoCallback() {
//                        @Override
//                        public void onSuccess(String result) {
//                            runOnUiThread(() -> handleFetchIMEISuccess(result));
//                        }
//
//                        @Override
//                        public void onError() {
//                            Log.d(TAG, "Failed to fetch IMEI");
//                            runOnUiThread(() -> Toast.makeText(OEMInfoActivity.this, "Failed to fetch IMEI", Toast.LENGTH_SHORT).show());
//                        }
//                    });
//                }
//
//                @Override
//                public void onError() {
//                    Log.d(TAG, "Failed to fetch serial number");
//                    runOnUiThread(() -> Toast.makeText(OEMInfoActivity.this, "Failed to fetch Serial Number", Toast.LENGTH_SHORT).show());
//                }
//            });
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