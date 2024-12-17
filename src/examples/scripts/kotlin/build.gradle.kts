import io.github.rururu387.JinjavaTask;
import io.github.rururu387.mergers.MergeStrategy;

// To enable autocompletion configure your IDE. Instructions for intellij in readme.md -> usage examples -> tinker

plugins {
    id("io.github.rururu387.gradle-jinjava-plugin") version "1.0-SNAPSHOT"
}

repositories {
    mavenLocal()
    mavenCentral()
}

val templateTask by tasks.registering(JinjavaTask::class) {
    ignoreMissingExtrasFiles(true)
    // Verbose way to instantiate RegularFileProperty
    val paramsFileProperty: RegularFileProperty = objects.fileProperty()
    paramsFileProperty.set(File("../../parameters/parameters.yaml"))
    addExtraFile(paramsFileProperty)
    // Inline way to obtain RegularFileProperty instance via plugin extension. See JinjavaExtension.createRegularFileProperty
    // Function can consume string, Path, File or a RegularFile. There is a similar function createDirectoryProperty too!
    addExtraFile(jinjava.createRegularFileProperty("/Extras/file/doe/not/exist"))
    mergeStrategy(MergeStrategy.MERGE_MAPS)
    writeMergedValues(jinjava.createRegularFileProperty(layout.buildDirectory.file("values.yaml").get()))

    ignoreMissingTemplateFiles(true)
    val nonExistentFileProperty: RegularFileProperty = jinjava.createRegularFileProperty(layout.projectDirectory.file("/Template/file/does/not/exist"))
    // Notice .get() for layout.buildDirectory
    addTemplateFile(nonExistentFileProperty, jinjava.createRegularFileProperty(layout.buildDirectory.file("/Destination/directories/wont/be/created").get()))

    addTemplateFile(jinjava.createRegularFileProperty(File("../../sources/subDir/src.yaml")),
        jinjava.createDirectoryProperty(layout.buildDirectory.dir("templateOutput/subDir").get()))
    addTemplateFile(jinjava.createRegularFileProperty(java.nio.file.Path.of("../../sources/src.html")),
        jinjava.createRegularFileProperty(layout.buildDirectory.file("templateOutput/index.html").get()))
}