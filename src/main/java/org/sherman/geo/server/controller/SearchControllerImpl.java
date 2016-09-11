package org.sherman.geo.server.controller;

import org.sherman.geo.server.domain.ReturnValue;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Denis Gabaydulin
 * @since 11.09.16
 */
@RestController
public class SearchControllerImpl implements SearchController {
    @Override
    @GetMapping("/api/search/users/{userId}/near")
    public ReturnValue<Boolean> isUserNearPlace(@PathVariable("userId") Long userId) {
        return new ReturnValue<Boolean>(userId > 10);
    }
}
