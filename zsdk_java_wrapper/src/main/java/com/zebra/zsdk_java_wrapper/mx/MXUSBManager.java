package com.zebra.zsdk_java_wrapper.mx;

import android.content.Context;
import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

public class MXUSBManager {

    /**
     * https://techdocs.zebra.com/mx/usbmgr/
     *
     * USB Client Mode Default
     * */
    public static void setUSBClientModeDefault(
            Context context,
            MXBase.UsbClientModeDefaultOptions option,
            long delaySeconds,
            Consumer<MXBase.ErrorInfo> callback) {

        Map<String, String> params = Collections.singletonMap(
                MXConst.UsbClientModeDefault,
                option.getString()
        );

        MXProfileProcessor.processProfileWithCallback(
                context,
                MXBase.ProfileXML.UsbClientModeDefault,
                MXBase.ProfileName.UsbClientModeDefault,
                params,
                delaySeconds,
                callback
        );
    }

    /**
     * Overload for setUSBClientModeDefault with default delaySeconds = 0.
     */
    public static void setUSBClientModeDefault(
            Context context,
            MXBase.UsbClientModeDefaultOptions option,
            Consumer<MXBase.ErrorInfo> callback) {
        setUSBClientModeDefault(context, option, 0L, callback);
    }
}
