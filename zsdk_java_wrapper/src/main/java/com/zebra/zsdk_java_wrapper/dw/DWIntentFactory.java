package com.zebra.zsdk_java_wrapper.dw;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.zebra.zsdk_java_wrapper.utils.JsonUtils;

import java.util.Random;
import java.util.function.Consumer;

class DWIntentFactory {

    public static final String TAG = "DWIntentFactory";

    public interface DWCallback {
        void onResult(@NonNull Intent intent, @Nullable Exception exception);
    }

    public static void callDWAPI(@NonNull Context context, 
                                @NonNull DWAPI.ActionExtraKeys extraKey, 
                                @NonNull Object extraValue, 
                                @NonNull DWCallback callback) {
        
        final Context ctx = context.getApplicationContext();
        final String commandIdentifier = extraKey.getValue() + "_" + new Random().nextInt(100000000);

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent == null) {
                    Log.e(TAG, "intent is null");
                    cleanupAndFinish(ctx, this, null, new Exception(DWAPI.ResultCodes.ERROR_INTENT_NULL.getValue()), callback);
                    return;
                }

                String receivedCommandIdentifier = intent.getStringExtra(DWConst.COMMAND_IDENTIFIER);
                if (receivedCommandIdentifier != null && !receivedCommandIdentifier.equals(commandIdentifier)) {
                    Log.w(TAG, "SKIP. intent command didn't match " + commandIdentifier);
                    return;
                }

                if (!DWAPI.ResultActionNames.RESULT_ACTION.getValue().equals(intent.getAction())) {
                    Log.e(TAG, "intent action is not " + DWAPI.ResultActionNames.RESULT_ACTION.getValue());
                    cleanupAndFinish(ctx, this, null, new Exception(DWAPI.ResultCodes.ERROR_INTENT_ACTION_NOT_MATCH.getValue()), callback);
                    return;
                }

                Bundle infoBundle = intent.getBundleExtra(DWAPI.Result.RESULT_INFO);
                if (infoBundle != null) {
                    String code = infoBundle.getString(DWAPI.Result.RESULT_CODE);
                    if (code == null) {
                        cleanupAndFinish(ctx, this, intent, null, callback);
                    } else {
                        if (DWAPI.ResultCodes.PLUGIN_BUNDLE_INVALID.getValue().equals(code) ||
                            DWAPI.ResultCodes.PLUGIN_NOT_SUPPORTED.getValue().equals(code) ||
                            DWAPI.ResultCodes.PLUGIN_DISABLED_IN_CONFIG.getValue().equals(code) ||
                            DWAPI.ResultCodes.PARAMETER_INVALID.getValue().equals(code)) {
                            
                            String dump = "";
                            if (extraValue instanceof Bundle) {
                                dump = JsonUtils.bundleToJson((Bundle) extraValue);
                            }
                            cleanupAndFinish(ctx, this, null, new Exception(code + " " + receivedCommandIdentifier + " " + dump), callback);
                        } else {
                            cleanupAndFinish(ctx, this, null, new Exception(code), callback);
                        }
                    }
                    return;
                } else {
                    Log.d(TAG, "no RESULT_INFO in intent");
                }

                cleanupAndFinish(ctx, this, intent, null, callback);
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(DWAPI.ResultActionNames.RESULT_ACTION.getValue());
        filter.addCategory(DWAPI.ResultCategoryNames.CATEGORY_DEFAULT.getValue());

        ContextCompat.registerReceiver(ctx, receiver, filter, ContextCompat.RECEIVER_EXPORTED);

        Intent apiIntent = new Intent();
        apiIntent.setAction(DWAPI.ActionNames.ACTION.getValue());
        
        if (extraValue instanceof String) {
            apiIntent.putExtra(extraKey.getValue(), (String) extraValue);
        } else if (extraValue instanceof Boolean) {
            apiIntent.putExtra(extraKey.getValue(), (Boolean) extraValue);
        } else if (extraValue instanceof Integer) {
            apiIntent.putExtra(extraKey.getValue(), (Integer) extraValue);
        } else if (extraValue instanceof Bundle) {
            apiIntent.putExtra(extraKey.getValue(), (Bundle) extraValue);
        } else if (extraValue instanceof Parcelable) {
            apiIntent.putExtra(extraKey.getValue(), (Parcelable) extraValue);
        } else if (extraValue instanceof Parcelable[]) {
            apiIntent.putExtra(extraKey.getValue(), (Parcelable[]) extraValue);
        }

        apiIntent.putExtra(DWConst.SEND_RESULT, DWAPI.SendResultOptions.LAST_RESULT.getValue());
        apiIntent.putExtra(DWConst.COMMAND_IDENTIFIER, commandIdentifier);

        ctx.sendOrderedBroadcast(apiIntent, null);
    }

    private static void cleanupAndFinish(Context ctx, BroadcastReceiver receiver, Intent intent, Exception ex, DWCallback callback) {
        ctx.unregisterReceiver(receiver);
        if (ex != null) {
            callback.onResult(new Intent(), ex);
        } else {
            callback.onResult(intent, null);
        }
    }
}
