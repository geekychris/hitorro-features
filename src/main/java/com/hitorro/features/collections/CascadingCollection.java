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
package com.hitorro.features.collections;

import com.hitorro.features.RealmId;

import java.util.List;

/**
 * User: chris
 */
public class CascadingCollection extends BaseCollection {
    BaseCollection bcArray[];

    public CascadingCollection(List<BaseCollection> collections) {
        bcArray = new BaseCollection[collections.size()];
        for (int i = 0; i < collections.size(); i++) {
            bcArray[i] = collections.get(i);
        }
    }

    public float score(int index, final long[] ids, final FeatureCollectionContext fcc) {
        BaseCollection bc = getFirstWithID(index, ids, fcc);
        if (bc == null) {
            return Float.MIN_VALUE;
        }
        return bc.score(index, ids, fcc);
    }

    public byte getByte(int index, long ids[], FeatureCollectionContext fcc) {
        BaseCollection bc = getFirstWithID(index, ids, fcc);
        if (bc == null) {
            return Byte.MIN_VALUE;
        }
        return bc.getByte(index, ids, fcc);
    }

    public short getShort(int index, long ids[], FeatureCollectionContext fcc) {
        BaseCollection bc = getFirstWithID(index, ids, fcc);
        if (bc == null) {
            return Short.MIN_VALUE;
        }
        return bc.getShort(index, ids, fcc);
    }

    public long getLong(int index, long ids[], FeatureCollectionContext fcc) {
        BaseCollection bc = getFirstWithID(index, ids, fcc);
        if (bc == null) {
            return Long.MIN_VALUE;
        }
        return bc.getLong(index, ids, fcc);
    }

    public int getInt(int index, long ids[], FeatureCollectionContext fcc) {
        BaseCollection bc = getFirstWithID(index, ids, fcc);
        if (bc == null) {
            return Integer.MIN_VALUE;
        }
        return bc.getInt(index, ids, fcc);
    }

    public double getDouble(int index, long ids[], FeatureCollectionContext fcc) {
        BaseCollection bc = getFirstWithID(index, ids, fcc);
        if (bc == null) {
            return Double.MIN_VALUE;
        }
        return bc.getDouble(index, ids, fcc);
    }


    public BaseCollection cloneMe() {
        return null;
    }

    public RealmId exists(int index, final long[] ids, final FeatureCollectionContext fcc) {
        BaseCollection bc = getFirstWithID(index, ids, fcc);
        if (bc == null) {
            return null;
        }
        return bc.realmId;
    }

    private BaseCollection getFirstWithID(int index, final long[] ids, final FeatureCollectionContext fcc) {
        for (BaseCollection bc : bcArray) {
            if (bc.exists(index, ids, fcc) != null) {
                return bc;
            }
        }
        return null;
    }
}
