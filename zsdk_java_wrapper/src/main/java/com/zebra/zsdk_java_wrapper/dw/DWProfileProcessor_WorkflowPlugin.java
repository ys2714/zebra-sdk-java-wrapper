package com.zebra.zsdk_java_wrapper.dw;

import android.content.Context;
import android.os.Bundle;

import com.zebra.zsdk_java_wrapper.utils.AssetsReader;
import com.zebra.zsdk_java_wrapper.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

public class DWProfileProcessor_WorkflowPlugin {

    public static Bundle bundleForWorkflowPlugin(Context context,
                                                 String profileName,
                                                 boolean enabled) {
        String enabledString = enabled ? "true" : "false";
        Map<String, String> params = new HashMap<>();
        params.put(DWConst.PROFILE_NAME, profileName);
        params.put(DWConst.PROFILE_ENABLED, "true");
        params.put("workflow_input_enabled", enabledString);

        String jsonString = AssetsReader.readFileToStringWithParams(
                context,
                DWConst.WorkflowPluginJSON,
                params
        );
        return JsonUtils.jsonToBundle(jsonString);
    }
}
