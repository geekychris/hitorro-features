package com.hitorro.features.extractor;

import com.hitorro.features.FeatureValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Results with a apply of features....only supports features that are single valued.
 */
public class MappedResults extends Results {
    private Map<String, FeatureValue> map = new HashMap();

    public void add(FeatureExtractor fe, FeatureValue fv) {
        super.add(fe, fv);
        map.put(fv.getFeature().getName(), fv);
    }

    public void clear() {
        super.clear();
        map.clear();
    }

    public FeatureValue get(String key) {
        return map.get(key);
    }
}
