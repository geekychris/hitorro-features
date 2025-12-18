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
package com.hitorro.features.io.compressor;

import com.fasterxml.jackson.databind.JsonNode;
import com.hitorro.features.DataType;
import com.hitorro.features.io.ByteIO;
import com.hitorro.features.io.FloatIO;
import com.hitorro.features.io.MappingFunction;
import com.hitorro.features.io.intf.FloatIntf;
import com.hitorro.util.io.largedata.compressedstreams.CInputStream;
import com.hitorro.util.io.largedata.compressedstreams.COutputStream;
import org.apache.lucene.util.SmallFloat;

import java.io.IOException;

/**
 * .
 */
public class SimilarityCompressor extends FloatIO implements FloatIntf, MappingFunction<FloatIO, ByteIO> {
    private ByteIO out;
    private String name;

    public float getFloat() {
        return 0;
    }

    public void setFloat(float f) {
        out.setByte(SmallFloat.floatToByte315(f));
    }

    public void init(String name, final JsonNode props) {
        // dont need any init.
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setNext(final ByteIO baseIO) {
        out = baseIO;
    }

    public DataType inputFormat() {
        return DataType.floatType;
    }

    public DataType outputFormat() {
        return DataType.byteType;
    }

    public MappingFunction cloneMapper() {
        SimilarityCompressor sc = new SimilarityCompressor();

        return sc;
    }

    public FloatIO cloneIO() {
        SimilarityCompressor sc = new SimilarityCompressor();
        return sc;
    }

    public void read(final CInputStream is) throws IOException {
        setFloat(is.readFloat());
    }

    public void write(final COutputStream os) {
    }

    public void setValue(final Float o) {
        setFloat(o.floatValue());
    }
}
