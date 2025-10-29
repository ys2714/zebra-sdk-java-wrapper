package com.zebra.zsdk_java_wrapper.mx;

import android.os.AsyncTask;
import android.util.Xml;

import com.symbol.emdk.EMDKResults;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.StringReader;

class ProcessProfileTask extends AsyncTask<Void, Void, EMDKResults> {

    public interface Delegate {
        EMDKResults processProfile();
        void processProfileSuccess();
        void processProfileFailure(MXBase.ErrorInfo errorInfo);
    }

    private Delegate delegate = null;

    ProcessProfileTask(Delegate delegate) {
        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected EMDKResults doInBackground(Void... voids) {
        //Call process profile to modify the profile of specified profile name
        // EMDKResults results = profileManager.processProfile(this.profileName, ProfileManager.PROFILE_FLAG.SET, params);
        return delegate.processProfile();
    }

    @Override
    protected void onPostExecute(EMDKResults results) {

        super.onPostExecute(results);

        String resultString = "";

        if (results.statusCode == EMDKResults.STATUS_CODE.SUCCESS) {
            delegate.processProfileSuccess();
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
                MXBase.ErrorInfo errorInfo = XMLReader.parseXML(parser);

                if (errorInfo == null) {
                    resultString = "Profile update success.";
                    delegate.processProfileSuccess();
                }
                else {
                    delegate.processProfileFailure(errorInfo);
                }

            } catch (XmlPullParserException e) {
                resultString =  e.getMessage();
                MXBase.ErrorInfo errorInfo = new MXBase.ErrorInfo();
                errorInfo.errorType = "XmlPullParserException";
                errorInfo.errorDescription = resultString;
                delegate.processProfileFailure(errorInfo);
            }
        }
    }
}
