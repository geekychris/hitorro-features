package com.hitorro.features;

import com.hitorro.util.core.ByteUtil;
import com.hitorro.util.core.string.Fmt;
import com.hitorro.util.io.StoreException;
import com.hitorro.util.io.largedata.compressedstreams.CInputStream;
import com.hitorro.util.io.largedata.compressedstreams.COutputStream;
import com.hitorro.util.typesystem.HTObjectInputStream;
import com.hitorro.util.typesystem.HTObjectOutputStream;
import com.hitorro.util.typesystem.HTSerializable;
import com.hitorro.util.typesystem.annotation.TypeClassMetaInfo;

import java.io.IOException;

/**
 * User: chris
 */

@TypeClassMetaInfo(shortTypeName = TypeClassMetaInfo.FeatureSetKey,
        isView = false,
        isPersisted = false,
        schemaVersion = FeatureValue.SerialVersion)

public class FeatureSetKey implements HTSerializable {
    public static final int SerialVersion = 1;
    protected transient Feature feature;
    protected int partition = -1;
    protected long featureHash;
    protected short ioCode;
    protected RealmId realm = RealmId.None;
    protected int index;
    protected String variable;
    protected String name = null;
    protected int hash;

    public static FeatureSetKey getFSKFromIS(CInputStream is) throws IOException {
        //fh p ioc realm <type>
        FeatureSetKey fsk = new FeatureSetKey();
        fsk.featureHash = is.readLong();
        fsk.feature = FeatureEngine.getEngine().getFeature(fsk.featureHash);
        fsk.partition = is.readInt();
        fsk.ioCode = is.readShort();
        fsk.realm = RealmId.getFromOOS(is);

        FeatureCardinality fc = FeatureCardinality.context.getByOrdinal(ByteUtil.getLowOfShortAsInt(fsk.ioCode));
        switch (fc) {
            case index:
                fsk.index = is.readInt();
                break;
            case named:
                fsk.variable = is.readString();
                break;
            default:
                break;
        }
        return fsk;
    }

    public static FeatureSetKey getFeatureAux(Feature feature, RealmId realmid, int partition) {
        return getFeatureKeyAux(feature, realmid, partition, null, -1);
    }

    public static FeatureSetKey getFeatureAux(Feature feature, RealmId realmid, int partition, String variable) {
        return getFeatureKeyAux(feature, realmid, partition, variable, -1);
    }

    public static FeatureSetKey getFeatureAux(Feature feature, RealmId realmid, int partition, int index) {
        return getFeatureKeyAux(feature, realmid, partition, null, index);
    }

    public static FeatureSetKey getFeatureKeyAux(Feature feature, RealmId realmid, int partition, String variable, int index) {
        FeatureSetKey fv = new FeatureSetKey();
        fv.realm = realmid;
        fv.featureHash = feature.getFeatureHash();
        fv.feature = FeatureEngine.getEngine().getFeature(fv.featureHash);

        fv.ioCode = fv.computeIOCode();
        fv.partition = partition;
        fv.variable = variable;
        fv.index = index;
        return fv;
    }

    public String toString() {
        if (name == null) {
            switch (feature.getFeatureCardinality()) {
                case index:
                    name = Fmt.S("%s.%s.[%s].(%s-%s)", feature.getName(), index, partition, realm.realm, realm.id);
                    break;
                case named:
                    name = Fmt.S("%s.%s.[%s].(%s-%s)", feature.getName(), variable, partition, realm.realm, realm.id);
                    break;
                default:
                    name = Fmt.S("%s.[%s].(%s-%s)", feature.getName(), partition, realm.realm, realm.id);
                    break;
            }

        }
        hash = name.hashCode();
        return name;
    }

    public boolean equals(Object o) {
        return toString().equals(o.toString());
    }

    public int hashCode() {
        return toString().hashCode();
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public RealmId getRealmId() {
        return this.realm;
    }

    public void setRealmId(RealmId rid) {
        this.realm = rid;
    }

    public void writeToOS(COutputStream os) throws IOException {
        os.writeLong(featureHash);
        os.writeInt(partition);
        os.writeShort(ioCode);
        realm.writeToOOS(os);

        switch (feature.getFeatureCardinality()) {
            case index:
                os.writeInt(index);
                break;
            case named:
                os.writeString(variable);
                break;
            default:
                break;
        }
    }

    public FeatureSetKey getParentKey() {
        FeatureSetKey key = getKey();
        if (key.feature.isDerived()) {
            key.setFeature(key.feature.getRootFeature());

        }
        return key;
    }

    public FeatureSetKey getKey() {
        FeatureSetKey key = new FeatureSetKey();
        key.feature = feature;
        key.featureHash = featureHash;
        key.realm = realm;
        key.partition = partition;
        key.variable = variable;
        key.index = index;
        key.ioCode = ioCode;
        key.name = name;
        return key;
    }

    protected short computeIOCode() {
        int type = feature.getDataType().getStableOrdinal();
        int card = feature.getFeatureCardinality().getStableOrdinal();
        return ByteUtil.combineTwoIntsInShort(type, card);
    }

    public int getParition() {
        return partition;
    }

    /**
     * Features can be partitioned, this is a simple ID, doesnt mean anything other than to the higher level logic that
     * chooses which partition to reside in.  This is typically married with the partitioning scheme of any other data
     * structures that dont have to be global to the system, such as text index or link index.
     * <p/>
     * Note that this could be computed from some kind of official chronicle publish time if it is a temporal based
     * partitioning.  Could be based on geo location, document type, company, realm..
     *
     * @param partition
     */
    public void setPartition(int partition) {
        this.partition = partition;
    }

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature f) {
        feature = f;
        featureHash = f.getFeatureHash();
        name = null;
    }

    /**
     * @param os
     * @throws java.io.IOException
     * @throws com.hitorro.util.io.StoreException
     */

    public void serialize(HTObjectOutputStream os) throws IOException, StoreException {
        os.writeInt(this.getSerializationVersion());
        os.writeInt(partition);
        os.writeLong(featureHash);
        os.writeShort(ioCode);
        // we want a self contained reference so id tracking can be disabled
        realm.writeToHTOIS(os);

        switch (feature.getFeatureCardinality()) {
            case index:
                os.writeInt(index);
                break;
            case named:
                os.writeString(variable);
                break;
            default:
                break;
        }
    }

    public void deserialize(HTObjectInputStream os) throws IOException, ClassNotFoundException, StoreException {
        int version = os.readInt();
        partition = os.readInt();
        featureHash = os.readLong();
        feature = FeatureEngine.getEngine().getFeature(featureHash);
        ioCode = os.readShort();
        realm = RealmId.getFromHTOIS(os);
        feature = FeatureEngine.getEngine().getFeature(featureHash);

        FeatureCardinality fc = FeatureCardinality.context.getByOrdinal(ByteUtil.getLowOfShortAsInt(ioCode));
        switch (fc) {
            case index:
                index = os.readInt();
                break;
            case named:
                variable = os.readString();
                break;
            default:
                break;
        }
    }

    public int getSerializationVersion() {
        return SerialVersion;
    }

    public boolean isPersisted() {
        return false;
    }

    public boolean hasGuid() {
        return false;
    }

    public boolean hasSoftGuid() {
        return false;
    }
}
