package com.hitorro.features.io;

import com.hitorro.features.io.intf.ValueIntf;
import com.hitorro.util.io.largedata.compressedstreams.CInputStream;
import com.hitorro.util.io.largedata.compressedstreams.COutputStream;

import java.io.IOException;

/**
 *
 */
public interface BaseIO<T extends BaseIO, E> extends ValueIntf<E> {

    BaseIO cloneIO();

    void read(CInputStream is) throws IOException;

    void write(COutputStream os) throws IOException;

    void setValue(E e);

    void consumeValue(T bio);

}
