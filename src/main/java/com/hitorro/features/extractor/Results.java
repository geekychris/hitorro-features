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
import com.hitorro.util.core.opers.HTPredicate;

import java.util.ArrayList;
import java.util.List;

/**
 * Result set of feature values generated during the extraction process
 */
public class Results {
    private List<FeatureValue> fvs = new ArrayList();

    public void add(FeatureExtractor fe, FeatureValue fv) {
        fv.setIsScratch(fe.isScratch());
        fvs.add(fv);
    }

    public void clear() {
        fvs.clear();
    }

    public List<FeatureValue> getAllValues() {
        return fvs;
    }

    /**
     * Get all the feature values that test a specific constraint.
     *
     * @param constraint
     * @return constrained listFiles of feature values
     */
    public List<FeatureValue> getValuesByConstraint(HTPredicate<FeatureValue> constraint) {
        List<FeatureValue> vals = new ArrayList();
        getValuesByConstraint(constraint, vals);
        return vals;

    }

    /**
     * Get all the feature values that test a specific constraint, adding them to an existing container
     *
     * @param constraint
     * @param vals
     * @return
     */
    public int getValuesByConstraint(HTPredicate<FeatureValue> constraint, List<FeatureValue> vals) {
        int added = 0;
        for (FeatureValue fv : fvs) {
            if (constraint.test(fv)) {
                vals.add(fv);
                added++;
            }
        }
        return added;
    }
}
