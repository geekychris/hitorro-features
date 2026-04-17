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

import com.hitorro.features.FeatureSetKey;
import com.hitorro.features.FeatureValue;
import com.hitorro.features.io.BaseIO;
import com.hitorro.features.io.key.LongKey;
import com.hitorro.util.basefile.fs.BaseFile;
import com.hitorro.util.io.largedata.compressedstreams.COutputStream;
import com.hitorro.util.io.largedata.compressedstreams.FSOutputStream;

import java.io.IOException;
import java.util.Iterator;

/**
 * Indexer takes a sequence of feature values (sorted by FeatureSetKey) and converts them into
 * a binary index consisting of a "postings" file and a "dictionary" file.
 * <p/>
 * The postings file consists of tuples: {@code <long_key><value>} per group, terminated by
 * {@code Long.MIN_VALUE}.
 * <p/>
 * The dictionary file maps: {@code <postings_offset><FeatureSetKey>}, terminated by
 * {@code Long.MIN_VALUE}.
 */
public class FeatureIndexer {
    protected final BaseFile directory;
    protected FeatureSetKey fsk = null;
    protected FeatureValue curr = null;
    protected long postingsFileOffset = 0;
    protected LongKey lk = new LongKey();
    protected BaseIO bio = null;
    protected int count = 0;
    private BaseFile postings;
    private BaseFile dictionary;
    private BaseFile postingsTmp;
    private BaseFile dictionaryTmp;
    private COutputStream postingsOS;
    private COutputStream dictionaryOS;

    public FeatureIndexer(BaseFile directory) {
        this.directory = directory;
    }

    public int processIterator(final Iterator<FeatureValue> iter) throws IOException {
        start();
        while (iter.hasNext()) {
            addToStream(iter.next());
        }
        stop();
        return count;
    }

    public boolean start() throws IOException {
        setupFiles();
        count = 0;
        return true;
    }

    public boolean addToStream(final FeatureValue f) throws IOException {
        curr = f;
        if (!curr.getKeysEqual(fsk)) {
            closeOutFeature();
            openFeature();
        }
        lk.setKey(curr.getID());
        bio.setValue(curr.getValue());
        lk.write(postingsOS);
        count++;
        return true;
    }

    public boolean stop() throws IOException {
        finalizeFiles();
        return true;
    }

    private boolean closeOutFeature() throws IOException {
        if (fsk != null) {
            lk.closeMarker(postingsOS);
            dictionaryOS.writeLong(postingsFileOffset);
            fsk.writeToOS(dictionaryOS);
        }
        postingsFileOffset = postingsOS.getCurrentFileOffset();
        return true;
    }

    private boolean openFeature() {
        fsk = curr.getKey();
        bio = curr.getFeature().getDataType().getCopyOfIO();
        lk.setBIO(bio);
        return true;
    }

    private boolean setupFiles() throws IOException {
        long t = System.currentTimeMillis();
        postings = directory.getChild("%s-0.%s", t, FeatureIndexManager.PostingsExt);
        dictionary = directory.getChild("%s-0.%s", t, FeatureIndexManager.DictionaryExt);
        postingsTmp = directory.getChild("%s-0.%stmp", t, FeatureIndexManager.PostingsExt);
        dictionaryTmp = directory.getChild("%s-0.%stmp", t, FeatureIndexManager.DictionaryExt);
        postingsOS = new FSOutputStream(postingsTmp);
        dictionaryOS = new FSOutputStream(dictionaryTmp);
        lk.setKey(0);
        fsk = null;
        return true;
    }

    private boolean finalizeFiles() throws IOException {
        closeOutFeature();
        dictionaryOS.writeLong(Long.MIN_VALUE);
        postingsOS.close();
        dictionaryOS.close();
        dictionaryTmp.renameTo(dictionary);
        postingsTmp.renameTo(postings);
        return true;
    }
}
