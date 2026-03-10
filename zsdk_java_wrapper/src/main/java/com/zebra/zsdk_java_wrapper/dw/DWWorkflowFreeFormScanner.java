package com.zebra.zsdk_java_wrapper.dw;

import android.content.Context;
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import java.util.HashMap;
import java.util.Map;

@Keep
public class DWWorkflowFreeFormScanner extends DWVirtualScanner {

    public DWWorkflowFreeFormScanner(@NonNull Context context) {
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
        params.put("PROFILE_NAME", "DWWorkflowFreeFormScanner");
        params.put("scanner_input_enabled", "false");
        params.put("workflow_input_enabled", "true");
        params.put("barcode_trigger_mode", "0");
        params.put("aim_type", "8");
        params.put("aim_timer", "6000");
        params.put("beam_timer", "6000");
        return params;
    }
}
