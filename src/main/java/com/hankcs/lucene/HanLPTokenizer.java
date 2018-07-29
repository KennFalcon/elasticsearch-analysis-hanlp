package com.hankcs.lucene;


import com.hankcs.cfg.Configuration;
import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.dictionary.stopword.CoreStopWordDictionary;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.TraditionalChineseTokenizer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

import java.io.BufferedReader;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;

/**
 * Tokenizer，抄袭ansj的
 */
public class HanLPTokenizer extends Tokenizer {
    /**
     * 当前词
     */
    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
    /**
     * 偏移量
     */
    private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
    /**
     * 距离
     */
    private final PositionIncrementAttribute positionAttr = addAttribute(PositionIncrementAttribute.class);
    /**
     * 词性
     */
    private TypeAttribute typeAtt = addAttribute(TypeAttribute.class);
    /**
     * 配置
     */
    private Configuration configuration;
    /**
     * 分词器
     */
    private SegmentWrapper segment;
    /**
     *
     */
    private final PorterStemmer stemmer = new PorterStemmer();

    /**
     * 单文档当前所在的总offset，当reset（切换multi-value fields中的value）的时候不清零，在end（切换field）时清零
     */
    private int totalOffset = 0;

    /**
     * @param segment       HanLP中的某个分词器
     * @param configuration 分词配置
     */
    public HanLPTokenizer(Segment segment, Configuration configuration) {
        this.configuration = configuration;
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
            Segment inner = segment;
            TraditionalChineseTokenizer.SEGMENT = inner;
            segment = new Segment() {
                @Override
                protected List<Term> segSentence(char[] sentence) {
                    List<Term> termList = TraditionalChineseTokenizer.segment(new String(sentence));
                    return termList;
                }
            };
        }
        this.segment = new SegmentWrapper(this.input, segment);
    }

    @Override
    final public boolean incrementToken() throws IOException {
        clearAttributes();
        int position = 0;
        Term term;
        boolean unIncreased = true;
        do {
            term = segment.next();
            if (term == null) {
                break;
            }
            if (configuration.isEnablePorterStemming() && term.nature == Nature.nx) {
                term.word = stemmer.stem(term.word);
            }
            final Term copyTerm = term;
            if ((!this.configuration.isEnableStopDictionary()) || (!AccessController.doPrivileged((PrivilegedAction<Boolean>) () -> CoreStopWordDictionary.shouldRemove(copyTerm)))) {
                position++;
                unIncreased = false;
            }
        }
        while (unIncreased);

        if (term != null) {
            positionAttr.setPositionIncrement(position);
            termAtt.setEmpty().append(term.word);
            offsetAtt.setOffset(correctOffset(totalOffset + term.offset),
                    correctOffset(totalOffset + term.offset + term.word.length()));
            typeAtt.setType(term.nature == null ? "null" : term.nature.toString());
            return true;
        } else {
            totalOffset += segment.offset;
            return false;
        }
    }

    @Override
    public void end() throws IOException {
        super.end();
        offsetAtt.setOffset(totalOffset, totalOffset);
        totalOffset = 0;
    }

    /**
     * 必须重载的方法，否则在批量索引文件时将会导致文件索引失败
     */
    @Override
    public void reset() throws IOException {
        super.reset();
        segment.reset(new BufferedReader(this.input));
    }
}
