package com.zebra.zsdk_java_wrapper.dw;

import android.content.Context;
import android.os.Bundle;
import com.zebra.zsdk_java_wrapper.utils.AssetsReader;
import com.zebra.zsdk_java_wrapper.utils.JsonUtils;
import java.util.HashMap;
import java.util.Map;

public class DWProfileProcessor_BDFPlugin {
    public static Bundle bundleForBDFPlugin(Context context,
                                            String profileName,
                                            DWAPI.Plugin.Output outputPlugin,
                                            boolean sendDate) {
        String sendDateStr = sendDate ? "true" : "false";
        Map<String, String> params = new HashMap<>();
        params.put(DWConst.PROFILE_NAME, profileName);
        params.put(DWConst.PROFILE_ENABLED, "true");
        params.put(DWConst.PACKAGE_NAME, context.getPackageName());
        params.put(DWConst.OUTPUT_PLUGIN_NAME, outputPlugin.getValue());
        params.put(DWConst.bdf_enabled, "true");
        params.put(DWConst.bdf_send_data, sendDateStr);

        String jsonString = AssetsReader.readFileToStringWithParams(
                context,
                DWConst.BDFPluginJSON,
                params
        );
        return JsonUtils.jsonToBundle(jsonString);
    }
}
