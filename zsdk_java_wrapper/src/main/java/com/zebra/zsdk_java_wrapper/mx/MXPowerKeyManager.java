package com.zebra.zsdk_java_wrapper.mx;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

/**
 * https://techdocs.zebra.com/emdk-for-android/14-0/mx/powerkeymgr/
 *
 * Main Functionality
 * Enable/Disable the following Power-off Menu Options:
 *  - Power-off Button
 *  - Airplane Mode
 *  - Touch Panel
 *  - Safe Mode
 * Enable/Disable Automatic Screen Lock Settings panel option
 * Enable/Disable "Lock screen instantly with power key" Settings panel option
 * */
public class MXPowerKeyManager {

    /**
     *  https://techdocs.zebra.com/emdk-for-android/14-0/mx/powerkeymgr/
     *
     *  Power-off Button Show/Hide (MX11.4+)
     *  Parm Name: PowerOffState
     *  Options:
     *  1 - Show: Enables the Power-off button to be shown after the device power key is long-pressed.
     *  2 - Hide: Prevents the Power-off button from being shown after the device power key is long-pressed.
     * */
    @RequiresApi(Build.VERSION_CODES.R)
    public static void powerKeyMenuEnablePowerOffButton(
            Context context,
            boolean enable,
            long delaySeconds,
            Consumer<MXBase.ErrorInfo> callback) {

        String enableValue = enable ? MXBase.ShowHideState.SHOW.getString() : MXBase.ShowHideState.HIDE.getString();
        Map<String, String> map = Collections.singletonMap(MXConst.PowerOffState, enableValue);

        MXProfileProcessor.processProfileWithCallback(
                context,
                MXBase.ProfileXML.PowerKeyManagerSetPowerOffState,
                MXBase.ProfileName.PowerKeyManagerSetPowerOffState,
                map,
                delaySeconds,
                callback
        );
    }

    /** Overload for default delaySeconds = 0 */
    @RequiresApi(Build.VERSION_CODES.R)
    public static void powerKeyMenuEnablePowerOffButton(
            Context context,
            boolean enable,
            Consumer<MXBase.ErrorInfo> callback) {
        powerKeyMenuEnablePowerOffButton(context, enable, 0, callback);
    }

    /**
     * https://techdocs.zebra.com/emdk-for-android/14-0/mx/powerkeymgr/
     *
     * Airplane Mode Show/Hide (MX4.3+) Note: Not supported in devices running Android 8.x Oreo or later
     * Parm Name: AirPlaneMode
     * Options:
     * 1 - Show Menu Option: Enables Airplane Mode to be controlled from the Power Off menu.
     * 2 - Do not show Menu Option: Prevents Airplane Mode from being controlled from the Power Off menu.
     * */
    public static void powerKeyMenuEnableAirplanModeButton(
            Context context,
            boolean enable,
            long delaySeconds,
            Consumer<MXBase.ErrorInfo> callback) {

        String enableValue = enable ? MXBase.ShowHideState.SHOW.getString() : MXBase.ShowHideState.HIDE.getString();
        Map<String, String> map = Collections.singletonMap(MXConst.AirPlaneMode, enableValue);

        MXProfileProcessor.processProfileWithCallback(
                context,
                MXBase.ProfileXML.PowerKeyManagerSetPowerOffState,
                MXBase.ProfileName.PowerKeyManagerSetPowerOffState,
                map,
                delaySeconds,
                callback
        );
    }

    /** Overload for default delaySeconds = 0 */
    public static void powerKeyMenuEnableAirplanModeButton(
            Context context,
            boolean enable,
            Consumer<MXBase.ErrorInfo> callback) {
        powerKeyMenuEnableAirplanModeButton(context, enable, 0, callback);
    }

    /**
     * https://techdocs.zebra.com/emdk-for-android/14-0/mx/powerkeymgr/
     *
     * Touch Panel Show/Hide (MX5.0+) Note: this is for glove mode
     * Parm Name: TouchPanel
     * Options:
     * 1 - Show Menu Option: Enables the Touch Panel to be controlled from the Power-off Menu.
     * 2 - Do not show Menu Option: Prevents the Touch Panel from being controlled from the Power-off Menu.
     * */
    public static void powerKeyMenuEnableTouchPanel(
            Context context,
            boolean enable,
            long delaySeconds,
            Consumer<MXBase.ErrorInfo> callback) {

        String enableValue = enable ? MXBase.ShowHideState.SHOW.getString() : MXBase.ShowHideState.HIDE.getString();
        Map<String, String> map = Collections.singletonMap(MXConst.TouchPanel, enableValue);

        MXProfileProcessor.processProfileWithCallback(
                context,
                MXBase.ProfileXML.PowerKeyManagerSetPowerOffState,
                MXBase.ProfileName.PowerKeyManagerSetPowerOffState,
                map,
                delaySeconds,
                callback
        );
    }

    /** Overload for default delaySeconds = 0 */
    public static void powerKeyMenuEnableTouchPanel(
            Context context,
            boolean enable,
            Consumer<MXBase.ErrorInfo> callback) {
        powerKeyMenuEnableTouchPanel(context, enable, 0, callback);
    }

    /**
     * https://techdocs.zebra.com/emdk-for-android/14-0/mx/powerkeymgr/
     *
     * Touch Panel Mode Show/Hide (MX10.2+) Note: this is for glove mode
     * Parm Name: TouchPanelMode
     * Options:
     * 1 - Show: Enables the Touch Panel Mode to be controlled from the Power-off Menu.
     * 2 - Do not show: Prevents the Touch Panel Mode from being controlled from the Power-off Menu.
     */
    public static void powerKeyMenuEnableTouchPanelMode(
            Context context,
            boolean enable,
            long delaySeconds,
            Consumer<MXBase.ErrorInfo> callback) {

        String enableValue = enable ? MXBase.ShowHideState.SHOW.getString() : MXBase.ShowHideState.HIDE.getString();
        Map<String, String> map = Collections.singletonMap(MXConst.TouchPanelMode, enableValue);

        MXProfileProcessor.processProfileWithCallback(
                context,
                MXBase.ProfileXML.PowerKeyManagerSetPowerOffState,
                MXBase.ProfileName.PowerKeyManagerSetPowerOffState,
                map,
                delaySeconds,
                callback
        );
    }

    /** Overload for default delaySeconds = 0 */
    public static void powerKeyMenuEnableTouchPanelMode(
            Context context,
            boolean enable,
            Consumer<MXBase.ErrorInfo> callback) {
        powerKeyMenuEnableTouchPanelMode(context, enable, 0, callback);
    }

    /**
     * Safe Mode Show/Hide (MX4.3+)
     * Parm Name: SafeMode
     * Options:
     * 1 - Show Menu Option: Enables Safe Mode to be controlled from the Power-off Menu.
     * 2 - Do not show Menu Option: Prevents Safe Mode from being controlled from the Power-off Menu.
     * */
    public static void powerKeyMenuEnableSafeMode(
            Context context,
            boolean enable,
            long delaySeconds,
            Consumer<MXBase.ErrorInfo> callback) {

        String enableValue = enable ? MXBase.ShowHideState.SHOW.getString() : MXBase.ShowHideState.HIDE.getString();
        Map<String, String> map = Collections.singletonMap(MXConst.SafeMode, enableValue);

        MXProfileProcessor.processProfileWithCallback(
                context,
                MXBase.ProfileXML.PowerKeyManagerSetPowerOffState,
                MXBase.ProfileName.PowerKeyManagerSetPowerOffState,
                map,
                delaySeconds,
                callback
        );
    }

    /** Overload for default delaySeconds = 0 */
    public static void powerKeyMenuEnableSafeMode(
            Context context,
            boolean enable,
            Consumer<MXBase.ErrorInfo> callback) {
        powerKeyMenuEnableSafeMode(context, enable, 0, callback);
    }

    /**
     * Automatic Screen Lock Enable/Disable (MX4.3+)
     * Parm Name: AutoScreenLockState
     *
     * Options:
     * 1 - Turn on: Forces the screen to lock whenever the screen is turned off using the power key.
     * 2 - Turn off: Prevents the screen from locking when turned off using the power key unless the Screen-lock Timeout Interval was exceeded.
     * */
    public static void powerKeyTriggerAutoScreenLock(
            Context context,
            boolean enable,
            long delaySeconds,
            Consumer<MXBase.ErrorInfo> callback) {

        String enableValue = enable ? MXBase.ShowHideState.SHOW.getString() : MXBase.ShowHideState.HIDE.getString();
        Map<String, String> map = Collections.singletonMap(MXConst.AutoScreenLockState, enableValue);

        MXProfileProcessor.processProfileWithCallback(
                context,
                MXBase.ProfileXML.PowerKeyManagerSetPowerOffState,
                MXBase.ProfileName.PowerKeyManagerSetPowerOffState,
                map,
                delaySeconds,
                callback
        );
    }

    /** Overload for default delaySeconds = 0 */
    public static void powerKeyTriggerAutoScreenLock(
            Context context,
            boolean enable,
            Consumer<MXBase.ErrorInfo> callback) {
        powerKeyTriggerAutoScreenLock(context, enable, 0, callback);
    }

    /**
     * Automatic Screen Lock Show/Hide in Settings (MX4.3+)
     * Parm Name: AutoScreenLockOption
     *
     * Options:
     * 1 - Turn on: Enables the Auto Screen Lock option to be controlled from the Android Settings panel.
     * 2 - Turn off: Prevents the Auto Screen Lock option from being controlled from the Android Settings panel.
     * */
    public static void powerKeyAutoScreenLockSettingsOptionEnable(
            Context context,
            boolean enable,
            long delaySeconds,
            Consumer<MXBase.ErrorInfo> callback) {

        String enableValue = enable ? MXBase.ShowHideState.SHOW.getString() : MXBase.ShowHideState.HIDE.getString();
        Map<String, String> map = Collections.singletonMap(MXConst.AutoScreenLockOption, enableValue);

        MXProfileProcessor.processProfileWithCallback(
                context,
                MXBase.ProfileXML.PowerKeyManagerSetPowerOffState,
                MXBase.ProfileName.PowerKeyManagerSetPowerOffState,
                map,
                delaySeconds,
                callback
        );
    }

    /** Overload for default delaySeconds = 0 */
    public static void powerKeyAutoScreenLockSettingsOptionEnable(
            Context context,
            boolean enable,
            Consumer<MXBase.ErrorInfo> callback) {
        powerKeyAutoScreenLockSettingsOptionEnable(context, enable, 0, callback);
    }
}
