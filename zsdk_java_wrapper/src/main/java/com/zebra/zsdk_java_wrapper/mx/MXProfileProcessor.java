package com.zebra.zsdk_java_wrapper.mx;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.EMDKManager.EMDKListener;
import com.symbol.emdk.EMDKResults;
import com.symbol.emdk.ProfileManager;
import com.zebra.zsdk_java_wrapper.oeminfo.OEMInfoHelper;
import com.zebra.zsdk_java_wrapper.utils.PackageManagerHelper;

import java.util.HashMap;
import java.util.Map;

public class MXProfileProcessor {

    private static final String TAG = MXProfileProcessor.class.getSimpleName();

    private final EMDKEventHandler eventHandler = new EMDKEventHandler();
    private final MXBase.EventListener listener;

    private ProfileManager profileManager = null;
    private EMDKManager emdkManager = null;
    private Context context = null;

    public MXProfileProcessor(MXBase.EventListener listener) {
        this.listener = listener;
    }

    public void connectEMDK(Context ctx) {
        this.context = ctx;
        EMDKResults results = EMDKManager.getEMDKManager(ctx.getApplicationContext(), this.eventHandler);
        if (results.statusCode != EMDKResults.STATUS_CODE.SUCCESS) {
            Log.e(TAG, "EMDKManager object creation failed.");
            MXBase.ErrorInfo errorInfo = new MXBase.ErrorInfo();
            errorInfo.errorType = "EMDKManager";
            errorInfo.errorDescription = "EMDKManager object creation failed";
            this.listener.onEMDKError(errorInfo);
        } else {
            Log.d(TAG, "EMDKManager object creation success.");
        }
    }

    public void disconnectEMDK() {
        if (profileManager != null) {
            profileManager = null;
        }
        if (emdkManager != null) {
            emdkManager.release();
            emdkManager = null;
        }
    }

    public void fetchSerialNumberInBackground(Context ctx, MXBase.FetchOEMInfoCallback callback) {
        fetchOEMInfoInBackground(ctx, MXConst.SERIAL_URI, callback);
    }

    public void fetchIMEIInBackground(Context ctx, MXBase.FetchOEMInfoCallback callback) {
        fetchOEMInfoInBackground(ctx, MXConst.IMEI_URI, callback);
    }

    public void fetchOEMInfoInBackground(Context ctx, String serviceId, MXBase.FetchOEMInfoCallback callback) {
        new Thread(() -> {
            String serial = OEMInfoHelper.getOEMInfo(ctx, serviceId);
            if (serial == null) {
                getCallServicePermission(ctx, serviceId, new MXBase.ProcessProfileCallback() {
                    @Override
                    public void onSuccess(String profileName) {
                        fetchOEMInfoInBackground(ctx, serviceId, callback);
                    }

                    @Override
                    public void onError(MXBase.ErrorInfo errorInfo) {
                        callback.onError();
                    }
                });
            } else {
                new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(serial));
            }
        }).start();
    }

    public void getAllDangerousPermissions(Context ctx, MXBase.ProcessProfileCallback callback) {
        String base64 = PackageManagerHelper.getPackageSignature(ctx);
        String name = ctx.getPackageName();
        callAccessManagerAllowPermission(
                MXBase.EPermissionType.ALL_DANGEROUS_PERMISSIONS.toString(),
                name,
                "",
                base64,
                callback
        );
    }

    public void getCallServicePermission(Context ctx, String serviceId, MXBase.ProcessProfileCallback callback) {
        String base64 = PackageManagerHelper.getPackageSignature(ctx);
        String name = ctx.getPackageName();
        callAccessManagerAllowCallService(serviceId, name, base64, callback);
    }

    public void callAccessManagerAllowCallService(String serviceIdentifier, String callerPackageName, String callerSignature, MXBase.ProcessProfileCallback callback) {
        Map<String, String> map = new HashMap<>();
        map.put(MXConst.serviceIdentifier, serviceIdentifier);
        map.put(MXConst.callerPackageName, callerPackageName);
        map.put(MXConst.callerSignature, callerSignature);
        processProfile(MXConst.AccessManagerAllowCallServiceXML, MXConst.AccessManagerAllowCallService, map, callback);
    }

    public void callAccessManagerAllowPermission(String permissionName, String appPackageName, String appClassName, String appSignature, MXBase.ProcessProfileCallback callback) {
        Map<String, String> map = new HashMap<>();
        map.put(MXConst.permissionAccessPermissionName, permissionName);
        map.put(MXConst.permissionAccessAction, "1"); // 1: allow
        map.put(MXConst.permissionAccessPackageName, appPackageName);
        map.put(MXConst.applicationClassName, appClassName);
        map.put(MXConst.permissionAccessSignature, appSignature);
        processProfile(MXConst.AccessManagerAllowPermissionXML, MXConst.AccessManagerAllowPermission, map, callback);
    }

    public void callAppManagerInstallAndStart(String apkPath, String packageName, String mainActivity, MXBase.ProcessProfileCallback callback) {
        Map<String, String> map = new HashMap<>();
        map.put(MXConst.apkFilePath, apkPath);
        map.put(MXConst.appPackageName, packageName);
        map.put(MXConst.mainActivityClass, mainActivity);
        processProfile(MXConst.AppManagerInstallAndStartXML, MXConst.AppManagerInstallAndStart, map, callback);
    }

    public void callPowerManagerFeature(MXBase.PowerManagerOptions option) {
        callPowerManagerFeature(option, null);
    }

    public void callPowerManagerFeature(MXBase.PowerManagerOptions option, MXBase.ProcessProfileCallback callback) {
        callPowerManagerFeature(option, null, callback);
    }

    public void callPowerManagerFeature(MXBase.PowerManagerOptions option, String osZipFilePath, MXBase.ProcessProfileCallback callback) {
        Map<String, String> map = new HashMap<>();
        switch (option) {
            case SLEEP_MODE:
            case REBOOT:
            case ENTERPRISE_RESET:
            case FACTORY_RESET:
            case FULL_DEVICE_WIPE:
                map.put(MXConst.resetAction, option.valueString());
                processProfile(MXConst.PowerManagerResetXML, MXConst.PowerManagerReset, map, callback);
                break;
            case OS_UPDATE:
                map.put(MXConst.resetAction, option.valueString());
                map.put(MXConst.zipFile, osZipFilePath);
                processProfile(MXConst.PowerManagerResetXML, MXConst.PowerManagerReset, map, callback);
                break;
        case CREATE_PROFILE:
        case DO_NOTHING:
            break;
        }
    }

    public void processProfile(String profileResId, String profileName, MXBase.ProcessProfileCallback callback) {
        processProfile(profileResId, profileName, null, callback);
    }

    public void processProfile(String profileResId, String profileName, Map<String, String> params, MXBase.ProcessProfileCallback callback) {
        new ProcessProfileTask(new ProcessProfileTask.Delegate() {
            @Override
            public EMDKResults processProfile() {
                if (profileManager == null) {
                    Log.e(TAG, "EMDK ProfileManager is not available.");
                    throw new RuntimeException("EMDK ProfileManager is not available.");
                }
                String command = XMLReader.readXmlFileToStringWithParams(context, profileResId, params);
                return profileManager.processProfile(profileName, ProfileManager.PROFILE_FLAG.SET, new String[]{command});
            }

            @Override
            public void processProfileSuccess() {
                if (callback != null) {
                    callback.onSuccess(profileName);
                }
            }

            @Override
            public void processProfileFailure(MXBase.ErrorInfo errorInfo) {
                if (callback != null) {
                    callback.onError(errorInfo);
                }
            }
        }).execute();
    }

    private class EMDKEventHandler implements EMDKListener {
        @Override
        public void onOpened(EMDKManager emdkManager) {
            MXProfileProcessor.this.emdkManager = emdkManager;
            profileManager = (ProfileManager) emdkManager.getInstance(EMDKManager.FEATURE_TYPE.PROFILE);
            listener.onEMDKSessionOpened();
        }

        @Override
        public void onClosed() {
            if (emdkManager != null) {
                emdkManager.release();
                emdkManager = null;
            }
            listener.onEMDKSessionClosed();
        }
    }
}
