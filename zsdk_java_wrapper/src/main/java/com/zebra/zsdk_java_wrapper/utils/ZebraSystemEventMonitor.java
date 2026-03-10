package com.zebra.zsdk_java_wrapper.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;

import java.util.function.Consumer;

/**
 * A monitor for system events such as screen on/off and application lifecycle changes.
 */
@Keep
public class ZebraSystemEventMonitor implements LifecycleEventObserver {

    private static final ZebraSystemEventMonitor INSTANCE = new ZebraSystemEventMonitor();

    private Runnable appCreateCallback;
    private Runnable appStartCallback;
    private Runnable appPauseCallback;
    private Runnable appResumeCallback;
    private Runnable appStopCallback;
    private Runnable appDestroyCallback;

    private ZebraSystemEventMonitor() {
        // Registering with ProcessLifecycleOwner must happen on the main thread.
        // In Kotlin's init block, it was implicitly assumed to be initialized correctly.
        // We do the same here by accessing the singleton instance.
    }

    static {
        // Initialize the singleton and register it as an observer.
        ProcessLifecycleOwner.get().getLifecycle().addObserver(INSTANCE);
    }

    public static ZebraSystemEventMonitor getInstance() {
        return INSTANCE;
    }

    /**
     * Registers a one-time listener for screen ON or OFF events.
     * The listener unregisters itself after the first event is received.
     *
     * @param context  The context to use for registration.
     * @param callback A callback that receives true for screen OFF and false for screen ON.
     */
    @Keep
    public static void registerScreenOFFListener(Context context, Consumer<Boolean> callback) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent == null) {
                    throw new RuntimeException("intent is null");
                }
                String action = intent.getAction();
                if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                    callback.accept(true);
                } else if (Intent.ACTION_SCREEN_ON.equals(action)) {
                    callback.accept(false);
                }
                context.unregisterReceiver(this);
            }
        };

        ContextCompat.registerReceiver(
                context.getApplicationContext(),
                receiver,
                filter,
                ContextCompat.RECEIVER_NOT_EXPORTED
        );
    }

    @Keep
    public static void registerAppCreateCallback(Runnable callback) {
        INSTANCE.appCreateCallback = callback;
    }

    @Keep
    public static void registerAppStartCallback(Runnable callback) {
        INSTANCE.appStartCallback = callback;
    }

    @Keep
    public static void registerAppPauseCallback(Runnable callback) {
        INSTANCE.appPauseCallback = callback;
    }

    @Keep
    public static void registerAppResumeCallback(Runnable callback) {
        INSTANCE.appResumeCallback = callback;
    }

    @Keep
    public static void registerAppStopCallback(Runnable callback) {
        INSTANCE.appStopCallback = callback;
    }

    @Keep
    public static void registerAppDestroyCallback(Runnable callback) {
        INSTANCE.appDestroyCallback = callback;
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        if (source.equals(ProcessLifecycleOwner.get())) {
            switch (event) {
                case ON_CREATE:
                    if (appCreateCallback != null) appCreateCallback.run();
                    break;
                case ON_START:
                    if (appStartCallback != null) appStartCallback.run();
                    break;
                case ON_PAUSE:
                    if (appPauseCallback != null) appPauseCallback.run();
                    break;
                case ON_RESUME:
                    if (appResumeCallback != null) appResumeCallback.run();
                    break;
                case ON_STOP:
                    if (appStopCallback != null) appStopCallback.run();
                    break;
                case ON_DESTROY:
                    if (appDestroyCallback != null) appDestroyCallback.run();
                    break;
                default:
                    break;
            }
        }
    }
}
