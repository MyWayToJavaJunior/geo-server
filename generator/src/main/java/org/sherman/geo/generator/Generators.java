package org.sherman.geo.generator;

import com.github.davidmoten.geo.LatLong;
import org.jetbrains.annotations.NotNull;
import org.sherman.geo.common.domain.UserLabel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author Denis Gabaydulin
 * @since 11.09.16
 */
@Service
public class Generators {
    @Autowired
    private UserIdGenerator userGeneratorService;

    @Autowired
    private CoordsGenerator coordsGenerator;

    @NotNull
    public Stream<UserLabel> generateUserLabels(@NotNull LatLong coords, int radius) {
        return Stream.generate(
                () -> new UserLabel(
                        userGeneratorService.generate().findAny().get(),
                        coordsGenerator.generate(coords, radius).findAny().get()
                )
        );
    }
}
