package org.sherman.geo.server.controller.request;

import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Denis Gabaydulin
 * @since 11.09.16
 */
public class SizeRequest {
    private Double lat;
    private Double lon;

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }
}
