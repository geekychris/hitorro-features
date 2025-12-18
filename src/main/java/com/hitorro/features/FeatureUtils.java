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
