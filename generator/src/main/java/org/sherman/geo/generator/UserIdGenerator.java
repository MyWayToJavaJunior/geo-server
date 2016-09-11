package org.sherman.geo.generator;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author Denis Gabaydulin
 * @since 11.09.16
 */
@Service
public class UserIdGenerator {

    @Autowired
    private IdGeneratorService generatorService;

    @NotNull
    public Stream<Long> generate() {
        return Stream.generate(new UserIdGeneratorSupplier(generatorService));
    }

    private static class UserIdGeneratorSupplier implements Supplier<Long> {
        final static int SHARD_ID = 1;
        final IdGeneratorService generatorService;

        private UserIdGeneratorSupplier(IdGeneratorService generatorService) {
            this.generatorService = generatorService;
        }

        @Override
        public Long get() {
            return generatorService.generate(SHARD_ID);
        }
    }
}
