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
package ht.features.collections.mapping;

import com.hitorro.features.collections.mapping.ScoreFunctionBase;
import com.hitorro.util.core.params.HTProperties;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for ScoreFunctionBase
 */
public class ScoreFunctionBaseTest {

    private TestScoreFunctionBase scoreFunction;

    // Concrete implementation for testing
    private static class TestScoreFunctionBase extends ScoreFunctionBase {
        // No additional methods needed for basic testing
    }

    @Before
    public void setUp() {
        scoreFunction = new TestScoreFunctionBase();
    }

    @Test
    public void testInitSetsDefaultValue() {
        HTProperties props = new HTProperties();
        scoreFunction.init(props);
        
        assertEquals(Float.MIN_VALUE, scoreFunction.getDefaultValue(), 0.0f);
    }

    @Test
    public void testGetDefaultValueBeforeInit() {
        // Before init, default should be 0.0f (Java default for float field)
        assertEquals(0.0f, scoreFunction.getDefaultValue(), 0.0f);
    }

    @Test
    public void testGetDefaultValueAfterInit() {
        HTProperties props = new HTProperties();
        scoreFunction.init(props);
        
        float defaultValue = scoreFunction.getDefaultValue();
        assertEquals(Float.MIN_VALUE, defaultValue, 0.0f);
    }

    @Test
    public void testInitWithNullProperties() {
        // Should handle null properties gracefully
        scoreFunction.init(null);
        assertEquals(Float.MIN_VALUE, scoreFunction.getDefaultValue(), 0.0f);
    }

    @Test
    public void testMultipleInitsDoNotChangeDefaultValue() {
        HTProperties props1 = new HTProperties();
        HTProperties props2 = new HTProperties();
        
        scoreFunction.init(props1);
        float firstValue = scoreFunction.getDefaultValue();
        
        scoreFunction.init(props2);
        float secondValue = scoreFunction.getDefaultValue();
        
        assertEquals(firstValue, secondValue, 0.0f);
        assertEquals(Float.MIN_VALUE, secondValue, 0.0f);
    }
}
