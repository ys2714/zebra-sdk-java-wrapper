package com.zebra.zsdk_java_wrapper.zdm;

import androidx.annotation.Keep;

@Keep
public class ZDMConst {

    @Keep
    public enum DelegationScope {
        SCOPE_DW_QUERY_API("delegation_scope_datawedge_query_api"),
        SCOPE_DW_RUNTIME_API("delegation_scope_datawedge_control_api"),
        SCOPE_DW_NOTIFICATION_API("delegation_scope_datawedge_notification_api"),
        SCOPE_DW_CONFIG_API("delegation_scope_datawedge_config_api");

        private final String value;

        DelegationScope(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
