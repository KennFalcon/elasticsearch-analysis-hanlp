package org.elasticsearch.index.analysis;

/**
 * Project: elasticsearch-analysis-hanlp
 * Description: Hanlp分词类型
 * Author: Kenn
 * Create: 2018-12-14 15:10
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
     * CRF分词
     */
    CRF,
    /**
     * N-最短路分词
     */
    N_SHORT,
    /**
     * 最短路分词
     */
    DIJKSTRA,
    /**
     * 极速词典分词
     */
    SPEED
}
