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
