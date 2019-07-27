package com.hankcs.lucene;

import com.hankcs.cfg.Configuration;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.model.perceptron.PerceptronLexicalAnalyzer;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.help.ESPluginLoggerFactory;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;

import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * @project: elasticsearch-analysis-hanlp
 * @description: NLP分析器
 * @author: Kenn
 * @create: 2018-12-14 15:10
 */
public class HanLPNLPAnalyzer extends Analyzer {

    private static final Logger logger = ESPluginLoggerFactory.getLogger(HanLPNLPAnalyzer.class.getName());

    /**
     * 分词配置
     */
    private Configuration configuration;

    public HanLPNLPAnalyzer(Configuration configuration) {
        this.configuration = configuration;
    }

    public HanLPNLPAnalyzer() {
        super();
    }

    @Override
    protected Analyzer.TokenStreamComponents createComponents(String fieldName) {
        return new Analyzer.TokenStreamComponents(
            TokenizerBuilder.tokenizer(AccessController.doPrivileged((PrivilegedAction<Segment>)() -> {
                try {
                    return new PerceptronLexicalAnalyzer();
                } catch (IOException e) {
                    logger.error("can not use nlp analyzer, provider default", e);
                    return HanLP.newSegment();
                }
            }), configuration));
    }
}
