package com.zebra.zsdk_java_wrapper.dw;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Keep
public final class DWScannerMap {

    private DWScannerMap() {}

    @Keep
    public static final class DWScannerInfo {
        private final String id;
        private final String name;
        private final int index;
        private final boolean connected;

        public DWScannerInfo(String id, String name, int index, boolean connected) {
            this.id = id;
            this.name = name;
            this.index = index;
            this.connected = connected;
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public int getIndex() { return index; }
        public boolean isConnected() { return connected; }
    }

    private static final Map<String, DWScannerInfo> _map = new HashMap<>();

    @Keep
    @Nullable
    public static synchronized DWScannerInfo getScannerInfo(String id) {
        return _map.get(id);
    }

    @Keep
    public static synchronized void setScannerInfo(String id, String name, int index, boolean connected) {
        DWScannerInfo info = new DWScannerInfo(id, name, index, connected);
        _map.put(id, info);
    }

    @Keep
    @NonNull
    public static synchronized List<DWScannerInfo> getScannerList() {
        return new ArrayList<>(_map.values());
    }
}
