package io.github.rururu387.mergers;

/**
 * This enum specifies different parameter merging strategies
 * @see #OVERRIDE_MAPS
 * @see #MERGE_MAPS
 */
public enum MergeStrategy {
    /**
     * Choose this if you want the properties to be merged the jinja2 (ansible) way. Only parameters with unique keys from
     * former source are preserved and all parameters from the latter source are included to the resulting parameters.
     * Example:<br/>
     * Source 1:
     * <pre><code>
     * key:
     *   subKey: value
     * key1: value1
     * list:
     *   - el1
     *   - subListMap:
     *     subListKey: value
     *   - el3
     * key2:
     *   - el
     * </code></pre>
     * Source 2:
     * <pre><code>
     * key:
     *   subKey2: value2
     * key2: value2
     * list:
     *   - el1
     *   - el2
     * </code></pre>
     * Resulting params:
     * <pre><code>
     * key: # Source 1 value ignored
     *   subKey2: value2
     * key1: value1
     * key2: value2 # Source 1 value ignored
     * list: # Source 1 value ignored
     *   - el1
     *   - el2
     * </code></pre>
     */
    OVERRIDE_MAPS,

    /**
     * Choose this if you want the properties to be merged the go templates (helm) way. Everything but maps gets
     * overriden in former parameters. Maps get merged, the resulting map contains entries from both sources.
     * If former source contained map but in the same path latter source contains list then map is discarded. Example:<br/>
     * Source 1:
     * <pre><code>
     * key:
     *   subKey: value
     * key1: value1
     * list:
     *   - el1
     *   - subListMap:
     *     subListKey: value
     *   - el3
     * key2:
     *   - el
     * </code></pre>
     * Source 2:
     * <pre><code>
     * key:
     *   subKey2: value2
     * key2: value2
     * list:
     *   - el1
     *   - el2
     * </code></pre>
     * Resulting params:
     * <pre><code>
     * key:
     *   subKey: value
     *   subKey2: value2
     * key1: value1
     * key2: value2 # Source 1 value ignored
     * list: # Source 1 value ignored
     *   - el1
     *   - el2
     * </code></pre>
     **/
    MERGE_MAPS;
}
