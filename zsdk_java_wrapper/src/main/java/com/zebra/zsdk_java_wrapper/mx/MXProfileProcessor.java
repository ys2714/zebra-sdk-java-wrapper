package com.zebra.zsdk_java_wrapper.mx;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Xml;

import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.EMDKResults;
import com.symbol.emdk.ProfileManager;
import com.symbol.emdk.EMDKManager.EMDKListener;
import com.zebra.zsdk_java_wrapper.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

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

    private ProcessProfileTask currentTask = null;

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

    public void callAccessManagerAllowCallService(String serviceIdentifier, String callerPackageName, String callerSignature) {
        Map<String, String> map = new HashMap<>();
        map.put(MXConst.serviceIdentifier, serviceIdentifier);
        map.put(MXConst.callerPackageName, callerPackageName);
        map.put(MXConst.callerSignature, callerSignature);
        processProfile(R.raw.profile_access_manager_allow_call_service, MXConst.AccessManagerAllowCallService, map);
    }

    public void callAccessManagerAllowPermission(String permissionName, String appPackageName, String appClassName, String appSignature) {
        Map<String, String> map = new HashMap<>();
        map.put(MXConst.permissionAccessPermissionName, permissionName);
        map.put(MXConst.permissionAccessAction, "1"); // 1: allow
        map.put(MXConst.permissionAccessPackageName, appPackageName);
        map.put(MXConst.applicationClassName, appClassName);
        map.put(MXConst.permissionAccessSignature, appSignature);
        processProfile(R.raw.profile_access_manager_allow_permission, MXConst.AccessManagerAllowPermission, map);
    }

    public void callAppManagerInstallAndStart(String apkPath, String packageName, String mainActivity) {
        Map<String, String> map = new HashMap<>();
        map.put("apkFilePath", apkPath);
        map.put("appPackageName", packageName);
        map.put("mainActivityClass", mainActivity);
        processProfile(R.raw.profile_app_manager_install_and_start, MXConst.AppManagerInstallAndStart, map);
    }

    public void callPowerManagerFeature(MXBase.PowerManagerOptions option) {
        callPowerManagerFeature(option, null);
    }

    public void callPowerManagerFeature(MXBase.PowerManagerOptions option, String osZipFilePath) {
        Map<String, String> map = new HashMap<>();
        switch (option) {
            case CREATE_PROFILE:
                break;
            case DO_NOTHING:
                break;
            case SLEEP_MODE:
                map.put(MXConst.resetAction, option.valueString());
                processProfile(R.raw.profile_power_manager_reset, MXConst.PowerManagerReset, map);
                break;
            case REBOOT:
                map.put(MXConst.resetAction, option.valueString());
                processProfile(R.raw.profile_power_manager_reset, MXConst.PowerManagerReset, map);
                break;
            case ENTERPRISE_RESET:
                map.put(MXConst.resetAction, option.valueString());
                processProfile(R.raw.profile_power_manager_reset, MXConst.PowerManagerReset, map);
                break;
            case FACTORY_RESET:
                map.put(MXConst.resetAction, option.valueString());
                processProfile(R.raw.profile_power_manager_reset, MXConst.PowerManagerReset, map);
                break;
            case FULL_DEVICE_WIPE:
                map.put(MXConst.resetAction, option.valueString());
                processProfile(R.raw.profile_power_manager_reset, MXConst.PowerManagerReset, map);
                break;
            case OS_UPDATE:
                map.put(MXConst.resetAction, option.valueString());
                map.put(MXConst.zipFile, osZipFilePath);
                processProfile(R.raw.profile_power_manager_reset, MXConst.PowerManagerReset, map);
                break;
        }
    }

    public void processProfile(int profileResId, String profileName) {
        processProfile(profileResId, profileName, null);
    }

    // please use: com.zebra.zsdk_java_wrapper.R
    public void processProfile(int profileResId, String profileName, Map<String, String> params) {
        String command1 = XMLReader.readXmlFileToString(this.context, profileResId).trim();
        if (params != null) {
            for (String key : params.keySet()) {
                String placeholder = "${"+key+"}" ;
                String value = params.get(key);
                if (value != null && command1.contains(placeholder)) {
                   command1 = command1.replace(placeholder, value);
                }
            }
        }
        if (currentTask != null) {
            currentTask.cancel(true);
            currentTask = null;
        }
        currentTask = new ProcessProfileTask(profileName);
        currentTask.execute(command1.trim());
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

    @SuppressLint("StaticFieldLeak")
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

            if (results.statusCode == EMDKResults.STATUS_CODE.SUCCESS) {
                MXProfileProcessor.this.listener.onEMDKProcessProfileSuccess(profileName);
                return;
            }
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
