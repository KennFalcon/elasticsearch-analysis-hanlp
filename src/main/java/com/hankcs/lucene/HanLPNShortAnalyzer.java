package com.hankcs.lucene;

import com.hankcs.cfg.Configuration;
import com.hankcs.hanlp.seg.NShort.NShortSegment;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

/**
 * N-最短路径分析器
 *
 * @author Kenn
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
        Tokenizer tokenizer = new HanLPTokenizer(new NShortSegment(), configuration);
        return new Analyzer.TokenStreamComponents(tokenizer);
    }
}
