package com.hitorro.features;

import com.hitorro.util.core.ArrayUtil;
import com.hitorro.util.core.string.Fmt;
import com.hitorro.util.core.string.StringUtil;
import com.hitorro.util.io.largedata.compressedstreams.CInputStream;
import com.hitorro.util.io.largedata.compressedstreams.COutputStream;
import com.hitorro.util.typesystem.HTObjectInputStream;
import com.hitorro.util.typesystem.HTObjectOutputStream;
import com.hitorro.util.typesystem.HTSerializable;
import com.hitorro.util.typesystem.annotation.TypeClassMetaInfo;

import java.io.IOException;

/**
 * Defines a scope to where a feature value is applied
 */
@TypeClassMetaInfo(shortTypeName = TypeClassMetaInfo.RealmId,
        isView = false,
        isPersisted = false,
        schemaVersion = RealmId.SerialVersion)
public class RealmId implements HTSerializable {
    public static final int SerialVersion = 1;
    public static RealmId None = new RealmId(0, 0);
    int realm;
    int id;
    private String strForm = null;


    public RealmId(int realm, int id) {
        this.realm = realm;
        this.id = id;
    }

    public RealmId() {

    }

    public static RealmId getFromHTOIS(HTObjectInputStream os) throws IOException {
        RealmId id = new RealmId();
        id.realm = os.readInt();
        id.id = os.readInt();
        return id;
    }

    /**
     * Assuming the RID is embedded in such things as a featurevalue just squirt out its constituent parts. This could
     * also be called from normal serialization machinery.
     *
     * @param ois
     * @return
     * @throws IOException
     */
    public static RealmId getFromOOS(CInputStream ois) throws IOException {
        RealmId id = new RealmId();
        id.realm = ois.readVInt();
        id.id = ois.readVInt();
        return id;
    }

    public static RealmId fromString(String s) {
        int parts[] = ArrayUtil.getIntArrayFromStringArray(StringUtil.tokenizeFromSingleChar(s, "_"));
        if (parts.length != 2) {
            return null;
        }
        return new RealmId(parts[0], parts[1]);
    }

    public void writeToHTOIS(HTObjectOutputStream os) throws IOException {
        os.writeInt(realm);
        os.writeInt(id);
    }

    public void writeToOOS(COutputStream oos) throws IOException {
        oos.writeVInt(realm);
        oos.writeVInt(id);
    }

    public String toString() {
        return getStringForm();
    }

    public String getStringForm() {
        if (strForm == null) {
            strForm = Fmt.S("%s_%s", realm, id);
        }
        return strForm;
    }

    public int hashcode() {
        return getStringForm().hashCode();
    }

    public boolean equals(Object o) {
        return getStringForm().equals(o);
    }

    public int getRealm() {
        return realm;
    }

    public void setRealm(int realm) {
        this.realm = realm;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void serialize(HTObjectOutputStream os) throws IOException {
        os.writeInt(this.getSerializationVersion());
        os.writeInt(realm);
        os.writeInt(id);
    }

    public void deserialize(HTObjectInputStream os) throws IOException {
        int version = os.readInt();
        realm = os.readInt();
        id = os.readInt();
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
