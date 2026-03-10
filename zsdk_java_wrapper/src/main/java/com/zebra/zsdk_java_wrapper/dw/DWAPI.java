package com.zebra.zsdk_java_wrapper.dw;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Keep;

import java.util.concurrent.CompletableFuture;

@Keep
public final class DWAPI {

    private DWAPI() {}

    public static final String TAG = "DWAPI";
    public static final long MILLISECONDS_DELAY_BETWEEN_API_CALLS = 200;

    @Keep
    public enum StringBoolean {
        TRUE("true"),
        FALSE("false");

        private final String value;
        StringBoolean(String value) { this.value = value; }
        public String getValue() { return value; }
    }

    @Keep
    public enum StringEnabled {
        ENABLED("ENABLED"),
        DISABLED("DISABLED");

        private final String value;
        StringEnabled(String value) { this.value = value; }
        public String getValue() { return value; }
    }

    @Keep
    public enum ActionNames {
        ACTION("com.symbol.datawedge.api.ACTION");

        private final String value;
        ActionNames(String value) { this.value = value; }
        public String getValue() { return value; }
    }

    @Keep
    public enum ActionExtraKeys {
        CREATE_PROFILE("com.symbol.datawedge.api.CREATE_PROFILE"),
        DELETE_PROFILE("com.symbol.datawedge.api.DELETE_PROFILE"),
        SWITCH_TO_PROFILE("com.symbol.datawedge.api.SWITCH_TO_PROFILE"),
        SWITCH_DATACAPTURE("com.symbol.datawedge.api.SWITCH_DATACAPTURE"),
        SWITCH_SCANNER_PARAMS("com.symbol.datawedge.api.SWITCH_SCANNER_PARAMS"),
        SET_CONFIG("com.symbol.datawedge.api.SET_CONFIG"),
        GET_CONFIG("com.symbol.datawedge.api.GET_CONFIG"),
        ENABLE_DATAWEDGE("com.symbol.datawedge.api.ENABLE_DATAWEDGE"),
        SCANNER_INPUT_PLUGIN("com.symbol.datawedge.api.SCANNER_INPUT_PLUGIN"),
        SOFT_SCAN_TRIGGER("com.symbol.datawedge.api.SOFT_SCAN_TRIGGER"),
        GET_DATAWEDGE_STATUS("com.symbol.datawedge.api.GET_DATAWEDGE_STATUS"),
        GET_SCANNER_STATUS("com.symbol.datawedge.api.GET_SCANNER_STATUS"),
        REGISTER_FOR_NOTIFICATION("com.symbol.datawedge.api.REGISTER_FOR_NOTIFICATION"),
        UNREGISTER_FOR_NOTIFICATION("com.symbol.datawedge.api.UNREGISTER_FOR_NOTIFICATION"),
        ENUMERATE_SCANNERS("com.symbol.datawedge.api.ENUMERATE_SCANNERS");

        private final String value;
        ActionExtraKeys(String value) { this.value = value; }
        public String getValue() { return value; }
    }

    @Keep
    public enum ResultCategoryNames {
        CATEGORY_DEFAULT("android.intent.category.DEFAULT");

        private final String value;
        ResultCategoryNames(String value) { this.value = value; }
        public String getValue() { return value; }
    }

    @Keep
    public enum ResultActionNames {
        RESULT_ACTION("com.symbol.datawedge.api.RESULT_ACTION"),
        SCAN_RESULT_ACTION("com.zebra.emdk_kotlin_wrapper.SCAN_RESULT_ACTION");

        private final String value;
        ResultActionNames(String value) { this.value = value; }
        public String getValue() { return value; }
    }

    @Keep
    public enum ResultExtraKeys {
        GET_DATAWEDGE_STATUS("com.symbol.datawedge.api.RESULT_GET_DATAWEDGE_STATUS"),
        GET_CONFIG("com.symbol.datawedge.api.RESULT_GET_CONFIG"),
        ENUMERATE_SCANNERS("com.symbol.datawedge.api.RESULT_ENUMERATE_SCANNERS"),
        SCANNER_STATUS("com.symbol.datawedge.api.RESULT_SCANNER_STATUS");

        private final String value;
        ResultExtraKeys(String value) { this.value = value; }
        public String getValue() { return value; }
    }

    @Keep
    public enum ConfigModeOptions {
        CREATE_IF_NOT_EXIST("CREATE_IF_NOT_EXIST"),
        OVERWRITE("OVERWRITE"),
        UPDATE("UPDATE");

        private final String value;
        ConfigModeOptions(String value) { this.value = value; }
        public String getValue() { return value; }
    }

    @Keep
    public enum ControlScannerInputPluginCommand {
        SUSPEND_PLUGIN("SUSPEND_PLUGIN"),
        RESUME_PLUGIN("RESUME_PLUGIN"),
        ENABLE_PLUGIN("ENABLE_PLUGIN"),
        DISABLE_PLUGIN("DISABLE_PLUGIN");

        private final String value;
        ControlScannerInputPluginCommand(String value) { this.value = value; }
        public String getValue() { return value; }
    }

    @Keep
    public enum ScannerStatus {
        UNKNOWN("UNKNOWN"),
        WAITING("WAITING"),
        SCANNING("SCANNING"),
        DISABLED("DISABLED"),
        CONNECTED("CONNECTED"),
        DISCONNECTED("DISCONNECTED");

        private final String value;
        ScannerStatus(String value) { this.value = value; }
        public String getValue() { return value; }
    }

    @Keep
    public enum ScannerIdentifiers {
        AUTO("AUTO"),
        INTERNAL_IMAGER("INTERNAL_IMAGER"),
        INTERNAL_LASER("INTERNAL_LASER"),
        INTERNAL_CAMERA("INTERNAL_CAMERA"),
        SERIAL_SSI("SERIAL_SSI"),
        BLUETOOTH_SSI("BLUETOOTH_SSI"),
        BLUETOOTH_RS5100("BLUETOOTH_RS5100"),
        BLUETOOTH_RS6000("BLUETOOTH_RS6000"),
        BLUETOOTH_DS2278("BLUETOOTH_DS2278"),
        BLUETOOTH_DS3678("BLUETOOTH_DS3678"),
        BLUETOOTH_DS8178("BLUETOOTH_DS8178"),
        BLUETOOTH_LI3678("BLUETOOTH_LI3678"),
        BLUETOOTH_ZEBRA("BLUETOOTH_ZEBRA"),
        PLUGABLE_SSI("PLUGABLE_SSI"),
        PLUGABLE_SSI_RS5000("PLUGABLE_SSI_RS5000"),
        USB_SSI_DS3608("USB_SSI_DS3608"),
        USB_TGCS_MP7000("USB_TGCS_MP7000"),
        USB_ZEBRA("USB_ZEBRA"),
        USB_ZEBRACRADLE("USB_ZEBRACRADLE"),
        USB_SNAPI_ZEBRA("USB_SNAPI_ZEBRA");

        private final String value;
        ScannerIdentifiers(String value) { this.value = value; }
        public String getValue() { return value; }
    }

    @Keep
    public enum SendResultOptions {
        NONE("NONE"),
        LAST_RESULT("LAST_RESULT"),
        COMPLETE_RESULT("COMPLETE_RESULT");

        private final String value;
        SendResultOptions(String value) { this.value = value; }
        public String getValue() { return value; }
    }

    @Keep
    public enum ResultCodes {
        APP_ALREADY_ASSOCIATED("APP_ALREADY_ASSOCIATED"),
        BUNDLE_EMPTY("BUNDLE_EMPTY"),
        DATAWEDGE_ALREADY_DISABLED("DATAWEDGE_ALREADY_DISABLED"),
        DATAWEDGE_ALREADY_ENABLED("DATAWEDGE_ALREADY_ENABLED"),
        DATAWEDGE_DISABLED("DATAWEDGE_DISABLED"),
        INPUT_NOT_ENABLED("INPUT_NOT_ENABLED"),
        OPERATION_NOT_ALLOWED("OPERATION_NOT_ALLOWED"),
        PARAMETER_INVALID("PARAMETER_INVALID"),
        PLUGIN_NOT_SUPPORTED("PLUGIN_NOT_SUPPORTED"),
        PLUGIN_BUNDLE_INVALID("PLUGIN_BUNDLE_INVALID"),
        PLUGIN_DISABLED_IN_CONFIG("PLUGIN_DISABLED_IN_CONFIG"),
        PROFILE_ALREADY_EXISTS("PROFILE_ALREADY_EXISTS"),
        PROFILE_ALREADY_SET("PROFILE_ALREADY_SET"),
        PROFILE_DISABLED("PROFILE_DISABLED"),
        PROFILE_HAS_APP_ASSOCIATION("PROFILE_HAS_APP_ASSOCIATION"),
        PROFILE_NAME_EMPTY("PROFILE_NAME_EMPTY"),
        PROFILE_NOT_FOUND("PROFILE_NOT_FOUND"),
        SCANNER_ALREADY_DISABLED("SCANNER_ALREADY_DISABLED"),
        SCANNER_ALREADY_ENABLED("SCANNER_ALREADY_ENABLED"),
        SCANNER_DISABLE_FAILED("SCANNER_DISABLE_FAILED"),
        SCANNER_ENABLE_FAILED("SCANNER_ENABLE_FAILED"),
        ERROR_INTENT_ACTION_NOT_MATCH("ERROR_INTENT_ACTION_NOT_MATCH"),
        ERROR_INTENT_NULL("ERROR_INTENT_NULL"),
        ERROR_NO_RESULT_INFO("ERROR_NO_RESULT_INFO"),
        ERROR_NO_DATAWEDGE_STATUS_IN_RESULT("ERROR_NO_DATAWEDGE_STATUS_IN_RESULT"),
        UNKNOWN("UNKNOWN");

        private final String value;
        ResultCodes(String value) { this.value = value; }
        public String getValue() { return value; }
    }

    @Keep
    public enum ResultInfoBundleKeys {
        PREVIOUS_DEFAULT_PROFILE("PREVIOUS_DEFAULT_PROFILE"),
        PREVIOUS_PROFILE("PREVIOUS_PROFILE"),
        PROFILE_NAME("PROFILE_NAME"),
        SOURCE_PROFILE_NAME("SOURCE_PROFILE_NAME"),
        RESULT_CODE("RESULT_CODE");

        private final String value;
        ResultInfoBundleKeys(String value) { this.value = value; }
        public String getValue() { return value; }
    }

    @Keep
    public enum SoftScanTriggerOptions {
        START_SCANNING("START_SCANNING"),
        STOP_SCANNING("STOP_SCANNING"),
        TOGGLE_SCANNING("TOGGLE_SCANNING");

        private final String value;
        SoftScanTriggerOptions(String value) { this.value = value; }
        public String getValue() { return value; }
    }

    @Keep
    public enum IntentDeliveryOptions {
        START_ACTIVITY(0),
        START_SERVICE(1),
        BROADCAST(2);

        private final int value;
        IntentDeliveryOptions(int value) { this.value = value; }
        public int getValue() { return value; }
    }

    @Keep
    public enum ScanInputModeOptions {
        SINGLE(1),
        UDI(2),
        MULTI_BARCODE(3),
        DOCUMENT_CAPTURE(5);

        private final int value;
        ScanInputModeOptions(int value) { this.value = value; }
        public int getValue() { return value; }
    }

    @Keep
    public enum WorkflowInputSourceOptions {
        IMAGER(1),
        CAMERA(2);

        private final int value;
        WorkflowInputSourceOptions(int value) { this.value = value; }
        public int getValue() { return value; }
    }

    @Keep
    public enum NotificationType {
        CONFIGURATION_UPDATE("CONFIGURATION_UPDATE"),
        PROFILE_SWITCH("PROFILE_SWITCH"),
        SCANNER_STATUS("SCANNER_STATUS"),
        WORKFLOW_STATUS("WORKFLOW_STATUS");

        private final String value;
        NotificationType(String value) { this.value = value; }
        public String getValue() { return value; }
    }

    @Keep
    public static final class ScanInputParams {
        public static final String MODE = "scanning_mode";
        public static final String MULTI_BARCODE_COUNT = "multi_barcode_count";
        public static final String SCANNER_SELECTION = "scanner_selection_by_identifier";
        public static final String TRIGGER_WAKEUP = "trigger-wakeup";
        public static final String ENABLED = "scanner_input_enabled";
    }

    @Keep
    public static final class WorkflowParams {
        public static final String SELECTED_NAME = "selected_workflow_name";

        public static final class Input {
            public static final String ENABLED = "workflow_input_enabled";
            public static final String SOURCE = "workflow_input_source";
        }

        public static final class FreeFormOCR {
            public static final String ENABLED = "workflow_free_form_ocr_enabled";
        }
    }

    @Keep
    public static final class ScanResult {
        public static final String SOURCE = "com.symbol.datawedge.source";
        public static final String TYPE = "com.symbol.datawedge.label_type";
        public static final String DATA = "com.symbol.datawedge.data_string";
        public static final String TIME = "com.symbol.datawedge.data_dispatch_time";
        public static final String SCANNER_ID = "com.symbol.datawedge.scanner_identifier";
        public static final String DECODE_MODE = "com.symbol.datawedge.decoded_mode";
    }

    @Keep
    public static final class App {
        public static final String PACKAGE_NAME = "PACKAGE_NAME";
        public static final String ACTIVITY_LIST = "ACTIVITY_LIST";
        public static final String APP_LIST = "APP_LIST";
    }

    @Keep
    public enum BarcodeTriggerMode {
        DISABLED(0),
        ENABLED(1);

        private final int value;
        BarcodeTriggerMode(int value) { this.value = value; }
        public int getValue() { return value; }
    }

    @Keep
    public static final class Plugin {
        public static final String NAME = "PLUGIN_NAME";
        public static final String CONFIG = "PLUGIN_CONFIG";

        public enum Input {
            BARCODE("BARCODE"),
            MSR("MSR"),
            RFID("RFID"),
            SERIAL("SERIAL"),
            VOICE("VOICE"),
            WORKFLOW("WORKFLOW");

            private final String value;
            Input(String value) { this.value = value; }
            public String getValue() { return value; }
        }

        public enum Processing {
            BDF("BDF"),
            ADF("ADF"),
            TOKEN("TOKEN");

            private final String value;
            Processing(String value) { this.value = value; }
            public String getValue() { return value; }
        }

        public enum Output {
            INTENT("INTENT"),
            KEYSTROKE("KEYSTROKE"),
            IP("IP");

            private final String value;
            Output(String value) { this.value = value; }
            public String getValue() { return value; }
        }

        public enum Utilities {
            DCP("DCP"),
            EKB("EKB");

            private final String value;
            Utilities(String value) { this.value = value; }
            public String getValue() { return value; }
        }

        public enum WorkflowName {
            LICENSE_PLATE("license_plate"),
            ID_SCANNING("id_scanning"),
            VIN_NUMBER("vin_number"),
            TIN_NUMBER("tin_number"),
            CONTAINER_SCANNING("container_scanning"),
            METER_READING("meter_reading"),
            FREE_FORM_CAPTURE("free_form_capture"),
            DOCUMENT_CAPTURE("document_capture"),
            PICKLIST_OCR("picklist_ocr"),
            FREE_FORM_OCR("free_form_ocr");

            private final String value;
            WorkflowName(String value) { this.value = value; }
            public String getValue() { return value; }
        }
    }

    @Keep
    public static final class DCPParams {
        public static final String ENABLED = "dcp_input_enabled";
        public static final String DOCK = "dcp_dock_button_on";
        public static final String MODE = "dcp_start_in";
        public static final String HIGH_POS = "dcp_highest_pos";
        public static final String LOW_POS = "dcp_lowest_pos";
        public static final String DRAG = "dcp_drag_detect_time";

        public static final class DockOptions {
            public static final String LEFT = "LEFT";
            public static final String RIGHT = "RIGHT";
            public static final String BOTH = "BOTH";
        }

        public static final class ModeOptions {
            public static final String FULLSCREEN = "FULLSCREEN";
            public static final String BUTTON = "BUTTON";
            public static final String BUTTON_ONLY = "BUTTON_ONLY";
        }
    }

    @Keep
    public static final class IntentParams {
        public static final String OUTPUT_ENABLED = "intent_output_enabled";
        public static final String ACTION = "intent_action";
        public static final String CATEGORY = "intent_category";
        public static final String DELIVERY = "intent_delivery";

        public enum DeliveryOptions {
            START_ACTIVITY(0),
            START_SERVICE(1),
            BROADCAST(2);

            private final int value;
            DeliveryOptions(int value) { this.value = value; }
            public int getValue() { return value; }
        }
    }

    @Keep
    public static final class Command {
        public static final String COMMAND = "COMMAND";
        public static final String COMMAND_IDENTIFIER = "COMMAND_IDENTIFIER";
        public static final String COMMAND_ID_CREATE_PROFILE = "COMMAND_ID_CREATE_PROFILE_123";
    }

    @Keep
    public static final class Result {
        public static final String SEND_RESULT = "SEND_RESULT";
        public static final String RESULT = "RESULT";
        public static final String RESULT_INFO = "RESULT_INFO";
        public static final String RESULT_CODE = "RESULT_CODE";
    }

    @Keep
    public static final class Profile {
        public static final String NAME = "PROFILE_NAME";
        public static final String CONFIG_MODE = "CONFIG_MODE";
        public static final String ENABLED = "PROFILE_ENABLED";
    }

    @Keep
    public static final class BundleParams {
        public static final String RESET_CONFIG = "RESET_CONFIG";
        public static final String PLUGIN_NAME = "PLUGIN_NAME";
        public static final String PARAM_LIST = "PARAM_LIST";
        public static final String OUTPUT_PLUGIN_NAME = "OUTPUT_PLUGIN_NAME";
    }

    @Keep
    public static final class BDFParams {
        public static final String ENABLED = "bdf_enabled";
        public static final String SEND_DATA = "bdf_send_data";
    }

    @Keep
    public static final class KeyStrokeParams {
        public static final String OUTPUT_ENABLED = "keystroke_output_enabled";
        public static final String ACTION_CHAR = "keystroke_action_char";
        public static final String DELAY_EXTENDED_ASCII = "keystroke_delay_extended_ascii";
        public static final String DELAY_CONTROL_CHARS = "keystroke_delay_control_chars";
    }

    @Keep
    public static final class OCRParams {
        public static final String TEMPLATE = "ocr_template";
        public static final String INVERSE = "inverse_ocr";
        public static final String ORIENTATION = "ocr_orientation";
        public static final String LINES = "ocr_lines";

        public static final class Decoder {
            public static final String ENABLE_OCR_A = "decoder_ocr_a";
            public static final String ENABLE_OCR_B = "decoder_ocr_b";
            public static final String ENABLE_MICR = "decoder_micr";
            public static final String ENABLE_US_CURRENCY = "decoder_us_currency";
            public static final String OCR_A_VARIANT = "ocr_a_variant";
            public static final String OCR_B_VARIANT = "ocr_b_variant";
        }

        public enum InverseOptions {
            REGULAR_ONLY(0),
            INVERSE_ONLY(1),
            AUTO(2);
            private final int value;
            InverseOptions(int value) { this.value = value; }
            public String getValue() { return String.valueOf(value); }
        }

        public enum OCRAVariant {
            FULL_ASCII(0),
            RESERVED_1(1),
            RESERVED_2(2),
            BANKING(3);
            private final int value;
            OCRAVariant(int value) { this.value = value; }
            public String getValue() { return String.valueOf(value); }
        }

        public enum OCRBVariant {
            FULL_ASCII(0),
            BANKING(1),
            LIMITED(2),
            TRAVEL_DOCUMENT_1(3),
            PASSPORT(4),
            ISBN_1(6),
            ISBN_2(7),
            TRAVEL_DOCUMENT_2(8),
            VISA_TYPE_A(9),
            VISA_TYPE_B(10),
            ICAO_TRAVEL_DOCUMENT(11);
            private final int value;
            OCRBVariant(int value) { this.value = value; }
            public String getValue() { return String.valueOf(value); }
        }

        public enum OrientationOptions {
            DEGREE_0(0),
            DEGREE_270(1),
            DEGREE_180(2),
            DEGREE_90(3),
            OMNIDIRECTIONAL(4);
            private final int value;
            OrientationOptions(int value) { this.value = value; }
            public String getValue() { return String.valueOf(value); }
        }

        public enum LinesOptions {
            LINE_1(1),
            LINE_2(2),
            LINE_3(3);
            private final int value;
            LinesOptions(int value) { this.value = value; }
            public String getValue() { return String.valueOf(value); }
        }

        public static final class Chars {
            public static final String MIN_CHARS = "ocr_min_chars";
            public static final String MAX_CHARS = "ocr_max_chars";
            public static final String CHAR_SUBSET = "ocr_subset";
            public static final String QUIET_ZONE = "ocr_quiet_zone";
            public static final String DEFAULT_SUBSET = "!\"#$%()*+,-./0123456789<>ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxyz\\^|";
        }

        public static final class CheckDigit {
            public static final String MODULUS = "ocr_check_digit_modulus";
            public static final String MULTIPLIER = "ocr_check_digit_multiplier";
            public static final String VALIDATION = "ocr_check_digit_validation";

            public enum ValidationOptions {
                NONE(0),
                PRODUCT_ADD_RL(1),
                DIGIT_ADD_RL(2),
                PRODUCT_ADD_LR(3),
                DIGIT_ADD_LR(4),
                PRODUCT_ADD_RL_SIMPLE(5),
                DIGIT_ADD_RL_SIMPLE(6),
                HEALTH_INDUSTRY(9);
                private final int value;
                ValidationOptions(int value) { this.value = value; }
                public String getValue() { return String.valueOf(value); }
            }
        }
    }

    public static CompletableFuture<Boolean> enableDW(Context context, boolean enabled) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        DWIntentFactory.callDWAPI(context, ActionExtraKeys.ENABLE_DATAWEDGE, enabled, (intent, ex) -> {
            if (ex == null) {
                future.complete(true);
            } else {
                if (enabled && ResultCodes.DATAWEDGE_ALREADY_ENABLED.getValue().equals(ex.getMessage())) {
                    future.complete(true);
                } else if (!enabled && ResultCodes.DATAWEDGE_ALREADY_DISABLED.getValue().equals(ex.getMessage())) {
                    future.complete(true);
                } else {
                    future.completeExceptionally(ex);
                }
            }
        });
        return future;
    }

    public static CompletableFuture<Boolean> sendGetDWStatusIntent(Context context) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        DWIntentFactory.callDWAPI(context, ActionExtraKeys.GET_DATAWEDGE_STATUS, "", (intent, ex) -> {
            if (ex == null) {
                if (ResultActionNames.RESULT_ACTION.getValue().equals(intent.getAction()) &&
                        intent.hasExtra(ResultExtraKeys.GET_DATAWEDGE_STATUS.getValue())) {
                    String result = intent.getStringExtra(ResultExtraKeys.GET_DATAWEDGE_STATUS.getValue());
                    boolean enabled = StringEnabled.ENABLED.getValue().equalsIgnoreCase(result);
                    future.complete(enabled);
                } else {
                    future.completeExceptionally(new Exception(ResultCodes.ERROR_NO_DATAWEDGE_STATUS_IN_RESULT.getValue()));
                }
            } else {
                future.completeExceptionally(ex);
            }
        });
        return future;
    }

    public static CompletableFuture<Boolean> sendSetConfigIntent(Context context, Bundle extra) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        DWIntentFactory.callDWAPI(context, ActionExtraKeys.SET_CONFIG, extra, (intent, ex) -> {
            if (ex == null) {
                future.complete(true);
            } else {
                future.completeExceptionally(ex);
            }
        });
        return future;
    }

    public static CompletableFuture<Bundle> sendGetConfigIntent(Context context, Bundle extra) {
        CompletableFuture<Bundle> future = new CompletableFuture<>();
        DWIntentFactory.callDWAPI(context, ActionExtraKeys.GET_CONFIG, extra, (intent, ex) -> {
            if (ex == null) {
                Bundle bundle = intent.getBundleExtra(ResultExtraKeys.GET_CONFIG.getValue());
                if (bundle != null) {
                    future.complete(bundle);
                } else {
                    future.completeExceptionally(new Exception("No Result"));
                }
            } else {
                future.completeExceptionally(ex);
            }
        });
        return future;
    }

    public static CompletableFuture<Boolean> sendCreateProfileIntent(Context context, String name) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        DWIntentFactory.callDWAPI(context, ActionExtraKeys.CREATE_PROFILE, name, (intent, ex) -> {
            if (ex == null) {
                future.complete(true);
            } else {
                future.completeExceptionally(ex);
            }
        });
        return future;
    }

    public static CompletableFuture<Boolean> sendDeleteProfileIntent(Context context, String name) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        DWIntentFactory.callDWAPI(context, ActionExtraKeys.DELETE_PROFILE, name, (intent, ex) -> {
            if (ex == null) {
                future.complete(true);
            } else {
                if (ResultCodes.PROFILE_NOT_FOUND.getValue().equals(ex.getMessage())) {
                    future.complete(true);
                } else {
                    future.completeExceptionally(ex);
                }
            }
        });
        return future;
    }

    public static CompletableFuture<Boolean> sendSwitchProfileIntent(Context context, String name) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        DWIntentFactory.callDWAPI(context, ActionExtraKeys.SWITCH_TO_PROFILE, name, (intent, ex) -> {
            if (ex == null) {
                future.complete(true);
            } else {
                if (ResultCodes.PROFILE_ALREADY_SET.getValue().equals(ex.getMessage())) {
                    future.complete(true);
                } else {
                    future.completeExceptionally(ex);
                }
            }
        });
        return future;
    }

    public static CompletableFuture<Boolean> sendSwitchScannerParamsIntent(Context context, Bundle bundle) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        DWIntentFactory.callDWAPI(context, ActionExtraKeys.SWITCH_SCANNER_PARAMS, bundle, (intent, ex) -> {
            if (ex == null) {
                future.complete(true);
            } else {
                future.completeExceptionally(ex);
            }
        });
        return future;
    }

    public static void registerNotification(Context context, NotificationType type, java.util.function.Consumer<Boolean> callback) {
        Bundle bundle = new Bundle();
        bundle.putString("com.symbol.datawedge.api.NOTIFICATION_TYPE", type.getValue());
        DWIntentFactory.callDWAPI(
                context,
                ActionExtraKeys.REGISTER_FOR_NOTIFICATION,
                bundle,
                (intent, ex) -> {
                    callback.accept(ex == null);
                });
    }

    public static void unregisterNotification(Context context, NotificationType type, java.util.function.Consumer<Boolean> callback) {
        Bundle bundle = new Bundle();
        bundle.putString("com.symbol.datawedge.api.NOTIFICATION_TYPE", type.getValue());
        DWIntentFactory.callDWAPI(
                context,
                ActionExtraKeys.UNREGISTER_FOR_NOTIFICATION,
                bundle,
                (intent, ex) -> {
                    callback.accept(ex == null);
                });
    }

    public static void softScanTrigger(Context context, SoftScanTriggerOptions option) {
        DWIntentFactory.callDWAPI(context, ActionExtraKeys.SOFT_SCAN_TRIGGER, option.getValue(), (intent, ex) -> {});
    }

    public static CompletableFuture<ScannerStatus> sendGetSelectedScannerStatusIntent(Context context) {
        CompletableFuture<ScannerStatus> future = new CompletableFuture<>();
        DWIntentFactory.callDWAPI(context, ActionExtraKeys.GET_SCANNER_STATUS, "", (intent, ex) -> {
            if (ex == null) {
                String statusStr = intent.getStringExtra(ResultExtraKeys.SCANNER_STATUS.getValue());
                try {
                    future.complete(ScannerStatus.valueOf(statusStr));
                } catch (Exception e) {
                    future.complete(ScannerStatus.UNKNOWN);
                }
            } else {
                future.completeExceptionally(ex);
            }
        });
        return future;
    }

    public static CompletableFuture<java.util.List<DWScannerMap.DWScannerInfo>> sendEnumerateScannersIntent(Context context) {
        CompletableFuture<java.util.List<DWScannerMap.DWScannerInfo>> future = new CompletableFuture<>();
        DWIntentFactory.callDWAPI(context, ActionExtraKeys.ENUMERATE_SCANNERS, "", (intent, ex) -> {
            if (ex == null) {
                java.util.List<Bundle> scannerBundles = intent.getParcelableArrayListExtra(ResultExtraKeys.ENUMERATE_SCANNERS.getValue());
                java.util.List<DWScannerMap.DWScannerInfo> scannerList = new java.util.ArrayList<>();
                if (scannerBundles != null) {
                    for (Bundle bundle : scannerBundles) {
                        String name = bundle.getString("SCANNER_NAME");
                        String id = bundle.getString("SCANNER_IDENTIFIER");
                        int index = bundle.getInt("SCANNER_INDEX");
                        boolean connected = bundle.getBoolean("SCANNER_CONNECTION_STATE");
                        DWScannerMap.DWScannerInfo info = new DWScannerMap.DWScannerInfo(id, name, index, connected);
                        scannerList.add(info);
                        DWScannerMap.setScannerInfo(id, name, index, connected);
                    }
                }
                future.complete(scannerList);
            } else {
                future.completeExceptionally(ex);
            }
        });
        return future;
    }

    public static void sendControlScannerInputPluginIntent(Context context, ControlScannerInputPluginCommand command) {
        DWIntentFactory.callDWAPI(context, ActionExtraKeys.SCANNER_INPUT_PLUGIN, command.getValue(), (intent, ex) -> {});
    }
}
