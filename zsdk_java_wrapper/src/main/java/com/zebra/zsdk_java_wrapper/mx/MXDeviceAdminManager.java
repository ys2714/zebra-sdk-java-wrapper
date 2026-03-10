package com.zebra.zsdk_java_wrapper.mx;

import android.content.Context;
import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

/**
 * https://techdocs.zebra.com/emdk-for-android/14-0/mx/devadmin/
 *
 * Screen Lock Type
 * Parm Name: ScreenLockType
 *
 * Options:
 * 0 - Do not change: This value (or the absence of this parm from the XML) will cause no change to the Screen Lock Type; any previously selected setting will be retained.
 * 1 - Swipe: Causes the Swipe screen-lock to be displayed whenever the Lock Screen is invoked.
 * 2 - Pattern: Causes the Pattern screen-lock to be displayed whenever the Lock Screen is invoked.
 * 3 - Pin: Causes the numerical "Pin" screen-lock to be displayed whenever the Lock Screen is invoked.
 * 4 - Password: Causes the Password screen-lock to be displayed whenever the Lock Screen is invoked.
 * 5 - None: Prevents the display of any Lock Screen at any time.
 * */
public class MXDeviceAdminManager {

    /**
     * Sets the Screen Lock Type using the DevAdmin Manager.
     *
     * @param context      The application context.
     * @param type         The screen lock type to set.
     * @param delaySeconds Delay in seconds before processing (optional).
     * @param callback     Callback to receive the result.
     */
    public static void setScreenLockType(
            Context context,
            MXBase.ScreenLockType type,
            long delaySeconds,
            Consumer<MXBase.ErrorInfo> callback) {

        Map<String, String> map = Collections.singletonMap(
                MXConst.ScreenLockType,
                type.getString()
        );

        MXProfileProcessor.processProfileWithCallback(
                context,
                MXBase.ProfileXML.DevAdminManagerDisableLockScreen,
                MXBase.ProfileName.DevAdminManagerDisableLockScreen,
                map,
                delaySeconds,
                callback
        );
    }

    /**
     * Overload for setScreenLockType with default delaySeconds = 0.
     */
    public static void setScreenLockType(
            Context context,
            MXBase.ScreenLockType type,
            Consumer<MXBase.ErrorInfo> callback) {
        setScreenLockType(context, type, 0L, callback);
    }
}
