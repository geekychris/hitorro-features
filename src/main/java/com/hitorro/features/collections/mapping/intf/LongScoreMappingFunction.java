package com.hitorro.features.collections.mapping.intf;

import com.hitorro.features.collections.FeatureCollectionContext;

/**
 *
 */
public interface LongScoreMappingFunction extends ScoreMappingFunction {
    long NULL = Long.MIN_VALUE;

    float score(long f, FeatureCollectionContext fcc);
}
