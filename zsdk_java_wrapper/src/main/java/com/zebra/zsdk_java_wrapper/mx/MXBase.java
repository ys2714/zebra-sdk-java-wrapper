package com.zebra.zsdk_java_wrapper.mx;

import android.text.TextUtils;

import com.symbol.emdk.EMDKResults;

import java.util.HashMap;
import java.util.Map;

public class MXBase {

    private static final String TAG = MXBase.class.getSimpleName();

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
        private static final String TAG = ErrorInfo.class.getSimpleName();
        // Contains the parm-error name (sub-feature that has error)
        public String errorName = "";
        // Contains the characteristic-error type (Root feature that has error)
        public String errorType = "";
        // contains the error description for parm or characteristic error.
        public String errorDescription = "";

        public ErrorInfo(String errorType, String errorName, String errorDescription) {
            this.errorName = errorName;
            this.errorType = errorType;
            this.errorDescription = errorDescription;
        }

        public ErrorInfo() {}

        public String buildFailureMessage() {
            String failureMessage;
            if (!TextUtils.isEmpty(errorName) && !TextUtils.isEmpty(errorType)) {
                failureMessage = errorName + " :" + "\n" + errorType + " :" + "\n"
                        + errorDescription;
            } else if (!TextUtils.isEmpty(errorName)) {
                failureMessage = errorName + " :" + "\n" + errorDescription;
            } else {
                failureMessage = errorType + " :" + "\n" + errorDescription;
            }
            return failureMessage;
        }
    }

    /**
     * Specifies the Power Manager action to be performed. The values correspond
     * to options available in the MX Power Manager profile.
     * <ul>
     * <li>{@code CREATE_PROFILE} (-1): Creates the profile without taking action.</li>
     * <li>{@code DO_NOTHING} (0): No power management action is taken.</li>
     * <li>{@code SLEEP_MODE} (1): Puts the device into sleep mode.</li>
     * <li>{@code REBOOT} (4): Reboots the device.</li>
     * <li>{@code ENTERPRISE_RESET} (5): Performs an enterprise reset.</li>
     * <li>{@code FACTORY_RESET} (6): Performs a factory reset.</li>
     * <li>{@code FULL_DEVICE_WIPE} (7): Performs a full device wipe.</li>
     * <li>{@code OS_UPDATE} (8): Initiates an OS update.</li>
     * </ul>
     */
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

    public enum EPermissionType {
        ACCESS_NOTIFICATIONS("android.permission.ACCESS_NOTIFICATIONS"),
        PACKAGE_USAGE_STATS("android.permission.PACKAGE_USAGE_STATS"),
        SYSTEM_ALERT_WINDOW("android.permission.SYSTEM_ALERT_WINDOW"),
        GET_APP_OPS_STATS("android.permission.GET_APP_OPS_STATS"),
        BATTERY_STATS("android.permission.BATTERY_STATS"),
        MANAGE_EXTERNAL_STORAGE("android.permission.MANAGE_EXTERNAL_STORAGE"),
        BIND_NOTIFICATION_LISTENER("android.permission.BIND_NOTIFICATION_LISTENER"),
        READ_LOGS("android.permission.READ_LOGS"),
        ALL_DANGEROUS_PERMISSIONS("ALL_DANGEROUS_PERMISSIONS"),
        ACCESS_RX_LOGGER("com.zebra.permission.ACCESS_RXLOGGER"),
        SCHEDULE_EXACT_ALARM("android.permission.SCHEDULE_EXACT_ALARM"),
        WRITE_SETTINGS("android.permission.WRITE_SETTINGS"),
        ACCESSIBILITY_SERVICE("ACCESSIBILITY_SERVICE_ACCESS");

        private final String stringContent;

        EPermissionType(String stringContent) {
            this.stringContent = stringContent;
        }

        @Override
        public String toString() {
            return stringContent;
        }

        private static final Map<String, EPermissionType> lookup = new HashMap<>();

        static {
            for (EPermissionType p : EPermissionType.values()) {
                lookup.put(p.toString(), p);
            }
        }

        public static EPermissionType fromString(String permissionType) {
            return lookup.get(permissionType);
        }
    }
}
