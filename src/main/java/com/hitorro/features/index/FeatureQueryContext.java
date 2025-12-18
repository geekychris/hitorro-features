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
package com.hitorro.features.index;

import com.hitorro.features.FeatureSetKey;
import com.hitorro.features.RealmId;
import com.hitorro.features.collections.BaseCollection;
import com.hitorro.features.collections.CascadingCollection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: chris
 */
public class FeatureQueryContext {
    private FeatureCacheManager fcm;

    public FeatureQueryContext(FeatureCacheManager fcm) {
        this.fcm = fcm;
    }

    public BaseCollection getCollection(FeatureSetKey fsk) throws IOException {
        return fcm.getCollection(fsk);
    }

    /**
     * gather all the tiers of features.   This is a gross level function.  It doesnt take account of ordering or what
     * levels should be infact used for this feature.
     *
     * @param fsk
     * @param ids
     * @return
     * @throws IOException
     */
    public BaseCollection getCollection(FeatureSetKey fsk, List<RealmId> ids) throws IOException {
        List<BaseCollection> colls = new ArrayList();
        for (RealmId id : ids) {
            BaseCollection bc = fcm.getCollection(fsk);
        }
        if (colls.size() == 0) {
            return null;
        }
        if (colls.size() == 1) {
            return colls.get(0);
        }
        CascadingCollection cc = new CascadingCollection(colls);
        BaseCollection bc = colls.get(0);
        cc.setIDIndex(0, bc.getDataType(), bc.getFeature(), null);
        return cc;
    }
}
