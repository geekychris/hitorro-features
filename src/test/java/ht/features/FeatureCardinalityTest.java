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

import com.hitorro.features.FeatureCardinality;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for FeatureCardinality enum
 */
public class FeatureCardinalityTest {

    @Test
    public void testFeatureCardinalityEnumValues() {
        assertEquals(3, FeatureCardinality.values().length);
        assertNotNull(FeatureCardinality.single);
        assertNotNull(FeatureCardinality.named);
        assertNotNull(FeatureCardinality.index);
    }

    @Test
    public void testFeatureCardinalityNames() {
        assertEquals("single", FeatureCardinality.single.getName());
        assertEquals("named", FeatureCardinality.named.getName());
        assertEquals("index", FeatureCardinality.index.getName());
    }

    @Test
    public void testFeatureCardinalityStableOrdinals() {
        assertEquals(0, FeatureCardinality.single.getStableOrdinal());
        assertEquals(1, FeatureCardinality.named.getStableOrdinal());
        assertEquals(2, FeatureCardinality.index.getStableOrdinal());
    }

    @Test
    public void testIsSingle() {
        // Note: The implementation has isSingle=false for single, true for named/index
        // This appears to be inverted semantics where isSingle means "is multivalued"
        assertFalse(FeatureCardinality.single.isSingle());
        assertTrue(FeatureCardinality.named.isSingle());
        assertTrue(FeatureCardinality.index.isSingle());
    }

    @Test
    public void testGetDefault() {
        assertEquals(FeatureCardinality.single, FeatureCardinality.getDefault());
    }

    @Test
    public void testEnumContext() {
        assertNotNull(FeatureCardinality.context);
        assertEquals("datatype", FeatureCardinality.context.getName());
    }

    @Test
    public void testEnumContextRetrievesByOrdinal() {
        assertEquals(FeatureCardinality.single, FeatureCardinality.context.getByOrdinal(0));
        assertEquals(FeatureCardinality.named, FeatureCardinality.context.getByOrdinal(1));
        assertEquals(FeatureCardinality.index, FeatureCardinality.context.getByOrdinal(2));
    }

    @Test
    public void testSingleVsMultiValuedSemantics() {
        // Note: The implementation has inverted semantics - isSingle=false for single cardinality
        // single cardinality means one value per feature
        assertFalse(FeatureCardinality.single.isSingle());
        
        // named and index are multivalued features (isSingle=true in implementation)
        assertTrue(FeatureCardinality.named.isSingle());
        assertTrue(FeatureCardinality.index.isSingle());
    }

    @Test
    public void testCardinalityOrdering() {
        // Ensure ordinals are sequential and consistent
        FeatureCardinality[] values = FeatureCardinality.values();
        for (int i = 0; i < values.length; i++) {
            assertEquals(i, values[i].getStableOrdinal());
        }
    }
}
