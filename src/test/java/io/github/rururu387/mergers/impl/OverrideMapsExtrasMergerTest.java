package io.github.rururu387.mergers.impl;

import org.junit.jupiter.api.Test;

public class OverrideMapsExtrasMergerTest extends MergerTestBase {
    public OverrideMapsExtrasMergerTest() {
        super(new OverrideMapsExtrasMerger());
    }

    @Test
    void WHEN_two_files_with_two_different_keys_merged_THEN_result_contains_both() {
        var overriddenYaml = """
                key: value
                """;
        var overridingYaml = """
                key2: value2
                """;
        var expectedYaml = """
                key: value
                key2: value2""";
        checkYamlMerge(expectedYaml, overriddenYaml, overridingYaml);
    }

    @Test
    void WHEN_two_files_with_same_key_and_subkey_merged_THEN_result_contains_overriding() {
        var overriddenYaml = """
                key:
                  subkey: val
                """;
        var overridingYaml = """
                key:
                  subkey: val2
                """;
        var expectedYaml = """
                key:
                  subkey: val2""";
        checkYamlMerge(expectedYaml, overriddenYaml, overridingYaml);
    }

    @Test
    void WHEN_two_files_with_two_different_lists_merged_THEN_result_contains_both() {
        var overridenYaml = """
                list1:
                  - a
                """;
        var overridingYaml = """
                list2:
                  - a
                """;
        var expectedYaml = """
                list1:
                  - a
                list2:
                  - a""";
        checkYamlMerge(expectedYaml, overridenYaml, overridingYaml);
    }

    @Test
    void WHEN_two_files_with_same_list_merged_THEN_result_contains_overriding_only() {
        var overriddenYaml = """
                list:
                  - a
                  - b
                  - 0
                """;
        var overridingYaml = """
                list:
                  - true
                  - 44
                  - abc
                """;
        var expectedYaml = """
                list:
                  - true
                  - 44
                  - abc
                """;
        checkYamlMerge(expectedYaml, overriddenYaml, overridingYaml);
    }

    @Test
    void WHEN_overriden_file_with_same_key_but_overriding_file_has_list_THEN_overriding_only() {
        var overriddenYaml = """
                key:
                  subKey: subVal
                """;
        var overridingYaml = """
                key:
                  - true
                  - 44
                  - abc
                """;
        var expectedYaml = """
                key:
                  - true
                  - 44
                  - abc
                """;
        checkYamlMerge(expectedYaml, overriddenYaml, overridingYaml);
    }

    // Key difference Between MERGE_MAPS merger and OVERRIDE_MAPS strategy
    @Test
    void WHEN_two_files_with_same_keys_but_different_subkeys_merged_THEN_result_contains_both_subkeys() {
        var overriddenYaml = """
                key:
                  subKey1: subValue1
                """;
        var overridingYaml = """
                key:
                  subKey2: subValue2
                """;
        var expectedYaml = """
                key:
                  subKey2: subValue2
                """;
        checkYamlMerge(expectedYaml, overriddenYaml, overridingYaml);
    }

    @Test
    void WHEN_two_files_with_same_keys_but_overriding_null_THEN_result_value_is_null() {
        var overriddenYaml = """
                key: value
                """;
        var overridingYaml = """
                key: null
                """;
        var expectedYaml = """
                key: null
                """;
        checkYamlMerge(expectedYaml, overriddenYaml, overridingYaml);
    }

    @Test
    void WHEN_two_files_with_same_keys_but_overriding_empty_map_THEN_result_value_is_empty_map() {
        var overriddenYaml = """
                key: value
                """;
        var overridingYaml = """
                key: {}
                """;
        var expectedYaml = """
                key: {}
                """;
        checkYamlMerge(expectedYaml, overriddenYaml, overridingYaml);
    }

    @Test
    void WHEN_two_files_merged_but_overriding_empty_map_THEN_result_value_is_present() {
        var overriddenYaml = """
                key: value
                """;
        var overridingYaml = """
                {}
                """;
        var expectedYaml = """
                key: value
                """;
        checkYamlMerge(expectedYaml, overriddenYaml, overridingYaml);
    }

    @Test
    void WHEN_two_files_merged_but_overridden_file_empty_THEN_result_value_is_present() {
        var overriddenYaml = "";
        var overridingYaml = """
                key: value
                """;
        var expectedYaml = """
                key: value
                """;
        checkYamlMerge(expectedYaml, overriddenYaml, overridingYaml);
    }

    @Test
    void WHEN_two_files_merged_but_overriding_file_empty_THEN_result_value_is_present() {
        var overriddenYaml = """
                key: value
                """;
        var overridingYaml = "";
        var expectedYaml = """
                key: value
                """;
        checkYamlMerge(expectedYaml, overriddenYaml, overridingYaml);
    }

    @Test
    void WHEN_two_files_merged_but_overriding_file_null_THEN_result_value_is_present() {
        var overriddenYaml = """
                key: value
                """;
        String overridingYaml = null;
        var expectedYaml = """
                key: value
                """;
        checkYamlMerge(expectedYaml, overriddenYaml, overridingYaml);
    }
}
