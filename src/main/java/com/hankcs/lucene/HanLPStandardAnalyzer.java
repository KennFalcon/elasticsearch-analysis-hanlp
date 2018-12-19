package com.hankcs.lucene;

import com.hankcs.cfg.Configuration;
import com.hankcs.hanlp.HanLP;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * @project: elasticsearch-analysis-hanlp
 * @description: 标准分析器
 * @author: Kenn
 * @create: 2018-12-14 15:10
 */
public class HanLPStandardAnalyzer extends Analyzer {
    /**
     * 分词配置
     */
    private Configuration configuration;

    public HanLPStandardAnalyzer(Configuration configuration) {
        this.configuration = configuration;
    }

    public HanLPStandardAnalyzer() {
        super();
    }

    @Override
    protected Analyzer.TokenStreamComponents createComponents(String fieldName) {
        Tokenizer tokenizer = AccessController.doPrivileged((PrivilegedAction<HanLPTokenizer>) () -> new HanLPTokenizer(HanLP.newSegment(), configuration));
        return new Analyzer.TokenStreamComponents(tokenizer);
    }
}
