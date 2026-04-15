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
package com.hitorro.features;

import com.hitorro.util.core.EnumContext;

import java.util.HashMap;
import java.util.Set;

/**
 * Determines if any segmentation is applied to feature values
 */
public enum PartitionModel {
    None("none"),
    Date("date"),
    // note in this case partitioning has to happen further upstream of the vectorization process or filtered
    // at read time (since hashes are the last thing to be used in the composite key for sorting)
    Hash("hash");

    public static EnumContext<PartitionModel> context;
    private static HashMap<String, PartitionModel> s_byShortName;
    private String name;

    PartitionModel(String name) {
        this.name = name;
        setMapEntry(this);
    }

    public static Set<String> getValidValues() {
        return s_byShortName.keySet();
    }

    public static PartitionModel getDefault() {
        return None;
    }

    public static PartitionModel getByName(String name) {
        return s_byShortName.get(name.toLowerCase());
    }

    public static final int size() {
        return s_byShortName.size();
    }

    private static void setMapEntry(PartitionModel filter) {
        if (context == null) {
            context = new EnumContext<PartitionModel>("featurekeytype");
        }

        context.setNames(filter, filter.getName(), filter.ordinal());
    }

    public String getName() {
        return name;
    }
}
