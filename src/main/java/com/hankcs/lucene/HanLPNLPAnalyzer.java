package com.hankcs.lucene;

import com.hankcs.cfg.Configuration;
import com.hankcs.hanlp.HanLP;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

/**
 * NLP分析器
 *
 * @author Kenn
 */
public class HanLPNLPAnalyzer extends Analyzer {
    /**
     * 分词配置
     */
    private Configuration configuration;

    public HanLPNLPAnalyzer(Configuration configuration) {
        this.configuration = configuration;
        this.configuration.enableNameRecognize(true).enableTranslatedNameRecognize(true)
                .enableJapaneseNameRecognize(true).enablePlaceRecognize(true).enableOrganizationRecognize(true)
                .enablePartOfSpeechTagging(true);
    }

    public HanLPNLPAnalyzer() {
        super();
    }

    @Override
    protected Analyzer.TokenStreamComponents createComponents(String fieldName) {
        Tokenizer tokenizer = new HanLPTokenizer(HanLP.newSegment(), configuration);
        return new Analyzer.TokenStreamComponents(tokenizer);
    }
}
