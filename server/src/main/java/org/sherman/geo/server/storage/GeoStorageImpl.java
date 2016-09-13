package org.sherman.geo.server.storage;

import com.github.davidmoten.geo.LatLong;
import com.google.common.base.Splitter;
import com.google.common.io.CharStreams;
import com.google.common.io.LineProcessor;
import org.jetbrains.annotations.NotNull;
import org.sherman.geo.common.domain.IndexedUserLabel;
import org.sherman.geo.common.domain.UserLabel;
import org.sherman.geo.common.util.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

/**
 * @author Denis Gabaydulin
 * @since 11.09.16
 */
@Component
public class GeoStorageImpl implements GeoStorage {
    private static final Logger log = LoggerFactory.getLogger(GeoStorageImpl.class);

    private final ConcurrentMap<Long, IndexedUserLabel> userLabels = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, AtomicInteger> geoHashIndexSize = new ConcurrentHashMap<>();

    @PostConstruct
    public void load() throws IOException {
        // FIXME
        File file = new File("/home/sherman/user_data");

        FileInputStream logStream = new FileInputStream(file);
        InputStreamReader reader = new InputStreamReader(logStream);

        int read = CharStreams.readLines(reader, new UserLabelLineProcessor());
        log.info("Elts {}", read);

        reader.close();
        logStream.close();
    }

    @Override
    public Optional<LatLong> getByUser(long userId) {
        return ofNullable(of(userLabels.get(userId))
                .map(label -> label.getLabel().getCoords())
                .orElse(null)
        );
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
}
