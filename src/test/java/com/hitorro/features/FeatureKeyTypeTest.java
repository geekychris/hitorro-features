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

import com.hitorro.features.FeatureKeyType;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for FeatureKeyType enum
 */
public class FeatureKeyTypeTest {

    @Test
    public void testFeatureKeyTypeEnumValues() {
        assertEquals(3, FeatureKeyType.values().length);
        assertNotNull(FeatureKeyType.document);
        assertNotNull(FeatureKeyType.company);
        assertNotNull(FeatureKeyType.eid);
    }

    @Test
    public void testFeatureKeyTypeNames() {
        assertEquals("document", FeatureKeyType.document.getName());
        assertEquals("named", FeatureKeyType.company.getName());
        assertEquals("epheralid", FeatureKeyType.eid.getName());
    }

    @Test
    public void testFeatureKeyTypeStableOrdinals() {
        assertEquals(0, FeatureKeyType.document.getStableOrdinal());
        assertEquals(1, FeatureKeyType.company.getStableOrdinal());
        assertEquals(2, FeatureKeyType.eid.getStableOrdinal());
    }

    @Test
    public void testIsSingle() {
        assertFalse(FeatureKeyType.document.isSingle());
        assertFalse(FeatureKeyType.company.isSingle());
        assertTrue(FeatureKeyType.eid.isSingle());
    }

    @Test
    public void testGetDefault() {
        assertEquals(FeatureKeyType.document, FeatureKeyType.getDefault());
    }

    @Test
    public void testEnumContext() {
        assertNotNull(FeatureKeyType.context);
        assertEquals("featurekeytype", FeatureKeyType.context.getName());
    }

    @Test
    public void testEnumContextRetrievesByOrdinal() {
        assertEquals(FeatureKeyType.document, FeatureKeyType.context.getByOrdinal(0));
        assertEquals(FeatureKeyType.company, FeatureKeyType.context.getByOrdinal(1));
        assertEquals(FeatureKeyType.eid, FeatureKeyType.context.getByOrdinal(2));
    }

    @Test
    public void testGetKeyBagCreatesNewArray() {
        long[] keys = FeatureKeyType.document.getKeyBag(null, 100L);
        assertNotNull(keys);
        assertEquals(3, keys.length);
        assertEquals(100L, keys[0]);
    }

    @Test
    public void testGetKeyBagUsesExistingArray() {
        long[] existingKeys = new long[3];
        long[] keys = FeatureKeyType.document.getKeyBag(existingKeys, 200L);
        assertSame(existingKeys, keys);
        assertEquals(200L, keys[0]);
    }

    @Test
    public void testGetKeyBagForEachType() {
        long[] keys = new long[3];
        
        keys = FeatureKeyType.document.getKeyBag(keys, 100L);
        assertEquals(100L, keys[0]);
        
        keys = FeatureKeyType.company.getKeyBag(keys, 200L);
        assertEquals(200L, keys[1]);
        
        keys = FeatureKeyType.eid.getKeyBag(keys, 300L);
        assertEquals(300L, keys[2]);
        
        // Verify all values are set correctly
        assertEquals(100L, keys[0]);
        assertEquals(200L, keys[1]);
        assertEquals(300L, keys[2]);
    }

    @Test
    public void testGetKeyBagWithZeroValue() {
        long[] keys = FeatureKeyType.document.getKeyBag(null, 0L);
        assertEquals(0L, keys[0]);
    }

    @Test
    public void testGetKeyBagWithNegativeValue() {
        long[] keys = FeatureKeyType.company.getKeyBag(null, -100L);
        assertEquals(-100L, keys[1]);
    }

    @Test
    public void testGetKeyBagWithMaxLong() {
        long[] keys = FeatureKeyType.eid.getKeyBag(null, Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, keys[2]);
    }

    @Test
    public void testGetKeyBagWithMinLong() {
        long[] keys = FeatureKeyType.document.getKeyBag(null, Long.MIN_VALUE);
        assertEquals(Long.MIN_VALUE, keys[0]);
    }

    @Test
    public void testKeyBagIndexMatchesOrdinal() {
        // Verify that getKeyBag uses ordinal as array index
        for (FeatureKeyType keyType : FeatureKeyType.values()) {
            long[] keys = new long[3];
            long testValue = 1000L + keyType.getStableOrdinal();
            keyType.getKeyBag(keys, testValue);
            assertEquals(testValue, keys[keyType.getStableOrdinal()]);
        }
    }
}
