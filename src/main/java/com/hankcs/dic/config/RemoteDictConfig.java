package com.hankcs.dic.config;

import com.hankcs.dic.Dictionary;
import com.hankcs.help.ESPluginLoggerFactory;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.core.internal.io.IOUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @project: elasticsearch-analysis-hanlp
 * @description: 远程词典配置
 * @author: Kenn
 * @create: 2018-12-18 15:23
 */
public class RemoteDictConfig {

    /**
     * 远程词典配置实例
     */
    private static RemoteDictConfig singleton;

    private static final Logger logger = ESPluginLoggerFactory.getLogger(RemoteDictConfig.class.getName());

    private static final String REMOTE_EXT_DICT = "remote_ext_dict";

    private static final String REMOTE_EXT_STOP = "remote_ext_stopwords";

    private Properties props;

    private String configFile;

    private RemoteDictConfig(String configFile) {
        this.configFile = configFile;
        this.props = new Properties();
        loadConfig();
    }

    public static synchronized RemoteDictConfig initial(String configFile) {
        if (singleton == null) {
            synchronized (Dictionary.class) {
                if (singleton == null) {
                    singleton = new RemoteDictConfig(configFile);
                }
            }
        }
        return singleton;
    }

    public boolean loadConfig() {
        InputStream input = null;
        try {
            logger.info("try load remote hanlp config from {}", configFile);
            input = new FileInputStream(configFile);
            props.loadFromXML(input);
        } catch (FileNotFoundException e) {
            logger.error("remote hanlp config isn't exist", e);
            return false;
        } catch (Exception e) {
            logger.error("can not load remote hanlp config", e);
            return false;
        } finally {
            try {
                IOUtils.close(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public List<String> getRemoteExtDictionarys() {
        return getRemoteExtFiles(REMOTE_EXT_DICT);
    }

    public List<String> getRemoteExtStopWordDictionarys() {
        return getRemoteExtFiles(REMOTE_EXT_STOP);
    }

    private List<String> getRemoteExtFiles(String key) {
        List<String> remoteExtFiles = new ArrayList<String>(2);
        String remoteExtStopWordDictCfg = getProperty(key);
        if (remoteExtStopWordDictCfg != null) {

            String[] filePaths = remoteExtStopWordDictCfg.split(";");
            for (String filePath : filePaths) {
                if (filePath != null && !"".equals(filePath.trim())) {
                    remoteExtFiles.add(filePath);

                }
            }
        }
        return remoteExtFiles;
    }

    private String getProperty(String key) {
        if (props != null) {
            return props.getProperty(key);
        }
        return null;
    }

    /**
     * 获取远程词典配置实例
     *
     * @return Dictionary 单例对象
     */
    public static RemoteDictConfig getSingleton() {
        if (singleton == null) {
            throw new IllegalStateException("远程词典配置尚未初始化，请先调用initial方法");
        }
        return singleton;
    }
}
