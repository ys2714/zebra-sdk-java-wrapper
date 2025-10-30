package com.zebra.zsdk_java_wrapper.mx;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import com.symbol.emdk.EMDKResults;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.StringReader;

class ProcessProfileTask extends AsyncTask<Void, Void, EMDKResults> {

    private static final String TAG = ProcessProfileTask.class.getSimpleName();

    public interface Delegate {
        EMDKResults processProfile();
        void processProfileSuccess();
        void processProfileFailure(MXBase.ErrorInfo errorInfo);
    }

    private final Delegate delegate;

    ProcessProfileTask(Delegate delegate) {
        this.delegate = delegate;
    }

    @Override
    protected EMDKResults doInBackground(Void... voids) {
        return delegate.processProfile();
    }

    @Override
    protected void onPostExecute(EMDKResults results) {
        super.onPostExecute(results);

        if (results.statusCode == EMDKResults.STATUS_CODE.SUCCESS) {
            delegate.processProfileSuccess();
            return;
        }

        if (results.statusCode == EMDKResults.STATUS_CODE.CHECK_XML) {
            String statusXMLResponse = results.getStatusString();
            try {
                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(new StringReader(statusXMLResponse));
                MXBase.ErrorInfo errorInfo = XMLReader.parseXML(parser);

                if (errorInfo == null) {
                    // This can happen if the XML does not contain an error, but the status was not SUCCESS.
                    // We can treat this as a success, or handle it as a specific case.
                    delegate.processProfileSuccess();
                } else {
                    delegate.processProfileFailure(errorInfo);
                }
            } catch (XmlPullParserException e) {
                Log.e(TAG, "Failed to parse XML response", e);
                delegate.processProfileFailure(
                        new MXBase.ErrorInfo(TAG, "XmlPullParserException", e.getMessage()));
            }
        } else {
            // Handle other potential failure status codes that don't provide an XML response.
            Log.e(TAG, "Profile processing failed with status code: " + results.statusCode);
            delegate.processProfileFailure(
                    new MXBase.ErrorInfo(TAG, "ProfileError", "Profile processing failed with status: " + results.statusCode));
        }
    }
}
