/*
 * Copyright (c) 2006-2025 Chris Collins
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.hitorro.features;

import com.hitorro.util.core.ByteUtil;
import com.hitorro.util.core.string.Fmt;
import com.hitorro.util.io.StoreException;
import com.hitorro.util.typesystem.HTObjectInputStream;
import com.hitorro.util.typesystem.HTObjectOutputStream;
import com.hitorro.util.typesystem.annotation.TypeClassMetaInfo;

import java.io.IOException;

/**
 * Wrapper around a value generated for a feature
 */
@TypeClassMetaInfo(shortTypeName = TypeClassMetaInfo.FeatureValue,
        isView = false,
        isPersisted = false,
        schemaVersion = FeatureValue.SerialVersion)
public class FeatureValue extends FeatureSetKey {

    public static final int SerialVersion = 1;
    long id;
    long time;
    Object value;
    private transient boolean isScratch = false;

    public static FeatureValue getFeature(Feature feature, long id, long time, Number value, int index) {
        FeatureValue fv = getFeatureAux(feature, id, time, value);
        fv.index = index;
        return fv;
    }

    public static FeatureValue getFeature(Feature feature, long id, long time, Number value, String var) {
        FeatureValue fv = getFeatureAux(feature, id, time, value);
        fv.variable = var;
        return fv;
    }

    public static FeatureValue getFeature(Feature feature, long id, long time, String value, int index) {
        FeatureValue fv = getFeatureAux(feature, id, time, value);
        fv.index = index;
        return fv;
    }

    public static FeatureValue getFeature(Feature feature, long id, long time, String value, String var) {
        FeatureValue fv = getFeatureAux(feature, id, time, value);
        fv.variable = var;
        return fv;
    }

    public static FeatureValue getFeature(Feature feature, long id, long time, Number i) {
        return getFeatureAux(feature, id, time, i);
    }

    public static FeatureValue getFeature(Feature feature, long id, long time, String s) {
        return getFeatureAux(feature, id, time, s);
    }

    private static FeatureValue getFeatureAux(Feature feature, long id, long time, Object value) {
        FeatureValue fv = new FeatureValue();
        fv.feature = feature;
        fv.featureHash = feature.getFeatureHash();
        fv.ioCode = fv.computeIOCode();
        fv.value = value;
        fv.id = id;
        fv.time = time;
        return fv;
    }

    public boolean isScratch() {
        return isScratch;
    }

    public String toString() {
        if (name == null) {
            switch (feature.getFeatureCardinality()) {
                case index:
                    name = Fmt.S("%s.%s.[%s].(%s-%s):%s", feature.getName(), index, partition, realm.realm, realm.id, id);
                    break;
                case named:
                    name = Fmt.S("%s.%s.[%s].(%s-%s):%s", feature.getName(), variable, partition, realm.realm, realm.id, id);
                    break;
                default:
                    name = Fmt.S("%s.[%s].(%s-%s):%s", feature.getName(), partition, realm.realm, realm.id, id);
                    break;
            }
        }
        hash = name.hashCode();
        return name;
    }

    public boolean equals(Object o) {
        return toString().equals(o.toString());
    }

    public void setIsScratch(boolean flag) {
        isScratch = flag;
    }

    public boolean getKeysEqual(FeatureSetKey key) {
        if (key == null) {
            return false;
        }
        return key.feature == feature && key.variable == variable && key.index == index &&
                key.realm.id == realm.id && key.realm.realm == realm.realm && key.partition == partition;
    }

    public Object getValue() {
        return value;
    }


    public long getID() {
        return id;
    }

    public long getTime() {
        return time;
    }


    /**
     * @param os
     * @throws IOException
     * @throws StoreException
     */

    public void serialize(HTObjectOutputStream os) throws IOException, StoreException {
        // I L L I L S VO XX XX
        os.writeInt(this.getSerializationVersion());
        super.serialize(os);
        os.writeLong(id);
        os.writeLong(time);

        switch (feature.getDataType()) {
            case byteType:
                os.writeByte(getNumber().byteValue());
                break;
            case shortType:
                os.writeShort(getNumber().shortValue());
                break;

            case integerType:
                os.writeInt(getNumber().intValue());
                break;

            case longType:
                os.writeLong(getNumber().longValue());
                break;

            case floatType:
                os.writeFloat(getNumber().floatValue());
                break;

            case doubleType:
                os.writeDouble(getNumber().doubleValue());
                break;

            case stringType:
                os.writeString((String) value);
                break;
            default:
                break;

        }
    }

    public Number getNumber() {
        return (Number) value;
    }

    public void deserialize(HTObjectInputStream os) throws IOException, ClassNotFoundException, StoreException {
        int version = os.readInt();
        super.deserialize(os);
        id = os.readLong();
        time = os.readLong();

        DataType dt = DataType.context.getByOrdinal(ByteUtil.getHighPartOfShortAsInt(ioCode));
        switch (dt) {
            case byteType:
                value = Byte.valueOf(os.readByte());
                break;
            case shortType:
                value = Short.valueOf(os.readShort());
                break;

            case integerType:
                value = Integer.valueOf(os.readInt());
                break;

            case longType:
                value = Long.valueOf(os.readLong());
                break;

            case floatType:
                value = Float.valueOf(os.readFloat());
                break;

            case doubleType:
                value = Double.valueOf(os.readDouble());
                break;

            case stringType:
                value = os.readString();
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
