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

/**
 * Determines if this is a single or multivalued feature
 */
public enum FeatureCardinality {
    single(0, "single", false),
    named(1, "named", true),
    index(2, "index", true);

    public static EnumContext<FeatureCardinality> context;
    private String name;
    private boolean isSingle;
    private int ord;

    FeatureCardinality(int ord, String name, boolean isSingle) {
        this.ord = ord;
        this.name = name;
        this.isSingle = isSingle;
        setMapEntry(this);
    }

    public static FeatureCardinality getDefault() {
        return single;
    }

    private static void setMapEntry(FeatureCardinality filter) {
        if (context == null) {
            context = new EnumContext<FeatureCardinality>("datatype");
        }

        context.setNames(filter, filter.getName(), filter.getStableOrdinal());
    }

    public int getStableOrdinal() {
        return ord;
    }

    public String getName() {
        return name;
    }

    public boolean isSingle() {
        return isSingle;
    }
}
