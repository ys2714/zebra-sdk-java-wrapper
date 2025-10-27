package com.zebra.zsdk_java_wrapper.oeminfo;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.zebra.zsdk_java_wrapper.mx.MXBase;
import com.zebra.zsdk_java_wrapper.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class OEMInfoHelper implements RetrieveOemInfo.OnOemInfoRetrievedListener {

    private static final String SERIAL_URI = "content://oem_info/oem.zebra.secure/build_serial";
    private static final String IMEI_URI = "content://oem_info/wan/imei";
    private static final Uri[] CONTENT_PROVIDER_URIS = {
            Uri.parse(SERIAL_URI)
            // Uri.parse(IMEI_URI)
    };
    // Content Provider Keys
    private static final String IMEI = "imei";
    private static final String BUILD_SERIAL = "build_serial";

    private RetrieveOemInfo currentTask = null;
    private MXBase.EventListener listener = null;

    public OEMInfoHelper(MXBase.EventListener listener) {
        this.listener = listener;
    }

    public void getSerialNumber(Context ctx) {
        if (currentTask != null) {
            currentTask.cancel(true);
            currentTask = null;
        }
        currentTask = new RetrieveOemInfo(ctx, new Uri[] {
                Uri.parse(SERIAL_URI)
        }, this);
        currentTask.execute();
    }

    public void writeDataToExternalStorage(Context context, String filename, String data) {
        FileUtils.saveTextToDownloads(context, filename, data);
    }

    @Override
    public void onDetailsRetrieved(Map<String, String> oemIdentifiers) {
        for (String key : oemIdentifiers.keySet()) {
            String value = oemIdentifiers.get(key);
            Log.i("", "OEM Info | " + key + "|" + value);
            if (key.equals(IMEI)) {
                listener.onEMDKFetchContentProviderSuccess(IMEI_URI, value);
            } else if (key.equals(BUILD_SERIAL)) {
                listener.onEMDKFetchContentProviderSuccess(SERIAL_URI, value);
            }
        }
    }

    @Override
    public void onPermissionError(String e) {

    }

    @Override
    public void onUnknownError(String e) {

    }
}
