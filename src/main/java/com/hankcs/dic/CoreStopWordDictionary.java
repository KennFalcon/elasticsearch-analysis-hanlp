package com.hankcs.dic;

import com.hankcs.hanlp.HanLP.Config;
import com.hankcs.hanlp.corpus.io.ByteArray;
import com.hankcs.hanlp.corpus.io.IOUtil;
import com.hankcs.hanlp.dictionary.stopword.Filter;
import com.hankcs.hanlp.dictionary.stopword.StopWordDictionary;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.utility.Predefine;
import com.hankcs.hanlp.utility.TextUtility;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.util.List;

/**
 * Project: elasticsearch-analysis-hanlp
 * Description: 对原停用词过滤做了修改，删除了对词性过滤，只对停用词词典中词进行过滤
 * Author: Hankcs
 * Editor Kenn
 * Create: 2019-03-18 19:16
 */
public class CoreStopWordDictionary {

    private static final StopWordDictionary dictionary;

    private static final Filter FILTER = term -> {
        // 除掉停用词
        String nature = term.nature != null ? term.nature.toString() : "空";
        char firstChar = nature.charAt(0);
        if (firstChar == 'w') {
            return false;
        } else {
            return !com.hankcs.hanlp.dictionary.stopword.CoreStopWordDictionary.contains(term.word);
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
        ByteArray byteArray = ByteArray.createByteArray(Config.CoreStopWordDictionaryPath + ".bin");
        if (byteArray == null) {
            try {
                dictionary = new StopWordDictionary(Config.CoreStopWordDictionaryPath);
                DataOutputStream out = new DataOutputStream(
                        new BufferedOutputStream(IOUtil.newOutputStream(Config.CoreStopWordDictionaryPath + ".bin")));
                dictionary.save(out);
                out.close();
            } catch (Exception var2) {
                Predefine.logger.severe(
                        "载入停用词词典" + Config.CoreStopWordDictionaryPath + "失败" + TextUtility.exceptionToString(var2));
                throw new RuntimeException("载入停用词词典" + Config.CoreStopWordDictionaryPath + "失败");
            }
        } else {
            dictionary = new StopWordDictionary();
            dictionary.load(byteArray);
        }
    }
}

