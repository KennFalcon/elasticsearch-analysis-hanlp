package com.hankcs.dic.cache;

import com.hankcs.dic.DictionaryFile;

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

    private static List<DictionaryFile> customDictionaryFileList = new ArrayList<>();

    public static List<DictionaryFile> getCustomDictionaryFileList() {
        return customDictionaryFileList;
    }

    public static synchronized void setCustomDictionaryFileList(List<DictionaryFile> customDictionaryFileList) {
        DictionaryFileCache.customDictionaryFileList = customDictionaryFileList;
    }
}
