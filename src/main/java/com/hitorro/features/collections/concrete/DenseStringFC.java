package com.hitorro.features.collections.concrete;

import com.hitorro.features.collections.BaseCollection;
import com.hitorro.features.collections.FeatureCollectionContext;
import com.hitorro.features.collections.mapping.intf.StringScoreMappingFunction;
import com.hitorro.features.io.StringIO;

/**
 *
 */
public class DenseStringFC extends DenseFeatureCollection<StringScoreMappingFunction, StringIO> {
    private String[] arr;
    private String nullValue;

    public DenseStringFC() {

    }

    public void setSize(int size) {
        arr = new String[size];
        nullValue = (String) feature.getNullValue();
        for (int i = 0; i < size; i++) {
            arr[i] = nullValue;
        }
    }

    @Override
    public void addRow(final int index, final StringIO value) {
        arr[index] = value.getString();
    }

    @Override
    public boolean contains(final int ind) {
        return arr[ind] != nullValue;
    }


    @Override
    public BaseCollection cloneMe() {
        return new DenseStringFC();
    }

    @Override
    public float score(int index, long ids[], FeatureCollectionContext fcc) {
        String b = getString(index, ids, fcc);

        return scoreFunction.score(b, fcc);
    }

    public String getString(int index, long ids[], FeatureCollectionContext fcc) {
        return arr[index];
    }
}
