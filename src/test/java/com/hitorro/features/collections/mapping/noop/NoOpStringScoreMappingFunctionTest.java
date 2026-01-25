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

import com.hitorro.features.collections.mapping.noop.NoOpStringScoreMappingFunction;
import com.hitorro.util.core.params.HTProperties;
import com.hitorro.features.collections.FeatureCollectionContext;
import com.hitorro.features.collections.mapping.intf.StringScoreMappingFunction;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for NoOpStringScoreMappingFunction
 */
public class NoOpStringScoreMappingFunctionTest {

    private NoOpStringScoreMappingFunction mappingFunction;
    private FeatureCollectionContext mockContext;

    @Before
    public void setUp() {
        mappingFunction = new NoOpStringScoreMappingFunction();
        HTProperties props = new HTProperties();
        mappingFunction.init(props);
        mockContext = null; // Context not used in this implementation
    }

    @Test
    public void testScoreParsesFloatString() {
        String value = "100.5";
        float score = mappingFunction.score(value, mockContext);
        assertEquals(100.5f, score, 0.0f);
    }

    @Test
    public void testScoreWithZeroString() {
        String value = "0.0";
        float score = mappingFunction.score(value, mockContext);
        assertEquals(0.0f, score, 0.0f);
    }

    @Test
    public void testScoreWithNegativeString() {
        String value = "-99.99";
        float score = mappingFunction.score(value, mockContext);
        assertEquals(-99.99f, score, 0.0001f);
    }

    @Test
    public void testScoreWithIntegerString() {
        String value = "42";
        float score = mappingFunction.score(value, mockContext);
        assertEquals(42.0f, score, 0.0f);
    }

    @Test
    public void testScoreWithNullValue() {
        String nullValue = StringScoreMappingFunction.NULL;
        float score = mappingFunction.score(nullValue, mockContext);
        assertEquals(Float.MIN_VALUE, score, 0.0f);
    }

    @Test
    public void testDefaultValueIsSet() {
        assertEquals(Float.MIN_VALUE, mappingFunction.getDefaultValue(), 0.0f);
    }

    @Test
    public void testScoreWithScientificNotation() {
        String value = "1.5e3";
        float score = mappingFunction.score(value, mockContext);
        assertEquals(1500.0f, score, 0.0f);
    }

    @Test
    public void testScoreWithNegativeScientificNotation() {
        String value = "-2.5e-2";
        float score = mappingFunction.score(value, mockContext);
        assertEquals(-0.025f, score, 0.0001f);
    }

    @Test
    public void testScoreWithLargeNumber() {
        String value = "999999.999";
        float score = mappingFunction.score(value, mockContext);
        assertEquals(999999.999f, score, 0.01f);
    }

    @Test
    public void testScoreWithVerySmallNumber() {
        String value = "0.000001";
        float score = mappingFunction.score(value, mockContext);
        assertEquals(0.000001f, score, 0.0000001f);
    }

    @Test(expected = NumberFormatException.class)
    public void testScoreWithInvalidString() {
        String value = "not_a_number";
        mappingFunction.score(value, mockContext);
    }

    @Test(expected = NumberFormatException.class)
    public void testScoreWithEmptyString() {
        String value = "";
        mappingFunction.score(value, mockContext);
    }

    @Test
    public void testScoreWithNullStringReturnsDefault() {
        // Null string is the NULL constant, so it returns default value
        String nullValue = null;
        float score = mappingFunction.score(nullValue, mockContext);
        assertEquals(Float.MIN_VALUE, score, 0.0f);
    }

    @Test
    public void testImplementsInterface() {
        assertTrue(mappingFunction instanceof StringScoreMappingFunction);
    }

    @Test
    public void testScoreWithInfinityString() {
        String posInf = "Infinity";
        String negInf = "-Infinity";
        
        assertEquals(Float.POSITIVE_INFINITY, mappingFunction.score(posInf, mockContext), 0.0f);
        assertEquals(Float.NEGATIVE_INFINITY, mappingFunction.score(negInf, mockContext), 0.0f);
    }

    @Test
    public void testScoreWithNaNString() {
        String nan = "NaN";
        float score = mappingFunction.score(nan, mockContext);
        assertTrue(Float.isNaN(score));
    }

    @Test
    public void testScoreWithLeadingWhitespace() {
        String value = "  42.5";
        float score = mappingFunction.score(value, mockContext);
        assertEquals(42.5f, score, 0.0f);
    }

    @Test
    public void testScoreWithTrailingWhitespace() {
        String value = "42.5  ";
        float score = mappingFunction.score(value, mockContext);
        assertEquals(42.5f, score, 0.0f);
    }
}
