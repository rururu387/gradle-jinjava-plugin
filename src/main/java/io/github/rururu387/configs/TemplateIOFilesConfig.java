package io.github.rururu387.configs;

import io.github.rururu387.configs.interfaces.TemplateIOFilesConfigurable;
import io.github.rururu387.exceptions.InvalidConfigurationException;
import jakarta.annotation.Nonnull;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.FileCollection;
import org.gradle.api.file.FileTree;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.OutputDirectories;
import org.gradle.api.tasks.OutputFiles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TemplateIOFilesConfig implements TemplateIOFilesConfigurable {
    static private final Logger LOGGER = LoggerFactory.getLogger(TemplateIOFilesConfig.class);
    protected ObjectFactory objectFactory;
    public boolean ignoreMissingTemplateFiles = false;

    @Inject
    public TemplateIOFilesConfig(ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
    }


    public final Map<RegularFileProperty, RegularFileProperty> templateToResolvedFileMap = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void addTemplateFile(@Nonnull RegularFileProperty jinjaTemplateFile, @Nonnull RegularFileProperty desiredResolvedFilePath) {
        templateToResolvedFileMap.put(jinjaTemplateFile, desiredResolvedFilePath);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addTemplateFiles(@Nonnull Map<RegularFileProperty, RegularFileProperty> templateFile) {
        templateToResolvedFileMap.putAll(templateFile);
    }

    public Map<RegularFileProperty, RegularFileProperty> getTemplateToResolvedFileMap() {
        return templateToResolvedFileMap;
    }

    @InputFiles
    Set<RegularFileProperty> getTemplateToResolvedFileMapInput() {
        return templateToResolvedFileMap.keySet();
    }

    @OutputFiles
    Collection<RegularFileProperty> getTemplateToResolvedFileMapOutput() {
        return templateToResolvedFileMap.values();
    }


    public final Map<RegularFileProperty, DirectoryProperty> templateToDirectoryFileMap = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void addTemplateFile(@Nonnull RegularFileProperty jinjaTemplateFile, @Nonnull DirectoryProperty desiredResolvedDirPath) {
        templateToDirectoryFileMap.put(jinjaTemplateFile, desiredResolvedDirPath);
    }

    @Nonnull
    public Map<RegularFileProperty, DirectoryProperty> getTemplateToDirectoryFileMap() {
        return templateToDirectoryFileMap;
    }

    @InputFiles
    Set<RegularFileProperty> getTemplateToDirectoryFileMapInput() {
        return templateToDirectoryFileMap.keySet();
    }

    @OutputDirectories
    Set<DirectoryProperty> getTemplateToDirectoryFileMapOutput() {
        return new HashSet<>(templateToDirectoryFileMap.values());
    }


    public final Map<FileCollection, DirectoryProperty> fileCollectionDirectoryPropertyMap = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void addTemplateFiles(@Nonnull FileCollection fileCollection, @Nonnull DirectoryProperty desiredResolvedDirPath) {
        fileCollectionDirectoryPropertyMap.put(fileCollection, desiredResolvedDirPath);
    }

    public Map<FileCollection, DirectoryProperty> getFileCollectionDirectoryPropertyMap() {
        return fileCollectionDirectoryPropertyMap;
    }

    @InputFiles
    FileCollection getFileCollectionDirectoryPropertyMapInput() {
        FileCollection fileCollection = objectFactory.fileCollection();
        for (var collection : fileCollectionDirectoryPropertyMap.keySet()) {
            fileCollection = fileCollection.plus(collection);
        }
        return fileCollection;
    }

    @OutputDirectories
    Collection<DirectoryProperty> getFileCollectionDirectoryPropertyMapOutput() {
        return fileCollectionDirectoryPropertyMap.values();
    }


    public final Map<DirectoryProperty, DirectoryOutputConfig> directoryToDirectoryConfigMap = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void addTemplateFiles(@Nonnull DirectoryProperty templateDirectory,
                                 @Nonnull DirectoryProperty desiredResolvedDir, boolean preserveDirStructure) {
        directoryToDirectoryConfigMap.put(templateDirectory, new DirectoryOutputConfig(desiredResolvedDir, preserveDirStructure));
    }

    public Map<DirectoryProperty, DirectoryOutputConfig> getDirectoryToDirectoryConfigMap() {
        return directoryToDirectoryConfigMap;
    }

    @InputFiles
    FileTree getDirectoryToDirectoryConfigMapInput() {
        FileTree fileTree = objectFactory.fileTree();
        var it = directoryToDirectoryConfigMap.keySet().iterator();
        if (it.hasNext()) {
            fileTree = it.next().getAsFileTree();
        }
        while (it.hasNext()) {
            fileTree.plus(it.next().getAsFileTree());
        }
        return fileTree;
    }

    @OutputDirectories
    Set<DirectoryProperty> getDirectoryToDirectoryConfigMapOutput() {
        return directoryToDirectoryConfigMap.values().stream().map(DirectoryOutputConfig::directoryProperty).collect(Collectors.toSet());
    }

    @Input
    public boolean isIgnoreMissingTemplateFiles() {
        return ignoreMissingTemplateFiles;
    }

    @Override
    public void ignoreMissingTemplateFiles(boolean ignoreMissingTemplateFiles) {
        this.ignoreMissingTemplateFiles = ignoreMissingTemplateFiles;
    }

    /**
     * This method should be called after plugin starts executing. It gets paths from lazy objects
     * @return map of paths. Keys are paths to all jinja2 template files, values are their destinations
     */
    public Map<Path, Path> resolveIOPaths() throws InvalidConfigurationException {
        var templateToDestination = new HashMap<Path, Path>();
        for (var templateToDestinationEntry : getTemplateToResolvedFileMap().entrySet()) {
            templateToDestination.put(templateToDestinationEntry.getKey().get().getAsFile().toPath(),
                    templateToDestinationEntry.getValue().get().getAsFile().toPath());
        }

        for (var templateToDirectoryEntry : getTemplateToDirectoryFileMap().entrySet()) {
            var templatePath = templateToDirectoryEntry.getKey().get().getAsFile().toPath();
            var templateFileName = templatePath.getFileName().toString();
            templateToDestination.put(templatePath, templateToDirectoryEntry.getValue().file(templateFileName).get().getAsFile().toPath());
        }

        for (var templatesToDirectoryEntry : getFileCollectionDirectoryPropertyMap().entrySet()) {
            for (var fileSystemLocation : templatesToDirectoryEntry.getKey().getElements().get()) {
                Path templatePath = fileSystemLocation.getAsFile().toPath();
                var templateFileName = templatePath.getFileName().toString();
                Path resolvedFilePath = templatesToDirectoryEntry.getValue().file(templateFileName).get().getAsFile().toPath();
                templateToDestination.put(templatePath, resolvedFilePath);
            }
        }

        for (var directoryToDirConfigEntry : getDirectoryToDirectoryConfigMap().entrySet()) {
            var templateDirPath = directoryToDirConfigEntry.getKey().getAsFile().get().toPath();
            var outputDir = directoryToDirConfigEntry.getValue().directoryProperty().getAsFile().get().toPath();
            try (Stream<Path> fileStream = Files.walk(templateDirPath)) {
                fileStream.filter(Files::isRegularFile).forEach(templateFilePath -> {
                    Path outputPath;
                    if (directoryToDirConfigEntry.getValue().preserveDirStructure()) {
                        outputPath = outputDir.resolve(templateDirPath.relativize(templateFilePath));
                    } else {
                        outputPath = outputDir.resolve(templateFilePath.getFileName().toString());
                    }
                    templateToDestination.put(templateFilePath, outputPath);
                });
            } catch (IOException e) {
                LOGGER.warn("Cannot access files in \"{}\" directory. " +
                        "SKIPPING!", templateDirPath.toAbsolutePath(), e);
            }
        }

        checkDestinationDuplicates(templateToDestination);

        return templateToDestination;
    }

    public static void checkDestinationDuplicates(Map<Path, Path> templateToDestination) throws InvalidConfigurationException {
        var reverseMap = new HashMap<Path, Path>();
        for (var templateToDestinationEntry : templateToDestination.entrySet()) {
            var prevValue = reverseMap.put(templateToDestinationEntry.getValue(), templateToDestinationEntry.getKey());
            if (prevValue != null) {
                String errorMsg = String.format("Configuration implies that templates %s and %s are both resolved " +
                                "into file %s. Destination paths cannot be equal. Please fix configuration. Aborting!",
                        prevValue, templateToDestinationEntry.getValue(), templateToDestinationEntry.getKey());
                LOGGER.error(errorMsg);
                throw new InvalidConfigurationException(errorMsg);
            }
        }
    }
}
