package io.github.rururu387.parsers.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import io.github.rururu387.parsers.ExtrasTextParser;
import jakarta.annotation.Nonnull;

import java.io.IOException;
import java.nio.file.Path;

public class PropertiesExtrasParser implements ExtrasTextParser {
    public static final ObjectMapper PROPERTIES_MAPPER = new JavaPropsMapper();

    @Override
    @Nonnull
    public JsonNode parse(@Nonnull String extrasFileText) throws IOException {
        return PROPERTIES_MAPPER.readTree(extrasFileText);
    }

    @Override
    public void dump(@Nonnull Path path, @Nonnull JsonNode jsonNode) throws IOException {
        PROPERTIES_MAPPER.writeValue(path.toFile(), jsonNode);
    }
}
