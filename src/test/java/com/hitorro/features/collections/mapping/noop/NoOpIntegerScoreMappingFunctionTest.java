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

import com.hitorro.features.collections.mapping.noop.NoOpIntegerScoreMappingFunction;
import com.hitorro.util.core.params.HTProperties;
import com.hitorro.features.collections.FeatureCollectionContext;
import com.hitorro.features.collections.mapping.intf.IntegerScoreMappingFunction;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for NoOpIntegerScoreMappingFunction
 */
public class NoOpIntegerScoreMappingFunctionTest {

    private NoOpIntegerScoreMappingFunction mappingFunction;
    private FeatureCollectionContext mockContext;

    @Before
    public void setUp() {
        mappingFunction = new NoOpIntegerScoreMappingFunction();
        HTProperties props = new HTProperties();
        mappingFunction.init(props);
        mockContext = null; // Context not used in this implementation
    }

    @Test
    public void testScoreReturnsFloatValue() {
        int value = 100;
        float score = mappingFunction.score(value, mockContext);
        assertEquals(100.0f, score, 0.0f);
    }

    @Test
    public void testScoreWithZero() {
        int value = 0;
        float score = mappingFunction.score(value, mockContext);
        assertEquals(0.0f, score, 0.0f);
    }

    @Test
    public void testScoreWithNegativeValue() {
        int value = -1000;
        float score = mappingFunction.score(value, mockContext);
        assertEquals(-1000.0f, score, 0.0f);
    }

    @Test
    public void testScoreWithMaxInteger() {
        int value = Integer.MAX_VALUE;
        float score = mappingFunction.score(value, mockContext);
        assertEquals((float) Integer.MAX_VALUE, score, 0.0f);
    }

    @Test
    public void testScoreWithMinInteger() {
        // Integer.MIN_VALUE is the NULL constant, so it returns default value
        int value = Integer.MIN_VALUE;
        float score = mappingFunction.score(value, mockContext);
        assertEquals(Float.MIN_VALUE, score, 0.0f);
    }

    @Test
    public void testScoreWithNullValue() {
        int nullValue = IntegerScoreMappingFunction.NULL;
        float score = mappingFunction.score(nullValue, mockContext);
        assertEquals(Float.MIN_VALUE, score, 0.0f);
    }

    @Test
    public void testDefaultValueIsSet() {
        assertEquals(Float.MIN_VALUE, mappingFunction.getDefaultValue(), 0.0f);
    }

    @Test
    public void testScorePreservesValue() {
        // Test various integer values
        int[] testValues = {-1000000, -1000, -1, 0, 1, 1000, 1000000};
        
        for (int value : testValues) {
            float expected = (float) value;
            float actual = mappingFunction.score(value, mockContext);
            assertEquals(expected, actual, 0.0f);
        }
    }

    @Test
    public void testImplementsInterface() {
        assertTrue(mappingFunction instanceof IntegerScoreMappingFunction);
    }

    @Test
    public void testLargeIntegerConversion() {
        // Test that large integers convert correctly to float
        int largeInt = 999999999;
        float score = mappingFunction.score(largeInt, mockContext);
        assertEquals((float) largeInt, score, 0.0f);
    }
}
