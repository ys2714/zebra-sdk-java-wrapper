package com.zebra.zsdk_java_wrapper.mx;

import android.content.Context;
import android.content.res.AssetManager;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class XMLReader {

    public static String readXmlFileToStringWithParams(Context context, String resourceId, Map<String, String> params) {

        String command1 = XMLReader.readXmlFileToString(context, resourceId).trim();
        if (params != null) {
            for (String key : params.keySet()) {
                String placeholder = "${"+key+"}" ;
                String value = params.get(key);
                if (value != null && command1.contains(placeholder)) {
                    command1 = command1.replace(placeholder, value);
                }
            }
        }
        return command1;
    }

    public static String readXmlFileToString(Context context, String resourceId) {
        return readXmlFileToString(context, resourceId, false);
    }

    public static String readXmlFileToString(Context context, String resourceId, boolean newline) {
        StringBuilder stringBuilder = new StringBuilder();
        InputStream inputStream = null;
        BufferedReader reader = null;

        try {
            AssetManager assetManager = context.getAssets();
            inputStream = assetManager.open(resourceId);
            // inputStream = context.getResources().openRawResource(resourceId);
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line.trim());
                if (newline) {
                    stringBuilder.append("\n"); // Add newline back if desired
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return stringBuilder.toString();
    }

    // Method to parse the XML response using XML Pull Parser
    public static MXBase.ErrorInfo parseXML(XmlPullParser myParser) {
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
}
