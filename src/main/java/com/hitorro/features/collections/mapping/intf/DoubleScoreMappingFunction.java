package com.hitorro.features.collections.mapping.intf;

import com.hitorro.features.collections.FeatureCollectionContext;

/**
 *
 */
public interface DoubleScoreMappingFunction extends ScoreMappingFunction {
    double NULL = Double.MIN_VALUE;

    float score(double f, FeatureCollectionContext fcc);
}

