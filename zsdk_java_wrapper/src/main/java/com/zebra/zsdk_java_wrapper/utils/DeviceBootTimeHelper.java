package com.zebra.zsdk_java_wrapper.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

public class DeviceBootTimeHelper {
    // Const
    private static final long OEMINFO_UPDATE_TIME_SINCE_BOOT_COMPLETED_MILLI = 30 * 1000;

    // Static Property
    private static DeviceBootTimeHelper _instance = null;
    private static Runnable _bootCompletedCallback = null;
    private static Runnable _oemInfoUpdatedCallback = null;
    private static final Handler _mainThreadHandler = new Handler(Looper.getMainLooper());

    // Property
    private boolean _bootCompleted = false;
    private boolean _oemInfoUpdated = false;

    public static DeviceBootTimeHelper shared() {
        if (_instance == null) {
            _instance = new DeviceBootTimeHelper();
        }
        return _instance;
    }

    public static class BootCompletedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
                if (_instance == null) {
                    _instance = new DeviceBootTimeHelper();
                }
                _instance._bootCompleted = true;
                if (_bootCompletedCallback != null) {
                    _bootCompletedCallback.run();
                    _bootCompletedCallback = null;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(OEMINFO_UPDATE_TIME_SINCE_BOOT_COMPLETED_MILLI);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        _instance._oemInfoUpdated = true;
                        _mainThreadHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (_oemInfoUpdatedCallback != null) {
                                    _oemInfoUpdatedCallback.run();
                                }
                            }
                        });
                    }
                }).start();
            }
        }
    }

    public Boolean isBootCompleted() {
        if (_bootCompleted) {
            return true;
        } else {
            return isUptimeExceedNormalBootTime(0);
        }
    }

    public Boolean isOEMInfoUpdated() {
        if (_oemInfoUpdated) {
            return true;
        } else {
            return isUptimeExceedNormalBootTime(OEMINFO_UPDATE_TIME_SINCE_BOOT_COMPLETED_MILLI);
        }
    }

    public void waitBootCompletedOneShot(Runnable callback) {
        _bootCompletedCallback = callback;
    }

    public void waitOEMInfoUpdateCompletedOneShot(Runnable _callback) {
        _oemInfoUpdatedCallback = _callback;
    }

    /**
     * the time threshold is measured using stop watch. my test result is around 53 - 55 seconds.
     * please measure your device boot time from the display light up to ACTION_BOOT_COMPLETED received.
     * */
    private Boolean isUptimeExceedNormalBootTime(long delta) {
        long uptime = SystemClock.uptimeMillis();
        if (Build.DEVICE.equals("TC26")) {
            return uptime > 60 * 1000 + delta;
        } else if (Build.DEVICE.equals("TC27")) {
            return uptime > 60 * 1000 + delta;
        } else {
            return uptime > 60 * 1000 + delta;
        }
    }
}
