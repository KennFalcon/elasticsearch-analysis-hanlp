package com.hankcs.lucene;

import com.hankcs.cfg.Configuration;
import com.hankcs.hanlp.seg.Other.DoubleArrayTrieSegment;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

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
        Tokenizer tokenizer = AccessController.doPrivileged((PrivilegedAction<HanLPTokenizer>) () -> new HanLPTokenizer(new DoubleArrayTrieSegment(), configuration));
        return new Analyzer.TokenStreamComponents(tokenizer);
    }
}
