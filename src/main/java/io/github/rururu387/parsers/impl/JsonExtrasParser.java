package io.github.rururu387.parsers.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.rururu387.parsers.ExtrasTextParser;
import jakarta.annotation.Nonnull;

import java.io.IOException;
import java.nio.file.Path;

public class JsonExtrasParser implements ExtrasTextParser {
    public static final ObjectMapper OM = new ObjectMapper();

    @Override
    @Nonnull
    public JsonNode parse(@Nonnull String extrasFileText) throws IOException {
        return OM.readTree(extrasFileText);
    }

    @Override
    public void dump(@Nonnull Path path, @Nonnull JsonNode jsonNode) throws IOException {
        OM.writeValue(path.toFile(), jsonNode);
    }
}
