package com.zebra.zsdk_java_wrapper.mx;

import android.icu.text.SelectFormat;
import android.text.TextUtils;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

@Keep
public class MXBase {

    private static final String TAG = MXBase.class.getSimpleName();

//    public interface FetchOEMInfoCallback {
//        void onSuccess(String result);
//        void onError();
//    }
//
//    public interface ProcessProfileCallback {
//        void onSuccess(String profileName);
//        void onError(ErrorInfo errorInfo);
//    }

    public interface EventListener {
        void onEMDKSessionOpened();
        void onEMDKSessionClosed();
        void onEMDKError(ErrorInfo errorInfo);
    }

    public static class ErrorInfo extends Throwable {
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

        public ErrorInfo(String errorDescription) {
            this.errorName = "default error name";
            this.errorType = "default error type";
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
        OS_UPDATE(8),
        OS_UPDATE_VERIFY(9),
        OS_UPGRADE(10),
        OS_DOWNGRADE(11),
        OS_UPGRADE_STREAMING(12),
        OS_DOWNGRADE_STREAMING(13),
        OS_CANCEL_ONGOING(14),
        POWER_OFF(15);

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

    public enum ProfileXML {
        None("None"),
        AccessManagerAllowPermission("profile_access_manager_allow_permission.xml"),
        AccessManagerAllowCallService("profile_access_manager_allow_call_service.xml"),
        AppManagerInstallAndStart("profile_app_manager_install_and_start.xml"),
        PowerManagerReset("profile_power_manager_reset.xml"),
        PowerManagerResetOSUpdate("profile_power_manager_reset_os_update.xml"),
        PowerManagerResetOSVerify("profile_power_manager_reset_os_verify.xml"),
        PowerManagerResetOSStreaming("profile_power_manager_reset_os_streaming.xml"),
        PowerManagerRecoveryModeAccess("profile_power_manager_recovery_mode_access.xml"),
        ClockSet("profile_clock_set.xml"),
        ClockResetAuto("profile_clock_reset_auto.xml"),
        DevAdminManagerDisableLockScreen("profile_dev_admin_manager_disable_lock_screen.xml"),
        DisplayManagerDisableScreenShot("profile_display_manager_disable_screenshot.xml"),
        PowerKeyManagerSetPowerOffState("profile_powerkey_manager_set_poweroff_state.xml"),
        KeymappingManagerSetKeySendIntent("profile_keymapping_manager_set_key_send_intent.xml"),
        KeymappingManagerSetAllToDefault("profile_keymapping_manager_set_all_to_default.xml"),
        DataWedgeManagerImportProfile("profile_datawedge_manager_import_profile.xml"),
        UsbClientModeDefault("profile_usb_manager_client_mode_default.xml"),
        TouchPanelSensitivity("profile_touch_panel_sensitivity.xml"),
        FileManagerCopyEmbeddedFreeFormOCR("profile_file_manager_copy_embedded_free_form_ocr.xml");

        private final String value;

        ProfileXML(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Deprecated
        @Override
        public String toString() {
            return value;
        }

        public String getString() {
            return value;
        }
    }

    public enum ProfileName {
        AccessManagerAllowPermission("AccessManagerAllowPermission"),
        AccessManagerAllowCallService("AccessManagerAllowCallService"),
        AppManagerInstallAndStart("AppManagerInstallAndStart"),
        PowerManagerReset("PowerManagerReset"),
        PowerManagerResetOSUpdate("PowerManagerResetOSUpdate"),
        PowerManagerResetOSVerify("PowerManagerResetOSVerify"),
        PowerManagerResetOSStreaming("PowerManagerResetOSStreaming"),
        PowerManagerRecoveryModeAccess("PowerManagerRecoveryModeAccess"),
        ClockSet("ClockSet"),
        ClockResetAuto("ClockResetAuto"),
        DevAdminManagerDisableLockScreen("DevAdminManagerDisableLockScreen"),
        DisplayManagerDisableScreenShot("DisplayManagerDisableScreenShot"),
        PowerKeyManagerSetPowerOffState("PowerKeyManagerSetPowerOffState"),
        KeymappingManagerSetKeySendIntent("KeymappingManagerSetKeySendIntent"),
        KeymappingManagerSetAllToDefault("KeymappingManagerSetAllToDefault"),
        DataWedgeManagerImportProfile("DataWedgeManagerImportProfile"),
        UsbClientModeDefault("UsbClientModeDefault"),
        TouchPanelSensitivity("TouchPanelSensitivity"),
        FileManagerCopyEmbeddedFreeFormOCR("FileManagerCopyEmbeddedFreeFormOCR");

        private final String value;

        ProfileName(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Deprecated
        @Override
        public String toString() {
            return value;
        }

        public String getString() {
            return value;
        }
    }

    public enum PowerManagerSuppressRebootOptions {

        DO_NOTHING(0),
        TRUE(1),
        FALSE(2);

        private final int value;

        PowerManagerSuppressRebootOptions(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        /**
         * This method is deprecated and should not be used.
         * Calling it will result in a RuntimeException.
         * Use getString() instead.
         */
        @Deprecated
        @Override
        public String toString() {
            throw new RuntimeException("Not Implemented. Please use getString() instead.");
        }

        /**
         * Returns the string representation of the enum's integer value.
         * @return The integer value as a String.
         */
        public String getString() {
            return String.valueOf(this.value);
        }
    }

    public enum PersistTouchMode {
        STYLUS_AND_FINGER("stylus_and_finger"),
        GLOVE_AND_FINGER("glove_and_finger"),
        FINGER_ONLY("finger"),
        STYLUS_GLOVE_AND_FINGER("stylus_and_glove_and_finger");

        private final String value;

        PersistTouchMode(String value) {
            this.value = value;
        }

        public static PersistTouchMode fromValue(String value) {
            if (STYLUS_AND_FINGER.value.equals(value)) {
                return STYLUS_AND_FINGER;
            }
            else if (GLOVE_AND_FINGER.value.equals(value)) {
                return GLOVE_AND_FINGER;
            }
            else if (FINGER_ONLY.value.equals(value)) {
                return FINGER_ONLY;
            }
            else if (STYLUS_GLOVE_AND_FINGER.value.equals(value)) {
                return STYLUS_GLOVE_AND_FINGER;
            }
            else {
                throw new RuntimeException("PersistTouchMode invalid value: " + value);
            }
        }

        public String getValue() {
            return this.value;
        }

        public TouchPanelSensitivityOptions convert() {
            switch (this) {
                case STYLUS_AND_FINGER:
                    return TouchPanelSensitivityOptions.STYLUS_AND_FINGER;
                case GLOVE_AND_FINGER:
                    return TouchPanelSensitivityOptions.GLOVE_AND_FINGER;
                case FINGER_ONLY:
                    return TouchPanelSensitivityOptions.FINGER_ONLY;
                case STYLUS_GLOVE_AND_FINGER:
                    return TouchPanelSensitivityOptions.STYLUS_GLOVE_AND_FINGER;
            }
            throw new RuntimeException("PersistTouchMode covert failed: " + this.value);
        }
    }

    public enum TouchPanelSensitivityOptions {

        DO_NOTHING(0, "Do not change"),
        STYLUS_AND_FINGER(1, "Stylus and Finger"),
        GLOVE_AND_FINGER(2, "Glove and Finger"),
        FINGER_ONLY(3, "Finger"),
        STYLUS_GLOVE_AND_FINGER(4, "Stylus and Glove and Finger");

        private final int value;
        private final String xmlValue;

        /**
         * Constructor for the enum constants.
         * @param value The integer representation of the option.
         * @param xmlValue The string value used for XML configuration.
         */
        TouchPanelSensitivityOptions(int value, String xmlValue) {
            this.value = value;
            this.xmlValue = xmlValue;
        }

        /**
         * Gets the integer representation of the option.
         * @return The integer value.
         */
        public int getValue() {
            return this.value;
        }

        /**
         * Gets the string value used for XML configuration.
         * @return The XML value string.
         */
        public String getXmlValue() {
            return this.xmlValue;
        }

        /**
         * Gets the integer value of the option as a String.
         * This is the equivalent of the Kotlin 'string' property.
         * @return The integer value converted to a String.
         */
        public String getString() {
            return String.valueOf(this.value);
        }

        /**
         * This method is intentionally broken to match the Kotlin implementation.
         * Developers should use getString() or getXmlValue() instead.
         *
         * @deprecated please use .getString() instead. This method will be removed in a future version.
         */
        @Override
        @Deprecated
        public String toString() {
            throw new RuntimeException("Not Implemented. Please use getString() or getXmlValue().");
        }
    }
}
