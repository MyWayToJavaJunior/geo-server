package org.sherman.geo.server.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.AssertJUnit.assertEquals;

/**
 * @author Denis Gabaydulin
 * @since 11.09.16
 */
@ContextConfiguration(
        loader = AnnotationConfigContextLoader.class,
        classes = {
                GeoStorageImpl.class
        })
public class GeoStorageTest extends AbstractTestNGSpringContextTests {
    private static final Logger log = LoggerFactory.getLogger(GeoStorageTest.class);

    @Autowired
    private GeoStorage geoStorage;

    @Test
    public void size() throws IOException, InterruptedException {
        assertEquals(geoStorage.getSizeByGeoHash("ucftpk"), 1778);
    }
}
