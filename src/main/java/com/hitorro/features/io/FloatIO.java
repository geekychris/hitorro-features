package com.hitorro.features.io;

import com.hitorro.features.io.intf.FloatIntf;
import com.hitorro.util.io.largedata.compressedstreams.CInputStream;
import com.hitorro.util.io.largedata.compressedstreams.COutputStream;

import java.io.IOException;

/**
 *
 */
public class FloatIO implements BaseIO<FloatIO, Float>, FloatIntf {
    private float d;

    public FloatIO cloneIO() {
        FloatIO o = new FloatIO();
        o.d = d;
        return o;
    }

    public float getFloat() {
        return d;
    }

    public void setFloat(float f) {
        d = f;
    }

    public void read(CInputStream is) throws IOException {
        d = is.readFloat();
    }

    public void write(COutputStream os) throws IOException {
        os.writeFloat(d);
    }

    public void setValue(final Float aFloat) {
        d = aFloat.floatValue();
    }

    public void consumeValue(final FloatIO bio) {
        setValue(bio.d);
    }
}
