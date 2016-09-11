package org.sherman.geo.server.controller;

import org.sherman.geo.server.controller.request.SizeRequest;
import org.sherman.geo.server.domain.ReturnValue;

/**
 * @author Denis Gabaydulin
 * @since 11.09.16
 */
public interface GeoController {
    ReturnValue<Boolean> isUserNearPlace(Long userId);

    ReturnValue<Integer> getSizeByCoords(SizeRequest request);
}
