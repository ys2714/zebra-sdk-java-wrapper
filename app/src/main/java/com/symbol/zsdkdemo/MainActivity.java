package com.symbol.zsdkdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.symbol.zsdkdemo.databinding.ActivityMainBinding;
import com.zebra.zsdk_java_wrapper.dw.DataWedgeHelper;
import com.zebra.zsdk_java_wrapper.emdk.EMDKHelper;
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
            passedAllCheck();
        } else {
            DeviceBootTimeHelper.shared().waitOEMInfoUpdateCompletedOneShot(
                    () -> {
                        passedAllCheck();
                    }
            );
        }
    }

    private void passedAllCheck() {
        EMDKHelper.getShared().prepare(this, (emdkPrepared) -> {
            if (!emdkPrepared) throw new RuntimeException("prepare EMDK failed !!!");
            DataWedgeHelper.getInstance().prepare(this, (dwPrepared) -> {
                if (!dwPrepared) throw new RuntimeException("prepare DataWedge failed !!!");
                binding.waitingBootText.setText("BOOT COMPLETE");
                binding.buttonDataWedge.setEnabled(true);
                binding.buttonPowerManager.setEnabled(true);
                binding.buttonOEMInfo.setEnabled(true);
            });
        });
    }

    private void setButtons() {
        binding.buttonDataWedge.setEnabled(false);
        binding.buttonDataWedge.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, DataWedgeActivity.class));
        });

        binding.buttonPowerManager.setEnabled(false);
        binding.buttonPowerManager.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, PowerManagerActivity.class));
        });

        binding.buttonOEMInfo.setEnabled(false);
        binding.buttonOEMInfo.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, OEMInfoActivity.class));
        });
    }
}