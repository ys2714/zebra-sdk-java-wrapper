package com.zebra.zsdk_java_wrapper;

import android.text.TextUtils;

public class MXBase {

    public interface EventListener {
        void onEMDKSessionOpened();
        void onEMDKSessionClosed();
        void onEMDKProcessProfileSuccess(String profileName);
        void onEMDKError(ErrorInfo errorInfo);
    }

    public static class ErrorInfo {
        // Contains the parm-error name (sub-feature that has error)
        public String errorName = "";
        // Contains the characteristic-error type (Root feature that has error)
        public String errorType = "";
        // contains the error description for parm or characteristic error.
        public String errorDescription = "";

        public String buildFailureMessage() {
            String failureMessage = "";
            if (!TextUtils.isEmpty(errorName) && !TextUtils.isEmpty(errorType))
                failureMessage = errorName + " :" + "\n" + errorType + " :" + "\n"
                        + errorDescription;
            else if (!TextUtils.isEmpty(errorName))
                failureMessage = errorName + " :" + "\n" + errorDescription;
            else
                failureMessage = errorType + " :" + "\n" + errorDescription;
            return failureMessage;
        }
    }

    // Initial Value of the Power Manager options to be executed in the
    // onOpened() method when the EMDK is ready. Default Value set in the wizard
    // is 0.
    // 0 -> Do Nothing
    // 1 -> Sleep Mode
    // 4 -> Reboot
    // 5 -> Enterprise Reset
    // 6 -> Factory Reset
    // 7 -> Full Device Wipe
    // 8 -> OS Update

    public enum PowerManagerOptions {
        CREATE_PROFILE(-1),
        DO_NOTHING(0),
        SLEEP_MODE(1),
        REBOOT(4),
        ENTERPRISE_RESET(5),
        FACTORY_RESET(6),
        FULL_DEVICE_WIPE(7),
        OS_UPDATE(8);

        final int value;

        PowerManagerOptions(int value) {
            this.value = value;
        }
    }
}
