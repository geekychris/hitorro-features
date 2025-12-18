package com.hitorro.features.index;

import com.hitorro.features.Feature;
import com.hitorro.features.FeatureSetKey;
import com.hitorro.features.Log;
import com.hitorro.util.io.largedata.compressedstreams.CInputStream;

import java.io.IOException;

/**
 * User: chris
 */
public class PostingsFeatureReader extends FeatureReader {
    protected CInputStream is;
    protected FeatureSetKey fsk;
    protected long offset;
    protected String name;

    public PostingsFeatureReader() {

    }

    public void close() throws IOException {
        is.close();
    }

    public boolean set(CInputStream is) {
        this.is = is;
        return true;
    }

    public boolean set(Feature feature, long offset, FeatureSetKey f, String name) {
        bio = feature.getDataType().getCopyOfIO();
        this.offset = offset;
        fsk = f;
        this.name = name;
        bio = fsk.getFeature().getDataType().getCopyOfIO();
        lk.setBIO(bio);
        return reset();
    }

    public String toString() {
        return name;
    }

    public boolean reset() {
        try {
            is.seek(offset);
            lk.setKey(0);
        } catch (IOException e) {
            Log.feature.error("Exception %s %e", e, e);
            return false;
        }
        return true;
    }

    public boolean next() throws IOException {
        if (lk.getKey() == Long.MIN_VALUE) {
            return false;
        }
        boolean b = lk.read(is);
        return b;
    }
}
