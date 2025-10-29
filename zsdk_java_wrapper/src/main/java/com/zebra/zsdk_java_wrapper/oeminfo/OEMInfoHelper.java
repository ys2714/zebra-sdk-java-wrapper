package com.zebra.zsdk_java_wrapper.oeminfo;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.zebra.zsdk_java_wrapper.mx.MXBase;
import com.zebra.zsdk_java_wrapper.mx.MXConst;
import com.zebra.zsdk_java_wrapper.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class OEMInfoHelper {

    private static final String TAG = "OEMInfoHelper";



    private MXBase.EventListener listener = null;

    public OEMInfoHelper(MXBase.EventListener listener) {
        this.listener = listener;
    }

    // this should run in background thread.
    public static String getOEMInfo(Context ctx, String serviceId) {
        String result = null;
        Cursor cursor = ctx.getContentResolver().query(Uri.parse(serviceId), null, null, null, null);
        if (cursor == null) {
            return null;
        } else if (cursor.getCount() < 1){
            cursor.close();
            return null;
        } else {
            cursor.moveToFirst();
            if(cursor.getColumnCount() > 0) {
                result = cursor.getString(0);
            }
            cursor.close();
            return result;
        }
    }

    public void writeDataToExternalStorage(Context context, String filename, String data) {
        FileUtils.saveTextToDownloads(context, filename, data);
    }
}
