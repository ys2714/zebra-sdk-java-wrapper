package com.zebra.zsdk_java_wrapper.mx;

import android.content.Context;
import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

/**
 * https://techdocs.zebra.com/mx/filemgr/
 *
 * XML:
 * profile_file_manager_copy_embedded_free_form_ocr.xml
 *
 * targetPathAndFileName:
 * /data/tmp/public/dwprofile_ocr_workflow.db
 * */
public class MXFileManager {

    /**
     * https://techdocs.zebra.com/mx/filemgr/
     *
     * XML:
     * profile_file_manager_copy_embedded_free_form_ocr.xml
     *
     * targetPathAndFileName:
     * /data/tmp/public/dwprofile_ocr_workflow.db
     * */
    public static void copyEmbeddedFreeFormOCRProfile(
            Context context,
            String targetPathAndFileName,
            long delaySeconds,
            Consumer<MXBase.ErrorInfo> callback) {

        Map<String, String> params = Collections.singletonMap(
                MXConst.TargetPathAndFileName,
                targetPathAndFileName
        );

        MXProfileProcessor.processProfileWithCallback(
                context,
                MXBase.ProfileXML.FileManagerCopyEmbeddedFreeFormOCR,
                MXBase.ProfileName.FileManagerCopyEmbeddedFreeFormOCR,
                params,
                delaySeconds,
                callback
        );
    }

    /**
     * Overload for copyEmbeddedFreeFormOCRProfile with default delaySeconds = 0.
     */
    public static void copyEmbeddedFreeFormOCRProfile(
            Context context,
            String targetPathAndFileName,
            Consumer<MXBase.ErrorInfo> callback) {
        copyEmbeddedFreeFormOCRProfile(context, targetPathAndFileName, 0L, callback);
    }
}
