package com.zebra.zsdk_java_wrapper.zdm;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.Keep;

@Keep
public class ZDMAuthHelper {

    public static final String COLUMN_QUERY_RESULT = "query_result";

    @Keep
    public static String acquireToken(Context context, ZDMConst.DelegationScope delegationScope) {
        Uri authorityUri = Uri.parse("content://com.zebra.devicemanager.zdmcontentprovider");
        Uri acquireTokenUri = Uri.withAppendedPath(authorityUri, "AcquireToken");

        if (acquireTokenUri == null) {
            return null;
        }

        String token = "";
        try {
            Cursor cursor = context.getContentResolver().query(
                    acquireTokenUri,
                    null,
                    "delegation_scope=?",
                    new String[]{delegationScope.getValue()},
                    null
            );
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(COLUMN_QUERY_RESULT);
                    if (columnIndex != -1) {
                        token = cursor.getString(columnIndex);
                    }
                }
                cursor.close();
            }
        } catch (Exception e) {
            if (e instanceof SecurityException) {
                Log.e("BroadcastProtection", "Invalid Token/Caller");
            } else {
                Log.e("BroadcastProtection", "Unknown Caller to acquire token");
            }
        }

        ZDMTokenStore.saveToken(delegationScope, token);
        return token;
    }
}
