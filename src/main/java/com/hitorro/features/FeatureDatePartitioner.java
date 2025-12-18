package com.hitorro.features;

import com.fasterxml.jackson.databind.JsonNode;
import com.hitorro.util.basefile.tools.queue.writer.partitioners.DateBasedWriterPartitioner;

import java.util.Date;

/**
 * .
 */
public class FeatureDatePartitioner extends DateBasedWriterPartitioner<FeatureValue> {
    @Override
    public void initForPass(final JsonNode params) {

    }

    public void setCurrent(FeatureValue o) {
        super.setCurrent(o);
    }

    public Date getDateField() {
        return new Date(getCurrent().getTime());
    }
}