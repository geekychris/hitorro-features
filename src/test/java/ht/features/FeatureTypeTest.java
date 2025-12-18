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

import com.hitorro.features.DerivedFeature;
import com.hitorro.features.Feature;
import com.hitorro.features.FeatureType;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for FeatureType enum
 */
public class FeatureTypeTest {

    @Test
    public void testFeatureTypeEnumValues() {
        assertEquals(3, FeatureType.values().length);
        assertNotNull(FeatureType.base);
        assertNotNull(FeatureType.derived);
        assertNotNull(FeatureType.function);
    }

    @Test
    public void testFeatureTypeNames() {
        assertEquals("base", FeatureType.base.getName());
        assertEquals("derived", FeatureType.derived.getName());
        assertEquals("function", FeatureType.function.getName());
    }

    @Test
    public void testFeatureShouldLookForChildren() {
        assertTrue(FeatureType.base.getShouldLookForChildren());
        assertFalse(FeatureType.derived.getShouldLookForChildren());
        assertFalse(FeatureType.function.getShouldLookForChildren());
    }

    @Test
    public void testGetFeatureInstance() {
        Feature baseFeature = FeatureType.base.getFeatureInstance();
        assertNotNull(baseFeature);
        assertEquals(Feature.class, baseFeature.getClass());

        Feature derivedFeature = FeatureType.derived.getFeatureInstance();
        assertNotNull(derivedFeature);
        assertEquals(DerivedFeature.class, derivedFeature.getClass());

        Feature functionFeature = FeatureType.function.getFeatureInstance();
        assertNotNull(functionFeature);
        assertEquals(Feature.class, functionFeature.getClass());
    }

    @Test
    public void testGetDefault() {
        assertEquals(FeatureType.base, FeatureType.getDefault());
    }

    @Test
    public void testGetByName() {
        assertEquals(FeatureType.base, FeatureType.getByName("base"));
        assertEquals(FeatureType.derived, FeatureType.getByName("derived"));
        assertEquals(FeatureType.function, FeatureType.getByName("function"));
    }

    @Test
    public void testGetByNameCaseInsensitive() {
        assertEquals(FeatureType.base, FeatureType.getByName("BASE"));
        assertEquals(FeatureType.derived, FeatureType.getByName("Derived"));
        assertEquals(FeatureType.function, FeatureType.getByName("FUNCTION"));
    }

    @Test
    public void testGetByNameInvalidName() {
        assertNull(FeatureType.getByName("invalid"));
        assertNull(FeatureType.getByName("unknown"));
    }

    @Test
    public void testSize() {
        assertEquals(3, FeatureType.size());
    }

    @Test
    public void testGetValidValues() {
        var validValues = FeatureType.getValidValues();
        assertNotNull(validValues);
        assertTrue(validValues.contains("base"));
        assertTrue(validValues.contains("derived"));
        assertTrue(validValues.contains("function"));
        assertEquals(3, validValues.size());
    }

    @Test
    public void testFeatureInstancesAreDistinct() {
        Feature feature1 = FeatureType.base.getFeatureInstance();
        Feature feature2 = FeatureType.base.getFeatureInstance();
        
        assertNotSame(feature1, feature2);
    }

    @Test
    public void testDerivedFeatureInstanceType() {
        Feature derivedFeature = FeatureType.derived.getFeatureInstance();
        assertTrue(derivedFeature instanceof DerivedFeature);
        assertTrue(derivedFeature instanceof Feature);
    }
}
