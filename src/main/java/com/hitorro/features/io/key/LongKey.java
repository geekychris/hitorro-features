package com.hitorro.features.io.key;

import com.hitorro.features.io.BaseIO;
import com.hitorro.util.io.largedata.compressedstreams.CInputStream;
import com.hitorro.util.io.largedata.compressedstreams.COutputStream;

import java.io.IOException;

/**
 *
 */
public class LongKey<T extends BaseIO> extends Key<T> {
    public long key;

    public long getKey() {
        return key;
    }

    public void setKey(long key) {
        this.key = key;
    }

    public void write(final COutputStream os) throws IOException {
        os.writeLong(key);
        io.write(os);
    }

    public boolean read(final CInputStream is) throws IOException {
        key = is.readLong();
        if (key == Long.MIN_VALUE) {
            return false;
        }
        io.read(is);
        return true;
    }

    public void close(final COutputStream os) throws IOException {
        closeMarker(os);
        os.close();
    }

    public void closeMarker(final COutputStream os) throws IOException {
        os.writeLong(Long.MIN_VALUE);
    }
}
