package org.sherman.geo.generator;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.github.davidmoten.geo.Direction;
import com.github.davidmoten.geo.GeoHash;
import com.github.davidmoten.geo.LatLong;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.CharStreams;
import com.google.common.io.LineProcessor;
import org.sherman.geo.common.domain.IndexedUserLabel;
import org.sherman.geo.common.domain.UserLabel;
import org.sherman.geo.common.util.GuavaCollectors;
import org.sherman.geo.common.util.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

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

    @Test(enabled = false)
    public void generateMoscow() throws IOException {
        int maxEltsPerHash = 50000;

        File file = new File("/home/sherman/user_data");
        FileWriter writer = new FileWriter(file, false);

        CsvSchema schema = CsvSchema.builder()
                .addColumn("user_id")
                .addColumn("lat")
                .addColumn("lon")
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

    @Test(enabled = false)
    public void generateDistanceError() throws IOException {
        File userData = new File("/home/sherman/user_data");

        FileInputStream logStream = new FileInputStream(userData);
        InputStreamReader reader = new InputStreamReader(logStream);

        UserLabelLineProcessor processor = new UserLabelLineProcessor();

        CharStreams.readLines(reader, processor);

        reader.close();
        logStream.close();

        // write data

        File outFile = new File("/home/sherman/distance_error");
        FileWriter writer = new FileWriter(outFile, false);

        CsvSchema schema = CsvSchema.builder()
                .addColumn("geo_hash")
                .addColumn("error")
                .build();

        CsvMapper mapper = new CsvMapper();
        ObjectWriter objectWriter = mapper.writer(schema);

        processor.getDistanceErrors().forEach(
                (geoHash, error) -> write(geoHash, error, objectWriter, writer)
        );

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

    private void write(String geoHash, int meters, ObjectWriter objectWriter, FileWriter fileWriter) {
        try {
            Map<String, Object> data = ImmutableMap.of(
                    "geo_hash", geoHash,
                    "error", meters
            );
            objectWriter.writeValues(fileWriter).write(data);
        } catch (IOException e) {
            log.error("Can't write value", e);
        }
    }

    private class UserLabelLineProcessor implements LineProcessor<Integer> {
        private int lines;
        private final ConcurrentMap<String, Integer> hashesToErrors = new ConcurrentHashMap<>();
        private final Random random = new Random();

        @Override
        public boolean processLine(String line) throws IOException {
            try {
                List<String> label = Splitter.on(',').splitToList(line);
                String geoHash = GeoHash.encodeHash(new LatLong(Double.parseDouble(label.get(1)), Double.parseDouble(label.get(2))), 6);
                hashesToErrors.putIfAbsent(geoHash, random.nextInt(500)); // max 500 meters
                lines++;
            } catch (Exception e) {
                log.info("Can't parse line {}", line, e);
            }

            return true;
        }

        @Override
        public Integer getResult() {
            return lines;
        }

        public ConcurrentMap<String, Integer> getDistanceErrors() {
            return hashesToErrors;
        }
    }
}
