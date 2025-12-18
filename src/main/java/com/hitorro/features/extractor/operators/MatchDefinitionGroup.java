package com.hitorro.features.extractor.operators;

import com.fasterxml.jackson.databind.JsonNode;
import com.hitorro.features.Feature;
import com.hitorro.features.extractor.FeatureExtractor;
import com.hitorro.util.core.HTAssert;
import com.hitorro.util.core.opers.HTPredicate;

/**
 *
 */
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
