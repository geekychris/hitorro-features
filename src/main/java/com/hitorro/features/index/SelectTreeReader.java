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
