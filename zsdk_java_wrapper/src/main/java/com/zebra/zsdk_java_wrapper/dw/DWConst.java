package com.zebra.zsdk_java_wrapper.dw;

import androidx.annotation.Keep;

@Keep
public final class DWConst {

    private DWConst() {}

    // JSON file names
    public static final String BindProfileJSON = "bind_profile_to_app.json";
    public static final String ScannerPluginJSON = "plugin_scanner.json";
    public static final String WorkflowPluginJSON = "plugin_workflow.json";
    public static final String IntentPluginJSON = "plugin_intent.json";
    public static final String IntentSecurePluginJSON = "plugin_intent_secure.json";
    public static final String BDFPluginJSON = "plugin_bdf.json";
    public static final String KeystrokePluginJSON = "plugin_keystroke.json";

    // Common
    public static final String SEND_RESULT = "SEND_RESULT";
    public static final String COMMAND_IDENTIFIER = "COMMAND_IDENTIFIER";
    public static final String PROFILE_NAME = "PROFILE_NAME";
    public static final String PROFILE_ENABLED = "PROFILE_ENABLED";
    public static final String CONFIG_MODE = "CONFIG_MODE";
    public static final String PLUGIN_CONFIG = "PLUGIN_CONFIG";
    public static final String PACKAGE_NAME = "PACKAGE_NAME";
    public static final String SIGNATURE = "SIGNATURE";
    public static final String PLUGIN_NAME = "PLUGIN_NAME";
    public static final String SET_CONFIG = "SET_CONFIG";

    public static final String OUTPUT_PLUGIN_NAME = "OUTPUT_PLUGIN_NAME";
    public static final String RESET_CONFIG = "RESET_CONFIG";
    public static final String PARAM_LIST = "PARAM_LIST";

    // Plugin BDF
    public static final String bdf_enabled = "bdf_enabled";
    public static final String bdf_send_data = "bdf_send_data";

    // Plugin BARCODE
    public static final String scanner_input_enabled = "scanner_input_enabled";
    public static final String scanner_selection = "scanner_selection";
    public static final String scanner_selection_by_identifier = "scanner_selection_by_identifier";
    public static final String scanning_mode = "scanning_mode";
    public static final String barcode_trigger_mode = "barcode_trigger_mode";

    // Plugin Keystroke
    public static final String keystroke_output_enabled = "keystroke_output_enabled";

    // Plugin Intent
    public static final String intent_output_enabled = "intent_output_enabled";
    public static final String intent_action = "intent_action";
    public static final String intent_category = "intent_category";
    public static final String intent_delivery = "intent_delivery";
    public static final String intent_component_info = "intent_component_info";
    public static final String intent_use_content_provider = "intent_use_content_provider";

    // Decoder
    public static final String decoder_australian_postal = "decoder_australian_postal";
    public static final String decoder_aztec = "decoder_aztec";
    public static final String decoder_canadian_postal = "decoder_canadian_postal";
    public static final String decoder_chinese_2of5 = "decoder_chinese_2of5";
    public static final String decoder_codabar = "decoder_codabar";
    public static final String decoder_code11 = "decoder_code11";
    public static final String decoder_code32 = "decoder_code32";
    public static final String decoder_code39 = "decoder_code39";
    public static final String decoder_code93 = "decoder_code93";
    public static final String decoder_code128 = "decoder_code128";
    public static final String decoder_composite_ab = "decoder_composite_ab";
    public static final String decoder_composite_c = "decoder_composite_c";
    public static final String decoder_datamatrix = "decoder_datamatrix";
    public static final String decoder_signature = "decoder_signature";
    public static final String decoder_d2of5 = "decoder_d2of5";
    public static final String decoder_dotcode = "decoder_dotcode";
    public static final String decoder_dutch_postal = "decoder_dutch_postal";
    public static final String decoder_ean8 = "decoder_ean8";
    public static final String decoder_ean13 = "decoder_ean13";
    public static final String decoder_finnish_postal_4s = "decoder_finnish_postal_4s";
    public static final String decoder_grid_matrix = "decoder_grid_matrix";
    public static final String decoder_gs1_databar = "decoder_gs1_databar";
    public static final String decoder_gs1_databar_lim = "decoder_gs1_databar_lim";
    public static final String decoder_gs1_databar_exp = "decoder_gs1_databar_exp";
    public static final String decoder_gs1_datamatrix = "decoder_gs1_datamatrix";
    public static final String decoder_gs1_qrcode = "decoder_gs1_qrcode";
    public static final String decoder_hanxin = "decoder_hanxin";
    public static final String decoder_i2of5 = "decoder_i2of5";
    public static final String decoder_japanese_postal = "decoder_japanese_postal";
    public static final String decoder_korean_3of5 = "decoder_korean_3of5";
    public static final String decoder_mailmark = "decoder_mailmark";
    public static final String decoder_matrix_2of5 = "decoder_matrix_2of5";
    public static final String decoder_maxicode = "decoder_maxicode";
    public static final String decoder_micr_e13b = "decoder_micr_e13b";
    public static final String decoder_micropdf = "decoder_micropdf";
    public static final String decoder_microqr = "decoder_microqr";
    public static final String decoder_msi = "decoder_msi";
    public static final String decoder_ocr_a = "decoder_ocr_a";
    public static final String decoder_ocr_b = "decoder_ocr_b";
    public static final String decoder_pdf417 = "decoder_pdf417";
    public static final String decoder_qrcode = "decoder_qrcode";
    public static final String decoder_tlc39 = "decoder_tlc39";
    public static final String decoder_trioptic39 = "decoder_trioptic39";
    public static final String decoder_uk_postal = "decoder_uk_postal";
    public static final String decoder_us_currency = "decoder_us_currency";
    public static final String decoder_usplanet = "decoder_usplanet";
    public static final String decoder_us_postal = "decoder_us_postal";
    public static final String decoder_uspostnet = "decoder_uspostnet";
    public static final String decoder_upca = "decoder_upca";
    public static final String decoder_upce0 = "decoder_upce0";
    public static final String decoder_upce1 = "decoder_upce1";
    public static final String decoder_us4state = "decoder_us4state";
    public static final String decoder_us4state_fics = "decoder_us4state_fics";

    // Decoder Params
    public static final String decoder_codabar_length1 = "decoder_codabar_length1";
    public static final String decoder_codabar_length2 = "decoder_codabar_length2";
    public static final String decoder_codabar_redundancy = "decoder_codabar_redundancy";
    public static final String decoder_codabar_clsi_editing = "decoder_codabar_clsi_editing";
    public static final String decoder_codabar_notis_editing = "decoder_codabar_notis_editing";
    public static final String decoder_code39_length1 = "decoder_code39_length1";
    public static final String decoder_code39_length2 = "decoder_code39_length2";
    public static final String decoder_code39_redundancy = "decoder_code39_redundancy";
    public static final String decoder_code39_verify_check_digit = "decoder_code39_verify_check_digit";
    public static final String decoder_code39_report_check_digit = "decoder_code39_report_check_digit";
    public static final String decoder_code39_full_ascii = "decoder_code39_full_ascii";
    public static final String decoder_code39_convert_to_code32 = "decoder_code39_convert_to_code32";
    public static final String decoder_code39_report_code32_prefix = "decoder_code39_report_code32_prefix";
    public static final String code39_enable_marginless_decode = "code39_enable_marginless_decode";
    public static final String decoder_code39_security_level = "decoder_code39_security_level";
    public static final String decoder_code128_length1 = "decoder_code128_length1";
    public static final String decoder_code128_length2 = "decoder_code128_length2";
    public static final String decoder_code128_redundancy = "decoder_code128_redundancy";
    public static final String decoder_code128_enable_ean128 = "decoder_code128_enable_ean128";
    public static final String decoder_code128_enable_isbt128 = "decoder_code128_enable_isbt128";
    public static final String decoder_code128_enable_plain = "decoder_code128_enable_plain";
    public static final String decoder_code128_isbt128_concat_mode = "decoder_code128_isbt128_concat_mode";
    public static final String decoder_code128_check_isbt_table = "decoder_code128_check_isbt_table";
    public static final String decoder_code128_security_level = "decoder_code128_security_level";
    public static final String code128_enable_marginless_decode = "code128_enable_marginless_decode";
    public static final String code128_ignore_fnc4 = "code128_ignore_fnc4";
    public static final String decoder_composite_ab_ucc_link_mode = "decoder_composite_ab_ucc_link_mode";
    public static final String decoder_upca_report_check_digit = "decoder_upca_report_check_digit";
    public static final String decoder_upca_preamble = "decoder_upca_preamble";
    public static final String decoder_upce0_report_check_digit = "decoder_upce0_report_check_digit";
    public static final String decoder_upce0_convert_to_upca = "decoder_upce0_convert_to_upca";
}
