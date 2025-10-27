package com.zebra.zsdk_java_wrapper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Xml;

import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.EMDKResults;
import com.symbol.emdk.ProfileManager;
import com.symbol.emdk.EMDKManager.EMDKListener;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.StringReader;

public class MXProfileProcessor {

    private class EMDKEventHandler implements EMDKListener {

        @Override
        public void onOpened(EMDKManager emdkManager) {
            MXProfileProcessor.this.emdkManager = emdkManager;
            MXProfileProcessor.this.profileManager = (ProfileManager) emdkManager.getInstance(EMDKManager.FEATURE_TYPE.PROFILE);
            MXProfileProcessor.this.listener.onEMDKSessionOpened();
        }

        @Override
        public void onClosed() {
            if (MXProfileProcessor.this.emdkManager != null) {
                MXProfileProcessor.this.emdkManager.release();
                MXProfileProcessor.this.emdkManager = null;
            }
            MXProfileProcessor.this.listener.onEMDKSessionClosed();
        }
    }

    private EMDKEventHandler eventHandler = new EMDKEventHandler();

    //Declare a variable to store ProfileManager object
    private ProfileManager profileManager = null;

    //Declare a variable to store EMDKManager object
    private EMDKManager emdkManager = null;

    private MXBase.EventListener listener = null;

    private Context context = null;

    public MXProfileProcessor(MXBase.EventListener listener) {
        this.listener = listener;
    }

    public void connectEMDK(Context ctx) {
        this.context = ctx;

        //The EMDKManager object will be created and returned in the callback.
        EMDKResults results = EMDKManager.getEMDKManager(ctx.getApplicationContext(), this.eventHandler);

        //Check the return status of EMDKManager object creation.
        if (results.statusCode == EMDKResults.STATUS_CODE.SUCCESS) {
            //EMDKManager object creation success
            //Status: EMDK object creation success
        }else {
            //EMDKManager object creation failed
            //Status: EMDK object creation failed
            MXBase.ErrorInfo errorInfo = new MXBase.ErrorInfo();
            errorInfo.errorType = "EMDKManager";
            errorInfo.errorDescription = "EMDKManager object creation failed";
            this.listener.onEMDKError(errorInfo);
        }
    }

    public void disconnectEMDK() {
        //Clean up the objects created by EMDK manager
        if (profileManager != null)
            profileManager = null;

        if (emdkManager != null) {
            emdkManager.release();
            emdkManager = null;
        }
    }

    // please use: com.zebra.zsdk_java_wrapper.R
    public void processProfile(int profileResId, String profileName) {
        String command1 = XMLReader.readXmlFileToString(this.context, profileResId);
        new ProcessProfileTask(profileName).execute(command1);
    }

    // Method to parse the XML response using XML Pull Parser
    private static MXBase.ErrorInfo parseXML(XmlPullParser myParser) {
        MXBase.ErrorInfo errorInfo = null;
        int event;
        try {
            // Retrieve error details if parm-error/characteristic-error in the response XML
            event = myParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = myParser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:

                        if (name.equals("parm-error")) {
                            errorInfo = new MXBase.ErrorInfo();
                            errorInfo.errorName = myParser.getAttributeValue(null, "name");
                            errorInfo.errorDescription = myParser.getAttributeValue(null, "desc");
                            return errorInfo;
                        }

                        if (name.equals("characteristic-error")) {
                            errorInfo = new MXBase.ErrorInfo();
                            errorInfo.errorType = myParser.getAttributeValue(null, "type");
                            errorInfo.errorDescription = myParser.getAttributeValue(null, "desc");
                            return errorInfo;
                        }

                        break;
                    case XmlPullParser.END_TAG:

                        break;
                }
                event = myParser.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return errorInfo;
    }

    private class ProcessProfileTask extends AsyncTask<String, Void, EMDKResults> {

        private String profileName = "";

        ProcessProfileTask(String profileName) {
            this.profileName = profileName;
        }

        @Override
        protected EMDKResults doInBackground(String... params) {
            //Call process profile to modify the profile of specified profile name
            EMDKResults results = profileManager.processProfile(this.profileName, ProfileManager.PROFILE_FLAG.SET, params);
            return results;
        }

        @Override
        protected void onPostExecute(EMDKResults results) {

            super.onPostExecute(results);

            String resultString = "";

            //Check the return status of processProfile
            if(results.statusCode == EMDKResults.STATUS_CODE.CHECK_XML) {

                // Get XML response as a String
                String statusXMLResponse = results.getStatusString();

                try {
                    // Create instance of XML Pull Parser to parse the response
                    XmlPullParser parser = Xml.newPullParser();
                    // Provide the string response to the String Reader that reads
                    // for the parser
                    parser.setInput(new StringReader(statusXMLResponse));
                    // Call method to parse the response
                    MXBase.ErrorInfo errorInfo = parseXML(parser);

                    if (errorInfo == null) {
                        resultString = "Profile update success.";
                        MXProfileProcessor.this.listener.onEMDKProcessProfileSuccess(profileName);
                    }
                    else {
                        MXProfileProcessor.this.listener.onEMDKError(errorInfo);
                    }

                } catch (XmlPullParserException e) {
                    resultString =  e.getMessage();
                    MXBase.ErrorInfo errorInfo = new MXBase.ErrorInfo();
                    errorInfo.errorType = "XmlPullParserException";
                    errorInfo.errorDescription = resultString;
                    MXProfileProcessor.this.listener.onEMDKError(errorInfo);
                }
            }
        }
    }
}
