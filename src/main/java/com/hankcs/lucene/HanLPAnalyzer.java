package com.hankcs.lucene;

import com.hankcs.cfg.Configuration;
import com.hankcs.hanlp.HanLP;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

/**
 * Hanlp默认分析器
 *
 * @author Kenn
 */
public class HanLPAnalyzer extends Analyzer {
    /**
     * 分词配置
     */
    private Configuration configuration;

    public HanLPAnalyzer(Configuration configuration) {
        this.configuration = configuration;
    }

    public HanLPAnalyzer() {
        super();
    }

    @Override
    protected Analyzer.TokenStreamComponents createComponents(String fieldName) {
        Tokenizer tokenizer = new HanLPTokenizer(HanLP.newSegment(), configuration);
        return new Analyzer.TokenStreamComponents(tokenizer);
    }
}
