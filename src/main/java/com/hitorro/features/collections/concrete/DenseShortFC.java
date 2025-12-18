package com.hitorro.features.collections.concrete;

import com.hitorro.features.collections.BaseCollection;
import com.hitorro.features.collections.FeatureCollectionContext;
import com.hitorro.features.collections.mapping.intf.ShortScoreMappingFunction;
import com.hitorro.features.io.ShortIO;

/**
 *
 */
public class DenseShortFC extends DenseFeatureCollection<ShortScoreMappingFunction, ShortIO> {
    private short[] arr;
    private short nullValue;

    public DenseShortFC() {

    }

    public void setSize(int size) {
        arr = new short[size];
        nullValue = (Short) feature.getNullValue();
        for (int i = 0; i < size; i++) {
            arr[i] = nullValue;
        }
    }

    @Override
    public void addRow(final int index, final ShortIO value) {
        arr[index] = value.getShort();
    }

    @Override
    public boolean contains(final int ind) {
        return arr[ind] != nullValue;
    }


    @Override
    public BaseCollection cloneMe() {
        return new DenseShortFC();
    }

    @Override
    public float score(int index, long ids[], FeatureCollectionContext fcc) {
        short b = getShort(index, ids, fcc);

        return scoreFunction.score(b, fcc);
    }

    public short getShort(int index, long ids[], FeatureCollectionContext fcc) {
        return arr[index];
    }
}