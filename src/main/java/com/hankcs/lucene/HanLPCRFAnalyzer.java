package com.hankcs.lucene;

import com.hankcs.cfg.Configuration;
import com.hankcs.hanlp.seg.CRF.CRFSegment;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * @project: elasticsearch-analysis-hanlp
 * @description: CRF分析器
 * @author: Kenn
 * @create: 2018-12-14 15:10
 */
@Deprecated
public class HanLPCRFAnalyzer extends Analyzer {

    private Configuration configuration;

    public HanLPCRFAnalyzer(Configuration configuration) {
        this.configuration = configuration;
    }

    public HanLPCRFAnalyzer() {
        super();
    }

    @Override
    protected Analyzer.TokenStreamComponents createComponents(String fieldName) {
        Tokenizer tokenizer = AccessController.doPrivileged((PrivilegedAction<HanLPTokenizer>) () -> new HanLPTokenizer(new CRFSegment(), configuration));
        return new Analyzer.TokenStreamComponents(tokenizer);
    }
}
