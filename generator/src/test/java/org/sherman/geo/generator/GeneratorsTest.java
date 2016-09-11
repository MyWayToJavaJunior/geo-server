package org.sherman.geo.generator;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.github.davidmoten.geo.Direction;
import com.github.davidmoten.geo.GeoHash;
import com.github.davidmoten.geo.LatLong;
import com.google.common.collect.ImmutableMap;
import org.sherman.geo.common.domain.UserLabel;
import org.sherman.geo.common.util.GuavaCollectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import static org.sherman.geo.common.util.GuavaCollectors.toImmutableList;

/**
 * @author Denis Gabaydulin
 * @since 11.09.16
 */
@ContextConfiguration(
        loader = AnnotationConfigContextLoader.class,
        classes = {
                Generators.class,
                IdGeneratorService.class,
                CoordsGenerator.class,
                UserIdGenerator.class
        })
public class GeneratorsTest extends AbstractTestNGSpringContextTests {
    private static final Logger log = LoggerFactory.getLogger(GeneratorsTest.class);

    @Autowired
    private Generators generators;

    @Test
    public void generateMoscow() throws IOException {
        int maxEltsPerHash = 50000;

        File file = new File("/home/sherman/user_data");
        FileWriter writer = new FileWriter(file, false);

        CsvSchema schema = CsvSchema.builder()
                .addColumn("user_id")
                .addColumn("lat")
                .addColumn("lon")
                //.setUseHeader(true)
                .build();

        CsvMapper mapper = new CsvMapper();
        ObjectWriter objectWriter = mapper.writer(schema);

        LatLong initialCoords = new LatLong(55.745d, 37.595d);
        String initial = GeoHash.encodeHash(initialCoords);

        generators.generateUserLabels(initialCoords, 10000).limit(maxEltsPerHash).forEach(
                elt -> write(elt, objectWriter, writer)
        );

        for (int i = 1; i < 6; i++) {
            LatLong coords = GeoHash.decodeHash(GeoHash.adjacentHash(initial, Direction.TOP, i));
            generators.generateUserLabels(coords, 10000).limit(maxEltsPerHash).forEach(
                    elt -> write(elt, objectWriter, writer)
            );

            coords = GeoHash.decodeHash(GeoHash.adjacentHash(initial, Direction.RIGHT, i));
            generators.generateUserLabels(coords, 10000).limit(maxEltsPerHash).forEach(
                    elt -> write(elt, objectWriter, writer)
            );

            coords = GeoHash.decodeHash(GeoHash.adjacentHash(initial, Direction.BOTTOM, i));
            generators.generateUserLabels(coords, 10000).limit(maxEltsPerHash).forEach(
                    elt -> write(elt, objectWriter, writer)
            );

            coords = GeoHash.decodeHash(GeoHash.adjacentHash(initial, Direction.LEFT, i));
            generators.generateUserLabels(coords, 10000).limit(maxEltsPerHash).forEach(
                    elt -> write(elt, objectWriter, writer)
            );
        }

        writer.flush();
        writer.close();
    }

    private void write(UserLabel userLabel, ObjectWriter objectWriter, FileWriter fileWriter) {
        try {
            Map<String, Object> data = ImmutableMap.of(
                    "user_id", userLabel.getUserId(),
                    "lat", userLabel.getCoords().getLat(),
                    "lon", userLabel.getCoords().getLon()
            );
            objectWriter.writeValues(fileWriter).write(data);
        } catch (IOException e) {
            log.error("Can't write value", e);
        }

    }
}
