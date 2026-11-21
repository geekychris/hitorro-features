package com.hitorro.features;

import com.hitorro.features.extractor.ExtractorDefinition;
import com.hitorro.features.extractor.FeatureExtractor;
import com.hitorro.features.extractor.baseextractors.IntFieldExtractor;
import com.hitorro.features.io.normalizer.FloatLogNorm;
import com.hitorro.features.io.normalizer.IntLogNorm;
import com.hitorro.util.json.JsonMappers;
import org.junit.Test;
import tools.jackson.databind.JsonNode;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import static org.junit.Assert.assertEquals;

/**
 * Pins how the configuration properties are read from JSON (well formed, wrongly typed, null and
 * missing values) to what hitorro-features 3.0.2 on Jackson 2 did. The golden file was produced by
 * running the 3.0.2 code on Jackson 2.18 with the same inputs and calls.
 */
public class Jackson2ConfigCompatTest {

    private static final String[] INPUTS = {
        "{}",
        "{\"base\":7,\"class\":\"a.B\",\"root\":\"r\",\"groups\":\"g1,g2\",\"mapping\":\"m1\",\"description\":\"d\",\"inputfields\":\"f1,f2,f3\",\"scratch\":true,\"field\":\"x\"}",
        "{\"base\":\"9\",\"class\":5,\"root\":true,\"groups\":[\"a\",\"b\"],\"description\":null,\"inputfields\":\"\",\"scratch\":\"true\",\"field\":1.5}",
        "{\"base\":3.7,\"class\":{\"x\":1},\"root\":[1],\"groups\":7,\"description\":{},\"inputfields\":[\"p\",\"q\"],\"scratch\":1,\"field\":null}",
        "{\"base\":true,\"scratch\":\"no\",\"inputfields\":null}",
        "{\"base\":null,\"scratch\":0}",
        "{\"base\":\"abc\",\"scratch\":false}",
        "{\"base\":12345678901}"
    };

    private static void p(Map<String, String> out, String k, Callable<Object> c) {
        String v;
        try {
            v = String.valueOf(c.call());
        } catch (Throwable t) {
            v = "!" + t.getClass().getSimpleName();
        }
        out.put(k, v);
    }

    @Test
    public void configPropertiesReadAsOnJackson2() throws Exception {
        Map<String, String> golden = new LinkedHashMap<>();
        try (InputStream in = getClass().getResourceAsStream("jackson2-config-properties.txt")) {
            for (String line : new String(in.readAllBytes(), StandardCharsets.UTF_8).split("\n")) {
                if (line.isEmpty()) continue;
                int eq = line.indexOf('=');
                golden.put(line.substring(0, eq), line.substring(eq + 1));
            }
        }
        Map<String, String> out = new LinkedHashMap<>();
        for (int i = 0; i < INPUTS.length; i++) {
            final JsonNode n = JsonMappers.json().readTree(INPUTS[i]);
            String k = "in" + i + ".";
            p(out, k + "IntLogNorm.base", () -> IntLogNorm.BaseKey.apply(n));
            p(out, k + "FloatLogNorm.base", () -> FloatLogNorm.BaseKey.apply(n));
            p(out, k + "FeatureEngine.class", () -> FeatureEngine.ClassKey.apply(n));
            p(out, k + "Feature.root", () -> Feature.RootFeatureKey.apply(n));
            p(out, k + "Feature.groups", () -> Feature.Groups.apply(n));
            p(out, k + "DerivedFeature.mapping", () -> DerivedFeature.Mappings.apply(n));
            p(out, k + "ExtractorDefinition.description", () -> ExtractorDefinition.Description.apply(n));
            p(out, k + "FeatureExtractor.description", () -> FeatureExtractor.Description.apply(n));
            p(out, k + "FeatureExtractor.inputfields", () -> FeatureExtractor.InputFields.apply(n));
            p(out, k + "FeatureExtractor.scratch", () -> FeatureExtractor.Scratch.apply(n));
            p(out, k + "IntFieldExtractor.field", () -> IntFieldExtractor.SectionKey.apply(n));
            p(out, k + "IntLogNorm.init", () -> { IntLogNorm m = new IntLogNorm(); m.init("x", n); return "ok"; });
            p(out, k + "FloatLogNorm.init", () -> { FloatLogNorm m = new FloatLogNorm(); m.init("x", n); return "ok"; });
        }
        assertEquals(golden.size(), out.size());
        for (Map.Entry<String, String> e : golden.entrySet()) {
            assertEquals(e.getKey(), e.getValue(), out.get(e.getKey()));
        }
    }
}
