package com.hankcs.lucene;

import com.hankcs.cfg.Configuration;
import com.hankcs.hanlp.HanLP;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

/**
 * 索引分析器
 *
 * @author Kenn
 */
public class HanLPIndexAnalyzer extends Analyzer {
    /**
     * 分词配置
     */
    private Configuration configuration;

    public HanLPIndexAnalyzer(Configuration configuration) {
        this.configuration = configuration;
        this.configuration.enableIndexMode(true);
    }

    public HanLPIndexAnalyzer() {
        super();
    }

    @Override
    protected Analyzer.TokenStreamComponents createComponents(String fieldName) {
        Tokenizer tokenizer = new HanLPTokenizer(HanLP.newSegment(), configuration);
        return new Analyzer.TokenStreamComponents(tokenizer);
    }
}
