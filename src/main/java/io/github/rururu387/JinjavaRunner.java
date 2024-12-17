package io.github.rururu387;

import com.hubspot.jinjava.Jinjava;
import com.hubspot.jinjava.interpret.InterpretException;
import io.github.rururu387.configs.TemplateIOFilesConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;

public class JinjavaRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(JinjavaRunner.class);

    public static void run(Map<Path, Path> templateToResolvedPathMap, Map<String, ?> extras, TemplateIOFilesConfig templateIOFilesConfigurable) throws IOException {
        var jinjava = new Jinjava();

        for (var templateEntry : templateToResolvedPathMap.entrySet()) {
            String renderedFile = null;
            try {
                var template = Files.readString(templateEntry.getKey());
                renderedFile = jinjava.render(template, extras);
            } catch (IOException | SecurityException e) {
                if (!templateIOFilesConfigurable.isIgnoreMissingTemplateFiles()) {
                    LOGGER.warn("Could not read template file {}", templateEntry.getKey().toAbsolutePath(), e);
                    throw e;
                }
                LOGGER.warn("Could not read template file {}. {}", templateEntry.getKey().toAbsolutePath(), e.toString());
            } catch (InterpretException e) {
                LOGGER.error("Could not interpret template file {}", templateEntry.getKey().toAbsolutePath(), e);
                throw e;
            }

            if (renderedFile != null) {
                if (Files.deleteIfExists(templateEntry.getValue())) {
                    LOGGER.warn("File {} already existed! Overridden it", templateEntry.getValue());
                }
                Files.createDirectories(templateEntry.getValue().getParent());
                Files.writeString(templateEntry.getValue(), renderedFile, StandardOpenOption.CREATE);
            }
        }
    }
}