package com.zebra.zsdk_java_wrapper.mx;

import android.util.Log;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;

public class XMLParser {

    private static final String TAG = XMLParser.class.getSimpleName();

    // Method to parse the XML response using XML Pull Parser
    public static MXBase.ErrorInfo parseXML(XmlPullParser myParser) {
        try {
            int event = myParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                if (event == XmlPullParser.START_TAG) {
                    String name = myParser.getName();
                    if ("parm-error".equals(name)) {
                        MXBase.ErrorInfo errorInfo = new MXBase.ErrorInfo();
                        errorInfo.errorName = myParser.getAttributeValue(null, "name");
                        errorInfo.errorDescription = myParser.getAttributeValue(null, "desc");
                        return errorInfo;
                    }

                    if ("characteristic-error".equals(name)) {
                        MXBase.ErrorInfo errorInfo = new MXBase.ErrorInfo();
                        errorInfo.errorType = myParser.getAttributeValue(null, "type");
                        errorInfo.errorDescription = myParser.getAttributeValue(null, "desc");
                        return errorInfo;
                    }
                }
                event = myParser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            Log.e(TAG, "Failed to parse XML response", e);
        }
        return null;
    }
}
