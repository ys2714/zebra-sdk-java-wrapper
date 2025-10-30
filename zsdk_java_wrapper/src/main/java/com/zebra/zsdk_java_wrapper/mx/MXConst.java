package com.zebra.zsdk_java_wrapper.mx;

public class MXConst {
    private static final String TAG = MXConst.class.getSimpleName();
    // Content Provider Keys
    private static final String IMEI = "imei";
    private static final String BUILD_SERIAL = "build_serial";

    public static final String SERIAL_URI = "content://oem_info/oem.zebra.secure/build_serial";
    public static final String IMEI_URI = "content://oem_info/wan/imei";

    // Profile XML file names in assets
    public static final String AccessManagerAllowPermissionXML = "profile_access_manager_allow_permission.xml";
    public static final String AccessManagerAllowCallServiceXML = "profile_access_manager_allow_call_service.xml";
    public static final String AppManagerInstallAndStartXML = "profile_app_manager_install_and_start.xml";
    public static final String PowerManagerResetXML = "profile_power_manager_reset.xml";

    // Profile Names
    public static final String AccessManagerAllowPermission = "AccessManagerAllowPermission";
    public static final String AccessManagerAllowCallService = "AccessManagerAllowCallService";
    public static final String AppManagerInstallAndStart = "AppManagerInstallAndStart";
    public static final String PowerManagerReset = "PowerManagerReset";

    // Profile Parameters
    public static final String resetAction = "resetAction";
    public static final String zipFile = "zipFile";

    public static final String serviceIdentifier = "serviceIdentifier";
    public static final String callerPackageName = "callerPackageName";
    public static final String callerSignature = "callerSignature";
    public static final String apkFilePath = "apkFilePath";
    public static final String appPackageName = "appPackageName";
    public static final String mainActivityClass = "mainActivityClass";
    /**
     * Defines the action to be taken for a permission request.
     * <ul>
     * <li><b>0: Do Nothing</b> - No change; prior settings are retained. (MX 10.0+, Android API 26+)</li>
     * <li><b>1: Allow</b> - Grants the permission to the app. (MX 10.0+, Android API 26+)</li>
     * <li><b>2: Deny</b> - Denies the permission to the app. (MX 10.0+, Android API 26+)</li>
     * <li><b>3: Allow User to choose</b> - Prompts the user to grant or deny the permission. This is the Android default. (MX 10.0+, Android API 26+)</li>
     * <li><b>4: Verify</b> - Verifies whether the permission is granted.</li>
     * </ul>
     */
    public static final String permissionAccessAction = "permissionAccessAction";
    public static final String permissionAccessPackageName = "permissionAccessPackageName";
    public static final String applicationClassName = "applicationClassName";
    public static final String permissionAccessPermissionName = "permissionAccessPermissionName";
    public static final String permissionAccessSignature = "permissionAccessSignature";
}
