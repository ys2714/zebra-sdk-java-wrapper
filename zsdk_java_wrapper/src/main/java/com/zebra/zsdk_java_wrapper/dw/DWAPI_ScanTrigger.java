package com.zebra.zsdk_java_wrapper.dw;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * DataWedge Scan Trigger related APIs.
 */
public class DWAPI_ScanTrigger {

    private DWAPI_ScanTrigger() {}

    /**
     * Controls the soft scan trigger.
     *
     * @param context The application context.
     * @param option The soft scan trigger option (START_SCANNING, STOP_SCANNING, etc.).
     */
    public static void softScanTrigger(Context context, DWAPI.SoftScanTriggerOptions option) {
        DWIntentFactory.callDWAPI(context, DWAPI.ActionExtraKeys.SOFT_SCAN_TRIGGER, option.getValue(), new DWIntentFactory.DWCallback() {
            @Override
            public void onResult(@NonNull Intent intent, @Nullable Exception ex) {
                if (ex == null) {
                    Log.d(DWAPI.TAG, "softScanTrigger success");
                } else {
                    Log.e(DWAPI.TAG, "softScanTrigger failed: " + ex.getMessage());
                }
            }
        });
    }

    /**
     * Sets the DCP (Data Capture Panel) button state.
     *
     * @param context The application context.
     * @param name The profile name.
     * @param enabled true to enable the DCP button, false to disable.
     */
    public static void sendSetDCPButtonIntent(Context context, String name, boolean enabled) {
        // DCP Params
        Bundle dcpParams = new Bundle();
        if (enabled) {
            dcpParams.putString(DWAPI.DCPParams.ENABLED, DWAPI.StringBoolean.TRUE.getValue());
        } else {
            dcpParams.putString(DWAPI.DCPParams.ENABLED, DWAPI.StringBoolean.FALSE.getValue());
        }
        dcpParams.putString(DWAPI.DCPParams.DOCK, DWAPI.DCPParams.DockOptions.BOTH);
        dcpParams.putString(DWAPI.DCPParams.MODE, DWAPI.DCPParams.ModeOptions.BUTTON);
        dcpParams.putString(DWAPI.DCPParams.HIGH_POS, "30");
        dcpParams.putString(DWAPI.DCPParams.LOW_POS, "70");
        dcpParams.putString(DWAPI.DCPParams.DRAG, "501");

        // DCP Plugin Bundle
        Bundle dcpPluginBundle = new Bundle();
        dcpPluginBundle.putString(DWAPI.Plugin.NAME, DWAPI.Plugin.Utilities.DCP.getValue());
        dcpPluginBundle.putString(DWAPI.BundleParams.RESET_CONFIG, DWAPI.StringBoolean.TRUE.getValue());
        dcpPluginBundle.putBundle(DWAPI.BundleParams.PARAM_LIST, dcpParams);

        // Main Config Bundle
        Bundle bundle = new Bundle();
        bundle.putString(DWAPI.Profile.NAME, name);
        bundle.putString(DWAPI.Profile.CONFIG_MODE, DWAPI.ConfigModeOptions.UPDATE.getValue());
        bundle.putString(DWAPI.Profile.ENABLED, DWAPI.StringBoolean.TRUE.getValue());
        if (enabled) {
            bundle.putBundle(DWAPI.Plugin.Utilities.DCP.getValue(), dcpPluginBundle);
        } else {
            bundle.putBundle(DWAPI.Plugin.Utilities.DCP.getValue(), null);
        }

        // Send Broadcast
        Intent intent = new Intent(DWAPI.ActionNames.ACTION.getValue());
        intent.putExtra(DWConst.SET_CONFIG, bundle);
        context.sendOrderedBroadcast(intent, null);
    }

    public static void setDCPButton(Context context, String name, boolean enabled) {
        sendSetDCPButtonIntent(context, name, enabled);
    }
}
