package org.elasticsearch.index.analysis;

import com.hankcs.cfg.Configuration;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.model.crf.CRFLexicalAnalyzer;
import com.hankcs.hanlp.model.perceptron.PerceptronLexicalAnalyzer;
import com.hankcs.hanlp.seg.Dijkstra.DijkstraSegment;
import com.hankcs.hanlp.seg.NShort.NShortSegment;
import com.hankcs.hanlp.seg.Other.DoubleArrayTrieSegment;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.lucene.TokenizerBuilder;
import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;

import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * @project: elasticsearch-analysis-hanlp
 * @description: Hanlp tokenizer factory
 * @author: Kenn
 * @create: 2018-12-14 15:10
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
        super(indexSettings, settings);
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

    @Deprecated
    public static HanLPTokenizerFactory getHanLPCRFTokenizerFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        return new HanLPTokenizerFactory(indexSettings, env, name, settings, HanLPType.CRF);
    }

    public static HanLPTokenizerFactory getHanLPSpeedTokenizerFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        return new HanLPTokenizerFactory(indexSettings, env, name, settings, HanLPType.SPEED);
    }

    @Override
    public Tokenizer create() {
        switch (this.hanLPType) {
            case INDEX:
                configuration.enableIndexMode(true);
                return TokenizerBuilder.tokenizer(AccessController.doPrivileged((PrivilegedAction<Segment>)HanLP::newSegment), configuration);
            case NLP:
                return TokenizerBuilder.tokenizer(AccessController.doPrivileged((PrivilegedAction<Segment>) () -> {
                    try {
                        return new PerceptronLexicalAnalyzer();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return HanLP.newSegment();
                }), configuration);
            case N_SHORT:
                configuration.enableCustomDictionary(false).enablePlaceRecognize(true).enableOrganizationRecognize(true);
                return TokenizerBuilder.tokenizer(AccessController.doPrivileged((PrivilegedAction<Segment>)NShortSegment::new), configuration);
            case DIJKSTRA:
                configuration.enableCustomDictionary(false).enablePlaceRecognize(true).enableOrganizationRecognize(true);
                return TokenizerBuilder.tokenizer(AccessController.doPrivileged((PrivilegedAction<Segment>)DijkstraSegment::new), configuration);
            case CRF:
                return TokenizerBuilder.tokenizer(AccessController.doPrivileged((PrivilegedAction<Segment>) () -> {
                    try {
                        return new CRFLexicalAnalyzer();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return HanLP.newSegment();
                }), configuration);
            case SPEED:
                configuration.enableCustomDictionary(false);
                return TokenizerBuilder.tokenizer(AccessController.doPrivileged((PrivilegedAction<Segment>)DoubleArrayTrieSegment::new), configuration);
            case HANLP:
            case STANDARD:
            default:
                return TokenizerBuilder.tokenizer(AccessController.doPrivileged((PrivilegedAction<Segment>)HanLP::newSegment), configuration);
        }
    }
}
