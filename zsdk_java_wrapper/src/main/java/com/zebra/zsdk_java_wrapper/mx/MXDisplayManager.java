package com.zebra.zsdk_java_wrapper.mx;

import android.content.Context;
import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

public class MXDisplayManager {

    /**
     * https://techdocs.zebra.com/emdk-for-android/14-0/mx/displaymgr/
     *
     * Screen Shot Enable/Disable
     * Parm Name: ScreenShotUsage
     *
     * Options:
     * 0 - Do Nothing
     * 1 - Enable
     * 2 - Disable
     * */
    public static void setScreenShotUsage(
            Context context,
            MXBase.ScreenShotUsage usage,
            long delaySeconds,
            Consumer<MXBase.ErrorInfo> callback) {

        Map<String, String> map = Collections.singletonMap(
                MXConst.ScreenShotUsage,
                usage.getString()
        );

        MXProfileProcessor.processProfileWithCallback(
                context,
                MXBase.ProfileXML.DisplayManagerDisableScreenShot,
                MXBase.ProfileName.DisplayManagerDisableScreenShot,
                map,
                delaySeconds,
                callback
        );
    }

    /**
     * Overload for setScreenShotUsage with default delaySeconds = 0.
     */
    public static void setScreenShotUsage(
            Context context,
            MXBase.ScreenShotUsage usage,
            Consumer<MXBase.ErrorInfo> callback) {
        setScreenShotUsage(context, usage, 0L, callback);
    }

    public static void enableScreenShotFeature(
            Context context,
            long delaySeconds,
            Consumer<MXBase.ErrorInfo> callback) {

        setScreenShotUsage(context, MXBase.ScreenShotUsage.ENABLE, delaySeconds, callback);
    }

    /**
     * Overload for enableScreenShotFeature with default delaySeconds = 0.
     */
    public static void enableScreenShotFeature(
            Context context,
            Consumer<MXBase.ErrorInfo> callback) {
        enableScreenShotFeature(context, 0L, callback);
    }
}
