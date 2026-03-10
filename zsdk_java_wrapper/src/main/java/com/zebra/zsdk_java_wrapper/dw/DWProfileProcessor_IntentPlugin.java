package com.zebra.zsdk_java_wrapper.dw;

import android.content.Context;
import android.os.Bundle;

import com.zebra.zsdk_java_wrapper.utils.AssetsReader;
import com.zebra.zsdk_java_wrapper.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

public class DWProfileProcessor_IntentPlugin {

    public static Bundle bundleForIntentPlugin(Context context,
                                               String profileName,
                                               DWAPI.ResultActionNames intentAction,
                                               DWAPI.ResultCategoryNames intentCategory,
                                               DWAPI.IntentDeliveryOptions deliveryOptions) {
        Map<String, String> params = new HashMap<>();
        params.put(DWConst.PROFILE_NAME, profileName);
        params.put(DWConst.PROFILE_ENABLED, "true");
        params.put(DWConst.intent_output_enabled, "true");
        params.put(DWConst.intent_action, intentAction.getValue());
        params.put(DWConst.intent_category, intentCategory.getValue());
        params.put(DWConst.intent_delivery, String.valueOf(deliveryOptions.getValue()));

        String jsonString = AssetsReader.readFileToStringWithParams(
                context,
                DWConst.IntentPluginJSON,
                params
        );
        return JsonUtils.jsonToBundle(jsonString);
    }
}
