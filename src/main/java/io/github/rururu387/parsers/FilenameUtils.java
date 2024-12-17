package io.github.rururu387.parsers;

import jakarta.annotation.Nonnull;

public class FilenameUtils {
    @Nonnull
    static public String getExtension(@Nonnull String fileName) {
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            return fileName.substring(i+1);
        }
        return "";
    }
}
