package com.hitorro.features.index;

import com.fasterxml.jackson.databind.JsonNode;
import com.hitorro.base.BaseBaseFileUtil;
import com.hitorro.features.FVComparator;
import com.hitorro.features.FeatureSetKey;
import com.hitorro.features.FeatureValue;
import com.hitorro.features.io.BaseIO;
import com.hitorro.features.io.key.LongKey;
import com.hitorro.util.basefile.fs.BaseFile;
import com.hitorro.util.core.iterator.mappers.DummyBaseMapper;
import com.hitorro.util.io.largedata.BaseFileAccessingObjectFactory;
import com.hitorro.util.io.largedata.HTObjectLinearizer;
import com.hitorro.util.io.largedata.compressedstreams.COutputStream;
import com.hitorro.util.io.largedata.compressedstreams.FSOutputStream;
import com.hitorro.util.typesystem.HTSerializableUtil;

import java.io.IOException;
import java.util.Iterator;

/**
 * Indexer takes a randomly ordered sequence of features values and converts them into an ordered listFiles that is then
 * turned into a "postings" file and a "dictionary" file.
 * <p/>
 * The postings file consists of a set of tuples of such: <long_key><value> <long_key><value> ... each set of postings
 * is terminated by <Long.MIN_VALUE>
 * <p/>
 * The dictionary file consists of a simple encoding of: <postings_offset><FeatureKeySet>
 * <postings_offset><FeatureKeySet> ..... terminate with: <Long.MIN_VALUE>
 * <p/>
 * User: chris
 */
public class FeatureIndexer extends HTObjectLinearizer<FeatureValue> {
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

    public FeatureIndexer(final int maxLength, final int maxMaturitySecs, final BaseFile directory, final int maxFilesBeforePreFileBasedMerge) {
        init(maxLength, maxMaturitySecs, directory, maxFilesBeforePreFileBasedMerge, "ser", getFactory());
    }

    public static BaseFileAccessingObjectFactory getFactory() {
        BaseFileAccessingObjectFactory fact =
                new BaseFileAccessingObjectFactory();
        fact.setNewFunction(FeatureIndexer::getNewValue)
                .setBaseFileToChainingMapper(BaseBaseFileUtil.bf2htser.combine(new DummyBaseMapper()))
                .setBaseFileSinkMapper(HTSerializableUtil.bf2htser.combine(new DummyBaseMapper()))
                .setNewArrayClass(FeatureValue.class).
                setComparitor(FVComparator.IDComp);
        return fact;
    }

    public static FeatureValue getNewValue(Object o) {
        return new FeatureValue();
    }


    public int processIterator(final Iterator<FeatureValue> iter) throws IOException {
        start();
        while (iter.hasNext()) {
            addToStream(iter.next());
        }
        stop();
        return count;
    }

    private boolean closeOutFeature() throws IOException {
        if (fsk != null) {
            // write out endofstream marker for posting.
            // postingsOS.w
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

    @Override
    public boolean init(JsonNode node) {
        /*directory = DirectoryKey.apply(map);
        init(MaxLength.apply(map), MaxSecsMaturity.apply(map), directory,
             maxFilesBeforePreFileBasedMerge, FVComparator.IDComp, "ser",
             new FeatureObjectFactory(FVComparator.IDComp));*/
        return true;
    }

    @Override
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

    @Override
    public boolean stop() throws IOException {
        finalizeFiles();
        return false;
    }
}

