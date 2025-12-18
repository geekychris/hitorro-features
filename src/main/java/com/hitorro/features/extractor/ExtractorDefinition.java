package com.hitorro.features.extractor;

import com.fasterxml.jackson.databind.JsonNode;
import com.hitorro.util.core.classes.ClassUtil;
import com.hitorro.util.core.error.ErrorCode;
import com.hitorro.util.json.keys.ClassProperty;
import com.hitorro.util.json.keys.StringProperty;

import java.util.List;

/**
 *
 */
public class ExtractorDefinition {
    public static final int NoImplEC = 1;
    public static final int NotFeatureExtractorClasslEC = 2;
    public static final StringProperty Description = new StringProperty("description", "", null);

    public static final ClassProperty Impl = new ClassProperty("class", "", null);

    private String name;
    private String description;
    private Class impl;
    private JsonNode props;

    public void init(List<ErrorCode> codes, String name, JsonNode map) {
        this.setName(name);
        setDescription(Description.apply(map));
        setImpl(Impl.apply(map));

        ErrorCode.addErrorTrue(name == null, codes, NoImplEC, "Extractor Definition name not provided", name);
        if (getImpl() == null) {
            ErrorCode.addErrorTrue(true, codes, NoImplEC, "No implementation for extractor %s", name);
        } else {
            ErrorCode.addErrorTrue(!ClassUtil.isSubClass(getImpl(), FeatureExtractor.class), codes,
                    NotFeatureExtractorClasslEC, "In %s not FE class: %s", name, ClassUtil.getBareName(getImpl()));
        }
        setProps(map.get("props"));
    }

    public FeatureExtractor getInstance() {
        return (FeatureExtractor) ClassUtil.getInstanceSwallowError(impl, FeatureExtractor.class);
    }


    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Class getImpl() {
        return impl;
    }

    public void setImpl(final Class impl) {
        this.impl = impl;
    }

    public JsonNode getProps() {
        return props;
    }

    public void setProps(final JsonNode props) {
        this.props = props;
    }
}
