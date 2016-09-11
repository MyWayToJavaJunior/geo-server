package org.sherman.geo.server.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Denis Gabaydulin
 * @since 11.09.16
 */
@SpringBootApplication(scanBasePackages = "org.sherman.geo.server")
public class GeoServerApp {
    public static void main(String[] args) {
        SpringApplication.run(GeoServerApp.class, args);
    }
}
