/*
 * Copyright (c) 2006-2025 Chris Collins
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
