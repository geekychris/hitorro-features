package com.hitorro.features;

import com.hitorro.util.basefile.fs.BaseFile;
import com.hitorro.util.core.iterator.mappers.BaseMapper;
import com.hitorro.util.core.iterator.mappers.DummyBaseMapper;
import com.hitorro.util.core.iterator.sinks.Sink;
import com.hitorro.util.io.largedata.iterator.HTSerializableSink;
import com.hitorro.util.typesystem.HTSerializable;

import java.io.IOException;

/**
 * Created by chris on 3/6/16.
 */
public class FeatureUtils {


    public static BaseMapper<BaseFile, Sink<? extends HTSerializable>> blaa = new BaseMapper<BaseFile, Sink<? extends HTSerializable>>() {
        public Sink<? extends HTSerializable> apply(BaseFile bf) {
            try {
                return new HTSerializableSink(bf.getDataOutputStream());
            } catch (IOException e) {
                return null;
            }
        }
    };
    public static BaseMapper<BaseFile, Sink<FeatureValue>> bf2htser2 = blaa.combine(new DummyBaseMapper<Sink<? extends HTSerializable>, Sink<FeatureValue>>());

}
