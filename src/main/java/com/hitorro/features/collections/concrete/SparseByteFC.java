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


import gnu.trove.map.hash.TLongByteHashMap;
import com.hitorro.features.RealmId;
import com.hitorro.features.collections.BaseCollection;
import com.hitorro.features.collections.FeatureCollectionContext;
import com.hitorro.features.collections.mapping.LuceneSimilarityByteScoreMappingFunction;
import com.hitorro.features.collections.mapping.intf.ByteScoreMappingFunction;
import com.hitorro.features.io.ByteIO;

/**
 * Sparse byte collection.  By default this function uses the lucene similarity function
 */
public class SparseByteFC extends SparseFeatureCollection<ByteScoreMappingFunction, ByteIO, TLongByteHashMap> {
    public SparseByteFC() {
        super();
        map = new TLongByteHashMap();
        scoreFunction = new LuceneSimilarityByteScoreMappingFunction();
    }

    public BaseCollection cloneMe() {
        return new SparseByteFC();
    }

    public void addRow(final long key, final ByteIO value) {
        map.put(key, value.getByte());
    }

    public RealmId exists(int index, long ids[], FeatureCollectionContext fcc) {
        if (map.contains(ids[this.idIndex])) {
            return realmId;
        }
        return null;
    }

    public float score(int index, long ids[], FeatureCollectionContext fcc) {
        byte b = getByte(index, ids, fcc);

        return scoreFunction.score(b, fcc);
    }

    public byte getByte(int index, long ids[], FeatureCollectionContext fcc) {
        return map.get(ids[idIndex]);
    }


}
