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
 * Key Types are used with a bag of keys.  Key types are typically scoped something like:
 * <p/>
 * 1 unique key per doc 1 unique key per some kind of group of docs (website domain id, country, language, anything
 * where features are not per document). 1 ephemeral id (if you have features that are User: chris
 */
public enum FeatureKeyType {
    document(0, "document", false),
    company(1, "named", false),
    //ephemeral id
    eid(2, "epheralid", true);

    public static EnumContext<FeatureKeyType> context;
    private String name;
    private boolean isSingle;
    private int ord;


    FeatureKeyType(int ord, String name, boolean isSingle) {
        this.ord = ord;
        this.name = name;
        this.isSingle = isSingle;
        setMapEntry(this);
    }

    public static FeatureKeyType getDefault() {
        return document;
    }

    private static void setMapEntry(FeatureKeyType filter) {
        if (context == null) {
            context = new EnumContext<FeatureKeyType>("featurekeytype");
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

    /**
     * populate the keys bag with the value for this particular
     *
     * @param keys
     * @param l
     * @return
     */
    public long[] getKeyBag(long keys[], long l) {
        if (keys == null) {
            keys = new long[values().length];
        }
        keys[ord] = l;
        return keys;
    }
}
