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

import ht.features.collections.mapping.ScoreFunctionBaseTest;
import ht.features.collections.mapping.noop.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test suite to run all unit tests for the hitorro-features project
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    // Core enum and class tests
    DataTypeTest.class,
    FeatureTypeTest.class,
    FeatureCardinalityTest.class,
    FeatureKeyTypeTest.class,
    PartitionModelTest.class,
    RealmIdTest.class,
    
    // Score mapping function tests
    ScoreFunctionBaseTest.class,
    NoOpByteScoreMappingFunctionTest.class,
    NoOpIntegerScoreMappingFunctionTest.class,
    NoOpFloatScoreMappingFunctionTest.class,
    NoOpStringScoreMappingFunctionTest.class
})
public class AllTestsSuite {
    // This class remains empty, it is used only as a holder for the above annotations
}
