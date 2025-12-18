package com.hitorro.features.collections.concrete;

import com.hitorro.features.collections.BaseCollection;
import com.hitorro.features.collections.FeatureCollectionContext;
import com.hitorro.features.collections.mapping.intf.ByteScoreMappingFunction;
import com.hitorro.features.io.ByteIO;

/**
 *
 */
public class DenseByteFC extends DenseFeatureCollection<ByteScoreMappingFunction, ByteIO> {
    private byte[] arr;
    private byte nullValue;

    public DenseByteFC() {

    }

    public void setSize(int size) {
        arr = new byte[size];
        nullValue = (Byte) feature.getNullValue();
        for (int i = 0; i < size; i++) {
            arr[i] = nullValue;
        }
    }

    @Override
    public void addRow(final int index, final ByteIO value) {
        arr[index] = value.getByte();
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
        byte b = getByte(index, ids, fcc);

        return scoreFunction.score(b, fcc);
    }

    @Override
    public byte getByte(int index, long ids[], FeatureCollectionContext fcc) {
        return arr[index];
    }
}
