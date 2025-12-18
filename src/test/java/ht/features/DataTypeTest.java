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
package ht.features;

import com.hitorro.features.DataType;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for DataType enum
 */
public class DataTypeTest {

    @Test
    public void testDataTypeEnumValues() {
        assertEquals(7, DataType.values().length);
        assertNotNull(DataType.byteType);
        assertNotNull(DataType.shortType);
        assertNotNull(DataType.longType);
        assertNotNull(DataType.integerType);
        assertNotNull(DataType.floatType);
        assertNotNull(DataType.doubleType);
        assertNotNull(DataType.stringType);
    }

    @Test
    public void testDataTypeNames() {
        assertEquals("byte", DataType.byteType.getName());
        assertEquals("short", DataType.shortType.getName());
        assertEquals("long", DataType.longType.getName());
        assertEquals("int", DataType.integerType.getName());
        assertEquals("float", DataType.floatType.getName());
        assertEquals("double", DataType.doubleType.getName());
        assertEquals("string", DataType.stringType.getName());
    }

    @Test
    public void testDataTypeWidths() {
        assertEquals(1, DataType.byteType.getWidth());
        assertEquals(2, DataType.shortType.getWidth());
        assertEquals(8, DataType.longType.getWidth());
        assertEquals(4, DataType.integerType.getWidth());
        assertEquals(4, DataType.floatType.getWidth());
        assertEquals(8, DataType.doubleType.getWidth());
        assertEquals(-1, DataType.stringType.getWidth());
    }

    @Test
    public void testDataTypeClassTypes() {
        assertEquals(Byte.class, DataType.byteType.getClassType());
        assertEquals(Short.class, DataType.shortType.getClassType());
        assertEquals(Long.class, DataType.longType.getClassType());
        assertEquals(Integer.class, DataType.integerType.getClassType());
        assertEquals(Float.class, DataType.floatType.getClassType());
        assertEquals(Double.class, DataType.doubleType.getClassType());
        assertEquals(String.class, DataType.stringType.getClassType());
    }

    @Test
    public void testDataTypeStableOrdinals() {
        assertEquals(0, DataType.byteType.getStableOrdinal());
        assertEquals(1, DataType.shortType.getStableOrdinal());
        assertEquals(2, DataType.longType.getStableOrdinal());
        assertEquals(3, DataType.integerType.getStableOrdinal());
        assertEquals(4, DataType.floatType.getStableOrdinal());
        assertEquals(5, DataType.doubleType.getStableOrdinal());
        assertEquals(6, DataType.stringType.getStableOrdinal());
    }

    @Test
    public void testGetNullValueWithNullString() {
        assertEquals(Byte.MIN_VALUE, DataType.byteType.getNullValue(null));
        assertEquals(Short.MIN_VALUE, DataType.shortType.getNullValue(null));
        assertEquals(Long.MIN_VALUE, DataType.longType.getNullValue(null));
        assertEquals(Integer.MIN_VALUE, DataType.integerType.getNullValue(null));
        assertEquals(Float.MIN_VALUE, DataType.floatType.getNullValue(null));
        assertEquals(Double.MIN_VALUE, DataType.doubleType.getNullValue(null));
        assertNull(DataType.stringType.getNullValue(null));
    }

    @Test
    public void testGetNullValueWithEmptyString() {
        assertEquals(Byte.MIN_VALUE, DataType.byteType.getNullValue(""));
        assertEquals(Short.MIN_VALUE, DataType.shortType.getNullValue(""));
        assertEquals(Long.MIN_VALUE, DataType.longType.getNullValue(""));
        assertEquals(Integer.MIN_VALUE, DataType.integerType.getNullValue(""));
        assertEquals(Float.MIN_VALUE, DataType.floatType.getNullValue(""));
        assertEquals(Double.MIN_VALUE, DataType.doubleType.getNullValue(""));
        assertEquals("", DataType.stringType.getNullValue(""));
    }

    @Test
    public void testGetNullValueWithParsableString() {
        assertEquals(Byte.valueOf((byte) 10), DataType.byteType.getNullValue("10"));
        assertEquals(Short.valueOf((short) 100), DataType.shortType.getNullValue("100"));
        assertEquals(Long.valueOf(1000L), DataType.longType.getNullValue("1000"));
        assertEquals(Integer.valueOf(500), DataType.integerType.getNullValue("500"));
        assertEquals(Float.valueOf(3.14f), DataType.floatType.getNullValue("3.14"));
        assertEquals(Double.valueOf(2.718), DataType.doubleType.getNullValue("2.718"));
        assertEquals("test", DataType.stringType.getNullValue("test"));
    }

    @Test
    public void testGetCopyOfIO() {
        assertNotNull(DataType.byteType.getCopyOfIO());
        assertNotNull(DataType.shortType.getCopyOfIO());
        assertNotNull(DataType.longType.getCopyOfIO());
        assertNotNull(DataType.integerType.getCopyOfIO());
        assertNotNull(DataType.floatType.getCopyOfIO());
        assertNotNull(DataType.doubleType.getCopyOfIO());
        assertNotNull(DataType.stringType.getCopyOfIO());
    }

    @Test
    public void testGetCopyOfSparseCollection() {
        assertNotNull(DataType.byteType.getCopyOfSparseCollection());
        assertNotNull(DataType.shortType.getCopyOfSparseCollection());
        assertNotNull(DataType.longType.getCopyOfSparseCollection());
        assertNotNull(DataType.integerType.getCopyOfSparseCollection());
        assertNotNull(DataType.floatType.getCopyOfSparseCollection());
        assertNotNull(DataType.doubleType.getCopyOfSparseCollection());
        assertNotNull(DataType.stringType.getCopyOfSparseCollection());
    }

    @Test
    public void testGetCopyOfDenseCollection() {
        assertNotNull(DataType.byteType.getCopyOfSDenseCollection());
        assertNotNull(DataType.shortType.getCopyOfSDenseCollection());
        assertNotNull(DataType.longType.getCopyOfSDenseCollection());
        assertNotNull(DataType.integerType.getCopyOfSDenseCollection());
        assertNotNull(DataType.floatType.getCopyOfSDenseCollection());
        assertNotNull(DataType.doubleType.getCopyOfSDenseCollection());
        assertNotNull(DataType.stringType.getCopyOfSDenseCollection());
    }

    @Test
    public void testGetFromStringMapper() {
        assertNotNull(DataType.byteType.getFromStringMapper());
        assertNotNull(DataType.shortType.getFromStringMapper());
        assertNotNull(DataType.longType.getFromStringMapper());
        assertNotNull(DataType.integerType.getFromStringMapper());
        assertNotNull(DataType.floatType.getFromStringMapper());
        assertNotNull(DataType.doubleType.getFromStringMapper());
        assertNotNull(DataType.stringType.getFromStringMapper());
    }

    @Test
    public void testEnumContext() {
        assertNotNull(DataType.context);
        assertEquals("datatype", DataType.context.getName());
        
        // Test that context can retrieve types by ordinal
        assertEquals(DataType.byteType, DataType.context.getByOrdinal(0));
        assertEquals(DataType.shortType, DataType.context.getByOrdinal(1));
        assertEquals(DataType.longType, DataType.context.getByOrdinal(2));
        assertEquals(DataType.integerType, DataType.context.getByOrdinal(3));
        assertEquals(DataType.floatType, DataType.context.getByOrdinal(4));
        assertEquals(DataType.doubleType, DataType.context.getByOrdinal(5));
        assertEquals(DataType.stringType, DataType.context.getByOrdinal(6));
    }

    @Test(expected = NumberFormatException.class)
    public void testGetNullValueWithInvalidByteString() {
        DataType.byteType.getNullValue("invalid");
    }

    @Test(expected = NumberFormatException.class)
    public void testGetNullValueWithInvalidIntegerString() {
        DataType.integerType.getNullValue("not_a_number");
    }

    @Test(expected = NumberFormatException.class)
    public void testGetNullValueWithInvalidFloatString() {
        DataType.floatType.getNullValue("abc");
    }
}
