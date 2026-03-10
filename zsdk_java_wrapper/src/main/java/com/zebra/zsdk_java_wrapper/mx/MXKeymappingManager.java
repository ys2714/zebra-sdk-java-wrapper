package com.zebra.zsdk_java_wrapper.mx;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class MXKeymappingManager {

    public static void remappingAllKeyToDefault(Context context,
                                         long delaySeconds,
                                         Consumer<MXBase.ErrorInfo> callback) {
        MXProfileProcessor.processProfileWithCallback(
                context,
                MXBase.ProfileXML.KeymappingManagerSetAllToDefault,
                MXBase.ProfileName.KeymappingManagerSetAllToDefault,
                null,
                delaySeconds,
                callback
        );
    }

    public static void remappingKeyToSendIntent(Context context,
                                         MXBase.KeyIdentifiers key,
                                         String intentAction,
                                         String intentCategory,
                                         long delaySeconds,
                                         Consumer<MXBase.ErrorInfo> callback) {
        Map<String, String> map = new HashMap<>();
        map.put(MXConst.KeyIdentifier, key.getString());
        map.put(MXConst.IntentAction, intentAction);
        map.put(MXConst.IntentCategory, intentCategory);
        MXProfileProcessor.processProfileWithCallback(
                context,
                MXBase.ProfileXML.KeymappingManagerSetKeySendIntent,
                MXBase.ProfileName.KeymappingManagerSetKeySendIntent,
                map,
                delaySeconds,
                callback
        );
    }
}
