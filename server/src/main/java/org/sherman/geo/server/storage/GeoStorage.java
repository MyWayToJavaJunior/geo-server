package org.sherman.geo.server.storage;

import org.jetbrains.annotations.NotNull;

/**
 * @author Denis Gabaydulin
 * @since 11.09.16
 */
public interface GeoStorage {
    public static final int MAX_LENGTH = 6; // ≤ 1.22km × 0.61km

    int getSize();

    int getSizeByGeoHash(@NotNull String geoHash);
}
