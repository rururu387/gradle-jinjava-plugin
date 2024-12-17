package io.github.rururu387.mergers.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.rururu387.mergers.ExtrasMerger;
import jakarta.annotation.Nullable;

import java.util.Iterator;
import java.util.Map;

public class OverrideMapsExtrasMerger implements ExtrasMerger {
    @Override
    public JsonNode merge(@Nullable JsonNode overriddenNode, @Nullable JsonNode overridingNode) {
        if (overridingNode == null) {
            return overriddenNode;
        }
        if (overridingNode.isObject()) {
            if (overriddenNode == null) {
                return overridingNode;
            }
            if (!overriddenNode.isObject()) {
                return overridingNode;
            }
            var overriddenObjNode = (ObjectNode) overriddenNode;
            var overridingObjNode = (ObjectNode) overridingNode;
            for (Iterator<Map.Entry<String, JsonNode>> it = overridingObjNode.fields(); it.hasNext(); ) {
                var overridingSubNode = it.next();
                overriddenObjNode.set(overridingSubNode.getKey(), overridingSubNode.getValue());
            }
        } else if (overridingNode.isArray()) {
            return overridingNode;
        }
        return overriddenNode;
    }
}
