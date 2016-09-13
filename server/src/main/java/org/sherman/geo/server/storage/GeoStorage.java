package org.sherman.geo.server.storage;

import com.github.davidmoten.geo.LatLong;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Denis Gabaydulin
 * @since 11.09.16
 */
public interface GeoStorage {
    int MAX_LENGTH = 6; // ≤ 1.22km × 0.61km

    Optional<LatLong> getByUser(long userId);

    Optional<Integer> getDistanceError(@NotNull String geoHash);

    int getSize();

    int getSizeByGeoHash(@NotNull String geoHash);
}
