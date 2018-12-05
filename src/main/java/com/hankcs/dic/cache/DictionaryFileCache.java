package com.hankcs.dic.cache;

import com.hankcs.cfg.Configuration;
import com.hankcs.dic.DictionaryFile;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.core.internal.io.IOUtils;
import org.elasticsearch.plugin.analysis.hanlp.AnalysisHanLPPlugin;

import java.io.*;
import java.nio.file.Path;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
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

    private static final Logger logger = Loggers.getLogger(DictionaryFileCache.class);

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
        List<DictionaryFile> dictionaryFiles = AccessController.doPrivileged((PrivilegedAction<List<DictionaryFile>>) () -> {
            List<DictionaryFile> dictionaryFileList = new ArrayList<>();
            DataInputStream in = null;
            try {
                in = new DataInputStream(new FileInputStream(file));
                int size = in.readInt();
                for (int i = 0; i < size; i++) {
                    DictionaryFile dictionaryFile = new DictionaryFile();
                    dictionaryFile.read(in);
                    dictionaryFileList.add(dictionaryFile);
                }
            } catch (IOException e) {
                logger.debug("can not load custom dictionary cache file", e);
            } finally {
                try {
                    IOUtils.close(in);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return dictionaryFileList;
        });
        setCustomDictionaryFileList(dictionaryFiles);
    }

    public static void writeCache() {
        AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
            DataOutputStream out = null;
            try {
                logger.info("begin write down hanlp custom dictionary file cache, file path: {}, custom dictionary file list: {}", cachePath.toFile().getAbsolutePath(), Arrays.toString(customDictionaryFileList.toArray()));
                out = new DataOutputStream(new FileOutputStream(cachePath.toFile()));
                out.writeInt(customDictionaryFileList.size());
                for (DictionaryFile dictionaryFile : customDictionaryFileList) {
                    dictionaryFile.write(out);
                }
                logger.info("write down hanlp custom dictionary file cache successfully");
            } catch (IOException e) {
                logger.debug("can not write down hanlp custom dictionary file cache", e);
            } finally {
                try {
                    IOUtils.close(out);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
