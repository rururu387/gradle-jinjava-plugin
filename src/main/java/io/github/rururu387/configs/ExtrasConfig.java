package io.github.rururu387.configs;

import io.github.rururu387.configs.interfaces.ExtrasConfigurable;
import io.github.rururu387.mergers.MergeStrategy;
import jakarta.annotation.Nonnull;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFiles;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExtrasConfig implements ExtrasConfigurable {
    public Map<String, Object> extras;

    public final List<RegularFileProperty> extraFiles = new ArrayList<>();

    public boolean ignoreMissingExtrasFiles = false;

    public MergeStrategy mergeStrategy = MergeStrategy.OVERRIDE_MAPS;

    public RegularFileProperty valuesOutputFile = null;

    @Input
    public boolean isIgnoreMissingExtrasFiles() {
        return ignoreMissingExtrasFiles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void ignoreMissingExtrasFiles(boolean ignoreMissingExtrasFiles) {
        this.ignoreMissingExtrasFiles = ignoreMissingExtrasFiles;
    }

    @Input
    public Map<String, Object> getExtrasMap() {
        return extras;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void extras(@Nonnull Map<String, Object> extras) {
        this.extras = extras;
    }

    @InputFiles
    public List<RegularFileProperty> getExtraFileProperties() {
        return extraFiles;
    }

    public List<Path> getExtraFilePaths() {
        return extraFiles.stream().map(fileProperty -> fileProperty.getAsFile().get().toPath()).toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addExtraFile(@Nonnull RegularFileProperty extraFileProperty) {
        this.extraFiles.add(extraFileProperty);
    }

    @Input
    public MergeStrategy getMergeStrategy() {
        return mergeStrategy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mergeStrategy(@Nonnull MergeStrategy mergeStrategy) {
        this.mergeStrategy = mergeStrategy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeMergedValues(RegularFileProperty valuesOutputFile) {
        this.valuesOutputFile = valuesOutputFile;
    }
}
