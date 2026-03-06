package com.zebra.zsdk_java_wrapper.mx;

import android.content.Context;
import android.database.ContentObserver;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.EMDKManager.EMDKListener;
import com.symbol.emdk.EMDKResults;
import com.symbol.emdk.ProfileManager;
import com.zebra.zsdk_java_wrapper.oeminfo.OEMInfoHelper;
import com.zebra.zsdk_java_wrapper.utils.AssetsReader;
import com.zebra.zsdk_java_wrapper.utils.FixedSizeQueue;
import com.zebra.zsdk_java_wrapper.utils.PackageManagerHelper;
import com.zebra.zsdk_java_wrapper.utils.Result;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class MXProfileProcessor {

    static final String TAG = MXProfileProcessor.class.getSimpleName();

    // Executor for running tasks on a background thread
    private final ExecutorService backgroundExecutor = Executors.newSingleThreadExecutor();

    // Handler for posting results to the main/UI thread
    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    // You need a thread-safe collection to hold the listeners, as they can be
    // accessed from different threads (main thread, background thread, ProfileManager's thread).
    private final FixedSizeQueue<ProfileDataListener> listeners = new FixedSizeQueue<ProfileDataListener>(20);

    private class EMDKEventHandler implements EMDKListener {
        @Override
        public void onOpened(EMDKManager emdkManager) {
            MXProfileProcessor.this.emdkManager = emdkManager;
            profileManager = (ProfileManager) emdkManager.getInstance(EMDKManager.FEATURE_TYPE.PROFILE);
            listener.onEMDKSessionOpened();
        }

        @Override
        public void onClosed() {
            if (emdkManager != null) {
                emdkManager.release();
                emdkManager = null;
            }
            listener.onEMDKSessionClosed();
        }
    }

    private final EMDKEventHandler eventHandler = new EMDKEventHandler();
    private final MXBase.EventListener listener;

    private ProfileManager profileManager = null;
    private EMDKManager emdkManager = null;
    private Context context = null;

    public MXProfileProcessor(MXBase.EventListener listener) {
        this.listener = listener;
    }

    public void connectEMDK(Context ctx) {
        this.context = ctx;
        EMDKResults results = EMDKManager.getEMDKManager(ctx.getApplicationContext(), this.eventHandler);
        if (results.statusCode != EMDKResults.STATUS_CODE.SUCCESS) {
            Log.e(TAG, "EMDKManager object creation failed.");
            MXBase.ErrorInfo errorInfo = new MXBase.ErrorInfo();
            errorInfo.errorType = "EMDKManager";
            errorInfo.errorDescription = "EMDKManager object creation failed";
            this.listener.onEMDKError(errorInfo);
        } else {
            Log.d(TAG, "EMDKManager object creation success.");
        }
    }

    public void disconnectEMDK() {
        if (profileManager != null) {
            profileManager = null;
        }
        if (emdkManager != null) {
            emdkManager.release();
            emdkManager = null;
        }
    }

    public void processProfile(MXBase.ProfileXML profileResId,
                               MXBase.ProfileName profileName,
                               BiConsumer<String, MXBase.ErrorInfo> callback) {
        processProfile(profileResId, profileName, null, callback);
    }

    public void processProfile(MXBase.ProfileXML profileResId,
                               MXBase.ProfileName profileName,
                               Map<String, String> params,
                               BiConsumer<String, MXBase.ErrorInfo> callback) {
        new ProcessProfileTask(new ProcessProfileTask.Delegate() {
            @Override
            public EMDKResults processProfile() {
                if (profileManager == null) {
                    Log.e(TAG, "EMDK ProfileManager is not available.");
                    throw new RuntimeException("EMDK ProfileManager is not available.");
                }
                String command = AssetsReader.readFileToStringWithParams(context, profileResId.getString(), params);
                return profileManager.processProfile(profileName.getString(), ProfileManager.PROFILE_FLAG.SET, new String[]{command});
            }

            @Override
            public void processProfileSuccess() {
                if (callback != null) {
                    callback.accept(profileName.getString(), null);
                }
            }

            @Override
            public void processProfileFailure(MXBase.ErrorInfo errorInfo) {
                if (callback != null) {
                    callback.accept(null, errorInfo);
                }
            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void processProfileWithCallback(
            Context context,
            MXBase.ProfileXML fileName,
            MXBase.ProfileName profileName,
            Map<String, String> params,
            long delaySeconds,
            Consumer<MXBase.ErrorInfo> callback) {

        backgroundExecutor.execute(() -> {
            try {
                 MXBase.ErrorInfo result;

                if (fileName == MXBase.ProfileXML.None) {
                    // processProfile is a blocking call, so it runs on the background thread.
                    processProfile(context, profileName, null).whenComplete( (errorInfo, throwable) -> {
                        // Post the result back to the main thread, equivalent to foregroundScope.launch
                        mainThreadHandler.post(() -> callback.accept(errorInfo));
                    });
                } else {
                    // Assume fileName.getString() is the Java equivalent of fileName.string
                    String content = AssetsReader.readFileToStringWithParams(
                            context,
                            fileName.getString(),
                            params
                    );

                    // Use stripIndent() (Java 12+) as a replacement for trimIndent().
                    // For older Java versions, you would need a custom utility function.
                    String strippedContent = content.trim().strip();

                    String[] contentArray = new String[]{strippedContent};
                    processProfile(context, profileName, contentArray).whenComplete( (errorInfo, throwable) -> {
                        mainThreadHandler.post(() -> callback.accept(errorInfo));
                    });
                }

                // Equivalent of Kotlin's delay()
                if (delaySeconds > 0) {
                    Thread.sleep(delaySeconds * 1000);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore the interrupted status
                // Optionally handle the interruption, e.g., by logging or calling back with an error
                mainThreadHandler.post(() -> {
                    // You might want to create a specific ErrorInfo for this case
                    callback.accept(new MXBase.ErrorInfo("Operation was interrupted."));
                });
            } catch (Exception e) {
                // It's good practice to catch other potential exceptions from file reading or processing
                mainThreadHandler.post(() -> {
                    // You might want to create a specific ErrorInfo for this case
                    callback.accept(new MXBase.ErrorInfo("An error occurred: " + e.getMessage()));
                });
            }
        });
    }

    public CompletableFuture<MXBase.ErrorInfo> processProfile(
            Context context,
            MXBase.ProfileName profileName,
            String[] profileContent) {

        CompletableFuture<MXBase.ErrorInfo> future = new CompletableFuture<>();

        // The callback logic that will complete the future.
        // This is the equivalent of the lambda passed to ProfileDataListener in Kotlin.
        ProfileDataListener listener = new ProfileDataListener(profileName, (theListener, errorInfo) -> {
            // Ensure we don't complete a future that's already done (e.g., cancelled).
            if (!future.isDone()) {
                listeners.remove(theListener); // Use your queue's remove method.
                future.complete(errorInfo);
            }
        }, (theListener) -> {
            profileManager.removeDataListener(theListener);
        });

        // Handle cancellation of the future
        future.whenComplete((res, err) -> {
            if (future.isCancelled()) {
                listener.onDisposal(); // Clean up the listener if the operation is cancelled.
            }
        });

        // Add the listener and trigger the async operation
        listeners.enqueue(listener); // Use your queue's enqueue method.
        profileManager.addDataListener(listener);
        profileManager.processProfileAsync(
                profileName.getString(),
                ProfileManager.PROFILE_FLAG.SET,
                profileContent
        );

        return future;
    }

    /** Overload with default delaySeconds = 0 */
    public void processProfileWithCallback(
            Context context,
            MXBase.ProfileXML fileName,
            MXBase.ProfileName profileName,
            Map<String, String> params,
            Consumer<MXBase.ErrorInfo> callback) {
        processProfileWithCallback(context, fileName, profileName, params, 0L, callback);
    }

    /** Overload with default params = null and delaySeconds = 0 */
    public void processProfileWithCallback(
            Context context,
            MXBase.ProfileXML fileName,
            MXBase.ProfileName profileName,
            Consumer<MXBase.ErrorInfo> callback) {
        processProfileWithCallback(context, fileName, profileName, null, 0L, callback);
    }

    // OEMInfo

    public void fetchProductModelInBackground(Context ctx, BiConsumer<String, MXBase.ErrorInfo> callback) {
        fetchOEMInfoInBackground(ctx, MXConst.PRODUCT_MODEL_URI, callback);
    }

    public void fetchSerialNumberInBackground(Context ctx, BiConsumer<String, MXBase.ErrorInfo> callback) {
        fetchOEMInfoInBackground(ctx, MXConst.SERIAL_URI, callback);
    }

    public void fetchIMEIInBackground(Context ctx, BiConsumer<String, MXBase.ErrorInfo> callback) {
        fetchOEMInfoInBackground(ctx, MXConst.IMEI_URI, callback);
    }

    public void fetchTouchModeInBackground(Context ctx, BiConsumer<String, MXBase.ErrorInfo> callback) {
        if (Build.DEVICE.equals("TC26")) {
            fetchOEMInfoInBackground(ctx, MXConst.VENDOR_TOUCH_MODE_URI, callback);
        } else {
            fetchOEMInfoInBackground(ctx, MXConst.TOUCH_MODE_URI, callback);
        }
    }

    public void addOEMInfoChangeObserver(Context context, String serviceId, ContentObserver observer) {
        OEMInfoHelper.startObserveOEMInfoChange(context, serviceId, observer);
    }

    public void removeOEMInfoChangeObserver(Context context, ContentObserver observer) {
        OEMInfoHelper.stopObserveOEMInfoChange(context, observer);
    }

    public void fetchOEMInfoInBackground(Context ctx,
                                         String serviceId,
                                         BiConsumer<String, MXBase.ErrorInfo> callback) {
        new Thread(() -> {
            String serial = OEMInfoHelper.getOEMInfo(ctx, serviceId);
            if (serial == null) {
                getCallServicePermission(ctx, serviceId, (profileName, errorInfo) -> {
                    if (profileName != null && errorInfo == null) {
                        fetchOEMInfoInBackground(ctx, serviceId, callback);
                    } else {
                        callback.accept(null, errorInfo);
                    }
                });
            } else {
                mainThreadHandler.post(
                        () -> {
                            callback.accept(serial, null);
                        }
                );
            }
        }).start();
    }

    // AccessManager

    public void getAllDangerousPermissions(Context ctx,
                                           BiConsumer<String, MXBase.ErrorInfo> callback) {
        String base64 = PackageManagerHelper.getPackageSignature(ctx);
        String name = ctx.getPackageName();
        callAccessManagerAllowPermission(
                MXBase.EPermissionType.ALL_DANGEROUS_PERMISSIONS.toString(),
                name,
                "",
                base64,
                callback
        );
    }

    public void getCallServicePermission(Context ctx,
                                         String serviceId,
                                         BiConsumer<String, MXBase.ErrorInfo> callback) {
        String base64 = PackageManagerHelper.getPackageSignature(ctx);
        String name = ctx.getPackageName();
        callAccessManagerAllowCallService(serviceId, name, base64, callback);
    }

    public void callAccessManagerAllowCallService(String serviceIdentifier,
                                                  String callerPackageName,
                                                  String callerSignature,
                                                  BiConsumer<String, MXBase.ErrorInfo> callback) {
        Map<String, String> map = new HashMap<>();
        map.put(MXConst.ServiceIdentifier, serviceIdentifier);
        map.put(MXConst.CallerPackageName, callerPackageName);
        map.put(MXConst.CallerSignature, callerSignature);
        processProfile(
                MXBase.ProfileXML.AccessManagerAllowCallService,
                MXBase.ProfileName.AccessManagerAllowCallService,
                map,
                callback
        );
    }

    public void callAccessManagerAllowPermission(String permissionName,
                                                 String appPackageName,
                                                 String appClassName,
                                                 String appSignature,
                                                 BiConsumer<String, MXBase.ErrorInfo> callback) {
        Map<String, String> map = new HashMap<>();
        map.put(MXConst.PermissionAccessPermissionName, permissionName);
        map.put(MXConst.PermissionAccessAction, "1"); // 1: allow
        map.put(MXConst.PermissionAccessPackageName, appPackageName);
        map.put(MXConst.ApplicationClassName, appClassName);
        map.put(MXConst.PermissionAccessSignature, appSignature);
        processProfile(
                MXBase.ProfileXML.AccessManagerAllowPermission,
                MXBase.ProfileName.AccessManagerAllowPermission,
                map,
                callback
        );
    }

    // PowerManager

    public void callPowerManagerFeature(MXBase.PowerManagerOptions option) {
        callPowerManagerFeature(option, null);
    }

    public void callPowerManagerFeature(MXBase.PowerManagerOptions option,
                                        BiConsumer<String, MXBase.ErrorInfo> callback) {
        callPowerManagerFeature(
                option,
                null,
                MXBase.PowerManagerSuppressRebootOptions.DO_NOTHING,
                callback
        );
    }

    public void callPowerManagerFeature(
            MXBase.PowerManagerOptions option,
            String osZipFilePath,
            MXBase.PowerManagerSuppressRebootOptions suppressReboot,
            BiConsumer<String, MXBase.ErrorInfo> callback) {
        Map<String, String> map = new HashMap<>();
        switch (option) {
            case SLEEP_MODE:
            case REBOOT:
            case POWER_OFF:
            case OS_CANCEL_ONGOING:
            case ENTERPRISE_RESET:
            case FACTORY_RESET:
            case FULL_DEVICE_WIPE:
                map.put(MXConst.ResetAction, option.valueString());
                processProfile(
                        MXBase.ProfileXML.PowerManagerReset,
                        MXBase.ProfileName.PowerManagerReset,
                        map,
                        callback);
                break;
            case OS_UPDATE:
            case OS_UPGRADE:
            case OS_DOWNGRADE:
                map.put(MXConst.ResetAction, option.valueString());
                map.put(MXConst.ZipFile, osZipFilePath);
                map.put(MXConst.SuppressReboot, suppressReboot.getString());
                processProfile(
                        MXBase.ProfileXML.PowerManagerResetOSUpdate,
                        MXBase.ProfileName.PowerManagerResetOSUpdate,
                        map,
                        callback);
                break;
            case OS_UPGRADE_STREAMING:
            case OS_DOWNGRADE_STREAMING:
                map.put(MXConst.ResetAction, option.valueString());
                map.put(MXConst.ZipFile, osZipFilePath);
                map.put(MXConst.SuppressReboot, suppressReboot.getString());
                processProfile(
                        MXBase.ProfileXML.PowerManagerResetOSStreaming,
                        MXBase.ProfileName.PowerManagerResetOSStreaming,
                        map,
                        callback);
                break;
            case OS_UPDATE_VERIFY:
                map.put(MXConst.ResetAction, option.valueString());
                map.put(MXConst.OsupdateVerifyFile, osZipFilePath);
                processProfile(
                        MXBase.ProfileXML.PowerManagerResetOSVerify,
                        MXBase.ProfileName.PowerManagerResetOSVerify,
                        map,
                        callback);
                break;
            case CREATE_PROFILE:
            case DO_NOTHING:
                break;
        }
    }

    // Touch Manager

    public void configTouchPanelSensitivity(
            Context context,
            MXBase.TouchPanelSensitivityOptions option,
            long delaySeconds,
            Consumer<MXBase.ErrorInfo> callback) {

        // Use Collections.singletonMap for a single-entry map, which is efficient.
        // Assumes option.xmlValue becomes option.getXmlValue() in Java.
        Map<String, String> params = Collections.singletonMap(
                MXConst.TouchActionAny,
                option.getXmlValue()
        );

        processProfileWithCallback(
                context,
                MXBase.ProfileXML.TouchPanelSensitivity,
                MXBase.ProfileName.TouchPanelSensitivity,
                params,
                delaySeconds,
                callback
        );
    }

    /**
     * Overloaded method to handle the default delaySeconds = 0.
     * This provides the same convenience as the Kotlin default parameter.
     */
    public void configTouchPanelSensitivity(
            Context context,
            MXBase.TouchPanelSensitivityOptions option,
            Consumer<MXBase.ErrorInfo> callback) {

        // Call the main method with a default delay of 0.
        configTouchPanelSensitivity(context, option, 0L, callback);
    }
}
