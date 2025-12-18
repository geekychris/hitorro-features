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
package com.hitorro.features.io.normalizer;

import com.fasterxml.jackson.databind.JsonNode;
import com.hitorro.features.DataType;
import com.hitorro.features.io.FloatIO;
import com.hitorro.features.io.IntegerIO;
import com.hitorro.features.io.MappingFunction;
import com.hitorro.features.io.intf.IntegerIntf;
import com.hitorro.util.io.largedata.compressedstreams.CInputStream;
import com.hitorro.util.io.largedata.compressedstreams.COutputStream;
import com.hitorro.util.json.keys.IntegerProperty;

import java.io.IOException;

/**
 * User: chris
 */
public class IntLogNorm extends IntegerIO implements IntegerIntf, MappingFunction<IntegerIO, FloatIO> {
    public static final IntegerProperty BaseKey = new IntegerProperty("base", "The logarithm base used for normalization uses", 15);
    private FloatIO out;
    private String name;
    private double logV = 1 / Math.log(16);

    public void setInteger(int f) {
        double logValue = logV * Math.log(1 + f);
        float score = (float) (1 - (1 / (1 + logValue)));

        out.setFloat(score);
    }

    public float getFloat() {
        return 0;
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
        return DataType.integerType;
    }

    public DataType outputFormat() {
        return DataType.floatType;
    }

    public MappingFunction cloneMapper() {
        IntLogNorm fln = new IntLogNorm();
        fln.logV = logV;
        return fln;
    }

    public IntegerIO cloneIO() {
        IntLogNorm fln = new IntLogNorm();
        fln.logV = logV;
        return fln;
    }

    public void read(final CInputStream is) throws IOException {
        setInteger(is.readInt());
    }

    public void write(final COutputStream os) {
    }

    public void setValue(final Integer i) {
        setInteger(i.intValue());
    }
}
