package com.hankcs.lucene;

import org.elasticsearch.index.analysis.HanLPTokenizerFactory;
import org.elasticsearch.indices.analysis.AnalysisFactoryTestCase;
import org.elasticsearch.plugin.analysis.hanlp.AnalysisHanLPPlugin;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2020/10/09
 *
 * @author Kenn
 */
public class AnalysisHanLPFactoryTests extends AnalysisFactoryTestCase {

    public AnalysisHanLPFactoryTests() {
        super(new AnalysisHanLPPlugin());
    }

    @Override
    protected Map<String, Class<?>> getTokenizers() {
        Map<String, Class<?>> tokenizers = new HashMap<>(super.getTokenizers());
        tokenizers.put("hanlp_all", HanLPTokenizerFactory.class);
        return tokenizers;
    }
}
