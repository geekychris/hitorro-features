package com.hitorro.features.collections.concrete;


import gnu.trove.map.hash.TLongShortHashMap;
import com.hitorro.features.RealmId;
import com.hitorro.features.collections.BaseCollection;
import com.hitorro.features.collections.FeatureCollectionContext;
import com.hitorro.features.collections.mapping.intf.ShortScoreMappingFunction;
import com.hitorro.features.collections.mapping.noop.NoOpShortScoreMappingFunction;
import com.hitorro.features.io.ShortIO;

/**
 *
 */
public class SparseShortFC extends SparseFeatureCollection<ShortScoreMappingFunction, ShortIO, TLongShortHashMap> {


    public SparseShortFC() {
        super();
        map = new TLongShortHashMap();
        scoreFunction = new NoOpShortScoreMappingFunction();
    }

    public BaseCollection cloneMe() {
        return new SparseShortFC();
    }

    public void addRow(final long key, final ShortIO value) {
        map.put(key, value.getShort());
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

    public short getShort(int index, long ids[], FeatureCollectionContext fcc) {
        return map.get(ids[idIndex]);
    }
}
