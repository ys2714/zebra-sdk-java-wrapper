package com.zebra.zsdk_java_wrapper.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import androidx.annotation.Keep;
import androidx.core.content.ContextCompat;

import com.zebra.zsdk_java_wrapper.mx.MXBase;
import com.zebra.zsdk_java_wrapper.mx.MXConst;
import com.zebra.zsdk_java_wrapper.mx.MXKeymappingManager;

@Keep
public class ZebraKeyEventMonitor {

    public static final String TAG = "ZebraKeyEventMonitor";

    public static final String KEY_DOWN_ACTION = "com.zebra.zsdk_java_wrapper.KEY_DOWN_ACTION";
    public static final String KEY_DOWN_CATEGORY = "android.intent.category.DEFAULT";

    static final FixedSizeQueue<KeyDownListener> listeners = new FixedSizeQueue<>(100);

    @Keep
    public static class KeyDownListener extends BroadcastReceiver implements FixedSizeQueueItem {
        private final Context context;
        private final MXBase.KeyIdentifiers keyIdentifier;
        private final Runnable keyDownCallback;

        public KeyDownListener(Context context, MXBase.KeyIdentifiers keyIdentifier, Runnable keyDownCallback) {
            this.context = context;
            this.keyIdentifier = keyIdentifier;
            this.keyDownCallback = keyDownCallback;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                throw new RuntimeException("intent is null");
            }
            if (intent.hasExtra(MXConst.EXTRA_KEY_IDENTIFIER)) {
                String identifier = intent.getStringExtra(MXConst.EXTRA_KEY_IDENTIFIER);
                if (keyIdentifier.getString().equals(identifier)) {
                    keyDownCallback.run();
                }
            }
        }

        @Override
        public String getID() {
            return keyIdentifier.getString();
        }

        @Override
        public void onDisposal() {
            context.unregisterReceiver(this);
        }
    }

    @Keep
    public static void resetAllKeyDownToDefault(Context context, long delaySeconds, Runnable completion) {
        MXKeymappingManager.remappingAllKeyToDefault(context, delaySeconds, errorInfo -> {
            if (errorInfo == null) {
                if (completion != null) {
                    completion.run();
                }
            } else {
                Log.e(TAG, "resetAllKeyDownToDefault failed: " + errorInfo.buildFailureMessage());
            }
        });
    }

    /** Overload for default delaySeconds = 0 */
    @Keep
    public static void resetAllKeyDownToDefault(Context context, Runnable completion) {
        resetAllKeyDownToDefault(context, 0, completion);
    }

    @Keep
    public static void registerKeyDownListener(
            Context context,
            MXBase.KeyIdentifiers key,
            long delaySeconds,
            Runnable keyDownCallback) {
        
        MXKeymappingManager.remappingKeyToSendIntent(
                context,
                key,
                KEY_DOWN_ACTION,
                KEY_DOWN_CATEGORY,
                delaySeconds,
                error -> {
                    if (error != null) {
                        Log.e(TAG, "remap key " + key + " to send intent failed: " + error.buildFailureMessage());
                    } else {
                        Context appContext = context.getApplicationContext();
                        KeyDownListener listener = new KeyDownListener(appContext, key, keyDownCallback);
                        listeners.enqueue(listener);
                        
                        IntentFilter filter = new IntentFilter();
                        filter.addAction(KEY_DOWN_ACTION);
                        filter.addCategory(KEY_DOWN_CATEGORY);
                        
                        ContextCompat.registerReceiver(
                                appContext,
                                listener,
                                filter,
                                ContextCompat.RECEIVER_NOT_EXPORTED
                        );
                    }
                }
        );
    }

    /** Overload for default delaySeconds = 0 */
    @Keep
    public static void registerKeyDownListener(
            Context context,
            MXBase.KeyIdentifiers key,
            Runnable keyDownCallback) {
        registerKeyDownListener(context, key, 0, keyDownCallback);
    }
}
