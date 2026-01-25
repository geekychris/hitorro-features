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

import com.hitorro.features.collections.mapping.noop.NoOpByteScoreMappingFunction;
import com.hitorro.util.core.params.HTProperties;
import com.hitorro.features.collections.FeatureCollectionContext;
import com.hitorro.features.collections.mapping.intf.ByteScoreMappingFunction;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for NoOpByteScoreMappingFunction
 */
public class NoOpByteScoreMappingFunctionTest {

    private NoOpByteScoreMappingFunction mappingFunction;
    private FeatureCollectionContext mockContext;

    @Before
    public void setUp() {
        mappingFunction = new NoOpByteScoreMappingFunction();
        HTProperties props = new HTProperties();
        mappingFunction.init(props);
        mockContext = null; // Context not used in this implementation
    }

    @Test
    public void testScoreReturnsFloatValue() {
        byte value = 10;
        float score = mappingFunction.score(value, mockContext);
        assertEquals(10.0f, score, 0.0f);
    }

    @Test
    public void testScoreWithZero() {
        byte value = 0;
        float score = mappingFunction.score(value, mockContext);
        assertEquals(0.0f, score, 0.0f);
    }

    @Test
    public void testScoreWithNegativeValue() {
        byte value = -10;
        float score = mappingFunction.score(value, mockContext);
        assertEquals(-10.0f, score, 0.0f);
    }

    @Test
    public void testScoreWithMaxByte() {
        byte value = Byte.MAX_VALUE;
        float score = mappingFunction.score(value, mockContext);
        assertEquals((float) Byte.MAX_VALUE, score, 0.0f);
    }

    @Test
    public void testScoreWithMinByte() {
        // Byte.MIN_VALUE is the NULL constant, so it returns default value
        byte value = Byte.MIN_VALUE;
        float score = mappingFunction.score(value, mockContext);
        assertEquals(Float.MIN_VALUE, score, 0.0f);
    }

    @Test
    public void testScoreWithNullValue() {
        byte nullValue = ByteScoreMappingFunction.NULL;
        float score = mappingFunction.score(nullValue, mockContext);
        assertEquals(Float.MIN_VALUE, score, 0.0f);
    }

    @Test
    public void testDefaultValueIsSet() {
        assertEquals(Float.MIN_VALUE, mappingFunction.getDefaultValue(), 0.0f);
    }

    @Test
    public void testScorePreservesValue() {
        // Test that score is a simple cast from byte to float
        for (byte b = -128; b < 127; b++) {
            if (b != ByteScoreMappingFunction.NULL) {
                float expected = (float) b;
                float actual = mappingFunction.score(b, mockContext);
                assertEquals(expected, actual, 0.0f);
            }
        }
    }

    @Test
    public void testImplementsInterface() {
        assertTrue(mappingFunction instanceof ByteScoreMappingFunction);
    }
}
