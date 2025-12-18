package com.hitorro.features.collections.concrete;

import gnu.trove.map.hash.TLongObjectHashMap;
import com.hitorro.features.RealmId;
import com.hitorro.features.collections.BaseCollection;
import com.hitorro.features.collections.FeatureCollectionContext;
import com.hitorro.features.collections.mapping.intf.StringScoreMappingFunction;
import com.hitorro.features.collections.mapping.noop.NoOpStringScoreMappingFunction;
import com.hitorro.features.io.StringIO;

/**
 *
 */
public class SparseStringFC extends SparseFeatureCollection<StringScoreMappingFunction, StringIO, TLongObjectHashMap<String>> {

    public SparseStringFC() {
        super();
        map = new TLongObjectHashMap();
        scoreFunction = new NoOpStringScoreMappingFunction();
    }

    public void addRow(final long key, final StringIO value) {
        map.put(key, value.getString());
    }

    public float score(int index, long ids[], FeatureCollectionContext fcc) {
        String s = getString(index, ids, fcc);

        return scoreFunction.score(s, fcc);
    }

    public RealmId exists(int index, long ids[], FeatureCollectionContext fcc) {
        if (map.contains(ids[this.idIndex])) {
            return realmId;
        }
        return null;
    }

    public BaseCollection cloneMe() {
        return new SparseStringFC();
    }

    public String getString(int index, long ids[], FeatureCollectionContext fcc) {
        return map.get(ids[idIndex]);
    }
}

