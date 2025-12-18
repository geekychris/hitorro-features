package com.hitorro.features.collections.concrete;


import gnu.trove.impl.hash.TPrimitiveHash;
import com.hitorro.features.RealmId;
import com.hitorro.features.collections.BaseCollection;
import com.hitorro.features.collections.CollectionType;
import com.hitorro.features.collections.FeatureCollectionContext;
import com.hitorro.features.collections.mapping.intf.ScoreMappingFunction;
import com.hitorro.features.io.BaseIO;
import com.hitorro.features.io.MappingFunction;

/**
 * All features that store their data in a datastructure that is not sequentially packed such as an array can use a
 * sparse collection
 */
public abstract class SparseFeatureCollection<S extends ScoreMappingFunction, B extends BaseIO, H extends TPrimitiveHash>
        extends BaseCollection<S> {
    protected H map;
    private MappingFunction mapFunction;

    public SparseFeatureCollection() {
        collectionType = CollectionType.Sparse;
    }

    /**
     * Add a row from
     *
     * @param key
     * @param value
     */
    public abstract void addRow(long key, B value);


    /**
     * Default implementation of
     *
     * @param ids
     * @return
     */
    public abstract RealmId exists(int index, long ids[], FeatureCollectionContext fcc);
}
