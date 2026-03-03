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

    public void processProfile(MXBase.ProfileXML profileResId, MXBase.ProfileName profileName, MXBase.ProcessProfileCallback callback) {
        processProfile(profileResId, profileName, null, callback);
    }

    public void processProfile(MXBase.ProfileXML profileResId, MXBase.ProfileName profileName, Map<String, String> params, MXBase.ProcessProfileCallback callback) {
        new ProcessProfileTask(new ProcessProfileTask.Delegate() {
            @Override
            public EMDKResults processProfile() {
                if (profileManager == null) {
                    Log.e(TAG, "EMDK ProfileManager is not available.");
                    throw new RuntimeException("EMDK ProfileManager is not available.");
                }
                String command = XMLReader.readXmlFileToStringWithParams(context, profileResId.getString(), params);
                return profileManager.processProfile(profileName.getString(), ProfileManager.PROFILE_FLAG.SET, new String[]{command});
            }

            @Override
            public void processProfileSuccess() {
                if (callback != null) {
                    callback.onSuccess(profileName.getString());
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

    // OEMInfo

    public void fetchProductModelInBackground(Context ctx, MXBase.FetchOEMInfoCallback callback) {
        fetchOEMInfoInBackground(ctx, MXConst.PRODUCT_MODEL_URI, callback);
    }

    public void fetchSerialNumberInBackground(Context ctx, MXBase.FetchOEMInfoCallback callback) {
        fetchOEMInfoInBackground(ctx, MXConst.SERIAL_URI, new MXBase.FetchOEMInfoCallback() {
            @Override
            public void onSuccess(String result) {
                callback.onSuccess(result);
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });
    }

    public void fetchIMEIInBackground(Context ctx, MXBase.FetchOEMInfoCallback callback) {
        fetchOEMInfoInBackground(ctx, MXConst.IMEI_URI, callback);
    }

    public void fetchTouchModeInBackground(Context ctx, MXBase.FetchOEMInfoCallback callback) {
        fetchOEMInfoInBackground(ctx, MXConst.TOUCH_MODE_URI, callback);
    }

    public void fetchVendorTouchModeInBackground(Context ctx, MXBase.FetchOEMInfoCallback callback) {
        fetchOEMInfoInBackground(ctx, MXConst.VENDOR_TOUCH_MODE_URI, callback);
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

    // AccessManager

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
        map.put(MXConst.ServiceIdentifier, serviceIdentifier);
        map.put(MXConst.CallerPackageName, callerPackageName);
        map.put(MXConst.CallerSignature, callerSignature);
        processProfile(
                MXBase.ProfileXML.AccessManagerAllowCallService,
                MXBase.ProfileName.AccessManagerAllowCallService,
                map,
                callback
        );
    }

    public void callAccessManagerAllowPermission(String permissionName, String appPackageName, String appClassName, String appSignature, MXBase.ProcessProfileCallback callback) {
        Map<String, String> map = new HashMap<>();
        map.put(MXConst.PermissionAccessPermissionName, permissionName);
        map.put(MXConst.PermissionAccessAction, "1"); // 1: allow
        map.put(MXConst.PermissionAccessPackageName, appPackageName);
        map.put(MXConst.ApplicationClassName, appClassName);
        map.put(MXConst.PermissionAccessSignature, appSignature);
        processProfile(
                MXBase.ProfileXML.AccessManagerAllowPermission,
                MXBase.ProfileName.AccessManagerAllowPermission,
                map,
                callback
        );
    }

    // PowerManager

    public void callPowerManagerFeature(MXBase.PowerManagerOptions option) {
        callPowerManagerFeature(option, null);
    }

    public void callPowerManagerFeature(MXBase.PowerManagerOptions option, MXBase.ProcessProfileCallback callback) {
        callPowerManagerFeature(
                option,
                null,
                MXBase.PowerManagerSuppressRebootOptions.DO_NOTHING,
                callback
        );
    }

    public void callPowerManagerFeature(
            MXBase.PowerManagerOptions option,
            String osZipFilePath,
            MXBase.PowerManagerSuppressRebootOptions suppressReboot,
            MXBase.ProcessProfileCallback callback) {
        Map<String, String> map = new HashMap<>();
        switch (option) {
            case SLEEP_MODE:
            case REBOOT:
            case POWER_OFF:
            case OS_CANCEL_ONGOING:
            case ENTERPRISE_RESET:
            case FACTORY_RESET:
            case FULL_DEVICE_WIPE:
                map.put(MXConst.ResetAction, option.valueString());
                processProfile(
                        MXBase.ProfileXML.PowerManagerReset,
                        MXBase.ProfileName.PowerManagerReset,
                        map,
                        callback);
                break;
            case OS_UPDATE:
            case OS_UPGRADE:
            case OS_DOWNGRADE:
                map.put(MXConst.ResetAction, option.valueString());
                map.put(MXConst.ZipFile, osZipFilePath);
                map.put(MXConst.SuppressReboot, suppressReboot.getString());
                processProfile(
                        MXBase.ProfileXML.PowerManagerResetOSUpdate,
                        MXBase.ProfileName.PowerManagerResetOSUpdate,
                        map,
                        callback);
                break;
            case OS_UPGRADE_STREAMING:
            case OS_DOWNGRADE_STREAMING:
                map.put(MXConst.ResetAction, option.valueString());
                map.put(MXConst.ZipFile, osZipFilePath);
                map.put(MXConst.SuppressReboot, suppressReboot.getString());
                processProfile(
                        MXBase.ProfileXML.PowerManagerResetOSStreaming,
                        MXBase.ProfileName.PowerManagerResetOSStreaming,
                        map,
                        callback);
                break;
            case OS_UPDATE_VERIFY:
                map.put(MXConst.ResetAction, option.valueString());
                map.put(MXConst.OsupdateVerifyFile, osZipFilePath);
                processProfile(
                        MXBase.ProfileXML.PowerManagerResetOSVerify,
                        MXBase.ProfileName.PowerManagerResetOSVerify,
                        map,
                        callback);
                break;
            case CREATE_PROFILE:
            case DO_NOTHING:
                break;
        }
    }
}
