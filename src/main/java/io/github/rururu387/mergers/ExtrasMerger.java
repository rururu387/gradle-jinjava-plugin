package io.github.rururu387.mergers;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.Nullable;

public interface ExtrasMerger {
    JsonNode merge(@Nullable JsonNode overriddenParams, @Nullable JsonNode overridingParams);
}
