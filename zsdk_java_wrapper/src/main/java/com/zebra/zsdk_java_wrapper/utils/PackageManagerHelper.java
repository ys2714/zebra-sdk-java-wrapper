package com.zebra.zsdk_java_wrapper.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.SigningInfo;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

public class PackageManagerHelper {

    public static String getPackageSignature(Context cx) {
        try {
            String hex = getSigningCertBase64(cx);
            return hex;
        } catch (Exception e) {
            Log.e("", e.getLocalizedMessage());
            return "";
        }
    }

    public static String getSigningCertBase64(Context cx) throws PackageManager.NameNotFoundException {
        Signature sig = getSigningCertificateHex(cx)[0];

        byte[] byteArray = sig.toByteArray();

        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }

    @SuppressLint("PackageManagerGetSignatures")
    public static Signature[] getSigningCertificateHex(Context cx)
            throws PackageManager.NameNotFoundException {
        Signature[] sigs;
        SigningInfo signingInfo;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            signingInfo = cx.getPackageManager().getPackageInfo(cx.getPackageName(), PackageManager.GET_SIGNING_CERTIFICATES).signingInfo;
            sigs = signingInfo.getApkContentsSigners();
        } else {
            sigs = cx.getPackageManager().getPackageInfo(cx.getPackageName(), PackageManager.GET_SIGNATURES).signatures;
        }
        return sigs;
    }
}
