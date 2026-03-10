package com.zebra.zsdk_java_wrapper.emdk;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.Keep;
import com.symbol.emdk.ProfileManager;
import com.symbol.emdk.VersionManager;
import java.util.function.Consumer;

@Keep
public class EMDKHelper {

    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    public final EMDKCore core = new EMDKCore();

    private static EMDKHelper instance;

    private EMDKHelper() {}

    @Keep
    public static synchronized EMDKHelper getShared() {
        if (instance == null) {
            instance = new EMDKHelper();
        }
        return instance;
    }

    @Keep
    public String getEMDKVersion() {
        return core.getEmdkVersion();
    }

    @Keep
    public String getMXVersion() {
        return core.getMxVersion();
    }

    @Keep
    public String getDWVersion() {
        return core.getDwVersion();
    }

    public ProfileManager getProfileManager() {
        return core.getProfileManager();
    }

    public VersionManager getVersionManager() {
        return core.getVersionManager();
    }

    @Keep
    public void prepare(Context context, Consumer<Boolean> callback) {
        core.prepareEMDKManager(context, success -> {
            mainHandler.post(() -> callback.accept(success));
        });
    }

    @Keep
    public void teardown() {
        core.teardown();
    }
}
