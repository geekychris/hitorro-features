package com.hitorro.features.io;

import com.hitorro.features.io.intf.ByteIntf;
import com.hitorro.util.io.largedata.compressedstreams.CInputStream;
import com.hitorro.util.io.largedata.compressedstreams.COutputStream;

import java.io.IOException;

/**
 * .
 */
public class ByteIO implements BaseIO<ByteIO, Byte>, ByteIntf {
    private byte d;

    public ByteIO cloneIO() {
        ByteIO o = new ByteIO();
        o.d = d;
        return o;
    }

    public byte getByte() {
        return d;
    }

    public void setByte(byte s) {
        d = s;
    }

    public void read(CInputStream is) throws IOException {
        d = is.readByte();
    }

    public void write(COutputStream os) throws IOException {
        os.writeByte(d);
    }

    public void setValue(final Byte aByte) {
        d = aByte.byteValue();
    }

    public void consumeValue(final ByteIO bio) {
        setValue(bio.d);
    }
}
