package org.elasticsearch.index.analysis;

import com.hankcs.cfg.Configuration;
import com.hankcs.lucene.HanLPAnalyzer;
import com.hankcs.lucene.HanLPCRFAnalyzer;
import com.hankcs.lucene.HanLPDijkstraAnalyzer;
import com.hankcs.lucene.HanLPIndexAnalyzer;
import com.hankcs.lucene.HanLPNLPAnalyzer;
import com.hankcs.lucene.HanLPNShortAnalyzer;
import com.hankcs.lucene.HanLPSpeedAnalyzer;
import com.hankcs.lucene.HanLPStandardAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;

/**
 * Project: elasticsearch-analysis-hanlp
 * Description: Hanlp analyzer provider
 * Author: Kenn
 * Create: 2018-12-14 15:10
 */
public class HanLPAnalyzerProvider extends AbstractIndexAnalyzerProvider<Analyzer> {

    private final Analyzer analyzer;

    public HanLPAnalyzerProvider(IndexSettings indexSettings, Environment env, String name, Settings settings, HanLPType hanLPType) {
        super(indexSettings, name, settings);
        Configuration configuration = new Configuration(env, settings);
        switch (hanLPType) {
            case HANLP:
                analyzer = new HanLPAnalyzer(configuration);
                break;
            case STANDARD:
                analyzer = new HanLPStandardAnalyzer(configuration);
                break;
            case INDEX:
                analyzer = new HanLPIndexAnalyzer(configuration);
                break;
            case NLP:
                analyzer = new HanLPNLPAnalyzer(configuration);
                break;
            case CRF:
                analyzer = new HanLPCRFAnalyzer(configuration);
                break;
            case N_SHORT:
                analyzer = new HanLPNShortAnalyzer(configuration);
                break;
            case DIJKSTRA:
                analyzer = new HanLPDijkstraAnalyzer(configuration);
                break;
            case SPEED:
                analyzer = new HanLPSpeedAnalyzer(configuration);
                break;
            default:
                analyzer = null;
                break;
        }
    }

    public static HanLPAnalyzerProvider getHanLPAnalyzerProvider(IndexSettings indexSettings, Environment env, String name,
                                                                 Settings settings) {
        return new HanLPAnalyzerProvider(indexSettings, env, name, settings, HanLPType.HANLP);
    }

    public static HanLPAnalyzerProvider getHanLPStandardAnalyzerProvider(IndexSettings indexSettings, Environment env, String name,
                                                                         Settings settings) {
        return new HanLPAnalyzerProvider(indexSettings, env, name, settings, HanLPType.STANDARD);
    }

    public static HanLPAnalyzerProvider getHanLPIndexAnalyzerProvider(IndexSettings indexSettings, Environment env, String name,
                                                                      Settings settings) {
        return new HanLPAnalyzerProvider(indexSettings, env, name, settings, HanLPType.INDEX);
    }

    public static HanLPAnalyzerProvider getHanLPNLPAnalyzerProvider(IndexSettings indexSettings, Environment env, String name,
                                                                    Settings settings) {
        return new HanLPAnalyzerProvider(indexSettings, env, name, settings, HanLPType.NLP);
    }

    public static HanLPAnalyzerProvider getHanLPCRFAnalyzerProvider(IndexSettings indexSettings, Environment env, String name,
                                                                    Settings settings) {
        return new HanLPAnalyzerProvider(indexSettings, env, name, settings, HanLPType.CRF);
    }

    public static HanLPAnalyzerProvider getHanLPNShortAnalyzerProvider(IndexSettings indexSettings, Environment env, String name,
                                                                       Settings settings) {
        return new HanLPAnalyzerProvider(indexSettings, env, name, settings, HanLPType.N_SHORT);
    }

    public static HanLPAnalyzerProvider getHanLPDijkstraAnalyzerProvider(IndexSettings indexSettings, Environment env, String name,
                                                                         Settings settings) {
        return new HanLPAnalyzerProvider(indexSettings, env, name, settings, HanLPType.DIJKSTRA);
    }

    public static HanLPAnalyzerProvider getHanLPSpeedAnalyzerProvider(IndexSettings indexSettings, Environment env, String name,
                                                                      Settings settings) {
        return new HanLPAnalyzerProvider(indexSettings, env, name, settings, HanLPType.SPEED);
    }

    @Override
    public Analyzer get() {
        return this.analyzer;
    }
}
