package com.symbol.zsdkdemo;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zebra.zsdk_java_wrapper.dw.DWBarcodeScanner;
import com.zebra.zsdk_java_wrapper.dw.DataWedgeHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DataWedgeViewModel extends ViewModel {

    public static final String barcodePluginName = "BARCODE";
    public static final String workflowPluginName = "WORKFLOW";

    MutableLiveData<String> currentInputPluginName = new MutableLiveData<>("BARCODE");

    final MutableLiveData<String> barcodeText = new MutableLiveData<>("");
    final MutableLiveData<String> ocrText = new MutableLiveData<>("");

    MutableLiveData<String> scannerStatus = new MutableLiveData<>("");
    MutableLiveData<String> sessionStatus = new MutableLiveData<>("");

    private DWBarcodeScanner scanner = null;

    public void handleOnCreate(Context context) {
        scanner = new DWBarcodeScanner(context);
    }

    public void handleOnResume(Context context) {
        if (scanner == null) return;
        scanner.open(s -> {
            scanner.select((instance) -> {
                instance.startListen((data) -> {
                    if (Objects.equals(currentInputPluginName.getValue(), workflowPluginName)) {
                        synchronized (ocrText) {
                            ocrText.setValue(data);
                        }
                    } else {
                        synchronized (barcodeText) {
                            barcodeText.setValue(data);
                            barcodeText.notify();
                        }
                    }
                });
                instance.startListenStatus((type, status) -> {
                    sessionStatus.setValue(status);
                });
                instance.resume();
                getScannerStatus(context);
            });
        });
    }

    public void handleOnPause(Context context) {
        if (scanner == null) return;
        scanner.stopListen();
        scanner.stopListenStatus();
        scanner.suspend();
    }

    public void handleOnDestroy(Context context) {
        handleOnPause(context);
    }

    public void startScanning() {
        if (scanner == null) return;
        scanner.startScan();
    }

    public void stopScanning() {
        if (scanner == null) return;
        scanner.stopScan();
    }

    public void getScannerStatus(Context context) {
        DataWedgeHelper.getInstance().getScannerStatus(context, 1, (status) -> {
            scannerStatus.setValue(status.toString());
        });
    }

    public void switchToBarcodePlugin(Context context) {
        if (scanner == null) return;
        Map<String, String> params = new HashMap<>();
        params.put("PROFILE_NAME", scanner.getProfileName());
        params.put("scanner_input_enabled", "true");
        params.put("workflow_input_enabled", "false");
        params.put("barcode_trigger_mode", "1");
        params.put("aim_type", "8");
        params.put("aim_timer", "6000");
        params.put("beam_timer", "6000");

        scanner.update(params, instance -> {
            instance.select(selectedInstance -> {
                currentInputPluginName.setValue(barcodePluginName);
            });
        });
    }

    public void switchToOCRPlugin(Context context) {
        if (scanner == null) return;
        Map<String, String> params = new HashMap<>();
        params.put("PROFILE_NAME", scanner.getProfileName());
        params.put("scanner_input_enabled", "false");
        params.put("workflow_input_enabled", "true");
        params.put("barcode_trigger_mode", "1");
        params.put("aim_type", "8");
        params.put("aim_timer", "6000");
        params.put("beam_timer", "6000");

        scanner.update(params, instance -> {
            instance.select(selectedInstance -> {
                currentInputPluginName.setValue(workflowPluginName);
            });
        });
    }
}
