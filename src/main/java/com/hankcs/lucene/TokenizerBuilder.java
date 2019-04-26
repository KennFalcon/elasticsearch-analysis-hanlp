package com.hankcs.lucene;

import com.hankcs.cfg.Configuration;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.TraditionalChineseTokenizer;
import org.apache.lucene.analysis.Tokenizer;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;

/**
 * @project: elasticsearch-analysis-hanlp
 * @description:
 * @author: Kenn
 * @create: 2019-04-25 09:47
 */
public class TokenizerBuilder {

    /**
     * 构建Tokenizer
     *
     * @param segment       原始segment
     * @param configuration 配置信息
     * @return 返回tokenizer
     */
    public static Tokenizer tokenizer(Segment segment, Configuration configuration) {
        Segment seg = segment(segment, configuration);
        return AccessController.doPrivileged((PrivilegedAction<HanLPTokenizer>)() -> new HanLPTokenizer(seg, configuration));
    }

    /**
     * 根据配置信息配置segment
     *
     * @param segment       原始segment
     * @param configuration 配置信息
     * @return 新segment
     */
    private static Segment segment(Segment segment, Configuration configuration) {
        segment.enableIndexMode(configuration.isEnableIndexMode())
            .enableNumberQuantifierRecognize(configuration.isEnableNumberQuantifierRecognize())
            .enableCustomDictionary(configuration.isEnableCustomDictionary())
            .enableTranslatedNameRecognize(configuration.isEnableTranslatedNameRecognize())
            .enableJapaneseNameRecognize(configuration.isEnableJapaneseNameRecognize())
            .enableOrganizationRecognize(configuration.isEnableOrganizationRecognize())
            .enablePlaceRecognize(configuration.isEnablePlaceRecognize())
            .enableNameRecognize(configuration.isEnableNameRecognize())
            .enablePartOfSpeechTagging(configuration.isEnablePartOfSpeechTagging())
            .enableOffset(configuration.isEnableOffset());
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
}
