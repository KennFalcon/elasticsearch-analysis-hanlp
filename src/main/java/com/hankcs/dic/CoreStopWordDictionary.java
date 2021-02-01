package com.hankcs.dic;

import com.hankcs.hanlp.HanLP.Config;
import com.hankcs.hanlp.corpus.io.ByteArray;
import com.hankcs.hanlp.corpus.io.IOUtil;
import com.hankcs.hanlp.dictionary.stopword.Filter;
import com.hankcs.hanlp.dictionary.stopword.StopWordDictionary;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.utility.Predefine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.elasticsearch.core.internal.io.IOUtils;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;

/**
 * Project: elasticsearch-analysis-hanlp
 * Description: 对原停用词过滤做了修改，删除了对词性过滤，只对停用词词典中词进行过滤
 * Author: Hankcs
 * Editor Kenn
 * Create: 2019-03-18 19:16
 */
public class CoreStopWordDictionary {

    private static final Logger logger = LogManager.getLogger(CoreStopWordDictionary.class);

    private static final StopWordDictionary dictionary;

    private static final Filter FILTER = term -> {
        // 除掉停用词
        String nature = term.nature != null ? term.nature.toString() : "空";
        char firstChar = nature.charAt(0);
        if (firstChar == 'w') {
            return false;
        } else {
            return !CoreStopWordDictionary.contains(term.word);
        }
    };

    public CoreStopWordDictionary() {
    }

    private static boolean contains(String key) {
        return dictionary.contains(key);
    }

    private static boolean shouldInclude(Term term) {
        return FILTER.shouldInclude(term);
    }

    public static boolean shouldRemove(Term term) {
        return !shouldInclude(term);
    }

    public static void add(String stopWord) {
        dictionary.add(stopWord);
    }

    public static void remove(String stopWord) {
        dictionary.remove(stopWord);
    }

    public static void apply(List<Term> termList) {
        termList.removeIf(CoreStopWordDictionary::shouldRemove);
    }

    static {
        ByteArray byteArray = isNeedUpdate() ? null :
                AccessController.doPrivileged((PrivilegedAction<ByteArray>) () ->
                        ByteArray.createByteArray(Config.CoreStopWordDictionaryPath + Predefine.BIN_EXT));
        if (byteArray == null) {
            try {
                dictionary = AccessController.doPrivileged((PrivilegedAction<StopWordDictionary>) () -> {
                    try {
                        return new StopWordDictionary(Config.CoreStopWordDictionaryPath);
                    } catch (IOException e) {
                        logger.error(() -> new ParameterizedMessage("load stop word dictionary from [{}] error",
                                Config.CoreStopWordDictionaryPath), e);
                        return null;
                    }
                });
                if (dictionary != null) {
                    AccessController.doPrivileged((PrivilegedAction<Boolean>) CoreStopWordDictionary::save);
                }
            } catch (Exception e) {
                logger.error(() ->
                        new ParameterizedMessage("load stop word dictionary from [{}] error", Config.CoreStopWordDictionaryPath), e);
                throw new RuntimeException("load stop word dictionary from [" + Config.CoreStopWordDictionaryPath + "] error");
            }
        } else {
            dictionary = new StopWordDictionary();
            dictionary.load(byteArray);
        }
    }

    private static boolean save() {
        DataOutputStream out = null;
        try {
            out = new DataOutputStream(new BufferedOutputStream(IOUtil.newOutputStream(Config.CoreStopWordDictionaryPath + ".bin")));
            dictionary.save(out);
            return true;
        } catch (Exception e) {
            logger.error(() ->
                    new ParameterizedMessage("can not save stop word dictionary to [{}] error", Config.CoreStopWordDictionaryPath), e);
            return false;
        } finally {
            IOUtils.closeWhileHandlingException(out);
        }
    }

    private static boolean isNeedUpdate() {
        File binFile = new File(Config.CoreStopWordDictionaryPath + Predefine.BIN_EXT);
        if (!AccessController.doPrivileged((PrivilegedAction<Boolean>) binFile::exists)) {
            return true;
        }
        File txtFile = new File(Config.CoreStopWordDictionaryPath);
        if (!AccessController.doPrivileged((PrivilegedAction<Boolean>) txtFile::exists)) {
            logger.error("can not find stop word dictionary from [{}]", Config.CoreStopWordDictionaryPath);
            throw new IllegalArgumentException("can not find stop word dictionary from [" + Config.CoreStopWordDictionaryPath + "]");
        }
        long binLastModified = binFile.lastModified();
        long txtLastModified = txtFile.lastModified();
        if (txtLastModified >= binLastModified) {
            AccessController.doPrivileged((PrivilegedAction<Boolean>) binFile::delete);
            return true;
        }
        return false;
    }
}

