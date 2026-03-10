package com.zebra.zsdk_java_wrapper.rxlogger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.Keep;
import androidx.core.content.ContextCompat;

import java.util.function.Consumer;

/**
 * Helper class for interacting with RXLogger via intents.
 * https://techdocs.zebra.com/rxlogger/latest/guide/apis/
 */
@Keep
public class RXLoggerHelper {

    public static final String ACTION_ENABLE = "com.symbol.rxlogger.intent.action.ENABLE";
    public static final String ACTION_DISABLE = "com.symbol.rxlogger.intent.action.DISABLE";
    public static final String ACTION_BACKUP_NOW = "com.symbol.rxlogger.intent.action.BACKUP_NOW";

    // This intent informs the third-party app that RxLogger has begun capturing logs.
    // RxLogger sends this acknowledgment intent once it enters the running state.
    public static final String ACTION_ENABLE_STATUS = "com.symbol.rxlogger.intent.action.ENABLE_STATUS";
    public static final String ACTION_DISABLE_STATUS = "com.symbol.rxlogger.intent.action.DISABLE_STATUS";

    // This provides the status of the backup operation. RxLogger sends an acknowledgment intent
    // to convey the backup status, including extras such as status, message and the absolute path of the backup zip file.
    public static final String ACTION_BACKUP_NOW_STATUS = "com.symbol.rxlogger.intent.action.BACKUP_NOW_STATUS";

    /**
     * Callback interface for dump result.
     */
    public interface DumpResultCallback {
        void onResult(String status, String message, String filepath);
    }

    /**
     * This initiates data collection for all enabled modules, equivalent to tapping the Start button.
     */
    @Keep
    public static void startRXLogger(Context context) {
        Intent intent = new Intent();
        intent.setAction(ACTION_ENABLE);
        context.sendOrderedBroadcast(intent, null);
    }

    /**
     * This stops data collection for all modules; the equivalent of tapping the Stop button.
     */
    @Keep
    public static void stopRXLogger(Context context) {
        Intent intent = new Intent();
        intent.setAction(ACTION_DISABLE);
        context.sendOrderedBroadcast(intent, null);
    }

    /**
     * This initiates a backup of log files in the RxLogger folder,
     * compressing them into a zip file named Backup-<date>-<HHMMSS>.zip.
     */
    @Keep
    public static void dumpRXLogger(Context context) {
        Intent intent = new Intent();
        intent.setAction(ACTION_BACKUP_NOW);
        context.sendOrderedBroadcast(intent, null);
    }

    /**
     * Registers a one-shot receiver to wait for RXLogger running status.
     * callback value:
     * - true: RXLogger is running
     * - false: RXLogger is stopped
     */
    @Keep
    public static void waitingRXLoggerRunningStatusOneShot(Context context, Consumer<Boolean> completion) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_ENABLE_STATUS);
        filter.addAction(ACTION_DISABLE_STATUS);

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String action = intent.getAction();
                    if (ACTION_ENABLE_STATUS.equals(action)) {
                        completion.accept(true);
                    } else if (ACTION_DISABLE_STATUS.equals(action)) {
                        completion.accept(false);
                    }
                }
                context.unregisterReceiver(this);
            }
        };

        ContextCompat.registerReceiver(
                context,
                receiver,
                filter,
                ContextCompat.RECEIVER_EXPORTED
        );
    }

    /**
     * Registers a one-shot receiver to wait for RXLogger dump result.
     */
    @Keep
    public static void waitingRXLoggerDumpResultOneShot(Context context, DumpResultCallback completion) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_BACKUP_NOW_STATUS);

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null && ACTION_BACKUP_NOW_STATUS.equals(intent.getAction())) {
                    String status = intent.getStringExtra("status");
                    String message = intent.getStringExtra("message");
                    String filepath = intent.getStringExtra("filepath");
                    completion.onResult(
                            status != null ? status : "",
                            message != null ? message : "",
                            filepath
                    );
                }
                context.unregisterReceiver(this);
            }
        };

        ContextCompat.registerReceiver(
                context,
                receiver,
                filter,
                ContextCompat.RECEIVER_EXPORTED
        );
    }
}
