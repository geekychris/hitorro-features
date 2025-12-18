package com.hitorro.features.collections.concrete;

import com.hitorro.features.collections.BaseCollection;
import com.hitorro.features.collections.FeatureCollectionContext;
import com.hitorro.features.collections.mapping.intf.DoubleScoreMappingFunction;
import com.hitorro.features.io.DoubleIO;

/**
 *
 */
public class DenseDoubleFC extends DenseFeatureCollection<DoubleScoreMappingFunction, DoubleIO> {
    private double[] arr;
    private double nullValue;

    public DenseDoubleFC() {

    }

    public void setSize(int size) {
        arr = new double[size];
        nullValue = (Double) feature.getNullValue();
        for (int i = 0; i < size; i++) {
            arr[i] = nullValue;
        }
    }

    @Override
    public void addRow(final int index, final DoubleIO value) {
        arr[index] = value.getDouble();
    }

    @Override
    public boolean contains(final int ind) {
        return arr[ind] != nullValue;
    }


    @Override
    public BaseCollection cloneMe() {
        return new DenseDoubleFC();
    }

    @Override
    public float score(int index, long ids[], FeatureCollectionContext fcc) {
        double b = getDouble(index, ids, fcc);

        return scoreFunction.score(b, fcc);
    }

    @Override
    public double getDouble(int index, long ids[], FeatureCollectionContext fcc) {
        return arr[index];
    }
}
