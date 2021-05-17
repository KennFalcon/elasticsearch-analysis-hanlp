package com.hankcs.lucene;

import com.hankcs.cfg.Configuration;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import org.apache.lucene.analysis.Tokenizer;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;

/**
 * Project: elasticsearch-analysis-hanlp
 * Description:
 * Author: Kenn
 * Create: 2019-04-25 09:47
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
        if (!configuration.isEnableCustomConfig()) {
            return segment.enableOffset(true);
        }
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
            return new Segment() {
                @Override
                protected List<Term> segSentence(char[] sentence) {
                    return segment.seg(HanLP.convertToSimplifiedChinese(new String(sentence)));
                }
            };
        }
        return segment;
    }
}
