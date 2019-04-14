package com.hankcs.lucene;

import com.hankcs.cfg.Configuration;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import org.apache.lucene.analysis.Analyzer;

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * @project: elasticsearch-analysis-hanlp
 * @description: 索引分析器
 * @author: Kenn
 * @create: 2018-12-14 15:10
 */
public class HanLPIndexAnalyzer extends BaseHanLPAnalyzer {
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
        Segment segment = buildSegment(AccessController.doPrivileged((PrivilegedAction<Segment>)HanLP::newSegment), configuration);
        return new Analyzer.TokenStreamComponents(buildBaseTokenizer(segment, configuration));
    }
}
