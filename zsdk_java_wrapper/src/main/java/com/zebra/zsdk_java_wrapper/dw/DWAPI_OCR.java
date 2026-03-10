package com.zebra.zsdk_java_wrapper.dw;

import android.os.Bundle;

public class DWAPI_OCR {

    private DWAPI_OCR() {}

    public static Bundle configOCRParams(Bundle bundle) {
        // Decoder
        bundle.putString(DWAPI.OCRParams.Decoder.ENABLE_OCR_A, DWAPI.StringBoolean.TRUE.getValue());
        bundle.putString(DWAPI.OCRParams.Decoder.ENABLE_OCR_B, DWAPI.StringBoolean.TRUE.getValue());
        bundle.putString(DWAPI.OCRParams.Decoder.ENABLE_MICR, DWAPI.StringBoolean.TRUE.getValue());
        bundle.putString(DWAPI.OCRParams.Decoder.ENABLE_US_CURRENCY, DWAPI.StringBoolean.TRUE.getValue());
        bundle.putString(DWAPI.OCRParams.Decoder.OCR_A_VARIANT, DWAPI.OCRParams.OCRAVariant.FULL_ASCII.getValue());
        bundle.putString(DWAPI.OCRParams.Decoder.OCR_B_VARIANT, DWAPI.OCRParams.OCRBVariant.ISBN_1.getValue());
        // Orientation
        bundle.putString(DWAPI.OCRParams.ORIENTATION, DWAPI.OCRParams.OrientationOptions.DEGREE_0.getValue());
        // Lines
        bundle.putString(DWAPI.OCRParams.LINES, DWAPI.OCRParams.LinesOptions.LINE_1.getValue());
        // Chars
        bundle.putString(DWAPI.OCRParams.Chars.MIN_CHARS, "3");
        bundle.putString(DWAPI.OCRParams.Chars.MAX_CHARS, "100");
        bundle.putString(DWAPI.OCRParams.Chars.CHAR_SUBSET, DWAPI.OCRParams.Chars.DEFAULT_SUBSET);
        bundle.putString(DWAPI.OCRParams.Chars.QUIET_ZONE, "60");
        // Template
        bundle.putString(DWAPI.OCRParams.TEMPLATE, "99999999");
        // Check Digit
        bundle.putString(DWAPI.OCRParams.CheckDigit.MODULUS, "10");
        bundle.putString(DWAPI.OCRParams.CheckDigit.MULTIPLIER, "121212121212");
        bundle.putString(DWAPI.OCRParams.CheckDigit.VALIDATION, DWAPI.OCRParams.CheckDigit.ValidationOptions.PRODUCT_ADD_LR.getValue());
        // Inverse
        bundle.putString(DWAPI.OCRParams.INVERSE, DWAPI.OCRParams.InverseOptions.AUTO.getValue());
        return bundle;
    }
}
