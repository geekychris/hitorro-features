package com.hitorro.features.collections.mapping.noop;

import com.hitorro.features.collections.FeatureCollectionContext;
import com.hitorro.features.collections.mapping.ScoreFunctionBase;
import com.hitorro.features.collections.mapping.intf.IntegerScoreMappingFunction;

/**
 *
 */
public class NoOpIntegerScoreMappingFunction extends ScoreFunctionBase implements IntegerScoreMappingFunction {
    public float score(int i, FeatureCollectionContext fcc) {
        if (i == NULL) {
            return getDefaultValue();
        }
        return (float) i;
    }
}
