package com.zebra.zsdk_java_wrapper.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();

    /**
     * Saves a text string to a file in the public "Download" directory.
     * This method is compatible with Android 10 (API 29) and newer versions,
     * using the MediaStore API to comply with Scoped Storage policies.
     *
     * @param context   The context to access ContentResolver.
     * @param fileName  The desired name of the file (e.g., "my-document.txt").
     * @param fileContent The text content to write into the file.
     */
    public static void saveTextToDownloads(Context context, String fileName, String fileContent) {
        // The implementation for Android 10 (API 29) and above is unified using MediaStore.
        // There is no need for conditional logic for versions 10, 11, 12, 13, or 14
        // as this approach is the standard for all of them.

        // Use ContentResolver to interact with the MediaStore.
        ContentResolver resolver = context.getContentResolver();

        // ContentValues stores the file's metadata.
        ContentValues contentValues = new ContentValues();

        // Set the file's display name. This is the name shown to the user.
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);

        // Set the file's MIME type.
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "text/plain");

        // Set the relative path for the file within the Downloads directory.
        // This ensures the file is placed in the correct public folder.
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

        // Get the URI for the MediaStore's Downloads collection.
        Uri collectionUri = MediaStore.Downloads.EXTERNAL_CONTENT_URI;

        // Insert the new file metadata into the MediaStore, which returns a URI for the new file.
        Uri fileUri = resolver.insert(collectionUri, contentValues);

        if (fileUri == null) {
            // This can happen if the system fails to create the file entry.
            Toast.makeText(context, "Failed to create file in Downloads", Toast.LENGTH_SHORT).show();
            return;
        }

        // Use a try-with-resources block to automatically close the OutputStream.
        try (OutputStream outputStream = resolver.openOutputStream(fileUri)) {
            if (outputStream == null) {
                throw new IOException("Failed to open output stream for " + fileUri);
            }
            // Write the text content to the file.
            outputStream.write(fileContent.getBytes(StandardCharsets.UTF_8));
            Toast.makeText(context, "File saved to Downloads folder", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            // Handle potential I/O errors, for example, if storage is full.
            Log.d(TAG, e.getMessage());
            Toast.makeText(context, "Error saving file: " + e.getMessage(), Toast.LENGTH_LONG).show();

            // If an error occurs, it's good practice to delete the incomplete file entry.
            resolver.delete(fileUri, null, null);
        }
    }
}
