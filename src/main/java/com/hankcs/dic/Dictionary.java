package com.hankcs.dic;

import com.hankcs.cfg.Configuration;
import com.hankcs.dic.cache.DictionaryFileCache;
import com.hankcs.dic.config.RemoteDictConfig;
import com.hankcs.hanlp.utility.Predefine;
import com.hankcs.help.ESPluginLoggerFactory;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.plugin.analysis.hanlp.AnalysisHanLPPlugin;

import java.nio.file.Path;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Project: elasticsearch-analysis-hanlp
 * Description: 词典类
 * Author: Kenn
 * Create: 2018-12-14 15:10
 */
public class Dictionary {
    /**
     * 词典单子实例
     */
    private static Dictionary singleton;
    /**
     * Hanlp配置文件名
     */
    private static final String CONFIG_FILE_NAME = "hanlp.properties";
    /**
     * Hanlp远程词典配置文件名
     */
    private static final String REMOTE_CONFIG_FILE_NAME = "hanlp-remote.xml";

    private static final Logger logger = ESPluginLoggerFactory.getLogger(Dictionary.class.getName());

    private static final ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);

    private Dictionary(Configuration configuration) {
        Path configDir = configuration.getEnvironment().configFile().resolve(AnalysisHanLPPlugin.PLUGIN_NAME);
        Predefine.HANLP_PROPERTIES_PATH = configDir.resolve(CONFIG_FILE_NAME).toString();
        logger.debug("hanlp properties path: {}", Predefine.HANLP_PROPERTIES_PATH);
        DictionaryFileCache.configCachePath(configuration);
        DictionaryFileCache.loadCache();
        RemoteDictConfig.initial(configDir.resolve(REMOTE_CONFIG_FILE_NAME).toString());
    }

    public static synchronized void initial(Configuration configuration) {
        if (singleton == null) {
            synchronized (Dictionary.class) {
                if (singleton == null) {
                    singleton = new Dictionary(configuration);
                    pool.scheduleAtFixedRate(new ExtMonitor(), 10, 60, TimeUnit.SECONDS);
                    if (configuration.isEnableRemoteDict()) {
                        for (String location : RemoteDictConfig.getSingleton().getRemoteExtDictionarys()) {
                            pool.scheduleAtFixedRate(new RemoteMonitor(location, "custom"), 10, 60, TimeUnit.SECONDS);
                        }

                        for (String location : RemoteDictConfig.getSingleton().getRemoteExtStopWordDictionarys()) {
                            pool.scheduleAtFixedRate(new RemoteMonitor(location, "stop"), 10, 60, TimeUnit.SECONDS);
                        }
                    }
                }
            }
        }
    }
}
