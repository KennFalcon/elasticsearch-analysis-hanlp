package com.hankcs.lucene;

import com.hankcs.cfg.Configuration;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import org.apache.lucene.analysis.Analyzer;

/**
 * @project: elasticsearch-analysis-hanlp
 * @description: 标准分析器
 * @author: Kenn
 * @create: 2018-12-14 15:10
 */
public class HanLPStandardAnalyzer extends BaseHanLPAnalyzer {
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
        Segment segment = buildSegment(HanLP.newSegment(), configuration);
        return new Analyzer.TokenStreamComponents(buildBaseTokenizer(segment, configuration));
    }
}
