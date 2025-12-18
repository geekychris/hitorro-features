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

import com.hitorro.features.collections.concrete.DenseByteFC;
import com.hitorro.features.collections.concrete.DenseFeatureCollection;
import com.hitorro.features.collections.concrete.SparseByteFC;
import com.hitorro.features.collections.concrete.SparseDoubleFC;
import com.hitorro.features.collections.concrete.SparseFeatureCollection;
import com.hitorro.features.collections.concrete.SparseFloatFC;
import com.hitorro.features.collections.concrete.SparseIntFC;
import com.hitorro.features.collections.concrete.SparseLongFC;
import com.hitorro.features.collections.concrete.SparseShortFC;
import com.hitorro.features.collections.concrete.SparseStringFC;
import com.hitorro.features.io.BaseIO;
import com.hitorro.features.io.ByteIO;
import com.hitorro.features.io.DoubleIO;
import com.hitorro.features.io.FloatIO;
import com.hitorro.features.io.IntegerIO;
import com.hitorro.features.io.LongIO;
import com.hitorro.features.io.ShortIO;
import com.hitorro.features.io.StringIO;
import com.hitorro.features.collections.concrete.*;
import com.hitorro.util.core.EnumContext;
import com.hitorro.util.core.iterator.mappers.BaseMapper;
import com.hitorro.util.core.iterator.mappers.BaseMappers;
import com.hitorro.util.core.string.StringUtil;

/**
 * Define the data types used by the collection machinery and associates ancillary classes associated with each type.
 */
public enum DataType {
    byteType(0, "byte", 1, Byte.class, new ByteIO(), new SparseByteFC(), new DenseByteFC(), BaseMappers.S2B) {
        public Object getNullValue(String s) {
            if (StringUtil.nullOrEmptyString(s)) {
                return Byte.MIN_VALUE;
            }
            return Byte.parseByte(s);
        }
    },
    shortType(1, "short", 2, Short.class, new ShortIO(), new SparseShortFC(), new DenseByteFC(), BaseMappers.S2S) {
        public Object getNullValue(String s) {
            if (StringUtil.nullOrEmptyString(s)) {
                return Short.MIN_VALUE;
            }
            return Short.parseShort(s);
        }
    },
    longType(2, "long", 8, Long.class, new LongIO(), new SparseLongFC(), new DenseByteFC(), BaseMappers.S2L) {
        public Object getNullValue(String s) {
            if (StringUtil.nullOrEmptyString(s)) {
                return Long.MIN_VALUE;
            }
            return Long.parseLong(s);
        }
    },
    integerType(3, "int", 4, Integer.class, new IntegerIO(), new SparseIntFC(), new DenseByteFC(), BaseMappers.S2I) {
        public Object getNullValue(String s) {
            if (StringUtil.nullOrEmptyString(s)) {
                return Integer.MIN_VALUE;
            }
            return Integer.parseInt(s);
        }
    },
    floatType(4, "float", 4, Float.class, new FloatIO(), new SparseFloatFC(), new DenseByteFC(), BaseMappers.S2F) {
        public Object getNullValue(String s) {
            if (StringUtil.nullOrEmptyString(s)) {
                return Float.MIN_VALUE;
            }
            return Float.parseFloat(s);
        }
    },
    doubleType(5, "double", 8, Double.class, new DoubleIO(), new SparseDoubleFC(), new DenseByteFC(), BaseMappers.S2D) {
        public Object getNullValue(String s) {
            if (StringUtil.nullOrEmptyString(s)) {
                return Double.MIN_VALUE;
            }
            return Double.parseDouble(s);
        }
    },
    stringType(6, "string", -1, String.class, new StringIO(), new SparseStringFC(), new DenseByteFC(), BaseMappers.S2String) {
        public Object getNullValue(String s) {
            return s;
        }
    };

    public static EnumContext<DataType> context;
    private String name;
    private int width;
    private Class classType;
    private BaseIO io;
    private SparseFeatureCollection bcSparse;
    private DenseFeatureCollection dense;
    private int ord;
    private BaseMapper fromString;
    DataType(int ord, String name, int width, Class classType, BaseIO io, SparseFeatureCollection bcSparse, DenseFeatureCollection dense, BaseMapper fromString) {
        this.ord = ord;
        this.name = name;
        this.width = width;
        this.classType = classType;
        this.io = io;
        this.bcSparse = bcSparse;
        this.dense = dense;
        this.fromString = fromString;

        setMapEntry(this);
    }

    private static void setMapEntry(DataType filter) {
        if (context == null) {
            context = new EnumContext<DataType>("datatype");
        }

        context.setNames(filter, filter.getName(), filter.getStableOrdinal());
    }

    public abstract Object getNullValue(String s);

    public int getWidth() {
        return width;
    }

    public String getName() {
        return name;
    }

    public Class getClassType() {
        return classType;
    }

    public BaseIO getCopyOfIO() {
        return io.cloneIO();
    }

    public SparseFeatureCollection getCopyOfSparseCollection() {
        return (SparseFeatureCollection) bcSparse.cloneMe();
    }

    public DenseFeatureCollection getCopyOfSDenseCollection() {
        return (DenseFeatureCollection) dense.cloneMe();
    }

    /**
     * BaseMapper that converts from a string representation to a boxed typed value.
     *
     * @return
     */
    public BaseMapper getFromStringMapper() {
        return fromString;
    }

    public int getStableOrdinal() {
        return ord;
    }
}
