package com.hankcs.lucene;

import com.hankcs.cfg.Configuration;
import com.hankcs.hanlp.HanLP;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * @project: elasticsearch-analysis-hanlp
 * @description: NLP分析器
 * @author: Kenn
 * @create: 2018-12-14 15:10
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
        Tokenizer tokenizer = AccessController.doPrivileged((PrivilegedAction<HanLPTokenizer>) () -> new HanLPTokenizer(HanLP.newSegment(), configuration));
        return new Analyzer.TokenStreamComponents(tokenizer);
    }
}
