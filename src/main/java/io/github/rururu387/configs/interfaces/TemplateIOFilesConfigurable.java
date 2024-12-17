package io.github.rururu387.configs.interfaces;

import jakarta.annotation.Nonnull;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.FileCollection;
import org.gradle.api.file.RegularFileProperty;

import java.util.Map;

public interface TemplateIOFilesConfigurable {
    /**
     * If set to false plugin throws errors when template file (first argument) passed to
     * {@link #addTemplateFile(RegularFileProperty, RegularFileProperty)} addTemplateFile} function (or other
     * convenience functions) could not be read.
     */
    void ignoreMissingTemplateFiles(boolean ignoreMissingTemplateFiles);

    /**
     * <h4>Warning</h4>
     * Plugin relies on <a href="https://github.com/HubSpot/jinjava">jinjava template engine</a> and therefore might
     * support only subset of jinja2 syntax.<br/>
     * Use function to add <br/>
     * @param jinjaTemplateFile first parameter - describes template file(s) formatted following the rules of
     * <a href="https://jinja.palletsprojects.com/en/stable/templates">jinja2 syntax</a>
     * @param desiredResolvedFilePath second parameter - describes desired destination path of resolved (first
     * parameter) file(s). Missing directories will be created<br/>
     */
    void addTemplateFile(@Nonnull RegularFileProperty jinjaTemplateFile, @Nonnull RegularFileProperty desiredResolvedFilePath);

    /**
     * Convenience method. Same as {@link #addTemplateFile(RegularFileProperty, RegularFileProperty) addTemplateFile}
     */
    void addTemplateFiles(@Nonnull Map<RegularFileProperty, RegularFileProperty> templateFiles);

    /**
     * Resolved (output) file name will be the same as the input file name
     * Convenience method. Same as {@link #addTemplateFile(RegularFileProperty, RegularFileProperty) addTemplateFile}
     */
    void addTemplateFile(@Nonnull RegularFileProperty jinjaTemplateFile, @Nonnull DirectoryProperty desiredResolvedDirPath);

    /**
     * Resolved (output) file names will be the same as the input file names
     * Convenience method. Same as {@link #addTemplateFile(RegularFileProperty, RegularFileProperty) addTemplateFile}
     */
    void addTemplateFiles(@Nonnull FileCollection fileCollection, @Nonnull DirectoryProperty desiredResolvedDirPath);

    /**
     * Function searches for template files recursively, resolves them and writes the output
     * @param templateDirectory is a directory that contains folders and jinja template files only
     * @param desiredResolvedDir is a directory where resolved files will be stored
     * @param preserveDirStructure if set to true all subdirectories that contain regular files (templates) will be
     * copied to desiredResolvedDir (relative paths will remain the same). Resolved files will be placed inside of them
     * accordingly. Otherwise, no subdirectories will be created and all resolved files will be stored in
     * desiredResolvedDir
     */
    void addTemplateFiles(@Nonnull DirectoryProperty templateDirectory, @Nonnull DirectoryProperty desiredResolvedDir, boolean preserveDirStructure);
}
