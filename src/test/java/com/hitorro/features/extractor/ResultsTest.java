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
package com.hitorro.features.extractor;

import com.hitorro.features.FeatureValue;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the Results container class.
 */
public class ResultsTest {

    private Results results;

    @Before
    public void setUp() {
        results = new Results();
    }

    @Test
    public void testNewResultsIsEmpty() {
        assertTrue(results.getAllValues().isEmpty());
        assertEquals(0, results.getAllValues().size());
    }

    @Test
    public void testAddSingleValue() {
        FeatureExtractor<?> mockExtractor = mock(FeatureExtractor.class);
        when(mockExtractor.isScratch()).thenReturn(false);

        FeatureValue mockValue = mock(FeatureValue.class);

        results.add(mockExtractor, mockValue);

        assertEquals(1, results.getAllValues().size());
        assertSame(mockValue, results.getAllValues().get(0));
        verify(mockValue).setIsScratch(false);
    }

    @Test
    public void testAddMultipleValues() {
        FeatureExtractor<?> mockExtractor = mock(FeatureExtractor.class);
        when(mockExtractor.isScratch()).thenReturn(false);

        FeatureValue fv1 = mock(FeatureValue.class);
        FeatureValue fv2 = mock(FeatureValue.class);
        FeatureValue fv3 = mock(FeatureValue.class);

        results.add(mockExtractor, fv1);
        results.add(mockExtractor, fv2);
        results.add(mockExtractor, fv3);

        assertEquals(3, results.getAllValues().size());
    }

    @Test
    public void testAddWithScratchExtractor() {
        FeatureExtractor<?> scratchExtractor = mock(FeatureExtractor.class);
        when(scratchExtractor.isScratch()).thenReturn(true);

        FeatureValue mockValue = mock(FeatureValue.class);

        results.add(scratchExtractor, mockValue);

        verify(mockValue).setIsScratch(true);
        assertEquals(1, results.getAllValues().size());
    }

    @Test
    public void testClear() {
        FeatureExtractor<?> mockExtractor = mock(FeatureExtractor.class);
        when(mockExtractor.isScratch()).thenReturn(false);

        results.add(mockExtractor, mock(FeatureValue.class));
        results.add(mockExtractor, mock(FeatureValue.class));

        assertEquals(2, results.getAllValues().size());

        results.clear();

        assertEquals(0, results.getAllValues().size());
        assertTrue(results.getAllValues().isEmpty());
    }

    @Test
    public void testClearAndReuse() {
        FeatureExtractor<?> mockExtractor = mock(FeatureExtractor.class);
        when(mockExtractor.isScratch()).thenReturn(false);

        results.add(mockExtractor, mock(FeatureValue.class));
        results.clear();
        results.add(mockExtractor, mock(FeatureValue.class));

        assertEquals(1, results.getAllValues().size());
    }

    @Test
    public void testGetValuesByConstraintMatchesAll() {
        FeatureExtractor<?> mockExtractor = mock(FeatureExtractor.class);
        when(mockExtractor.isScratch()).thenReturn(false);

        results.add(mockExtractor, mock(FeatureValue.class));
        results.add(mockExtractor, mock(FeatureValue.class));
        results.add(mockExtractor, mock(FeatureValue.class));

        List<FeatureValue> matched = results.getValuesByConstraint(fv -> true);

        assertEquals(3, matched.size());
    }

    @Test
    public void testGetValuesByConstraintMatchesNone() {
        FeatureExtractor<?> mockExtractor = mock(FeatureExtractor.class);
        when(mockExtractor.isScratch()).thenReturn(false);

        results.add(mockExtractor, mock(FeatureValue.class));
        results.add(mockExtractor, mock(FeatureValue.class));

        List<FeatureValue> matched = results.getValuesByConstraint(fv -> false);

        assertTrue(matched.isEmpty());
    }

    @Test
    public void testGetValuesByConstraintOnEmptyResults() {
        List<FeatureValue> matched = results.getValuesByConstraint(fv -> true);
        assertTrue(matched.isEmpty());
    }

    @Test
    public void testGetValuesByConstraintWithExistingList() {
        FeatureExtractor<?> mockExtractor = mock(FeatureExtractor.class);
        when(mockExtractor.isScratch()).thenReturn(false);

        results.add(mockExtractor, mock(FeatureValue.class));
        results.add(mockExtractor, mock(FeatureValue.class));

        List<FeatureValue> existing = new ArrayList<>();
        existing.add(mock(FeatureValue.class)); // pre-existing entry

        int added = results.getValuesByConstraint(fv -> true, existing);

        assertEquals(2, added);
        assertEquals(3, existing.size()); // 1 pre-existing + 2 added
    }
}
