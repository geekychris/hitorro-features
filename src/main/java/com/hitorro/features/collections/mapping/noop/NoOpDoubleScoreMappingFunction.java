package com.hitorro.features.collections.mapping.noop;

import com.hitorro.features.collections.FeatureCollectionContext;
import com.hitorro.features.collections.mapping.ScoreFunctionBase;
import com.hitorro.features.collections.mapping.intf.DoubleScoreMappingFunction;

/**
 *
 */
public class NoOpDoubleScoreMappingFunction extends ScoreFunctionBase implements DoubleScoreMappingFunction {
    public float score(double f, FeatureCollectionContext fcc) {
        if (f == NULL) {
            return getDefaultValue();
        }
        return (float) f;
    }
}