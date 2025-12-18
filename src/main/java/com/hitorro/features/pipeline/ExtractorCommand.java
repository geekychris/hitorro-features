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
