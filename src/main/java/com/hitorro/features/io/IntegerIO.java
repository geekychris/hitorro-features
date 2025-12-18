package com.hitorro.features.io;

import com.hitorro.features.io.intf.IntegerIntf;
import com.hitorro.util.io.largedata.compressedstreams.CInputStream;
import com.hitorro.util.io.largedata.compressedstreams.COutputStream;

import java.io.IOException;

/**
 *
 */
public class IntegerIO implements BaseIO<IntegerIO, Integer>, IntegerIntf {
    private int d;

    public IntegerIO cloneIO() {
        IntegerIO o = new IntegerIO();
        o.d = d;
        return o;
    }

    public int getInteger() {
        return d;
    }

    public void setInteger(int i) {
        d = i;
    }

    public void read(CInputStream is) throws IOException {
        d = is.readInt();
    }

    public void write(COutputStream os) throws IOException {
        os.writeInt(d);
    }

    public void setValue(final Integer i) {
        d = i.intValue();
    }

    public void consumeValue(final IntegerIO bio) {
        setValue(bio.d);
    }
}
