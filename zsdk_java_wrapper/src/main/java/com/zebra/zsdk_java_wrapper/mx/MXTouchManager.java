package com.zebra.zsdk_java_wrapper.mx;

import android.content.Context;
import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

/**
 * https://techdocs.zebra.com/mx/touchmgr/
 * */
public class MXTouchManager {

    /**
     * https://techdocs.zebra.com/mx/touchmgr/
     *
     * CAUTION!!!
     * please use following strings instead of int value
     * the EMDK Android Studio plugin exported xml showing these strings.
     *
     * Do not change
     * Finger
     * Glove and Finger
     * Stylus and Finger
     * Stylus and Glove and Finger
     * */
    public static void configTouchPanelSensitivity(
            Context context,
            MXBase.TouchPanelSensitivityOptions option,
            long delaySeconds,
            Consumer<MXBase.ErrorInfo> callback) {

        Map<String, String> params = Collections.singletonMap(
                MXConst.TouchActionAny,
                option.getXmlValue()
        );

        MXProfileProcessor.processProfileWithCallback(
                context,
                MXBase.ProfileXML.TouchPanelSensitivity,
                MXBase.ProfileName.TouchPanelSensitivity,
                params,
                delaySeconds,
                callback
        );
    }

    /**
     * Overload for configTouchPanelSensitivity with default delaySeconds = 0.
     */
    public static void configTouchPanelSensitivity(
            Context context,
            MXBase.TouchPanelSensitivityOptions option,
            Consumer<MXBase.ErrorInfo> callback) {
        configTouchPanelSensitivity(context, option, 0L, callback);
    }
}
