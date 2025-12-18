package com.hitorro.features.collections.mapping.noop;

import com.hitorro.features.collections.FeatureCollectionContext;
import com.hitorro.features.collections.mapping.ScoreFunctionBase;
import com.hitorro.features.collections.mapping.intf.ByteScoreMappingFunction;

/**
 * Do nothing implementation of byte scorer
 */
public class NoOpByteScoreMappingFunction extends ScoreFunctionBase implements ByteScoreMappingFunction {
    public float score(byte b, FeatureCollectionContext fcc) {
        if (b == NULL) {
            return getDefaultValue();
        }
        return (float) b;
    }
}
