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


import gnu.trove.impl.hash.TPrimitiveHash;
import com.hitorro.features.RealmId;
import com.hitorro.features.collections.BaseCollection;
import com.hitorro.features.collections.CollectionType;
import com.hitorro.features.collections.FeatureCollectionContext;
import com.hitorro.features.collections.mapping.intf.ScoreMappingFunction;
import com.hitorro.features.io.BaseIO;
import com.hitorro.features.io.MappingFunction;

/**
 * All features that store their data in a datastructure that is not sequentially packed such as an array can use a
 * sparse collection
 */
public abstract class SparseFeatureCollection<S extends ScoreMappingFunction, B extends BaseIO, H extends TPrimitiveHash>
        extends BaseCollection<S> {
    protected H map;
    private MappingFunction mapFunction;

    public SparseFeatureCollection() {
        collectionType = CollectionType.Sparse;
    }

    /**
     * Add a row from
     *
     * @param key
     * @param value
     */
    public abstract void addRow(long key, B value);


    /**
     * Default implementation of
     *
     * @param ids
     * @return
     */
    public abstract RealmId exists(int index, long ids[], FeatureCollectionContext fcc);
}
