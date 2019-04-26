package com.hankcs.lucene;

import com.hankcs.cfg.Configuration;
import com.hankcs.hanlp.seg.Other.DoubleArrayTrieSegment;
import com.hankcs.hanlp.seg.Segment;
import org.apache.lucene.analysis.Analyzer;

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * @project: elasticsearch-analysis-hanlp
 * @description: 极速词典分析器
 * @author: Kenn
 * @create: 2018-12-14 15:10
 */
public class HanLPSpeedAnalyzer extends Analyzer {
    /**
     * 分词配置
     */
    private Configuration configuration;

    public HanLPSpeedAnalyzer(Configuration configuration) {
        this.configuration = configuration;
        this.configuration.enableCustomDictionary(false);
    }

    public HanLPSpeedAnalyzer() {
        super();
    }

    @Override
    protected Analyzer.TokenStreamComponents createComponents(String fieldName) {
        return new Analyzer.TokenStreamComponents(TokenizerBuilder.tokenizer(AccessController.doPrivileged((PrivilegedAction<Segment>)DoubleArrayTrieSegment::new), configuration));
    }
}
