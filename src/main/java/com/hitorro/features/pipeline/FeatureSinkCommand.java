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
