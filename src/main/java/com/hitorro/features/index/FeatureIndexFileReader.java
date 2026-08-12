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

import gnu.trove.map.hash.TObjectLongHashMap;
import com.hitorro.features.Feature;
import com.hitorro.features.FeatureSetKey;
import com.hitorro.util.basefile.fs.BaseFile;
import com.hitorro.util.core.ArrayUtil;
import com.hitorro.util.core.string.StringUtil;
import com.hitorro.util.io.largedata.compressedstreams.CInputStream;
import com.hitorro.util.io.largedata.compressedstreams.FSInputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * User: chris
 */
public class FeatureIndexFileReader extends FeatureIndexReader implements Comparable<FeatureIndexFileReader> {
    private BaseFile postingFile;
    private BaseFile dictionaryFile;
    private TObjectLongHashMap<FeatureSetKey> map = new TObjectLongHashMap();
    private List<FeatureSetKey> keys = new ArrayList();
    private long generation = -1;
    private long timeId = -1;
    private CInputStream dictIs = null;
    private long offset;
    private FeatureSetKey fsk = null;

    public FeatureIndexFileReader(BaseFile postingFile) throws IOException {
        this.postingFile = postingFile;

        this.dictionaryFile = postingFile.getPeerExtension(FeatureIndexManager.DictionaryExt);
        readDictIntoMap();
        computeGeneration();
    }

    public long getTimeID() {
        return timeId;
    }

    public long getGenerationID() {
        return generation;
    }

    public void delete() throws IOException {
        postingFile.delete();
        dictionaryFile.delete();
    }

    /**
     * Iterator ordered in the same order as the dictionary file.  Really the iterator should red the file rather than
     * create a listFiles (read: I did the cheap solution for now)
     *
     * @return
     */
    public Iterator<FeatureSetKey> getFeatureKeys() {
        return keys.iterator();
    }

    public BaseFile getPostingsFile() {
        return postingFile;
    }

    public int getAllKeys(Set<FeatureSetKey> set) {
        set.addAll(keys);
        return keys.size();
    }

    private void computeGeneration() {
        String name = postingFile.getNameSansExtension();
        long parts[] = ArrayUtil.getLongArrayFromStringArray(StringUtil.tokenizeFromSingleChar(name, "-"));
        if (parts.length < 2) {
            return;
        }
        timeId = parts[0];
        generation = parts[1];
    }

    private boolean readDictIntoMap() throws IOException {
        resetDictIter();
        while (readNextDictEntry()) {
            map.put(this.getCurrentFSK(), offset);
            keys.add(this.getCurrentFSK());

        }
        return true;
    }

    public void resetDictIter() throws IOException {
        dictIs = new FSInputStream(new java.io.File(dictionaryFile.getAbsolutePath()));
    }

    public PostingsFeatureReader getReader(FeatureSetKey fsk, Feature feature,
                                           CachedPostingsReader cpr) throws IOException {
        if (!map.containsKey(fsk)) {
            return null;
        }
        long l = map.get(fsk);
        PostingsFeatureReader reader = cpr.get(this, this.postingFile);

        if (reader.set(feature, l, fsk, this.postingFile.getName())) {
            return reader;
        }
        return null;
    }

    public boolean readNextDictEntry() throws IOException {
        if (dictIs == null) {
            return false;
        }
        offset = dictIs.readLong();
        if (offset == Long.MIN_VALUE) {
            return false;
        }
        fsk = FeatureSetKey.getFSKFromIS(dictIs);
        return true;
    }

    public FeatureSetKey getCurrentFSK() {
        return fsk;
    }


    public boolean equals(Object o) {
        if (o instanceof FeatureIndexFileReader) {
            return getPostingsFile().equals(((FeatureIndexFileReader) o).getPostingsFile());
        }
        return false;
    }

    public int hashcode() {
        return getPostingsFile().hashCode();
    }

    public int compareTo(final FeatureIndexFileReader featureIndexFileReader) {
        return dictionaryFile.compareTo(featureIndexFileReader.getPostingsFile());
    }
}


