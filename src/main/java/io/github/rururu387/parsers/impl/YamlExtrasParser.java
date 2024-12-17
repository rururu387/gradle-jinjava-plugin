package io.github.rururu387.parsers.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.github.rururu387.parsers.ExtrasTextParser;
import jakarta.annotation.Nonnull;

import java.io.IOException;
import java.nio.file.Path;

public class YamlExtrasParser implements ExtrasTextParser {
    public static final ObjectMapper YAML_MAPPER = new ObjectMapper(new YAMLFactory());

    @Override
    @Nonnull
    public JsonNode parse(@Nonnull String extrasFileText) throws IOException {
        return YAML_MAPPER.readTree(extrasFileText);
    }

    @Override
    public void dump(@Nonnull Path path, @Nonnull JsonNode jsonNode) throws IOException {
        YAML_MAPPER.writeValue(path.toFile(), jsonNode);
    }
}