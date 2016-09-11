package org.sherman.geo.server.controller;

import com.github.davidmoten.geo.GeoHash;
import com.github.davidmoten.geo.LatLong;
import org.sherman.geo.server.controller.request.SizeRequest;
import org.sherman.geo.server.domain.Error;
import org.sherman.geo.server.domain.ErrorCode;
import org.sherman.geo.server.domain.ReturnValue;
import org.sherman.geo.server.storage.GeoStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static org.sherman.geo.server.domain.ErrorCode.INVALID_REQUEST;

/**
 * @author Denis Gabaydulin
 * @since 11.09.16
 */
@RestController
public class GeoControllerImpl implements GeoController {

    @Autowired
    private GeoStorage geoStorage;

    @Override
    @GetMapping("/api/geo/users/{userId}/near")
    public ReturnValue<Boolean> isUserNearPlace(@PathVariable("userId") Long userId) {
        return new ReturnValue<>(userId > 10);
    }

    @Override
    @GetMapping("/api/geo/hash/size")
    public ReturnValue<Integer> getSizeByCoords(SizeRequest request) {
        // TODO: use jsr-303 validation
        if (request.getLon() == null || request.getLat() == null) {
            return new ReturnValue<>(new Error(INVALID_REQUEST, "Can't find required lat,lon params"));
        }

        String hash = GeoHash.encodeHash(request.getLat(), request.getLon(), GeoStorage.MAX_LENGTH);

        return new ReturnValue<Integer>(geoStorage.getSizeByGeoHash(hash));
    }
}
