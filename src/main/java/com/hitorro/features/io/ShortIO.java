package com.hitorro.features.io;

import com.hitorro.features.io.intf.ShortIntf;
import com.hitorro.util.io.largedata.compressedstreams.CInputStream;
import com.hitorro.util.io.largedata.compressedstreams.COutputStream;

import java.io.IOException;

/**
 *
 */
public class ShortIO implements BaseIO<ShortIO, Short>, ShortIntf {
    private short d;

    public short getShort() {
        return d;
    }

    public void setShort(short s) {
        d = s;
    }

    public ShortIO cloneIO() {
        ShortIO o = new ShortIO();
        o.d = d;
        return o;
    }

    public void read(CInputStream is) throws IOException {
        d = is.readShort();
    }

    public void write(COutputStream os) throws IOException {
        os.writeShort(d);
    }

    public void setValue(final Short aShort) {
        d = aShort.shortValue();
    }

    public void consumeValue(final ShortIO bio) {
        setValue(bio.d);
    }
}
