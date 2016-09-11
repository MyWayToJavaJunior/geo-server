package org.sherman.geo.generator;

import com.github.davidmoten.geo.LatLong;
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
public class CoordsGeneratorTest extends AbstractTestNGSpringContextTests {
    private static final Logger log = LoggerFactory.getLogger(CoordsGeneratorTest.class);

    @Test
    public void generateTen() {
        /*CoordsGenerator.generate(new LatLong(55.745d, 37.595d), 5000).limit(100)
                .forEach(l -> log.info("{}", l));*/
    }
}
