package com.zebra.zsdk_java_wrapper.emdk;

import android.content.Context;
import androidx.annotation.Keep;
import com.symbol.emdk.EMDKBase;
import com.symbol.emdk.EMDKException;
import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.ProfileManager;
import com.symbol.emdk.VersionManager;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Keep
public class EMDKCore {

    private EMDKManager _manager = null;
    private ProfileManager _profileManager = null;
    private VersionManager _versionManager = null;

    public VersionManager getVersionManager() {
        return _versionManager;
    }

    public ProfileManager getProfileManager() {
        return _profileManager;
    }

    public String getEmdkVersion() {
        if (_versionManager == null) {
            throw new RuntimeException("please call this after EMDKHelper.shared.prepare() return");
        }
        return _versionManager.getVersion(VersionManager.VERSION_TYPE.EMDK);
    }

    public String getMxVersion() {
        if (_versionManager == null) {
            throw new RuntimeException("please call this after EMDKHelper.shared.prepare() return");
        }
        return _versionManager.getVersion(VersionManager.VERSION_TYPE.MX);
    }

    public String getDwVersion() {
        if (_versionManager == null) {
            throw new RuntimeException("please call this after EMDKHelper.shared.prepare() return");
        }
        return _versionManager.getVersion(VersionManager.VERSION_TYPE.BARCODE);
    }

    @Keep
    public void prepareEMDKManager(Context context, Consumer<Boolean> complete) {
        if (_manager != null) {
            complete.accept(true);
            return;
        }
        EMDKManager.getEMDKManager(
            context.getApplicationContext(),
            new EMDKManager.EMDKListener() {

                @Keep
                @Override
                public void onOpened(EMDKManager manager) {
                    if (manager == null) {
                        complete.accept(false);
                        return;
                    }
                    _manager = manager;

                    try {
                        CompletableFuture<VersionManager> versionFuture = prepareVersionManager(manager);
                        CompletableFuture<ProfileManager> profileFuture = prepareProfileManager(manager);

                        versionFuture.thenCombine(profileFuture, (v, p) -> {
                                    _versionManager = v;
                                    _profileManager = p;
                                    return true;
                                }).thenAccept(complete::accept)
                                .exceptionally(ex -> {
                                    complete.accept(false);
                                    return null;
                                });
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

                @Keep
                @Override
                public void onClosed() {
                    teardown();
                }
            }
        );
    }

    public void teardown() {
        _versionManager = null;
        _profileManager = null;
        if (_manager != null) {
            _manager.release();
            _manager = null;
        }
    }

    @Keep
    private CompletableFuture<VersionManager> prepareVersionManager(EMDKManager emdkManager) throws EMDKException {
        CompletableFuture<VersionManager> future = new CompletableFuture<>();
        emdkManager.getInstanceAsync(
            EMDKManager.FEATURE_TYPE.VERSION,
            new EMDKManager.StatusListener() {

                @Keep
                @Override
                public void onStatus(EMDKManager.StatusData status, EMDKBase base) {
                    if (status == null) {
                        future.completeExceptionally(new Exception("status null"));
                        return;
                    }
                    if (status.getFeatureType() != EMDKManager.FEATURE_TYPE.VERSION) {
                        future.completeExceptionally(new Exception("wrong feature type"));
                        return;
                    }
                    if (!(base instanceof VersionManager)) {
                        future.completeExceptionally(new Exception("get null manager"));
                        return;
                    }
                    future.complete((VersionManager) base);
                }
            }
        );
        return future;
    }

    @Keep
    private CompletableFuture<ProfileManager> prepareProfileManager(EMDKManager emdkManager) throws EMDKException {
        CompletableFuture<ProfileManager> future = new CompletableFuture<>();
        emdkManager.getInstanceAsync(
            EMDKManager.FEATURE_TYPE.PROFILE,
            new EMDKManager.StatusListener() {

                @Keep
                @Override
                public void onStatus(EMDKManager.StatusData status, EMDKBase base) {
                    if (status == null) {
                        future.completeExceptionally(new Exception("status null"));
                        return;
                    }
                    if (status.getFeatureType() != EMDKManager.FEATURE_TYPE.PROFILE) {
                        future.completeExceptionally(new Exception("wrong feature type"));
                        return;
                    }
                    if (!(base instanceof ProfileManager)) {
                        future.completeExceptionally(new Exception("get null manager"));
                        return;
                    }
                    future.complete((ProfileManager) base);
                }
            }
        );
        return future;
    }
}
