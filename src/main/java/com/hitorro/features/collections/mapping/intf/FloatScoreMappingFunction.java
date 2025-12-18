package com.hitorro.features.collections.mapping.intf;

import com.hitorro.features.collections.FeatureCollectionContext;

/**
 *
 */
public interface FloatScoreMappingFunction extends ScoreMappingFunction {
    float NULL = Float.MIN_VALUE;

    float score(float f, FeatureCollectionContext fcc);
}
