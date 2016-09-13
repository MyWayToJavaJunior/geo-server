package org.sherman.geo.server.controller;

import com.github.davidmoten.geo.GeoHash;
import com.github.davidmoten.geo.LatLong;
import org.sherman.geo.server.controller.request.CoordsRequest;
import org.sherman.geo.server.domain.Error;
import org.sherman.geo.server.domain.ReturnValue;
import org.sherman.geo.server.service.GeoService;
import org.sherman.geo.server.storage.GeoStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static com.github.davidmoten.geo.GeoHash.encodeHash;
import static org.sherman.geo.server.domain.ErrorCode.INVALID_REQUEST;

/**
 * @author Denis Gabaydulin
 * @since 11.09.16
 */
@RestController
public class GeoControllerImpl implements GeoController {

    @Autowired
    private GeoStorage geoStorage;

    @Autowired
    private GeoService geoService;

    @Override
    @GetMapping("/api/geo/users/{userId}/near")
    public ReturnValue<Boolean> isUserNearLabel(@PathVariable("userId") Long userId, CoordsRequest request) {
        if (userId == null) {
            return new ReturnValue<>(new Error(INVALID_REQUEST, "Can't find required userId param"));
        }

        if (!isValidCoordsRequest(request)) {
            return new ReturnValue<>(new Error(INVALID_REQUEST, "Can't find required lat,lon params"));
        }

        return new ReturnValue<>(geoService.isUserNearLabel(userId, new LatLong(request.getLat(), request.getLon())));
    }

    @Override
    @GetMapping("/api/geo/hash/size")
    public ReturnValue<Integer> getSizeByCoords(CoordsRequest request) {
        if (!isValidCoordsRequest(request)) {
            return new ReturnValue<>(new Error(INVALID_REQUEST, "Can't find required lat,lon params"));
        }

        String hash = encodeHash(request.getLat(), request.getLon(), GeoStorage.MAX_LENGTH);

        return new ReturnValue<>(geoStorage.getSizeByGeoHash(hash));
    }

    private boolean isValidCoordsRequest(CoordsRequest request) {
        // TODO: use jsr-303 validation
        return !(request.getLon() == null || request.getLat() == null);
    }
}
