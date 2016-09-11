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

    @Test
    public void test() {

        log.info("{}", GeoHash.decodeHash(GeoHash.adjacentHash("ucftp", Direction.BOTTOM, 4)).getLat());
        log.info("{}", GeoHash.decodeHash(GeoHash.adjacentHash("ucftp", Direction.BOTTOM, 4)).getLon());
        /*log.info("{}", GeoHash.decodeHash("ucftp"));
        String hash = GeoHash.encodeHash(55.7436,37.6283411, 5);
        String hash1 = GeoHash.encodeHash(55.7426,37.6283411, 5);
        String hash2 = GeoHash.encodeHash(55.7416,37.6283411, 5);
        log.info("{}", hash);
        log.info("{}", hash1);
        log.info("{}", hash2);*/

        //log.info("{}",  new Position(55.7415168d,37.6283451d).getDistanceToKm(new Position(55.7415168d,37.6283451d+0.01)));

    }
}
