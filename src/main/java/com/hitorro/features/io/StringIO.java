package com.hitorro.features.io;

import com.hitorro.features.io.intf.StringIntf;
import com.hitorro.util.io.largedata.compressedstreams.CInputStream;
import com.hitorro.util.io.largedata.compressedstreams.COutputStream;

import java.io.IOException;

/**
 *
 */
public class StringIO implements BaseIO<StringIO, String>, StringIntf {
    private String d;

    public String getString() {
        return d;
    }

    public void setString(String s) {
        d = s;
    }

    public StringIO cloneIO() {
        StringIO o = new StringIO();
        o.d = d;
        return o;
    }

    public void read(CInputStream is) throws IOException {
        d = is.readString();
    }

    public void write(COutputStream os) throws IOException {
        os.writeString(d);
    }

    public void setValue(final String s) {
        d = s;
    }

    public void consumeValue(final StringIO bio) {
        setValue(bio.d);
    }
}

