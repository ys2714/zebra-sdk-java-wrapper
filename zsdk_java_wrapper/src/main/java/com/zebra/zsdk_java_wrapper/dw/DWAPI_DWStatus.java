package com.zebra.zsdk_java_wrapper.dw;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.concurrent.CompletableFuture;

/**
 * DataWedge Status related APIs.
 */
public class DWAPI_DWStatus {

    private DWAPI_DWStatus() {}

    /**
     * Enables or disables the DataWedge service.
     *
     * @param context The application context.
     * @param enabled true to enable DataWedge, false to disable it.
     * @return A CompletableFuture that completes with true if the operation was successful.
     */
    public static CompletableFuture<Boolean> enableDW(Context context, boolean enabled) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        DWIntentFactory.callDWAPI(context, DWAPI.ActionExtraKeys.ENABLE_DATAWEDGE, enabled, new DWIntentFactory.DWCallback() {
            @Override
            public void onResult(@NonNull Intent intent, @Nullable Exception ex) {
                if (ex == null) {
                    future.complete(true);
                } else {
                    String message = ex.getMessage();
                    if (enabled && DWAPI.ResultCodes.DATAWEDGE_ALREADY_ENABLED.getValue().equals(message)) {
                        future.complete(true);
                    } else if (!enabled && DWAPI.ResultCodes.DATAWEDGE_ALREADY_DISABLED.getValue().equals(message)) {
                        future.complete(true);
                    } else {
                        future.completeExceptionally(ex);
                    }
                }
            }
        });
        return future;
    }

    /**
     * Retrieves the status of the DataWedge service.
     *
     * @param context The application context.
     * @return A CompletableFuture that completes with true if DataWedge is enabled, false otherwise.
     */
    public static CompletableFuture<Boolean> sendGetDWStatusIntent(Context context) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        DWIntentFactory.callDWAPI(context, DWAPI.ActionExtraKeys.GET_DATAWEDGE_STATUS, "", new DWIntentFactory.DWCallback() {
            @Override
            public void onResult(@NonNull Intent intent, @Nullable Exception ex) {
                if (ex == null) {
                    if (DWAPI.ResultActionNames.RESULT_ACTION.getValue().equals(intent.getAction()) &&
                            intent.hasExtra(DWAPI.ResultExtraKeys.GET_DATAWEDGE_STATUS.getValue())) {
                        String result = intent.getStringExtra(DWAPI.ResultExtraKeys.GET_DATAWEDGE_STATUS.getValue());
                        boolean enabled = DWAPI.StringEnabled.ENABLED.getValue().equalsIgnoreCase(result);
                        Log.d(DataWedgeHelper.TAG, "CHECK DW STATUS PROFILE RESULT: " + enabled);
                        future.complete(enabled);
                    } else {
                        Log.e(DataWedgeHelper.TAG, "CHECK DW STATUS PROFILE FAILED: no dw status in intent");
                        future.completeExceptionally(new Exception(DWAPI.ResultCodes.ERROR_NO_DATAWEDGE_STATUS_IN_RESULT.getValue()));
                    }
                } else {
                    future.completeExceptionally(ex);
                }
            }
        });
        return future;
    }
}
