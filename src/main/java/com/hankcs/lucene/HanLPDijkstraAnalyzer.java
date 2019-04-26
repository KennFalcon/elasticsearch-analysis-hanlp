package com.hankcs.lucene;

import com.hankcs.cfg.Configuration;
import com.hankcs.hanlp.seg.Dijkstra.DijkstraSegment;
import com.hankcs.hanlp.seg.Segment;
import org.apache.lucene.analysis.Analyzer;

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * @project: elasticsearch-analysis-hanlp
 * @description: 最短路分析器
 * @author: Kenn
 * @create: 2018-12-14 15:10
 */
public class HanLPDijkstraAnalyzer extends Analyzer {
    /**
     * 分词配置
     */
    private Configuration configuration;

    public HanLPDijkstraAnalyzer(Configuration configuration) {
        this.configuration = configuration;
        this.configuration.enableCustomDictionary(false).enablePlaceRecognize(true).enableOrganizationRecognize(true);
    }

    public HanLPDijkstraAnalyzer() {
        super();
    }

    @Override
    protected Analyzer.TokenStreamComponents createComponents(String fieldName) {
        return new Analyzer.TokenStreamComponents(TokenizerBuilder.tokenizer(AccessController.doPrivileged((PrivilegedAction<Segment>)DijkstraSegment::new), configuration));
    }
}
