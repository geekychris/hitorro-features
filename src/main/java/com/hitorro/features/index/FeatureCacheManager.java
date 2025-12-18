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
package com.hitorro.features.index;

import com.hitorro.features.FeatureSetKey;
import com.hitorro.features.collections.BaseCollection;
import com.hitorro.util.basefile.fs.BaseFile;
import com.hitorro.util.core.map.LRUHashMap;

import java.io.IOException;

/**
 * User: chris
 */
public class FeatureCacheManager {
    FeatureIndexManager fim;
    private BaseFile root;
    private LRUHashMap<FeatureSetKey, BaseCollection> map = new LRUHashMap(1000);
    private Object cacheSync = new Object();

    public FeatureCacheManager(BaseFile dir) throws IOException {
        this.root = dir;
        fim = new FeatureIndexManager(dir, this);
    }

    public FeatureIndexManager getFIM() {
        return fim;
    }

    public FeatureQueryContext getQueryContext() {
        return new FeatureQueryContext(this);
    }

    void reloadCache(FeatureSetKey fsk, boolean onlyIfCached) throws IOException {
        BaseCollection bc = null;
        synchronized (cacheSync) {
            bc = map.get(fsk);
        }
        if (onlyIfCached && bc == null) {
            // we had not cached it yet anyway so we will not replace it.
            return;
        }
        getCollection(fsk);
    }

    /**
     * Naive implementation of the cache
     *
     * @param fsk
     * @return
     * @throws IOException
     */
    public BaseCollection getCollection(FeatureSetKey fsk) throws IOException {
        BaseCollection bc = null;

        synchronized (cacheSync) {
            bc = map.get(fsk);
        }

        if (bc == null) {
            bc = fim.getFeatureCollection(fsk);
            if (bc != null) {
                synchronized (cacheSync) {
                    map.put(fsk, bc);
                }
            }
        }
        return bc;
    }
}

class CacheElement {
    BaseCollection bc;
    long time;
}
