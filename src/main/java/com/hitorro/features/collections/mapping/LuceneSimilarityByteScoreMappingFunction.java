package com.hitorro.features.collections.mapping;

import com.hitorro.features.collections.FeatureCollectionContext;
import com.hitorro.features.collections.mapping.intf.ByteScoreMappingFunction;
import com.hitorro.util.core.params.HTProperties;
import org.apache.lucene.util.SmallFloat;

/**
 * Assuming we are packing the byte from a float using lucenes float-> byte function
 */
public class LuceneSimilarityByteScoreMappingFunction implements ByteScoreMappingFunction {
    public byte defaultVal;

    public float score(byte b, FeatureCollectionContext fcc) {
        if (b == NULL) {
            return getDefaultValue();
        }
        return SmallFloat.byte315ToFloat(b);
    }

    public void init(HTProperties props) {
        defaultVal = NULL;
    }

    public float getDefaultValue() {
        return defaultVal;
    }
}
