package com.hitorro.features.io.key;

import com.hitorro.features.io.BaseIO;
import com.hitorro.util.io.largedata.compressedstreams.CInputStream;
import com.hitorro.util.io.largedata.compressedstreams.COutputStream;

import java.io.IOException;

/**
 *
 */
public class IntKey<T extends BaseIO> extends Key<T> {
    public int key;

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void write(final COutputStream os) throws IOException {
        os.writeVInt(key);
        io.write(os);
    }

    public boolean read(final CInputStream is) throws IOException {
        key = is.readVInt();
        if (key == Integer.MIN_VALUE) {
            return false;
        }
        io.read(is);
        return true;
    }

    public void close(final COutputStream os) throws IOException {
        os.writeVInt(Integer.MIN_VALUE);
        os.close();
    }
}
