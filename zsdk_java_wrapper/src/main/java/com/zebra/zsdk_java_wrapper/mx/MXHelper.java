package com.zebra.zsdk_java_wrapper.mx;

import android.content.Context;
import android.os.Build;

import androidx.annotation.Keep;
import androidx.annotation.RequiresApi;

import com.zebra.zsdk_java_wrapper.utils.DeviceInfoUtils;
import com.zebra.zsdk_java_wrapper.zdm.ZDMConst;

import java.util.function.Consumer;

@Keep
public class MXHelper {

    @Keep
    public static void whiteListApproveApp(Context context, long delaySeconds, Consumer<Boolean> callback) {
        MXAccessManager.getCallServicePermission(
                context,
                ZDMConst.DelegationScope.SCOPE_DW_CONFIG_API.getValue(),
                delaySeconds,
                (error) -> callback.accept(error == null)
        );
    }

    @Keep
    public static void whiteListApproveApp(Context context, Consumer<Boolean> callback) {
        whiteListApproveApp(context, 0, callback);
    }

    @Keep
    public static void setDeviceToSleep(Context context, long delaySeconds) {
        MXPowerManager.callPowerManagerFeature(
                context,
                MXBase.PowerManagerOptions.SLEEP_MODE,
                null,
                MXBase.PowerManagerSuppressRebootOptions.DO_NOTHING,
                delaySeconds,
                error -> {}
        );
    }

    @Keep
    public static void setDeviceToSleep(Context context) {
        setDeviceToSleep(context, 0);
    }

    @Keep
    public static void setDeviceToReboot(Context context, long delaySeconds) {
        MXPowerManager.callPowerManagerFeature(
                context,
                MXBase.PowerManagerOptions.REBOOT,
                null,
                MXBase.PowerManagerSuppressRebootOptions.DO_NOTHING,
                delaySeconds,
                error -> {}
        );
    }

    @Keep
    public static void setDeviceToReboot(Context context) {
        setDeviceToReboot(context, 0);
    }

    @Keep
    public static void upgradeOS(Context context, String zipFilePath, boolean suppressReboot, long delaySeconds) {
        MXBase.PowerManagerSuppressRebootOptions rebootOption = suppressReboot ?
                MXBase.PowerManagerSuppressRebootOptions.TRUE : MXBase.PowerManagerSuppressRebootOptions.FALSE;
        MXPowerManager.callPowerManagerFeature(
                context,
                MXBase.PowerManagerOptions.OS_UPGRADE,
                zipFilePath,
                rebootOption,
                delaySeconds,
                error -> {}
        );
    }

    @Keep
    public static void upgradeOS(Context context, String zipFilePath, boolean suppressReboot) {
        upgradeOS(context, zipFilePath, suppressReboot, 0);
    }

    @Keep
    public static void downgradeOS(Context context, String zipFilePath, long delaySeconds) {
        MXPowerManager.callPowerManagerFeature(
                context,
                MXBase.PowerManagerOptions.OS_DOWNGRADE,
                zipFilePath,
                MXBase.PowerManagerSuppressRebootOptions.DO_NOTHING,
                delaySeconds,
                error -> {}
        );
    }

    @Keep
    public static void downgradeOS(Context context, String zipFilePath) {
        downgradeOS(context, zipFilePath, 0);
    }

    @Keep
    public static void streamUpgradeOS(Context context, String zipFilePath, boolean suppressReboot, long delaySeconds) {
        MXBase.PowerManagerSuppressRebootOptions rebootOption = suppressReboot ?
                MXBase.PowerManagerSuppressRebootOptions.TRUE : MXBase.PowerManagerSuppressRebootOptions.FALSE;
        MXPowerManager.callPowerManagerFeature(
                context,
                MXBase.PowerManagerOptions.OS_UPGRADE_STREAMING,
                zipFilePath,
                rebootOption,
                delaySeconds,
                error -> {}
        );
    }

    @Keep
    public static void streamUpgradeOS(Context context, String zipFilePath, boolean suppressReboot) {
        streamUpgradeOS(context, zipFilePath, suppressReboot, 0);
    }

    @Keep
    public static void streamDowngradeOS(Context context, String zipFilePath, long delaySeconds) {
        MXPowerManager.callPowerManagerFeature(
                context,
                MXBase.PowerManagerOptions.OS_DOWNGRADE_STREAMING,
                zipFilePath,
                MXBase.PowerManagerSuppressRebootOptions.DO_NOTHING,
                delaySeconds,
                error -> {}
        );
    }

    @Keep
    public static void streamDowngradeOS(Context context, String zipFilePath) {
        streamDowngradeOS(context, zipFilePath, 0);
    }

    @Keep
    public static void cancelOngoingUpdate(Context context) {
        MXPowerManager.callPowerManagerFeature(
                context,
                MXBase.PowerManagerOptions.OS_CANCEL_ONGOING,
                "",
                MXBase.PowerManagerSuppressRebootOptions.DO_NOTHING,
                0,
                error -> {}
        );
    }

    @Keep
    public static void checkOSZipFile(Context context, String manifestFilePath, long delaySeconds, Consumer<Boolean> callback) {
        MXPowerManager.callPowerManagerFeature(
                context,
                MXBase.PowerManagerOptions.OS_UPDATE_VERIFY,
                manifestFilePath,
                MXBase.PowerManagerSuppressRebootOptions.DO_NOTHING,
                delaySeconds,
                error -> callback.accept(error == null)
        );
    }

    @Keep
    public static void checkOSZipFile(Context context, String manifestFilePath, Consumer<Boolean> callback) {
        checkOSZipFile(context, manifestFilePath, 0, callback);
    }

    @Keep
    public static void setSystemClock(Context context, String timeZone, String date, String time, long delaySeconds, Consumer<Boolean> callback) {
        MXClockManager.callClockSet(
                context,
                true,
                timeZone,
                date,
                time,
                delaySeconds,
                error -> callback.accept(error == null)
        );
    }

    @Keep
    public static void setSystemClock(Context context, String timeZone, String date, String time, Consumer<Boolean> callback) {
        setSystemClock(context, timeZone, date, time, 0, callback);
    }

    @Keep
    public static void resetSystemClockToNTP(Context context, String ntpServer, String syncInterval, long delaySeconds, Consumer<Boolean> callback) {
        MXClockManager.callClockResetAuto(
                context,
                true,
                ntpServer,
                syncInterval,
                delaySeconds,
                error -> callback.accept(error == null)
        );
    }

    @Keep
    public static void resetSystemClockToNTP(Context context, String ntpServer, String syncInterval, Consumer<Boolean> callback) {
        resetSystemClockToNTP(context, ntpServer, syncInterval, 0, callback);
    }

    @Keep
    public static void setScreenLockType(Context context, MXBase.ScreenLockType lockType, long delaySeconds, Consumer<Boolean> callback) {
        MXDeviceAdminManager.setScreenLockType(
                context,
                lockType,
                delaySeconds,
                error -> callback.accept(error == null)
        );
    }

    @Keep
    public static void setScreenLockType(Context context, MXBase.ScreenLockType lockType, Consumer<Boolean> callback) {
        setScreenLockType(context, lockType, 0, callback);
    }

    @Keep
    public static void setScreenShotUsage(Context context, MXBase.ScreenShotUsage usage, long delaySeconds, Consumer<Boolean> callback) {
        MXDisplayManager.setScreenShotUsage(
                context,
                usage,
                delaySeconds,
                error -> callback.accept(error == null)
        );
    }

    @Keep
    public static void setScreenShotUsage(Context context, MXBase.ScreenShotUsage usage, Consumer<Boolean> callback) {
        setScreenShotUsage(context, usage, 0, callback);
    }

    @RequiresApi(Build.VERSION_CODES.R)
    @Keep
    public static void setPowerKeyMenuEnablePowerOffButton(Context context, boolean enable, long delaySeconds, Consumer<Boolean> callback) {
        MXPowerKeyManager.powerKeyMenuEnablePowerOffButton(context, enable, delaySeconds, error -> callback.accept(error == null));
    }

    @RequiresApi(Build.VERSION_CODES.R)
    @Keep
    public static void setPowerKeyMenuEnablePowerOffButton(Context context, boolean enable, Consumer<Boolean> callback) {
        setPowerKeyMenuEnablePowerOffButton(context, enable, 0, callback);
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Keep
    public static void setRecoveryModeAccess(Context context, boolean enable, long delaySeconds, Consumer<Boolean> callback) {
        MXBase.PowerManagerRecoveryModeAccessOptions option = enable ?
                MXBase.PowerManagerRecoveryModeAccessOptions.ENABLE : MXBase.PowerManagerRecoveryModeAccessOptions.DISABLE;
        MXPowerManager.callPowerManagerRecoveryModeControlFeature(context, option, delaySeconds, error -> callback.accept(error == null));
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Keep
    public static void setRecoveryModeAccess(Context context, boolean enable, Consumer<Boolean> callback) {
        setRecoveryModeAccess(context, enable, 0, callback);
    }

    @Keep
    public static void setUSBClientModeChargingOnly(Context context, long delaySeconds, Consumer<Boolean> callback) {
        MXUSBManager.setUSBClientModeDefault(context, MXBase.UsbClientModeDefaultOptions.CHARGING_ONLY, delaySeconds, error -> callback.accept(error == null));
    }

    @Keep
    public static void setUSBClientModeChargingOnly(Context context, Consumer<Boolean> callback) {
        setUSBClientModeChargingOnly(context, 0, callback);
    }

    @Keep
    public static void setUSBClientModeFileTransfer(Context context, long delaySeconds, Consumer<Boolean> callback) {
        MXUSBManager.setUSBClientModeDefault(context, MXBase.UsbClientModeDefaultOptions.FILE_TRANSFER, delaySeconds, error -> callback.accept(error == null));
    }

    @Keep
    public static void setUSBClientModeFileTransfer(Context context, Consumer<Boolean> callback) {
        setUSBClientModeFileTransfer(context, 0, callback);
    }

    @Keep
    public static void setUSBClientModeTethering(Context context, long delaySeconds, Consumer<Boolean> callback) {
        MXUSBManager.setUSBClientModeDefault(context, MXBase.UsbClientModeDefaultOptions.USB_TETHERING, delaySeconds, error -> callback.accept(error == null));
    }

    @Keep
    public static void setUSBClientModeTethering(Context context, Consumer<Boolean> callback) {
        setUSBClientModeTethering(context, 0, callback);
    }

    @Keep
    public static void setTouchPanelSensitivity(Context context, MXBase.TouchPanelSensitivityOptions option, long delaySeconds, Consumer<Boolean> callback) {
        MXTouchManager.configTouchPanelSensitivity(context, option, delaySeconds, error -> callback.accept(error == null));
    }

    @Keep
    public static void setTouchPanelSensitivity(Context context, MXBase.TouchPanelSensitivityOptions option, Consumer<Boolean> callback) {
        setTouchPanelSensitivity(context, option, 0, callback);
    }

    @Keep
    public static void powerKeyTriggerAutoScreenLock(Context context, boolean enable, long delaySeconds, Consumer<Boolean> callback) {
        MXPowerKeyManager.powerKeyTriggerAutoScreenLock(context, enable, delaySeconds, error -> callback.accept(error == null));
    }

    @Keep
    public static void powerKeyTriggerAutoScreenLock(Context context, boolean enable, Consumer<Boolean> callback) {
        powerKeyTriggerAutoScreenLock(context, enable, 0, callback);
    }

    @Keep
    public static void powerKeyAutoScreenLockSettingsOptionEnable(Context context, boolean enable, long delaySeconds, Consumer<Boolean> callback) {
        MXPowerKeyManager.powerKeyAutoScreenLockSettingsOptionEnable(context, enable, delaySeconds, error -> callback.accept(error == null));
    }

    @Keep
    public static void powerKeyAutoScreenLockSettingsOptionEnable(Context context, boolean enable, Consumer<Boolean> callback) {
        powerKeyAutoScreenLockSettingsOptionEnable(context, enable, 0, callback);
    }

    @Keep
    public static void fetchProductModel(Context context, long delaySeconds, Consumer<String> callback) {
        MXOEMInfoManager.fetchProductModelInBackground(context, (result, error) -> callback.accept(result != null ? result : ""));
    }

    @Keep
    public static void fetchProductModel(Context context, Consumer<String> callback) {
        fetchProductModel(context, 0, callback);
    }

    @Keep
    public static void fetchSerialNumber(Context context, long delaySeconds, Consumer<String> callback) {
        MXOEMInfoManager.fetchSerialNumberInBackground(context, (result, error) -> callback.accept(result != null ? result : ""));
    }

    @Keep
    public static void fetchSerialNumber(Context context, Consumer<String> callback) {
        fetchSerialNumber(context, 0, callback);
    }

    @Keep
    public static void fetchPPID(Context context, boolean isDevDevice, long delaySeconds, Consumer<String> callback) {
        fetchSerialNumber(context, delaySeconds, result -> {
            if (result.isEmpty()) {
                callback.accept("");
                return;
            }
            String suffix = result.length() > 5 ? result.substring(result.length() - 5) : result;
            String prefix = isDevDevice ? "619" : "610";
            String yppid = prefix + suffix;
            callback.accept(yppid);
        });
    }

    @Keep
    public static void fetchPPID(Context context, boolean isDevDevice, Consumer<String> callback) {
        fetchPPID(context, isDevDevice, 0, callback);
    }

    @Keep
    public static void fetchIMEI(Context context, long delaySeconds, Consumer<String> callback) {
        if (!DeviceInfoUtils.hasTelephonyFeature(context)) {
            callback.accept("");
            return;
        }
        MXOEMInfoManager.fetchIMEIInBackground(context, (result, error) -> callback.accept(result != null ? result : ""));
    }

    @Keep
    public static void fetchIMEI(Context context, Consumer<String> callback) {
        fetchIMEI(context, 0, callback);
    }

    @Keep
    public static void fetchTouchMode(Context context, long delaySeconds, Consumer<String> callback) {
        MXOEMInfoManager.fetchTouchModeInBackground(context, (result, error) -> callback.accept(result != null ? result : ""));
    }

    @Keep
    public static void fetchTouchMode(Context context, Consumer<String> callback) {
        fetchTouchMode(context, 0, callback);
    }

    @Keep
    public static void fetchVendorTouchMode(Context context, long delaySeconds, Consumer<String> callback) {
        // This was likely intended to be similar to fetchTouchMode but with vendor URI
        MXOEMInfoManager.fetchTouchModeInBackground(context, (result, error) -> callback.accept(result != null ? result : ""));
    }

    @Keep
    public static void fetchVendorTouchMode(Context context, Consumer<String> callback) {
        fetchVendorTouchMode(context, 0, callback);
    }

    @Keep
    public static void fetchOSUpdateStatus(Context context, MXBase.OSUpdateStatusCallback callback) {
        MXOEMInfoManager.fetchOSUpdateStatusInBackground(context, callback);
    }

    @Keep
    public static void setKeyMappingToSendIntent(Context context, MXBase.KeyIdentifiers keyIdentifier, String intentAction, String intentCategory, long delaySeconds, Consumer<Boolean> callback) {
        MXKeymappingManager.remappingKeyToSendIntent(
                context,
                keyIdentifier,
                intentAction,
                intentCategory,
                delaySeconds,
                error -> callback.accept(error == null)
        );
    }

    @Keep
    public static void setKeyMappingToSendIntent(Context context, MXBase.KeyIdentifiers keyIdentifier, String intentAction, String intentCategory, Consumer<Boolean> callback) {
        setKeyMappingToSendIntent(context, keyIdentifier, intentAction, intentCategory, 0, callback);
    }

    @Keep
    public static void setKeyMappingToDefault(Context context, long delaySeconds, Consumer<Boolean> callback) {
        MXKeymappingManager.remappingAllKeyToDefault(context, delaySeconds, error -> callback.accept(error == null));
    }

    @Keep
    public static void setKeyMappingToDefault(Context context, Consumer<Boolean> callback) {
        setKeyMappingToDefault(context, 0, callback);
    }

    @Keep
    public static void copyAndImportFreeFormOCRProfile(Context context, long delaySeconds, Consumer<Boolean> callback) {
        String target = "/data/tmp/public/dwprofile_ocr_workflow.db";
        MXFileManager.copyEmbeddedFreeFormOCRProfile(context, target, delaySeconds, errorInfo1 -> {
            if (errorInfo1 != null) {
                callback.accept(false);
            } else {
                MXDataWedgeManager.importProfile(context, target, delaySeconds, errorInfo2 -> {
                    callback.accept(errorInfo2 == null);
                });
            }
        });
    }

    @Keep
    public static void copyAndImportFreeFormOCRProfile(Context context, Consumer<Boolean> callback) {
        copyAndImportFreeFormOCRProfile(context, 0, callback);
    }
}
