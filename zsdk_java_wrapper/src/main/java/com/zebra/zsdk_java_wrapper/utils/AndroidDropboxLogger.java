package com.zebra.zsdk_java_wrapper.utils;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.os.DropBoxManager;
import android.util.Log;

import androidx.annotation.Keep;

import java.util.Date;
import java.util.Locale;

/**
 * only system app or platform key signed app can call dropbox log related APIs.
 *
 * <uses-permission android:name="android.permission.READ_LOGS" />
 * <uses-permission android:name="android.permission.READ_DROPBOX_DATA" />
 * <uses-permission android:name="android.permission.PACKAGE_USAGE_STATS" />
 * */
@Keep
public class AndroidDropboxLogger {

    @Keep
    public void getDropBoxLogs(Context context) {
        DropBoxManager dropBoxManager = (DropBoxManager) context.getSystemService(Context.DROPBOX_SERVICE);

        if (dropBoxManager == null) {
            Log.e("DropBoxReader", "Could not get DropBoxManager service.");
            return;
        }

        // A timestamp to start from, e.g., 0L for all logs since the epoch.
        long timestamp = 0L;
        StringBuilder logStringBuilder = new StringBuilder();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        Log.d("DropBoxReader", "Starting to read DropBox entries...");

        while (true) {
            // Get the next entry after the given timestamp
            DropBoxManager.Entry entry = dropBoxManager.getNextEntry(null, timestamp);

            if (entry == null) {
                Log.d("DropBoxReader", "No more entries found.");
                break; // Exit the loop when there are no more entries
            }

            try {
                Date entryTime = new Date(entry.getTimeMillis());
                String entryTag = entry.getTag();

                logStringBuilder.append("========================================\n");
                logStringBuilder.append("Tag: ").append(entryTag).append("\n");
                logStringBuilder.append("Time: ").append(dateFormat.format(entryTime)).append("\n");
                logStringBuilder.append("----------------------------------------\n");

                // Read the text content of the entry
                String entryText = entry.getText(500); // Read up to 500 characters

                if (entryText != null) {
                    logStringBuilder.append(entryText);
                    logStringBuilder.append("\n");
                } else {
                    logStringBuilder.append("(No text content or content is not text)\n");
                }

                // Update the timestamp to the current entry's time to get the next one
                timestamp = entry.getTimeMillis();

            } finally {
                // Always close the entry to release resources
                entry.close();
            }
        }

        // Print all collected logs to Logcat
        if (logStringBuilder.length() > 0) {
            Log.i("DropBoxLogs", logStringBuilder.toString());
        } else {
            Log.i("DropBoxLogs", "No DropBox logs were found.");
        }
    }
}
