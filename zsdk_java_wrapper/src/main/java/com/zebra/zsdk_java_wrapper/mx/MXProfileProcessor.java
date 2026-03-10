package com.zebra.zsdk_java_wrapper.mx;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Xml;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.symbol.emdk.EMDKResults;
import com.symbol.emdk.ProfileManager;
import com.symbol.emdk.ProfileManager.DataListener;
import com.symbol.emdk.ProfileManager.ResultData;
import com.zebra.zsdk_java_wrapper.emdk.EMDKHelper;
import com.zebra.zsdk_java_wrapper.utils.AssetsReader;
import com.zebra.zsdk_java_wrapper.utils.FixedSizeQueue;
import com.zebra.zsdk_java_wrapper.utils.FixedSizeQueueItem;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.StringReader;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * https://techdocs.zebra.com/emdk-for-android/14-0/guide/profile-manager-guides/
 */
@Keep
public class MXProfileProcessor {

    public static final String TAG = MXProfileProcessor.class.getSimpleName();

    private static final Handler mainHandler = new Handler(Looper.getMainLooper());
    private static final ExecutorService backgroundExecutor = Executors.newCachedThreadPool();

    private static ProfileManager getProfileManager() {
        ProfileManager manager = EMDKHelper.getShared().getProfileManager();
        if (manager == null) {
            throw new RuntimeException("please call EMDKHelper.prepare() before get profileManager");
        }
        return manager;
    }

    // handling max 20 profiles at the same time, for the 21th profile, the first will be disposed
    private static final FixedSizeQueue<ProfileDataListener> listeners = new FixedSizeQueue<>(20);

    @Keep
    private static class ProfileDataListener implements DataListener, FixedSizeQueueItem {
        private final MXBase.ProfileName profileName;
        private final Consumer<MXBase.ErrorInfo> callback;

        public ProfileDataListener(MXBase.ProfileName profileName, Consumer<MXBase.ErrorInfo> callback) {
            this.profileName = profileName;
            this.callback = callback;
        }

        @Keep
        @Override
        public void onData(ResultData data) {
            if (data == null) {
                callback.accept(new MXBase.ErrorInfo("ProfileDataListener - receive null data !"));
                return;
            }
            if (data.getProfileName().equals(profileName.getString())) {
                EMDKResults.STATUS_CODE statusCode = data.getResult().statusCode;
                if (statusCode == EMDKResults.STATUS_CODE.SUCCESS) {
                    callback.accept(null);
                } else if (statusCode == EMDKResults.STATUS_CODE.CHECK_XML) {
                    try {
                        XmlPullParser parser = Xml.newPullParser();
                        parser.setInput(new StringReader(data.getResult().getStatusString()));
                        MXBase.ErrorInfo errorInfo = XMLParser.parseXML(parser);
                        callback.accept(errorInfo);
                    } catch (XmlPullParserException e) {
                        Log.e(TAG, "Failed to parse XML response", e);
                        callback.accept(new MXBase.ErrorInfo(TAG, "XmlPullParserException", e.getMessage() != null ? e.getMessage() : "Unknown parser exception"));
                    }
                } else {
                    Log.e(TAG, "failed with response: " + data.getResult().getStatusString());
                    callback.accept(new MXBase.ErrorInfo(TAG, data.getResult().getStatusString(), data.getResult().getExtendedStatusMessage()));
                }
            }
        }

        @NonNull
        @Override
        public String getID() {
            return profileName.getString();
        }

        @Override
        public void onDisposal() {
            getProfileManager().removeDataListener(this);
        }
    }

    public static void processProfileWithCallback(Context context,
                                                  MXBase.ProfileXML fileName,
                                                  MXBase.ProfileName profileName,
                                                  @Nullable Map<String, String> params,
                                                  long delaySeconds,
                                                  Consumer<MXBase.ErrorInfo> callback) {
        backgroundExecutor.execute(() -> {
            MXBase.ErrorInfo result;
            if (fileName == MXBase.ProfileXML.None) {
                result = processProfile(profileName, null).join();
            } else {
                String content = AssetsReader.readFileToStringWithParams(context, fileName.getString(), params).trim();
                result = processProfile(profileName, new String[]{content}).join();
            }

            if (delaySeconds > 0) {
                try {
                    Thread.sleep(delaySeconds * 1000);
                } catch (InterruptedException e) {
                    Log.e(TAG, "Delay interrupted", e);
                }
            }

            mainHandler.post(() -> callback.accept(result));
        });
    }

    static CompletableFuture<MXBase.ErrorInfo> processProfile(MXBase.ProfileName profileName,
                                                              @Nullable String[] profileContent) {
        CompletableFuture<MXBase.ErrorInfo> future = new CompletableFuture<>();
        
        final ProfileDataListener[] listenerRef = new ProfileDataListener[1];
        listenerRef[0] = new ProfileDataListener(profileName, errorInfo -> {
            listeners.remove(listenerRef[0]);
            future.complete(errorInfo);
        });

        listeners.enqueue(listenerRef[0]);
        getProfileManager().addDataListener(listenerRef[0]);

        EMDKResults results = getProfileManager().processProfileAsync(
                profileName.getString(),
                ProfileManager.PROFILE_FLAG.SET,
                profileContent);

        if (results.statusCode != EMDKResults.STATUS_CODE.PROCESSING) {
            listeners.remove(listenerRef[0]);
            getProfileManager().removeDataListener(listenerRef[0]);
            if (results.statusCode == EMDKResults.STATUS_CODE.SUCCESS) {
                future.complete(null);
            } else {
                future.complete(new MXBase.ErrorInfo(TAG, results.statusCode.toString(), results.getStatusString()));
            }
        }

        return future;
    }

    public static class DevAdminReceiver extends DeviceAdminReceiver {
    }
}
