package com.hankcs.lucene;

import com.hankcs.cfg.Configuration;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.TraditionalChineseTokenizer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;

/**
 * @project: elasticsearch-analysis-hanlp
 * @description:
 * @author: Kenn
 * @create: 2018-12-21 10:41
 */
abstract class BaseHanLPAnalyzer extends Analyzer {

    /**
     * 根据配置信息配置segment
     *
     * @param segment       原始segment
     * @param configuration 配置信息
     * @return 新segment
     */
    protected Segment buildSegment(Segment segment, Configuration configuration) {
        segment.enableIndexMode(configuration.isEnableIndexMode())
                .enableNumberQuantifierRecognize(configuration.isEnableNumberQuantifierRecognize())
                .enableCustomDictionary(configuration.isEnableCustomDictionary())
                .enableTranslatedNameRecognize(configuration.isEnableTranslatedNameRecognize())
                .enableJapaneseNameRecognize(configuration.isEnableJapaneseNameRecognize())
                .enableOrganizationRecognize(configuration.isEnableOrganizationRecognize())
                .enablePlaceRecognize(configuration.isEnablePlaceRecognize())
                .enableNameRecognize(configuration.isEnableNameRecognize())
                .enablePartOfSpeechTagging(configuration.isEnablePartOfSpeechTagging());
        if (configuration.isEnableTraditionalChineseMode()) {
            segment.enableIndexMode(false);
            TraditionalChineseTokenizer.SEGMENT = segment;
            return new Segment() {
                @Override
                protected List<Term> segSentence(char[] sentence) {
                    return TraditionalChineseTokenizer.segment(new String(sentence));
                }
            };
        }
        return segment;
    }

    /**
     * 创建基础Tokenizer
     * @param segment 分词器
     * @param configuration 配置信息
     * @return Tokenizer
     */
    protected Tokenizer buildBaseTokenizer(Segment segment, Configuration configuration) {
        return AccessController.doPrivileged((PrivilegedAction<HanLPTokenizer>) () -> new HanLPTokenizer(segment, configuration));
    }
}
