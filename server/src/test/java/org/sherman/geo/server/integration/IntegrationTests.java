package org.sherman.geo.server.integration;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;
import org.sherman.geo.server.app.GeoServerApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static org.testng.Assert.assertEquals;

/**
 * @author Denis Gabaydulin
 * @since 11.09.16
 */
public class IntegrationTests extends AbstractTestNGSpringContextTests {
    private static final Logger log = LoggerFactory.getLogger(IntegrationTests.class);

    private AsyncHttpClient httpClient;

    @Test
    public void getSizeByHash() throws InterruptedException, ExecutionException {
        Response response = httpClient.prepareGet("http://localhost:8080/api/geo/hash/size?lat=55.800134&lon=37.571392").execute().get();

        assertEquals(response.getResponseBody(), "{\"result\":1722}");
    }

    @BeforeTest
    private void beforeTest() {
        httpClient = new DefaultAsyncHttpClient();

        GeoServerApp.main(new String[]{});
    }

    @AfterTest
    private void afterTest() throws IOException {
        httpClient.close();
    }
}
