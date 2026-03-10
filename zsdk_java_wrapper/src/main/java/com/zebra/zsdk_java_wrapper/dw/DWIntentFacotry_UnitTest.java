package com.zebra.zsdk_java_wrapper.dw;

import android.content.Context;
import android.os.Bundle;
import java.util.ArrayList;

public class DWIntentFacotry_UnitTest {

    public static Bundle simpleCreateProfileBundle(Context context, String profileName) {
        Bundle profileConfig = new Bundle();
        profileConfig.putString("PROFILE_NAME", profileName);
        profileConfig.putString("PROFILE_ENABLED", "true");
        profileConfig.putString("CONFIG_MODE", "CREATE_IF_NOT_EXIST");

        Bundle appBundle = new Bundle();
        appBundle.putString("PACKAGE_NAME", context.getPackageName());
        appBundle.putStringArray("ACTIVITY_LIST", new String[]{"*"});

        profileConfig.putParcelableArray("APP_LIST", new Bundle[]{appBundle});
        return profileConfig;
    }

    public static Bundle simpleBarcodePluginBundle(Context context, String profileName) {
        Bundle profileConfig = new Bundle();
        profileConfig.putString("PROFILE_NAME", profileName);
        profileConfig.putString("PROFILE_ENABLED", "true");
        profileConfig.putString("CONFIG_MODE", "UPDATE");

        Bundle pluginConfig = new Bundle();
        pluginConfig.putString("PLUGIN_NAME", "BARCODE");
        pluginConfig.putString("RESET_CONFIG", "true");

        Bundle paramList = new Bundle();
        paramList.putString("scanner_selection", "auto");
        paramList.putString("scanner_input_enabled", "true");
        paramList.putString("decoder_code128", "true");
        paramList.putString("decoder_code39", "true");
        paramList.putString("decoder_ean13", "true");
        paramList.putString("decoder_upca", "true");

        pluginConfig.putBundle("PARAM_LIST", paramList);
        profileConfig.putBundle("PLUGIN_CONFIG", pluginConfig);

        return profileConfig;
    }

    public static Bundle simpleKeystrokePluginBundle(Context context, String profileName) {
        Bundle profileConfig = new Bundle();
        profileConfig.putString("PROFILE_NAME", profileName);
        profileConfig.putString("PROFILE_ENABLED", "true");
        profileConfig.putString("CONFIG_MODE", "UPDATE");

        Bundle pluginConfig = new Bundle();
        pluginConfig.putString("PLUGIN_NAME", "KEYSTROKE");
        pluginConfig.putString("RESET_CONFIG", "true");

        Bundle paramList = new Bundle();
        paramList.putString("keystroke_output_enabled", "false");

        pluginConfig.putBundle("PARAM_LIST", paramList);
        profileConfig.putBundle("PLUGIN_CONFIG", pluginConfig);

        return profileConfig;
    }

    public static Bundle simpleIntentPluginBundle(Context context, String profileName, String intentAction) {
        Bundle profileConfig = new Bundle();
        profileConfig.putString("PROFILE_NAME", profileName);
        profileConfig.putString("PROFILE_ENABLED", "true");
        profileConfig.putString("CONFIG_MODE", "UPDATE");

        Bundle pluginConfig = new Bundle();
        pluginConfig.putString("PLUGIN_NAME", "INTENT");
        pluginConfig.putString("RESET_CONFIG", "true");

        Bundle paramList = new Bundle();
        paramList.putString("intent_output_enabled", "true");
        paramList.putString("intent_action", intentAction);
        paramList.putString("intent_category", "android.intent.category.DEFAULT");
        paramList.putString("intent_delivery", String.valueOf(DWAPI.IntentDeliveryOptions.BROADCAST.getValue()));

        pluginConfig.putBundle("PARAM_LIST", paramList);
        profileConfig.putBundle("PLUGIN_CONFIG", pluginConfig);

        return profileConfig;
    }

    public static Bundle barcodeInputIntentOutputBundle(Context context, String profileName, String intentAction) {
        Bundle profileConfig = new Bundle();
        profileConfig.putString("PROFILE_NAME", profileName);
        profileConfig.putString("PROFILE_ENABLED", "true");
        profileConfig.putString("CONFIG_MODE", "CREATE_IF_NOT_EXIST");

        ArrayList<Bundle> pluginConfigs = new ArrayList<>();

        // Barcode Plugin
        Bundle barcodePlugin = new Bundle();
        barcodePlugin.putString("PLUGIN_NAME", "BARCODE");
        barcodePlugin.putString("RESET_CONFIG", "true");
        Bundle barcodeParams = new Bundle();
        barcodeParams.putString("scanner_selection", "auto");
        barcodeParams.putString("scanner_selection_by_identifier", "AUTO");
        barcodeParams.putString("scanner_input_enabled", "true");
        barcodeParams.putString("decoder_code128", "true");
        barcodeParams.putString("decoder_ean13", "true");
        barcodePlugin.putBundle("PARAM_LIST", barcodeParams);
        pluginConfigs.add(barcodePlugin);

        // Keystroke Plugin
        Bundle keystrokePlugin = new Bundle();
        keystrokePlugin.putString("PLUGIN_NAME", "KEYSTROKE");
        keystrokePlugin.putString("RESET_CONFIG", "true");
        Bundle keystrokeParams = new Bundle();
        keystrokeParams.putString("keystroke_output_enabled", "false");
        keystrokePlugin.putBundle("PARAM_LIST", keystrokeParams);
        pluginConfigs.add(keystrokePlugin);

        // Intent Plugin
        Bundle intentPlugin = new Bundle();
        intentPlugin.putString("PLUGIN_NAME", "INTENT");
        intentPlugin.putString("RESET_CONFIG", "true");
        Bundle intentParams = new Bundle();
        intentParams.putString("intent_output_enabled", "true");
        intentParams.putString("intent_action", intentAction);
        intentParams.putString("intent_category", "android.intent.category.DEFAULT");
        intentParams.putString("intent_delivery", String.valueOf(DWAPI.IntentDeliveryOptions.BROADCAST.getValue()));
        intentPlugin.putBundle("PARAM_LIST", intentParams);
        pluginConfigs.add(intentPlugin);

        profileConfig.putParcelableArrayList("PLUGIN_CONFIG", pluginConfigs);

        return profileConfig;
    }
}
