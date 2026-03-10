package com.zebra.zsdk_java_wrapper.dw;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.concurrent.CompletableFuture;

/**
 * DataWedge Profile related APIs.
 */
public class DWAPI_Profile {

    private DWAPI_Profile() {}

    public static CompletableFuture<Boolean> sendSetConfigIntent(Context context, Bundle extra) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        DWIntentFactory.callDWAPI(context, DWAPI.ActionExtraKeys.SET_CONFIG, extra, (intent, ex) -> {
            if (ex == null) {
                future.complete(true);
            } else {
                future.completeExceptionally(ex);
            }
        });
        return future;
    }

    public static CompletableFuture<Bundle> sendGetConfigIntent(Context context, Bundle extra) {
        CompletableFuture<Bundle> future = new CompletableFuture<>();
        DWIntentFactory.callDWAPI(context, DWAPI.ActionExtraKeys.GET_CONFIG, extra, (intent, ex) -> {
            if (ex == null) {
                Bundle bundle = intent.getBundleExtra(DWAPI.ResultExtraKeys.GET_CONFIG.getValue());
                if (bundle != null) {
                    future.complete(bundle);
                } else {
                    future.completeExceptionally(new Exception("No Result"));
                }
            } else {
                future.completeExceptionally(ex);
            }
        });
        return future;
    }

    public static CompletableFuture<Boolean> sendCreateProfileIntent(Context context, String name) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        DWIntentFactory.callDWAPI(context, DWAPI.ActionExtraKeys.CREATE_PROFILE, name, (intent, ex) -> {
            if (ex == null) {
                future.complete(true);
            } else {
                future.completeExceptionally(ex);
            }
        });
        return future;
    }

    public static CompletableFuture<Boolean> sendDeleteProfileIntent(Context context, String name) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        DWIntentFactory.callDWAPI(context, DWAPI.ActionExtraKeys.DELETE_PROFILE, name, (intent, ex) -> {
            if (ex == null) {
                future.complete(true);
            } else {
                if (DWAPI.ResultCodes.PROFILE_NOT_FOUND.getValue().equals(ex.getMessage())) {
                    future.complete(true);
                } else {
                    future.completeExceptionally(ex);
                }
            }
        });
        return future;
    }

    public static CompletableFuture<Boolean> sendSwitchProfileIntent(Context context, String name) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        DWIntentFactory.callDWAPI(context, DWAPI.ActionExtraKeys.SWITCH_TO_PROFILE, name, (intent, ex) -> {
            if (ex == null) {
                future.complete(true);
            } else {
                if (DWAPI.ResultCodes.PROFILE_ALREADY_SET.getValue().equals(ex.getMessage())) {
                    future.complete(true);
                } else {
                    future.completeExceptionally(ex);
                }
            }
        });
        return future;
    }

    public static CompletableFuture<Boolean> sendSwitchScannerParamsIntent(Context context, Bundle bundle) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        DWIntentFactory.callDWAPI(context, DWAPI.ActionExtraKeys.SWITCH_SCANNER_PARAMS, bundle, (intent, ex) -> {
            if (ex == null) {
                future.complete(true);
            } else {
                future.completeExceptionally(ex);
            }
        });
        return future;
    }
}
