package com.hankcs.dic;

import com.hankcs.hanlp.utility.Predefine;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.common.io.PathUtils;
import org.elasticsearch.common.logging.ESLoggerFactory;
import org.elasticsearch.plugin.analysis.hanlp.AnalysisHanLPPlugin;

import java.io.File;
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


    private final String CONFIG_FILE_NAME = "hanlp.properties";

    private static final Logger logger = ESLoggerFactory.getLogger(Dictionary.class);

    private static ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);

    private Dictionary() {
        Predefine.HANLP_PROPERTIES_PATH = getConfigInPluginDir().resolve(CONFIG_FILE_NAME).toString();
        logger.debug("hanlp properties path: {}", Predefine.HANLP_PROPERTIES_PATH);
    }

    public static synchronized Dictionary initial() {
        if (singleton == null) {
            synchronized (Dictionary.class) {
                if (singleton == null) {
                    singleton = new Dictionary();
                    pool.scheduleAtFixedRate(new Monitor(), 10, 60, TimeUnit.SECONDS);
                    return singleton;
                }
            }
        }
        return singleton;
    }

    public Path getConfigInPluginDir() {
        return PathUtils.get(new File(AnalysisHanLPPlugin.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent(), new String[]{"config"}).toAbsolutePath();
    }
}
