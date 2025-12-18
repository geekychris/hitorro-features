package com.hitorro.features.collections.mapping.intf;

import com.hitorro.util.core.params.HTProperties;

/**
 * Score feautures that store their data in a form that needs to be expanded (such as byte->float) implement one of
 * these
 */
public interface ScoreMappingFunction {

    /**
     * Called on initialization to define any specific behavior this mapping function needs
     *
     * @param props
     */
    void init(HTProperties props);


    /**
     * Allows a way to return what the default value is
     *
     * @return
     */
    float getDefaultValue();
}
