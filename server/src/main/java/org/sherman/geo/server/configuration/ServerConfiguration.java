package org.sherman.geo.server.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author Denis Gabaydulin
 * @since 16/09/2016
 */
@Configuration
public class ServerConfiguration {
    @Value("${geo.server.user.data.file}")
    private String userDataFileName;

    @Value("${geo.server.distance.error.data.file}")
    private String distanceErrorDataFileName;

    public String getUserDataFileName() {
        return userDataFileName;
    }

    public String getDistanceErrorDataFileName() {
        return distanceErrorDataFileName;
    }
}
