package com.hitorro.features.pipeline;

import com.fasterxml.jackson.databind.JsonNode;
import com.hitorro.base.docprocessing.PipelineElement;
import com.hitorro.features.FeatureValue;
import com.hitorro.features.extractor.Results;
import com.hitorro.util.core.Log;
import com.hitorro.util.core.iterator.sinks.Sink;
import com.hitorro.util.io.StoreException;

import java.io.IOException;

/**
 * Send off features to their target location.
 */
//TODO UPDATE (ValueSource to Object.....ValueSource was a Lucene class????)
public class FeatureSinkCommand implements Sink<PipelineElement<Object, Results, Sink<FeatureValue>>> {
    @Override
    public boolean init(final JsonNode node) {
        return false;
    }

    @Override
    public boolean start() throws IOException {
        return false;
    }

    public boolean add(final PipelineElement<Object, Results, Sink<FeatureValue>> pe) {
        Results res = pe.getResult();
        Sink<FeatureValue> fst = pe.getTarget();
        try {
            fst.addAll(res.getAllValues());
        } catch (IOException e) {
            Log.docprocessor.error("Unable to consume features error %s %e", e, e);
        } catch (StoreException e) {
            Log.docprocessor.error("Unable to consume features error %s %e", e, e);
        }
        return true;
    }

    @Override
    public boolean stop() throws IOException {
        return false;
    }

    public void close() {

    }
}
