package com.hankcs.dic.cache;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hankcs.cfg.Configuration;
import com.hankcs.dic.DictionaryFile;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.common.logging.ESLoggerFactory;
import org.elasticsearch.plugin.analysis.hanlp.AnalysisHanLPPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Kenn
 * Date: 2018/2/8
 * Time: 17:33
 * Project: elasticsearch-analysis-hanlp
 * Description:
 */
public class DictionaryFileCache {

    private static final Logger logger = ESLoggerFactory.getLogger(DictionaryFileCache.class);

    private static Path cachePath = null;

    private static final String DICTIONAY_FILE_CACHE_RECORD_FILE = "hanlp.cache";

    private static List<DictionaryFile> customDictionaryFileList = new ArrayList<>();

    public static synchronized void configCachePath(Configuration configuration) {
        cachePath = configuration.getEnvironment().pluginsFile().resolve(AnalysisHanLPPlugin.PLUGIN_NAME).resolve(DICTIONAY_FILE_CACHE_RECORD_FILE);
    }

    public static void loadCache() {
        File file = cachePath.toFile();
        if (!file.exists()) {
            return;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, DictionaryFile.class);
        List<DictionaryFile> dictionaryFiles = AccessController.doPrivileged((PrivilegedAction<List<DictionaryFile>>) () -> {
            try {
                return objectMapper.readValue(file, javaType);
            } catch (IOException e) {
                logger.debug("can not load custom dictionary cache file", e);
                return new ArrayList<>();
            }
        });
        setCustomDictionaryFileList(dictionaryFiles);
    }

    public static void writeCache() {
        AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.writeValue(cachePath.toFile(), customDictionaryFileList);
            } catch (IOException e) {
                logger.debug("can not write custom dictionary cache file", e);
            }
            return null;
        });
    }

    public static List<DictionaryFile> getCustomDictionaryFileList() {
        return customDictionaryFileList;
    }

    public static synchronized void setCustomDictionaryFileList(List<DictionaryFile> customDictionaryFileList) {
        DictionaryFileCache.customDictionaryFileList = customDictionaryFileList;
    }
}
