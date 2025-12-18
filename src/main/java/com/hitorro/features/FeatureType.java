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

import java.util.HashMap;
import java.util.Set;

/**
 *
 */
public enum FeatureType {
    base("base", true) {
        public Feature getFeatureInstance() {
            return new Feature();
        }
    },
    derived("derived", false) {
        public Feature getFeatureInstance() {
            return new DerivedFeature();
        }
    },
    function("function", false) {
        public Feature getFeatureInstance() {
            return new Feature();
        }
    };

    private static HashMap<String, FeatureType> s_byShortName;

    private String name;
    private boolean shouldLookForChildren;

    FeatureType(String name, boolean shouldLookForChildren) {
        this.name = name;
        this.shouldLookForChildren = shouldLookForChildren;
        setMapEntry(this);
    }

    public static Set<String> getValidValues() {
        return s_byShortName.keySet();
    }

    public static FeatureType getDefault() {
        return base;
    }

    public static FeatureType getByName(String name) {
        return s_byShortName.get(name.toLowerCase());
    }

    public static int size() {
        return s_byShortName.size();
    }

    private static void setMapEntry(FeatureType filter) {
        if (s_byShortName == null) {
            s_byShortName = new HashMap<String, FeatureType>();
        }
        s_byShortName.put(filter.getName(), filter);
    }

    public abstract Feature getFeatureInstance();

    public boolean getShouldLookForChildren() {
        return shouldLookForChildren;
    }

    public String getName() {
        return name;
    }
}
