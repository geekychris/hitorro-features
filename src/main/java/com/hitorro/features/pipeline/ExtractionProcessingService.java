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

import com.hitorro.base.docprocessing.DocProcessingChannelLoader;
import com.hitorro.base.docprocessing.DocProcessor;
import com.hitorro.base.docprocessing.DocProcessorEnqueueHandler;
import com.hitorro.features.FeatureEngine;
import com.hitorro.features.extractor.ExtractionSet;
import com.hitorro.features.extractor.ExtractorManager;
import com.hitorro.util.core.iterator.queue.AbstractEnqueue;
import com.hitorro.util.core.map.MapUtil;
import com.hitorro.util.core.params.HTProperties;
import com.hitorro.util.core.params.KeyMap;
import com.hitorro.util.core.string.Fmt;
import com.hitorro.util.json.keys.IntegerProperty;
import com.hitorro.util.json.keys.StringProperty;
import com.hitorro.util.startupframework.phases.ServiceDefinition;
import com.hitorro.util.typesystem.ValueSourceBaseFields;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@ServiceDefinition(dependentService = {},
        shortName = "extractorpipeline",
        description = "Extraction processing service",
        debugCommands = {},
        typeManagedClasses = {}
)
public class ExtractionProcessingService {
    public static final StringProperty ExtractorGroupKey = new StringProperty("extractor.processing.groups", "", "a");
    public static final IntegerProperty ExtractorInputQueueLengthKey =
            new IntegerProperty("extractor.processing.inputqlength", "Input queue length", 1000);
    public static final IntegerProperty ExtractorThreadCountKey =
            new IntegerProperty("extractor.processing.threadcount", "Extractor thread count", 4);

    public static final IntegerProperty ExtractorOutputQueueLengthKey =
            new IntegerProperty("extractor.processing.outputqlength", "Output queue length", 1000);

    private static final String ShortName = "ExtractorPipeline";


    private static ExtractionProcessingService service;

    private ValueSourceBaseFields baseFields = new ValueSourceBaseFields();
    private DocProcessorEnqueueHandler dpeh;
    private List<DocProcessingChannelLoader> channels = new ArrayList();

    public static ExtractionProcessingService getService() {
        return service;
    }

    public String init(boolean dbInit, final boolean upgrading, final long currentVersion, final long targetVersion) {
        service = this;
        /**
         * XXX need to fix
         */
        if (1 == 1) {
            return "";
        }
        String extractorGroups = ExtractorGroupKey.apply();

        DocProcessor docProcessor = new DocProcessor(ShortName, true);

        ExtractorManager em = FeatureEngine.getEngine().getExtractorManager();
        ExtractionSet es = em.getExtractionSet(extractorGroups, baseFields);
        ExtractorCommand ec = new ExtractorCommand(es);
        FeatureSinkCommand fsk = new FeatureSinkCommand();
        AbstractEnqueue queue = docProcessor.addStep(ec,
                ExtractorInputQueueLengthKey.apply(),
                "Extractor step",
                ExtractorThreadCountKey.apply(),
                "Step for processing features");
        docProcessor.addSink(fsk, ExtractorOutputQueueLengthKey.apply(),
                "FeatureSender",
                "Sink for sending features from the extractor");
        docProcessor.initialize();
        List<KeyMap> channelMaps = MapUtil.getSubMaps(HTProperties.getProperties().getMap(), "extractor.channels");
        for (KeyMap km : channelMaps) {
            String name = km.getKey();
            DocProcessingChannelLoader dpcl = new DocProcessingChannelLoader(docProcessor, queue, name);

            try {
                //XXX TODO stopped using keymap, need to use JsonNode

                if (!dpcl.getEnqueueHandler(null)) {
                    return Fmt.S("Could not initialize extractor channel: %s", name);
                }
            } catch (IOException e) {
                return Fmt.S("Could not initialize extractor channel: %s error %s %e", name, e, e);
            }
            channels.add(dpcl);
        }
        return null;
    }

    public String start(boolean dbInit) {
        // start the handlers
        for (DocProcessingChannelLoader dpcl : channels) {
            dpcl.start();
        }
        return null;
    }

    public String deInit() {
        return null;
    }
}
