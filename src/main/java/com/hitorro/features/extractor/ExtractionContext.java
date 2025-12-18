package com.hitorro.features.extractor;

import com.hitorro.util.core.DeInitIntf;
import com.hitorro.util.core.Log;
import com.hitorro.util.typesystem.BaseFields;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains state associated with extraction.  Used on per thread basis so that we can share state info between
 * extractors
 */
public class ExtractionContext implements DeInitIntf {
    private Map<Object, Object> perExtraction = new HashMap();

    private Map<Object, ResetableContext> lifetime = new HashMap();
    private List<ResetableContext> contexts = new ArrayList();
    private BaseFields baseFields;

    public ExtractionContext() {

    }

    public boolean setBaseFields(BaseFields baseFields, Object o) {
        this.baseFields = baseFields.getInstance();
        return this.baseFields.set(o);
    }

    public BaseFields getBaseFields() {
        return baseFields;
    }

    public void start() {
        clearPerExtractionCache();
        resestLifetime();
    }

    /**
     * Finished a collection cycle for one object
     */
    public void stop() {

    }

    public void clearPerExtractionCache() {
        perExtraction.clear();

    }

    public void resestLifetime() {
        for (ResetableContext rc : contexts) {
            rc.reset();
        }
    }

    //************************** Per extraction phase values *****************************************
    public Object getExtractionCacheValue(Object key) {
        return perExtraction.get(key);
    }

    public Object setExtractionCacheValue(Object key, Object value) {
        return perExtraction.put(key, value);
    }

    //*************************** LIFETIME VALUES (Not per extraction phase but life of compContext *******


    /**
     * Livetime objects must implement the resetable compContext interface.  This allows them to be "self instantiating"
     *
     * @param key
     * @return
     */
    public ResetableContext getLifetimeCacheValue(ResetableContext key) {
        ResetableContext rc = lifetime.get(key);
        if (rc == null) {
            // dont have one yet, created it.
            rc = key.getInstance(this);
            lifetime.put(key, rc);
            contexts.add(rc);
        }
        return rc;
    }

    public void deinit() {
        for (ResetableContext rc : contexts) {
            try {
                rc.deinit();
            } catch (Exception e) {
                Log.extractors.error("Unable to deinit %s", rc);
            }
        }
    }
}
