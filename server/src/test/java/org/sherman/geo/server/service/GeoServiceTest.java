package org.sherman.geo.server.service;

import com.github.davidmoten.geo.Direction;
import com.github.davidmoten.geo.GeoHash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

/**
 * @author Denis Gabaydulin
 * @since 11.09.16
 */
@ContextConfiguration(
        loader = AnnotationConfigContextLoader.class,
        classes = {
                GeoServiceImpl.class
        })
public class GeoServiceTest extends AbstractTestNGSpringContextTests {
    private static final Logger log = LoggerFactory.getLogger(GeoServiceTest.class);
}
