package com.hitorro.features.io.normalizer;

import com.fasterxml.jackson.databind.JsonNode;
import com.hitorro.features.DataType;
import com.hitorro.features.io.FloatIO;
import com.hitorro.features.io.MappingFunction;
import com.hitorro.features.io.intf.FloatIntf;
import com.hitorro.util.io.largedata.compressedstreams.CInputStream;
import com.hitorro.util.io.largedata.compressedstreams.COutputStream;
import com.hitorro.util.json.keys.IntegerProperty;

import java.io.IOException;

/**
 * User: chris
 */
public class FloatLogNorm extends FloatIO implements FloatIntf, MappingFunction<FloatIO, FloatIO> {
    public static final IntegerProperty BaseKey = new IntegerProperty("base", "The logarithm base used for normalization uses", 15);
    private FloatIO out;
    private String name;
    private double logV = 1 / Math.log(16);

    public void setFloat(float f) {

        // convertToPdf log (1 + value) to desired base
        double logValue = logV * Math.log(1 + f);
        float score = (float) (1 - (1 / (1 + logValue)));

        out.setFloat(score);
    }

    public void init(String name, final JsonNode props) {
        int logBase = BaseKey.apply(props);
        logV = 1 / Math.log(logBase);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setNext(final FloatIO baseIO) {
        out = baseIO;
    }

    public DataType inputFormat() {
        return DataType.floatType;
    }

    public DataType outputFormat() {
        return DataType.floatType;
    }

    public MappingFunction cloneMapper() {
        FloatLogNorm fln = new FloatLogNorm();
        fln.logV = logV;
        return fln;
    }

    public FloatIO cloneIO() {
        FloatLogNorm fln = new FloatLogNorm();
        fln.logV = logV;
        return fln;
    }

    public void read(final CInputStream is) throws IOException {
        setFloat(is.readFloat());
    }

    public void write(final COutputStream os) {
    }

    public void setValue(final Float aFloat) {
        setFloat(aFloat.floatValue());
    }
}

