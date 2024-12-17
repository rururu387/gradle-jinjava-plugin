package io.github.rururu387.configs;

import io.github.rururu387.TestBase;
import io.github.rururu387.exceptions.InvalidConfigurationException;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TemplateIOFilesConfigTest extends TestBase {
    @Test
    void WHEN_regular_file_properties_as_source_and_destination_THEN_map_contains_them() throws Exception {
        // Arrange
        var templateIOFilesConfig = new TemplateIOFilesConfig(objectFactory);

        var templateFileProperty = jinjavaExt.createRegularFileProperty(TEMPLATE_PATH);
        var resolvedFilePath = Path.of("src/test/resources/resolvedFiles/holyTruth.txt").toAbsolutePath();
        var resolvedFileProperty = jinjavaExt.createRegularFileProperty(resolvedFilePath);
        templateIOFilesConfig.addTemplateFile(templateFileProperty, resolvedFileProperty);

        var templateFileProperty2 = jinjavaExt.createRegularFileProperty(TEMPLATE_PATH2);
        var resolvedFilePath2 = Path.of("src/test/resources/resolvedFiles/templateWithExtras.txt").toAbsolutePath();
        var resolvedFileProperty2 = jinjavaExt.createRegularFileProperty(resolvedFilePath2);
        templateIOFilesConfig.addTemplateFile(templateFileProperty2, resolvedFileProperty2);

        // Act
        var templateToResolvedPaths = templateIOFilesConfig.resolveIOPaths();

        // Assert
        // Make sure that paths template and destination paths were set correctly
        assertEquals(resolvedFilePath, templateToResolvedPaths.get(TEMPLATE_PATH));
        assertEquals(resolvedFilePath2, templateToResolvedPaths.get(TEMPLATE_PATH2));

        // Make sure that plugin returns correct input file properties to gradle so it can determine if plugin execution
        // is actually needed
        var inputFiles = templateIOFilesConfig.getTemplateToResolvedFileMapInput();
        assertEquals(2, inputFiles.size());
        assertTrue(inputFiles.contains(templateFileProperty));
        assertTrue(inputFiles.contains(templateFileProperty2));

        // Make sure that plugin returns correct output file properties to gradle
        var outputFiles = templateIOFilesConfig.getTemplateToResolvedFileMapOutput();
        assertEquals(2, outputFiles.size());
        assertTrue(outputFiles.contains(resolvedFileProperty));
        assertTrue(outputFiles.contains(resolvedFileProperty2));
    }

    @Test
    void WHEN_multiple_inputs_map_to_single_output_THEN_error() throws Exception {
        // Arrange
        var templateIOFilesConfig = new TemplateIOFilesConfig(objectFactory);

        var templateFileProperty = jinjavaExt.createRegularFileProperty(TEMPLATE_PATH);
        var resolvedFilePath = Path.of("src/test/resources/resolvedFiles/holyTruth.txt").toAbsolutePath();
        var resolvedFileProperty = jinjavaExt.createRegularFileProperty(resolvedFilePath);
        templateIOFilesConfig.addTemplateFile(templateFileProperty, resolvedFileProperty);

        var templateFileProperty2 = jinjavaExt.createRegularFileProperty(TEMPLATE_PATH2);
        templateIOFilesConfig.addTemplateFile(templateFileProperty2, resolvedFileProperty);

        // Assert
        var exception = assertThrows(InvalidConfigurationException.class,
                // Act
                templateIOFilesConfig::resolveIOPaths);

        // Make sure that error message contains absolute paths to files that are configured incorrectly
        assertTrue(exception.getMessage().contains(TEMPLATE_PATH.toString()));
        assertTrue(exception.getMessage().contains(TEMPLATE_PATH2.toString()));
        assertTrue(exception.getMessage().contains(resolvedFilePath.toString()));
    }

    @Test
    void WHEN_regular_file_properties_as_source_and_directory_property_as_destination_THEN_map_contains_correct_paths() throws Exception {
        // Arrange
        var templateIOFilesConfig = new TemplateIOFilesConfig(objectFactory);

        var templateFileProperty = jinjavaExt.createRegularFileProperty(TEMPLATE_PATH);
        Path resolvedDirPath = Path.of("src/test/resources/resolvedFiles").toAbsolutePath();
        var resolvedDirProperty = jinjavaExt.createDirectoryProperty(resolvedDirPath);
        templateIOFilesConfig.addTemplateFile(templateFileProperty, resolvedDirProperty);

        var templateFileProperty2 = jinjavaExt.createRegularFileProperty(TEMPLATE_PATH2);
        templateIOFilesConfig.addTemplateFile(templateFileProperty2, resolvedDirProperty);

        // Act
        var templateToResolvedPaths = templateIOFilesConfig.resolveIOPaths();

        // Assert
        // Make sure that paths template and destination paths were set correctly
        assertEquals(resolvedDirPath.resolve("holyTruth.txt"), templateToResolvedPaths.get(TEMPLATE_PATH));
        assertEquals(resolvedDirPath.resolve("templateWithExtras.txt"), templateToResolvedPaths.get(TEMPLATE_PATH2));

        // Make sure that plugin returns correct input file properties to gradle so it can determine if plugin execution
        // is actually needed
        var inputFiles = templateIOFilesConfig.getTemplateToDirectoryFileMapInput();
        assertEquals(2, inputFiles.size());
        assertTrue(inputFiles.contains(templateFileProperty));
        assertTrue(inputFiles.contains(templateFileProperty2));

        // Make sure that plugin returns correct output file properties to gradle
        var outputFiles = templateIOFilesConfig.getTemplateToDirectoryFileMapOutput();
        assertEquals(1, outputFiles.size());
        assertTrue(outputFiles.contains(resolvedDirProperty));
    }

    @Test
    void WHEN_file_collection_as_source_and_directory_as_destination_THEN_map_contains_correct_paths() throws Exception {
        // Arrange
        var templateIOFilesConfig = new TemplateIOFilesConfig(objectFactory);

        var templateFileCollection = createFileCollection(List.of(TEMPLATE_PATH, TEMPLATE_PATH2));

        var resolvedDirPath = Path.of("src/test/resources/resolvedFiles").toAbsolutePath();
        var resolvedDirProperty = jinjavaExt.createDirectoryProperty(resolvedDirPath);
        templateIOFilesConfig.addTemplateFiles(templateFileCollection, resolvedDirProperty);

        // Act
        var templateToResolvedPaths = templateIOFilesConfig.resolveIOPaths();

        // Assert
        // Make sure that paths template and destination paths were set correctly
        assertEquals(resolvedDirPath.resolve("holyTruth.txt"), templateToResolvedPaths.get(TEMPLATE_PATH));
        assertEquals(resolvedDirPath.resolve("templateWithExtras.txt"), templateToResolvedPaths.get(TEMPLATE_PATH2));

        // Make sure that plugin returns correct input file properties to gradle so it can determine if plugin execution
        // is actually needed
        var inputFileCollection = templateIOFilesConfig.getFileCollectionDirectoryPropertyMapInput();
        var fileSet = inputFileCollection.getFiles();
        assertEquals(2, fileSet.size());
        assertTrue(fileSet.contains(TEMPLATE_PATH.toFile()));
        assertTrue(fileSet.contains(TEMPLATE_PATH2.toFile()));

        // Make sure that plugin returns correct output file properties to gradle
        var outputFiles = templateIOFilesConfig.getFileCollectionDirectoryPropertyMapOutput();
        assertEquals(1, outputFiles.size());
        assertTrue(outputFiles.contains(resolvedDirProperty));
    }

    @Test
    void WHEN_directory_as_source_and_destination_and_structure_preserved_THEN_map_contains_correct_paths() throws Exception {
        // Arrange
        var templateIOFilesConfig = new TemplateIOFilesConfig(objectFactory);

        var templateDirProperty = jinjavaExt.createDirectoryProperty(TEMPLATE_DIR);

        var resolvedDirPath = Path.of("src/test/resources/resolvedFiles").toAbsolutePath();
        var resolvedDirProperty = jinjavaExt.createDirectoryProperty(resolvedDirPath);
        templateIOFilesConfig.addTemplateFiles(templateDirProperty, resolvedDirProperty, true);

        // Act
        var templateToResolvedPaths = templateIOFilesConfig.resolveIOPaths();

        // Assert
        // Make sure that paths template and destination paths were set correctly
        assertEquals(resolvedDirPath.resolve("holyTruth.txt"), templateToResolvedPaths.get(TEMPLATE_PATH));
        assertEquals(resolvedDirPath.resolve("templateWithExtras.txt"), templateToResolvedPaths.get(TEMPLATE_PATH2));
        assertEquals(resolvedDirPath.resolve("subDir/holyTruth2.txt"), templateToResolvedPaths.get(TEMPLATE_PATH3));

        // Make sure that plugin returns correct input file properties to gradle so it can determine if plugin execution
        // is actually needed
        var inputFileCollection = templateIOFilesConfig.getDirectoryToDirectoryConfigMapInput();
        var fileSet = inputFileCollection.getFiles();
        assertEquals(3, fileSet.size());
        assertTrue(fileSet.contains(TEMPLATE_PATH.toFile()));
        assertTrue(fileSet.contains(TEMPLATE_PATH2.toFile()));
        assertTrue(fileSet.contains(TEMPLATE_PATH3.toFile()));

        // Make sure that plugin returns correct output file properties to gradle
        var outputFiles = templateIOFilesConfig.getDirectoryToDirectoryConfigMapOutput();
        assertEquals(1, outputFiles.size());
        assertTrue(outputFiles.contains(resolvedDirProperty));
    }

    @Test
    void WHEN_directory_as_source_and_destination_and_structure_not_preserved_THEN_map_contains_correct_paths() throws Exception {
        // Arrange
        var templateIOFilesConfig = new TemplateIOFilesConfig(objectFactory);

        var templateDirProperty = jinjavaExt.createDirectoryProperty(TEMPLATE_DIR);

        var resolvedDirPath = Path.of("src/test/resources/resolvedFiles").toAbsolutePath();
        var resolvedDirProperty = jinjavaExt.createDirectoryProperty(resolvedDirPath);
        templateIOFilesConfig.addTemplateFiles(templateDirProperty, resolvedDirProperty, false);

        // Act
        var templateToResolvedPaths = templateIOFilesConfig.resolveIOPaths();

        // Assert
        // Make sure that paths template and destination paths were set correctly
        assertEquals(resolvedDirPath.resolve("holyTruth.txt"), templateToResolvedPaths.get(TEMPLATE_PATH));
        assertEquals(resolvedDirPath.resolve("templateWithExtras.txt"), templateToResolvedPaths.get(TEMPLATE_PATH2));
        assertEquals(resolvedDirPath.resolve("holyTruth2.txt"), templateToResolvedPaths.get(TEMPLATE_PATH3));

        // Make sure that plugin returns correct input file properties to gradle so it can determine if plugin execution
        // is actually needed
        var inputFileCollection = templateIOFilesConfig.getDirectoryToDirectoryConfigMapInput();
        var fileSet = inputFileCollection.getFiles();
        assertEquals(3, fileSet.size());
        assertTrue(fileSet.contains(TEMPLATE_PATH.toFile()));
        assertTrue(fileSet.contains(TEMPLATE_PATH2.toFile()));
        assertTrue(fileSet.contains(TEMPLATE_PATH3.toFile()));

        // Make sure that plugin returns correct output file properties to gradle
        var outputFiles = templateIOFilesConfig.getDirectoryToDirectoryConfigMapOutput();
        assertEquals(1, outputFiles.size());
        assertTrue(outputFiles.contains(resolvedDirProperty));
    }
}
