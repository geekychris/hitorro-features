package com.hitorro.features.collections.mapping.noop;

import com.hitorro.features.collections.FeatureCollectionContext;
import com.hitorro.features.collections.mapping.intf.FloatScoreMappingFunction;
import com.hitorro.util.core.params.HTProperties;

/**
 *
 */
public class NoOpFloatScoreMappingFunction implements FloatScoreMappingFunction {
    public float defaultVal;

    public float score(float f, FeatureCollectionContext fcc) {
        if (f == NULL) {
            return getDefaultValue();
        }
        return f;
    }

    public void init(HTProperties props) {
        defaultVal = NULL;
    }

    public float getDefaultValue() {
        return defaultVal;
    }
}
