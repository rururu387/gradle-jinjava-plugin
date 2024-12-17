package io.github.rururu387;

import org.gradle.api.Project;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.FileCollection;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.model.ObjectFactory;
import org.gradle.testfixtures.ProjectBuilder;

import java.nio.file.Path;
import java.util.Collection;

public class TestBase {
    protected static final Path TEMPLATE_DIR = Path.of("src/test/resources/templates").toAbsolutePath();
    protected static final Path EXPECTED_FILES_DIR = Path.of("src/test/resources/expectedFiles").toAbsolutePath();
    protected static final Path TEMPLATE_PATH = Path.of("src/test/resources/templates/holyTruth.txt").toAbsolutePath();
    protected static final Path TEMPLATE_PATH2 = Path.of("src/test/resources/templates/templateWithExtras.txt").toAbsolutePath();
    protected static final Path TEMPLATE_PATH3 = Path.of("src/test/resources/templates/subDir/holyTruth2.txt").toAbsolutePath();
    protected static final Project project = ProjectBuilder.builder().build();
    protected static final ObjectFactory objectFactory = project.getObjects();
    protected static final JinjavaExtension jinjavaExt = new JinjavaExtension(objectFactory);

    protected FileCollection createFileCollection(Collection<Path> paths) {
        var fileCollection = objectFactory.fileCollection();
        fileCollection.setFrom(paths);
        return fileCollection;
    }
}
