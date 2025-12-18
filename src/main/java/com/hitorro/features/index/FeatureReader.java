package com.hitorro.features.index;

import com.hitorro.features.io.BaseIO;
import com.hitorro.features.io.key.LongKey;

import java.io.IOException;

/**
 * User: chris
 */
public abstract class FeatureReader {
    protected LongKey lk = new LongKey();
    protected BaseIO bio;

    public void setBio(BaseIO in) {
        bio = in;
        lk.setBIO(bio);
    }

    public LongKey getLongKey() {
        return lk;
    }

    public BaseIO getBaseIO() {
        return bio;
    }

    public abstract boolean reset() throws IOException;

    public abstract boolean next() throws IOException;

    public BaseIO getBIO() {
        return bio;
    }
}
