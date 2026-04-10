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
package com.hitorro.features;

import com.fasterxml.jackson.databind.JsonNode;
import com.hitorro.features.extractor.ExtractorManager;
import com.hitorro.features.io.MappingFunction;
import com.hitorro.util.core.error.ErrorCode;
import com.hitorro.util.core.string.StringUtil;
import com.hitorro.util.json.keys.StringListFromDelimitedKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class DerivedFeature extends Feature {
    public static final StringListFromDelimitedKey Mappings = new StringListFromDelimitedKey("mapping", "", ",", new ArrayList());

    private MappingFunction[] mappingFunctions;


    public boolean init(List<ErrorCode> errList,
                        String propsPath,
                        JsonNode map,
                        FeatureEngine featureEngine,
                        ExtractorManager extractorEngine) {
        super.init(errList, propsPath, map, featureEngine, extractorEngine);
        this.rootFeatureName = StringUtil.toLowerCaseIfNotNull(RootKey.apply(map));
        List<String> mapperNames = Mappings.apply(map);
        mappingFunctions = new MappingFunction[mapperNames.size()];

        for (int i = 0; i < mapperNames.size(); i++) {
            MappingFunction mf = featureEngine.getMappingFunction(mapperNames.get(i));
            if (mf == null) {
                ErrorCode.addErrorTrue(true, errList, 1, "No mapping function defined for %s", mapperNames.get(i));
                continue;
            }
            mappingFunctions[i] = mf.cloneMapper();
            if (i > 0) {
                ErrorCode.addErrorTrue(mappingFunctions[i - 1].outputFormat() != mappingFunctions[i].inputFormat(),
                        errList, 1, "me :%s outputType %s != me %s inputType %s",
                        mapperNames.get(i - 1), mappingFunctions[i - 1].outputFormat().toString(),
                        mapperNames.get(i), mappingFunctions[i].inputFormat().toString());
            }
        }

        int errorCount = errList.size();
        ErrorCode.addErrorTrue(StringUtil.nullOrEmptyString(rootFeatureName), errList, ErrNoName,
                "Root name not provided for feature %s", propsPath);

        // report of the addition of errors only
        return errorCount == errList.size();
    }

    /**
     * Look for listChildren or parents depending on relationship so that at usage time it has all dependencies.
     *
     * @param errList
     * @param features
     * @return
     */
    public boolean finalize(List<ErrorCode> errList, Map<String, Feature> features) {
        this.rootFeature = features.get(rootFeatureName);
        int errorCount = errList.size();
        ErrorCode.addErrorTrue(rootFeature == null, errList, ErrNoRootFeature,
                "Derived Feature %s could not find its parent feature %s", name, rootFeatureName);

        if (mappingFunctions.length == 0) {
            ErrorCode.addErrorTrue(true, errList, 1, "Feature %s needs at least one mapping function", name);
            return false;
        }
        ErrorCode.addErrorTrue(rootFeature.dataType != mappingFunctions[0].inputFormat(),
                errList, 1, "feature :%s outputType %s != me %s inputType %s",
                rootFeatureName, rootFeature.dataType.toString(),
                mappingFunctions[0].getName(), mappingFunctions[0].inputFormat().toString());
        ErrorCode.addErrorTrue(dataType != mappingFunctions[mappingFunctions.length - 1].outputFormat(),
                errList, 1, "derived feature :%s inputType %s != me %s outputType %s",
                rootFeatureName, dataType.toString(),
                mappingFunctions[mappingFunctions.length - 1].getName(),
                mappingFunctions[mappingFunctions.length - 1].outputFormat().toString());

        return errorCount == errList.size();
    }

    public MappingFunction[] getMappingFunctions() {
        return mappingFunctions;
    }

    public void setMappingFunctions(MappingFunction[] mappingFunctions) {
        this.mappingFunctions = mappingFunctions;
    }

    public boolean isDerived() {
        return true;
    }
}
