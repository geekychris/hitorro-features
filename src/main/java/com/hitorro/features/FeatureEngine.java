package com.hitorro.features;

import com.fasterxml.jackson.databind.JsonNode;
import gnu.trove.map.hash.TLongObjectHashMap;
import com.hitorro.features.extractor.ExtractorManager;
import com.hitorro.features.io.MappingFunction;
import com.hitorro.jsontypesystem.JVS;
import com.hitorro.util.core.classes.ClassUtil;
import com.hitorro.util.core.error.ErrorCode;
import com.hitorro.util.core.opers.HTPredicate;
import com.hitorro.util.core.params.JsonKeyMap;
import com.hitorro.util.json.keys.StringProperty;
import com.hitorro.util.json.keys.propaccess.PropaccessError;

import java.util.*;

/**
 *
 */
public class FeatureEngine {
    public static final String RootKey = "features";
    public static final String MappersKey = "mappers";

    public static StringProperty ClassKey = new StringProperty("class", "", null);

    private static FeatureEngine fe = null;
    private Map<String, Feature> features = new TreeMap();
    private TLongObjectHashMap<Feature> featByHash = new TLongObjectHashMap();
    private List<Feature> featList = new ArrayList();
    private ExtractorManager extractorEngine;

    private Map<String, MappingFunction> mappingFunctionsMap = new HashMap();

    public static FeatureEngine getEngine() {
        return fe;
    }

    public static void setEngine(FeatureEngine f) {
        fe = f;
    }

    public MappingFunction getMappingFunction(String name) {
        return mappingFunctionsMap.get(name);
    }

    public void init(List<ErrorCode> codes, JVS root) throws PropaccessError {
        try {
            initMappers(codes, root);
        } catch (PropaccessError propaccessError) {

        }
        configureExtractors(codes, root);
        List<JsonKeyMap> keys = root.getSubMaps(RootKey);
        for (JsonKeyMap m : keys) {
            Feature f = Feature.getFeature(codes, m.getKey(), m.getValue(), this, extractorEngine);
            if (f != null) {
                addFeature(f);
            }
        }
        for (Feature f : features.values()) {
            f.finalize(codes, features);
        }
    }

    private void initMappers(List<ErrorCode> codes, JVS root) throws PropaccessError {

        List<JsonKeyMap> keys = root.getSubMaps(MappersKey);
        for (JsonKeyMap m : keys) {
            JsonNode map = m.getValue();
            String clazz = ClassKey.apply(map);
            JsonNode initMap = map.get("props");
            MappingFunction mf = (MappingFunction) ClassUtil.getInstanceSwallowError(clazz, MappingFunction.class);
            if (mf == null) {
                ErrorCode.addErrorTrue(true, codes, 1, "Unable to construct mapping function %s, class: %s", m.getKey(), clazz);
                continue;
            }
            mf.init(m.getKey(), initMap);
            mappingFunctionsMap.put(m.getKey(), mf);
        }
    }

    private void configureExtractors(final List<ErrorCode> codes, final JVS root) throws PropaccessError {
        extractorEngine = new ExtractorManager();
        extractorEngine.init(codes, root, this);
    }

    public ExtractorManager getExtractorManager() {
        return extractorEngine;
    }

    public Feature addFeature(Feature f) {
        Feature fOut = features.put(f.getName(), f);
        featByHash.put(f.getFeatureHash(), f);
        featList.add(f);
        return fOut;
    }

    public Feature getFeature(String name) {
        return features.get(name.toLowerCase());
    }

    public Feature getFeature(long hash) {
        return featByHash.get(hash);
    }

    public List<Feature> getFeaturesByConstraint(HTPredicate<Feature> oper) {
        List<Feature> list = new ArrayList();
        getFeaturesByConstraint(oper, list);
        return list;
    }

    public int getFeaturesByConstraint(HTPredicate<Feature> oper, List<Feature> list) {
        int counter = 0;

        for (Feature f : featList) {
            if (oper.test(f)) {
                list.add(f);
                counter++;
            }
        }
        return counter;
    }
}
