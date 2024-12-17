package io.github.rururu387;

import org.gradle.api.file.Directory;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.RegularFile;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.model.ObjectFactory;

import java.io.File;
import java.nio.file.Path;

/**
 * This extension has convenience methods so RegularFileProperty objects could be easily instantiated. Existing in
 * gradle api method is too verbose IMO
 */
public class JinjavaExtension {
    private final ObjectFactory objectFactory;

    public JinjavaExtension(ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
    }

    public RegularFileProperty createRegularFileProperty() {
        return objectFactory.fileProperty();
    }

    public RegularFileProperty createRegularFileProperty(RegularFile file) {
        var fileProperty = objectFactory.fileProperty();
        fileProperty.set(file);
        return fileProperty;
    }

    public RegularFileProperty createRegularFileProperty(File file) {
        var fileProperty = objectFactory.fileProperty();
        fileProperty.set(file);
        return fileProperty;
    }

    public RegularFileProperty createRegularFileProperty(Path path) {
        var fileProperty = objectFactory.fileProperty();
        fileProperty.set(path.toFile());
        return fileProperty;
    }

    public RegularFileProperty createRegularFileProperty(String path) {
        var fileProperty = objectFactory.fileProperty();
        fileProperty.set(new File(path));
        return fileProperty;
    }

    public DirectoryProperty createDirectoryProperty() {
        return objectFactory.directoryProperty();
    }

    public DirectoryProperty createDirectoryProperty(Directory directory) {
        var fileProperty = objectFactory.directoryProperty();
        fileProperty.set(directory);
        return fileProperty;
    }

    public DirectoryProperty createDirectoryProperty(File directory) {
        var fileProperty = objectFactory.directoryProperty();
        fileProperty.set(directory);
        return fileProperty;
    }

    public DirectoryProperty createDirectoryProperty(Path path) {
        var fileProperty = objectFactory.directoryProperty();
        fileProperty.set(path.toFile());
        return fileProperty;
    }

    public DirectoryProperty createDirectoryProperty(String path) {
        var fileProperty = objectFactory.directoryProperty();
        fileProperty.set(new File(path));
        return fileProperty;
    }
}
