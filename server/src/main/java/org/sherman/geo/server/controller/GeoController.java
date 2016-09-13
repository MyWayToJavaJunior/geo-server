package org.sherman.geo.server.controller;

import org.sherman.geo.server.controller.request.CoordsRequest;
import org.sherman.geo.server.domain.ReturnValue;

/**
 * @author Denis Gabaydulin
 * @since 11.09.16
 */
public interface GeoController {
    ReturnValue<Boolean> isUserNearLabel(Long userId, CoordsRequest request);

    ReturnValue<Integer> getSizeByCoords(CoordsRequest request);
}
