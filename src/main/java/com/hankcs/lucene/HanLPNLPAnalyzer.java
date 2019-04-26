package com.hankcs.lucene;

import com.hankcs.cfg.Configuration;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import org.apache.lucene.analysis.Analyzer;

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
        return new Analyzer.TokenStreamComponents(TokenizerBuilder.tokenizer(AccessController.doPrivileged((PrivilegedAction<Segment>)HanLP::newSegment), configuration));
    }
}
