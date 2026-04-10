/*
 * Copyright (c) 2006-2025 Chris Collins
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.hitorro.features.collections.concrete;

import com.hitorro.features.collections.BaseCollection;
import com.hitorro.features.collections.FeatureCollectionContext;
import com.hitorro.features.collections.mapping.intf.ByteScoreMappingFunction;
import com.hitorro.features.io.ByteIO;


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
