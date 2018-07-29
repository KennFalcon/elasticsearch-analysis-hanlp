package org.elasticsearch.index.analysis;

import com.hankcs.cfg.Configuration;
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
    /**
     * 分词类型
     */
    private HanLPType hanLPType;
    /**
     * 分词配置
     */
    private Configuration configuration;

    public HanLPTokenizerFactory(IndexSettings indexSettings, Environment env, String name, Settings settings, HanLPType hanLPType) {
        super(indexSettings, name, settings);
        this.hanLPType = hanLPType;
        this.configuration = new Configuration(env, settings);
    }

    public static HanLPTokenizerFactory getHanLPTokenizerFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        return new HanLPTokenizerFactory(indexSettings, env, name, settings, HanLPType.HANLP);
    }

    public static HanLPTokenizerFactory getHanLPStandardTokenizerFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        return new HanLPTokenizerFactory(indexSettings, env, name, settings, HanLPType.STANDARD);
    }

    public static HanLPTokenizerFactory getHanLPIndexTokenizerFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        return new HanLPTokenizerFactory(indexSettings, env, name, settings, HanLPType.INDEX);
    }

    public static HanLPTokenizerFactory getHanLPNLPTokenizerFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        return new HanLPTokenizerFactory(indexSettings, env, name, settings, HanLPType.NLP);
    }

    public static HanLPTokenizerFactory getHanLPNShortTokenizerFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        return new HanLPTokenizerFactory(indexSettings, env, name, settings, HanLPType.N_SHORT);
    }

    public static HanLPTokenizerFactory getHanLPDijkstraTokenizerFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        return new HanLPTokenizerFactory(indexSettings, env, name, settings, HanLPType.DIJKSTRA);
    }

    public static HanLPTokenizerFactory getHanLPCRFTokenizerFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        return new HanLPTokenizerFactory(indexSettings, env, name, settings, HanLPType.CRF);
    }

    public static HanLPTokenizerFactory getHanLPSpeedTokenizerFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        return new HanLPTokenizerFactory(indexSettings, env, name, settings, HanLPType.SPEED);
    }

    @Override
    public Tokenizer create() {
        switch (this.hanLPType) {
            case HANLP:
                return new HanLPTokenizer(HanLP.newSegment(), configuration);
            case STANDARD:
                return new HanLPTokenizer(HanLP.newSegment(), configuration);
            case INDEX:
                configuration.enableIndexMode(true);
                return new HanLPTokenizer(HanLP.newSegment(), configuration);
            case NLP:
                configuration.enableNameRecognize(true).enableTranslatedNameRecognize(true).enableJapaneseNameRecognize(true).enablePlaceRecognize(true).enableOrganizationRecognize(true).enablePartOfSpeechTagging(true);
                return new HanLPTokenizer(HanLP.newSegment(), configuration);
            case N_SHORT:
                configuration.enableCustomDictionary(false).enablePlaceRecognize(true).enableOrganizationRecognize(true);
                return new HanLPTokenizer(new NShortSegment(), configuration);
            case DIJKSTRA:
                configuration.enableCustomDictionary(false).enablePlaceRecognize(true).enableOrganizationRecognize(true);
                return new HanLPTokenizer(new DijkstraSegment(), configuration);
            case CRF:
                configuration.enablePartOfSpeechTagging(true);
                return new HanLPTokenizer(new CRFSegment(), configuration);
            case SPEED:
                configuration.enableCustomDictionary(false);
                return new HanLPTokenizer(new DoubleArrayTrieSegment(), configuration);
            default:
                return new HanLPTokenizer(HanLP.newSegment(), configuration);
        }
    }
}
