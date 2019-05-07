/*
 * <summary></summary>
 * <author>hankcs</author>
 * <email>me@hankcs.com</email>
 * <create-date>2015/10/6 18:51</create-date>
 *
 * <copyright file="SegmentWrapper.java">
 * Copyright (c) 2003-2015, hankcs. All Right Reserved, http://www.hankcs.com/
 * </copyright>
 */
package com.hankcs.lucene;

import com.hankcs.cfg.Configuration;
import com.hankcs.hanlp.dictionary.other.CharTable;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;

import java.io.Reader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;
import java.util.Scanner;

/**
 * @author hankcs
 *
 * 根据hankcs的代码稍作了一下修改
 */
public class SegmentWrapper {

    Scanner scanner;

    Segment segment;
    /**
     * 因为next是单个term出去的，所以在这里做一个记录
     */
    Term[] termArray;
    /**
     * termArray下标
     */
    int index;
    /**
     * term的偏移量，由于wrapper是按行读取的，必须对term.offset做一个校正
     */
    int offset;

    Configuration configuration;

    public SegmentWrapper(Reader reader, Segment segment, Configuration configuration) {
        scanner = createScanner(reader);
        this.segment = segment;
        this.configuration = configuration;
    }

    /**
     * 重置分词器
     *
     * @param reader
     */
    public void reset(Reader reader) {
        scanner = createScanner(reader);
        termArray = null;
        index = 0;
        offset = 0;
    }

    public Term next() {
        if (termArray != null && index < termArray.length) {
            return termArray[index++];
        }
        if (!scanner.hasNext()) {
            return null;
        }
        String line = scanner.next();
        while (isBlank(line)) {
            offset += line.length() + 1;
            if (scanner.hasNext()) {
                line = scanner.next();
            } else {
                return null;
            }
        }

        final String lineNeedSeg = line;
        List<Term> termList = AccessController.doPrivileged((PrivilegedAction<List<Term>>)() -> {
            char[] text = lineNeedSeg.toCharArray();
            if (configuration.isEnableNormalization()) {
                AccessController.doPrivileged((PrivilegedAction) () -> {
                    CharTable.normalization(text);
                    return null;
                });
            }
            return segment.seg(text);
        });
        if (termList.size() == 0) {
            return null;
        }
        termArray = termList.toArray(new Term[0]);

        for (Term term: termArray) {
            term.offset = term.offset + offset;
        }
        offset += line.length();
        index = 0;
        return termArray[index++];
    }

    /**
     * 判断字符串是否为空（null和空格）
     *
     * @param cs
     * @return
     */
    private static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static Scanner createScanner(Reader reader) {
        return new Scanner(reader).useDelimiter("\n");
    }
}
