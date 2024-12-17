package io.github.rururu387.configs.interfaces;

import io.github.rururu387.mergers.MergeStrategy;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.gradle.api.file.RegularFileProperty;

import java.util.Map;

public interface ExtrasConfigurable {
    /**
     * If set to true plugin does not throw errors when specified via {@link #addExtraFile(RegularFileProperty) addExtrasFile}
     * file not found. Defaults to true.
     */
    void ignoreMissingExtrasFiles(boolean ignoreMissingExtrasFiles);

    /**
     * Pass parameters from gradle script to jinja template. These parameters will get merged last (just like in ansible
     * and helm).
     */
    void extras(@Nonnull Map<String, Object> extras);

    /**
     * <h4>Warning</h4>
     * Order of calls of this function is important. The file parameters for which the function is called earlier will
     * be overwritten by the file parameters for which the function is called later.
     * @param extraFileProperty file containing parameters that will be used for templating. Somewhat like extras files
     * in Ansible. Can have one of the following formats:
     * <ul>
     *  <li>yaml</li>
     *  <li>json</li>
     *  <li>toml</li>
     *  <li>properties</li>
     * </ul>
     * Make sure to set correct file extensions. If file extension is unrecognized it is parsed as if it was yaml.
     * @see MergeStrategy
     */
    void addExtraFile(@Nonnull RegularFileProperty extraFileProperty);

    /**
     * Specify the way extras (parameters) get merged. Default is OVERRIDE_MAPS, parameters are merged the ansible way.
     * @see MergeStrategy#MERGE_MAPS
     * @see MergeStrategy#OVERRIDE_MAPS
     */
    void mergeStrategy(@Nonnull MergeStrategy mergeStrategy);

    /**
     * Specify file where merged variables should be written. Useful for debugging.
     * If not set or set to null - don't print values file.
     */
    void writeMergedValues(@Nullable RegularFileProperty destinationFile);
}
