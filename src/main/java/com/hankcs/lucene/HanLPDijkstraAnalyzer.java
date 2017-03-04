package com.hankcs.lucene;

import com.hankcs.hanlp.seg.Dijkstra.DijkstraSegment;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

import java.util.Set;

/**
 * 最短路分析器
 *
 * @author Kenn
 */
public class HanLPDijkstraAnalyzer extends Analyzer {

    private boolean enablePorterStemming;
    private Set<String> filter;

    /**
     * @param filter               停用词
     * @param enablePorterStemming 是否分析词干（仅限英文）
     */
    public HanLPDijkstraAnalyzer(Set<String> filter, boolean enablePorterStemming) {
        this.filter = filter;
        this.enablePorterStemming = enablePorterStemming;
    }

    /**
     * @param enablePorterStemming 是否分析词干.进行单复数,时态的转换
     */
    public HanLPDijkstraAnalyzer(boolean enablePorterStemming) {
        this.enablePorterStemming = enablePorterStemming;
    }

    public HanLPDijkstraAnalyzer() {
        super();
    }

    /**
     * 重载Analyzer接口，构造分词组件
     */
    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        Tokenizer tokenizer = new HanLPTokenizer(new DijkstraSegment().enableCustomDictionary(false)
                .enablePlaceRecognize(true).enableOrganizationRecognize(true), filter, enablePorterStemming);
        return new TokenStreamComponents(tokenizer);
    }

}
