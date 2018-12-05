package org.elasticsearch.index.analysis;

/**
 * Created by Kenn on 2017/3/19.
 */
public enum HanLPType {
    /**
     * 默认分词
     */
    HANLP,
    /**
     * 标准分词
     */
    STANDARD,
    /**
     * 索引分词
     */
    INDEX,
    /**
     * NLP分词
     */
    NLP,
    /**
     * N-最短路分词
     */
    N_SHORT,
    /**
     * 最短路分词
     */
    DIJKSTRA,
    /**
     * CRF分词
     */
    @Deprecated
    CRF,
    /**
     * 极速词典分词
     */
    SPEED
}
