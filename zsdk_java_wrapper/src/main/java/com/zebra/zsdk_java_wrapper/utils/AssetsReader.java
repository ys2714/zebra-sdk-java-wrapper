package com.zebra.zsdk_java_wrapper.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class AssetsReader {

    private static final String TAG = "AssetsReader";

    private AssetsReader() {}

    public static String readFileToStringWithParams(Context context,
                                                    String fileName,
                                                    Map<String, String> params) {
        final Map<String, String> pmap = params != null ? params : Collections.emptyMap();

        String command = readFileToString(
                context,
                fileName,
                line -> {
                    String result = null;
                    final String prefix = "=\\["; // Escaped '['
                    final String suffix = "\\]";  // Escaped ']'
                    final String pattern = prefix + "(.*?)" + suffix;
                    final Pattern placeholderRegex = Pattern.compile(pattern);
                    final Matcher placeholderResult = placeholderRegex.matcher(line);

                    if (!placeholderResult.find()) {
                        // normal static line
                        result = line;
                    } else {
                        // line with parameters
                        final String placeholder = placeholderResult.group(0);
                        for (Map.Entry<String, String> entity : pmap.entrySet()) {
                            final String key = entity.getKey();
                            final String value = entity.getValue();
                            if (line.contains("\"" + key + "\"")) {
                                // find the param
                                if (line.contains(placeholder)) {
                                    // has variable value
                                    result = line.replace(placeholder, value);
                                    Log.d(TAG, "replace " + placeholder + " with " + value + " in " + fileName);
                                } else {
                                    // has fixed default value
                                    result = line;
                                }
                                // already find the param, do not check other parameters
                                break;
                            }
                        }
                        // no input for the param, skip the line if it was not mandatory
                        if (result == null) {
                            if (placeholder.toUpperCase().equals(placeholder)) {
                                throw new RuntimeException("mandatory parameter not filled: " + placeholder);
                            }
                            if (Pattern.compile("(\\{|\\[)+\\s+\"").matcher(line).find()) {
                                throw new RuntimeException("can not skip content before first delimiter: " + line);
                            }
                            if (Pattern.compile("\"\\s+(\\}|\\])+").matcher(line).find()) {
                                throw new RuntimeException("can not skip content after last delimiter: " + line);
                            }
                            result = null;
                        }
                    }
                    // return the result line
                    return result;
                }
        );

        // check the parameters
        if (params != null) {
            params.forEach((key, value) -> {
                String placeholder = "=[" + key + "]";
                if (command.contains(placeholder)) {
                    throw new RuntimeException("parameter: " + placeholder + " in XML not filled correctly");
                }
            });
        }
        return command;
    }

    public static String readFileToString(Context context,
                                          String fileName,
                                          Function<String, String> lineProcessor) {
        StringBuilder stringBuilder = new StringBuilder();
        AssetManager assetManager = context.getAssets();
        char delimiter;
        if (fileName.contains(".xml")) {
            delimiter = '>';
        } else if (fileName.contains(".json")) {
            delimiter = ',';
        } else {
            delimiter = '\n';
        }

        try {
            InputStream inputStream = assetManager.open(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            int nextChar;
            while ((nextChar = bufferedReader.read()) != -1) {
                StringBuilder lineBuilder = new StringBuilder().append((char) nextChar);
                while ((nextChar = bufferedReader.read()) != -1 && (char) nextChar != delimiter) {
                    lineBuilder.append((char) nextChar);
                }
                // We need to manually append the delimiter if it's not the end of the file.
                if((char) nextChar == delimiter) {
                    lineBuilder.append((char)nextChar);
                }

                String cleanLine = compressStringByTrim(lineBuilder.toString());
                String processedLine = lineProcessor.apply(cleanLine);
                if (processedLine != null && !processedLine.isEmpty()) {
                    stringBuilder.append(processedLine);
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Error reading file from assets: " + fileName, e);
        }

        String result = stringBuilder.toString();
        if (delimiter == ',') {
            if (result.endsWith(",")) {
                result = result.substring(0, result.length() - 1);
            }
            result = result.replace(",}", "}");
            result = result.replace(",]", "]");
            result = result.replace(", }", "}");
            result = result.replace(", ]", "]");
        }
        return result;
    }

    private static String trimNewLines(String input) {
        return input.replace("\n", "").replace("\r", "");
    }

    private static String trimIndent(String input) {
        return input.strip();
    }

    private static String compressStringByTrim(String input) {
        return trimNewLines(trimIndent(input.trim()));
    }
}