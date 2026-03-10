package com.zebra.zsdk_java_wrapper.mx;

import android.content.Context;

import com.zebra.zsdk_java_wrapper.utils.PackageManagerHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * https://techdocs.zebra.com/mx/accessmgr/
 **/
class MXAccessManager {

    static void getAllDangerousPermissions(Context context,
                                           long delaySeconds,
                                           Consumer<MXBase.ErrorInfo> callback) {
        String base64 = PackageManagerHelper.getPackageSignature(context);
        String name = context.getPackageName();
        callAccessManagerAllowPermission(
                context,
                MXBase.EPermissionType.ALL_DANGEROUS_PERMISSIONS.toString(),
                name,
                "",
                base64,
                delaySeconds,
                callback
        );
    }

    static void getCallServicePermission(Context context,
                                         String serviceId,
                                         long delaySeconds,
                                         Consumer<MXBase.ErrorInfo> callback) {
        String base64 = PackageManagerHelper.getPackageSignature(context);
        String name = context.getPackageName();
        callAccessManagerAllowCallService(context, serviceId, name, base64, delaySeconds, callback);
    }

    static void callAccessManagerAllowCallService(Context context,
                                                  String serviceIdentifier,
                                                  String callerPackageName,
                                                  String callerSignature,
                                                  long delaySeconds,
                                                  Consumer<MXBase.ErrorInfo> callback) {
        Map<String, String> map = new HashMap<>();
        map.put(MXConst.ServiceIdentifier, serviceIdentifier);
        map.put(MXConst.CallerPackageName, callerPackageName);
        map.put(MXConst.CallerSignature, callerSignature);
        MXProfileProcessor.processProfileWithCallback(
                context,
                MXBase.ProfileXML.AccessManagerAllowCallService,
                MXBase.ProfileName.AccessManagerAllowCallService,
                map,
                delaySeconds,
                callback
        );
    }

    static void callAccessManagerAllowPermission(Context context,
                                                 String permissionName,
                                                 String appPackageName,
                                                 String appClassName,
                                                 String appSignature,
                                                 long delaySeconds,
                                                 Consumer<MXBase.ErrorInfo> callback) {
        Map<String, String> map = new HashMap<>();
        map.put(MXConst.PermissionAccessPermissionName, permissionName);
        map.put(MXConst.PermissionAccessAction, "1"); // 1: allow
        map.put(MXConst.PermissionAccessPackageName, appPackageName);
        map.put(MXConst.ApplicationClassName, appClassName);
        map.put(MXConst.PermissionAccessSignature, appSignature);
        MXProfileProcessor.processProfileWithCallback(
                context,
                MXBase.ProfileXML.AccessManagerAllowPermission,
                MXBase.ProfileName.AccessManagerAllowPermission,
                map,
                delaySeconds,
                callback
        );
    }
}
