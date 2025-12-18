package com.hitorro.features.collections.mapping.noop;

import com.hitorro.features.collections.FeatureCollectionContext;
import com.hitorro.features.collections.mapping.ScoreFunctionBase;
import com.hitorro.features.collections.mapping.intf.ShortScoreMappingFunction;

/**
 *
 */
public class NoOpShortScoreMappingFunction extends ScoreFunctionBase implements ShortScoreMappingFunction {
    public float score(short s, FeatureCollectionContext fcc) {
        if (s == NULL) {
            return getDefaultValue();
        }
        return s;
    }
}
