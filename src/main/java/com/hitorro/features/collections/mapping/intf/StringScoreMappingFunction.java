package com.hitorro.features.collections.mapping.intf;

import com.hitorro.features.collections.FeatureCollectionContext;

/**
 *
 */
public interface StringScoreMappingFunction extends ScoreMappingFunction {
    String NULL = null;

    float score(String s, FeatureCollectionContext fcc);
}
