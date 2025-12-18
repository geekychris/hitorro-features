package com.hitorro.features.io.key;

import com.hitorro.features.io.BaseIO;
import com.hitorro.util.io.largedata.compressedstreams.CInputStream;
import com.hitorro.util.io.largedata.compressedstreams.COutputStream;

import java.io.IOException;

/**
 * .
 */
public abstract class Key<T extends BaseIO> {
    protected T io;

    public T getBIO() {
        return io;
    }

    public void setBIO(T io) {
        this.io = io;
    }

    public abstract void write(COutputStream os) throws IOException;

    public abstract boolean read(CInputStream is) throws IOException;

    public abstract void close(COutputStream os) throws IOException;
}
