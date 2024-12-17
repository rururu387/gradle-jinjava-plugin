package io.github.rururu387;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.rururu387.configs.JinjavaTaskConfig;
import io.github.rururu387.parsers.UniversalFileParser;
import io.github.rururu387.parsers.impl.JsonExtrasParser;
import io.github.rururu387.mergers.UniversalExtrasMerger;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.tasks.TaskAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class JinjavaTask extends JinjavaTaskConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(JinjavaTask.class);

    @Inject
    public JinjavaTask(ObjectFactory objectFactory) {
        super(objectFactory);
    }

    /**
     * Create a new task each time you need a new set of parameters to template one or multiple files
     */
    @TaskAction
    public void template() {
        try {
            Map<Path, Path> templateToResolvedPathMap;
            templateToResolvedPathMap = templateIoFilesConfig.resolveIOPaths();

            // Merge all extra parameters from extra files to
            List<Path> extraFilePaths = extrasConfig.getExtraFilePaths();
            UniversalExtrasMerger extrasMerger = new UniversalExtrasMerger(extrasConfig.getMergeStrategy());
            UniversalFileParser fileParser = new UniversalFileParser();
            JsonNode mergedExtras = null;
            for (var extraFilePath : extraFilePaths) {
                JsonNode newExtras;
                newExtras = fileParser.parse(extraFilePath, extrasConfig.isIgnoreMissingExtrasFiles());
                if (mergedExtras == null) {
                    mergedExtras = newExtras;
                    continue;
                }
                mergedExtras = extrasMerger.merge(mergedExtras, newExtras);
            }

            var mapExtras = JsonExtrasParser.OM.valueToTree(extrasConfig.getExtrasMap());
            mergedExtras = extrasMerger.merge(mergedExtras, mapExtras);

            if (extrasConfig.valuesOutputFile != null) {
                Path valuesOutputPath = extrasConfig.valuesOutputFile.getAsFile().get().toPath();
                try {
                    Files.createDirectories(valuesOutputPath.getParent());
                    UniversalFileParser.getParser(valuesOutputPath).dump(valuesOutputPath, mergedExtras);
                } catch (Exception e) {
                    LOGGER.error("Could not write values file {}: {}", valuesOutputPath.toAbsolutePath(), e.toString());
                }
            }

            JinjavaRunner.run(templateToResolvedPathMap, JsonExtrasParser.OM.convertValue(mergedExtras,
                    new TypeReference<Map<String, ?>>() {}), templateIoFilesConfig);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
