package org.elasticsearch.indices.analysis;

import org.elasticsearch.common.inject.AbstractModule;

public class HanLPIndicesAnalysisModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(HanLPIndicesAnalysis.class).asEagerSingleton();
    }

}