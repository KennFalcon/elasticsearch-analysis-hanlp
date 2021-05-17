package com.hankcs.dic;

import com.hankcs.dic.cache.DictionaryFileCache;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.utility.Predefine;
import com.hankcs.utility.CustomDictionaryUtility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.SpecialPermission;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * Project: elasticsearch-analysis-hanlp
 * Description: 自定义词典监控线程
 * Author: Kenn
 * Create: 2018-12-14 15:10
 */
public class ExtMonitor implements Runnable {

    private static final Logger logger = LogManager.getLogger(ExtMonitor.class);

    ExtMonitor() {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new SpecialPermission());
        }
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void run() {
        List<DictionaryFile> originalDictionaryFileList = DictionaryFileCache.getCustomDictionaryFileList();
        logger.debug("hanlp original custom dictionary: {}", Arrays.toString(originalDictionaryFileList.toArray()));
        reloadProperty();
        List<DictionaryFile> currentDictionaryFileList = getCurrentDictionaryFileList(HanLP.Config.CustomDictionaryPath);
        logger.debug("hanlp current custom dictionary: {}", Arrays.toString(currentDictionaryFileList.toArray()));
        boolean isModified = false;
        for (DictionaryFile currentDictionaryFile : currentDictionaryFileList) {
            if (!originalDictionaryFileList.contains(currentDictionaryFile)) {
                isModified = true;
                break;
            }
        }
        if (isModified) {
            logger.info("reloading hanlp custom dictionary");
            try {
                AccessController.doPrivileged((PrivilegedAction) CustomDictionaryUtility::reload);
            } catch (Exception e) {
                logger.error("can not reload hanlp custom dictionary", e);
            }
            DictionaryFileCache.setCustomDictionaryFileList(currentDictionaryFileList);
            DictionaryFileCache.writeCache();
            logger.info("finish reload hanlp custom dictionary");
        } else {
            logger.info("hanlp custom dictionary isn't modified, so no need reload");
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void reloadProperty() {
        Properties p = new Properties();
        try {
            ClassLoader loader = AccessController.doPrivileged(
                    (PrivilegedAction<ClassLoader>) () -> Thread.currentThread().getContextClassLoader()
            );
            if (loader == null) {
                loader = HanLP.Config.class.getClassLoader();
            }
            p.load(
                    new InputStreamReader(Predefine.HANLP_PROPERTIES_PATH == null
                            ? Objects.requireNonNull(loader.getResourceAsStream("hanlp.properties"))
                            : new FileInputStream(Predefine.HANLP_PROPERTIES_PATH), StandardCharsets.UTF_8)
            );
            String root = p.getProperty("root", "").replaceAll("\\\\", "/");
            if (root.length() > 0 && !root.endsWith("/")) {
                root += "/";
            }
            String[] pathArray = p.getProperty("CustomDictionaryPath", "data/dictionary/custom/CustomDictionary.txt").split(";");
            String prePath = root;
            for (int i = 0; i < pathArray.length; ++i) {
                if (pathArray[i].startsWith(" ")) {
                    pathArray[i] = prePath + pathArray[i].trim();
                } else {
                    pathArray[i] = root + pathArray[i];
                    int lastSplash = pathArray[i].lastIndexOf('/');
                    if (lastSplash != -1) {
                        prePath = pathArray[i].substring(0, lastSplash + 1);
                    }
                }
            }
            AccessController.doPrivileged((PrivilegedAction) () -> HanLP.Config.CustomDictionaryPath = pathArray);
        } catch (Exception e) {
            logger.error("can not find hanlp.properties", e);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private List<DictionaryFile> getCurrentDictionaryFileList(String[] customDictionaryPaths) {
        List<DictionaryFile> dictionaryFileList = new ArrayList<>();
        for (String customDictionaryPath : customDictionaryPaths) {
            String[] customDictionaryPathTuple = customDictionaryPath.split(" ");
            String path = customDictionaryPathTuple[0].trim();
            logger.debug("hanlp custom path: {}", path);
            File file = new File(path);
            AccessController.doPrivileged((PrivilegedAction) () -> {
                if (file.exists()) {
                    if (customDictionaryPathTuple.length > 1) {
                        if (customDictionaryPathTuple[1] == null || customDictionaryPathTuple[1].length() == 0) {
                            dictionaryFileList.add(new DictionaryFile(path, file.lastModified()));
                        } else {
                            dictionaryFileList.add(new DictionaryFile(path, customDictionaryPathTuple[1].trim(), file.lastModified()));
                        }
                    } else {
                        dictionaryFileList.add(new DictionaryFile(path, file.lastModified()));
                    }
                }
                return null;
            });
        }
        return dictionaryFileList;
    }
}
