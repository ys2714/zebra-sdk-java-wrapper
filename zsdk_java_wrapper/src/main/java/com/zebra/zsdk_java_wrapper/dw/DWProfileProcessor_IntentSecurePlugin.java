package com.zebra.zsdk_java_wrapper.dw;

import android.content.Context;
import android.os.Bundle;

import com.zebra.zsdk_java_wrapper.utils.AssetsReader;
import com.zebra.zsdk_java_wrapper.utils.JsonUtils;
import com.zebra.zsdk_java_wrapper.utils.PackageUtils;

import java.util.HashMap;
import java.util.Map;

public class DWProfileProcessor_IntentSecurePlugin {

    public static Bundle bundleForIntentSecurePlugin(Context context,
                                                     String profileName,
                                                     String intentAction,
                                                     DWAPI.IntentDeliveryOptions deliveryOptions) {
        Map<String, String> params = new HashMap<>();
        params.put(DWConst.PROFILE_NAME, profileName);
        params.put(DWConst.PROFILE_ENABLED, "true");
        params.put(DWConst.PACKAGE_NAME, context.getPackageName());
        params.put(DWConst.intent_output_enabled, "true");
        params.put(DWConst.intent_action, intentAction);
        params.put(DWConst.intent_category, "android.intent.category.DEFAULT");
        params.put(DWConst.intent_delivery, String.valueOf(deliveryOptions.getValue()));
        params.put(DWConst.intent_use_content_provider, "false");
        params.put(DWConst.SIGNATURE, PackageUtils.getPackageSignature(context));

        String jsonString = AssetsReader.readFileToStringWithParams(
                context,
                DWConst.IntentSecurePluginJSON,
                params
        );
        return JsonUtils.jsonToBundle(jsonString);
    }
}
