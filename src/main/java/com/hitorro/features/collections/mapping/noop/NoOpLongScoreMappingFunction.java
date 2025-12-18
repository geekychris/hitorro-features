package com.hitorro.features.collections.mapping.noop;

import com.hitorro.features.collections.FeatureCollectionContext;
import com.hitorro.features.collections.mapping.ScoreFunctionBase;
import com.hitorro.features.collections.mapping.intf.LongScoreMappingFunction;

/**
 *
 */
public class NoOpLongScoreMappingFunction extends ScoreFunctionBase implements LongScoreMappingFunction {
    public float score(long i, FeatureCollectionContext fcc) {
        if (i == NULL) {
            return getDefaultValue();
        }
        return (float) i;
    }
}
