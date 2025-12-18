package com.hitorro.features.extractor;

import com.hitorro.features.Feature;
import com.hitorro.features.FeatureEngine;
import com.hitorro.features.extractor.operators.MatchDefinitionGroup;
import com.hitorro.jsontypesystem.JVS;
import com.hitorro.util.core.error.ErrorCode;
import com.hitorro.util.core.opers.HTPredicate;
import com.hitorro.util.core.params.JsonKeyMap;
import com.hitorro.util.core.string.StringUtil;
import com.hitorro.util.json.keys.propaccess.PropaccessError;
import com.hitorro.util.typesystem.BaseFields;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Keep track of all the extractor definitions
 */
public class ExtractorManager {

    private Map<String, ExtractorDefinition> defs = new TreeMap();
    private List<ExtractorDefinition> defsList = new ArrayList();
    private FeatureEngine fe;

    public void init(List<ErrorCode> codes, JVS map, FeatureEngine fe) throws PropaccessError {
        this.fe = fe;
        List<JsonKeyMap> extractors = map.getSubMaps("extractors");
        for (JsonKeyMap km : extractors) {
            ExtractorDefinition ed = new ExtractorDefinition();
            ed.init(codes, km.getKey().toLowerCase(), km.getValue());
            defs.put(ed.getName(), ed);
            defsList.add(ed);
        }
    }

    /**
     * get the extraction set for a given group.  At this point you must know the correct BaseFields impl to get such
     * things as id and created time out of the object.
     *
     * @param group
     * @param bf
     * @return
     */
    public ExtractionSet getExtractionSet(String group, BaseFields bf) {
        MatchDefinitionGroup op = new MatchDefinitionGroup(group);
        List<Feature> feats = fe.getFeaturesByConstraint(op);
        ExtractionSet set = new ExtractionSet(bf, feats);
        return set;
    }

    public ExtractorDefinition getDefinition(String def) {
        if (StringUtil.nullOrEmptyString(def)) {
            return null;
        }
        return defs.get(def.toLowerCase());
    }

    public List<ExtractorDefinition> getMatching(HTPredicate<ExtractorDefinition> oper) {
        List<ExtractorDefinition> l = new ArrayList();
        getMatching(oper, l);
        return l;
    }

    public int getMatching(HTPredicate<ExtractorDefinition> oper, List<ExtractorDefinition> list) {
        int count = 0;
        for (ExtractorDefinition ed : defsList) {
            if (oper.test(ed)) {
                count++;
                list.add(ed);
            }
        }
        return count;
    }
}
