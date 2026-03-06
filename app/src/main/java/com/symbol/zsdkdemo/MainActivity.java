package com.symbol.zsdkdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.symbol.zsdkdemo.databinding.ActivityMainBinding;
import com.zebra.zsdk_java_wrapper.utils.DeviceBootTimeHelper;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setButtons();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (DeviceBootTimeHelper.shared().isOEMInfoUpdated()) {
            binding.waitingBootText.setText("BOOT COMPLETE");
            binding.buttonPowerManager.setEnabled(true);
            binding.buttonOEMInfo.setEnabled(true);
        } else {
            DeviceBootTimeHelper.shared().waitOEMInfoUpdateCompletedOneShot(
                    () -> {
                        binding.waitingBootText.setText("BOOT COMPLETE");
                        binding.buttonPowerManager.setEnabled(true);
                        binding.buttonOEMInfo.setEnabled(true);
                    }
            );
        }
    }

    private void setButtons() {
        binding.buttonPowerManager.setEnabled(false);
        binding.buttonPowerManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PowerManagerActivity.class));
            }
        });
        binding.buttonOEMInfo.setEnabled(false);
        binding.buttonOEMInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, OEMInfoActivity.class));
            }
        });
    }
}