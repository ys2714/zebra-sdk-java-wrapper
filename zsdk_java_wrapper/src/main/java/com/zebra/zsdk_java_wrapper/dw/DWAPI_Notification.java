package com.zebra.zsdk_java_wrapper.dw;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zebra.zsdk_java_wrapper.zdm.ZDMConst;
import com.zebra.zsdk_java_wrapper.zdm.ZDMTokenStore;

import java.util.function.Consumer;

/**
 * DataWedge Notification related APIs.
 */
public class DWAPI_Notification {

    private DWAPI_Notification() {}

    /**
     * Registers for DataWedge notifications.
     *
     * @param context The application context.
     * @param notificationType The type of notification to register for.
     * @param callback A callback that receives true if registration was successful.
     */
    public static void registerNotification(Context context, DWAPI.NotificationType notificationType, Consumer<Boolean> callback) {
        String packageName = context.getApplicationContext().getPackageName();
        String token = ZDMTokenStore.getToken(ZDMConst.DelegationScope.SCOPE_DW_NOTIFICATION_API);

        Bundle bundle = new Bundle();
        bundle.putString("com.symbol.datawedge.api.APPLICATION_NAME", packageName);
        bundle.putString("com.symbol.datawedge.api.NOTIFICATION_TYPE", notificationType.getValue());
        bundle.putString("APPLICATION_PACKAGE", packageName);
        bundle.putString("TOKEN", token);

        DWIntentFactory.callDWAPI(context, DWAPI.ActionExtraKeys.REGISTER_FOR_NOTIFICATION, bundle, (intent, ex) -> {
            callback.accept(ex == null);
        });
    }

    /**
     * Unregisters from DataWedge notifications.
     *
     * @param context The application context.
     * @param notificationType The type of notification to unregister from.
     * @param callback A callback that receives true if unregistration was successful.
     */
    public static void unregisterNotification(Context context, DWAPI.NotificationType notificationType, Consumer<Boolean> callback) {
        String packageName = context.getApplicationContext().getPackageName();
        String token = ZDMTokenStore.getToken(ZDMConst.DelegationScope.SCOPE_DW_NOTIFICATION_API);

        Bundle bundle = new Bundle();
        bundle.putString("com.symbol.datawedge.api.APPLICATION_NAME", packageName);
        bundle.putString("com.symbol.datawedge.api.NOTIFICATION_TYPE", notificationType.getValue());
        bundle.putString("APPLICATION_PACKAGE", packageName);
        bundle.putString("TOKEN", token);

        DWIntentFactory.callDWAPI(context, DWAPI.ActionExtraKeys.UNREGISTER_FOR_NOTIFICATION, bundle, (intent, ex) -> {
            callback.accept(ex == null);
        });
    }
}
