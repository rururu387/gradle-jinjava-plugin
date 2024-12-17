package io.github.rururu387;

import io.github.rururu387.mergers.MergeStrategy;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JinjavaTaskTest extends TestBase {
    @Test
    void WHEN_directory_templated_THEN_resolved_files_created_with_correct_content() throws IOException {
        // Arrange
        JinjavaTask jinjavaTask = project.getTasks().create("templateTask", JinjavaTask.class);
        var templateDirProperty = jinjavaExt.createDirectoryProperty(TEMPLATE_DIR);
        var resolvedDirProperty = jinjavaExt.createDirectoryProperty(Path.of("build/test/resources/resolvedFiles").toAbsolutePath());
        jinjavaTask.addTemplateFiles(templateDirProperty, resolvedDirProperty, true);

        var paramsJsonPath = Path.of("src/test/resources/extras/parameters.json").toAbsolutePath();
        jinjavaTask.addExtraFile(jinjavaExt.createRegularFileProperty(paramsJsonPath));
        var paramsPropertiesPath = Path.of("src/test/resources/extras/parameters.properties").toAbsolutePath();
        jinjavaTask.addExtraFile(jinjavaExt.createRegularFileProperty(paramsPropertiesPath));
        var paramsTomlPath = Path.of("src/test/resources/extras/parameters.toml").toAbsolutePath();
        jinjavaTask.addExtraFile(jinjavaExt.createRegularFileProperty(paramsTomlPath));
        var paramsYamlPath = Path.of("src/test/resources/extras/parameters.yaml").toAbsolutePath();
        jinjavaTask.addExtraFile(jinjavaExt.createRegularFileProperty(paramsYamlPath));
        var paramsUnknownPath = Path.of("src/test/resources/extras/parameters.yaml.unknown").toAbsolutePath();
        jinjavaTask.addExtraFile(jinjavaExt.createRegularFileProperty(paramsUnknownPath));
        Map<String, Object> extraParams = Map.of("from_set_extras", "This is a value from test!", "array",
                Map.of("set_from_extras", List.of("This", "is", "an", "array", "from", "test")));
        jinjavaTask.mergeStrategy(MergeStrategy.MERGE_MAPS);
        jinjavaTask.extras(extraParams);

        // Act
        jinjavaTask.template();

        // Assert
        var expectedResolvedFileHolyTruth = EXPECTED_FILES_DIR.resolve("holyTruth.txt");
        String expectedResolvedHolyTruthStr = Files.readString(expectedResolvedFileHolyTruth);
        var expectedResolvedHolyTruth2Path = EXPECTED_FILES_DIR.resolve("subDir/holyTruth2.txt");
        String expectedResolvedHolyTruth2Str = Files.readString(expectedResolvedHolyTruth2Path);
        var expectedResolvedFileWithExtrasPath = EXPECTED_FILES_DIR.resolve("templateWithExtras.txt");
        String expectedResolvedFileWithExtrasStr = Files.readString(expectedResolvedFileWithExtrasPath);

        var actualResolvedHolyTruthPath = Path.of("build/test/resources/resolvedFiles/holyTruth.txt").toAbsolutePath();
        var actualResolvedHolyTruthStr = Files.readString(actualResolvedHolyTruthPath);
        var actualResolvedHolyTruth2Path = Path.of("build/test/resources/resolvedFiles/subDir/holyTruth2.txt").toAbsolutePath();
        var actualResolvedHolyTruth2Str = Files.readString(actualResolvedHolyTruth2Path);
        var actualResolvedTemplateWithExtrasPath = Path.of("build/test/resources/resolvedFiles/templateWithExtras.txt").toAbsolutePath();
        var actualResolvedTemplateWithExtrasStr = Files.readString(actualResolvedTemplateWithExtrasPath);

        assertEquals(expectedResolvedHolyTruthStr, actualResolvedHolyTruthStr);
        assertEquals(expectedResolvedHolyTruth2Str, actualResolvedHolyTruth2Str);
        assertEquals(expectedResolvedFileWithExtrasStr, actualResolvedTemplateWithExtrasStr);
    }
}
