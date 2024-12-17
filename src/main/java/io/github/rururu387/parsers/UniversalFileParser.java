package io.github.rururu387.parsers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import io.github.rururu387.parsers.impl.JsonExtrasParser;
import io.github.rururu387.parsers.impl.PropertiesExtrasParser;
import io.github.rururu387.parsers.impl.TomlExtrasParser;
import io.github.rururu387.parsers.impl.YamlExtrasParser;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class UniversalFileParser {
    static private final Logger LOGGER = LoggerFactory.getLogger(UniversalFileParser.class);
    static private YamlExtrasParser YAML_EXTRAS_PARSER;
    static private JsonExtrasParser JSON_EXTRAS_PARSER;
    static private TomlExtrasParser TOML_EXTRAS_PARSER;
    static private PropertiesExtrasParser PROPERTIES_EXTRAS_PARSER;

    public static YamlExtrasParser getYamlExtrasParser() {
        if (YAML_EXTRAS_PARSER == null) {
            YAML_EXTRAS_PARSER = new YamlExtrasParser();
        }
        return YAML_EXTRAS_PARSER;
    }

    public static JsonExtrasParser getJsonExtrasParser() {
        if (JSON_EXTRAS_PARSER == null) {
            JSON_EXTRAS_PARSER = new JsonExtrasParser();
        }
        return JSON_EXTRAS_PARSER;
    }

    public static TomlExtrasParser getTomlExtrasParser() {
        if (TOML_EXTRAS_PARSER == null) {
            TOML_EXTRAS_PARSER = new TomlExtrasParser();
        }
        return TOML_EXTRAS_PARSER;
    }

    public static PropertiesExtrasParser getPropertiesExtrasParser() {
        if (PROPERTIES_EXTRAS_PARSER == null) {
            PROPERTIES_EXTRAS_PARSER = new PropertiesExtrasParser();
        }
        return PROPERTIES_EXTRAS_PARSER;
    }

    public static ExtrasTextParser getParser(Path extrasFilePath) {
        return (switch (FilenameUtils.getExtension(extrasFilePath.getFileName().toString()).toLowerCase()) {
            case "json" -> getJsonExtrasParser();
            case "toml" -> getTomlExtrasParser();
            case "properties" -> getPropertiesExtrasParser();
            default -> getYamlExtrasParser();
        });
    }

    @Nonnull
    public JsonNode parse(@Nullable Path extrasFilePath, boolean ignoreMissingFiles) throws IOException {
        if (extrasFilePath == null || !Files.isRegularFile(extrasFilePath)) {
            LOGGER.warn("Could not find extras file {}", extrasFilePath);
            if (!ignoreMissingFiles) {
                throw new FileNotFoundException("Could not find extras file: " + extrasFilePath);
            }
            return JsonNodeFactory.instance.objectNode();
        }

        String extrasFileStr;
        try {
            extrasFileStr = Files.readString(extrasFilePath);
        } catch (IOException e) {
            LOGGER.warn("Could not read extras file {}:\n", extrasFilePath.toAbsolutePath(), e);
            if (!ignoreMissingFiles) {
                throw e;
            }
            return JsonNodeFactory.instance.objectNode();
        }

        try {
            return getParser(extrasFilePath).parse(extrasFileStr);
        } catch (Exception e) {
            return JsonNodeFactory.instance.objectNode();
        }
    }
}
