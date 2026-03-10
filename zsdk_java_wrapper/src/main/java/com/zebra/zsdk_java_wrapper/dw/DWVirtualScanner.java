package com.zebra.zsdk_java_wrapper.dw;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Keep
public abstract class DWVirtualScanner {

    protected final Context context;
    protected final String TAG = "DWVirtualScanner";

    private DataWedgeHelper.ScanDataListener dataListener = null;
    private DataWedgeHelper.SessionStatusListener statusListener = null;

    public DWVirtualScanner(@NonNull Context context) {
        this.context = context;
    }

    @NonNull
    public abstract String getCreateJSONFileName();

    @NonNull
    public abstract String getUpdateJSONFileName();

    @NonNull
    public abstract Map<String, String> getParameters();

    @Nullable
    public String getProfileName() {
        return getParameters().get("PROFILE_NAME");
    }

    @Keep
    @NonNull
    public CompletableFuture<Boolean> asyncOpen() {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        DataWedgeHelper.getInstance().configWithJSON(
                context,
                getCreateJSONFileName(),
                getParameters(),
                success -> {
                    if (success) {
                        if (getProfileName() != null) {
                            future.complete(true);
                        } else {
                            future.completeExceptionally(new RuntimeException(TAG + " profileName is null"));
                        }
                    } else {
                        future.completeExceptionally(new RuntimeException(TAG + " open failed: " + getCreateJSONFileName()));
                    }
                }
        );
        return future;
    }

    @Keep
    @NonNull
    public DWVirtualScanner open(@Nullable Consumer<DWVirtualScanner> completion) {
            DataWedgeHelper.getInstance().configWithJSON(
                    context,
                    getCreateJSONFileName(),
                    getParameters(),
                    success -> {
                        if (success) {
                            if (getProfileName() != null) {
                                if (completion != null) completion.accept(this);
                            } else {
                                throw new RuntimeException(TAG + " profileName is null");
                            }
                        } else {
                            throw new RuntimeException(TAG + " open failed: " + getCreateJSONFileName());
                        }
                    }
            );
        return this;
    }

    @Keep
    @NonNull
    public DWVirtualScanner update(@NonNull Map<String, String> params, @Nullable Consumer<DWVirtualScanner> completion) {
        DataWedgeHelper.getInstance().configWithJSON(
                context,
                getUpdateJSONFileName(),
                params,
                success -> {
                    if (success) {
                        if (getProfileName() != null) {
                            if (completion != null) completion.accept(this);
                        } else {
                            throw new RuntimeException(TAG + " profileName is null");
                        }
                    } else {
                        throw new RuntimeException(TAG + " open failed: " + getUpdateJSONFileName());
                    }
                }
        );
        return this;
    }

    @Keep
    @NonNull
    public DWVirtualScanner select(@Nullable Consumer<DWVirtualScanner> completion) {
        String profile = getProfileName();
        if (profile != null) {
            DataWedgeHelper.getInstance().switchProfile(context, profile, success -> {
                if (completion != null) completion.accept(this);
            });
        }
        return this;
    }

    @Keep
    @NonNull
    public DWVirtualScanner suspend() {
        DataWedgeHelper.getInstance().controlScannerInputPlugin(context, DWAPI.ControlScannerInputPluginCommand.SUSPEND_PLUGIN, null);
        return this;
    }

    @Keep
    @NonNull
    public DWVirtualScanner resume() {
        DataWedgeHelper.getInstance().controlScannerInputPlugin(context, DWAPI.ControlScannerInputPluginCommand.RESUME_PLUGIN, null);
        return this;
    }

    @Keep
    @NonNull
    public DWVirtualScanner enable() {
        DataWedgeHelper.getInstance().controlScannerInputPlugin(context, DWAPI.ControlScannerInputPluginCommand.ENABLE_PLUGIN, null);
        return this;
    }

    @Keep
    @NonNull
    public DWVirtualScanner disable() {
        DataWedgeHelper.getInstance().controlScannerInputPlugin(context, DWAPI.ControlScannerInputPluginCommand.DISABLE_PLUGIN, null);
        return this;
    }

    @Keep
    @NonNull
    public DWVirtualScanner switchParameter(@NonNull String key, @NonNull String value) {
        Bundle bundle = new Bundle();
        bundle.putString(key, value);
        DataWedgeHelper.getInstance().switchScannerParams(context, bundle, null);
        return this;
    }

    @Keep
    public void startListen(@NonNull Consumer<String> onData) {
        if (dataListener != null) {
            return;
        }
        dataListener = new DataWedgeHelper.ScanDataListener() {
            @Override
            public void onData(String type, String value, String timestamp) {
                onData.accept(value);
            }

            @NonNull
            @Override
            public String getID() {
                return String.valueOf(hashCode());
            }

            @Override
            public void onDisposal() {
                dataListener = null;
            }
        };
        DataWedgeHelper.getInstance().addScanDataListener(dataListener);
    }

    @Keep
    public void stopListen() {
        if (dataListener != null) {
            DataWedgeHelper.getInstance().removeScanDataListener(dataListener);
            dataListener = null;
        }
    }

    @Keep
    public void startListenStatus(@NonNull BiConsumer<DWAPI.NotificationType, String> onValue) {
        if (statusListener != null) {
            return;
        }
        statusListener = new DataWedgeHelper.SessionStatusListener() {
            @Override
            public void onStatus(DWAPI.NotificationType type, String status, String profileName) {
                onValue.accept(type, status);
            }

            @NonNull
            @Override
            public String getID() {
                return String.valueOf(hashCode());
            }

            @Override
            public void onDisposal() {
                statusListener = null;
            }
        };
        DataWedgeHelper.getInstance().addSessionStatusListener(statusListener);
        DataWedgeHelper.getInstance().enableScannerStatusNotification(context, null);
        DataWedgeHelper.getInstance().enableWorkflowStatusNotification(context, null);
    }

    @Keep
    public void stopListenStatus() {
        if (statusListener != null) {
            DataWedgeHelper.getInstance().removeSessionStatusListener(statusListener);
            DataWedgeHelper.getInstance().disableScannerStatusNotification(context, null);
            DataWedgeHelper.getInstance().disableWorkflowStatusNotification(context, null);
            statusListener = null;
        }
    }

    @Keep
    @NonNull
    public DWVirtualScanner startScan() {
        DataWedgeHelper.getInstance().softScanTrigger(context, DWAPI.SoftScanTriggerOptions.START_SCANNING);
        return this;
    }

    @Keep
    @NonNull
    public DWVirtualScanner stopScan() {
        DataWedgeHelper.getInstance().softScanTrigger(context, DWAPI.SoftScanTriggerOptions.STOP_SCANNING);
        return this;
    }

    @Keep
    @NonNull
    public DWVirtualScanner close() {
        String profile = getProfileName();
        if (profile != null) {
            if (dataListener != null) {
                dataListener.onDisposal();
            }
            DataWedgeHelper.getInstance().deleteProfile(context, profile, success -> {
                Log.d(TAG, "close DWVirtualScanner success: " + profile);
            });
        }
        return this;
    }
}
