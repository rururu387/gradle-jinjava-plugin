package io.github.rururu387.mergers.impl;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.rururu387.parsers.impl.YamlExtrasParser;
import io.github.rururu387.mergers.ExtrasMerger;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MergerTestBase {
    protected static final YamlExtrasParser YAML_EXTRAS_PARSER = new YamlExtrasParser();
    protected final ExtrasMerger merger;

    public MergerTestBase(ExtrasMerger merger) {
        this.merger = merger;
    }

    protected void checkYamlMerge(String expectedYaml, String overriddenYaml, String overridingYaml) {
        try {
            JsonNode expectedNode = null;
            JsonNode overriddenNode = null;
            JsonNode overridingNode = null;
            if (expectedYaml != null) {
                expectedNode = YAML_EXTRAS_PARSER.parse(expectedYaml);
            }
            if (overriddenYaml != null) {
                overriddenNode = YAML_EXTRAS_PARSER.parse(overriddenYaml);
            }
            if (overridingYaml != null) {
                overridingNode = YAML_EXTRAS_PARSER.parse(overridingYaml);
            }
            var actualNode = merger.merge(overriddenNode, overridingNode);
            assertEquals(expectedNode, actualNode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
