package com.hitorro.features.collections.mapping.intf;

import com.hitorro.features.collections.FeatureCollectionContext;

/**
 *
 */
public interface ShortScoreMappingFunction extends ScoreMappingFunction {
    short NULL = Short.MIN_VALUE;

    float score(short f, FeatureCollectionContext fcc);
}
