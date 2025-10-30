package com.zebra.zsdk_java_wrapper.oeminfo;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

/**
 * A utility class for retrieving OEM information from the device.
 * This class should not be instantiated.
 */
public final class OEMInfoHelper {

    private static final String TAG = OEMInfoHelper.class.getSimpleName();

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private OEMInfoHelper() {
        // This class is not meant to be instantiated.
    }

    /**
     * Retrieves OEM information, such as serial number or IMEI, from a content provider.
     * This method should be called from a background thread to avoid blocking the UI.
     *
     * @param ctx       The context to use for accessing the content resolver.
     * @param serviceId The URI of the content provider service.
     * @return The requested OEM information as a String, or null if it cannot be retrieved.
     */
    public static String getOEMInfo(Context ctx, String serviceId) {
        // Use try-with-resources to ensure the cursor is automatically closed.
        try (Cursor cursor = ctx.getContentResolver().query(Uri.parse(serviceId), null, null, null, null)) {
            // Check if the cursor is valid and contains at least one row.
            if (cursor != null && cursor.moveToFirst() && cursor.getColumnCount() > 0) {
                return cursor.getString(0);
            }
        } catch (Exception e) {
            // Log any exceptions that occur during the query.
            Log.e(TAG, "Error querying OEM info for service: " + serviceId, e);
        }
        // Return null if the query fails or no data is found.
        return null;
    }
}
