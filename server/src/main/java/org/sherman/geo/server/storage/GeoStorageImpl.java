package org.sherman.geo.server.storage;

import com.github.davidmoten.geo.LatLong;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.CharStreams;
import com.google.common.io.LineProcessor;
import org.jetbrains.annotations.NotNull;
import org.sherman.geo.common.domain.IndexedUserLabel;
import org.sherman.geo.common.domain.UserLabel;
import org.sherman.geo.common.util.GuavaCollectors;
import org.sherman.geo.common.util.Maps;
import org.sherman.geo.server.configuration.ServerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static org.sherman.geo.common.util.GuavaCollectors.toImmutableMap;

/**
 * @author Denis Gabaydulin
 * @since 11.09.16
 */
@Component
public class GeoStorageImpl implements GeoStorage {
    private static final Logger log = LoggerFactory.getLogger(GeoStorageImpl.class);

    @Autowired
    private ServerConfiguration serverConfiguration;

    private final ConcurrentMap<Long, IndexedUserLabel> userLabels = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, AtomicInteger> geoHashIndexSize = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Integer> distanceErrors = new ConcurrentHashMap<>();

    @PostConstruct
    public void load() throws IOException {
        loadData(serverConfiguration.getUserDataFileName(), new UserLabelLineProcessor());
        loadData(serverConfiguration.getDistanceErrorDataFileName(), new DistanceErrorLineProcessor());
    }

    @Override
    public Optional<IndexedUserLabel> getByUser(long userId) {
        return ofNullable(userLabels.get(userId));
    }

    @Override
    public Optional<Integer> getDistanceError(@NotNull String geoHash) {
        return ofNullable(distanceErrors.get(geoHash));
    }

    @Override
    public int getSize() {
        return userLabels.size();
    }

    @Override
    public int getSizeByGeoHash(@NotNull String geoHash) {
        return ofNullable(geoHashIndexSize.get(geoHash))
                .map(AtomicInteger::get)
                .orElse(0);
    }

    @NotNull
    @Override
    public Map<String, Integer> getDistribution() {
        return geoHashIndexSize.entrySet()
                .stream()
                .map(e -> new AbstractMap.SimpleImmutableEntry<>(e.getKey(), e.getValue().get()))
                .collect(toImmutableMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private void loadData(String fileName, LineProcessor<Integer> processor) throws IOException {
        File data = new File(fileName);

        FileInputStream logStream = new FileInputStream(data);
        InputStreamReader reader = new InputStreamReader(logStream);

        int read = CharStreams.readLines(reader, processor);

        log.info("Elts {}", read);

        reader.close();
        logStream.close();
    }

    private class UserLabelLineProcessor implements LineProcessor<Integer> {
        private int lines;

        @Override
        public boolean processLine(String line) throws IOException {
            try {
                List<String> label = Splitter.on(',').splitToList(line);
                UserLabel userLabel = new UserLabel(
                        Long.parseLong(label.get(0)),
                        new LatLong(Double.parseDouble(label.get(1)), Double.parseDouble(label.get(2)))
                );

                IndexedUserLabel indexedUserLabel = new IndexedUserLabel(userLabel, MAX_LENGTH);

                userLabels.put(userLabel.getUserId(), indexedUserLabel);

                Maps.atomicPut(geoHashIndexSize, indexedUserLabel.getHash(), new AtomicInteger())
                        .incrementAndGet();

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
    }

    private class DistanceErrorLineProcessor implements LineProcessor<Integer> {
        private int lines;

        @Override
        public boolean processLine(String line) throws IOException {
            try {
                List<String> distanceError = Splitter.on(',').splitToList(line);

                distanceErrors.put(distanceError.get(0), Integer.parseInt(distanceError.get(1)));

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
    }
}
