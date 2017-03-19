package org.elasticsearch.index.analysis;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.CRF.CRFSegment;
import com.hankcs.hanlp.seg.Dijkstra.DijkstraSegment;
import com.hankcs.hanlp.seg.NShort.NShortSegment;
import com.hankcs.hanlp.seg.Other.DoubleArrayTrieSegment;
import com.hankcs.lucene.HanLPTokenizer;
import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;

/**
 * Created by Kenn on 2017/3/15.
 */
public class HanLPTokenizerFactory extends AbstractTokenizerFactory {

    private HanLPType hanLPType;

    private boolean enablePorterStemming;

    public HanLPTokenizerFactory(IndexSettings indexSettings, String name, Settings settings, HanLPType hanLPType) {
        super(indexSettings, name, settings);
        this.hanLPType = hanLPType;
        this.enablePorterStemming = settings.getAsBoolean("enablePorterStemming", false);
    }

    public static HanLPTokenizerFactory getHanLPTokenizerFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        return new HanLPTokenizerFactory(indexSettings, name, settings, HanLPType.HANLP);
    }

    public static HanLPTokenizerFactory getHanLPStandardTokenizerFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        return new HanLPTokenizerFactory(indexSettings, name, settings, HanLPType.STANDARD);
    }

    public static HanLPTokenizerFactory getHanLPIndexTokenizerFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        return new HanLPTokenizerFactory(indexSettings, name, settings, HanLPType.INDEX);
    }

    public static HanLPTokenizerFactory getHanLPNLPTokenizerFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        return new HanLPTokenizerFactory(indexSettings, name, settings, HanLPType.NLP);
    }

    public static HanLPTokenizerFactory getHanLPNShortTokenizerFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        return new HanLPTokenizerFactory(indexSettings, name, settings, HanLPType.N_SHORT);
    }

    public static HanLPTokenizerFactory getHanLPDijkstraTokenizerFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        return new HanLPTokenizerFactory(indexSettings, name, settings, HanLPType.DIJKSTRA);
    }

    public static HanLPTokenizerFactory getHanLPCRFTokenizerFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        return new HanLPTokenizerFactory(indexSettings, name, settings, HanLPType.CRF);
    }

    public static HanLPTokenizerFactory getHanLPSpeedTokenizerFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        return new HanLPTokenizerFactory(indexSettings, name, settings, HanLPType.SPEED);
    }

    @Override
    public Tokenizer create() {
        switch (hanLPType) {
            case HANLP:
                return new HanLPTokenizer(HanLP.newSegment(), null, enablePorterStemming);
            case STANDARD:
                return new HanLPTokenizer(HanLP.newSegment(), null, enablePorterStemming);
            case INDEX:
                return new HanLPTokenizer(HanLP.newSegment().enableIndexMode(true), null, enablePorterStemming);
            case NLP:
                return new HanLPTokenizer(
                        HanLP.newSegment().enableNameRecognize(true).enableTranslatedNameRecognize(true)
                                .enableJapaneseNameRecognize(true).enablePlaceRecognize(true)
                                .enableOrganizationRecognize(true).enablePartOfSpeechTagging(true),
                        null, enablePorterStemming);
            case N_SHORT:
                return new HanLPTokenizer(new NShortSegment().enableCustomDictionary(false)
                        .enablePlaceRecognize(true).enableOrganizationRecognize(true), null,
                        enablePorterStemming);
            case DIJKSTRA:
                return new HanLPTokenizer(new DijkstraSegment().enableCustomDictionary(false)
                        .enablePlaceRecognize(true).enableOrganizationRecognize(true), null,
                        enablePorterStemming);
            case CRF:
                return new HanLPTokenizer(new CRFSegment(), null, enablePorterStemming);

            case SPEED:
                return new HanLPTokenizer(new DoubleArrayTrieSegment().enableCustomDictionary(true).enablePartOfSpeechTagging(true), null,
                        enablePorterStemming);
            default:
                return null;
        }
    }

}
