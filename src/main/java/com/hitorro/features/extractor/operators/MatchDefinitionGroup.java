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
package com.hitorro.features.extractor.operators;

import com.fasterxml.jackson.databind.JsonNode;
import com.hitorro.features.Feature;
import com.hitorro.features.extractor.FeatureExtractor;
import com.hitorro.util.core.HTAssert;
import com.hitorro.util.core.opers.HTPredicate;


public class MatchDefinitionGroup implements HTPredicate<Feature> {
    private String group;

    public MatchDefinitionGroup(String group) {
        this.group = group.toLowerCase();
    }

    public void initForPass() {

    }

    public boolean initFromMap(final JsonNode map) {
        // MUST IMPLEMENT
        HTAssert.assertThat(false, "MatchDefinitionGroup.initFromMap not implemented");
        return false;
    }

    public boolean test(final Feature feature) {
        FeatureExtractor fe = feature.getFeatureExtractor();
        if (fe != null && fe.getExtractorDefinition() != null) {
            for (String groupE : feature.getExtractorGroups()) {
                if (group.equals(groupE)) {
                    return true;
                }
            }
        }
        return false;
    }
}
