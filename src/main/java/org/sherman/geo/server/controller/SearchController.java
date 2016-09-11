package org.sherman.geo.server.controller;

import org.sherman.geo.server.domain.ReturnValue;

/**
 * @author Denis Gabaydulin
 * @since 11.09.16
 */
public interface SearchController {
    ReturnValue<Boolean> isUserNearPlace(Long userId);
}
