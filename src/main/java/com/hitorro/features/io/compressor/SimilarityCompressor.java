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
