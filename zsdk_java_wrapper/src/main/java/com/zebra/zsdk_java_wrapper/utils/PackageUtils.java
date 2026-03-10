package com.zebra.zsdk_java_wrapper.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.Keep;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A utility class for retrieving package information, such as the application's signature.
 */
@Keep
public class PackageUtils {

    private static final String TAG = PackageUtils.class.getSimpleName();

    @Keep
    public static String getPackageSignatureSHA1(Context context) {
        try {
            Signature[] signatures = getSigningCertificates(context);
            // A package must be signed, so the signature array should not be empty.
            if (signatures != null && signatures.length > 0) {
                // We are interested in the first signature.
                Signature signature = signatures[0];
                return getSHA1(signature.toByteArray());
            } else {
                Log.w(TAG, "No signatures found for package: " + context.getPackageName());
                return "";
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Failed to get package signature. Package not found.", e);
            return "";
        }
    }

    /**
     * Retrieves the Base64-encoded signature of the application package.
     *
     * @param context The context to use for accessing the package manager.
     * @return The Base64-encoded signature as a String, or an empty string if an error occurs.
     */
    @Keep
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
                return "";
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Failed to get package signature. Package not found.", e);
            return "";
        }
    }

    /**
     * Retrieves the signing certificates for the current application package.
     * This method handles the differences between modern (API 28+) and legacy Android versions.
     *
     * @param context The context to use for accessing the package manager.
     * @return An array of [Signature] objects.
     * @throws PackageManager.NameNotFoundException if the package name is not found.
     */
    @SuppressLint("PackageManagerGetSignatures")
    private static Signature[] getSigningCertificates(Context context) throws PackageManager.NameNotFoundException {
        String packageName = context.getPackageName();
        PackageManager pm = context.getPackageManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // For Android P (API 28) and above, use GET_SIGNING_CERTIFICATES
            PackageInfo packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES);
            if (packageInfo.signingInfo != null) {
                return packageInfo.signingInfo.getApkContentsSigners();
            } else {
                return new Signature[0];
            }
        } else {
            // For older versions, use the deprecated GET_SIGNATURES
            @SuppressWarnings("deprecation")
            PackageInfo packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            return packageInfo.signatures != null ? packageInfo.signatures : new Signature[0];
        }
    }

    private static String getSHA1(byte[] sig) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA1");
            digest.update(sig);
            byte[] hashtext = digest.digest();
            return bytesToHex(hashtext);
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "SHA1 algorithm not found", e);
            return "";
        }
    }

    private static String bytesToHex(byte[] bytes) {
        char[] hexArray = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8',
                '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
