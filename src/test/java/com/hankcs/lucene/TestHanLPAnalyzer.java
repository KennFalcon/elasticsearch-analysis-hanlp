package com.hankcs.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.BaseTokenStreamTestCase;

/**
 * Created on 2020/10/09
 *
 * @author Kenn
 */
public class TestHanLPAnalyzer extends BaseTokenStreamTestCase {

    public void test() throws Exception {
        Analyzer analyzer = new HanLPAnalyzer();
        String sentence = "张三\n\n\n新买的手机";
        String[] result = { "张三", "\n\n\n", "新买", "的", "手机" };
        assertAnalyzesTo(analyzer, sentence, result);
        analyzer.close();
    }
}
