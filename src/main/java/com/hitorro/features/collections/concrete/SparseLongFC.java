package com.hitorro.features.collections.concrete;


import gnu.trove.map.hash.TLongLongHashMap;
import com.hitorro.features.RealmId;
import com.hitorro.features.collections.BaseCollection;
import com.hitorro.features.collections.FeatureCollectionContext;
import com.hitorro.features.collections.mapping.intf.LongScoreMappingFunction;
import com.hitorro.features.collections.mapping.noop.NoOpLongScoreMappingFunction;
import com.hitorro.features.io.LongIO;

/**
 *
 */
public class SparseLongFC extends SparseFeatureCollection<LongScoreMappingFunction, LongIO, TLongLongHashMap> {
    public SparseLongFC() {
        super();
        map = new TLongLongHashMap();
        scoreFunction = new NoOpLongScoreMappingFunction();
    }

    public BaseCollection cloneMe() {
        return new SparseLongFC();
    }

    public void addRow(final long key, final LongIO value) {
        map.put(key, value.getLong());
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

    public long getLong(int index, long ids[], FeatureCollectionContext fcc) {
        return map.get(ids[idIndex]);
    }
}