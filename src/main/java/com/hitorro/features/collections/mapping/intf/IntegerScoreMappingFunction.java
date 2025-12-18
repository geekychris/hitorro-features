package com.hitorro.features.collections.mapping.intf;

import com.hitorro.features.collections.FeatureCollectionContext;

/**
 *
 */
public interface IntegerScoreMappingFunction extends ScoreMappingFunction {
    int NULL = Integer.MIN_VALUE;

    float score(int f, FeatureCollectionContext fcc);
}
