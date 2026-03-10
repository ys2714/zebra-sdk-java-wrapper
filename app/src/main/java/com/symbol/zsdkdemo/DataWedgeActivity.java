package com.symbol.zsdkdemo;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;
import com.symbol.zsdkdemo.databinding.ActivityDataWedgeBinding;
import com.zebra.zsdk_java_wrapper.mx.MXBase;

public class DataWedgeActivity extends AppCompatActivity {

    private ActivityDataWedgeBinding binding;
    private DataWedgeViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDataWedgeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new DataWedgeViewModel();
        viewModel.handleOnCreate(this);

        setupButtons();
        setupText();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.handleOnResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewModel.handleOnPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.handleOnDestroy(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event == null) return super.onKeyDown(keyCode, event);
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == MXBase.KeyCodes.SCAN.getValue()) {
                Log.d("", "LEFT Scan PRESSED");
                viewModel.startScanning();
            }
            else if (keyCode == MXBase.KeyCodes.RIGHT_TRIGGER_1.getValue()) {
                Log.d("", "RIGHT Scan PRESSED");
                viewModel.startScanning();
            }
            else if (keyCode == MXBase.KeyCodes.LEFT_TRIGGER_1.getValue()) {
                Log.d("", "PTT PRESSED");
                finish();
            }
            else if (keyCode == MXBase.KeyCodes.RIGHT_TRIGGER_2.getValue()) {
                Log.d("", "R2 PRESSED (if exist)");
            }
        } else if (event.getAction() == KeyEvent.ACTION_UP) {
            if (keyCode == MXBase.KeyCodes.SCAN.getValue()) {
                Log.d("", "LEFT Scan RELEASED");
                viewModel.stopScanning();
            }
            else if (keyCode == MXBase.KeyCodes.RIGHT_TRIGGER_1.getValue()) {
                Log.d("", "RIGHT Scan RELEASED");
                viewModel.stopScanning();
            }
            else if (keyCode == MXBase.KeyCodes.LEFT_TRIGGER_1.getValue()) {
                Log.d("", "PTT RELEASED");
                finish();
            }
            else if (keyCode == MXBase.KeyCodes.RIGHT_TRIGGER_2.getValue()) {
                Log.d("", "R2 RELEASED (if exist)");
            }
        } else {

        }

        return super.onKeyDown(keyCode, event);
    }

    private void setupText() {
        viewModel.barcodeText.observe(this, s -> {
            binding.scanDataText.setText(s);
        });
    }

    private void setupButtons() {
        binding.scanButton.setOnClickListener((v)-> {
            viewModel.startScanning();
        });
    }
}