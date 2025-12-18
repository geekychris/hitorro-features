package com.hitorro.features;

import com.hitorro.util.core.EnumContext;

import java.util.HashMap;
import java.util.Set;

/**
 * Determines if any segmentation is applied to feature values
 */
public enum PartitionModel {
    None("none"),
    Date("date"),
    // note in this case partitioning has to happen further upstream of the vectorization process or filtered
    // at read time (since hashes are the last thing to be used in the composite key for sorting)
    Hash("hash");

    public static EnumContext<PartitionModel> context;
    private static HashMap<String, PartitionModel> s_byShortName;
    private String name;

    PartitionModel(String name) {
        this.name = name;
        setMapEntry(this);
    }

    public static Set<String> getValidValues() {
        return s_byShortName.keySet();
    }

    public static PartitionModel getDefault() {
        return None;
    }

    public static PartitionModel getByName(String name) {
        return s_byShortName.get(name.toLowerCase());
    }

    public static int size() {
        return s_byShortName.size();
    }

    private static void setMapEntry(PartitionModel filter) {
        if (context == null) {
            context = new EnumContext<PartitionModel>("featurekeytype");
        }

        context.setNames(filter, filter.getName(), filter.ordinal());
    }

    public String getName() {
        return name;
    }
}
