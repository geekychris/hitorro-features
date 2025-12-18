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

import com.hitorro.base.docprocessing.PipelineElement;
import com.hitorro.features.FeatureValue;
import com.hitorro.features.extractor.ExtractionContext;
import com.hitorro.features.extractor.ExtractionSet;
import com.hitorro.features.extractor.Results;
import com.hitorro.util.core.iterator.sinks.Sink;
import com.hitorro.util.core.thread.farm.FarmCommand;
import com.hitorro.util.typesystem.valuesource.ValueSourceForClass;

/**
 * Run through the extractors against the provided document
 */
public class ExtractorCommand extends FarmCommand<PipelineElement<ValueSourceForClass, Results, Sink<FeatureValue>>,
        PipelineElement<ValueSourceForClass, Results, Sink<FeatureValue>>,
        ExtractionContext> {
    private final ExtractionSet<ValueSourceForClass> set;

    public ExtractorCommand(ExtractionSet<ValueSourceForClass> set) {
        this.set = set;
    }

    public PipelineElement apply(final PipelineElement<ValueSourceForClass, Results, Sink<FeatureValue>> inElement) {
        long start = System.currentTimeMillis();
        ExtractionContext ec = getExtractionContext();
        ValueSourceForClass elem = inElement.getElement();
        Results results = new Results();
        set.extract(elem, results, ec);
        inElement.setResult(results);
        inElement.addTimeToProcessingTImeAccumulator(System.currentTimeMillis() - start);

        return inElement;
    }

    private ExtractionContext getExtractionContext() {
        ExtractionContext ec = this.getThreadData();
        if (ec == null) {
            ec = set.getExtractionContext();
            this.setThreadData(ec);
        }

        return ec;
    }
}
