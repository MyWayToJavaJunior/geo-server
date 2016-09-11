package org.sherman.geo.server.storage;

import org.jetbrains.annotations.NotNull;

/**
 * @author Denis Gabaydulin
 * @since 11.09.16
 */
public interface GeoStorage {
    int getSize();

    int getSizeByGeoHash(@NotNull String geoHash);
}
