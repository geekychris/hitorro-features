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

import com.hitorro.features.PartitionModel;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for PartitionModel enum
 */
public class PartitionModelTest {

    @Test
    public void testPartitionModelEnumValues() {
        assertEquals(3, PartitionModel.values().length);
        assertNotNull(PartitionModel.None);
        assertNotNull(PartitionModel.Date);
        assertNotNull(PartitionModel.Hash);
    }

    @Test
    public void testPartitionModelNames() {
        assertEquals("none", PartitionModel.None.getName());
        assertEquals("date", PartitionModel.Date.getName());
        assertEquals("hash", PartitionModel.Hash.getName());
    }

    @Test
    public void testGetDefault() {
        assertEquals(PartitionModel.None, PartitionModel.getDefault());
    }

    @Test
    public void testEnumContext() {
        assertNotNull(PartitionModel.context);
        assertEquals("featurekeytype", PartitionModel.context.getName());
    }

    @Test
    public void testEnumContextRetrievesByOrdinal() {
        assertEquals(PartitionModel.None, PartitionModel.context.getByOrdinal(0));
        assertEquals(PartitionModel.Date, PartitionModel.context.getByOrdinal(1));
        assertEquals(PartitionModel.Hash, PartitionModel.context.getByOrdinal(2));
    }

    @Test
    public void testOrdinalValues() {
        // Test that ordinals are sequential
        assertEquals(0, PartitionModel.None.ordinal());
        assertEquals(1, PartitionModel.Date.ordinal());
        assertEquals(2, PartitionModel.Hash.ordinal());
    }

    @Test
    public void testNoneIsDefault() {
        // Verify that None is the recommended default
        PartitionModel defaultModel = PartitionModel.getDefault();
        assertEquals(PartitionModel.None, defaultModel);
        assertEquals("none", defaultModel.getName());
    }

    @Test
    public void testPartitionModelSemantics() {
        // Test that each model has distinct semantics
        assertNotEquals(PartitionModel.None.getName(), PartitionModel.Date.getName());
        assertNotEquals(PartitionModel.Date.getName(), PartitionModel.Hash.getName());
        assertNotEquals(PartitionModel.None.getName(), PartitionModel.Hash.getName());
    }
}
