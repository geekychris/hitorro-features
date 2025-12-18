package com.hitorro.features.collections.concrete;

import com.hitorro.features.collections.BaseCollection;
import com.hitorro.features.collections.FeatureCollectionContext;
import com.hitorro.features.collections.mapping.intf.FloatScoreMappingFunction;
import com.hitorro.features.io.FloatIO;

/**
 *
 */
public class DenseFloatFC extends DenseFeatureCollection<FloatScoreMappingFunction, FloatIO> {
    private float[] arr;
    private float nullValue;

    public DenseFloatFC() {

    }

    public void setSize(int size) {
        arr = new float[size];
        nullValue = (Float) feature.getNullValue();
        for (int i = 0; i < size; i++) {
            arr[i] = nullValue;
        }
    }

    @Override
    public void addRow(final int index, final FloatIO value) {
        arr[index] = value.getFloat();
    }

    @Override
    public boolean contains(final int ind) {
        return arr[ind] != nullValue;
    }


    @Override
    public BaseCollection cloneMe() {
        return new DenseFloatFC();
    }

    @Override
    public float score(int index, long ids[], FeatureCollectionContext fcc) {
        float b = getFloat(index, ids, fcc);

        return scoreFunction.score(b, fcc);
    }

    public float getFloat(int index, long ids[], FeatureCollectionContext fcc) {
        return arr[index];
    }
}
