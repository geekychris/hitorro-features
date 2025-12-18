package com.hitorro.features.index;

import com.hitorro.features.DerivedFeature;
import com.hitorro.features.FSetKeyComparator;
import com.hitorro.features.Feature;
import com.hitorro.features.FeatureSetKey;
import com.hitorro.features.Log;
import ht.features.*;
import com.hitorro.features.collections.BaseCollection;
import com.hitorro.features.collections.concrete.SparseFeatureCollection;
import com.hitorro.features.io.BaseIO;
import com.hitorro.features.io.MappingFunction;
import com.hitorro.features.io.key.LongKey;
import com.hitorro.util.basefile.filters.FileExtension;
import com.hitorro.util.basefile.fs.BaseFile;
import com.hitorro.util.core.ArrayUtil;
import com.hitorro.util.core.iterator.LikeRowMergingIterator;
import com.hitorro.util.core.string.Fmt;
import com.hitorro.util.core.string.StringUtil;
import com.hitorro.util.io.largedata.TakeRightRowMerger;
import com.hitorro.util.io.largedata.compressedstreams.COutputStream;
import com.hitorro.util.io.largedata.compressedstreams.FSInputStream;
import com.hitorro.util.io.largedata.compressedstreams.FSOutputStream;
import com.hitorro.util.io.largedata.iterator.SelectionTreeIterator;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;

/**
 * User: chris
 */
public class FeatureIndexManager {
    public static final String PostingsExt = "postings";
    public static final String DictionaryExt = "dictionary";

    public static Predicate<BaseFile> postingsFilter = new FileExtension(PostingsExt, true);
    public static Predicate<BaseFile> dictionaryFilter = new FileExtension(DictionaryExt, true);
    public static Predicate<BaseFile> orFilter = postingsFilter.or(dictionaryFilter);


    public static FileExtension filter = new FileExtension(PostingsExt, true);
    List<FeatureIndexFileReader> indexes = new ArrayList();
    Map<BaseFile, FeatureIndexFileReader> map = new HashMap();
    private BaseFile rootDir;
    private ReadWriteLock rwlock = new ReentrantReadWriteLock();
    private FeatureCacheManager fcm;

    public FeatureIndexManager(BaseFile root, FeatureCacheManager fcm) throws IOException {
        rootDir = root;
        this.fcm = fcm;
        loadFromDir(new ArrayList());
    }

    /**
     * We have a new set of indices, they were probably copied to the box from the index generator.
     * <p/>
     * These files are MOVED so if you need them for something else copy them first.
     * <p/>
     * Must be on the same file system
     *
     * @param dir
     */
    public boolean addDirectoryOfIndices(BaseFile dir) throws IOException {
        BaseFile list[] = dir.listFiles(orFilter);
        for (BaseFile f : list) {
            BaseFile moveTo = rootDir.getChild(f.getName());
            if (!f.renameTo(moveTo)) {
                Log.featureIndex.error("Unable to move file %s to %s", f, moveTo);
                return false;
            }
        }
        loadFromDir(new ArrayList());
        return true;
    }


    /**
     * loader and reloader of feature file readers.  note we dont load features here, just their dictionaries. If we
     * find this takes too long and it causes issues with delays in response time to queries (as we are taking a gross
     * write lock).  One should consider refining the structure so that we load all the new readers in and then take a
     * lock as we remove the old stuff and put the new stuff to the master data structure.
     *
     * @param removeList
     * @throws IOException
     */
    private synchronized void loadFromDir(List<FeatureIndexFileReader> removeList) throws IOException {
        HashSet<FeatureSetKey> fskSet = new HashSet();
        Lock lock = rwlock.writeLock();
        try {
            lock.lock();
            BaseFile list[] = rootDir.listFiles(filter);
            if (!ArrayUtil.nullOrEmpty(list)) {
                for (BaseFile file : list) {
                    if (!map.containsKey(file)) {
                        FeatureIndexFileReader reader = new FeatureIndexFileReader(file);
                        if (reader.getGenerationID() > 0) {
                            Log.featureIndex.debug("Gathering updated FSK's from a new reader: %s", reader);
                            reader.getAllKeys(fskSet);
                        }

                        map.put(file, reader);
                        indexes.add(reader);
                    }
                }
            }
            for (FeatureIndexFileReader old : removeList) {
                // take out those that shouldnt be here
                old.delete();
                indexes.remove(old);
                map.remove(old);
            }
            Collections.sort(indexes);
        } finally {
            lock.unlock();
        }
        // reload from cache all those things that are new
        if (fcm != null) {
            for (FeatureSetKey fsk : fskSet) {
                fcm.reloadCache(fsk, true);
            }
        }
    }

    /**
     * Debug method to read through all the tiers of the index and verify they can be read. Verify in this sense is a
     * loose term meaning, check that it doesnt blow up.
     *
     * @param set
     * @return
     * @throws IOException
     */
    public boolean validateAllReaders(FeatureSetKey set) throws IOException {
        Feature feature = set.getFeature();
        CachedPostingsReader cpr = new CachedPostingsReader();
        for (int i = this.indexes.size() - 1; i >= 0; i--) {
            FeatureIndexFileReader fileReader = this.indexes.get(i);
            PostingsFeatureReader reader = fileReader.getReader(set, feature, cpr);
            if (reader != null) {
                int count = 0;
                while (reader.next()) {
                    count++;
                }
            }
        }
        cpr.close();
        return true;
    }

    /**
     * Dumb at the moment, but the smarts are in determining how many files should be used in the apply process
     *
     * @return
     */
    public boolean merge() throws IOException {
        List<FeatureIndexFileReader> indicesSubset = new ArrayList();
        indicesSubset.addAll(indexes);
        mergeAux(indicesSubset);
        loadFromDir(indicesSubset);
        return true;
    }

    private boolean mergeAllAux() throws IOException {
        return mergeAux(this.indexes);
    }

    private boolean mergeAux(List<FeatureIndexFileReader> indicesSubset) throws IOException {
        BaseFile largestFile = indicesSubset.get(indicesSubset.size() - 1).getPostingsFile();
        String name = largestFile.getNameSansExtension();
        long parts[] = ArrayUtil.getLongArrayFromStringArray(StringUtil.tokenizeFromSingleChar(name, "-"));
        String newName = Fmt.S("%s-%s", parts[0], parts[1] + 1);
        BaseFile postings = rootDir.getChild("%s.%s", newName, FeatureIndexManager.PostingsExt);

        BaseFile dictionary = rootDir.getChild("%s.%s", newName, FeatureIndexManager.DictionaryExt);

        BaseFile postingsTmp = rootDir.getChild("%s.%stmp", newName, FeatureIndexManager.PostingsExt);

        BaseFile dictionaryTmp = rootDir.getChild("%s.%stmp", newName, FeatureIndexManager.DictionaryExt);
        FSOutputStream postingsOS = new FSOutputStream(postingsTmp);
        FSOutputStream dictionaryOS = new FSOutputStream(dictionaryTmp);

        Queue<Iterator<FeatureSetKey>> q = new LinkedList();
        for (FeatureIndexFileReader fileReader : indicesSubset) {
            Iterator<FeatureSetKey> iter = fileReader.getFeatureKeys();
            if (iter != null) {
                q.add(iter);
            }
        }
        while (q.size() > 1) {
            q.add(new SelectionTreeIterator(FSetKeyComparator.KeyComp, q.remove(), q.remove()));
        }

        Iterator<FeatureSetKey> iter = q.remove();
        LikeRowMergingIterator<FeatureSetKey> merger = new LikeRowMergingIterator(iter, FSetKeyComparator.KeyComp, TakeRightRowMerger.me);
        CachedPostingsReader cpr = new CachedPostingsReader();
        while (merger.hasNext()) {
            FeatureSetKey fsk = merger.next();
            int count = writeOut(fsk, indicesSubset, dictionaryOS, postingsOS, cpr);
        }
        cpr.close();

        dictionaryOS.writeLong(Long.MIN_VALUE);
        dictionaryOS.close();
        postingsOS.close();
        postingsTmp.renameTo(postings);
        dictionaryTmp.renameTo(dictionary);
        return true;
    }

    public FeatureReader getReader(FeatureSetKey set, CachedPostingsReader cpr) throws IOException {
        return getReader(set, indexes, cpr);
    }

    /**
     * write out one posting for the current key.
     *
     * @param set
     * @param indicesSubset
     * @param dictionary
     * @param postings
     * @return
     * @throws IOException
     */
    private int writeOut(FeatureSetKey set, List<FeatureIndexFileReader> indicesSubset,
                         COutputStream dictionary, COutputStream postings, CachedPostingsReader cpr) throws IOException {
        FeatureReader reader = getReader(set, indicesSubset, cpr);
        long offset = postings.getCurrentFileOffset();
        int count = 0;
        while (reader.next()) {
            reader.lk.write(postings);
            count++;
        }
        reader.lk.closeMarker(postings);
        dictionary.writeLong(offset);
        set.writeToOS(dictionary);

        return count;
    }

    private FeatureReader getReader(FeatureSetKey set, List<FeatureIndexFileReader> indicesSubset,
                                    CachedPostingsReader cpr) throws IOException {
        Feature feature = set.getFeature();
        Queue<FeatureReader> q = new LinkedList();
        for (FeatureIndexFileReader fileReader : indicesSubset) {
            FeatureReader reader = fileReader.getReader(set, feature, cpr);
            if (reader != null) {
                q.add(reader);
            }
        }
        if (q.size() == 0) {
            return null;
        }
        while (q.size() > 1) {
            SelectTreeReader str = new SelectTreeReader(q.remove(), q.remove(), feature);
            q.add(str);
        }
        return q.remove();

    }

    /**
     * Currently we only support sparse data so no need to check for the ephemeral id or mapping to something dense.
     *
     * @param fsk
     * @return
     * @throws IOException
     */
    BaseCollection getFeatureCollection(FeatureSetKey fsk) throws IOException {
        Lock lock = rwlock.readLock();
        CachedPostingsReader cpr = new CachedPostingsReader();
        try {
            lock.lock();
            Feature collectionFeature;
            SparseFeatureCollection collection;
            if (fsk.getFeature().isDerived()) {
                FeatureSetKey rootKeySet = fsk.getParentKey();
                DerivedFeature df = (DerivedFeature) fsk.getFeature();
                collectionFeature = df;
                FeatureReader reader = getReader(rootKeySet, cpr);
                MappingFunction mf[] = df.getMappingFunctions();
                for (int i = 0; i < mf.length; i++) {
                    mf[0] = (mf[0]).cloneMapper();
                    if (i > 0) {
                        mf[0].setNext((BaseIO) mf[i]);
                    }
                }


                if (reader == null) {
                    return null;
                }
                collection = fsk.getFeature().getDataType().getCopyOfSparseCollection();

                LongKey lk = reader.getLongKey();

                BaseIO setter = df.getDataType().getCopyOfIO();
                if (mf.length > 0) {
                    reader.setBio((BaseIO) mf[0]);
                    mf[mf.length - 1].setNext(setter);
                }

                while (reader.next()) {
                    collection.addRow(lk.getKey(), setter);
                }
            } else {
                FeatureReader reader = getReader(fsk, cpr);
                collectionFeature = fsk.getFeature();
                if (reader == null) {
                    return null;
                }
                collection = fsk.getFeature().getDataType().getCopyOfSparseCollection();
                LongKey lk = reader.getLongKey();
                BaseIO bio = reader.getBaseIO();

                while (reader.next()) {
                    collection.addRow(lk.getKey(), bio);
                }
            }
            collection.setIDIndex(collectionFeature.getKeyType().getStableOrdinal(), collectionFeature.getDataType(), collectionFeature, fsk.getRealmId());
            return collection;
        } finally {
            lock.unlock();
            cpr.close();
        }
    }
}

class CachedPostingsReader {
    Map<FeatureIndexFileReader, PostingsFeatureReader> cachedPostingsReaders = new HashMap();

    public PostingsFeatureReader get(FeatureIndexFileReader fifr, BaseFile postingFile) throws IOException {
        PostingsFeatureReader reader = cachedPostingsReaders.get(fifr);
        if (reader == null) {
            reader = new PostingsFeatureReader();
            reader.set(new FSInputStream(postingFile));
            cachedPostingsReaders.put(fifr, reader);
        }
        return reader;
    }

    public void close() throws IOException {
        for (PostingsFeatureReader pfr : cachedPostingsReaders.values()) {
            pfr.close();
        }
    }
}
