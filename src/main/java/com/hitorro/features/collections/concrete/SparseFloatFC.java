package com.hitorro.features.collections.concrete;

import gnu.trove.map.hash.TLongFloatHashMap;
import com.hitorro.features.RealmId;
import com.hitorro.features.collections.BaseCollection;
import com.hitorro.features.collections.FeatureCollectionContext;
import com.hitorro.features.collections.mapping.intf.FloatScoreMappingFunction;
import com.hitorro.features.collections.mapping.noop.NoOpFloatScoreMappingFunction;
import com.hitorro.features.io.FloatIO;

/**
 *
 */
public class SparseFloatFC extends SparseFeatureCollection<FloatScoreMappingFunction, FloatIO, TLongFloatHashMap> {

    public SparseFloatFC() {
        super();
        map = new TLongFloatHashMap();
        scoreFunction = new NoOpFloatScoreMappingFunction();
    }

    public BaseCollection cloneMe() {
        return new SparseFloatFC();
    }

    public void addRow(final long key, final FloatIO value) {
        map.put(key, value.getFloat());
    }

    public RealmId exists(int index, long ids[], FeatureCollectionContext fcc) {
        if (map.contains(ids[this.idIndex])) {
            return realmId;
        }
        return null;
    }

    public float score(int index, long ids[], FeatureCollectionContext fcc) {
        float f = getFloat(index, ids, fcc);

        return scoreFunction.score(f, fcc);
    }

    public float getFloat(int index, long ids[], FeatureCollectionContext fcc) {
        return map.get(ids[idIndex]);
    }
}

