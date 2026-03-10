package com.zebra.zsdk_java_wrapper.utils;

import androidx.annotation.Keep;

@Keep
public interface FixedSizeQueueItem {
    String getID();
    void onDisposal();
}
