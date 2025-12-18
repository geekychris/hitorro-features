package com.hitorro.features.collections.concrete;


import gnu.trove.map.hash.TLongByteHashMap;
import com.hitorro.features.RealmId;
import com.hitorro.features.collections.BaseCollection;
import com.hitorro.features.collections.FeatureCollectionContext;
import com.hitorro.features.collections.mapping.LuceneSimilarityByteScoreMappingFunction;
import com.hitorro.features.collections.mapping.intf.ByteScoreMappingFunction;
import com.hitorro.features.io.ByteIO;

/**
 * Sparse byte collection.  By default this function uses the lucene similarity function
 */
public class SparseByteFC extends SparseFeatureCollection<ByteScoreMappingFunction, ByteIO, TLongByteHashMap> {
    public SparseByteFC() {
        super();
        map = new TLongByteHashMap();
        scoreFunction = new LuceneSimilarityByteScoreMappingFunction();
    }

    public BaseCollection cloneMe() {
        return new SparseByteFC();
    }

    public void addRow(final long key, final ByteIO value) {
        map.put(key, value.getByte());
    }

    public RealmId exists(int index, long ids[], FeatureCollectionContext fcc) {
        if (map.contains(ids[this.idIndex])) {
            return realmId;
        }
        return null;
    }

    public float score(int index, long ids[], FeatureCollectionContext fcc) {
        byte b = getByte(index, ids, fcc);

        return scoreFunction.score(b, fcc);
    }

    public byte getByte(int index, long ids[], FeatureCollectionContext fcc) {
        return map.get(ids[idIndex]);
    }


}
