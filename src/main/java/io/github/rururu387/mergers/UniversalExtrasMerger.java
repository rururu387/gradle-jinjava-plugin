package io.github.rururu387.mergers;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.rururu387.mergers.impl.MergeMapsExtrasMerger;
import io.github.rururu387.mergers.impl.OverrideMapsExtrasMerger;

public class UniversalExtrasMerger {
    private final MergeStrategy mergeStrategy;

    private static ExtrasMerger mergeMapsMerger;
    private static ExtrasMerger overrideMapsMerger;

    public UniversalExtrasMerger(MergeStrategy mergeStrategy) {
        this.mergeStrategy = mergeStrategy;
    }

    private static ExtrasMerger getMergeMapsMerger() {
        if (mergeMapsMerger == null) {
             mergeMapsMerger = new MergeMapsExtrasMerger();
        }
        return mergeMapsMerger;
    }

    private static ExtrasMerger getOverrideMapsMerger() {
        if (overrideMapsMerger == null) {
            overrideMapsMerger = new OverrideMapsExtrasMerger();
        }
        return overrideMapsMerger;
    }

    public JsonNode merge(JsonNode overriddenParams, JsonNode overridingParams) {
        return (switch (mergeStrategy) {
            case MERGE_MAPS -> getMergeMapsMerger();
            case OVERRIDE_MAPS -> getOverrideMapsMerger();
        }).merge(overriddenParams, overridingParams);
    }
}
