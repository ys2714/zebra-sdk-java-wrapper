package com.zebra.zsdk_java_wrapper.zdm;

import androidx.annotation.Keep;

@Keep
public class ZDMTokenStore {

    private static String dwQueryAccessToken = "";
    private static String dwRuntimeAccessToken = "";
    private static String dwNotificationAccessToken = "";
    private static String dwConfigAccessToken = "";

    @Keep
    public static void saveToken(ZDMConst.DelegationScope scope, String accessToken) {
        if (accessToken == null) return;
        switch (scope) {
            case SCOPE_DW_QUERY_API:
                dwQueryAccessToken = accessToken;
                break;
            case SCOPE_DW_RUNTIME_API:
                dwRuntimeAccessToken = accessToken;
                break;
            case SCOPE_DW_NOTIFICATION_API:
                dwNotificationAccessToken = accessToken;
                break;
            case SCOPE_DW_CONFIG_API:
                dwConfigAccessToken = accessToken;
                break;
        }
    }

    @Keep
    public static String getToken(ZDMConst.DelegationScope scope) {
        switch (scope) {
            case SCOPE_DW_QUERY_API:
                return dwQueryAccessToken;
            case SCOPE_DW_RUNTIME_API:
                return dwRuntimeAccessToken;
            case SCOPE_DW_NOTIFICATION_API:
                return dwNotificationAccessToken;
            case SCOPE_DW_CONFIG_API:
                return dwConfigAccessToken;
            default:
                return "";
        }
    }
}
