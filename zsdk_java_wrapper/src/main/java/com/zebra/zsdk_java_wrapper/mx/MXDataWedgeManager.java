package com.zebra.zsdk_java_wrapper.mx;

import android.content.Context;
import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

public class MXDataWedgeManager {

    /**
     * https://techdocs.zebra.com/mx/datawedgemgr/
     *
     * XML:
     * profile_datawedge_manager_import_profile.xml
     *
     * Path:
     * /data/tmp/public/dwprofile_ocr_workflow.db
     */
    public static void importProfile(
            Context context,
            String profileFileName,
            long delaySeconds,
            Consumer<MXBase.ErrorInfo> callback) {
        
        Map<String, String> params = Collections.singletonMap(
                MXConst.ConfigurationFile,
                profileFileName
        );

        MXProfileProcessor.processProfileWithCallback(
                context,
                MXBase.ProfileXML.DataWedgeManagerImportProfile,
                MXBase.ProfileName.DataWedgeManagerImportProfile,
                params,
                delaySeconds,
                callback
        );
    }

    /**
     * Overload for importProfile with default delaySeconds = 0.
     */
    public static void importProfile(
            Context context,
            String profileFileName,
            Consumer<MXBase.ErrorInfo> callback) {
        importProfile(context, profileFileName, 0L, callback);
    }
}
