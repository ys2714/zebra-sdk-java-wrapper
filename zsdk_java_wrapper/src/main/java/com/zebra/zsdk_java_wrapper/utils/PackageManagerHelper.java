package com.zebra.zsdk_java_wrapper.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.SigningInfo;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

/**
 * A utility class for retrieving package information, such as the application's signature.
 * This class cannot be instantiated.
 */
public final class PackageManagerHelper {

    private static final String TAG = PackageManagerHelper.class.getSimpleName();

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private PackageManagerHelper() {
        // This class is not meant to be instantiated.
    }

    /**
     * Retrieves the Base64-encoded signature of the application package.
     *
     * @param context The context to use for accessing the package manager.
     * @return The Base64-encoded signature as a String, or an empty string if an error occurs.
     */
    public static String getPackageSignature(Context context) {
        try {
            Signature[] signatures = getSigningCertificates(context);
            // A package must be signed, so the signature array should not be empty.
            if (signatures != null && signatures.length > 0) {
                // We are interested in the first signature.
                Signature signature = signatures[0];
                return Base64.encodeToString(signature.toByteArray(), Base64.NO_WRAP);
            } else {
                Log.w(TAG, "No signatures found for package: " + context.getPackageName());
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Failed to get package signature. Package not found.", e);
        }
        return "";
    }

    /**
     * Retrieves the signing certificates for the current application package.
     * This method handles the differences between modern (API 28+) and legacy Android versions.
     *
     * @param context The context to use for accessing the package manager.
     * @return An array of {@link Signature} objects.
     * @throws PackageManager.NameNotFoundException if the package name is not found.
     */
    @SuppressLint("PackageManagerGetSignatures")
    private static Signature[] getSigningCertificates(Context context)
            throws PackageManager.NameNotFoundException {

        String packageName = context.getPackageName();
        PackageManager pm = context.getPackageManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // For Android P (API 28) and above, use GET_SIGNING_CERTIFICATES
            PackageInfo packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES);
            SigningInfo signingInfo = packageInfo.signingInfo;
            return signingInfo.getApkContentsSigners();
        } else {
            // For older versions, use the deprecated GET_SIGNATURES
            PackageInfo packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            return packageInfo.signatures;
        }
    }
}
