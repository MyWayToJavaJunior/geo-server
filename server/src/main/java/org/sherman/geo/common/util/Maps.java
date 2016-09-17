package org.sherman.geo.common.util;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ConcurrentMap;

public class Maps {
    private Maps() {
    }

    @NotNull
    public static <K, V> V atomicPut(@NotNull ConcurrentMap<K, V> map, @NotNull K key, @NotNull V value) {
        final V prev = map.putIfAbsent(key, value);
        if (prev != null) {
            return prev;
        }
        return value;
    }
}
