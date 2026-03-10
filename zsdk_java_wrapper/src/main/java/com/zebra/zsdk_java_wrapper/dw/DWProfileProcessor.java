package com.zebra.zsdk_java_wrapper.dw;

import android.content.Context;
import android.os.Bundle;

import com.zebra.zsdk_java_wrapper.utils.AssetsReader;
import com.zebra.zsdk_java_wrapper.utils.JsonUtils;
import com.zebra.zsdk_java_wrapper.utils.PackageUtils;

import java.util.HashMap;
import java.util.Map;

public class DWProfileProcessor {

    public static final String TAG = "DWProfileProcessor";

    private DWProfileProcessor() {}

    public static Bundle bundleForBindProfile(Context context, String profileName, String packageName) {
        Map<String, String> params = new HashMap<>();
        params.put(DWConst.PROFILE_NAME, profileName);
        params.put(DWConst.PROFILE_ENABLED, "true");
        params.put(DWConst.PACKAGE_NAME, packageName);

        String jsonString = AssetsReader.readFileToStringWithParams(
                context,
                DWConst.BindProfileJSON,
                params
        );
        return JsonUtils.jsonToBundle(jsonString);
    }

    public static Bundle bundleForBarcodePlugin(Context context,
                                                String profileName,
                                                boolean enable,
                                                boolean enableHardTrigger,
                                                DWAPI.ScanInputModeOptions scanningMode) {
        String pluginEnabled = enable ? "true" : "false";
        String hardTriggerEnabled = String.valueOf(enableHardTrigger ? DWAPI.BarcodeTriggerMode.ENABLED.getValue() : DWAPI.BarcodeTriggerMode.DISABLED.getValue());

        Map<String, String> params = new HashMap<>();
        params.put(DWConst.PROFILE_NAME, profileName);
        params.put(DWConst.PROFILE_ENABLED, "true");

        params.put(DWConst.scanner_input_enabled, pluginEnabled);
        params.put(DWConst.scanner_selection, "auto");
        params.put(DWConst.scanner_selection_by_identifier, DWAPI.ScannerIdentifiers.AUTO.getValue());
        params.put(DWConst.scanning_mode, String.valueOf(scanningMode.getValue()));
        params.put(DWConst.barcode_trigger_mode, hardTriggerEnabled);
        
        // Code128
        params.put(DWConst.decoder_code128, "true");
        // Others
        params.put(DWConst.decoder_ean13, "true");
        params.put(DWConst.decoder_pdf417, "true");
        params.put(DWConst.decoder_qrcode, "true");
        // UPC
        params.put(DWConst.decoder_upca, "true");
        
        // OCR
        params.put("ocr_check_digit_multiplier", "121212121212");
        params.put("ocr_lines", "1");
        params.put("ocr_b_variant", "0");
        params.put("ocr_quiet_zone", "50");
        params.put("decoder_ocr_a", "false");
        params.put("decoder_ocr_b", "false");
        params.put("ocr_subset", "!\\\"#$%()*+,-./0123456789<>ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxyz\\\\^|");
        params.put("ocr_check_digit_modulus", "1");
        params.put("ocr_orientation", "0");
        params.put("ocr_a_variant", "0");
        params.put("ocr_b_variant", "0");
        params.put("ocr_check_digit_validation", "0");
        params.put("ocr_max_chars", "100");
        params.put("ocr_template", "99999999");
        params.put("ocr_min_chars", "3");

        String jsonString = AssetsReader.readFileToStringWithParams(
                context,
                DWConst.ScannerPluginJSON,
                params
        );
        return JsonUtils.jsonToBundle(jsonString);
    }

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

    public static Bundle bundleForWorkflowPlugin(Context context,
                                                 String profileName, boolean enabled) {
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
