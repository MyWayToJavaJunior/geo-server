package org.sherman.geo.server.service;

import com.github.davidmoten.geo.LatLong;
import org.jetbrains.annotations.NotNull;

/**
 * @author Denis Gabaydulin
 * @since 11.09.16
 */
public interface GeoService {
    boolean isUserNearLabel(long userId, @NotNull LatLong coords);
}
