package com.hitorro.features.collections.concrete;

import com.hitorro.features.RealmId;
import com.hitorro.features.collections.BaseCollection;
import com.hitorro.features.collections.CollectionType;
import com.hitorro.features.collections.FeatureCollectionContext;
import com.hitorro.features.collections.mapping.intf.ScoreMappingFunction;
import com.hitorro.features.io.BaseIO;
import com.hitorro.features.io.MappingFunction;

/**
 *
 */
public abstract class DenseFeatureCollection<S extends ScoreMappingFunction, B extends BaseIO>
        extends BaseCollection<S> {
    private MappingFunction mapFunction;

    public DenseFeatureCollection() {
        collectionType = CollectionType.Dense;
    }

    public abstract void setSize(int size);

    /**
     * Add a row from
     *
     * @param index
     * @param value
     */
    public abstract void addRow(int index, B value);


    /**
     * Default implementation of
     *
     * @param ids
     * @return
     */
    public RealmId exists(int index, long ids[], FeatureCollectionContext fcc) {
        if (contains(index)) {
            return realmId;
        }
        return null;
    }

    public abstract boolean contains(int ind);
}
