package com.zebra.zsdk_java_wrapper.mx;

import android.text.TextUtils;
import androidx.annotation.Keep;

@Keep
public class MXBase {

    private static final String TAG = MXBase.class.getSimpleName();

    public interface EventListener {
        void onEMDKSessionOpened();
        void onEMDKSessionClosed();
        void onEMDKError(ErrorInfo errorInfo);
    }

    public static class ErrorInfo extends Throwable {
        public String errorName = "";
        public String errorType = "";
        public String errorDescription = "";

        public ErrorInfo(String errorType, String errorName, String errorDescription) {
            super(errorDescription);
            this.errorName = errorName;
            this.errorType = errorType;
            this.errorDescription = errorDescription;
        }

        public ErrorInfo(String errorDescription) {
            super(errorDescription);
            this.errorName = "default error name";
            this.errorType = "default error type";
            this.errorDescription = errorDescription;
        }

        public ErrorInfo() {
            super();
        }

        public String buildFailureMessage() {
            if (!TextUtils.isEmpty(errorName) && !TextUtils.isEmpty(errorType)) {
                return errorName + " :" + "\n" + errorType + " :" + "\n" + errorDescription;
            } else if (!TextUtils.isEmpty(errorName)) {
                return errorName + " :" + "\n" + errorDescription;
            } else {
                return errorType + " :" + "\n" + errorDescription;
            }
        }
    }

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

        public String getString() {
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

        TouchPanelSensitivityOptions(int value, String xmlValue) {
            this.value = value;
            this.xmlValue = xmlValue;
        }

        public String getXmlValue() {
            return this.xmlValue;
        }

        public String getString() {
            return String.valueOf(this.value);
        }
    }

    public enum KeyIdentifiers {
        SCAN("SCAN"),
        GRIP_TRIGGER("GRIP_TRIGGER"),
        LEFT_TRIGGER_1("LEFT_TRIGGER_1"),
        RIGHT_TRIGGER_1("RIGHT_TRIGGER_1"),
        REAR_BUTTON("REAR_BUTTON"),
        CENTER_TRIGGER_1("CENTER_TRIGGER_1");

        private final String value;

        KeyIdentifiers(String value) {
            this.value = value;
        }

        public String getString() {
            return value;
        }
    }

    public enum ScreenLockType {
        DO_NOT_CHANGE(0),
        SWIPE(1),
        PATTERN(2),
        PIN(3),
        PASSWORD(4),
        NONE(5);

        private final int value;

        ScreenLockType(int value) {
            this.value = value;
        }

        public String getString() {
            return String.valueOf(value);
        }
    }

    public enum ScreenShotUsage {
        DO_NOTHING(0),
        ENABLE(1),
        DISABLE(2);

        private final int value;

        ScreenShotUsage(int value) {
            this.value = value;
        }

        public String getString() {
            return String.valueOf(value);
        }
    }

    /**
     * https://techdocs.zebra.com/mx/keymappingmgr/#key-code
     *
     * */
    public enum KeyCodes {
        VOLUMEUP(24),
        VOLUMEDOWN(25),
        SCAN(10036),
        LEFT_TRIGGER_1(102),
        RIGHT_TRIGGER_1(103),
        LEFT_TRIGGER_2(104),
        RIGHT_TRIGGER_2(105),
        HEADSET_HOOK(79),
        BACK(4),
        HOME(3),
        MENU(82),
        POWER(26),
        DO_NOT_DISTURB(10043), //WS50
        CHANNEL_SWITCH(10044), //FR55
        DURESS(10045); //emergency call button

        private final int value;

        KeyCodes(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public String getString() {
            return String.valueOf(value);
        }
    }
    
    public enum OSUpdateStatus {
        PASSED, FAILED, CANCELLED, IN_PROGRESS, IN_SUSPEND, WAITING_FOR_REBOOT
    }

    public interface OSUpdateStatusCallback {
        void onResult(OSUpdateStatus status, String detail, String timestamp);
    }

    public enum ShowHideState {
        DO_NOT_CHANGE(0), SHOW(1), HIDE(2);

        private final int value;

        ShowHideState(int value) {
            this.value = value;
        }

        public String getString() {
            return String.valueOf(value);
        }
    }

    public enum PowerManagerRecoveryModeAccessOptions {
        ENABLE(1),
        DISABLE(2);

        private final int value;

        PowerManagerRecoveryModeAccessOptions(int value) {
            this.value = value;
        }

        public String getString() {
            return String.valueOf(value);
        }
    }

    public enum UsbClientModeDefaultOptions {
        DO_NOT_CHANGE(0),
        CHARGING_ONLY(1),
        FILE_TRANSFER(2),
        PTP(3),
        USB_TETHERING(4),
        USB_STORAGE(5);

        private final int value;

        UsbClientModeDefaultOptions(int value) {
            this.value = value;
        }

        public String getString() {
            return String.valueOf(value);
        }
    }
}
