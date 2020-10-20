package com.hankcs.lucene;

import com.hankcs.cfg.Configuration;
import com.hankcs.hanlp.seg.NShort.NShortSegment;
import com.hankcs.hanlp.seg.Segment;
import org.apache.lucene.analysis.Analyzer;

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Project: elasticsearch-analysis-hanlp
 * Description: N-最短路径分析器
 * Author: Kenn
 * Create: 2018-12-14 15:10
 */
public class HanLPNShortAnalyzer extends Analyzer {
    /**
     * 分词配置
     */
    private Configuration configuration;

    public HanLPNShortAnalyzer(Configuration configuration) {
        this.configuration = configuration;
        this.configuration.enableCustomDictionary(false).enablePlaceRecognize(true).enableOrganizationRecognize(true);
    }

    public HanLPNShortAnalyzer() {
        super();
    }

    @Override
    protected Analyzer.TokenStreamComponents createComponents(String fieldName) {
        return new Analyzer.TokenStreamComponents(TokenizerBuilder.tokenizer(
            AccessController.doPrivileged((PrivilegedAction<Segment>)() -> new NShortSegment().enableCustomDictionary(
                false).enablePlaceRecognize(true).enableOrganizationRecognize(true)), configuration));
    }
}
