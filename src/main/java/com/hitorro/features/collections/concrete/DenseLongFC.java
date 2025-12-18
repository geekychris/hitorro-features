package com.hitorro.features.collections.concrete;

import com.hitorro.features.collections.BaseCollection;
import com.hitorro.features.collections.FeatureCollectionContext;
import com.hitorro.features.collections.mapping.intf.LongScoreMappingFunction;
import com.hitorro.features.io.LongIO;

/**
 *
 */
public class DenseLongFC extends DenseFeatureCollection<LongScoreMappingFunction, LongIO> {
    private long[] arr;
    private long nullValue;

    public DenseLongFC() {

    }

    public void setSize(int size) {
        arr = new long[size];
        nullValue = (Long) feature.getNullValue();
        for (int i = 0; i < size; i++) {
            arr[i] = nullValue;
        }
    }

    @Override
    public void addRow(final int index, final LongIO value) {
        arr[index] = value.getLong();
    }

    @Override
    public boolean contains(final int ind) {
        return arr[ind] != nullValue;
    }


    @Override
    public BaseCollection cloneMe() {
        return new DenseLongFC();
    }

    @Override
    public float score(int index, long ids[], FeatureCollectionContext fcc) {
        long b = getLong(index, ids, fcc);

        return scoreFunction.score(b, fcc);
    }

    public long getLong(int index, long ids[], FeatureCollectionContext fcc) {
        return arr[index];
    }
}
