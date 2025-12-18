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
package com.hitorro.features.index;

import com.hitorro.features.Feature;

import java.io.IOException;

/**
 * User: chris
 */
public class SelectTreeReader extends FeatureReader {
    private FeatureReader left;
    private FeatureReader right;
    private boolean leftNext = true;

    private boolean rightNext = true;

    private long leftKey;
    private long rightKey;

    public SelectTreeReader(FeatureReader left, FeatureReader right, Feature feature) throws IOException {
        this.left = left;
        this.right = right;
        bio = feature.getDataType().getCopyOfIO();
        lk.setBIO(bio);
        //reset();
        leftNext = left.next();
        rightNext = right.next();
    }

    public boolean reset() throws IOException {
        left.reset();
        right.reset();
        leftNext = left.next();
        rightNext = right.next();
        return true;
    }

    public boolean next() throws IOException {
        if (leftNext) {
            leftKey = left.lk.key;
            if (rightNext) {
                rightKey = right.lk.key;
                // both channels have data

                if (leftKey > rightKey) {
                    lk.setKey(rightKey);
                    bio.consumeValue(right.bio);
                    rightNext = right.next();
                } else if (leftKey < rightKey) {
                    lk.setKey(leftKey);
                    bio.consumeValue(left.bio);
                    leftNext = left.next();
                } else {
                    // right has presidence as the left is an older tier.
                    lk.setKey(rightKey);
                    bio.consumeValue(right.bio);
                    rightNext = right.next();
                    leftNext = left.next();
                }
            } else {
                leftKey = left.lk.key;
                lk.setKey(leftKey);
                bio.consumeValue(left.bio);
                leftNext = left.next();
            }

            return true;
        } else if (rightNext) {
            rightKey = right.lk.key;
            lk.setKey(rightKey);
            bio.consumeValue(right.bio);
            rightNext = right.next();
            return true;

        }
        return false;
    }
}
