package com.hankcs.lucene;

import com.hankcs.cfg.Configuration;
import com.hankcs.hanlp.seg.Other.DoubleArrayTrieSegment;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

/**
 * 极速词典分析器
 *
 * @author Kenn
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
        Tokenizer tokenizer = new HanLPTokenizer(new DoubleArrayTrieSegment(), this.configuration);
        return new Analyzer.TokenStreamComponents(tokenizer);
    }
}
