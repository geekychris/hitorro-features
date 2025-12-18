package com.hitorro.features.extractor;

import com.hitorro.features.FeatureValue;
import com.hitorro.util.core.opers.HTPredicate;

import java.util.ArrayList;
import java.util.List;

/**
 * Result set of feature values generated during the extraction process
 */
public class Results {
    private List<FeatureValue> fvs = new ArrayList();

    public void add(FeatureExtractor fe, FeatureValue fv) {
        fv.setIsScratch(fe.isScratch());
        fvs.add(fv);
    }

    public void clear() {
        fvs.clear();
    }

    public List<FeatureValue> getAllValues() {
        return fvs;
    }

    /**
     * Get all the feature values that test a specific constraint.
     *
     * @param constraint
     * @return constrained listFiles of feature values
     */
    public List<FeatureValue> getValuesByConstraint(HTPredicate<FeatureValue> constraint) {
        List<FeatureValue> vals = new ArrayList();
        getValuesByConstraint(constraint, vals);
        return vals;

    }

    /**
     * Get all the feature values that test a specific constraint, adding them to an existing container
     *
     * @param constraint
     * @param vals
     * @return
     */
    public int getValuesByConstraint(HTPredicate<FeatureValue> constraint, List<FeatureValue> vals) {
        int added = 0;
        for (FeatureValue fv : fvs) {
            if (constraint.test(fv)) {
                vals.add(fv);
                added++;
            }
        }
        return added;
    }
}
