package org.elasticsearch.index.analysis;

/**
 * @project: elasticsearch-analysis-hanlp
 * @description: Hanlp分词类型
 * @author: Kenn
 * @create: 2018-12-14 15:10
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
     * 极速词典分词
     */
    SPEED
}
