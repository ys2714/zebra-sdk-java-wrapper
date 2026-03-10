package com.zebra.zsdk_java_wrapper.dw;

import android.content.Context;
import android.os.Bundle;
import com.zebra.zsdk_java_wrapper.utils.AssetsReader;
import com.zebra.zsdk_java_wrapper.utils.JsonUtils;
import java.util.HashMap;
import java.util.Map;

/**
 * (*) Notes related to scanner_selection_by_identifier:
 *
 * Sending "auto" as the scanner identifier in the multiple scanner bundle returns error code "PARAMETER_INVALID" with more detailed error code "AUTO_NOT_SUPPORTED_IN_MULTI_SCANNER_MODE".
 * Sending an unsupported trigger does not return any error code.
 * If the same trigger is assigned to a different scanner in a different scanner category, the scanner that is processed last gets the priority. Processing order of the plugins cannot be guaranteed.
 * Only one internal scanner can be added. If an attempt is made to add another internal scanner, the scanner that is processed last gets the priority. Processing order of the plugins cannot be guaranteed.
 * Although triggers can be set that are not supported by that device, only supported triggers are displayed in the UI.
 * When using multiple scanners, the parameter scanner_selection_by_identifier must be used with DataWedge APIs such as SWITCH_SCANNER_PARAMS, SOFT_SCAN_TRIGGER, etc. Otherwise error COMMAND_NOT_SUPPORTED is encountered.
 */
public class DWProfileProcessor_BarcodePlugin {

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
}
