package com.hitorro.features.collections.concrete;

import gnu.trove.map.hash.TLongIntHashMap;
import com.hitorro.features.RealmId;
import com.hitorro.features.collections.BaseCollection;
import com.hitorro.features.collections.FeatureCollectionContext;
import com.hitorro.features.collections.mapping.intf.IntegerScoreMappingFunction;
import com.hitorro.features.collections.mapping.noop.NoOpIntegerScoreMappingFunction;
import com.hitorro.features.io.IntegerIO;

/**
 *
 */
public class SparseIntFC extends SparseFeatureCollection<IntegerScoreMappingFunction, IntegerIO, TLongIntHashMap> {


    public SparseIntFC() {
        super();
        map = new TLongIntHashMap();
        scoreFunction = new NoOpIntegerScoreMappingFunction();
    }

    public BaseCollection cloneMe() {
        return new SparseIntFC();
    }

    public void addRow(final long key, final IntegerIO value) {
        map.put(key, value.getInteger());
    }


    public RealmId exists(int index, long ids[], FeatureCollectionContext fcc) {
        if (map.contains(ids[this.idIndex])) {
            return realmId;
        }
        return null;
    }

    public float score(int index, long ids[], FeatureCollectionContext fcc) {
        short f = getShort(index, ids, fcc);

        return scoreFunction.score(f, fcc);
    }

    public int getInteger(int index, long ids[], FeatureCollectionContext fcc) {
        return map.get(ids[idIndex]);
    }
}