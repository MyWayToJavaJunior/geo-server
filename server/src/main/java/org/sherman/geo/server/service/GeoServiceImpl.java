package org.sherman.geo.server.service;

import com.github.davidmoten.geo.GeoHash;
import org.springframework.stereotype.Service;

/**
 * @author Denis Gabaydulin
 * @since 11.09.16
 */
@Service
public class GeoServiceImpl implements GeoService {

    public void test() {
        GeoHash.encodeHash(55.7415168d,37.6283451d);
    }
}
