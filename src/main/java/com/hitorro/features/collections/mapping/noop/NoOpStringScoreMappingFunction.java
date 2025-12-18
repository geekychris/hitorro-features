package com.hitorro.features.collections.mapping.noop;

import com.hitorro.features.collections.FeatureCollectionContext;
import com.hitorro.features.collections.mapping.ScoreFunctionBase;
import com.hitorro.features.collections.mapping.intf.StringScoreMappingFunction;

/**
 * Non performant mechanism to apply scores from strings.  Assumes that string looks like a float in text form.
 */
public class NoOpStringScoreMappingFunction extends ScoreFunctionBase implements StringScoreMappingFunction {
    public float score(String s, FeatureCollectionContext fcc) {
        if (s == NULL) {
            return getDefaultValue();
        }
        return Float.parseFloat(s);
    }
}
