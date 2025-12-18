package com.hitorro.features.io;

import com.hitorro.features.io.intf.DoubleIntf;
import com.hitorro.util.io.largedata.compressedstreams.CInputStream;
import com.hitorro.util.io.largedata.compressedstreams.COutputStream;

import java.io.IOException;

/**
 *
 */
public class DoubleIO implements BaseIO<DoubleIO, Double>, DoubleIntf {
    private double d;

    public DoubleIO cloneIO() {
        DoubleIO o = new DoubleIO();
        o.d = d;
        return o;
    }

    public double getDouble() {
        return d;
    }

    public void setDouble(double s) {
        d = s;
    }

    public void read(CInputStream is) throws IOException {
        d = is.readDouble();
    }

    public void write(COutputStream os) throws IOException {
        os.writeDouble(d);
    }

    public void setValue(final Double aDouble) {
        d = aDouble.doubleValue();
    }

    public void consumeValue(final DoubleIO bio) {
        setValue(bio.d);
    }
}
