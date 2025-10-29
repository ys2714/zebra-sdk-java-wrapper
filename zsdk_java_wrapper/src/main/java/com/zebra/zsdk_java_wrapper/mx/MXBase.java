package com.zebra.zsdk_java_wrapper.mx;

import android.text.TextUtils;

public class MXBase {

    public interface FetchOEMInfoCallback {
        void onSuccess(String result);
        void onError();
    }

    public interface ProcessProfileCallback {
        void onSuccess(String profileName);
        void onError(ErrorInfo errorInfo);
    }

    public interface EventListener {
        void onEMDKSessionOpened();
        void onEMDKSessionClosed();
        void onEMDKError(ErrorInfo errorInfo);
    }

    public static class ErrorInfo {
        // Contains the parm-error name (sub-feature that has error)
        public String errorName = "";
        // Contains the characteristic-error type (Root feature that has error)
        public String errorType = "";
        // contains the error description for parm or characteristic error.
        public String errorDescription = "";

        public String buildFailureMessage() {
            String failureMessage = "";
            if (!TextUtils.isEmpty(errorName) && !TextUtils.isEmpty(errorType))
                failureMessage = errorName + " :" + "\n" + errorType + " :" + "\n"
                        + errorDescription;
            else if (!TextUtils.isEmpty(errorName))
                failureMessage = errorName + " :" + "\n" + errorDescription;
            else
                failureMessage = errorType + " :" + "\n" + errorDescription;
            return failureMessage;
        }
    }

    // Initial Value of the Power Manager options to be executed in the
    // onOpened() method when the EMDK is ready. Default Value set in the wizard
    // is 0.
    // 0 -> Do Nothing
    // 1 -> Sleep Mode
    // 4 -> Reboot
    // 5 -> Enterprise Reset
    // 6 -> Factory Reset
    // 7 -> Full Device Wipe
    // 8 -> OS Update

    public enum PowerManagerOptions {
        CREATE_PROFILE(-1),
        DO_NOTHING(0),
        SLEEP_MODE(1),
        REBOOT(4),
        ENTERPRISE_RESET(5),
        FACTORY_RESET(6),
        FULL_DEVICE_WIPE(7),
        OS_UPDATE(8);

        final int value;

        PowerManagerOptions(int value) {
            this.value = value;
        }

        String valueString() {
            return String.valueOf(value);
        }
    }

    public enum EPermissionType
    {
        ACCESS_NOTIFICATIONS(           "android.permission.ACCESS_NOTIFICATIONS"           ),
        PACKAGE_USAGE_STATS(            "android.permission.PACKAGE_USAGE_STATS"            ),
        SYSTEM_ALERT_WINDOW(            "android.permission.SYSTEM_ALERT_WINDOW"            ),
        GET_APP_OPS_STATS(              "android.permission.GET_APP_OPS_STATS"              ),
        BATTERY_STATS(                  "android.permission.BATTERY_STATS"                  ),
        MANAGE_EXTERNAL_STORAGE(        "android.permission.MANAGE_EXTERNAL_STORAGE"        ),
        BIND_NOTIFICATION_LISTENER(     "android.permission.BIND_NOTIFICATION_LISTENER"     ),
        READ_LOGS(                      "android.permission.READ_LOGS"                      ),
        ALL_DANGEROUS_PERMISSIONS(      "ALL_DANGEROUS_PERMISSIONS"                         ),
        ACCESS_RX_LOGGER(               "com.zebra.permission.ACCESS_RXLOGGER"              ),
        SCHEDULE_EXACT_ALARM(           "android.permission.SCHEDULE_EXACT_ALARM"           ),
        WRITE_SETTINGS(                 "android.permission.WRITE_SETTINGS"                 ),
        ACCESSIBILITY_SERVICE(          "ACCESSIBILITY_SERVICE_ACCESS"                      );

        String stringContent = "";
        EPermissionType(String stringContent)
        {
            this.stringContent = stringContent;
        }

        @Override
        public String toString() {
            return stringContent;
        }

        public static EPermissionType fromString(String permissionType)
        {
            switch(permissionType)
            {
                case "android.permission.ACCESS_NOTIFICATIONS":
                    return ACCESS_NOTIFICATIONS;
                case "android.permission.PACKAGE_USAGE_STATS":
                    return PACKAGE_USAGE_STATS;
                case "android.permission.SYSTEM_ALERT_WINDOW":
                    return SYSTEM_ALERT_WINDOW;
                case "android.permission.GET_APP_OPS_STATS":
                    return GET_APP_OPS_STATS;
                case "android.permission.BATTERY_STATS":
                    return BATTERY_STATS;
                case "android.permission.MANAGE_EXTERNAL_STORAGE":
                    return MANAGE_EXTERNAL_STORAGE;
                case "android.permission.BIND_NOTIFICATION_LISTENER":
                    return BIND_NOTIFICATION_LISTENER;
                case "android.permission.READ_LOGS":
                    return READ_LOGS;
                case "ALL_DANGEROUS_PERMISSIONS":
                    return ALL_DANGEROUS_PERMISSIONS;
                case "com.zebra.permission.ACCESS_RXLOGGER":
                    return ACCESS_RX_LOGGER;
                case "android.permission.SCHEDULE_EXACT_ALARM":
                    return SCHEDULE_EXACT_ALARM;
                case "android.permission.WRITE_SETTINGS":
                    return WRITE_SETTINGS;
                case "ACCESSIBILITY_SERVICE_ACCESS":
                    return ACCESSIBILITY_SERVICE;
                default:
                    return null;
            }
        }
    }
}
