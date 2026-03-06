package com.zebra.zsdk_java_wrapper.mx;

import android.util.Log;
import android.util.Xml;

import com.symbol.emdk.ProfileManager;
import com.zebra.zsdk_java_wrapper.utils.FixedSizeQueueItem;
import com.zebra.zsdk_java_wrapper.utils.Result;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.StringReader;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ProfileDataListener implements ProfileManager.DataListener, FixedSizeQueueItem {

    private final MXBase.ProfileName profileName;
    private final BiConsumer<ProfileDataListener, MXBase.ErrorInfo> onDataCallback;
    private final Consumer<ProfileDataListener> onDisposalCallback;

    public ProfileDataListener(
            MXBase.ProfileName profileName,
            BiConsumer<ProfileDataListener, MXBase.ErrorInfo> onDataCallback,
            Consumer<ProfileDataListener> onDisposalCallback
    ) {
        this.profileName = profileName;
        this.onDataCallback = onDataCallback;
        this.onDisposalCallback = onDisposalCallback;
    }

    @Override
    public void onData(ProfileManager.ResultData data) {
        if (data == null) {
            onDataCallback.accept(this, new MXBase.ErrorInfo("ProfileDataListener - receive null data !"));
            return;
        }

        if (data.getProfileName().equals(profileName.getString())) {
            switch (data.getResult().statusCode) {
                case SUCCESS:
                    onDataCallback.accept(this, null);
                    break;

                case CHECK_XML:
                    try {
                        XmlPullParser parser = Xml.newPullParser();
                        parser.setInput(new StringReader(data.getResult().getStatusString()));

                        // Assuming XMLParser.parseXML returns a Result object and has a functional interface
                        MXBase.ErrorInfo errorInfo = XMLParser.parseXML(parser);
                        if (errorInfo == null) {
                            onDataCallback.accept(this, null);
                        } else {
                            onDataCallback.accept(this, errorInfo);
                        }

                    } catch (XmlPullParserException e) {
                        Log.e(MXProfileProcessor.TAG, "Failed to parse XML response", e);
                        String message = e.getMessage() != null ? e.getMessage() : "Unknown parser exception";
                        MXBase.ErrorInfo error = new MXBase.ErrorInfo(
                                MXProfileProcessor.TAG,
                                "XmlPullParserException",
                                message
                        );
                        onDataCallback.accept(this, error);
                    }
                    break;

                default:
                    Log.e(MXProfileProcessor.TAG, "failed with response: " + data.getResult().getStatusString());
                    MXBase.ErrorInfo error = new MXBase.ErrorInfo(
                            MXProfileProcessor.TAG,
                            data.getResult().getStatusString(),
                            data.getResult().getExtendedStatusMessage()
                    );
                    onDataCallback.accept(this, error);
                    break;
            }
        } else {
            // other profile name, ignore
        }
    }

    @Override
    public String getID() {
        return profileName.getString();
    }

    @Override
    public void onDisposal() {
        onDisposalCallback.accept(this);
    }
}
