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
package com.hitorro.features.collections.mapping.noop;

import com.hitorro.features.collections.mapping.noop.NoOpFloatScoreMappingFunction;
import com.hitorro.util.core.params.HTProperties;
import com.hitorro.features.collections.FeatureCollectionContext;
import com.hitorro.features.collections.mapping.intf.FloatScoreMappingFunction;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for NoOpFloatScoreMappingFunction
 */
public class NoOpFloatScoreMappingFunctionTest {

    private NoOpFloatScoreMappingFunction mappingFunction;
    private FeatureCollectionContext mockContext;

    @Before
    public void setUp() {
        mappingFunction = new NoOpFloatScoreMappingFunction();
        HTProperties props = new HTProperties();
        mappingFunction.init(props);
        mockContext = null; // Context not used in this implementation
    }

    @Test
    public void testScoreReturnsFloatValue() {
        float value = 100.5f;
        float score = mappingFunction.score(value, mockContext);
        assertEquals(100.5f, score, 0.0f);
    }

    @Test
    public void testScoreWithZero() {
        float value = 0.0f;
        float score = mappingFunction.score(value, mockContext);
        assertEquals(0.0f, score, 0.0f);
    }

    @Test
    public void testScoreWithNegativeValue() {
        float value = -99.99f;
        float score = mappingFunction.score(value, mockContext);
        assertEquals(-99.99f, score, 0.0001f);
    }

    @Test
    public void testScoreWithMaxFloat() {
        float value = Float.MAX_VALUE;
        float score = mappingFunction.score(value, mockContext);
        assertEquals(Float.MAX_VALUE, score, 0.0f);
    }

    @Test
    public void testScoreWithMinFloat() {
        float value = Float.MIN_VALUE;
        float score = mappingFunction.score(value, mockContext);
        assertEquals(Float.MIN_VALUE, score, 0.0f);
    }

    @Test
    public void testScoreWithNullValue() {
        float nullValue = FloatScoreMappingFunction.NULL;
        float score = mappingFunction.score(nullValue, mockContext);
        assertEquals(FloatScoreMappingFunction.NULL, score, 0.0f);
    }

    @Test
    public void testDefaultValueIsNull() {
        // NoOpFloatScoreMappingFunction uses NULL as default
        assertEquals(FloatScoreMappingFunction.NULL, mappingFunction.getDefaultValue(), 0.0f);
    }

    @Test
    public void testScorePreservesValue() {
        // Test various float values
        float[] testValues = {-1000.5f, -1.0f, 0.0f, 1.0f, 3.14159f, 1000.999f};
        
        for (float value : testValues) {
            float score = mappingFunction.score(value, mockContext);
            assertEquals(value, score, 0.0001f);
        }
    }

    @Test
    public void testImplementsInterface() {
        assertTrue(mappingFunction instanceof FloatScoreMappingFunction);
    }

    @Test
    public void testScoreWithInfinity() {
        float posInf = Float.POSITIVE_INFINITY;
        float negInf = Float.NEGATIVE_INFINITY;
        
        assertEquals(posInf, mappingFunction.score(posInf, mockContext), 0.0f);
        assertEquals(negInf, mappingFunction.score(negInf, mockContext), 0.0f);
    }

    @Test
    public void testScoreWithNaN() {
        float nan = Float.NaN;
        float score = mappingFunction.score(nan, mockContext);
        assertTrue(Float.isNaN(score));
    }

    @Test
    public void testInitSetsDefaultValue() {
        NoOpFloatScoreMappingFunction newFunction = new NoOpFloatScoreMappingFunction();
        HTProperties props = new HTProperties();
        newFunction.init(props);
        
        assertEquals(FloatScoreMappingFunction.NULL, newFunction.getDefaultValue(), 0.0f);
    }
}
