package com.hitorro.features.io;

import com.hitorro.features.io.intf.LongIntf;
import com.hitorro.util.io.largedata.compressedstreams.CInputStream;
import com.hitorro.util.io.largedata.compressedstreams.COutputStream;

import java.io.IOException;

/**
 *
 */
public class LongIO implements BaseIO<LongIO, Long>, LongIntf {
    private long d;

    public LongIO cloneIO() {
        LongIO o = new LongIO();
        o.d = d;
        return o;
    }

    public long getLong() {
        return d;
    }

    public void setLong(long l) {
        d = l;
    }

    public void read(CInputStream is) throws IOException {
        d = is.readLong();
    }

    public void write(COutputStream os) throws IOException {
        os.writeLong(d);
    }

    public void setValue(final Long aLong) {
        d = aLong.longValue();
    }

    public void consumeValue(final LongIO bio) {
        setValue(bio.d);
    }
}
