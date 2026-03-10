package com.zebra.zsdk_java_wrapper.dw;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * DataWedge Scanner related APIs.
 */
public class DWAPI_Scanner {

    private DWAPI_Scanner() {}

    public static CompletableFuture<List<DWScannerMap.DWScannerInfo>> sendEnumerateScannersIntent(Context context) {
        CompletableFuture<List<DWScannerMap.DWScannerInfo>> future = new CompletableFuture<>();
        DWIntentFactory.callDWAPI(context, DWAPI.ActionExtraKeys.ENUMERATE_SCANNERS, "", (intent, ex) -> {
            if (ex == null) {
                handleEnumerateScannersResult(intent, future::complete);
            } else {
                future.completeExceptionally(ex);
            }
        });
        return future;
    }

    public static CompletableFuture<DWAPI.ScannerStatus> sendGetSelectedScannerStatusIntent(Context context) {
        CompletableFuture<DWAPI.ScannerStatus> future = new CompletableFuture<>();
        DWIntentFactory.callDWAPI(context, DWAPI.ActionExtraKeys.GET_SCANNER_STATUS, "", (intent, ex) -> {
            if (ex == null) {
                handleGetSelectedScannerStatusResult(intent, status -> {
                    if (status != null) {
                        future.complete(status);
                    } else {
                        future.completeExceptionally(new Exception("Scanner status is null"));
                    }
                });
            } else {
                future.completeExceptionally(ex);
            }
        });
        return future;
    }

    public static CompletableFuture<Boolean> sendSwitchDataCaptureIntent(Context context, DWAPI.Plugin.Input plugin) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        DWIntentFactory.callDWAPI(context, DWAPI.ActionExtraKeys.SWITCH_DATACAPTURE, plugin.getValue(), (intent, ex) -> {
            if (ex == null) {
                future.complete(true);
            } else {
                future.completeExceptionally(ex);
            }
        });
        return future;
    }

    public static CompletableFuture<Boolean> sendControlScannerInputPluginIntent(Context context, DWAPI.ControlScannerInputPluginCommand command) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        DWIntentFactory.callDWAPI(context, DWAPI.ActionExtraKeys.SCANNER_INPUT_PLUGIN, command.getValue(), (intent, ex) -> {
            if (ex == null) {
                future.complete(true);
            } else {
                String message = ex.getMessage();
                if ("SCANNER_ALREADY_SUSPENDED".equals(message) ||
                        "SCANNER_ALREADY_RESUMED".equals(message) ||
                        "SCANNER_ALREADY_DISABLED".equals(message) ||
                        "SCANNER_ALREADY_ENABLED".equals(message)) {
                    future.complete(true);
                } else {
                    future.completeExceptionally(ex);
                }
            }
        });
        return future;
    }

    private static void handleEnumerateScannersResult(Intent intent, Consumer<List<DWScannerMap.DWScannerInfo>> callback) {
        if (DWAPI.ResultActionNames.RESULT_ACTION.getValue().equals(intent.getAction()) &&
                intent.hasExtra(DWAPI.ResultExtraKeys.ENUMERATE_SCANNERS.getValue())) {

            List<Bundle> bundles;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundles = intent.getParcelableArrayListExtra(DWAPI.ResultExtraKeys.ENUMERATE_SCANNERS.getValue(), Bundle.class);
            } else {
                bundles = intent.getParcelableArrayListExtra(DWAPI.ResultExtraKeys.ENUMERATE_SCANNERS.getValue());
            }

            if (bundles != null) {
                for (Bundle bundle : bundles) {
                    String name = bundle.getString("SCANNER_NAME");
                    boolean connected = bundle.getBoolean("SCANNER_CONNECTION_STATE");
                    int index = bundle.getInt("SCANNER_INDEX");
                    String id = bundle.getString("SCANNER_IDENTIFIER");
                    if (id != null && name != null) {
                        DWScannerMap.setScannerInfo(id, name, index, connected);
                        Log.d(DataWedgeHelper.TAG, "ScannerInfo: [name: " + name + ", connected: " + connected + ", index: " + index + ", id: " + id + "]");
                    }
                }
            }
            Log.d(DataWedgeHelper.TAG, "LIST SCANNER SUCCESS");
            callback.accept(DWScannerMap.getScannerList());
        } else {
            Log.e(DataWedgeHelper.TAG, "LIST SCANNER FAILED: no enumerate scanners in intent");
            callback.accept(Collections.emptyList());
        }
    }

    private static void handleGetSelectedScannerStatusResult(Intent intent, Consumer<DWAPI.ScannerStatus> callback) {
        if (DWAPI.ResultActionNames.RESULT_ACTION.getValue().equals(intent.getAction()) &&
                intent.hasExtra(DWAPI.ResultExtraKeys.SCANNER_STATUS.getValue())) {

            String statusStr = intent.getStringExtra(DWAPI.ResultExtraKeys.SCANNER_STATUS.getValue());
            DWAPI.ScannerStatus statusEnum = null;
            for (DWAPI.ScannerStatus s : DWAPI.ScannerStatus.values()) {
                if (s.getValue().equals(statusStr)) {
                    statusEnum = s;
                    break;
                }
            }
            callback.accept(statusEnum);
        } else {
            Log.e(DataWedgeHelper.TAG, "GET SCANNER STATUS FAILED: no scanner status in intent");
            callback.accept(null);
        }
    }
}
