package com.hankcs.dic;

import com.hankcs.cfg.Configuration;
import com.hankcs.dic.cache.DictionaryFileCache;
import com.hankcs.dic.config.RemoteDictConfig;
import org.elasticsearch.plugin.analysis.hanlp.AnalysisHanLPPlugin;

import java.nio.file.Path;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Project: elasticsearch-analysis-hanlp
 * Description: 词典类
 * Author: Kenn
 * Create: 2018-12-14 15:10
 */
public class Dictionary {
    /**
     * 词典实例
     */
    private static Dictionary singleton;
    /**
     * Hanlp远程词典配置文件名
     */
    private static final String REMOTE_CONFIG_FILE_NAME = "hanlp-remote.xml";

    private static final ScheduledExecutorService pool = Executors.newScheduledThreadPool(1, new ThreadFactory() {

        private final AtomicInteger counter = new AtomicInteger();

        @Override
        public Thread newThread(Runnable r) {
            String threadName = "remote-dict-monitor-" + counter.getAndIncrement();
            return new Thread(r, threadName);
        }
    });

    private final Configuration configuration;

    private Dictionary(Configuration configuration) {
        this.configuration = configuration;
    }

    private void setUp() {
        Path configDir = configuration.getEnvironment().configFile().resolve(AnalysisHanLPPlugin.PLUGIN_NAME);
        DictionaryFileCache.configCachePath(configuration);
        DictionaryFileCache.loadCache();
        RemoteDictConfig.initial(configDir.resolve(REMOTE_CONFIG_FILE_NAME).toString());
    }

    public static synchronized void initial(Configuration configuration) {
        if (singleton == null) {
            synchronized (Dictionary.class) {
                if (singleton == null) {
                    singleton = new Dictionary(configuration);
                    singleton.setUp();
                    pool.scheduleAtFixedRate(new ExtMonitor(), 10, 60, TimeUnit.SECONDS);
                    if (configuration.isEnableRemoteDict()) {
                        for (String location : RemoteDictConfig.getSingleton().getRemoteExtDictionaries()) {
                            pool.scheduleAtFixedRate(new RemoteMonitor(location, "custom"), 10, 60, TimeUnit.SECONDS);
                        }

                        for (String location : RemoteDictConfig.getSingleton().getRemoteExtStopWordDictionaries()) {
                            pool.scheduleAtFixedRate(new RemoteMonitor(location, "stop"), 10, 60, TimeUnit.SECONDS);
                        }
                    }
                }
            }
        }
    }
}
