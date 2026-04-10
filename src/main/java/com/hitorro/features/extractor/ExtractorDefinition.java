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
package com.hitorro.features.extractor;

import com.fasterxml.jackson.databind.JsonNode;
import com.hitorro.util.core.classes.ClassUtil;
import com.hitorro.util.core.error.ErrorCode;
import com.hitorro.util.json.keys.ClassProperty;
import com.hitorro.util.json.keys.StringProperty;

import java.util.List;


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
