package org.sherman.geo.server.service;

import com.github.davidmoten.geo.LatLong;
import com.github.davidmoten.grumpy.core.Position;
import org.jetbrains.annotations.NotNull;
import org.sherman.geo.server.storage.GeoStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Denis Gabaydulin
 * @since 11.09.16
 */
@Service
public class GeoServiceImpl implements GeoService {

    @Autowired
    private GeoStorage geoStorage;

    @Override
    public boolean isUserNearLabel(long userId, @NotNull LatLong coords) {
        return geoStorage.getByUser(userId)
                .map(labelCoords -> new Position(labelCoords.getLat(), labelCoords.getLon())
                        .getDistanceToKm(new Position(coords.getLat(), coords.getLon())))
                // to meters
                .map(km -> km / 1000)
                // FIXME: check error
                .map(m -> true)
                .orElse(false);
    }
}
