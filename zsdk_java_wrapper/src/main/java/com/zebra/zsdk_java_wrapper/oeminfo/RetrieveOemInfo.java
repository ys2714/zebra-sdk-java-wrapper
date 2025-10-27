package com.zebra.zsdk_java_wrapper.oeminfo;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class RetrieveOemInfo extends AsyncTask<Void, Void, Void> {

    // debugging
    private static final String TAG = "RetrieveOemInfo";

    // Main Thread Handler
    private Handler mHandler = new Handler(Looper.getMainLooper());

    // Variables
    private WeakReference<Context> mContextWeakRef;
    private Uri[] mContentProviderUris;
    private OnOemInfoRetrievedListener mOnOemInfoRetrievedListener;

    public RetrieveOemInfo(Context context, Uri[] contentProviderUris,
                           OnOemInfoRetrievedListener onOemInfoRetrievedListener) {
        this.mContextWeakRef = new WeakReference<>(context);
        this.mContentProviderUris = contentProviderUris;
        this.mOnOemInfoRetrievedListener = onOemInfoRetrievedListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mHandler.post(() ->  {
            // run on main thread
        });
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Context cx = mContextWeakRef.get();
        if (cx != null) {
            // Init Holder
            Map<String, String> oemIdentifiers = new HashMap<>();

            // Loop Content Providers
            for (Uri contentProviderUri : mContentProviderUris) {
                // Grab Cursor using URI
                Cursor cursor = cx.getContentResolver().query(contentProviderUri, null,
                        null, null, null);

                // Validate Permissions
                if (cursor == null || cursor.getCount() < 1) {
                    mHandler.post(() -> mOnOemInfoRetrievedListener.onPermissionError("Permission Error"));
                    return null;
                }

                // Loop Cursor
                while (cursor.moveToNext()) {
                    // Validate Cursor
                    if (cursor.getColumnCount() == 0) {
                        mHandler.post(() -> mOnOemInfoRetrievedListener.onUnknownError("Cursor count is 0"));
                    } else {
                        // Loop Cursor Columns
                        for (int i = 0; i < cursor.getColumnCount(); i++) {
                            try {
                                oemIdentifiers.put(cursor.getColumnName(i), cursor.getString(
                                        cursor.getColumnIndex(cursor.getColumnName(i))));
                            } catch (Exception e) {
                                mHandler.post(() ->
                                        mOnOemInfoRetrievedListener.onUnknownError(e.getMessage()));
                            }
                        }
                    }
                }

                // Close Cursor
                cursor.close();
            }

            // Return Values
            mHandler.post(() -> mOnOemInfoRetrievedListener.onDetailsRetrieved(oemIdentifiers));
        } else {
            Log.e(TAG, "Could not access valid context, quitting.");
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mHandler.post(() -> {
            //run on main thread
        });
    }

    public interface OnOemInfoRetrievedListener {
        void onDetailsRetrieved(Map<String, String> oemIdentifiers);
        void onPermissionError(String e);
        void onUnknownError(String e);
    }

}
