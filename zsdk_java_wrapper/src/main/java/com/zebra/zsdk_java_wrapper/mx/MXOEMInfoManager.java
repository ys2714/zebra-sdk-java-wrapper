package com.zebra.zsdk_java_wrapper.mx;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import com.zebra.zsdk_java_wrapper.oeminfo.OEMInfoHelper;

import java.util.function.BiConsumer;

/**
 * https://techdocs.zebra.com/oeminfo/consume/
 * */
public class MXOEMInfoManager {

    // Handler for posting results to the main/UI thread
    private static final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    public static void fetchProductModelInBackground(Context ctx, BiConsumer<String, MXBase.ErrorInfo> callback) {
        fetchOEMInfoInBackground(ctx, MXConst.PRODUCT_MODEL_URI, callback);
    }

    public static void fetchSerialNumberInBackground(Context ctx, BiConsumer<String, MXBase.ErrorInfo> callback) {
        fetchOEMInfoInBackground(ctx, MXConst.SERIAL_URI, callback);
    }

    public static void fetchIMEIInBackground(Context ctx, BiConsumer<String, MXBase.ErrorInfo> callback) {
        fetchOEMInfoInBackground(ctx, MXConst.IMEI_URI, callback);
    }

    public static void fetchTouchModeInBackground(Context ctx, BiConsumer<String, MXBase.ErrorInfo> callback) {
        if (Build.DEVICE.equals("TC26")) {
            fetchOEMInfoInBackground(ctx, MXConst.VENDOR_TOUCH_MODE_URI, callback);
        } else {
            fetchOEMInfoInBackground(ctx, MXConst.TOUCH_MODE_URI, callback);
        }
    }

    public static void addOEMInfoChangeObserver(Context context, String serviceId, ContentObserver observer) {
        OEMInfoHelper.startObserveOEMInfoChange(context, serviceId, observer);
    }

    public static void removeOEMInfoChangeObserver(Context context, ContentObserver observer) {
        OEMInfoHelper.stopObserveOEMInfoChange(context, observer);
    }

    public static void fetchOEMInfoInBackground(Context context,
                                         String serviceId,
                                         BiConsumer<String, MXBase.ErrorInfo> callback) {
        new Thread(() -> {
            String serial = OEMInfoHelper.getOEMInfo(context, serviceId);
            if (serial == null) {
                MXAccessManager.getCallServicePermission(
                        context,
                        serviceId,
                        0,
                        (errorInfo) -> {
                            if (errorInfo == null) {
                                fetchOEMInfoInBackground(context, serviceId, callback);
                            } else {
                                callback.accept(null, errorInfo);
                            }
                        });
            } else {
                mainThreadHandler.post(
                        () -> {
                            callback.accept(serial, null);
                        }
                );
            }
        }).start();
    }

    /**
     * https://techdocs.zebra.com/oeminfo/consume/
     *
     * status: PASSED, FAILED, CANCELLED, IN_PROGRESS, IN_SUSPEND, WAITING_FOR_REBOOT
     * detail: Verbose text describing the status
     * ts: Returns an epoch timestamp indicating when the intent was received
     * */
    public static void fetchOSUpdateStatusInBackground(
            Context context,
            MXBase.OSUpdateStatusCallback callback) {

        new Thread(() -> {
            String statusStr = OEMInfoHelper.getOEMInfo(context, MXConst.OSUPDATE_STATUS_URI);
            String detail = OEMInfoHelper.getOEMInfo(context, MXConst.OSUPDATE_DETAIL_URI);
            String timestamp = OEMInfoHelper.getOEMInfo(context, MXConst.OSUPDATE_TIMESTAMP_URI);

            mainThreadHandler.post(() -> {
                MXBase.OSUpdateStatus status = null;
                if (statusStr != null) {
                    try {
                        status = MXBase.OSUpdateStatus.valueOf(statusStr);
                    } catch (IllegalArgumentException e) {
                        // Handle potential naming mismatches if necessary
                    }
                }
                callback.onResult(status, detail, timestamp);
            });
        }).start();
    }
}
