package com.hitorro.features;

import com.hitorro.jsontypesystem.propreaders.JVSProperties;
import com.hitorro.util.core.error.ErrorCode;
import com.hitorro.util.core.string.Fmt;
import com.hitorro.util.json.keys.propaccess.PropaccessError;
import com.hitorro.util.startupframework.phases.ServiceDefinition;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

@ServiceDefinition(dependentService = {},
        shortName = "features",
        description = "Feature machinery",
        debugCommands = {},
        typeManagedClasses = {FeatureSetKey.class, FeatureValue.class, RealmId.class}
)
public class FeatureService {
    public String init(boolean dbInit, final boolean upgrading, final long currentVersion, final long targetVersion) {
        FeatureEngine fe = new FeatureEngine();
        List<ErrorCode> codes = new ArrayList();
        try {
            fe.init(codes, JVSProperties.getProperties());
        } catch (PropaccessError e) {
            return Fmt.S("FeatureService service failed to initialize %s %e", e, e);
        }
        if (codes.size() > 0) {
            for (ErrorCode ec : codes) {
                Log.feature.error("Error initing feature %s", ec.toString());
            }
            return Fmt.S("FeatureService got %s errors initializing", codes.size());
        }
        FeatureEngine.setEngine(fe);

        return null;
    }

    public String start(boolean dbInit) {
        return null;
    }


    public String deInit() {
        return null;
    }
}
