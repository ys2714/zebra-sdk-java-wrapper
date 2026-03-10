package com.zebra.zsdk_java_wrapper.dw;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * https://techdocs.zebra.com/datawedge/7-5/guide/api/setconfig/#scannerinputparameters
 * */
@Keep
public class DWBarcodeScanner extends DWVirtualScanner {

    @Keep
    public enum DecoderType {
        AZTEC("decoder_aztec"),
        CODE39("decoder_code39"),
        CODE_128("decoder_code128"),
        NW7("decoder_codabar"),
        QR("decoder_qrcode"),
        ITF("decoder_i2of5"),
        PDF_417("decoder_pdf417"),
        MICRO_PDF("decoder_micropdf"),
        DATA_MATRIX("decoder_datamatrix"),
        JAN_EAN_8("decoder_ean8"),
        JAN_EAN_13("decoder_ean13"),
        UPCA("decoder_upca"),
        UPCE0("decoder_upce0"),
        UPCE1("decoder_upce1"),
        MAILMARK("decoder_mailmark"),
        MAXICODE("decoder_maxicode"),
        GS1_DATABAR("decoder_gs1_databar"),
        GS1_DATABAR_EXP("decoder_gs1_databar_exp"),
        CODABAR("decoder_codabar");

        private final String key;

        DecoderType(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    /**
     * aim_type:
     *
     * 0 - Trigger
     * 1 - Timed Hold
     * 2 - Timed Release
     * 3 - Press And Release
     * 4 - Presentation
     * 5 - Continuous Read
     * 6 - Press and Sustain
     * 7 – Press and Continue
     * 8 - Timed Continuous
     * */
    @Keep
    public enum AimType {
        TRIGGER(0),
        TIMED_HOLD(1),
        TIMED_RELEASE(2),
        PRESS_AND_RELEASE(3),
        PRESENTATION(4),
        CONTINUOUS_READ(5),
        PRESS_AND_SUSTAIN(6),
        PRESS_AND_CONTINUE(7),
        TIMED_CONTINUOUS(8);

        private final int value;

        AimType(int value) {
            this.value = value;
        }

        public String getString() {
            return String.valueOf(value);
        }
    }

    public DWBarcodeScanner(@NonNull Context context) {
        super(context);
    }

    @Override
    @NonNull
    public String getCreateJSONFileName() {
        return "barcode_intent_advanced_create.json";
    }

    @Override
    @NonNull
    public String getUpdateJSONFileName() {
        return "barcode_intent_advanced_update.json";
    }

    @Override
    @NonNull
    public Map<String, String> getParameters() {
        Map<String, String> params = new HashMap<>();
        params.put("PROFILE_NAME", "DWBarcodeScanner");
        params.put("scanner_input_enabled", "true");
        params.put("workflow_input_enabled", "false");
        params.put("barcode_trigger_mode", "0");
        params.put("aim_type", "0");
        params.put("aim_timer", "6000");
        params.put("beam_timer", "6000");
        return params;
    }

    @Keep
    @NonNull
    public DWBarcodeScanner switchAimType(@NonNull AimType aimType) {
        Bundle bundle = new Bundle();
        bundle.putString("aim_type", aimType.getString());
        DataWedgeHelper.getInstance().switchScannerParams(context, bundle, null);
        return this;
    }

    @Keep
    @NonNull
    public DWBarcodeScanner switchDecoderType(@NonNull DecoderType[] decoderTypes) {
        Bundle bundle = new Bundle();
        for (DecoderType type : DecoderType.values()) {
            bundle.putString(type.getKey(), "false");
        }
        for (DecoderType type : decoderTypes) {
            bundle.putString(type.getKey(), "true");
        }
        DataWedgeHelper.getInstance().switchScannerParams(context, bundle, null);
        return this;
    }
}
