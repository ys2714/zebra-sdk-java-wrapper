package com.zebra.zsdk_java_wrapper.dw;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

@Keep
public class DWTestFixture {

    private static final HandlerThread handlerThread;
    private static final Handler handler;

    static {
        handlerThread = new HandlerThread("DWIntentFactory_background_thread");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
    }

    private DWTestFixture() {}

    @Keep
    public static void test(Context context) {
        DWIntentFactory.callDWAPI(context, DWAPI.ActionExtraKeys.ENABLE_DATAWEDGE, true, new DWIntentFactory.DWCallback() {
            @Override
            public void onResult(@NonNull Intent intent, @Nullable Exception exception) {
                // Result handled here
            }
        });
    }
}
