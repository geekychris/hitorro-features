package com.hitorro.features.extractor;

import com.hitorro.features.Feature;
import com.hitorro.util.core.Log;
import com.hitorro.util.typesystem.BaseFields;

import java.util.List;

/**
 *
 */
public class ExtractionSet<O> {
    private FeatureExtractor<O> feArr[];

    private BaseFields bfMaster;

    public ExtractionSet(BaseFields bf, List<Feature> features) {
        this.bfMaster = bf;
        feArr = new FeatureExtractor[features.size()];
        for (int i = 0; i < features.size(); i++) {
            feArr[i] = features.get(i).getFeatureExtractor();
        }
    }

    public int size() {
        return feArr.length;
    }

    /**
     * Get a new extraction compContext that has the correct base fields on it.
     *
     * @return
     */
    public ExtractionContext getExtractionContext() {
        return new ExtractionContext();
    }

    public int extract(O o, Results results, ExtractionContext context) {
        context.start();
        int count = 0;
        if (!context.setBaseFields(bfMaster, o)) {
            Log.extractors.error("Unable to compute base fields");
            return -1;
        }
        for (FeatureExtractor fe : feArr) {
            count += fe.extract(o, results, context);
        }
        context.stop();
        return count;
    }
}
