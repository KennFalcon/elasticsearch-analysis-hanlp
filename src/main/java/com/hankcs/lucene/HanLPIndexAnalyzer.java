package com.hankcs.lucene;

import com.hankcs.cfg.Configuration;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import org.apache.lucene.analysis.Analyzer;

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Project: elasticsearch-analysis-hanlp
 * Description: 索引分析器
 * Author: Kenn
 * Create: 2018-12-14 15:10
 */
public class HanLPIndexAnalyzer extends Analyzer {
    /**
     * 分词配置
     */
    private final Configuration configuration;

    public HanLPIndexAnalyzer(Configuration configuration) {
        super();
        this.configuration = configuration;
        enableConfiguration();
    }

    @Override
    protected Analyzer.TokenStreamComponents createComponents(String fieldName) {
        return new Analyzer.TokenStreamComponents(
                TokenizerBuilder.tokenizer(
                        AccessController.doPrivileged(
                                (PrivilegedAction<Segment>) () -> HanLP.newSegment().enableIndexMode(true)),
                        configuration));
    }

    private void enableConfiguration() {
        this.configuration.enableIndexMode(true);
    }
}
