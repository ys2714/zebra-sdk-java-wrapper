package com.zebra.zsdk_java_wrapper;

import android.content.Context;
import android.text.TextUtils;
import android.util.Xml;

import com.symbol.emdk.*;
import com.symbol.emdk.EMDKManager.EMDKListener;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.StringReader;

public class MXPowerManagerHelper implements EMDKListener {

    // contains status of the profile operation
    private String profileName = null;
    private MXBase.EventListener listener = null;
    private ProfileManager profileManager = null;
    private EMDKManager emdkManager = null;

    public MXPowerManagerHelper(MXBase.EventListener listener) {
        this.listener = listener;
    }

    public void setupEMDK(Context ctx, String profileName) {
        this.profileName = profileName;

        //The EMDKManager object will be created and returned in the callback.
        EMDKResults results = EMDKManager.getEMDKManager(ctx.getApplicationContext(), this);

        //Check the return status of getEMDKManager
        if (results.statusCode == EMDKResults.STATUS_CODE.SUCCESS) {
            // EMDKManager object creation success
        } else {
            // EMDKManager object creation failed
        }
    }

    public void releaseEMDK() {
        emdkManager.release();
        emdkManager = null;
        profileManager = null;
    }

    @Override
    public void onOpened(EMDKManager emdkManager) {
        // This callback will be issued when the EMDK is ready to use.
        // Get the ProfileManager object to process the profiles
        profileManager = (ProfileManager) emdkManager
                .getInstance(EMDKManager.FEATURE_TYPE.PROFILE);

        callFeature(MXBase.PowerManagerOptions.CREATE_PROFILE, null);

        listener.onEMDKSessionOpened();
    }

    @Override
    public void onClosed() {
        listener.onEMDKSessionClosed();
    }

    // Method to handle EMDKResult by extracting response and parsing it
    private void handleEMDKResult(EMDKResults results) {
        // Get XML response as a String
        String statusXMLResponse = results.getStatusString();
        MXBase.ErrorInfo errorInfo = null;
        try {
            // Create instance of XML Pull Parser to parse the response
            XmlPullParser parser = Xml.newPullParser();
            // Provide the string response to the String Reader that reads
            // for the parser
            parser.setInput(new StringReader(statusXMLResponse));
            // Call method to parse the response
            errorInfo = parseXML(parser);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        if (errorInfo != null) {
            listener.onEMDKError(errorInfo);
        }
    }

    // Method to parse the XML response using XML Pull Parser
    private MXBase.ErrorInfo parseXML(XmlPullParser myParser) {
        MXBase.ErrorInfo errorInfo = null;
        int event;
        try {
            event = myParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = myParser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        // Get Status, error name and description in case of
                        // parm-error
                        if (name.equals("parm-error")) {
                            errorInfo = new MXBase.ErrorInfo();
                            errorInfo.errorName = myParser.getAttributeValue(null, "name");
                            errorInfo.errorDescription = myParser.getAttributeValue(null,
                                    "desc");

                            // Get Status, error type and description in case of
                            // parm-error
                        } else if (name.equals("characteristic-error")) {
                            errorInfo = new MXBase.ErrorInfo();
                            errorInfo.errorType = myParser.getAttributeValue(null, "type");
                            errorInfo.errorDescription = myParser.getAttributeValue(null,
                                    "desc");
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = myParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorInfo.errorName = "unknown error";
            errorInfo.errorDescription = e.getLocalizedMessage();
        }
        return errorInfo;
    }

    // Method that applies the modified settings to the EMDK Profile based on
    // user selected options of Power Manager feature.
    public void callFeature(MXBase.PowerManagerOptions option, String zipFilePath) {
        int value = option.value;

        if (profileManager == null) {
            profileManager = (ProfileManager) emdkManager
                    .getInstance(EMDKManager.FEATURE_TYPE.PROFILE);
        }

        // Prepare XML to modify the existing profile
        String[] modifyData = new String[1];
        if (option == MXBase.PowerManagerOptions.CREATE_PROFILE) {
            // Call processPrfoile with profile name and SET flag to create the
            // profile. The modifyData can be null.
        } else if (option == MXBase.PowerManagerOptions.OS_UPDATE) {
            // String that gets the path of the OS Update Package from Edit Text
            // If the OS Package path entered by user is empty then display
            // a Toast
            if (TextUtils.isEmpty(zipFilePath)) {
                MXBase.ErrorInfo errorInfo = new MXBase.ErrorInfo();
                errorInfo.errorType = "File Path";
                errorInfo.errorName = "Incorrect Path";
                errorInfo.errorDescription = "Incorrect File Path...";
                listener.onEMDKError(errorInfo);

                return;
            }

            // Modified XML input for OS Update feature that contains path
            // to the update package
            modifyData[0] = "&lt;?xml version=\"1.0\" encoding=\"utf-8\"?&gt;"
                    + "&lt;characteristic type=\"Profile\"&gt;"
                    + "&lt;parm name=\"ProfileName\" value=\"" + profileName + "\"/&gt;"
                    + "&lt;characteristic type=\"PowerMgr\"&gt;"
                    + "&lt;parm name=\"ResetAction\" value=\"" + value + "\"/&gt;"
                    + "&lt;characteristic type=\"file-details\"&gt;"
                    + "&lt;parm name=\"ZipFile\" value=\"" + zipFilePath + "\"/&gt;"
                    + "&lt;/characteristic&gt;" + "&lt;/characteristic&gt;"
                    + "&lt;/characteristic&gt;";

        } else {
            // Modified XML input for Sleep and Reboot feature based on user
            // selected options of radio button
            // value = 1 -> Sleep Mode
            // value = 4 -> Rebbot
            modifyData[0] = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                    + "<characteristic type=\"Profile\">"
                    + "<parm name=\"ProfileName\" value=\"" + profileName + "\"/>"
                    + "<characteristic type=\"PowerMgr\">"
                    + "<parm name=\"ResetAction\" value=\"" + value + "\"/>"
                    + "</characteristic>" + "</characteristic>";
        }

        // Call process profile to modify the profile of specified profile
        // name
        EMDKResults results = profileManager.processProfile(profileName,
                ProfileManager.PROFILE_FLAG.SET, modifyData);

        if (option == MXBase.PowerManagerOptions.CREATE_PROFILE) {
            return;
        }

        if (results.statusCode == EMDKResults.STATUS_CODE.CHECK_XML) {
            // Method call to handle EMDKResult
            handleEMDKResult(results);
        } else if (results.statusCode == EMDKResults.STATUS_CODE.FAILURE) {
            MXBase.ErrorInfo errorInfo = new MXBase.ErrorInfo();
            errorInfo.errorType = "set profile";
            errorInfo.errorName = "set profile error";
            errorInfo.errorDescription = results.statusCode.toString();
            listener.onEMDKError(errorInfo);
        } else {
            MXBase.ErrorInfo errorInfo = new MXBase.ErrorInfo();
            errorInfo.errorType = "set profile";
            errorInfo.errorName = "unknown error";
            errorInfo.errorDescription = results.statusCode.toString();
            listener.onEMDKError(errorInfo);
        }
    }
}
