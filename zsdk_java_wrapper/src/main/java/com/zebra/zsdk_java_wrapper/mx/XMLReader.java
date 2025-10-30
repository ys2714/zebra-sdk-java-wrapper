package com.zebra.zsdk_java_wrapper.mx;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class XMLReader {

    private static final String TAG = XMLReader.class.getSimpleName();

    public static String readXmlFileToStringWithParams(Context context, String resourceId, Map<String, String> params) {
        String command = readXmlFileToString(context, resourceId).trim();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String placeholder = String.format("${%s}", entry.getKey());
                String value = entry.getValue();
                if (value != null) {
                    command = command.replace(placeholder, value);
                }
            }
        }
        return command;
    }

    public static String readXmlFileToString(Context context, String resourceId) {
        return readXmlFileToString(context, resourceId, false);
    }

    public static String readXmlFileToString(Context context, String resourceId, boolean newline) {
        StringBuilder stringBuilder = new StringBuilder();
        AssetManager assetManager = context.getAssets();

        try (InputStream inputStream = assetManager.open(resourceId);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line.trim());
                if (newline) {
                    stringBuilder.append("\n");
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Error reading XML file from assets: " + resourceId, e);
        }
        return stringBuilder.toString();
    }

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
