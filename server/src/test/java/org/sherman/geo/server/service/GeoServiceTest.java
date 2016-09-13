package org.sherman.geo.server.service;

import com.github.davidmoten.geo.LatLong;
import org.kubek2k.springockito.annotations.ReplaceWithMock;
import org.kubek2k.springockito.annotations.SpringockitoAnnotatedContextLoader;
import org.mockito.Mockito;
import org.sherman.geo.server.storage.GeoStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Optional;

import static java.util.Optional.empty;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * @author Denis Gabaydulin
 * @since 11.09.16
 */
@ContextConfiguration(
        loader = SpringockitoAnnotatedContextLoader.class,
        classes = {
                GeoServiceImpl.class
        })
public class GeoServiceTest extends AbstractTestNGSpringContextTests {
    private static final Logger log = LoggerFactory.getLogger(GeoServiceTest.class);

    @Autowired
    private GeoService geoService;

    @Autowired
    @ReplaceWithMock
    private GeoStorage geoStorage;

    @Test
    public void noUser() {
        when(geoStorage.getByUser(eq(42L))).thenReturn(empty());

        assertFalse(geoService.isUserNearLabel(42L, new LatLong(42d, 42d)));
    }

    @Test
    public void nearby() {
        when(geoStorage.getByUser(eq(42L))).thenReturn(Optional.of(new LatLong(42d, 42d)));

        assertTrue(geoService.isUserNearLabel(42L, new LatLong(42d, 42d)));
    }

    @BeforeMethod
    private void reset() {
        Mockito.reset(geoStorage);
    }
}
