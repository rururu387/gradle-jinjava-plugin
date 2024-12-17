package io.github.rururu387.configs;

import org.gradle.api.file.DirectoryProperty;

public record DirectoryOutputConfig(DirectoryProperty directoryProperty, boolean preserveDirStructure) {}
