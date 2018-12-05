package com.hankcs.dic;

import com.hankcs.cfg.Configuration;
import com.hankcs.dic.cache.DictionaryFileCache;
import com.hankcs.hanlp.utility.Predefine;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.plugin.analysis.hanlp.AnalysisHanLPPlugin;

import java.nio.file.Path;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Kenn
 * Date: 2018/2/8
 * Time: 15:56
 * Project: elasticsearch-analysis-hanlp
 * Description:
 */
public class Dictionary {
    /**
     * 词典单子实例
     */
    private static Dictionary singleton;
    /**
     * 配置文件目录
     */
    private Path configDir;

    private final String CONFIG_FILE_NAME = "hanlp.properties";

    private static final Logger logger = Loggers.getLogger(Dictionary.class);

    private static ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);

    private Dictionary(Configuration configuration) {
        configDir = configuration.getEnvironment().configFile().resolve(AnalysisHanLPPlugin.PLUGIN_NAME);
        Predefine.HANLP_PROPERTIES_PATH = configDir.resolve(CONFIG_FILE_NAME).toString();
        logger.debug("hanlp properties path: {}", Predefine.HANLP_PROPERTIES_PATH);
        DictionaryFileCache.configCachePath(configuration);
        DictionaryFileCache.loadCache();
    }

    public static synchronized Dictionary initial(Configuration configuration) {
        if (singleton == null) {
            synchronized (Dictionary.class) {
                if (singleton == null) {
                    singleton = new Dictionary(configuration);
                    pool.scheduleAtFixedRate(new Monitor(), 10, 60, TimeUnit.SECONDS);
                    return singleton;
                }
            }
        }
        return singleton;
    }
}
