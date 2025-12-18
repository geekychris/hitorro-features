package com.hitorro.features;

import java.util.HashMap;
import java.util.Set;

/**
 *
 */
public enum FeatureType {
    base("base", true) {
        public Feature getFeatureInstance() {
            return new Feature();
        }
    },
    derived("derived", false) {
        public Feature getFeatureInstance() {
            return new DerivedFeature();
        }
    },
    function("function", false) {
        public Feature getFeatureInstance() {
            return new Feature();
        }
    };

    private static HashMap<String, FeatureType> s_byShortName;

    private String name;
    private boolean shouldLookForChildren;

    FeatureType(String name, boolean shouldLookForChildren) {
        this.name = name;
        this.shouldLookForChildren = shouldLookForChildren;
        setMapEntry(this);
    }

    public static Set<String> getValidValues() {
        return s_byShortName.keySet();
    }

    public static FeatureType getDefault() {
        return base;
    }

    public static FeatureType getByName(String name) {
        return s_byShortName.get(name.toLowerCase());
    }

    public static int size() {
        return s_byShortName.size();
    }

    private static void setMapEntry(FeatureType filter) {
        if (s_byShortName == null) {
            s_byShortName = new HashMap<String, FeatureType>();
        }
        s_byShortName.put(filter.getName(), filter);
    }

    public abstract Feature getFeatureInstance();

    public boolean getShouldLookForChildren() {
        return shouldLookForChildren;
    }

    public String getName() {
        return name;
    }
}
