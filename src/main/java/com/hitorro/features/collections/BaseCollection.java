package com.hitorro.features.collections;

import com.hitorro.features.DataType;
import com.hitorro.features.Feature;
import com.hitorro.features.RealmId;
import com.hitorro.features.collections.mapping.intf.ScoreMappingFunction;

/**
 * Base implementation of a collection.  This covers both concrete collections and functions.
 */
public abstract class BaseCollection<S extends ScoreMappingFunction> {
    protected int idIndex;
    protected DataType dataType;
    protected CollectionType collectionType;
    protected S scoreFunction;
    protected Feature feature;
    protected RealmId realmId;

    /**
     * Collections MUST set a default function (to simplify the scoring logic, however this function can be replaced at
     * init time to something more custom.
     *
     * @param function
     */
    public void setScoreFunction(S function) {
        scoreFunction = function;
    }

    /**
     * Which id out of an array of ids should be used, and what datatype should we expect. The datatype is more of a way
     * of validating the correct scoring chain.
     *
     * @param idIndex
     * @param dt
     */
    public void setIDIndex(int idIndex, DataType dt, Feature feature, RealmId realmId) {
        this.idIndex = idIndex;
        dataType = dt;
        this.feature = feature;
        this.realmId = realmId;
    }

    /**
     * Score can be provided for
     *
     * @param ids
     * @return
     */
    public abstract float score(int index, long ids[], FeatureCollectionContext fcc);

    public abstract BaseCollection cloneMe();

    /**
     * Default way of displaying the score in
     *
     * @param ids
     * @return
     */
    public String scoreDisplay(int index, long ids[], FeatureCollectionContext fcc) {
        return Float.toString(score(index, ids, fcc));
    }

    public Feature getFeature() {
        return feature;
    }

    public int getIDIndex() {
        return idIndex;
    }

    /**
     * expected type of the collection
     *
     * @return
     */
    public DataType getDataType() {
        return dataType;
    }

    /**
     * Is this a dense, sparse, function type collection.
     *
     * @return
     */
    public CollectionType getCollectionType() {
        return collectionType;
    }

    //***************** Getters that are expected to be overidden by the real deal ***************
    //***************** if you call these on collections that are not of that true type **********
    //***************** millage will vary                                          ***************

    public byte getByte(int index, long ids[], FeatureCollectionContext fcc) {
        return (byte) score(index, ids, fcc);
    }

    public short getShort(int index, long ids[], FeatureCollectionContext fcc) {
        return (short) score(index, ids, fcc);
    }

    public long getLong(int index, long ids[], FeatureCollectionContext fcc) {
        return (long) score(index, ids, fcc);
    }

    public int getInt(int index, long ids[], FeatureCollectionContext fcc) {
        return (int) score(index, ids, fcc);
    }

    public double getDouble(int index, long ids[], FeatureCollectionContext fcc) {
        return (double) score(index, ids, fcc);
    }

    public abstract RealmId exists(int index, long ids[], FeatureCollectionContext fcc);
}
