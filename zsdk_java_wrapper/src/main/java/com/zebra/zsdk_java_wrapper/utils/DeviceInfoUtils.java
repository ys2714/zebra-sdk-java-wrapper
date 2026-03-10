package com.zebra.zsdk_java_wrapper.utils;

import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.Keep;

@Keep
public class DeviceInfoUtils {

    public static Boolean hasTelephonyFeature(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
    }
}
