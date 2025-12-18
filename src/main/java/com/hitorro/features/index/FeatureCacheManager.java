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
