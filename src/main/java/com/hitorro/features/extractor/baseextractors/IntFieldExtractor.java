package com.hitorro.features.extractor.baseextractors;

import com.fasterxml.jackson.databind.JsonNode;
import com.hitorro.features.Feature;
import com.hitorro.features.FeatureValue;
import com.hitorro.features.extractor.ExtractionContext;
import com.hitorro.features.extractor.ExtractorDefinition;
import com.hitorro.features.extractor.FeatureExtractor;
import com.hitorro.features.extractor.Results;
import com.hitorro.util.json.keys.StringProperty;
import com.hitorro.util.typesystem.BaseFields;
import com.hitorro.util.typesystem.valuesource.ValueSourceForClass;

/**
 *
 */
public class IntFieldExtractor extends FeatureExtractor<ValueSourceForClass> {
    public static final StringProperty SectionKey = new StringProperty("field", "", null);

    private String field;

    public boolean init(final JsonNode props, ExtractorDefinition ed, String featureName, Feature feature) {
        super.init(props, ed, featureName, feature);
        field = SectionKey.apply(props);
        return false;
    }

    public int extract(final ValueSourceForClass v, final Results results, final ExtractionContext ec) {
        BaseFields bf = ec.getBaseFields();
        Object o = v.getValue(v, field);
        Number num = (Number) o;
        FeatureValue fv = FeatureValue.getFeature(feature, bf.getId(), bf.getCreated(), num.intValue());
        results.add(this, fv);
        return 1;
    }
}
