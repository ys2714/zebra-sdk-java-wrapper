package com.zebra.zsdk_java_wrapper.mx;

import android.content.Context;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * https://techdocs.zebra.com/emdk-for-android/14-0/mx/powermgr/
 *
 * Reset Action
 *
 * https://techdocs.zebra.com/mx/conditionmgr/
 *
 * It's important to note that PowerMgr actions such as Reboot and Reset, once executed,
 * prevent the execution of subsequent actions submitted by the Request XML document,
 * including the submission of a Result XML to the application sending the original Request.
 * Zebra therefore recommends using Condition Manager in conjunction with PowerMgr to ensure that
 * appropriate conditions exist on a device before attempting to perform "risky" operations such as OS updates,
 * the failure of which can render a device unusable, severely limited or otherwise in need of service.
 * */
public class MXPowerManager {

    /**
     * https://techdocs.zebra.com/emdk-for-android/14-0/mx/powermgr/
     *
     * Reset Action
     *
     * https://techdocs.zebra.com/mx/conditionmgr/
     *
     * It's important to note that PowerMgr actions such as Reboot and Reset, once executed,
     * prevent the execution of subsequent actions submitted by the Request XML document,
     * including the submission of a Result XML to the application sending the original Request.
     * Zebra therefore recommends using Condition Manager in conjunction with PowerMgr to ensure that
     * appropriate conditions exist on a device before attempting to perform "risky" operations such as OS updates,
     * the failure of which can render a device unusable, severely limited or otherwise in need of service.
     * */
    public static void callPowerManagerFeature(
            Context context,
            MXBase.PowerManagerOptions option,
            String zipFile,
            MXBase.PowerManagerSuppressRebootOptions suppressReboot,
            long delaySeconds,
            Consumer<MXBase.ErrorInfo> callback) {

        Map<String, String> map = new HashMap<>();

        switch (option) {
            case SLEEP_MODE:
            case REBOOT:
            case POWER_OFF:
            case OS_CANCEL_ONGOING:
            case ENTERPRISE_RESET:
            case FACTORY_RESET:
            case FULL_DEVICE_WIPE:
                map.put(MXConst.ResetAction, option.getString());
                MXProfileProcessor.processProfileWithCallback(
                        context,
                        MXBase.ProfileXML.PowerManagerReset,
                        MXBase.ProfileName.PowerManagerReset,
                        map,
                        delaySeconds,
                        callback
                );
                break;

            case OS_UPDATE:
            case OS_UPGRADE:
            case OS_DOWNGRADE:
                String filePath = (zipFile != null) ? zipFile : "";
                map.put(MXConst.ResetAction, option.getString());
                map.put(MXConst.ZipFile, filePath);
                map.put(MXConst.SuppressReboot, suppressReboot.getString());
                MXProfileProcessor.processProfileWithCallback(
                        context,
                        MXBase.ProfileXML.PowerManagerResetOSUpdate,
                        MXBase.ProfileName.PowerManagerResetOSUpdate,
                        map,
                        delaySeconds,
                        callback
                );
                break;

            case OS_UPGRADE_STREAMING:
            case OS_DOWNGRADE_STREAMING:
                String fileUrl = (zipFile != null) ? zipFile : "";
                map.put(MXConst.ResetAction, option.getString());
                map.put(MXConst.RemoteZipFile, fileUrl);
                map.put(MXConst.SuppressReboot, suppressReboot.getString());
                MXProfileProcessor.processProfileWithCallback(
                        context,
                        MXBase.ProfileXML.PowerManagerResetOSStreaming,
                        MXBase.ProfileName.PowerManagerResetOSStreaming,
                        map,
                        delaySeconds,
                        callback
                );
                break;

            case OS_UPDATE_VERIFY:
                String verifyPath = (zipFile != null) ? zipFile : "";
                map.put(MXConst.ResetAction, option.getString());
                map.put(MXConst.OsupdateVerifyFile, verifyPath);
                MXProfileProcessor.processProfileWithCallback(
                        context,
                        MXBase.ProfileXML.PowerManagerResetOSVerify,
                        MXBase.ProfileName.PowerManagerResetOSVerify,
                        map,
                        delaySeconds,
                        callback
                );
                break;

            case CREATE_PROFILE:
            case DO_NOTHING:
            default:
                // Do nothing
                break;
        }
    }

    /** Overload for callPowerManagerFeature with default suppressReboot and delaySeconds */
    public static void callPowerManagerFeature(
            Context context,
            MXBase.PowerManagerOptions option,
            String zipFile,
            Consumer<MXBase.ErrorInfo> callback) {
        callPowerManagerFeature(context, option, zipFile, MXBase.PowerManagerSuppressRebootOptions.DO_NOTHING, 0, callback);
    }

    /** Overload for callPowerManagerFeature with default zipFile, suppressReboot and delaySeconds */
    public static void callPowerManagerFeature(
            Context context,
            MXBase.PowerManagerOptions option,
            Consumer<MXBase.ErrorInfo> callback) {
        callPowerManagerFeature(context, option, null, MXBase.PowerManagerSuppressRebootOptions.DO_NOTHING, 0, callback);
    }

    /**
     * https://techdocs.zebra.com/emdk-for-android/14-0/mx/powermgr/
     *
     * Recovery Mode Access
     *
     * IMPORTANT PERSISTENCE NOTE: This setting persists on the device ONLY if the Enterprise Reset / Factory Reset is initiated by a barcode.
     * Resetting the device through the Android Settings panel or by any other means removes all settings created for this feature.
     * */
    public static void callPowerManagerRecoveryModeControlFeature(
            Context context,
            MXBase.PowerManagerRecoveryModeAccessOptions option,
            long delaySeconds,
            Consumer<MXBase.ErrorInfo> callback) {

        Map<String, String> map = Collections.singletonMap(
                MXConst.RecoveryModeAccess,
                option.getString()
        );

        MXProfileProcessor.processProfileWithCallback(
                context,
                MXBase.ProfileXML.PowerManagerRecoveryModeAccess,
                MXBase.ProfileName.PowerManagerRecoveryModeAccess,
                map,
                delaySeconds,
                callback
        );
    }

    /** Overload for callPowerManagerRecoveryModeControlFeature with default delaySeconds = 0 */
    public static void callPowerManagerRecoveryModeControlFeature(
            Context context,
            MXBase.PowerManagerRecoveryModeAccessOptions option,
            Consumer<MXBase.ErrorInfo> callback) {
        callPowerManagerRecoveryModeControlFeature(context, option, 0L, callback);
    }
}
