package io.github.rururu387.parsers;


import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.Nonnull;

import java.io.IOException;
import java.nio.file.Path;

public interface ExtrasTextParser {
    @Nonnull
    JsonNode parse(@Nonnull String extrasFileText) throws IOException;
    void dump(@Nonnull Path path, @Nonnull JsonNode jsonNode) throws IOException;
}
