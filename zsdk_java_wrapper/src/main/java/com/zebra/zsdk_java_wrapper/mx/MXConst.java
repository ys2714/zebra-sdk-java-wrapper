package com.zebra.zsdk_java_wrapper.mx;

public class MXConst {
    // Content Provider Keys
    private static final String IMEI = "imei";
    private static final String BUILD_SERIAL = "build_serial";

    public static final String SERIAL_URI = "content://oem_info/oem.zebra.secure/build_serial";
    public static final String IMEI_URI = "content://oem_info/wan/imei";

    public static String AccessManagerAllowPermissionXML = "profile_access_manager_allow_permission.xml";
    public static String AccessManagerAllowCallServiceXML = "profile_access_manager_allow_call_service.xml";
    public static String AppManagerInstallAndStartXML = "profile_app_manager_install_and_start.xml";
    public static String PowerManagerResetXML = "profile_power_manager_reset.xml";

    public static String AccessManagerAllowPermission = "AccessManagerAllowPermission";
    public static String AccessManagerAllowCallService = "AccessManagerAllowCallService";
    public static String AppManagerInstallAndStart = "AppManagerInstallAndStart";
    public static String PowerManagerReset = "PowerManagerReset";

    public static String resetAction = "resetAction";
    public static String zipFile = "zipFile";

    public static String serviceIdentifier = "serviceIdentifier";
    public static String callerPackageName = "callerPackageName";
    public static String callerSignature = "callerSignature";

    /*
    0	Do Nothing	This value (or the absence of this parm from the XML) causes no change; any prior settings are retained.
        MX: 10.0+
        Android API: 26+

    1	Allow	Grants permission to an app.
        MX: 10.0+
        Android API: 26+

    2	Deny	Denies permission to an app.
        MX: 10.0+
        Android API: 26+

    3	Allow User to choose	Prompts device user to grant or deny permission to an app for access to a device resource (such as a camera or scanner). This is the Android-default setting.
        MX: 10.0+
        Android API: 26+

    4	Verify	Verifies whether permission is granted to an app.
     */
    public static String permissionAccessAction = "permissionAccessAction";
    public static String permissionAccessPackageName = "permissionAccessPackageName";
    public static String applicationClassName = "applicationClassName";
    public static String permissionAccessPermissionName = "permissionAccessPermissionName";
    public static String permissionAccessSignature = "permissionAccessSignature";
}
