package org.sherman.geo.server.service;

import com.github.davidmoten.geo.LatLong;
import com.github.davidmoten.grumpy.core.Position;
import org.jetbrains.annotations.NotNull;
import org.sherman.geo.common.domain.IndexedUserLabel;
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
                .map(labelCoords -> new Distance(
                        labelCoords,
                        new Position(labelCoords.getLabel().getCoords().getLat(), labelCoords.getLabel().getCoords().getLon())
                                .getDistanceToKm(new Position(coords.getLat(), coords.getLon())))
                )
                // return false for unknown distance error
                .map(d -> d.getMeters() < geoStorage.getDistanceError(d.label.getHash()).orElse(0))
                .orElse(false);
    }

    private static class Distance {
        final IndexedUserLabel label;
        final double diffKm;

        private Distance(IndexedUserLabel label, double diffKm) {
            this.label = label;
            this.diffKm = diffKm;
        }

        int getMeters() {
            return (int) Math.round(diffKm * 1000);
        }
    }
}
