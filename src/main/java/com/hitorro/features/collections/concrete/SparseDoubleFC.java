package com.hitorro.features.collections.concrete;

import gnu.trove.map.hash.TLongDoubleHashMap;
import com.hitorro.features.RealmId;
import com.hitorro.features.collections.BaseCollection;
import com.hitorro.features.collections.FeatureCollectionContext;
import com.hitorro.features.collections.mapping.intf.DoubleScoreMappingFunction;
import com.hitorro.features.collections.mapping.noop.NoOpDoubleScoreMappingFunction;
import com.hitorro.features.io.DoubleIO;

/**
 *
 */
public class SparseDoubleFC extends SparseFeatureCollection<DoubleScoreMappingFunction, DoubleIO, TLongDoubleHashMap> {
    public SparseDoubleFC() {
        super();
        map = new TLongDoubleHashMap();
        scoreFunction = new NoOpDoubleScoreMappingFunction();
    }

    public BaseCollection cloneMe() {
        return new SparseDoubleFC();
    }

    public void addRow(final long key, final DoubleIO value) {
        map.put(key, value.getDouble());
    }

    public float score(int index, long ids[], FeatureCollectionContext fcc) {
        double f = getDouble(index, ids, fcc);

        return scoreFunction.score(f, fcc);
    }

    public RealmId exists(int index, long ids[], FeatureCollectionContext fcc) {
        if (map.contains(ids[this.idIndex])) {
            return realmId;
        }
        return null;
    }

    public double getDouble(int index, long ids[], FeatureCollectionContext fcc) {
        return map.get(ids[idIndex]);
    }
}
