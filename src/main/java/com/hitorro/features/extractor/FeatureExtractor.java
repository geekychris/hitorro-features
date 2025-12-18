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
import com.hitorro.features.Feature;
import com.hitorro.util.json.keys.BooleanProperty;
import com.hitorro.util.json.keys.StringListFromDelimitedKey;
import com.hitorro.util.json.keys.StringProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Base implementation of an extractor.  Subclass this for a real extractor.
 */
public abstract class FeatureExtractor<O> {
    public static final StringProperty Description = new StringProperty("description", "", null);
    public static final StringListFromDelimitedKey InputFields = new StringListFromDelimitedKey("inputfields", "", ",", new ArrayList());
    public static final BooleanProperty Scratch = new BooleanProperty("scratch", "", false);
    protected JsonNode props;
    protected boolean isScratch;
    protected String[] inputFields;
    protected String featureName;
    protected String description;
    protected ExtractorDefinition ed;
    protected Feature feature;


    /**
     * Typically this is ONE, to save on computation consider sharing a Resetable compContext object
     */
    public String getFeatureName() {
        return featureName;
    }

    /**
     * The apply passed in here is a composite of the extractor definition and the extractor (extractors takes
     * presidence)
     *
     * @param props
     * @param ed
     * @return
     */
    public boolean init(JsonNode props, ExtractorDefinition ed, String featureName, Feature feature) {
        this.props = props;
        this.ed = ed;
        this.featureName = featureName;
        this.feature = feature;
        setScratch(Scratch.apply(props));
        List<String> f = InputFields.apply(props);
        setInputFields(f.toArray(new String[f.size()]));
        setDescription(Description.apply(props));

        return true;
    }

    /**
     * Feature name of: GROUP_FAMILY_ALGORITHM_XX Group : Static, Dynamic FAMILY: such things as TEXT
     *
     * @return
     */

    public abstract int extract(O object, Results results, ExtractionContext ec);

    public ExtractorDefinition getExtractorDefinition() {
        return ed;
    }

    public boolean isScratch() {
        return isScratch;
    }

    public void setScratch(final boolean scratch) {
        isScratch = scratch;
    }

    public String[] getInputFields() {
        return inputFields;
    }

    public void setInputFields(final String[] inputFields) {
        this.inputFields = inputFields;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }
}
