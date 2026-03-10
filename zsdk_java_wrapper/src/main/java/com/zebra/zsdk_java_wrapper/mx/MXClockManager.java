package com.zebra.zsdk_java_wrapper.mx;

import android.content.Context;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * https://techdocs.zebra.com/mx/clock/
 */
public class MXClockManager {

    /**
     * Resets the clock to automatic mode with the specified NTP server and sync interval.
     */
    public static void callClockResetAuto(
            Context context,
            boolean is24Hours,
            String ntpServer,
            String syncInterval,
            long delaySeconds,
            Consumer<MXBase.ErrorInfo> callback) {
        
        String militaryTime = is24Hours ? "1" : "2";
        Map<String, String> map = new HashMap<>();
        map.put(MXConst.TimeZone, "GMT+9"); // ntp update every 30min, so use GMT+9 as default
        map.put(MXConst.NTPServer, ntpServer);
        map.put(MXConst.SyncInterval, syncInterval);
        map.put(MXConst.MilitaryTime, militaryTime);

        MXProfileProcessor.processProfileWithCallback(
                context,
                MXBase.ProfileXML.ClockResetAuto,
                MXBase.ProfileName.ClockResetAuto,
                map,
                delaySeconds,
                callback
        );
    }

    /**
     * Overload for callClockResetAuto with default delaySeconds = 0.
     */
    public static void callClockResetAuto(
            Context context,
            boolean is24Hours,
            String ntpServer,
            String syncInterval,
            Consumer<MXBase.ErrorInfo> callback) {
        callClockResetAuto(context, is24Hours, ntpServer, syncInterval, 0L, callback);
    }

    /**
     * Sets the clock manually with the specified date, time, and timezone.
     */
    public static void callClockSet(
            Context context,
            boolean is24Hours,
            String timeZone,
            String date,
            String time,
            long delaySeconds,
            Consumer<MXBase.ErrorInfo> callback) {
        
        String militaryTime = is24Hours ? "1" : "2";
        Map<String, String> map = new HashMap<>();
        map.put(MXConst.AutoTime, "false");
        map.put(MXConst.TimeZone, timeZone);
        map.put(MXConst.Date, date);
        map.put(MXConst.Time, time);
        map.put(MXConst.MilitaryTime, militaryTime);

        MXProfileProcessor.processProfileWithCallback(
                context,
                MXBase.ProfileXML.ClockSet,
                MXBase.ProfileName.ClockSet,
                map,
                delaySeconds,
                callback
        );
    }

    /**
     * Overload for callClockSet with default delaySeconds = 0.
     */
    public static void callClockSet(
            Context context,
            boolean is24Hours,
            String timeZone,
            String date,
            String time,
            Consumer<MXBase.ErrorInfo> callback) {
        callClockSet(context, is24Hours, timeZone, date, time, 0L, callback);
    }
}
