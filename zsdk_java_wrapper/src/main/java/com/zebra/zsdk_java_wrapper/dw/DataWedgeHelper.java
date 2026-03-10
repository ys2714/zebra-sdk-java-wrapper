package com.zebra.zsdk_java_wrapper.dw;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.zebra.zsdk_java_wrapper.utils.AssetsReader;
import com.zebra.zsdk_java_wrapper.utils.FixedSizeQueue;
import com.zebra.zsdk_java_wrapper.utils.FixedSizeQueueItem;
import com.zebra.zsdk_java_wrapper.utils.JsonUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Keep
public class DataWedgeHelper {

    public static final String TAG = "DataWedgeHelper";

    @Keep
    public interface ScanDataListener extends FixedSizeQueueItem {
        void onData(String type, String value, String timestamp);
    }

    @Keep
    public interface SessionStatusListener extends FixedSizeQueueItem {
        void onStatus(DWAPI.NotificationType type, String status, String profileName);
    }

    private static DataWedgeHelper instance;

    private DataReceiver dataReceiver = null;
    private NotificationReceiver notificationReveiver = null;

    private final FixedSizeQueue<ScanDataListener> scanDataListeners = new FixedSizeQueue<>(50);
    private final FixedSizeQueue<SessionStatusListener> sessionStatusListeners = new FixedSizeQueue<>(50);

    private final ExecutorService backgroundExecutor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private DataWedgeHelper() {}

    @NonNull
    public static synchronized DataWedgeHelper getInstance() {
        if (instance == null) {
            instance = new DataWedgeHelper();
        }
        return instance;
    }

    private void notifyDataListeners(String type, String value, String timestamp) {
        for (ScanDataListener listener : scanDataListeners.getItems()) {
            listener.onData(type, value, timestamp);
        }
    }

    private void notifyStatusListeners(DWAPI.NotificationType type, String status, String profileName) {
        for (SessionStatusListener listener : sessionStatusListeners.getItems()) {
            listener.onStatus(type, status, profileName);
        }
    }

    @Keep
    public void addScanDataListener(ScanDataListener listener) {
        scanDataListeners.enqueue(listener);
    }

    @Keep
    public void removeScanDataListener(ScanDataListener listener) {
        scanDataListeners.remove(listener);
    }

    @Keep
    public void addSessionStatusListener(SessionStatusListener listener) {
        sessionStatusListeners.enqueue(listener);
    }

    @Keep
    public void removeSessionStatusListener(SessionStatusListener listener) {
        sessionStatusListeners.remove(listener);
    }

    @Keep
    public void prepare(Context context, Consumer<Boolean> callback) {
        registerReceiverIfNeeded(context);
        backgroundExecutor.execute(() -> {
            try {
                boolean enabled = false;
                while (!enabled) {
                    DWAPI_DWStatus.enableDW(context, true).join();
                    enabled = Boolean.TRUE.equals(DWAPI_DWStatus.sendGetDWStatusIntent(context).join());
                    if (!enabled) {
                        Thread.sleep(100); // Small sleep to avoid tight loop
                    }
                }
                
                List<DWScannerMap.DWScannerInfo> scannerList = DWAPI_Scanner.sendEnumerateScannersIntent(context).join();
                boolean success = scannerList != null && !scannerList.isEmpty();
                
                if (!success) {
                    Log.e(TAG, "Scanner List empty. device should at least have one internal camera");
                }
                
                mainHandler.post(() -> callback.accept(success));

            } catch (Exception e) {
                Log.e(TAG, "DW NOT ENABLED. Exception: " + e.getMessage());
                mainHandler.post(() -> callback.accept(false));
            }
        });
    }

    @Keep
    public void checkDWStatus(Context context, Consumer<Boolean> callback) {
        backgroundExecutor.execute(() -> {
            try {
                boolean enabled = Boolean.TRUE.equals(DWAPI_DWStatus.sendGetDWStatusIntent(context).join());
                Thread.sleep(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS);
                mainHandler.post(() -> callback.accept(enabled));
            } catch (Exception e) {
                Log.e(TAG, "DW NOT ENABLED. Exception: " + e.getMessage());
                try {
                    Thread.sleep(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS);
                } catch (InterruptedException ignored) {}
                mainHandler.post(() -> callback.accept(false));
            }
        });
    }

    @Keep
    public void enableDW(Context context, @Nullable Consumer<Boolean> callback) {
        backgroundExecutor.execute(() -> {
            try {
                DWAPI_DWStatus.enableDW(context, true).join();
                Thread.sleep(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS);
                if (callback != null) {
                    mainHandler.post(() -> callback.accept(true));
                }
            } catch (Exception e) {
                try {
                    Thread.sleep(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS);
                } catch (InterruptedException ignored) {}
                if (callback != null) {
                    if (DWAPI.ResultCodes.DATAWEDGE_ALREADY_ENABLED.getValue().equals(e.getMessage())) {
                        Log.d(TAG, "DATAWEDGE ALREADY ENABLED");
                        mainHandler.post(() -> callback.accept(true));
                    } else {
                        Log.e(TAG, "ENABLE DW FAIL. Exception: " + e.getMessage());
                        mainHandler.post(() -> callback.accept(false));
                    }
                }
            }
        });
    }

    @Keep
    public void disableDW(Context context, @Nullable Consumer<Boolean> callback) {
        backgroundExecutor.execute(() -> {
            try {
                DWAPI_DWStatus.enableDW(context, false).join();
                Thread.sleep(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS);
                if (callback != null) {
                    mainHandler.post(() -> callback.accept(true));
                }
            } catch (Exception e) {
                try {
                    Thread.sleep(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS);
                } catch (InterruptedException ignored) {}
                if (callback != null) {
                    if (DWAPI.ResultCodes.DATAWEDGE_ALREADY_DISABLED.getValue().equals(e.getMessage())) {
                        Log.d(TAG, "DATAWEDGE ALREADY DISABLED");
                        mainHandler.post(() -> callback.accept(true));
                    } else {
                        Log.e(TAG, "DISABLE DW FAIL. Exception: " + e.getMessage());
                        mainHandler.post(() -> callback.accept(false));
                    }
                }
            }
        });
    }

    @Keep
    public void enableScannerStatusNotification(Context context, @Nullable Consumer<Boolean> callback) {
        DWAPI_Notification.registerNotification(context, DWAPI.NotificationType.SCANNER_STATUS, success -> {
            if (callback != null) callback.accept(success);
        });
    }

    @Keep
    public void disableScannerStatusNotification(Context context, @Nullable Consumer<Boolean> callback) {
        DWAPI_Notification.unregisterNotification(context, DWAPI.NotificationType.SCANNER_STATUS, success -> {
            if (callback != null) callback.accept(success);
        });
    }

    @Keep
    public void enableWorkflowStatusNotification(Context context, @Nullable Consumer<Boolean> callback) {
        DWAPI_Notification.registerNotification(context, DWAPI.NotificationType.WORKFLOW_STATUS, success -> {
            if (callback != null) callback.accept(success);
        });
    }

    @Keep
    public void disableWorkflowStatusNotification(Context context, @Nullable Consumer<Boolean> callback) {
        DWAPI_Notification.unregisterNotification(context, DWAPI.NotificationType.WORKFLOW_STATUS, success -> {
            if (callback != null) callback.accept(success);
        });
    }

    @Keep
    public void createProfile(Context context, String name, @Nullable Consumer<Boolean> callback) {
        backgroundExecutor.execute(() -> {
            try {
                boolean success = Boolean.TRUE.equals(DWAPI_Profile.sendCreateProfileIntent(context, name).join());
                Thread.sleep(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS);
                if (callback != null) {
                    mainHandler.post(() -> callback.accept(success));
                }
            } catch (Exception e) {
                try {
                    Thread.sleep(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS);
                } catch (InterruptedException ignored) {}
                if (callback != null) {
                    String message = e.getMessage();
                    if (e.getCause() != null) message = e.getCause().getMessage();

                    if (DWAPI.ResultCodes.PROFILE_NOT_FOUND.getValue().equals(message)) {
                        Log.d(TAG, "PROFILE ALREADY DELETED");
                        mainHandler.post(() -> callback.accept(true));
                    } else if (DWAPI.ResultCodes.PROFILE_ALREADY_EXISTS.getValue().equals(message)) {
                        Log.d(TAG, "PROFILE ALREADY EXISTS");
                        mainHandler.post(() -> callback.accept(true));
                    } else {
                        Log.e(TAG, "CREATE PROFILE FAIL. Exception: " + message);
                        mainHandler.post(() -> callback.accept(false));
                    }
                }
            }
        });
    }

    @Keep
    public void getProfile(Context context, String name, @Nullable Consumer<Bundle> callback) {
        backgroundExecutor.execute(() -> {
            try {
                Bundle configBundle = new Bundle();
                configBundle.putString("PROFILE_NAME", name);
                
                Bundle pluginConfig = new Bundle();
                ArrayList<String> pluginNames = new ArrayList<>();
                Collections.addAll(pluginNames, "BARCODE", "MSR", "RFID", "SERIAL", "VOICE", "WORKFLOW", "INTENT", "KEYSTROKE", "IP", "DCP", "EKB");
                pluginConfig.putStringArrayList("PLUGIN_NAME", pluginNames);
                
                configBundle.putBundle("PLUGIN_CONFIG", pluginConfig);
                
                Bundle result = DWAPI_Profile.sendGetConfigIntent(context, configBundle).join();
                if (callback != null) {
                    mainHandler.post(() -> callback.accept(result));
                }
            } catch (Exception e) {
                if (callback != null) {
                    mainHandler.post(() -> callback.accept(new Bundle()));
                }
            }
        });
    }

    @Keep
    public void deleteProfile(Context context, String name, @Nullable Consumer<Boolean> callback) {
        backgroundExecutor.execute(() -> {
            try {
                boolean success = Boolean.TRUE.equals(DWAPI_Profile.sendDeleteProfileIntent(context, name).join());
                Thread.sleep(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS);
                if (callback != null) {
                    mainHandler.post(() -> callback.accept(success));
                }
            } catch (Exception e) {
                try {
                    Thread.sleep(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS);
                } catch (InterruptedException ignored) {}
                if (callback != null) {
                    String message = e.getMessage();
                    if (e.getCause() != null) message = e.getCause().getMessage();

                    if (DWAPI.ResultCodes.PROFILE_NOT_FOUND.getValue().equals(message)) {
                        mainHandler.post(() -> callback.accept(true));
                    } else {
                        Log.e(TAG, "DELETE PROFILE FAIL. Exception: " + message);
                        mainHandler.post(() -> callback.accept(false));
                    }
                }
            }
        });
    }

    @Keep
    public void switchProfile(Context context, String name, @Nullable Consumer<Boolean> callback) {
        backgroundExecutor.execute(() -> {
            try {
                boolean success = Boolean.TRUE.equals(DWAPI_Profile.sendSwitchProfileIntent(context, name).join());
                if (callback != null) {
                    mainHandler.post(() -> callback.accept(success));
                }
            } catch (Exception e) {
                if (callback != null) {
                    Log.e(TAG, "SWITCH PROFILE FAIL. Exception: " + e.getMessage());
                    mainHandler.post(() -> callback.accept(false));
                }
            }
        });
    }

    @Keep
    public void bindProfileToApp(Context context, String name, String packageName, @Nullable Consumer<Boolean> callback) {
        backgroundExecutor.execute(() -> {
            try {
                Bundle bundle = DWProfileProcessor.bundleForBindProfile(context, name, packageName);
                DWAPI_Profile.sendSetConfigIntent(context, bundle).join();
                Thread.sleep(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS);
                if (callback != null) {
                    mainHandler.post(() -> callback.accept(true));
                }
            } catch (Exception e) {
                try {
                    Thread.sleep(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS);
                } catch (InterruptedException ignored) {}
                if (callback != null) {
                    mainHandler.post(() -> callback.accept(false));
                }
            }
        });
    }

    @Keep
    public void configBarcodePlugin(Context context, String name, boolean enable, boolean hardTrigger, @Nullable Consumer<Boolean> callback) {
        backgroundExecutor.execute(() -> {
            try {
                Bundle bundle = DWProfileProcessor_BarcodePlugin.bundleForBarcodePlugin(
                        context,
                        name,
                        enable,
                        hardTrigger,
                        DWAPI.ScanInputModeOptions.SINGLE);
                DWAPI_Profile.sendSetConfigIntent(context, bundle).join();
                Thread.sleep(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS);
                if (callback != null) {
                    mainHandler.post(() -> callback.accept(true));
                }
            } catch (Exception e) {
                try {
                    Thread.sleep(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS);
                } catch (InterruptedException ignored) {}
                if (callback != null) {
                    mainHandler.post(() -> callback.accept(false));
                }
            }
        });
    }

    @Keep
    public void configWorkflowPlugin(Context context, String name, boolean enable, @Nullable Consumer<Boolean> callback) {
        backgroundExecutor.execute(() -> {
            try {
                Bundle bundle = DWProfileProcessor_WorkflowPlugin.bundleForWorkflowPlugin(context, name, enable);
                DWAPI_Profile.sendSetConfigIntent(context, bundle).join();
                Thread.sleep(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS);
                if (callback != null) {
                    mainHandler.post(() -> callback.accept(true));
                }
            } catch (Exception e) {
                try {
                    Thread.sleep(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS);
                } catch (InterruptedException ignored) {}
                if (callback != null) {
                    mainHandler.post(() -> callback.accept(false));
                }
            }
        });
    }

    @Keep
    public void configKeystrokePlugin(Context context, String name, boolean enable, @Nullable Consumer<Boolean> callback) {
        backgroundExecutor.execute(() -> {
            try {
                Bundle bundle = DWProfileProcessor_KeystrokePlugin.bundleForKeystrokePlugin(context, name, enable);
                DWAPI_Profile.sendSetConfigIntent(context, bundle).join();
                Thread.sleep(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS);
                if (callback != null) {
                    mainHandler.post(() -> callback.accept(true));
                }
            } catch (Exception e) {
                try {
                    Thread.sleep(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS);
                } catch (InterruptedException ignored) {}
                if (callback != null) {
                    mainHandler.post(() -> callback.accept(false));
                }
            }
        });
    }

    @Keep
    public void configIntentPlugin(Context context, String name, @Nullable Consumer<Boolean> callback) {
        backgroundExecutor.execute(() -> {
            try {
                Bundle bundle = DWProfileProcessor_IntentPlugin.bundleForIntentPlugin(
                        context,
                        name,
                        DWAPI.ResultActionNames.SCAN_RESULT_ACTION,
                        DWAPI.ResultCategoryNames.CATEGORY_DEFAULT,
                        DWAPI.IntentDeliveryOptions.BROADCAST
                );
                DWAPI_Profile.sendSetConfigIntent(context, bundle).join();
                Thread.sleep(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS);
                if (callback != null) {
                    mainHandler.post(() -> callback.accept(true));
                }
            } catch (Exception e) {
                try {
                    Thread.sleep(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS);
                } catch (InterruptedException ignored) {}
                if (callback != null) {
                    mainHandler.post(() -> callback.accept(false));
                }
            }
        });
    }

    @Keep
    public void configWithJSON(Context context, String fileName, Map<String, String> params, @Nullable Consumer<Boolean> callback) {
        backgroundExecutor.execute(() -> {
            try {
                String jsonString = AssetsReader.readFileToStringWithParams(context, fileName, params);
                Bundle bundle = JsonUtils.jsonToBundle(jsonString);
                DWAPI_Profile.sendSetConfigIntent(context, bundle).join();
                Thread.sleep(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS);
                if (callback != null) {
                    mainHandler.post(() -> callback.accept(true));
                }
            } catch (Exception e) {
                try {
                    Thread.sleep(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS);
                } catch (InterruptedException ignored) {}
                if (callback != null) {
                    mainHandler.post(() -> callback.accept(false));
                }
            }
        });
    }

    @Keep
    public void switchScannerParams(Context context, Bundle bundle, @Nullable Consumer<Boolean> callback) {
        backgroundExecutor.execute(() -> {
            try {
                DWAPI_Profile.sendSwitchScannerParamsIntent(context, bundle).join();
                if (callback != null) {
                    mainHandler.post(() -> callback.accept(true));
                }
            } catch (Exception e) {
                if (callback != null) {
                    mainHandler.post(() -> callback.accept(false));
                }
            }
        });
    }

    @Keep
    public void controlScannerInputPlugin(Context context, DWAPI.ControlScannerInputPluginCommand command, @Nullable Consumer<Boolean> callback) {
        backgroundExecutor.execute(() -> {
            try {
                DWAPI_Scanner.sendControlScannerInputPluginIntent(context, command).join();
                if (callback != null) {
                    mainHandler.post(() -> callback.accept(true));
                }
            } catch (Exception e) {
                if (callback != null) {
                    mainHandler.post(() -> callback.accept(false));
                }
            }
        });
    }

    @Keep
    public void softScanTrigger(Context context, DWAPI.SoftScanTriggerOptions option) {
        DWAPI_ScanTrigger.softScanTrigger(context, option);
    }

    @Keep
    public DWScannerMap.DWScannerInfo getScannerInfo(Context context, String id) {
        return DWScannerMap.getScannerInfo(id);
    }

    @Keep
    public void getScannerStatus(Context context, int delaySeconds, Consumer<DWAPI.ScannerStatus> callback) {
        backgroundExecutor.execute(() -> {
            try {
                Thread.sleep(delaySeconds * 1000L);
                DWAPI.ScannerStatus status = DWAPI_Scanner.sendGetSelectedScannerStatusIntent(context).join();
                mainHandler.post(() -> callback.accept(status));
            } catch (Exception e) {
                Log.e(TAG, "GET SCANNER STATUS FAIL. Exception: " + e.getMessage());
                mainHandler.post(() -> callback.accept(DWAPI.ScannerStatus.UNKNOWN));
            }
        });
    }

    @Keep
    public void getScannerList(Context context, Consumer<List<DWScannerMap.DWScannerInfo>> callback) {
        backgroundExecutor.execute(() -> {
            try {
                List<DWScannerMap.DWScannerInfo> scannerList = DWAPI_Scanner.sendEnumerateScannersIntent(context).join();
                Thread.sleep(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS);
                mainHandler.post(() -> callback.accept(scannerList));
            } catch (Exception e) {
                Log.e(TAG, "GET SCANNER LIST FAIL. Exception: " + e.getMessage());
                try {
                    Thread.sleep(DWAPI.MILLISECONDS_DELAY_BETWEEN_API_CALLS);
                } catch (InterruptedException ignored) {}
                mainHandler.post(() -> callback.accept(Collections.emptyList()));
            }
        });
    }

    private void registerReceiverIfNeeded(Context context) {
        if (dataReceiver != null) {
            Log.d(TAG, "DataReceiver already registered. skip");
        } else {
            dataReceiver = new DataReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addCategory(DWAPI.ResultCategoryNames.CATEGORY_DEFAULT.getValue());
            filter.addAction(DWAPI.ResultActionNames.SCAN_RESULT_ACTION.getValue());
            ContextCompat.registerReceiver(
                    context.getApplicationContext(),
                    dataReceiver,
                    filter,
                    ContextCompat.RECEIVER_EXPORTED
            );
        }

        if (notificationReveiver != null) {
            Log.d(TAG, "NotificationReceiver already registered. skip");
        } else {
            notificationReveiver = new NotificationReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addCategory(DWAPI.ResultCategoryNames.CATEGORY_DEFAULT.getValue());
            filter.addAction("com.symbol.datawedge.api.NOTIFICATION_ACTION");
            ContextCompat.registerReceiver(
                    context.getApplicationContext(),
                    notificationReveiver,
                    filter,
                    ContextCompat.RECEIVER_EXPORTED
            );
        }
    }

    private class DataReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                Toast.makeText(context, "intent is null", Toast.LENGTH_LONG).show();
                return;
            }
            if (DWAPI.ResultActionNames.SCAN_RESULT_ACTION.getValue().equals(intent.getAction())) {
                handleDWScanOutputAction(intent);
            } else {
                Toast.makeText(context, "Unknown action", Toast.LENGTH_LONG).show();
            }
        }

        private void handleDWScanOutputAction(Intent intent) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                String type = extras.getString(DWAPI.ScanResult.TYPE, "");
                String data = extras.getString(DWAPI.ScanResult.DATA, "");
                long timestamp = extras.getLong(DWAPI.ScanResult.TIME);
                Date date = new Date(timestamp);
                notifyDataListeners(type, data, date.toString());
            }
        }
    }

    private class NotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || !"com.symbol.datawedge.api.NOTIFICATION_ACTION".equals(intent.getAction())) {
                return;
            }
            if (!intent.hasExtra("com.symbol.datawedge.api.NOTIFICATION")) {
                return;
            }
            Bundle bundle = intent.getBundleExtra("com.symbol.datawedge.api.NOTIFICATION");
            if (bundle == null) return;

            String typeString = bundle.getString("NOTIFICATION_TYPE");
            if (typeString == null) return;

            try {
                DWAPI.NotificationType type = DWAPI.NotificationType.valueOf(typeString);
                switch (type) {
                    case CONFIGURATION_UPDATE:
                        notifyStatusListeners(type, "", "");
                        break;
                    case PROFILE_SWITCH: {
                        String enabled = bundle.getString("PROFILE_ENABLED", "");
                        String profile = bundle.getString("PROFILE_NAME", "");
                        notifyStatusListeners(type, enabled, profile);
                        break;
                    }
                    case SCANNER_STATUS: {
                        String status = bundle.getString("STATUS", "");
                        String profile = bundle.getString("PROFILE_NAME", "");
                        notifyStatusListeners(type, status, profile);
                        break;
                    }
                    case WORKFLOW_STATUS: {
                        String status = bundle.getString("STATUS", "");
                        String profile = bundle.getString("PROFILE_NAME", "");
                        notifyStatusListeners(type, status, profile);
                        break;
                    }
                }
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "Unknown notification type: " + typeString);
            }
        }
    }
}
