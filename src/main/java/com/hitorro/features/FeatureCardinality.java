package com.hitorro.features;


import com.hitorro.util.core.EnumContext;

/**
 * Determines if this is a single or multivalued feature
 */
public enum FeatureCardinality {
    single(0, "single", false),
    named(1, "named", true),
    index(2, "index", true);

    public static EnumContext<FeatureCardinality> context;
    private String name;
    private boolean isSingle;
    private int ord;

    FeatureCardinality(int ord, String name, boolean isSingle) {
        this.ord = ord;
        this.name = name;
        this.isSingle = isSingle;
        setMapEntry(this);
    }

    public static FeatureCardinality getDefault() {
        return single;
    }

    private static void setMapEntry(FeatureCardinality filter) {
        if (context == null) {
            context = new EnumContext<FeatureCardinality>("datatype");
        }

        context.setNames(filter, filter.getName(), filter.getStableOrdinal());
    }

    public int getStableOrdinal() {
        return ord;
    }

    public String getName() {
        return name;
    }

    public boolean isSingle() {
        return isSingle;
    }
}
