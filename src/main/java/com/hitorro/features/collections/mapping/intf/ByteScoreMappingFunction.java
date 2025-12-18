package com.hitorro.features.collections.mapping.intf;

import com.hitorro.features.collections.FeatureCollectionContext;

/**
 *
 */
public interface ByteScoreMappingFunction extends ScoreMappingFunction {
    byte NULL = Byte.MIN_VALUE;

    float score(byte b, FeatureCollectionContext fcc);
}
