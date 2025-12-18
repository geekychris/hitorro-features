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
