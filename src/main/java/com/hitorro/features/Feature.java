package com.hitorro.features;

import com.fasterxml.jackson.databind.JsonNode;
import com.hitorro.features.extractor.ExtractorDefinition;
import com.hitorro.features.extractor.ExtractorManager;
import com.hitorro.features.extractor.FeatureExtractor;
import com.hitorro.jsontypesystem.JVS;
import com.hitorro.util.core.error.ErrorCode;
import com.hitorro.util.core.hash.FPHash64;
import com.hitorro.util.core.string.StringUtil;
import com.hitorro.util.json.JSONUtil;
import com.hitorro.util.json.keys.EnumProperty;
import com.hitorro.util.json.keys.StringListFromDelimitedKey;
import com.hitorro.util.json.keys.StringProperty;
import com.hitorro.util.json.keys.ValidatedStringProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Feature defines all the meta information about what a feature is all about.
 */
public class Feature {
    public static final ValidatedStringProperty FeatureTypeKey =
            new ValidatedStringProperty("ftype", "Feature type",
                    FeatureType.getDefault().getName(), FeatureType.getValidValues());
    public static final StringProperty NameKey =
            new StringProperty("name", "", null);
    public static final StringProperty RootKey =
            new StringProperty("rootname", "", null);
    public static final EnumProperty<DataType> DataTypeKey = new EnumProperty<DataType>("type", "", null, DataType.context);
    public static final EnumProperty<FeatureCardinality> CardinalityKey = new EnumProperty<FeatureCardinality>("cardinality", "", FeatureCardinality.single, FeatureCardinality.context);
    public static final EnumProperty<FeatureKeyType> KeyTypeKey = new EnumProperty<FeatureKeyType>("keytype", "", FeatureKeyType.document, FeatureKeyType.context);
    public static final EnumProperty<PartitionModel> SegmentationKey = new EnumProperty<PartitionModel>("segmentation", "", PartitionModel.None, PartitionModel.context) {
        public String getName(DataType e) {
            return e.getName().toLowerCase();
        }
    };
    // keys for derived
    public static final StringProperty RootFeatureKey = new StringProperty("root", "", null);
    public static final StringListFromDelimitedKey Groups = new StringListFromDelimitedKey("groups", "", ",", new ArrayList());
    public static int ErrNoName = 1;
    public static int ErrNoCardinality = 2;
    public static int ErrNoDataType = 3;
    public static int ErrNoSegmentationModel = 4;
    public static int ErrNoRootFeature = 5;
    public static int ErrNoKeyTypeModel = 6;
    public static int ErrNoExtractorName = 20;
    public static int ErrNoExtractorDefinitionName = 21;
    public static int ErrNoExtractorImplName = 22;
    protected String name;
    protected DerivedFeature[] derived;
    protected DataType dataType;
    protected PartitionModel model;
    protected FeatureCardinality cardinality;
    protected FeatureType featureType = FeatureType.base;
    protected Feature rootFeature;
    protected String rootFeatureName;
    protected long featHash;
    protected FeatureExtractor fe;
    protected FeatureKeyType keyType;
    protected String nullValueString = null;
    private String[] groups;

    /**
     * Factory method to get the correct type of feature
     *
     * @param errList
     * @param propsPath
     * @param map       derived from the properties system or perhaps over the wire
     * @return feature or null if could not
     */
    public static Feature getFeature(List<ErrorCode> errList, String propsPath,
                                     JsonNode map, FeatureEngine fe, ExtractorManager extractorEngine) {
        FeatureType ft = FeatureType.getByName(FeatureTypeKey.apply(map));
        if (ft != null) {
            Feature feat = ft.getFeatureInstance();
            if (feat.init(errList, propsPath, map, fe, extractorEngine)) {
                // only return the feature if valid
                return feat;
            }

        }
        return null;
    }

    public Object getNullValue() {
        return dataType.getNullValue(nullValueString);
    }

    public FeatureExtractor getFeatureExtractor() {
        return fe;
    }

    public String getRootFeatureName() {
        return rootFeatureName;
    }

    public void setRootFeatureName(String f) {
        this.rootFeatureName = f;
    }

    public Feature getRootFeature() {
        return rootFeature;
    }

    public void setRootFeature(Feature f) {
        this.rootFeature = f;
    }

    public FeatureType getFeatureType() {
        return featureType;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public long getFeatureHash() {
        return featHash;
    }

    public FeatureKeyType getKeyType() {
        return keyType;
    }

    public boolean init(List<ErrorCode> errList,
                        String propsPath,
                        JsonNode map,
                        FeatureEngine featureEngine,
                        ExtractorManager extractorEngine) {
        name = StringUtil.toLowerCaseIfNotNull(NameKey.apply(map));
        if (name != null) {
            featHash = FPHash64.getFP(name);
        }
        cardinality = CardinalityKey.apply(map);
        dataType = DataTypeKey.apply(map);
        model = SegmentationKey.apply(map);
        keyType = KeyTypeKey.apply(map);
        int errorCount = errList.size();
        ErrorCode.addErrorTrue(StringUtil.nullOrEmptyString(name), errList, ErrNoName, "Name not provided for feature %s", propsPath);
        ErrorCode.addErrorTrue(cardinality == null, errList, ErrNoCardinality, "Cardinality not set for %s", name);
        ErrorCode.addErrorTrue(dataType == null, errList, ErrNoDataType, "DataType not set for %s", name);
        ErrorCode.addErrorTrue(model == null, errList, ErrNoSegmentationModel, "Model not set for %s", name);
        ErrorCode.addErrorTrue(keyType == null, errList, ErrNoKeyTypeModel, "No key type for %s", name);


        JsonNode extMap = map.get("extractor");
        if (!JSONUtil.nullOrEmpty(extMap)) {
            String extName = NameKey.apply(extMap);
            if (StringUtil.nullOrEmptyString(extName)) {
                ErrorCode.addErrorTrue(true, errList, ErrNoExtractorName, "No extractor name provided for %s", name);
            } else {
                ExtractorDefinition fed = extractorEngine.getDefinition(extName);
                List<String> gTmp = Groups.apply(extMap);
                groups = gTmp.toArray(new String[gTmp.size()]);

                if (fed == null) {

                    ErrorCode.addErrorTrue(true, errList, ErrNoExtractorDefinitionName,
                            "No extractor def for name provided for %s/%s", name, extName);
                } else {
                    FeatureExtractor fe = fed.getInstance();
                    if (fe == null) {

                        ErrorCode.addErrorTrue(true, errList, ErrNoExtractorImplName,
                                "No extractor instance for name provided for %s/%s", name, extName);
                    } else {
                        JVS jvs = new JVS();
                        jvs.merge(new JVS(fed.getProps()));
                        jvs.merge(new JVS(map));

                        fe.init(jvs.getJsonNode(), fed, name, this);
                        this.fe = fe;
                    }
                }
            }
        }

        // report of the addition of errors only
        return errorCount == errList.size();
    }

    public String[] getExtractorGroups() {
        return groups;
    }

    public void setExtractorGroups(final String[] groups) {
        this.groups = groups;
    }

    /**
     * Look for listChildren or parents depending on relationship so that at usage time it has all dependencies.
     *
     * @param errList
     * @param features
     * @return
     */
    public boolean finalize(List<ErrorCode> errList, Map<String, Feature> features) {
        if (featureType.getShouldLookForChildren()) {
            List<Feature> depsOn = new ArrayList();
            for (Feature feat : features.values()) {
                String rfn = feat.getRootFeatureName();
                if (!StringUtil.nullOrEmptyString(rfn)) {
                    if (name.equals(rfn)) {
                        depsOn.add(feat);
                    }
                }
            }
            DerivedFeature feats[] = new DerivedFeature[depsOn.size()];
            for (int i = 0; i < feats.length; i++) {
                feats[i] = (DerivedFeature) depsOn.get(i);
            }
        }
        return true;
    }

    public FeatureCardinality getFeatureCardinality() {
        return cardinality;
    }

    public void setFeatureCardinality(FeatureCardinality cardinality) {
        this.cardinality = cardinality;
    }

    public void setSegmentationModel(PartitionModel model) {
        this.model = model;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DerivedFeature[] getDerivedFeatures() {
        return derived;
    }

    public void setDerivedFeatures(DerivedFeature[] der) {
        this.derived = der;
    }

    public boolean isDerived() {
        return false;
    }

}
