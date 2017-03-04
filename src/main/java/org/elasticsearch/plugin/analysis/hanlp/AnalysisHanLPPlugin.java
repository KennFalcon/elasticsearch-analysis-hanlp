package org.elasticsearch.plugin.analysis.hanlp;

import org.elasticsearch.common.inject.Module;
import org.elasticsearch.indices.analysis.HanLPIndicesAnalysisModule;
import org.elasticsearch.plugins.Plugin;

import java.util.Collection;
import java.util.Collections;

public class AnalysisHanLPPlugin extends Plugin {

    @Override
    public String name() {
        return "analysis-hanlp";
    }

    @Override
    public String description() {
        return "hanlp analysis";
    }

    @Override
    public Collection<Module> nodeModules() {
        return Collections.<Module>singletonList(new HanLPIndicesAnalysisModule());
    }

}
