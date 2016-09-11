package org.sherman.geo.generator;

import com.github.davidmoten.geo.LatLong;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author Denis Gabaydulin
 * @since 11.09.16
 */
@Service
public class CoordsGenerator {

    @NotNull
    public Stream<LatLong> generate(@NotNull LatLong latLong, int radius) {
        return Stream.generate(new CoordSupplier(latLong, radius));
    }

    private static class CoordSupplier implements Supplier<LatLong> {
        final Random random = new Random();
        final int radius;
        final LatLong coords;

        private CoordSupplier(LatLong coords, int radius) {
            this.coords = coords;
            this.radius = radius;
        }

        @Override
        public LatLong get() {
            double radiusInDegrees = getRadiusInDegrees(radius);

            double u = random.nextDouble();
            double v = random.nextDouble();
            double w = radiusInDegrees * Math.sqrt(u);
            double t = 2 * Math.PI * v;
            double x = w * Math.cos(t);
            double y = w * Math.sin(t);

            double new_x = x / Math.cos(coords.getLat());

            double lon = new_x + coords.getLon();
            double lat = y + coords.getLat();

            return new LatLong(lat, lon);
        }

        private double getRadiusInDegrees(int radius) {
            return radius / 111000f;
        }
    }
}
