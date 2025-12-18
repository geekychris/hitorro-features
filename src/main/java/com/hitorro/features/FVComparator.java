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

import java.util.Comparator;

/**
 * Need a deterministic order to how feature values get ordered before they get turned into vectors and other
 * "efficient" data processing pipelines.  Order is as follows:
 * <p/>
 * For segmentation by specific "feature", meaning everything except its ID, we order by the composite set of fields:
 * <p/>
 * parition (if the data is partitioned realmid feature hash index variable (if exists)
 * <p/>
 * <p/>
 * .... then if hash level
 * <p/>
 * id
 */
public class FVComparator implements Comparator<FeatureValue> {
    public static final FVComparator SetComp = new FVComparator(CompType.set);

    public static final FVComparator IDComp = new FVComparator(CompType.id);
    private CompType ct;

    private FVComparator(CompType ct) {
        this.ct = ct;
    }

    public int compare(final FeatureValue f1, final FeatureValue f2) {
        // Partition
        if (f1.partition != f2.partition) {
            if (f1.partition > f2.partition) {
                return 1;
            }
            return -1;
        }
        // Realm
        if (f1.realm.id != f2.realm.id) {
            if (f1.realm.id > f2.realm.id) {
                return 1;
            }
            return -1;
        }
        // feature hash
        if (f1.featureHash != f2.featureHash) {
            if (f1.featureHash > f2.featureHash) {
                return 1;
            }
            return -1;
        }
        // feature index
        if (f1.index != f2.index) {
            if (f1.index > f2.index) {
                return 1;
            }
            return -1;
        }
        // feature index
        if (f1.variable != null) {
            if (f2.variable != null) {
                int v = f1.variable.compareTo(f2.variable);
                if (v != 0) {
                    return v;
                }
            } else {
                // f2 doesnt contain a value so we consider f1 greater (...this shouldnt happen as the feature was
                // the same!!!
                return 1;
            }
        } else {
            if (f2.variable != null) {
                // f2 contains a value so it wins (this shouldnt happen either!!!)
                return -1;
            }
        }
        if (f1.index != f2.index) {
            if (f1.index > f2.index) {
                return 1;
            }
            return -1;
        }

        // ..... and only if we are in ID mode
        if (ct == CompType.id) {
            if (f1.id != f2.id) {
                if (f1.id > f2.id) {
                    return 1;
                }
            }
        }

        // same
        return 0;
    }

    enum CompType {
        set, id
    }
}
