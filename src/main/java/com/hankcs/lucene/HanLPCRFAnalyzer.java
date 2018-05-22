package com.hankcs.lucene;

import com.hankcs.cfg.Configuration;
import com.hankcs.hanlp.seg.CRF.CRFSegment;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

/**
 * CRF分析器
 *
 * @author Kenn
 */
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
        Tokenizer tokenizer = new HanLPTokenizer(new CRFSegment(), configuration);
        return new Analyzer.TokenStreamComponents(tokenizer);
    }
}
