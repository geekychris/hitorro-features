package com.hitorro.features.collections.mapping;

import com.hitorro.features.collections.mapping.intf.ScoreMappingFunction;
import com.hitorro.util.core.params.HTProperties;

/**
 * Implement default setup of a nop
 */
public abstract class ScoreFunctionBase implements ScoreMappingFunction {
    private float defaultValue;

    public void init(HTProperties props) {
        defaultValue = Float.MIN_VALUE;
    }

    public float getDefaultValue() {
        return defaultValue;
    }
}
