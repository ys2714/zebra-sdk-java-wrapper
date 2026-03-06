package com.zebra.zsdk_java_wrapper.utils;

public interface FixedSizeQueueItem {
    String getID();
    void onDisposal();
}
