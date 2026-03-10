package com.zebra.zsdk_java_wrapper.utils;

import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.Keep;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Keep
public class JsonUtils {

    @Keep
    public static String bundleToJson(Bundle bundle) {
        try {
            return toJsonObject(bundle).toString(4);
        } catch (JSONException e) {
            return "{}";
        }
    }

    private static JSONObject toJsonObject(Bundle bundle) throws JSONException {
        JSONObject json = new JSONObject();
        if (bundle == null) return json;

        for (String key : bundle.keySet()) {
            Object value = bundle.get(key);
            if (value instanceof Bundle) {
                json.put(key, toJsonObject((Bundle) value));
            } else if (value instanceof List) {
                JSONArray jsonArray = new JSONArray();
                for (Object item : (List<?>) value) {
                    if (item instanceof Bundle) {
                        jsonArray.put(toJsonObject((Bundle) item));
                    } else {
                        jsonArray.put(item);
                    }
                }
                json.put(key, jsonArray);
            } else if (value instanceof Object[]) {
                JSONArray jsonArray = new JSONArray();
                for (Object item : (Object[]) value) {
                    if (item instanceof Bundle) {
                        jsonArray.put(toJsonObject((Bundle) item));
                    } else {
                        jsonArray.put(item);
                    }
                }
                json.put(key, jsonArray);
            } else {
                json.put(key, value);
            }
        }
        return json;
    }

    @Keep
    public static Bundle jsonToBundle(String jsonString) {
        try {
            JSONObject jsonObj = new JSONObject(jsonString);
            return toBundle(jsonObj);
        } catch (JSONException e) {
            return new Bundle();
        }
    }

    private static Bundle toBundle(JSONObject jsonObject) throws JSONException {
        Bundle bundle = new Bundle();
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object value = jsonObject.get(key);
            if (value instanceof JSONObject) {
                bundle.putBundle(key, toBundle((JSONObject) value));
            } else if (value instanceof JSONArray) {
                JSONArray list = (JSONArray) value;
                if (list.length() > 0) {
                    Object firstItem = list.get(0);
                    if (firstItem instanceof JSONObject) {
                        ArrayList<Parcelable> parcelables = new ArrayList<>();
                        for (int i = 0; i < list.length(); i++) {
                            JSONObject item = (JSONObject) list.get(i);
                            parcelables.add(toBundle(item));
                        }
                        bundle.putParcelableArrayList(key, parcelables);
                    } else if (firstItem instanceof String) {
                        ArrayList<String> strings = new ArrayList<>();
                        for (int i = 0; i < list.length(); i++) {
                            strings.add((String) list.get(i));
                        }
                        bundle.putStringArrayList(key, strings);
                    }
                }
            } else if (value instanceof String) {
                bundle.putString(key, (String) value);
            } else if (value instanceof Boolean) {
                bundle.putBoolean(key, (Boolean) value);
            } else if (value instanceof Integer) {
                bundle.putInt(key, (Integer) value);
            } else if (value instanceof Long) {
                bundle.putLong(key, (Long) value);
            } else if (value instanceof Double) {
                bundle.putDouble(key, (Double) value);
            }
        }
        return bundle;
    }

    public static String trimNewLines(String str) {
        if (str == null) return null;
        return str.replace("\n", "").replace("\r", "");
    }

    public static String trimSpace(String str) {
        if (str == null) return null;
        return str.replaceAll("\\s+", " ");
    }

    public static String compressStringByTrimAll(String str) {
        if (str == null) return null;
        return trimSpace(trimNewLines(trimIndent(str.trim())));
    }

    public static String compressStringByTrim(String str) {
        if (str == null) return null;
        return trimNewLines(trimIndent(str.trim()));
    }

    /**
     * Simple implementation of Kotlin's trimIndent() for Java.
     */
    private static String trimIndent(String str) {
        if (str == null || str.isEmpty()) return str;
        String[] lines = str.split("\n", -1);
        int minIndent = Integer.MAX_VALUE;
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            int indent = 0;
            while (indent < line.length() && Character.isWhitespace(line.charAt(indent))) {
                indent++;
            }
            if (indent < minIndent) minIndent = indent;
        }

        if (minIndent == Integer.MAX_VALUE) return str;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (line.length() >= minIndent) {
                sb.append(line.substring(minIndent));
            } else {
                sb.append(line);
            }
            if (i < lines.length - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
