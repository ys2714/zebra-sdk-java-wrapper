package com.zebra.zsdk_java_wrapper.dw;

import android.content.Context;
import android.os.Bundle;

import com.zebra.zsdk_java_wrapper.utils.AssetsReader;
import com.zebra.zsdk_java_wrapper.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

public class DWProfileProcessor_KeystrokePlugin {

    public static Bundle bundleForKeystrokePlugin(Context context, String profileName, boolean enable) {
        Map<String, String> params = new HashMap<>();
        params.put(DWConst.PROFILE_NAME, profileName);
        params.put(DWConst.PROFILE_ENABLED, "true");
        params.put(DWConst.keystroke_output_enabled, enable ? "true" : "false");

        String jsonString = AssetsReader.readFileToStringWithParams(
                context,
                DWConst.KeystrokePluginJSON,
                params
        );
        return JsonUtils.jsonToBundle(jsonString);
    }
}
