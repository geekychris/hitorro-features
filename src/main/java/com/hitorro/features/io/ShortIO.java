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
package com.hitorro.features.io;

import com.hitorro.features.io.intf.ShortIntf;
import com.hitorro.util.io.largedata.compressedstreams.CInputStream;
import com.hitorro.util.io.largedata.compressedstreams.COutputStream;

import java.io.IOException;

/**
 *
 */
public class ShortIO implements BaseIO<ShortIO, Short>, ShortIntf {
    private short d;

    public short getShort() {
        return d;
    }

    public void setShort(short s) {
        d = s;
    }

    public ShortIO cloneIO() {
        ShortIO o = new ShortIO();
        o.d = d;
        return o;
    }

    public void read(CInputStream is) throws IOException {
        d = is.readShort();
    }

    public void write(COutputStream os) throws IOException {
        os.writeShort(d);
    }

    public void setValue(final Short aShort) {
        d = aShort.shortValue();
    }

    public void consumeValue(final ShortIO bio) {
        setValue(bio.d);
    }
}
