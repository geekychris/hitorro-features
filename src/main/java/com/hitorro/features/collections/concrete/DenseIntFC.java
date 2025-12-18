package com.hitorro.features.collections.concrete;

import com.hitorro.features.collections.BaseCollection;
import com.hitorro.features.collections.FeatureCollectionContext;
import com.hitorro.features.collections.mapping.intf.IntegerScoreMappingFunction;
import com.hitorro.features.io.IntegerIO;

/**
 *
 */
public class DenseIntFC extends DenseFeatureCollection<IntegerScoreMappingFunction, IntegerIO> {
    private int[] arr;
    private int nullValue;

    public DenseIntFC() {

    }

    public void setSize(int size) {
        arr = new int[size];
        nullValue = (Integer) feature.getNullValue();
        for (int i = 0; i < size; i++) {
            arr[i] = nullValue;
        }
    }

    @Override
    public void addRow(final int index, final IntegerIO value) {
        arr[index] = value.getInteger();
    }

    @Override
    public boolean contains(final int ind) {
        return arr[ind] != nullValue;
    }


    @Override
    public BaseCollection cloneMe() {
        return new DenseByteFC();
    }

    @Override
    public float score(int index, long ids[], FeatureCollectionContext fcc) {
        int b = getInteger(index, ids, fcc);

        return scoreFunction.score(b, fcc);
    }

    public int getInteger(int index, long ids[], FeatureCollectionContext fcc) {
        return arr[index];
    }
}
