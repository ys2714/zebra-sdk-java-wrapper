package com.zebra.zsdk_java_wrapper.mx;

import androidx.annotation.Keep;

@Keep
public final class MXConst {

    private static final String TAG = MXConst.class.getSimpleName();

    // Private constructor to prevent instantiation
    private MXConst() {}

    // Content Provider Keys
    private static final String IMEI = "imei";
    private static final String BUILD_SERIAL = "build_serial";

    public static final String OSUPDATE_STATUS_URI = "content://oem_info/oem.zebra.osupdate/status";
    public static final String OSUPDATE_DETAIL_URI = "content://oem_info/oem.zebra.osupdate/detail";
    public static final String OSUPDATE_TIMESTAMP_URI = "content://oem_info/oem.zebra.osupdate/ts";
    public static final String PRODUCT_MODEL_URI = "content://oem_info/oem.zebra.secure/ro_product_model";
    public static final String SERIAL_URI = "content://oem_info/oem.zebra.secure/build_serial";
    public static final String IMEI_URI = "content://oem_info/wan/imei";
    public static final String TOUCH_MODE_URI = "content://oem_info/oem.zebra.software/persist.sys.touch_mode";
    public static final String VENDOR_TOUCH_MODE_URI = "content://oem_info/oem.zebra.software/persist.vendor.sys.touch_mode";

    // DW Access Control
    public static final String AUTHORITY_URI = "content://com.zebra.devicemanager.zdmcontentprovider";

    // Profile Parameters
    public static final String ResetAction = "ResetAction";
    public static final String RecoveryModeAccess = "RecoveryModeAccess";
    public static final String UsbClientModeDefault = "UsbClientModeDefault";
    public static final String TouchPanelSensitivity = "TouchPanelSensitivity";
    public static final String TouchActionAny = "TouchActionAny";
    public static final String ZipFile = "ZipFile";
    public static final String RemoteZipFile = "RemoteZipFile";
    public static final String OsupdateVerifyFile = "OsupdateVerifyFile";
    public static final String SuppressReboot = "SuppressReboot";
    public static final String ServiceIdentifier = "ServiceIdentifier";
    public static final String CallerPackageName = "CallerPackageName";
    public static final String CallerSignature = "CallerSignature";
    public static final String APK = "APK";
    public static final String Package = "Package";
    public static final String Class = "Class";
    public static final String AutoTime = "AutoTime";
    public static final String NTPServer = "NTPServer";
    public static final String SyncInterval = "SyncInterval";
    public static final String TimeZone = "TimeZone";
    public static final String Date = "Date";
    public static final String Time = "Time";
    public static final String MilitaryTime = "MilitaryTime"; // 0: do not change
    public static final String militaryTimeON = "1"; // 1: turn ON
    public static final String militaryTimeOFF = "2"; // 2: turn OFF
    public static final String DevAdminPkg = "DevAdminPkg";
    public static final String DevAdminClass = "DevAdminClass";
    public static final String ScreenLockType = "ScreenLockType";
    public static final String ScreenShotUsage = "ScreenShotUsage";
    public static final String PowerOffState = "PowerOffState";
    public static final String AirPlaneMode = "AirPlaneMode";
    public static final String SafeMode = "SafeMode";
    public static final String TouchPanel = "TouchPanel";
    public static final String TouchPanelMode = "TouchPanelMode";
    public static final String AutoScreenLockOption = "AutoScreenLockOption";
    public static final String AutoScreenLockState = "AutoScreenLockState";
    public static final String KeyIdentifier = "KeyIdentifier";
    public static final String BaseIntentAction = "BaseIntentAction";
    public static final String BaseIntentCategory = "BaseIntentCategory";
    public static final String BaseIntentStringExtraName = "BaseIntentStringExtraName";
    public static final String BaseIntentStringExtraValue = "BaseIntentStringExtraValue";
    public static final String TargetPathAndFileName = "TargetPathAndFileName";
    public static final String ConfigurationFile = "ConfigurationFile";
    public static final String EXTRA_KEY_IDENTIFIER = "EXTRA_KEY_IDENTIFIER";

    // Ignored Value
    public static final String ignoredValue = "ignoredValue";

    /**
     * Defines the action to be taken for a permission request.
     * - <b>0: Do Nothing</b> - No change; prior settings are retained. (MX 10.0+, Android API 26+)
     * - <b>1: Allow</b> - Grants the permission to the app. (MX 10.0+, Android API 26+)
     * - <b>2: Deny</b> - Denies the permission to the app. (MX 10.0+, Android API 26+)
     * - <b>3: Allow User to choose</b> - Prompts the user to grant or deny the permission. This is the Android default. (MX 10.0+, Android API 26+)
     * - <b>4: Verify</b> - Verifies whether the permission is granted.
     */
    public static final String PermissionAccessAction = "PermissionAccessAction";
    public static final String PermissionAccessPackageName = "PermissionAccessPackageName";
    public static final String ApplicationClassName = "ApplicationClassName";
    public static final String PermissionAccessPermissionName = "PermissionAccessPermissionName";
    public static final String PermissionAccessSignature = "PermissionAccessSignature";
}