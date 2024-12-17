package io.github.rururu387.parsers.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.toml.TomlFactory;
import io.github.rururu387.parsers.ExtrasTextParser;
import jakarta.annotation.Nonnull;

import java.io.IOException;
import java.nio.file.Path;

public class TomlExtrasParser implements ExtrasTextParser {
    public static final ObjectMapper TOML_MAPPER = new ObjectMapper(new TomlFactory());

    @Override
    @Nonnull
    public JsonNode parse(@Nonnull String extrasFileText) throws IOException {
        return TOML_MAPPER.readTree(extrasFileText);
    }

    @Override
    public void dump(@Nonnull Path path, @Nonnull JsonNode jsonNode) throws IOException {
        TOML_MAPPER.writeValue(path.toFile(), jsonNode);
    }
}
