package io.github.rururu387.configs;

import io.github.rururu387.configs.interfaces.ExtrasConfigurable;
import io.github.rururu387.configs.interfaces.TemplateIOFilesConfigurable;
import io.github.rururu387.mergers.MergeStrategy;
import jakarta.annotation.Nonnull;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.FileCollection;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.tasks.CacheableTask;
import org.gradle.work.DisableCachingByDefault;

import javax.inject.Inject;
import java.util.Map;

@CacheableTask
public class JinjavaTaskConfig extends DefaultTask implements TemplateIOFilesConfigurable, ExtrasConfigurable {
    public TemplateIOFilesConfig templateIoFilesConfig;
    public ExtrasConfig extrasConfig;

    @Inject
    public JinjavaTaskConfig(ObjectFactory objectFactory) {
        this.templateIoFilesConfig = new TemplateIOFilesConfig(objectFactory);
        this.extrasConfig = new ExtrasConfig();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void ignoreMissingTemplateFiles(boolean ignoreMissingTemplateFiles) {
        templateIoFilesConfig.ignoreMissingTemplateFiles(ignoreMissingTemplateFiles);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addTemplateFile(@Nonnull RegularFileProperty jinjaTemplateFile, @Nonnull RegularFileProperty desiredResolvedFilePath) {
        templateIoFilesConfig.addTemplateFile(jinjaTemplateFile, desiredResolvedFilePath);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addTemplateFiles(@Nonnull Map<RegularFileProperty, RegularFileProperty> templateFiles) {
        templateIoFilesConfig.addTemplateFiles(templateFiles);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addTemplateFile(@Nonnull RegularFileProperty jinjaTemplateFile, @Nonnull DirectoryProperty desiredResolvedDirPath) {
        templateIoFilesConfig.addTemplateFile(jinjaTemplateFile, desiredResolvedDirPath);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addTemplateFiles(@Nonnull FileCollection fileCollection, @Nonnull DirectoryProperty desiredResolvedDirPath) {
        templateIoFilesConfig.addTemplateFiles(fileCollection, desiredResolvedDirPath);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addTemplateFiles(@Nonnull DirectoryProperty templateDirectory, @Nonnull DirectoryProperty desiredResolvedDir, boolean preserveDirStructure) {
        templateIoFilesConfig.addTemplateFiles(templateDirectory, desiredResolvedDir, preserveDirStructure);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void ignoreMissingExtrasFiles(boolean ignoreMissingExtrasFiles) {
        extrasConfig.ignoreMissingExtrasFiles(ignoreMissingExtrasFiles);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void extras(@Nonnull Map<String, Object> extras) {
        extrasConfig.extras(extras);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addExtraFile(@Nonnull RegularFileProperty extraFileProperty) {
        extrasConfig.addExtraFile(extraFileProperty);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mergeStrategy(@Nonnull MergeStrategy mergeStrategy) {
        extrasConfig.mergeStrategy(mergeStrategy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeMergedValues(RegularFileProperty valuesOutputFile) {
        extrasConfig.writeMergedValues(valuesOutputFile);
    }
}
